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
    public static Move getBestMove(Board board, int depth, boolean isWhiteTurn) {
        double eval;
        ArrayList<Move> pickableMoves = new ArrayList<>();
        double maxEval = isWhiteTurn ? -100000 : 100000;
        Move bestMove = null;
        // run minimax on all possible moves, execute best
        ArrayList<Move> allMoves = getAllPossibleMoves(board, isWhiteTurn);
        for (Move m : allMoves) {
            Board newB = new Board(board);
            newB.move(m.getSrc(), m.getDest());
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
        return Math.random() > .2 ? bestMove : randomGoodMove;
    }

    // MINIMAX ALGORITHM
    // how to track the move tree
    public static double minimax(Board board, int depth, boolean maximizingPlayer, double alpha, double beta) {
        if (depth == 0) {
            // return heuristic value of board
            // how to return a move instead of an eval?
            if (numNodesProcessed % 1000000 == 0) {
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
                newBoard.move(m.getSrc(), m.getDest());
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
                newBoard.move(m.getSrc(), m.getDest());
                // need to associate m with value
                value = Math.min(value, minimax(newBoard, depth-1, true, alpha, beta));
                beta = Math.min(beta, value);
                if (beta <= alpha) break;
            }
            return value;
        }
    }


    // get legal moves for all pieces on the board
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
            TODO: Detect doubled pawns (-.2)
            TODO: Checkmate patterns
            TODO: Theoretical openings (advanced)
            TODO: Endgames
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

}
