import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.File;
import java.io.IOException;

public class ChessTesting {

    public static void main(String[] args) {

        // need to write a pgn -> src dest conversion
        //

        long startTime = System.nanoTime();

        Board b = new Board();
//        OpeningBook.cleanUciInput();


//        OpeningBook.getGamesFromPosition("d2d4 g8f6 g1f3");
//        Move m1 = new Move("e2", "e4");
//        Move m2 = new Move("g2", "c4");
//        System.out.println(m1.compareTo(m2));
//        OpeningBook.sortUciInput();

        // need final board (where eval comes from)
        // if entire history: move is get(size-depth)
        // might not need moveHistory

//        while (!b.whiteWins && !b.blackWins) {
//            b.move(Engine.getBestMove(b,1,whiteTurn));
//            System.out.println(b.getMoveHistory());
//            whiteTurn = !whiteTurn;
//        }

        boolean whiteTurn = true;
        for (int i = 0; i < 30; i++) {
            b.move(Engine.getBestMove(b, 2, whiteTurn));
            whiteTurn = !whiteTurn;
        }


//        int moveIdx = 1;
//        Move best = Engine.getBestMove(b,2,true);
//        while (best != null) {
//            b.move(best);
//            whiteTurn = !whiteTurn;
//            best = Engine.getBestMove(b,2,whiteTurn);
//            if (moveIdx % 10 == 0) System.out.println("Move "+moveIdx);
//            moveIdx++;
//        }

        System.out.println(b.getMoveHistory());
        System.out.println("Entered Move: "+Board.timesEnteredMove);
        System.out.println("Actually Moved: "+Board.timesMoved);
        System.out.println("Best Moves Called: "+Engine.bestMovesCalled);
        System.out.println("isLegalMove Called: "+Piece.isLegalMovesCalls);
        System.out.println("isLegalMove (Pawn) Called: "+Piece.isLegalMovePawn);
        System.out.println("isLegalMove (Knight) Called: "+Piece.isLegalMoveKnight);
        System.out.println("isLegalMove (King) Called: "+Piece.isLegalMoveKing);
        System.out.println("getLegalMovesCalled Called: "+Board.getLegalMovesCalled);
        System.out.println("Copy Constructor Called: "+Board.copyContstructorCalls);
        System.out.println("Times evaluated: "+Engine.timesEvaluated);

        long endTime   = System.nanoTime();
        long totalTime = endTime - startTime;
        System.out.println();
        System.out.println("Runtime: "+totalTime / 1000000000.0+" seconds");
    }
}
