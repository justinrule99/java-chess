public class King extends Piece{

    public King(boolean isWhite) {
        super(isWhite);
        setValue(90);
    }

    @Override
    public String getImageName() {
        return isWhite() ? "images/Chess_klt60.png" : "images/Chess_kdt60.png";
    }

    // check defined as: opponent has a legal move (any piece) onto king's square

    @Override
    public boolean isLegalMove(String src, String dest, Board board) {
        // moves one square in any direction, cannot move into checks


        return false;
    }

    @Override
    public String toString() {
        return "K";
    }
}
