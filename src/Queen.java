public class Queen extends Piece{

    public Queen(boolean isWhite) {
        super(isWhite);
        setValue(9);
    }

    @Override
    public String getImageName() {
        return isWhite() ? "images/Chess_qlt60.png" : "images/Chess_qdt60.png";
    }

    @Override
    public boolean isLegalMove(String src, String dest, Board board) {
        // combines bishop and rook.

        Piece b = new Bishop(isWhite());
        Piece r = new Rook(isWhite());

        if (b.isLegalMove(src, dest, board) || r.isLegalMove(src, dest, board)) return true;


        return false;
    }

    @Override
    public String toString() {
        return "Q";
    }
}
