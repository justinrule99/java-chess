
// contains info shared between all pieces

public abstract class Piece {

    // null if captured
    protected Square currentSquare;
    protected boolean white;
    protected int value;
    // not necessary if we can use instanceOf
    protected boolean isKing;

    public Piece(boolean white) {
        this.white = white;
        this.isKing = false;
//        this.isKing = isKing;
    }

    // how to define movement behavior

    public boolean isLegalMove(String src, String dest, Board board) {
        // see if move resolves check (if in check)?
        System.out.println("SUPER H");
        // super, does this run on children?
        if (board.inCheck()) return false;

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
