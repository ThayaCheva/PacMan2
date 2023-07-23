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
import java.util.Properties;

public class GoldCheck implements LevelCheck {
    public boolean levelCheck(File xmlFile, GameCallback gameCallback) {
        try {
            ArrayList<Location> Gold = new ArrayList<>();
            ArrayList<Location> Wall = new ArrayList<>();
            String paths = xmlFile.toString().replace("\\", "/");
            // Load the XML file
            SAXBuilder builder = new SAXBuilder();
            org.jdom.Document document = (org.jdom.Document) builder.build(xmlFile);
            Properties properties = new Properties();

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
                    if (cellType.equals("GoldTile")) {
                        Gold.add(location);
                    }
                    else if (cellType.equals("WallTile")) {
                        Wall.add(location);
                    }

                }
            }

            int count = 0;
            String levelCheck = "";
            for (int i = 0; i < Gold.size(); i++) {
                if (Wall.contains(Gold.get(i))) {
                    if (count == 0) {
                        levelCheck += String.format("[%s - Gold not accessible: (4,7); (4,8)", xmlFile.getName());
                    }
                    if (i == Gold.size() - 1) {
                        levelCheck += String.format("%s]", Gold.get(i));

                    }
                    else {
                        levelCheck += String.format("%s; ", Gold.get(i));
                    }
                }
            }
            gameCallback.writeString(levelCheck);
            if(levelCheck.length()==0){
                return true;
            }
            else{
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


}
