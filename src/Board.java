import java.util.ArrayList;
import java.util.HashMap;

public class Board {
    // represents a standard chess board
    // keep track of eval to form hashtable

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_WHITE = "\u001B[37m";

    // probably unused
    public static HashMap<Integer, Board> gameStates = new HashMap<>();

    // faster to keep each piece with current location instead of all squares??
    private Piece[] whitePieces;
    private Piece[] blackPieces;

    // 2d array of squares: will have piece info
    private Square[][] squares;
    private int moveNumber;
    private boolean whiteToMove;
    public boolean whiteWins = false;
    public boolean blackWins = false;
    public String whiteKing = "e1";
    public String blackKing = "e8";
    private ArrayList<Move> moveHistory;
    public boolean whiteCanCastleQ = true;
    public boolean whiteCanCastleK = true;
    public boolean blackCanCastleQ = true;
    public boolean blackCanCastleK = true;
    public double eval = 0.0;

    public Board(Board b, boolean switchTurn) {
        this(b);
        if (switchTurn) {
            whiteToMove = !b.whiteToMove;
        }
    }

    public static int copyContstructorCalls = 0;

    public Board(Board b) {
        copyContstructorCalls++;
        // implement a findKing(color) method for prev board
        // shallow copy
        squares = new Square[8][8];

        // [rank][file]
        // ranks bottom to top
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                // HACK: DECREMENET RANK AND FILE
                // still wrong sometimes?
                // this changes for everything, not just kings
                if (b.squares[i][j].rank > 8 || b.squares[i][j].file > 8) {
                    System.out.println("ERROR: rank or file was off");
                }
//                System.out.println(i+","+j+": "+b.squares[i][j]);
                Square s = new Square(b.squares[i][j].isWhite(), b.squares[i][j].getCurrentPiece(), b.squares[i][j].rank-1, b.squares[i][j].file-1);
                squares[i][j] = s;
            }
        }
        moveHistory = new ArrayList<>();
        // b move history is always empty
        // need to copy moveHistory
        this.moveHistory.addAll(b.moveHistory);

        whiteKing = b.whiteKing;
        blackKing = b.blackKing;

        moveNumber = b.moveNumber;
        whiteToMove = b.whiteToMove;
        whiteWins = b.whiteWins;
        blackWins = b.blackWins;
        whiteCanCastleQ = b.whiteCanCastleQ;
        whiteCanCastleK = b.whiteCanCastleK;
        blackCanCastleQ = b.blackCanCastleQ;
        blackCanCastleK = b.blackCanCastleK;
        eval = b.eval;
    }

    // initialize the game with pieces on starting squares
    public Board() {
        // squares [ranks][files]
        squares = new Square[8][8];
        // SQUARE, NOT PIECE
        boolean isWhite = true;
        moveNumber = 0;
        whiteToMove = true;
        moveHistory = new ArrayList<>();
        // ranks Bot to top
        // need so that squares[2][3] gives piece on d3
        // now gives piece on d5
        // squares[0][0] should give a1, instead gives a8
        for (int i = 0; i < 8; i++) {
            // files L->R
            for (int j = 0; j < 8; j++) {
                if (i == 1 || i == 6) {
                    squares[i][j] = new Square(isWhite, new Pawn(i == 1), i, j);
                } else if (i == 0 || i == 7) {
                    if (j == 0 || j == 7) {
                        squares[i][j] = new Square(isWhite, new Rook(i == 0), i,j);
                    } else if (j == 1 || j == 6) {
                        squares[i][j] = new Square(isWhite, new Knight(i == 0), i,j);
                    } else if (j == 2 || j ==5) {
                        squares[i][j] = new Square(isWhite, new Bishop(i == 0), i,j);
                    } else if (j == 3) {
                        squares[i][j] = new Square(isWhite, new Queen(i == 0), i,j);
                    } else {
                        Square e1 = new Square(isWhite, new King(i == 0) , i,j);
                        Square e8 = new Square(isWhite, new King(i == 0) , i,j);

                        if (i == 0) {
                            squares[i][j] = e1;
                        } else {
                            squares[i][j] = e8;
                        }
                    }
                } else {
                    squares[i][j] = new Square(isWhite, null, i,j);
                }
                isWhite = !isWhite;
            }
            isWhite = !isWhite;
        }

        // set current square on pieces
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8 ; j++) {
                Square curSquare = getSquare(i,j);
                Piece cur = curSquare.getCurrentPiece();
                if (cur != null) {
                    cur.setCurrentSquare(curSquare);
                }
            }
        }
    }

    public boolean isWhiteToMove() {
        return whiteToMove;
    }

    // buggy if you move out of turn
    public void handleCastle(boolean kingside) {
        // know legal move
//        System.out.println("is white moving: "+isWhiteToMove());
        int r = isWhiteToMove() ? 0 : 7;
        if (isWhiteToMove()) {
            whiteCanCastleQ = false;
            whiteCanCastleK = false;
        } else {
            blackCanCastleK = false;
            blackCanCastleQ = false;
        }
        Piece king = squares[r][4].getCurrentPiece();
        Piece rook = squares[r][kingside ? 7 : 0].getCurrentPiece();

        squares[r][4].setCurrentPiece(null);

        if (kingside) {
            squares[r][7].setCurrentPiece(null);
            squares[r][6].setCurrentPiece(king);
            squares[r][5].setCurrentPiece(rook);
        } else {
            squares[r][0].setCurrentPiece(null);
            squares[r][2].setCurrentPiece(king);
            squares[r][3].setCurrentPiece(rook);
        }
    }

    public boolean move(Move m) {
//        boolean moveSuccess = move(m.getSrc(), m.getDest(), false);
//        if (!moveSuccess) {
//            return move(m.getSrc(), m.getDest(), false);
//        }
//        return moveSuccess;
        return move(m.getSrc(), m.getDest(), false);
    }

    public static int timesEnteredMove = 0;
    public static int timesMoved = 0;

    // absolute garbage, no error checking
    // but we need it for speed and preventing accidental recursion
    public boolean forceMove(String source, String dest) {
        int srcFile = codeToFile(source);
        int srcRank = codeToRank(source);
        int destFile = codeToFile(dest);
        int destRank = codeToRank(dest);


        Piece movingPiece = squares[srcRank-1][srcFile-1].getCurrentPiece();

        squares[srcRank-1][srcFile-1].setCurrentPiece(null);
        whiteToMove = !whiteToMove;
        squares[destRank-1][destFile-1].setCurrentPiece(movingPiece);
        return true;
    }

    // if in check, move must resolve check
    // returns false if didn't move, true if did
    public boolean move(String source, String dest, boolean skipChecks) {
        int srcFile = codeToFile(source);
        int srcRank = codeToRank(source);
        int destFile = codeToFile(dest);
        int destRank = codeToRank(dest);

        // dont let you move a piece if its not your turn
        timesEnteredMove++;

        // with piece array: would be


        // indices always rank, file
        // illegal move if no piece on source
        Piece movingPiece = squares[srcRank-1][srcFile-1].getCurrentPiece();
        if (movingPiece == null) {
            System.out.println("Illegal Move! No piece on first selected square");
            return false;
        }

        // error with inCheck params: when to flip?
        if (movingPiece.isLegalMove(source, dest, this)) {
            if (!skipChecks && inCheck(false)) {
                Board testBoard = new Board(this, true);

                testBoard.move(source, dest, true);
                if (testBoard.inCheck(false)) {
                    return false;
                }
            }

            if (movingPiece instanceof King && Math.abs(srcFile-destFile) >= 2) {
                handleCastle(destFile-srcFile == 2);
                moveHistory.add(new Move(source, dest));
                whiteToMove = !whiteToMove;
                moveNumber++;
                timesMoved++;
                return true;
            }

            if (movingPiece instanceof King) {
                if (whiteToMove) {
                    whiteKing = dest;
                } else {
                    blackKing = dest;
                }
            }

            squares[srcRank-1][srcFile-1].setCurrentPiece(null);


            whiteToMove = !whiteToMove;
            squares[destRank-1][destFile-1].setCurrentPiece(movingPiece);

            movingPiece.setCurrentSquare(getSquare(dest));

            // handle pawn promotion here (auto queen probably)
            if (movingPiece instanceof Pawn && (destRank == 8 || destRank == 1)) {
                // movingPiece is at end, change to queen
                squares[destRank-1][destFile-1].setCurrentPiece(new Queen(!whiteToMove));
            }

            moveHistory.add(new Move(source, dest));
            moveNumber++;
        } else {
            System.out.println("Illegal Move! Other Reason");
            return false;
        }

        timesMoved++;
        return true;
    }

    public ArrayList<Move> getMoveHistory() {
        return moveHistory;
    }

    public int codeToFile(String code) {
        return (int) code.charAt(0) - 96;
    }

    public int codeToRank(String code) {
        // flip rank index ex: 4 to 4 or 2 to 6
        // why not flipped on rook?
        // don't do 9-rank
        return (int) code.charAt(1) - 48;
    }
    // 3, 4 to "d3"
    public String rankAndFileToCode(int rank, int file) {
        // convert 8 to 'h'
        // 1 to 'a'
        return String.valueOf((char) (file + 96)) + rank;
    }

    // are YOU in check? if yes, ret true
    // need to prevent moving into check
    public boolean inCheck(boolean flip) {
        // whiteToMove innacurate for moving into check
        boolean turn = whiteToMove;
        if (flip) {
            turn = !whiteToMove;
        }
//        Square kingSquare = findKing(whiteToMove);
        Square kingSquare = findKingStr(turn);
        if (kingSquare == null) {
            return false;
        }

        // all possible moves for hypothetical next turn (aka: under attack)
        ArrayList<Move> moves = Engine.getAllPossibleMoves(this, !turn);

        for (Move m : moves) {
            if (m.getDest().equals(kingSquare.toString())) {
//                System.out.println("Check: "+kingSquare.toString());
                return true;
            }
        }

        return false;
    }


    public int getMoveNumber() {
        return moveNumber;
    }

    // if input "e2", output is "e3" and "e4"
    // THIS RUNS (numPieces) TIMES WHEN GETTING ALL LEGAL MOVES INEFFICIENT AF
    // need to optimize, don't check every square for all pieces (ex: if srcsquare pawn, only check where max distance is 2
    public static int getLegalMovesCalled = 0;
    public ArrayList<Move> getLegalMoves(String srcSquare) {
        ArrayList<Move> possibleMoves = new ArrayList<>();
        getLegalMovesCalled++;

        if (getSquare(srcSquare).getCurrentPiece() == null) {
            return possibleMoves;
        }

        // for one piece, look at legal moves (pawn on e2)
        // optimizations: bishops stay on color, pawns don't move more than 2, rooks stay on rank or file
        for (int i = 1; i <= 8; i++) { // file
            for (int j = 1; j <= 8; j++) { // rank
                String destSquare = rankAndFileToCode(j,i);
                Piece piece = getSquare(srcSquare).getCurrentPiece();

                if (srcSquare.equals(destSquare)) continue;

                if (piece.isLegalMove(srcSquare, destSquare, this)) {
                    possibleMoves.add(new Move(srcSquare, destSquare));
                }
            }
        }

        return possibleMoves;
    }

    public Square getSquare(String algSquare) {
        int rank = codeToRank(algSquare);
        int file = codeToFile(algSquare);
        return squares[rank-1][file-1];
    }

    // ex: 3, 4 means "c4"
    public Square getSquare(int file, int rank) {
        return squares[rank-1][file-1];
    }

    public int distance(String src, String dest) {
        // returns optimal distance between two squares (only if straight or diagonal)
        int srcRank = (int) src.charAt(1) - 48;
        int destRank = (int) dest.charAt(1) - 48;

        int srcFile = (int) src.charAt(0) - 96;
        int destFile = (int) dest.charAt(0) - 96;

        int rankPart = (destRank-srcRank)*(destRank-srcRank);
        int filePart = (destFile-srcFile)*(destFile-srcFile);
        double dist = Math.sqrt(rankPart + filePart);
        if (dist != Math.floor(dist)) return -1;


        return (int) dist;
    }


    // get whole square from string
    public Square findKingStr(boolean white) {
        if (white) {
            return getSquare(whiteKing);
        } else {
            return getSquare(blackKing);
        }
    }

    @Override
    public int hashCode() {
        // unique id for board position: sum(posweight*rank*file)
        int code = 0;
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                Piece p = getSquare(i,j).getCurrentPiece();
                if (p != null) {
                    // since 1 for all pawns, many possible duplicates
                    code += (p.getValue()*(i+1)*(j+9));
                }
            }
        }
        return code;
    }

    @Override
    public String toString() {
        // [rank][file]
        int rankNum = 8;
        StringBuilder boardString = new StringBuilder();
        for (int i = 7; i >= 0; i--) {
            for (int j = 0; j < 8; j++) {
                Piece curPiece = squares[i][j].getCurrentPiece();
                if (curPiece == null) {
                    boardString.append("_");
                } else {
                    if (squares[i][j].getCurrentPiece().isWhite()) {
                        boardString.append(ANSI_WHITE);
                    } else {
                        boardString.append(ANSI_GREEN);
                    }
                    boardString.append(squares[i][j].getCurrentPiece());
                    boardString.append(ANSI_RESET);
                }
                boardString.append(" ");
            }
            boardString.append(" ");
            boardString.append(rankNum--).append("\n");
        }
        boardString.append("\n");
        for (int i = 0; i < 8; i++) {
            char ch = (char) (97+i);
            boardString.append(ch);
            boardString.append(" ");
        }

        System.out.println(whiteKing);
        System.out.println(blackKing);
        return boardString.toString();
    }
}
