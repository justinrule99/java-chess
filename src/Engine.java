import java.util.ArrayList;

/*
    Chess engine with customizable search depth
    Uses Minimax algorithm with alpha-beta pruning for efficiency
 */
public class Engine {

    public Engine() {

    }

    public static int numNodesProcessed = 0;


    // sometimes doesn't play a move?
    // sometimes plays slightly worse move for rng
    public static Move getBestMove(Board board, int depth, boolean isWhiteTurn) {
        double eval;
        ArrayList<Move> pickableMoves = new ArrayList<>();
        double maxEval = isWhiteTurn ? -100000 : 100000;
        Move bestMove = null;
        // run minimax on all possible moves, execute best
        ArrayList<Move> allMoves = getAllPossibleMoves(board, isWhiteTurn);
        ArrayList<Move> allCheckLegalMoves = new ArrayList<>(allMoves);

        for (Move m : allMoves) {
            Board newB = new Board(board);
            if (!newB.move(m)) {
                allCheckLegalMoves.remove(m);
            }
        }

        // This is checkmate
        if (allCheckLegalMoves.size() == 0) {
            System.out.println("ERROR: NO LEGAL MOVES");
            if (depth == 1) {
                if (isWhiteTurn) {
                    board.blackWins = true;
                } else {
                    board.whiteWins = true;
                }
            } else {
                return null;
            }

            // might find a checkmate in analysis but not actually be there
        }

        for (Move m : allCheckLegalMoves) {
            // should call inCheck() here instead of in Board
            Board newB = new Board(board);
            newB.move(m);

            // error if none in allCheckLegalMoves

            eval = Engine.minimax(newB, depth, !isWhiteTurn,-1000000, 1000000);
            if (isWhiteTurn) {
                if (eval > maxEval) {
                    pickableMoves.add(m);
                    bestMove = m;
                    maxEval = eval;
                } else if (eval+.09 > maxEval) {
                    pickableMoves.add(m);
                }
            } else {
                if (eval <= maxEval) {
                    pickableMoves.add(m);
                    bestMove = m;
                    maxEval = eval;
                } else if (eval-.1 <= maxEval) {
                    pickableMoves.add(m);
                }
            }
        }
        // 50/50 chance of picking a pickableMove or bestMove
        Move randomGoodMove = pickableMoves.get((int)Math.floor(Math.random()*pickableMoves.size()));
//        return bestMove;
        return Math.random() > .3 ? bestMove : randomGoodMove;
    }

    // MINIMAX ALGORITHM
    // how to track the move tree
    public static double minimax(Board board, int depth, boolean maximizingPlayer, double alpha, double beta) {
        if (depth == 0) {
            // return heuristic value of board
            // how to return a move instead of an eval?
            if (numNodesProcessed % 100000 == 0) {
                System.out.println("Analyzed "+numNodesProcessed+" positions..");

            }
            // always only one move
            return evaluate(board);
        }

        double value;

        if (maximizingPlayer) {
            // white
            ArrayList<Move> whiteMoves = getAllPossibleMoves(board, true);
            value = -10000;
            for (Move m : whiteMoves) {
                numNodesProcessed++;
                Board newBoard = new Board(board);
                newBoard.move(m);
                value = Math.max(value, minimax(newBoard, depth-1, false, alpha, beta));
                alpha = Math.max(alpha, value);
                if (beta <= alpha) break;
            }
            return value;
        } else {
            // black
            ArrayList<Move> blackMoves = getAllPossibleMoves(board, false);
            value = 10000;
            for (Move m : blackMoves) {
                numNodesProcessed++;
                Board newBoard = new Board(board);
                newBoard.move(m);
                // need to associate m with value
                value = Math.min(value, minimax(newBoard, depth-1, true, alpha, beta));
                beta = Math.min(beta, value);
                if (beta <= alpha) break;
            }
            return value;
        }
    }


    // get legal moves for all pieces on the board
    // optimize to use piece position, not entire board
    public static ArrayList<Move> getAllPossibleMoves(Board board, boolean whiteTurn) {
        ArrayList<Move> moves = new ArrayList<>();
        Piece curPiece;

            // get all legal moves for each white piece
            // i: file, j: rank
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                curPiece = board.getSquare(i,j).getCurrentPiece();

                if (curPiece != null && curPiece.isWhite() == whiteTurn) {
                    moves.addAll(board.getLegalMoves(board.rankAndFileToCode(j,i)));
                }
            }
        }

        return moves;
    }

    public static double evaluate(Board board) {
        // should just have an array of pieces with self contained position (faster)
        // we can make this WAY faster with each piece's location (not reliant on board)
        double eval = 0.0;

        /*
            TODO: Break paths with no chances
            TODO: Evaluate mobility (+.05 per move)
            TODO: Improve board representation (for efficiency)
            TODO: Encourage development
            TODO: Eval attacking chances
            TODO: Eval King safety
            TODO: Detect doubled pawns (-.2)
            TODO: Checkmate patterns
            TODO: Theoretical openings (advanced)
            TODO: Endgame tables
         */

        // file, rank
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                Piece p = board.getSquare(i,j).getCurrentPiece();
                if (p != null) {
                    // only hits corners
                    double middleIshBonus = ((i == 3 || i == 6) || (j == 3 || j == 6)) ? .6 : .0;
                    double centerBonus = ((i == 4 || i == 5) && (j == 4 || j == 5)) ? 1 : .0;

                    if (p.isWhite()) {
                        eval += p.getValue();
                        eval += centerBonus + middleIshBonus;
                    } else {
                        eval -= p.getValue();
                        eval = eval - centerBonus - middleIshBonus;
                    }
                }
            }
        }


        // eval central control, king safety, future attacks?
            // central control: pieces + attacks in center, c-f, 3-6
        // ++ for pieces in center not under attack
        // -- if own king in center and unprotected


        // pos: white, neg: black
        return eval;
    }


    // testing a second heuristic based on claude shannon's 1949 paper
    // always evaluates in relation to white
    public static double evaluate2(Board board) {
        double eval = 0.0;
        int whQueen = 0;
        int whRook = 0;
        int whBishopKnight = 0;
        int whPawn = 0;
        // control of center, pos = advantage white
        double centerFight = 0.0;

        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                Piece p = board.getSquare(i,j).getCurrentPiece();
                if (p != null) {
                    int inc = p.isWhite() ? 1 : -1;
                    // times weight?
                    double centerInc = p.isWhite() ? .7 : -.6;
                    // do based on type
                    if (p.getValue() == 1) {
                        whPawn += inc;
                    } else if (p.getValue() == 3) {
                        whBishopKnight += inc;
                    } else if (p.getValue() == 5) {
                        whRook += inc;
                    } else if (p.getValue() == 9) {
                        whQueen += inc;
                    }

                    int file = p.getCurrentSquare().getFile();
                    int rank = p.getCurrentSquare().getRank();
                    if (file >= 3 && file <= 6 && rank >= 3 && rank <= 6 ) {
                        // need to reward 4 or 5 vs 3 or 6
                        centerFight += centerInc;
                    }
                }
            }
        }

        // calc mobility
//        int whMobility = getAllPossibleMoves(board, true).size();
//        int blMobility = getAllPossibleMoves(board, false).size();
        int whMobility = 0;
        int blMobility = 0;

        eval = 9*whQueen + 5*whRook + 3*whBishopKnight + whPawn + .1*(whMobility-blMobility) + centerFight;

        return eval;
    }
}
