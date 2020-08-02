import java.util.Scanner;

public class RunEngine {

    // Command line interface for the chess engine
    public static void main(String[] args) {

        String moveSrc = "";
        String moveDest = "";
        Scanner sc = new Scanner(System.in);
        Board board = new Board();
        // will have src and dest (how to do alg notation to automatically find?)
        // types of alg notation: Rxd4+ : piece, captures, square, check

        System.out.println(board);

        // get all legal moves for a position

        while (true) {
            System.out.println("Enter a move..");

            moveSrc = sc.next();
            if (moveSrc.equals("exit")) break;

            board.getLegalMoves(moveSrc);
            // also get piece on src
            Piece srcPiece = board.getSquare(moveSrc).getCurrentPiece();
            if (srcPiece != null) {
                System.out.println("Piece at src: (before process): "+srcPiece+", iswhite: "+srcPiece.isWhite());
            } else {
                System.out.println("Piece Here (before process) is NULL");
            }

            moveDest = sc.next();


            board.move(moveSrc, moveDest);
            // null at moveDest (e4)
            Piece destPiece = board.getSquare(moveDest).getCurrentPiece();
            if (destPiece != null) {
                System.out.println("Piece at dest: (after process): "+destPiece+", iswhite: "+destPiece.isWhite());
            } else {
                System.out.println("Piece Here (after process) is NULL");
            }

            System.out.println(board);
            System.out.println(Engine.evaluate(board));


            // calculate
            System.out.println("Black plays \"\"");
        }
    }
}


