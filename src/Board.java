import java.util.ArrayList;

public class Board {
    // represents a standard chess board

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_WHITE = "\u001B[37m";

    // 2d array of squares: will have piece info
    private Square[][] squares;
    private int moveNumber;
    private boolean whiteToMove;
    private King whiteKing = new King(true);
    private King blackKing = new King(false);
    private ArrayList<Move> moveHistory;

    public Board(Board b) {
        // shallow copy
        squares = new Square[8][8];

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Square s = new Square(b.squares[i][j].isWhite(), b.squares[i][j].getCurrentPiece(), b.squares[i][j].rank, b.squares[i][j].file);
                squares[i][j] = s;
            }
        }
        moveHistory = new ArrayList<>();
        // b move history is always empty
        // need to copy moveHistory
        this.moveHistory.addAll(b.moveHistory);
        moveNumber = b.moveNumber;
        whiteToMove = b.whiteToMove;

        whiteKing = new King(b.whiteKing.isWhite());
        whiteKing.setCurrentSquare(b.whiteKing.getCurrentSquare());

        blackKing = new King(b.blackKing.isWhite());
        whiteKing.setCurrentSquare(b.blackKing.getCurrentSquare());
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
                        if (i == 0) {
                            Square e1 = new Square(isWhite, whiteKing , i,j);
                            squares[i][j] = e1;
                            whiteKing.setCurrentSquare(e1);
                        } else {
                            Square e8 = new Square(isWhite, blackKing , i,j);
                            squares[i][j] = e8;
                            blackKing.setCurrentSquare(e8);
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

    public void move(Move m) {
        move(m.getSrc(), m.getDest());
    }

    public void move(String source, String dest) {
        // move("d3", "d4") will see piece
        int srcFile = codeToFile(source);
        int srcRank = codeToRank(source);

        // indices always rank, file
        // illegal move if no piece on source
        Piece movingPiece = squares[srcRank-1][srcFile-1].getCurrentPiece();
        if (movingPiece == null) {
            System.out.println("Illegal Move! No piece on first selected square");
            return;
        }

        if (movingPiece.isLegalMove(source, dest, this)) {
//            if (inCheck()) {
//                System.out.println("CURRENTLY IN CHECK! NEED TO RESOLVE");
//                return;
//            }

            squares[srcRank-1][srcFile-1].setCurrentPiece(null);

            int destFile = codeToFile(dest);
            int destRank = codeToRank(dest);
            whiteToMove = !whiteToMove;
            squares[destRank-1][destFile-1].setCurrentPiece(movingPiece);
            // change currentSquare on movingPiece
            movingPiece.setCurrentSquare(getSquare(dest));
            // e2 e4: squares[3][4]

            // handle pawn promotion here (auto queen)

            moveHistory.add(new Move(source, dest));
            moveNumber++;
        } else {
            System.out.println("Illegal Move! Other Reason");
        }
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

    // need to know where kings are at all times
    public boolean inCheck() {

        // whites turn: if black has a legal move against white's king, white is in check and needs to resolve it

        // stack overflow sometimes?? y tho
        Square kingSquare = whiteToMove ? whiteKing.getCurrentSquare() : blackKing.getCurrentSquare();
        if (kingSquare == null) {
            System.out.println("KS NULL: Exiting");
            return false;
        }
        ArrayList<Move> moves = Engine.getAllPossibleMoves(this, !whiteToMove);
        for (Move m : moves) {
            if (m.getDest().equals(kingSquare.toString())) {
                System.out.println("CHECK");
                return true;
            }
        }

        return false;
    }


    public int getMoveNumber() {
        return moveNumber;
    }

    // if input "e2", output is "e3" and "e4"
    // runs every time engine processes a node
    public ArrayList<Move> getLegalMoves(String srcSquare) {
        ArrayList<Move> possibleMoves = new ArrayList<>();
        // for one pawn, look at legal moves (pawn on e2)
        // for each square, check if legal?? then evaluate
        // check movement patterns to avoid checking every square
        for (int i = 0; i < 8; i++) { // file
            for (int j = 0; j < 8; j++) { // rank
                // check if alg notation legal move
                StringBuilder algNot = new StringBuilder();
                // convert i to a-g, j to 1-8
                algNot.append((char) (97+i));
                // don't check move to yourself

                algNot.append(j+1);
                // null sometimes
                if (getSquare(srcSquare).getCurrentPiece() == null) {
                    return possibleMoves;
                }
                if (srcSquare.equals(algNot.toString())) continue;

                if (getSquare(srcSquare).getCurrentPiece().isLegalMove(srcSquare, algNot.toString(), this)) {
                    possibleMoves.add(new Move(srcSquare, algNot.toString()));
                }
            }
        }

        return possibleMoves;
    }

    public Square getSquare(String algSquare) {
        int rank = codeToRank(algSquare);
        int file = codeToFile(algSquare);
//        System.out.println("in getsquare, rnak is: "+rank);
        return squares[rank-1][file-1];
    }

    // ex: 3, 4 means "c4"
    public Square getSquare(int file, int rank) {
        return squares[rank-1][file-1];
    }

    @Override
    public int hashCode() {
        // unique id for board position: sum(posweight*rank*file)
        int code = 0;
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                Piece p = getSquare(i,j).getCurrentPiece();
                if (p != null) {
                    code += (p.getValue()*i*j);
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
        // goes backwards now for ranks??
        for (int i = 7; i >= 0; i--) {
            for (int j = 0; j < 8; j++) {
                if (squares[i][j].getCurrentPiece() == null) {
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
        return boardString.toString();
    }
}
