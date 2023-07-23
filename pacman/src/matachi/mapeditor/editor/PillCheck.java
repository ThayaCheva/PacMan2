// Team 03
// Rohit Sandeep Ambakkat 1200197
// Thaya Chevaphatrakul 1167144
// Tran Than Han Ha 1472202
package src.matachi.mapeditor.editor;

import ch.aplu.jgamegrid.Location;
import org.jdom.input.SAXBuilder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


import src.utility.GameCallback;

public class PillCheck implements LevelCheck{
    public boolean levelCheck(File xmlFile, GameCallback gameCallback) {
        try {
            ArrayList<Location> Gold = new ArrayList<>();
            ArrayList<Location> Pill = new ArrayList<>();
            String paths = xmlFile.toString().replace("\\", "/");

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
                    if (cellType.equals("PillTile")) {
                        Pill.add(location);
                    }
                    else if (cellType.equals("GoldTile")) {
                        Gold.add(location);
                    }
                }
            }

            if (Gold.size() < 2 || Pill.size() < 2) {
                String levelCheck = String.format("[%s - less than 2 Gold and Pill]", xmlFile.getName());
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
