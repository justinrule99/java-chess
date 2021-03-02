import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.File;
import java.io.IOException;

public class ChessTesting {

    public static void main(String[] args) {

        // need to write a pgn -> src dest conversion

        long startTime = System.nanoTime();

        Board b = new Board();


        // need final board (where eval comes from)
        // if entire history: move is get(size-depth)
        // might not need moveHistory

//        while (!b.whiteWins && !b.blackWins) {
//            b.move(Engine.getBestMove(b,1,whiteTurn));
//            System.out.println(b.getMoveHistory());
//            whiteTurn = !whiteTurn;
//        }

        boolean whiteTurn = true;
//        for (int i = 0; i < 30; i++) {
//            // 10 move game
//            b.move(Engine.getBestMove(b, 2, whiteTurn));
//            whiteTurn = !whiteTurn;
//        }


        int moveIdx = 1;
        Move best = Engine.getBestMove(b,2,true);
        while (best != null) {
            b.move(best);
            whiteTurn = !whiteTurn;
            best = Engine.getBestMove(b,2,whiteTurn);
            if (moveIdx % 10 == 0) System.out.println("Move "+moveIdx);
            moveIdx++;
        }

        System.out.println(b.getMoveHistory());


        long endTime   = System.nanoTime();
        long totalTime = endTime - startTime;
        System.out.println();
        System.out.println("Runtime: "+totalTime / 1000000000.0+" seconds");
    }
}
