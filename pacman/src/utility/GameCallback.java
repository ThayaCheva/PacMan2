// Team 03
// Rohit Sandeep Ambakkat 1200197
// Thaya Chevaphatrakul 1167144
// Tran Than Han Ha 1472202
package src.utility;

import ch.aplu.jgamegrid.Location;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import src.Monster;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

/**
 * Please do not change this class. This class is used for testing and your code needs to print the correct output to pass the test
 */
public class GameCallback {
    private String logFilePath = "Log.txt";
    private String levelLogPath = "LevelLog.txt";
    private FileWriter fileWriter = null;

    public GameCallback(String file) {
        try {
            if (file.equals("log")) {
                fileWriter = new FileWriter(new File(logFilePath));
            }
            else {
                fileWriter = new FileWriter(new File(levelLogPath));
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void writeString(String str) {
        try {
            fileWriter.write(str);
            fileWriter.write("\n");
            fileWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void endOfGame(String gameResult) {
        writeString(gameResult);
    }

    public void pacManLocationChanged(Location pacmanLocation, int score, int nbPills) {
        String pacmanLocationString = String.format("[PacMan] Location: %d-%d. Score: %d. Pills: %d", pacmanLocation.getX(),
                pacmanLocation.getY(), score, nbPills);
        writeString(pacmanLocationString);
    }

    public void monsterLocationChanged(Monster monster) {
        String monsterLocationString = String.format("[%s] Location: %d-%d", monster.getType(),
                monster.getLocation().getX(), monster.getLocation().getY());
        writeString(monsterLocationString);
    }

    public void pacManEatPillsAndItems(Location pacmanLocation, String type) {
        String pillOrItemLocationString = String.format("[PacMan] Location: %d-%d. Eat Pill/Item: %s", pacmanLocation.getY(),
                pacmanLocation.getY(), type);
        writeString(pillOrItemLocationString);
    }
}
