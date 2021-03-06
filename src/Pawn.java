public class Pawn extends Piece {

    public Pawn(boolean isWhite) {
        super(isWhite);
        setValue(1);
    }

    @Override
    public String getImageName() {
        return isWhite() ? "images/Chess_plt60.png" : "images/Chess_pdt60.png";
    }

    // TODO: pawn promotion

    @Override
    public boolean isLegalMove(String src, String dest, Board board) {
        isLegalMovesCalls++;
        isLegalMovePawn++;

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

            if (destRank <= srcRank || destRank > srcRank+maxDistance) {
                return false;
            }

            if ((srcFile+1 == destFile || srcFile-1 == destFile) && board.getSquare(dest).getCurrentPiece() != null && !board.getSquare(dest).getCurrentPiece().isWhite()) {
                // legal captures move
                // doesn't check distance
                // return dist == 1
                return srcRank+1 == destRank;
            }

            if (destRank == srcRank+2) {
                // check in between
                if(board.getSquare(srcFile, srcRank+1).getCurrentPiece() != null) {
                    return false;
                }
            }

            Piece destPiece = board.getSquare(dest).getCurrentPiece();

            // if exists piece in path and is same color
            if (destPiece != null) {
                return false;
            }

            return srcFile == destFile;

        } else { // black pawn
            // only moves up one square at a time or two
            int maxDistance = srcRank == 7 ? 2 : 1;
            // allow 2 or 1??
            // bad if diff between src rank and dest rank is > maxDistance
            if (srcRank < destRank) {
                // also check collisions straight ahead
                return false;
            }

            if (destRank < srcRank-maxDistance) {
                return false;
            }

            if ((srcFile+1 == destFile || srcFile-1 == destFile) && board.getSquare(dest).getCurrentPiece() != null && board.getSquare(dest).getCurrentPiece().isWhite()) {
                // legal captures move
                return srcRank-1 == destRank;
            }

            if (destRank == srcRank-2) {
                // check in between
                if(board.getSquare(srcFile, srcRank-1).getCurrentPiece() != null) {
                    return false;
                }
            }

            Piece destPiece = board.getSquare(dest).getCurrentPiece();

            // if exists piece in path and is same color
            if (destPiece != null) {
                return false;
            }

            return srcFile == destFile;
        }
    }


    @Override
    public String toString() {
        return "P";
    }
}
