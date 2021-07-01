import java.io.*;
import java.util.*;

// basic opening book using openings.txt
// tree traversal on thousands of master games

public class OpeningBook {


    public OpeningBook() {

    }


    public static Move getMoveOpening(Board b) {
        // traverse tree here to find best move
        ArrayList<Move> history = b.getMoveHistory();
        StringBuilder sb = new StringBuilder();

        String lastMove = "";
        for (int i = 0; i < history.size(); i++) {
            if (i == history.size()-1) {
                lastMove = history.get(i).toStringUCI();
            }
            sb.append(history.get(i).toStringUCI());
            sb.append(" ");
        }


        ArrayList<String> games = getGamesFromPosition(sb.toString());

        if (games.isEmpty()) return null;

        int moveIdx = (int) (Math.random() * (games.size()));
        String selectedGame = games.get(moveIdx);
        System.out.println("selected game:");
        System.out.println(selectedGame);

        int lastMoveStartIdx = selectedGame.indexOf(lastMove);
        if (b.getMoveNumber() == 0) {
            return new Move("e2", "e4");
        }
        return new Move(selectedGame.substring(lastMoveStartIdx+5,lastMoveStartIdx+7), selectedGame.substring(lastMoveStartIdx+7,lastMoveStartIdx+9));
    }

    // opening: a string of moves played so far (getMoveHistory() without spaces)
    // find match
    // inefficient, this traverses all games each time, doesn't remove
    // should only look at certain, pre-approved games from prev moves
    public static ArrayList<String> getGamesFromPosition(String opening) {
        // use reader to loop thru lines, only act if start matches opening

        String bookFile = "nakacleaned.txt";
        ArrayList<String> games = new ArrayList<>();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(bookFile));

            String line;
            while ((line = reader.readLine()) != null) {
                if (line.indexOf(opening) == 0) {
                    games.add(line);
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        return games;
    }

    // trim [] game data from UCI games in txt file
    public static void cleanUciInput() {
        String filename = "naka.txt";
        String tempFilename = "nakacleaned.txt";

        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFilename));

            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.contains("[") && !line.contains("]")) {
                    System.out.println(line);
                    writer.write(line+"\n");
                }
            }
            reader.close();
            writer.close();
        } catch(Exception e) {
            e.printStackTrace();
        }

    }

    // won't sort, none of the lines will be the same
    public static void sortUciInput() {
        // sort a line from a file
        String bookFile = "temp.txt";
        ArrayList<String> allGames = new ArrayList<>();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(bookFile));

            String line;
            while ((line = reader.readLine()) != null) {
                allGames.add(line);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        Collections.sort(allGames);
        for (String game : allGames) {
            System.out.println(game);
            System.out.println();
        }
    }

}
