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
    // making shallow copies of king somewhere?
    public King whiteKing = new King(true);
    public King blackKing = new King(false);
    private ArrayList<Move> moveHistory;

    public Board(Board b, boolean switchTurn) {
        this(b);
        if (switchTurn) {
            whiteToMove = !b.whiteToMove;
        }
    }

    public Board(Board b) {
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
        moveNumber = b.moveNumber;
        whiteToMove = b.whiteToMove;

        // this is a shallow copy constructor?
        // does this break all pieces or just king?
        whiteKing = new King(b.whiteKing.isWhite());
//        System.out.println("whikiing square in copy: ");
        whiteKing.setCurrentSquare(b.whiteKing.getCurrentSquare());

        // building off the previous, might not be current
        whiteKing.setCurrentSquare(b.findKing(true));

//        System.out.println(whiteKing.getCurrentSquare());


        blackKing = new King(b.blackKing.isWhite());
//        blackKing.setCurrentSquare(b.blackKing.getCurrentSquare());
        blackKing.setCurrentSquare(b.findKing(false));
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
                        Square e1 = new Square(isWhite, whiteKing , i,j);
                        Square e8 = new Square(isWhite, blackKing , i,j);
                        whiteKing.setCurrentSquare(e1);
                        blackKing.setCurrentSquare(e8);

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

    public boolean move(Move m) {
        return move(m.getSrc(), m.getDest(), false);
    }

    // if in check, move must resolve check
    // returns false if didn't move, true if did
    public boolean move(String source, String dest, boolean skipChecks) {
        // move("d3", "d4") will see piece

        // king movements cause problems in engine, fine for humans
        // this blocks king movements for now
//        if (source.equals("e1") || source.equals("e8")) {
//            return;
//        }

        int srcFile = codeToFile(source);
        int srcRank = codeToRank(source);


        // indices always rank, file
        // illegal move if no piece on source
        Piece movingPiece = squares[srcRank-1][srcFile-1].getCurrentPiece();
        if (movingPiece == null) {
            System.out.println("Illegal Move! No piece on first selected square");
            return false;
        }

        // no check stuff right nowp

        // should this be in move?
        if (movingPiece.isLegalMove(source, dest, this)) {
            if (!skipChecks && inCheck()) {
                Board testBoard = new Board(this, true);

                testBoard.move(source, dest, true);
//                 switch side?
                if (testBoard.inCheck()) {
                    return false;
                }
            }

            squares[srcRank-1][srcFile-1].setCurrentPiece(null);

            int destFile = codeToFile(dest);
            int destRank = codeToRank(dest);
            whiteToMove = !whiteToMove;
            squares[destRank-1][destFile-1].setCurrentPiece(movingPiece);
            // need to ignore move
            if (movingPiece instanceof King) {
                if (movingPiece.isWhite()) {
                    whiteKing.setCurrentSquare(getSquare(dest));
                } else {
                    blackKing.setCurrentSquare(getSquare(dest));
                }
            }
            movingPiece.setCurrentSquare(getSquare(dest));
            // e2 e4: squares[3][4]

            // handle pawn promotion here (auto queen)

            moveHistory.add(new Move(source, dest));
            moveNumber++;
        } else {
            System.out.println("Illegal Move! Other Reason");
            return false;
        }

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
    // usage: if it's blacks turn to move, they must resolve their own check (if exists) while not moving into check
    // this doesn't handle moving into check?
    // error: doesnt move at all when in check
    public boolean inCheck() {
        // whites turn: if black has a legal move against white's king, white is in check and needs to resolve it

        Square kingSquare = findKing(whiteToMove);

        // loop thru pieces?
        // still sets
        // have king


        if (kingSquare == null) {
            System.out.println("KS NULL: Exiting");
            return false;
        }

        // all possible moves for hypothetical next turn (aka: under attack)
        ArrayList<Move> moves = Engine.getAllPossibleMoves(this, !whiteToMove);

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
    // runs every time engine processes a node
    public ArrayList<Move> getLegalMoves(String srcSquare) {
        ArrayList<Move> possibleMoves = new ArrayList<>();
        // for one pawn, look at legal moves (pawn on e2)
        for (int i = 0; i < 8; i++) { // file
            for (int j = 0; j < 8; j++) { // rank
                // check if alg notation legal move
                StringBuilder algNot = new StringBuilder();
                // convert i to a-g, j to 1-8
                algNot.append((char) (97+i));

                algNot.append(j+1);
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
        return squares[rank-1][file-1];
    }

    // ex: 3, 4 means "c4"
    public Square getSquare(int file, int rank) {
        return squares[rank-1][file-1];
    }

    // [rank][file]
    // only gets black king on e8 here
    public Square findKing(boolean white) {
        for (int i = 7; i >= 0; i--) {
            for (int j = 0; j < 8; j++) {
                Piece curPiece = squares[i][j].getCurrentPiece();
                if (curPiece == null) {
                    continue;
                }
                if (curPiece.isKing) {
//                    System.out.println("king color: "+curPiece.isWhite()+", "+squares[i][j]);
                    if (white == curPiece.isWhite()) {
                        return squares[i][j];
                    }
                }
            }
        }
        return null;
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
        for (int i = 7; i >= 0; i--) {
            for (int j = 0; j < 8; j++) {
                Piece curPiece = squares[i][j].getCurrentPiece();
                if (curPiece == null) {
                    boardString.append("_");
                } else {
//                    System.out.println("piece at "+squares[i][j].toString());

                    if (curPiece.isKing) {
                        System.out.println(i);
                        System.out.println(j);
                        // squares[7][4] (aka e8) prints as f9 (real bug or printing error??)
                        System.out.println("IN PINRT WE HAVE KING AT "+squares[i][j].toString());
                        if (curPiece.isWhite()) {
                            whiteKing.setCurrentSquare(squares[i][j]);
                        } else {
                            blackKing.setCurrentSquare(squares[i][j]);
                        }
                    }

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

        System.out.println(whiteKing.getCurrentSquare());
        System.out.println(blackKing.getCurrentSquare());
        return boardString.toString();
    }
}
