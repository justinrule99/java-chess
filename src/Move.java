

public class Move {

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

    @Override
    public String toString() {
        return src + " "+ dest;
    }
}
