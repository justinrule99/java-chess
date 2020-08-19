public class Bishop extends Piece{

    public Bishop(boolean isWhite) {
        super(isWhite);
        setValue(3);
    }

    @Override
    public String getImageName() {
        return isWhite() ? "images/Chess_blt60.png" : "images/Chess_bdt60.png";
    }

    @Override
    public boolean isLegalMove(String src, String dest, Board board) {

        // moves diagonally, forwards and backwards. always stays on the same colored square

        // make new board, do move, check if in check?

        // check diag, then collisions, then captures
        Piece srcPiece = board.getSquare(src).getCurrentPiece();

        int srcRank = (int) src.charAt(1) - 48;
        int destRank = (int) dest.charAt(1) - 48;

        int srcFile = (int) src.charAt(0) - 96;
        int destFile = (int) dest.charAt(0) - 96;

        // diff between src/dest rank == src/dest file
        // abs val of diff betwen src and dest the same
        if (Math.abs(srcRank-destRank) == Math.abs(srcFile-destFile)) {
            // check collisions and captures

            // cases: rank+file+, rank-file-, rank+file-, rank-file+
            if (srcRank > destRank && srcFile > destFile) {
                // rank-file-
                // doesn't work
                // down left
                for (int i = 0; i < srcRank-destRank-1; i++) {
                    // false if collision
                    Piece movingThrough = board.getSquare(srcFile-i-1, srcRank-i-1).getCurrentPiece();
                    if (movingThrough != null) {
//                        System.out.println("--Collision! at "+board.rankAndFiletoCode(srcRank-i-1, srcFile-i-1));
                        return false;
                    }
                }
                Piece destPiece = board.getSquare(dest).getCurrentPiece();
                if (destPiece != null) {
                    return destPiece.isWhite() != srcPiece.isWhite();
                }
                return true;

            } else if (srcRank < destRank && srcFile < destFile) {
                // rank+file+
                // fully working
                for (int i = 0; i < (destRank-srcRank)-1; i++) {
                    // false if collision
                    Piece movingThrough = board.getSquare(srcFile+i+1, srcRank+i+1).getCurrentPiece();
                    if (movingThrough != null) {
//                        System.out.println("++Collision! at "+board.rankAndFiletoCode(srcRank+i+1, srcFile+i+1)+" with piece "+movingThrough);
                        return false;
                    }
                }
                Piece destPiece = board.getSquare(dest).getCurrentPiece();
                if (destPiece != null) {
                    return destPiece.isWhite() != srcPiece.isWhite();
                }
                return true;

            } else if (srcRank > destRank && srcFile < destFile) {
                // rank-file+
                // fully working
                for (int i = 0; i < srcRank-destRank-1; i++) {
                    // false if collision
                    Piece movingThrough = board.getSquare(srcFile+i+1, srcRank-i-1).getCurrentPiece();
                    if (movingThrough != null) {
//                        System.out.println("r-f+Collision! at "+board.rankAndFiletoCode(srcRank-i-1, srcFile+i+1));
                        return false;
                    }
                }
                Piece destPiece = board.getSquare(dest).getCurrentPiece();
                if (destPiece != null) {
                    return destPiece.isWhite() != srcPiece.isWhite();
                }
                return true;

            } else {
                // rank+file-
                // fully working

                // ex: f1-d3
                for (int i = 0; i < destRank-srcRank-1; i++) {
                    Piece movingThrough = board.getSquare(srcFile-i-1, srcRank+i+1).getCurrentPiece();
                    if (movingThrough != null) {
//                        System.out.println("r+f-Collision! at "+board.rankAndFiletoCode(srcRank+i+1, srcFile-i-1));
                        return false;
                    }
                }
                // check dest: if white, collide, else capture
                Piece destPiece = board.getSquare(dest).getCurrentPiece();
                if (destPiece != null) {
                    return destPiece.isWhite() != srcPiece.isWhite();
                }
                return true;
            }
        }



        return false;
    }

    @Override
    public String toString() {
        return "B";
    }
}
