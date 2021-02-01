public class Knight extends Piece{

    public Knight(boolean isWhite) {
        super(isWhite);
        setValue(3);
    }

    @Override
    public String getImageName() {
        return isWhite() ? "images/Chess_nlt60.png" : "images/Chess_ndt60.png";
    }

    @Override
    public boolean isLegalMove(String src, String dest, Board board) {


        // can jump. moves 2 squares one direction, one square perpendicular to first direction.

        int srcRank = (int) src.charAt(1) - 48;
        int destRank = (int) dest.charAt(1) - 48;

        int srcFile = (int) src.charAt(0) - 96;
        int destFile = (int) dest.charAt(0) - 96;

        Piece pieceToMove = board.getSquare(src).getCurrentPiece();
        // abs dist src/dest rank == 1 and abs dist file == 2 OR other way around
        if ((Math.abs(srcRank-destRank) == 2 && Math.abs(srcFile-destFile) == 1) || (Math.abs(srcRank-destRank) == 1 && Math.abs(srcFile-destFile) == 2)) {
            // deal with self collisions, then captures
            Piece destPiece = board.getSquare(dest).getCurrentPiece();
            if (destPiece != null) {
                return !destPiece.isWhite() == pieceToMove.isWhite();
            }
            return true;
        }

        // check for discovered check
        return false;
    }

    @Override
    public String toString() {
        return "N";
    }
}
