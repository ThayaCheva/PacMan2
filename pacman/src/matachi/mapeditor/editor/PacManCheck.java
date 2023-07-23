// Team 03
// Rohit Sandeep Ambakkat 1200197
// Thaya Chevaphatrakul 1167144
// Tran Than Han Ha 1472202
package src.matachi.mapeditor.editor;

import ch.aplu.jgamegrid.Location;
import org.jdom.input.SAXBuilder;
import src.utility.GameCallback;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PacManCheck extends CompositeLevelCheck {
    public boolean levelCheck(File xmlFile, GameCallback gameCallback) {
        try {
            ArrayList<Location> PacStart = new ArrayList<>();

            // Load the XML file
            SAXBuilder builder = new SAXBuilder();
            org.jdom.Document document = builder.build(xmlFile);

            // Extract the cells
            org.jdom.Element rootNode = document.getRootElement();

            List rows = rootNode.getChildren("row");
            for (int row = 0; row < rows.size(); row++) {
                org.jdom.Element cellsElem = (org.jdom.Element) rows.get(row);
                List cells = cellsElem.getChildren("cell");

                for (int col = 0; col < cells.size(); col++) {
                    org.jdom.Element cell = (org.jdom.Element) cells.get(col);
                    String cellType = cell.getText().trim();
                    Location location =  new Location(col, row);
                    if (cellType.equals("PacTile")) {
                        PacStart.add(location);
                    }

                }
            }

            if (PacStart.size() == 0) {
                String levelCheck = String.format("[%s - no start for PacMan]", xmlFile.getName());
                gameCallback.writeString(levelCheck);
                return false;
            }
            if (PacStart.size() > 1) {
                String levelCheck = String.format("[%s - more than one start for Pacman: ", xmlFile.getName());
                for (int i = 0; i < PacStart.size(); i++) {
                    if (i == PacStart.size() - 1) {
                        levelCheck += String.format("%s]", PacStart.get(i));
                    }
                    else {
                        levelCheck += String.format("%s; ", PacStart.get(i));
                    }

                }
                gameCallback.writeString(levelCheck);
                return false;
            }
            else{
                return true;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
