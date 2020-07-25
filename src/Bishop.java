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

        // check diag, then collisions, then captures

        int srcRank = (int) src.charAt(1) - 48;
        int destRank = (int) dest.charAt(1) - 48;

        int srcFile = (int) src.charAt(0) - 96;
        int destFile = (int) dest.charAt(0) - 96;

        // diff between src/dest rank == src/dest file
        // abs val of diff betwen src and dest the same
        if (Math.abs(srcRank-destRank) == Math.abs(srcFile-destFile)) {
            // check collisions and captures
            // along the way and at dest
            // create arr strings of travel path, check collisions in all
            // smaller rank -> larger rank
            int smallerRank = Math.min(srcRank, destRank);
            int largerRank = Math.max(srcRank, destRank);
            int smallerFile = Math.min(srcFile, destFile);
            int largerFile = Math.max(srcFile, destFile);

            int iIdx = 0;
            int jIdx = 0;

            // cases: rank+file+, rank-file-, rank+file-, rank-file+
            if (srcRank > destRank && srcFile > destFile) {
                // rank-file-
                // loop (dec) both
                for (int i = (srcRank-destRank)-1; i >= 0; i--) {
                    // false if collision
                    Piece movingThrough = board.getSquare(srcFile-i-1, srcRank-i-1).getCurrentPiece();
                    if (movingThrough != null) {
                        System.out.println("--Collision! at "+board.fileAndRankToCode(srcRank-i-1, srcFile-i-1));
                        return false;
                    }
                }
                return true;

            } else if (srcRank < destRank && srcFile < destFile) {
                // rank+file+
                for (int i = 0; i < (destRank-srcRank)-1; i++) {
                    // false if collision
                    Piece movingThrough = board.getSquare(srcFile+i+1, srcRank+i+1).getCurrentPiece();
                    if (movingThrough != null) {
                        System.out.println("++Collision! at "+board.fileAndRankToCode(srcRank+i+1, srcFile+i+1));
                        return false;
                    }
                }
                return true;

            } else if (srcRank > destRank && srcFile < destFile) {
                // rank-file+
                // top right from black
                for (int i = srcRank-destRank-1; i >= 0; i--) {
                    // false if collision
                    Piece movingThrough = board.getSquare(srcFile+i+1, srcRank-i-1).getCurrentPiece();
                    if (movingThrough != null) {
                        System.out.println("Collision! at "+board.fileAndRankToCode(srcRank-i-1, srcFile+i+1));
                        return false;
                    }
                }
                return true;

            } else {
                // rank+file-

                for (int i = 0; i < srcRank-destRank-1; i++) {
                    // false if collision
                    Piece movingThrough = board.getSquare(srcFile-i-1, srcRank+i+1).getCurrentPiece();
                    if (movingThrough != null) {
                        System.out.println("Collision! at "+board.fileAndRankToCode(srcRank+i+1, srcFile-i-1));
                        return false;
                    }
                }
                return true;

            }

//            for (int i = 0; i < largerRank-smallerRank; i++) {
//                for (int j = 0; j < largerFile-smallerFile; j++) {
//                    if ((board.getSquare(j + smallerFile, i + smallerRank).getCurrentPiece() != null) && i == j) {
//                        System.out.println("COLISON at " + board.fileAndRankToCode(i+smallerRank, j+smallerFile));
//                        return false;
//                    }
//                }
//            }


        }

        return false;
    }

    @Override
    public String toString() {
        return "B";
    }
}
