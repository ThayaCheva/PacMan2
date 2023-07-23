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

public class PortalCheck implements LevelCheck {
    public boolean levelCheck(File xmlFile, GameCallback gameCallback) {
        try {
            ArrayList<Location> GoldPortal = new ArrayList<>();
            ArrayList<Location> WPortal = new ArrayList<>();
            ArrayList<Location> GreyPortal = new ArrayList<>();
            ArrayList<Location> YPortal = new ArrayList<>();

            // Load the XML file
            boolean check;
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
                    if (cellType.equals("PortalDarkGoldTile")) {
                        GoldPortal.add(location);
                    }
                    else if (cellType.equals("PortalWhiteTile")) {
                        WPortal.add(location);
                    }
                    else if (cellType.equals("PortalYellowTile")) {
                        YPortal.add(location);
                    }
                    else if (cellType.equals("PortalDarkGrayTile")) {
                        GreyPortal.add(location);
                    }
                }
            }

            check = checkItem(YPortal, "Yellow", xmlFile.getName(), gameCallback);
            if (check == false) {
                return false;
            }
            check = checkItem(WPortal, "White", xmlFile.getName(), gameCallback);
            if (check == false) {
                return false;
            }
            check = checkItem(GoldPortal, "Dark Gold", xmlFile.getName(), gameCallback);
            if (check == false) {
                return false;
            }
            check = checkItem(GreyPortal, "Dark Grey", xmlFile.getName(), gameCallback);
            if (check == false) {
                return false;
            }
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean checkItem(ArrayList<Location> portal, String name, String xmlFile, GameCallback gameCallback) {
        if (portal.size() != 2) {
            String levelCheck = String.format("[%s - portal %s count is not 2: ", xmlFile, name);
            if(portal.size() == 0){
                levelCheck = String.format("[%s - portal %s count is not 2]", xmlFile, name);
                gameCallback.writeString(levelCheck);
                return true;
            }
            for (int i = 0; i < portal.size(); i++) {
                if (i == portal.size() - 1) {
                    levelCheck += String.format("%s]", portal.get(i));
                }
                else {
                    levelCheck += String.format("%s; ", portal.get(i));
                }
            }
            gameCallback.writeString(levelCheck);
            return false;
        }
        else{
            return true;
        }
    }

}
