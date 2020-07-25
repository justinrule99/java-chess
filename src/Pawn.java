

public class Pawn extends Piece {


    public Pawn(boolean isWhite) {
        super(isWhite);
        setValue(1);
    }

    @Override
    public String getImageName() {
        return isWhite() ? "images/Chess_plt60.png" : "images/Chess_pdt60.png";
    }

    // should each piece have a move history?

    // fails for: c7 c5
    @Override
    public boolean isLegalMove(String src, String dest, Board board) {
//        int srcRank = (int) src.charAt(1);
        int srcRank = (int) src.charAt(1) - 48;
        int destRank = (int) dest.charAt(1) - 48;

        int srcFile = (int) src.charAt(0) - 96;
        int destFile = (int) dest.charAt(0) - 96;
        // check movement rules, then in the way, then checks
        // first check white
        if (isWhite()) {

            // check distance FIRST, ret false if bad
            // still need to move a pawn twice if on second rank??
            int maxDistance = srcRank == 2 ? 2 : 1;
            if ((int) dest.charAt(1) <= (int) src.charAt(1) || (int) dest.charAt(1) > ((int) src.charAt(1))+maxDistance) {
                // also check collisions straight ahead
                return false;
            }

            if ((srcFile+1 == destFile || srcFile-1 == destFile) && board.getSquare(dest).getCurrentPiece() != null && !board.getSquare(dest).getCurrentPiece().isWhite()) {
                // legal captures move
                // doesn't check distance
                return true;
            }

            // only moves up one square at a time or two
            // also check collisions straight ahead
            if (board.getSquare(dest).getCurrentPiece() == null) {
                return srcFile == destFile;
            }
            // handle captures if possible

            // handle pawn promotion later

        } else { // black pawn
            // only moves up one square at a time or two
            int maxDistance = srcRank == 7 ? 2 : 1;
            // allow 2 or 1??
            // bad if diff between src rank and dest rank is > maxDistance
            if ((int) src.charAt(1) < (int) dest.charAt(1)) {
                // also check collisions straight ahead
                System.out.println("fell dist bad: "+dest);

                return false;

            }

            if ((int) dest.charAt(1) < ((int) src.charAt(1))-maxDistance) {
                System.out.println("fell from too long: "+dest);
                return false;
            }

            if ((srcFile+1 == destFile || srcFile-1 == destFile) && board.getSquare(dest).getCurrentPiece() != null && board.getSquare(dest).getCurrentPiece().isWhite()) {
                // legal captures move
                System.out.println("Legal captures move");
                return true;
            }

            if (board.getSquare(dest).getCurrentPiece() == null) {
                return srcFile == destFile;
            }
        }

        // check if move creates a discovered check
        System.out.println("fell thru: "+dest);
        return false;
    }


    @Override
    public String toString() {
        return "P";
    }
}
