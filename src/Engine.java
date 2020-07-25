import java.util.ArrayList;

// assumes game (Board object) works properly
public class Engine {
    private Board board;

    public Engine(Board board) {
        this.board = board;
    }

    public Move getBestMove(boolean whiteTurn) {

        return new Move("e2", "e4");
    }

    // get legal moves for all pieces on the board
    public ArrayList<Move> getAllPossibleMoves(boolean whiteTurn) {
        ArrayList<Move> moves = new ArrayList<>();

        return moves;
    }

}
