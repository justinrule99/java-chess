
// one square on a chess board
public class Square {
    private boolean isWhite;
    // null if empty
    private Piece currentPiece;
    private int rank;
    private int file;

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

    @Override
    public String toString() {
        // alg notation from rank and file
        StringBuilder sb = new StringBuilder();
        sb.append((char) (file+96));
        sb.append(rank);

        return sb.toString();
    }
}
