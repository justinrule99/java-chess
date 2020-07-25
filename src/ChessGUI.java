import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

public class ChessGUI extends JPanel implements MouseListener {

    private Board board;
    private Image image;
    private Square squareSelected;
    private ArrayList<String> legalMoves;

    public ChessGUI(Board board) {
        super();
        this.board = board;
        //            image = ImageIO.read(new File("images/Chess_bdt60.png"));
        legalMoves = new ArrayList<>();
        board = new Board();
        addMouseListener(this);


    }

    // since array starts a1 at upper left, gui always shows white on top


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

        // draw image at correct spot (change with each move)
        // fill images sqaures with images (will updated according to game object on change)
        // board object already has locations, transfer to images
        // i = file, j = rank
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                Piece currentPiece = board.getSquare(i,j).getCurrentPiece();
                if (currentPiece == null) continue;
                image = new ImageIcon(currentPiece.getImageName()).getImage();
                g.drawImage(image, (100*i)-100, (100*j)-100, this);
            }
        }
    }

    // mouse listener: convert xy mouse event to alg notation, then show possible moves with that piece (if a piece)

    // GUI for the chess engine
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setSize(800,830);
        frame.getContentPane().add(new ChessGUI(new Board()));
        frame.setBackground(new Color(51,204,51));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

    }


    // can't move same piece twice in a row?
    // doesn't properly update board object?
    @Override
    public void mouseClicked(MouseEvent e) {
        int file = (e.getX() / 100)+1;
        int rank = ((e.getY() / 100)+1);

        String code = board.fileAndRankToCode(rank, file);
        // everything currently flipped

        if (legalMoves.contains(code)) {
            // convert a square object to an alg code?
            // src code is e7 instead of e2 ??
            System.out.println("SRC CODE:  "+squareSelected);
            System.out.println("DEST CODE: "+code);
            // might not be changing actual board object
            board.move(squareSelected.toString(), code);
            // check if piece on e4 now
            // visually rank flipped
            // numbers flipped on algsquare (either process or algsquare -> arr)
            Piece afterMove = board.getSquare("e4").getCurrentPiece();
            if (afterMove == null) {
                System.out.println("NOT PIECE AFTER MOVE on e4");
            } else System.out.println("paice her");

            legalMoves.clear();
            squareSelected = null;
            this.repaint();
            return;
        }

        System.out.println("FILE: "+file);
        System.out.println("RANK:"+rank);
        String algSquare = board.fileAndRankToCode(rank, file);
        System.out.println("ALGSQUARE: "+algSquare);
        squareSelected = board.getSquare(algSquare);
        // board does not update? chessgui using wrong board object
        Piece pieceAtClick = board.getSquare(algSquare).getCurrentPiece();
        if (pieceAtClick != null) {
            System.out.println("piece clicked on:"+pieceAtClick);
            // ISWHITE IS WRONG FOR EVERYTHING
            System.out.println("iswhite: "+pieceAtClick.isWhite());
            // paint sqaures of possible moves
            // file and rank are backwards??
            legalMoves = board.getLegalMoves(algSquare);
            // set state: current piece highlighted, listening for legal move, then execute
        } else {
            System.out.println("No piece here.. (from gui top level)");
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
}
