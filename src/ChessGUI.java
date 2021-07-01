import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/* TODO
    Bugs: Moving INTO Check might be allowed
    Checkmate
    FEN Support
    Speed
    ----
    Checkmate Patterns
    Faster Evaluation
 */


public class ChessGUI extends JPanel implements MouseListener, KeyListener {

    private final boolean SHOW_IMAGE_BACKGROUND = true;

    private Board board;
    private Image image;
    private Square squareSelected;
    private ArrayList<Move> legalMoves;
    private boolean analysisMode = false;

    public ChessGUI(Board board) {
        super();
        this.board = board;
        // might not be updated always (would autowire?)
        legalMoves = new ArrayList<>();
        addMouseListener(this);
        addKeyListener(this);
    }

    // adds focus to JFrame so KeyListener works
    public void addNotify() {
        super.addNotify();
        requestFocus();
    }

    @Override
    public void paintComponent(Graphics g){
        // doesnt redraw pictures on move
        g.setColor(new Color(20,82,20));
        g.fillRect(0, 0, 800, 800);
        for(int i = 0; i <= 800; i+=200){
            for(int j = 0; j <= 800; j+=200){
                g.clearRect(i, j, 100, 100);
            }
        }

        for(int i = 100; i <= 900; i+=200){
            for(int j = 100; j <= 900; j+=200){
                g.clearRect(i, j, 100, 100);
            }
        }

        Image background = new ImageIcon("images/wooden_chessboard.png").getImage();
        if (SHOW_IMAGE_BACKGROUND) {
            g.drawImage(background, 0, 0, this);
        }


        // draw image at correct spot (change with each move)
        // i = file, j = rank
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                Piece currentPiece = board.getSquare(i,j).getCurrentPiece();
                if (currentPiece == null) continue;
                image = new ImageIcon(currentPiece.getImageName()).getImage();
                // 9-i to flip natural rendering over y axis
                g.drawImage(image, (100*(i))-100, (100*(9-j))-100, this);
            }
        }

        if (SHOW_IMAGE_BACKGROUND) {
            g.setColor(Color.BLACK);
        } else {
            g.setColor(new Color(2,20,200));
        }
        for (Move m : legalMoves) {
            // rank*100, file*100, 100,100
            g.fillRect((100*board.codeToFile(m.getDest()))-55,840-(100*board.codeToRank(m.getDest())),20,20);
        }
    }

    // GUI for the chess engine
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setSize(800,830);
        frame.getContentPane().add(new ChessGUI(new Board()));


        frame.setBackground(new Color(51,204,51));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int file = (e.getX() / 100)+1;
        int rank = 9-((e.getY() / 100)+1);

        // acts as dest
        String code = board.rankAndFileToCode(rank, file);

        if (squareSelected != null) {
            if (legalMoves.contains(new Move(squareSelected.toString(), code))) {
                board.move(new Move(squareSelected.toString(), code));


//                Move opponentBest = Engine.getBestMove(board, 2, false);
//                if (opponentBest == null) {
//                    System.out.println("Checkmate: White Wins");
//                }
//                board.move(new Move(opponentBest.getSrc(), opponentBest.getDest()));

//                System.out.println(board.getMoveHistory());

                legalMoves.clear();
                squareSelected = null;
                whiteTurn = !whiteTurn;
                this.repaint();
                return;
            }
        }

        // clicking a piece to move on next click
        String algSquare = board.rankAndFileToCode(rank, file);
        squareSelected = board.getSquare(algSquare);

        Piece pieceAtClick = board.getSquare(algSquare).getCurrentPiece();

        if (pieceAtClick != null) {
            legalMoves = board.getLegalMoves(algSquare);
            this.repaint();
        } else {
            System.out.println("No piece here.. (from gui top level)");
            legalMoves.clear();
            squareSelected = null;
        }
    }


    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void keyTyped(KeyEvent keyEvent) {


    }

    private boolean whiteTurn = true;
    private String[] fileGame;
    private int fileMoveIdx = 0;

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        // quit if key q
        if (keyEvent.getKeyChar() == 'q') System.exit(0);

        // goto analysis mode
        if (keyEvent.getKeyChar() == 'a') {

            board = new Board();
            analysisMode = true;
            try {
                Scanner sc = new Scanner(new File("game.txt"));
                fileGame = sc.nextLine().split(" ");

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            this.repaint();
            return;
        }

        if (keyEvent.getKeyChar() == 'n' && analysisMode) {
            // next move in sequence
//            System.out.println(fileGame[fileMoveIdx].substring(0,1));
            board.move(new Move(fileGame[fileMoveIdx].substring(0,2), fileGame[fileMoveIdx++].substring(2,4)));
            this.repaint();
            return;
        }


        if (keyEvent.getKeyChar() == 'c') {
            System.out.println("white turn: "+whiteTurn);
            System.out.println("check turn: "+board.inCheck(false));

            System.out.println(board.getMoveHistory());
            System.out.println(board.hashCode());
            return;
        }

        if (keyEvent.getKeyChar() == 'p') {
            System.out.println(board);
            System.out.println(Engine.evaluate(board));
            System.out.println(Engine.evaluate2(board));
            return;
        }

        if (keyEvent.getKeyChar() == 'b') {
            System.out.println(Engine.getBestMove(board, 2, whiteTurn));
            return;
        }


        Move best = Engine.getBestMove(board, 2, whiteTurn);
        board.move(best);
        this.repaint();
        whiteTurn = !whiteTurn;
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {

    }
}
