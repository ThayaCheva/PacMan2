// PacGrid.java
// Team 03
// Rohit Sandeep Ambakkat 1200197
// Thaya Chevaphatrakul 1167144
// Tran Than Han Ha 1472202
package src;

import ch.aplu.jgamegrid.*;
import org.jdom.input.SAXBuilder;
import src.utility.PropertiesLoader;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class PacManGameGrid
{

  private int nbHorzCells;
  private int nbVertCells;
  private int[][] mazeArray;

  public PacManGameGrid(int nbHorzCells, int nbVertCells)
  {
    this.nbHorzCells = nbHorzCells;
    this.nbVertCells = nbVertCells;
    mazeArray = new int[nbVertCells][nbHorzCells];
    String maze =
            "xxxxxxxxxxxxxxxxxxxx" + // 0
            "x....x....g...x....x" + // 1
            "xgxx.x.xxxxxx.x.xx.x" + // 2
            "x.x.......i.g....x.x" + // 3
            "x.x.xx.xx  xx.xx.x.x" + // 4
            "x......x    x......x" + // 5
            "x.x.xx.xxxxxx.xx.x.x" + // 6
            "x.x......gi......x.x" + // 7
            "xixx.x.xxxxxx.x.xx.x" + // 8
            "x...gx....g...x....x" + // 9
            "xxxxxxxxxxxxxxxxxxxx";// 10
  }

  public void convertStruct(String mapFile) {
    String maze = convertXML(mapFile);
    // Copy structure into integer array
    for (int i = 0; i < nbVertCells; i++)
    {
      for (int k = 0; k < nbHorzCells; k++) {
        int value = toInt(maze.charAt(nbHorzCells * i + k));
        mazeArray[i][k] = value;
      }
    }
  }

  public int getCell(Location location)
  {
    return mazeArray[location.y][location.x];
  }
  private int toInt(char c)
  {
    if (c == 'x')
      return 0;
    if (c == '.')
      return 1;
    if (c == ' ')
      return 2;
    if (c == 'g')
      return 3;
    if (c == 'i')
      return 4;
    if (c == 'k')
      return 5;
    if (c == 'l')
      return 6;
    if (c == 'y')
      return 7;
    if (c == 'w')
      return 8;

    return -1;
  }

  // Function to convert .xml to String maze structure
  public String convertXML(String xmlFile) {
    String mapTiles = "";
    try {
      // Load the XML file
      ArrayList<String> map = new ArrayList<>();
      SAXBuilder builder = new SAXBuilder();
      org.jdom.Document document = builder.build(new File(xmlFile));

      // Extract the cells
      org.jdom.Element rootNode = document.getRootElement();

      List rows = rootNode.getChildren("row");
      for (int row = 0; row < rows.size(); row++) {
        org.jdom.Element cellsElem = (org.jdom.Element) rows.get(row);
        List cells = cellsElem.getChildren("cell");
        String rowString = "";
        for (int col = 0; col < cells.size(); col++) {
          org.jdom.Element cell = (org.jdom.Element) cells.get(col);
          String cellType = cell.getText().trim();

          if (cellType.equals("PortalDarkGoldTile")) {
            cellType = "k";
          }
          else if (cellType.equals("PortalWhiteTile")) {
            cellType = "w";
          }
          else if (cellType.equals("PortalYellowTile")) {
            cellType = "y";
          }
          else if (cellType.equals("PortalDarkGrayTile")) {
            cellType = "l";
          }
          else if (cellType.equals("PillTile")) {
            cellType = ".";
          }
          else if (cellType.equals("PathTile")) {
            cellType = " ";
          }
          else if (cellType.equals("WallTile")) {
            cellType = "x";
          }
          else if (cellType.equals("GoldTile")) {
            cellType = "g";
          }
          else if (cellType.equals("IceTile")) {
            cellType = "i";
          }
          else {
            cellType = " ";
          }

          rowString += cellType;
        }
        map.add(rowString);
      }

      // Create the maze
      for (String r : map) {
        mapTiles += r;
      }

    } catch (Exception e) {
      e.printStackTrace();
    }
    return mapTiles;
  }

  // Function to generate a .properties file from a .xml file
  public Properties getProps(String xmlFile, String propertiesFile) {
    try {
      // Load the XML file
      SAXBuilder builder = new SAXBuilder();
      org.jdom.Document document = builder.build(new File(xmlFile));
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
          if (cellType.equals("PortalDarkGoldTile")) {
            cellType = "DarkGoldPortal.location";
          }
          else if (cellType.equals("PortalWhiteTile")) {
            cellType = "WhitePortal.location";
          }
          else if (cellType.equals("PortalYellowTile")) {
            cellType = "YellowPortal.location";
          }
          else if (cellType.equals("PacTile")) {
            cellType = "PacMan.location";
          }
          else if (cellType.equals("PortalDarkGrayTile")) {
            cellType = "DarkGrayPortal.location";
          }
          else if (cellType.equals("PillTile")) {
            cellType = "Pills.location";
          }
          else if (cellType.equals("TX5Tile")) {
            cellType = "TX5.location";
          }
          else if (cellType.equals("TrollTile")) {
            cellType = "Troll.location";
          }
          else if (cellType.equals("GoldTile")) {
            cellType = "Gold.location";
          }
          else if (cellType.equals("IceTile")) {
            cellType = "Ice.location";
          }
          else if (cellType.equals("PacTile")) {
            cellType = "PacMan.location";
          }

          // Add data into properties file
          if (!cellType.equals("PathTile")) {
            String location =  col + "," + row + ";";
            String tileType = properties.getProperty(cellType);
            if (tileType != null) {
              properties.setProperty(cellType, tileType + location);
            } else {
              properties.setProperty(cellType, location);
            }
          }
        }
      }

      // Add all properties key that should be included in the properties but aren't provided by the .xml file
      if (!properties.containsKey("PacMan.move")) {
        properties.setProperty("PacMan.move", "");
      }
      if (!properties.containsKey("Gold.location")) {
        properties.setProperty("Gold.location", "");
      }
      if (!properties.containsKey("Pills.location")) {
        properties.setProperty("Pills.location", "");
      }

      // Save the Properties to a file
      FileOutputStream fos = new FileOutputStream(propertiesFile);
      properties.store(fos, null);
      fos.close();

      // Remove the ";" from the end of each line in the properties file
      try {
        Path writePath = Paths.get(propertiesFile);
        List<String> lines = Files.readAllLines(writePath);
        List<String> newLine = new ArrayList<>();

        for (String l : lines) {
            newLine.add(l.replaceAll(";" + "+$", ""));
        }

        Files.write(writePath, newLine);
      } catch (IOException e) {
        e.printStackTrace();
      }

      PropertiesLoader propertiesLoader = new PropertiesLoader();
      return propertiesLoader.loadPropertiesFile(propertiesFile);

    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }
}


