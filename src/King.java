public class King extends Piece{

    public King(boolean isWhite) {
        super(isWhite);
        this.isKing = true;
        setValue(10);
    }

    @Override
    public String getImageName() {
        return isWhite() ? "images/Chess_klt60.png" : "images/Chess_kdt60.png";
    }

    // check defined as: opponent has a legal move (any piece) onto king's square

    @Override
    public boolean isLegalMove(String src, String dest, Board board) {

        // moves one square in any direction (except castles), cannot move into checks
        // if in check, must resolve!

        // abs diff rank && abs diff file <= 1
        int srcRank = (int) src.charAt(1) - 48;
        int destRank = (int) dest.charAt(1) - 48;

        int srcFile = (int) src.charAt(0) - 96;
        int destFile = (int) dest.charAt(0) - 96;

        if (Math.abs(srcRank-destRank) <= 1 && Math.abs(srcFile-destFile) <= 1) {

            // check collisions, checks, etc
            // if dest has piece same color, invalid
            Piece destPiece = board.getSquare(dest).getCurrentPiece();
            if (destPiece != null && destPiece.isWhite() == isWhite()) return false;

            // after each (simulated) move: check if in check
            // if yes, false
//            Board simAfter = new Board(board);
//            simAfter.move(src, dest, true);
//            if (simAfter.inCheck()) return false;


            return true;
        }


        return false;
    }

    @Override
    public String toString() {
        return "K";
    }
}
