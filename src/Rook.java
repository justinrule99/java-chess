public class Rook extends Piece {

    public Rook(boolean isWhite) {
        super(isWhite);
        setValue(5);
    }

    @Override
    public String getImageName() {
        return isWhite() ? "images/Chess_rlt60.png" : "images/Chess_rdt60.png";
    }

    @Override
    public boolean isLegalMove(String src, String dest, Board board) {
        isLegalMovesCalls++;
        // inCheck needs to be nonreliant on getpossiblemoves (recursion)

        // rook must move straight line, unobstructed
        // srcrank == destrank OR srcfile == destfile no exceptions
        Piece sourcePiece = board.getSquare(src).getCurrentPiece();
        Piece destPiece = board.getSquare(dest).getCurrentPiece();
        if (destPiece instanceof King) return false;

        // refactor to be shared later
        int srcRank = (int) src.charAt(1) - 48;
        int destRank = (int) dest.charAt(1) - 48;

        int srcFile = (int) src.charAt(0) - 96;
        int destFile = (int) dest.charAt(0) - 96;



        // legal movement
        if (srcRank == destRank) {
            // HORIZONTAL

            if (srcFile > destFile) {
                // moving "left" from white
                for (int i = srcFile-1; i >= destFile ; i--) {
                    Piece movingThrough = board.getSquare(i, srcRank).getCurrentPiece();
                    if (movingThrough != null) {
                        if (movingThrough.isWhite() == sourcePiece.isWhite()) {
                            return false;
                        } else {
                            return i == destFile;
                        }
                    }
                }
            } else {
                // moving "right" from white
                for (int i = srcFile+1; i <= destFile ; i++) {
                    Piece movingThrough = board.getSquare(i, srcRank).getCurrentPiece();
                    if (movingThrough != null) {
                        if (movingThrough.isWhite() == sourcePiece.isWhite()) {
                            return false;
                        } else {
                            return i == destFile;
                        }
                    }
                }
            }

            return true;

        } else if (srcFile == destFile) {
            // VERTICAL
            if (srcRank > destRank) {
                // going "down"
                for (int i = srcRank-1; i >= destRank; i--) {
                    Piece movingThrough = board.getSquare(srcFile, i).getCurrentPiece();

                    if (movingThrough != null) {
                        if (movingThrough.isWhite() == sourcePiece.isWhite()) {
                            return false;
                        } else {
                            return i == destRank;
                        }
                    }
                }
            } else {
                // going "up"
                for (int i = srcRank+1; i <= destRank; i++) {
                    Piece movingThrough = board.getSquare(srcFile, i).getCurrentPiece();
                    if (movingThrough != null) {
                        if (movingThrough.isWhite() == sourcePiece.isWhite()) {
                            return false;
                        } else {
                            return i == destRank;
                        }
                    }

                }
            }

            // cannot move INTO check
            // simulate move
            return true;
        }

        return false;
    }

    @Override
    public String toString() {
        return "R";
    }
}
