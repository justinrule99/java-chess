import java.util.ArrayList;

public class ChessTesting {

    public static void main(String[] args) {


        long startTime = System.nanoTime();
        Board b = new Board();

//
//        b.move("e2","e4");
//        b.move("e4","e5");
//
////        System.out.println(b.getMoveHistory());
//
//        // erases old history
//        Board b2 = new Board(b);
////        System.out.println(b2.getMoveHistory());
//        b2.move("d2", "d3");
//        System.out.println(b.getMoveHistory());
//
//        System.out.println(b2.getMoveHistory());


        // need final board (where eval comes from)
        // if entire history: move is get(size-depth)
        // might not need moveHistory
        System.out.println(Engine.getBestMove(b, 4,true));



        long endTime   = System.nanoTime();
        long totalTime = endTime - startTime;
        System.out.println("Runtime: "+totalTime / 1000000000.0+" seconds");


    }
}
