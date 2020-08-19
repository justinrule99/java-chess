import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

public class ChessGUI extends JPanel implements MouseListener, KeyListener {

    private final boolean SHOW_IMAGE_BACKGROUND = true;

    private Board board;
    private Image image;
    private Square squareSelected;
    private ArrayList<Move> legalMoves;

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
            g.fillRect((100*board.codeToFile(m.getDest()))-60,840-(100*board.codeToRank(m.getDest())),20,20);
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

        // will move
        if (squareSelected != null) {
            if (legalMoves.contains(new Move(squareSelected.toString(), code))) {
                board.move(squareSelected.toString(), code);

                Move opponentBest = Engine.getBestMove(board, 3, false);
                board.move(opponentBest.getSrc(), opponentBest.getDest());
                System.out.println(board.getMoveHistory());

                legalMoves.clear();
                squareSelected = null;
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
    @Override
    public void keyPressed(KeyEvent keyEvent) {
        // quit if key q
        if (keyEvent.getKeyChar() == 'q') System.exit(0);


        Move best = Engine.getBestMove(board, 3, whiteTurn);
        board.move(best.getSrc(), best.getDest());
        this.repaint();
        whiteTurn = !whiteTurn;

    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {

    }
}
