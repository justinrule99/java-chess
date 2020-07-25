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
        // rook must move straight line, unobstructed
        // srcrank == destrank OR srcfile == destfile no exceptions
        Piece sourcePiece = board.getSquare(src).getCurrentPiece();

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
                    // doesn't work
                    if (movingThrough != null) {
                        System.out.println("moveing theoh a pic HORIZ: "+src+" thru "+dest);
                        if (movingThrough.isWhite() == sourcePiece.isWhite()) {
                            return false;
                        } else {
                            // opp colors: handle capture: should only happen on dest?
                        }
                    }
                }
            } else {
                // moving "right" from white
                for (int i = srcFile+1; i <= destFile ; i++) {
                    Piece movingThrough = board.getSquare(i, srcRank).getCurrentPiece();
                    if (movingThrough != null) {
                        System.out.println("moveing theoh a pic HORIZ: "+src+" thru "+dest);
                        if (movingThrough.isWhite() == sourcePiece.isWhite()) {
                            return false;
                        } else {
                            // opp colors: handle capture: should only happen on dest?
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
                    System.out.println("moveing theoh a pic: "+src+" thru "+dest+", TYPE: "+movingThrough);

                    if (movingThrough != null) {
                        if (movingThrough.isWhite() == sourcePiece.isWhite()) {
                            return false;
                        } else {
                            // opp colors: handle capture: should only happen on dest?
                            System.out.println("(down) no piece on file "+ srcFile+" rank "+i);

                        }
                    }
                }
            } else {
                // going "up"
                for (int i = srcRank+1; i < destRank; i++) {
                    Piece movingThrough = board.getSquare(srcFile, i).getCurrentPiece();
                    if (movingThrough != null) {
                        if (movingThrough.isWhite() == sourcePiece.isWhite()) {
                            return false;
                        } else {
                            // opp colors: handle capture: should only happen on dest?
                            System.out.println("no piece on file "+ srcFile+" rank "+i);
                        }
                    }

                }
            }

            // if moving backwards: not correct
            // need to decrement if going backwards

            // gone through entire move, no problems
            return true;
        }


        return false;
    }

    @Override
    public String toString() {
        return "R";
    }
}
