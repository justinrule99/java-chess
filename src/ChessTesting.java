import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.File;
import java.io.IOException;

public class ChessTesting {

    public static void main(String[] args) {

        // need to write a pgn -> src dest conversion

        long startTime = System.nanoTime();

        Board b = new Board();

        Board newBoard = new Board(b);
        System.out.println();
        System.out.println();
        System.out.println();

        newBoard.move(Engine.getBestMove(newBoard, 1, true));


        Board newBoard2 = new Board(newBoard);


        // need final board (where eval comes from)
        // if entire history: move is get(size-depth)
        // might not need moveHistory

//        boolean whiteTurn = true;
//        for (int i = 0; i < 30; i++) {
//            // 10 move game
//            b.move(Engine.getBestMove(b, 2, whiteTurn));
//            whiteTurn = !whiteTurn;
//        }

//        b.move(new Move("e2", "e4"));


//        Move best = Engine.getBestMove(b,4,b.isWhiteToMove());
//        System.out.println("best move:"+best);
//        System.out.println(Engine.evaluate(b));
//        b.move(best);


        System.out.println(b.getMoveHistory());


        long endTime   = System.nanoTime();
        long totalTime = endTime - startTime;
        System.out.println();
        System.out.println("Runtime: "+totalTime / 1000000000.0+" seconds");
    }
}
