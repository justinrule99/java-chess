import com.sun.security.jgss.GSSUtil;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Stack;

public class Board {
    // represents a standard chess board

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    // 2d array of squares: will have piece info
    // need move history in stack
    private Square[][] squares;
    private int moveNumber;
    private boolean whiteToMove;
    private ArrayList<Move> moveHistory;

    //TODO: ctor to create new board based on given position

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
    }

    // initialize the game with pieces on starting squares
    public Board() {
        // squares [ranks][files]
        squares = new Square[8][8];
        // will need to flip board later
        // now: i = 7 is rank 1, i = 1 is rank 7
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
                        squares[i][j] = new Square(isWhite, new King(i == 0), i,j);
                    }
                } else {
                    squares[i][j] = new Square(isWhite, null, i,j);
                }
                isWhite = !isWhite;
            }
            isWhite = !isWhite;
        }
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
            squares[srcRank-1][srcFile-1].setCurrentPiece(null);

            int destFile = codeToFile(dest);
            // flipped
            int destRank = codeToRank(dest);
            // piece moves to opposite side visually, array correct
            whiteToMove = !whiteToMove;
            squares[destRank-1][destFile-1].setCurrentPiece(movingPiece);
            // e2 e4: squares[3][4]

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
        int rank = (int) code.charAt(1) - 48;
        // flip rank index ex: 4 to 4 or 2 to 6
        // why not flipped on rook?
        // don't do 9-rank
        return rank;
    }
    // 3, 4 to "d3"
    public String fileAndRankToCode(int rank, int file) {
        StringBuilder sb = new StringBuilder();
        // convert 8 to 'h'
        // 1 to 'a'

        sb.append((char) (file+96));
        sb.append(rank);


        return sb.toString();
    }

    public ArrayList<Square> findPieces(Piece p, boolean isWhite) {
        ArrayList<Square> squares = new ArrayList<>();
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                Piece cur = getSquare(i,j).getCurrentPiece();
                if (cur != null) {

                }
            }
        }

        return squares;
    }

    public boolean inCheck() {
        if (whiteToMove){
            // white in check if black has any legal moves with dest as whites king
            ArrayList<Move> m = Engine.getAllPossibleMoves(this, false);
            // find white's king
        } else {
            // black in check if white has any legal moves with dest as blacks king
            // find black's king
        }

        return false;
    }

    public int getMoveNumber() {
        return moveNumber;
    }

    // if input "e2", output is "e3" and "e4"
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
