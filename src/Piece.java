
// contains info shared between all pieces

public abstract class Piece {

    // null if captured
    protected String currentSquare;
    protected boolean white;
    protected int value;

    public Piece(boolean white) {
        this.white = white;
    }

    // how to define movement behavior

    public abstract boolean isLegalMove(String src, String dest, Board board);
    public abstract String getImageName();

    // assume legal now?
    public void movePiece(String destSquare) {
        currentSquare = destSquare;
    }


    public String getCurrentSquare() {
        return currentSquare;
    }

    public void setCurrentSquare(String currentSquare) {
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