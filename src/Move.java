

public class Move implements Comparable<Move> {

    // src, dest, captures, etc
    // should keep up to date board

    public String src;
    public String dest;

    public Move(String src, String dest) {
        this.src = src;
        this.dest = dest;
    }

    public String getSrc() {
        return src;
    }

    public String getDest() {
        return dest;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public void setDest(String dest) {
        this.dest = dest;
    }

    public String toAlgebraic(Board board) {
        Piece piece = board.getSquare(src).getCurrentPiece();
        System.out.println(src);
        System.out.println(dest);
        if (piece instanceof Pawn) {
            return dest;
        }

        return piece + dest;
    }

    public String toStringUCI() {
        return src+dest;
    }

    @Override
    public String toString() {
        return src + " "+ dest;
    }

    @Override
    public int compareTo(Move m) {
        // compare based on src + dest
        // if both 0: same.
        int m1 = src.compareTo(m.getSrc());
        int m2 = dest.compareTo(m.getDest());

        System.out.println("sources: "+m1);
        System.out.println("dest: "+m2);

//        if (m1 == 0 && m2 == 0) return 0;

        return m1 + m2;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Move)) return false;
        Move compare = (Move) obj;
        return this.src.equals(compare.src) && this.dest.equals(compare.dest);
    }
}
