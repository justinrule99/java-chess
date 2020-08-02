
// one square on a chess board
public class Square {
    private boolean isWhite;
    // null if empty
    private Piece currentPiece;
    public final int rank;
    public final int file;

    public Square(boolean isWhite, Piece currentPiece, int rank, int file) {
        // init square with color and starting piece
        this.isWhite = isWhite;
        this.currentPiece = currentPiece;
        this.rank = rank+1;
        this.file = file+1;
    }


    public boolean isWhite() {
        return isWhite;
    }

    public void setWhite(boolean white) {
        isWhite = white;
    }

    public Piece getCurrentPiece() {
        return currentPiece;
    }

    public void setCurrentPiece(Piece currentPiece) {
        this.currentPiece = currentPiece;
    }

    public int getRank() {return rank;}
    public int getFile() {return file;}

    @Override
    public String toString() {
        // alg notation from rank and file

        return String.valueOf((char) (file + 96)) + rank;
    }
}
