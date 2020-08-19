
// contains info shared between all pieces

public abstract class Piece {

    // null if captured
    protected Square currentSquare;
    protected boolean white;
    protected int value;

    public Piece(boolean white) {
        this.white = white;
    }

    // how to define movement behavior

    public boolean isLegalMove(String src, String dest, Board board) {
        // see if move resolves check (if in check)?

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
