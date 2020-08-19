import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.File;
import java.io.IOException;

public class ChessTesting {

    public static void main(String[] args) {


        long startTime = System.nanoTime();
        Board b = new Board();



        // need final board (where eval comes from)
        // if entire history: move is get(size-depth)
        // might not need moveHistory

        boolean whiteTurn = true;
        for (int i = 0; i < 10; i++) {
            // 50 move game
            b.move(Engine.getBestMove(b, 3, whiteTurn));
            whiteTurn = !whiteTurn;
        }
        System.out.println(b.getMoveHistory());


        long endTime   = System.nanoTime();
        long totalTime = endTime - startTime;
        System.out.println();
        System.out.println("Runtime: "+totalTime / 1000000000.0+" seconds");
    }
}
