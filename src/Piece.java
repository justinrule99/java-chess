
// contains info shared between all pieces

public abstract class Piece {

    // null if captured
    protected Square currentSquare;
    protected boolean white;
    protected int value;
    // not necessary if we can use instanceOf

    public Piece(boolean white) {
        this.white = white;
    }

    // how to define movement behavior
    public static int isLegalMovesCalls = 0;
    public static int isLegalMovePawn = 0;
    public static int isLegalMoveKnight = 0;
    public static int isLegalMoveKing = 0;

    public boolean isLegalMove(String src, String dest, Board board) {
        // see if move resolves check (if in check)?
        System.out.println("SUPER H");
        // super, does this run on children?
        if (board.inCheck(false)) return false;

        // do move on new board, check if in check (cannot move into check)
        return true;
    }
    public abstract String getImageName();

    public Square getCurrentSquare() {
        return currentSquare;
    }

    public void setCurrentSquare(Square currentSquare) {
        this.currentSquare = currentSquare;
    }

    public boolean isWhite() {
        return white;
    }

    public void setColor(boolean white) {
        this.white = white;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
