// PacMan.java
// Simple PacMan implementation
// Team 03
// Rohit Sandeep Ambakkat 1200197
// Thaya Chevaphatrakul 1167144
// Tran Than Han Ha 1472202
package src;

import ch.aplu.jgamegrid.*;
import src.matachi.mapeditor.editor.EditorAdapter;
import src.utility.GameCallback;
import src.utility.Portals;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Properties;

public class Game extends GameGrid
{
  public static final String DEFAULT_PROPERTIES_PATH = "src/Game/map_prop.properties";
  private final static int nbHorzCells = 20;
  private final static int nbVertCells = 11;

  protected PacActor pacActor = new PacActor(this);
  private Monster troll = new Monster(this, MonsterType.Troll);
  private Monster tx5 = new Monster(this, MonsterType.TX5);

  private ArrayList<Location> pillAndItemLocations = new ArrayList<Location>();
  private ArrayList<Actor> iceCubes = new ArrayList<Actor>();
  private ArrayList<Actor> goldPieces = new ArrayList<Actor>();
  private GameCallback gameCallback;
  private Properties properties;
  private int seed = 30006;

  private ArrayList<Location> propertyPillLocations = new ArrayList<>();
  private ArrayList<Location> propertyGoldLocations = new ArrayList<>();

  private Portals portals;
  private ArrayList<ArrayList<Location>> allPortals = new ArrayList<>();

  private ArrayList<File> mapFiles;
  protected PacManGameGrid grid;

  public Properties getProperties() {
    return properties;
  }

  public Game(GameCallback gameCallback, Properties properties, ArrayList<File> mapFiles) throws Exception {
    //Setup game
    super(nbHorzCells, nbVertCells, 20, false);
    int map_played = 0;
    grid = new PacManGameGrid(nbHorzCells, nbVertCells);
    this.gameCallback = gameCallback;
    this.properties = properties;
    this.mapFiles = mapFiles;
    setSimulationPeriod(100);
    setTitle("[PacMan in the Multiverse]");
    // Setup for auto test
    pacActor.setPropertyMoves(properties.getProperty("PacMan.move"));
    pacActor.setAuto(Boolean.parseBoolean(properties.getProperty("PacMan.isAuto")));

    GGBackground bg = getBg();

    // Setup Random seeds
    seed = Integer.parseInt(properties.getProperty("seed"));
    pacActor.setSeed(seed);
    troll.setSeed(seed);
    tx5.setSeed(seed);
    addKeyRepeatListener(pacActor.getRegularMove());
    setKeyRepeatPeriod(150);
    troll.setSlowDown(3);
    tx5.setSlowDown(3);
    pacActor.setSlowDown(3);

    // Create starting map
    generateNewMap(map_played);

    //Run the game
    doRun();
    show();

    // Loop to look for collision in the application thread
    // This makes it improbable that we miss a hit
    boolean hasPacmanBeenHit;
    boolean hasPacmanEatAllPills;
    setupPillAndItemsLocations();
    int maxPillsAndItems = countPillsAndItems();

    do {
      hasPacmanBeenHit = troll.getLocation().equals(pacActor.getLocation()) ||
              tx5.getLocation().equals(pacActor.getLocation());
      hasPacmanEatAllPills = pacActor.getNbPills() >= maxPillsAndItems;

      // Change map when pacman eats all the pills on that current map
      if (hasPacmanEatAllPills) {
        map_played++;
        generateNewMap(map_played);
        maxPillsAndItems = countPillsAndItems();
      }
      delay(10);

    } while(!hasPacmanBeenHit && map_played < mapFiles.size());
    delay(120);

    Location loc = pacActor.getLocation();
    troll.setStopMoving(true);
    tx5.setStopMoving(true);
    pacActor.removeSelf();

    String title = "";
    if (hasPacmanBeenHit) {
      bg.setPaintColor(Color.red);
      title = "GAME OVER";
      addActor(new Actor("sprites/explosion3.gif"), loc);
    }

    // When the game ends, close the game and open editor
    else if (hasPacmanEatAllPills) {
      if(mapFiles.size() == 1) {
        ProgramAdapter adapter = new EditorAdapter();
        adapter.loadMap(mapFiles.get(0),properties);
      }
      else {
        ProgramAdapter adapter= new EditorAdapter();
        adapter.loadMap(null,properties);
      }
      hide();
    }

    setTitle(title);
    gameCallback.endOfGame(title);

    doPause();
  }

  public GameCallback getGameCallback() {
    return gameCallback;
  }

  // Generate a new map until all maps in the folder has been played
  private void generateNewMap(int map_played) {
    if (map_played < mapFiles.size()) {
      removeAllActors();
      propertyPillLocations.clear();
      propertyGoldLocations.clear();
      allPortals.clear();
      pacActor.setNbPills(0);
      this.properties = grid.getProps(mapFiles.get(map_played).toString(), DEFAULT_PROPERTIES_PATH);
      grid.convertStruct(mapFiles.get(map_played).toString());
      loadPillAndItemsLocations();
      loadPortalsLocations();
      GGBackground bg = getBg();
      drawGrid(bg);
      portals = new Portals(allPortals);
      portals.portal.subscribe(pacActor);
      portals.portal.subscribe(tx5);
      portals.portal.subscribe(troll);
      tx5.stopMoving(5);
      setupActorLocations();
    }
  }

  private void setupActorLocations() {
    String[] trollLocations = this.properties.getProperty("Troll.location").split(",");
    String[] tx5Locations = this.properties.getProperty("TX5.location").split(",");
    String[] pacManLocations = this.properties.getProperty("PacMan.location").split(",");
    int trollX = Integer.parseInt(trollLocations[0]);
    int trollY = Integer.parseInt(trollLocations[1]);

    int tx5X = Integer.parseInt(tx5Locations[0]);
    int tx5Y = Integer.parseInt(tx5Locations[1]);

    int pacManX = Integer.parseInt(pacManLocations[0]);
    int pacManY = Integer.parseInt(pacManLocations[1]);
    addActor(troll, new Location(trollX, trollY), Location.NORTH);
    addActor(pacActor, new Location(pacManX, pacManY));
    addActor(tx5, new Location(tx5X, tx5Y), Location.NORTH);
    troll.show();
    pacActor.show();
    tx5.show();
    doRun();
    refresh();
  }

  public Portals getPortal() {
    return portals;
  }

  private int countPillsAndItems() {
    int pillsAndItemsCount = 0;
    for (int y = 0; y < nbVertCells; y++)
    {
      for (int x = 0; x < nbHorzCells; x++)
      {
        Location location = new Location(x, y);
        int a = grid.getCell(location);
        if (a == 1 && propertyPillLocations.size() == 0) { // Pill
          pillsAndItemsCount++;
        } else if (a == 3 && propertyGoldLocations.size() == 0) { // Gold
          pillsAndItemsCount++;
        }
      }
    }
    if (propertyPillLocations.size() != 0) {
      pillsAndItemsCount += propertyPillLocations.size();
    }

    if (propertyGoldLocations.size() != 0) {
      pillsAndItemsCount += propertyGoldLocations.size();
    }

    return pillsAndItemsCount;
  }

  public ArrayList<Location> getPillAndItemLocations() {
    return pillAndItemLocations;
  }

  // Function to load all Portal locations from properties file
  private void loadPortalsLocations() {
    ArrayList<Location> DarkGoldPortalLocations = new ArrayList<>();
    ArrayList<Location> DarkGrayPortalLocations = new ArrayList<>();
    ArrayList<Location> WhitePortalLocations = new ArrayList<>();
    ArrayList<Location> YellowPortalLocations = new ArrayList<>();

    String GoldPortalLocationsString = properties.getProperty("DarkGoldPortal.location");
    if (GoldPortalLocationsString != null) {
      String[] singleGoldPortalLocation = GoldPortalLocationsString.split(";");
      for (String portalLocation: singleGoldPortalLocation) {
        String[] locationStrings = portalLocation.split(",");
        DarkGoldPortalLocations.add(new Location(Integer.parseInt(locationStrings[0]), Integer.parseInt(locationStrings[1])));
      }
    }

    String WhitePortalLocationsString = properties.getProperty("WhitePortal.location");
    if (WhitePortalLocationsString != null) {
      String[] singleWhitePortalLocation = WhitePortalLocationsString.split(";");
      for (String portalLocation: singleWhitePortalLocation) {
        String[] locationStrings = portalLocation.split(",");
        WhitePortalLocations.add(new Location(Integer.parseInt(locationStrings[0]), Integer.parseInt(locationStrings[1])));
      }
    }

    String GrayPortalLocationsString = properties.getProperty("DarkGrayPortal.location");
    if (GrayPortalLocationsString != null) {
      String[] singleGrayPortalLocation = GrayPortalLocationsString.split(";");
      for (String portalLocation: singleGrayPortalLocation) {
        String[] locationStrings = portalLocation.split(",");
        DarkGrayPortalLocations.add(new Location(Integer.parseInt(locationStrings[0]), Integer.parseInt(locationStrings[1])));
      }
    }

    String YellowPortalLocationsString = properties.getProperty("YellowPortal.location");
    if (YellowPortalLocationsString != null) {
      String[] singleYellowPortalLocation = YellowPortalLocationsString.split(";");
      for (String portalLocation: singleYellowPortalLocation) {
        String[] locationStrings = portalLocation.split(",");
        YellowPortalLocations.add(new Location(Integer.parseInt(locationStrings[0]), Integer.parseInt(locationStrings[1])));
      }
    }

    allPortals.add(WhitePortalLocations);
    allPortals.add(DarkGoldPortalLocations);
    allPortals.add(DarkGrayPortalLocations);
    allPortals.add(YellowPortalLocations);
  }

  private void loadPillAndItemsLocations() {
    String pillsLocationString = properties.getProperty("Pills.location");
    if (pillsLocationString != null) {
      String[] singlePillLocationStrings = pillsLocationString.split(";");
      for (String singlePillLocationString: singlePillLocationStrings) {
        String[] locationStrings = singlePillLocationString.split(",");
        propertyPillLocations.add(new Location(Integer.parseInt(locationStrings[0]), Integer.parseInt(locationStrings[1])));
      }
    }

    String goldLocationString = properties.getProperty("Gold.location");
    if (goldLocationString != null) {
      String[] singleGoldLocationStrings = goldLocationString.split(";");
      for (String singleGoldLocationString: singleGoldLocationStrings) {
        String[] locationStrings = singleGoldLocationString.split(",");
        propertyGoldLocations.add(new Location(Integer.parseInt(locationStrings[0]), Integer.parseInt(locationStrings[1])));
      }
    }
  }

  private void setupPillAndItemsLocations() {
    for (int y = 0; y < nbVertCells; y++)
    {
      for (int x = 0; x < nbHorzCells; x++)
      {
        Location location = new Location(x, y);
        int a = grid.getCell(location);
        if (a == 1 && propertyPillLocations.size() == 0) {
          pillAndItemLocations.add(location);
        }
        if (a == 3 &&  propertyGoldLocations.size() == 0) {
          pillAndItemLocations.add(location);
        }
        if (a == 4) {
          pillAndItemLocations.add(location);
        }
      }
    }


    if (propertyPillLocations.size() > 0) {
      for (Location location : propertyPillLocations) {
        pillAndItemLocations.add(location);
      }
    }
    if (propertyGoldLocations.size() > 0) {
      for (Location location : propertyGoldLocations) {
        pillAndItemLocations.add(location);
      }
    }
  }

  private void drawGrid(GGBackground bg)
  {
    bg.clear(Color.gray);
    bg.setPaintColor(Color.white);
    for (int y = 0; y < nbVertCells; y++)
    {
      for (int x = 0; x < nbHorzCells; x++)
      {
        bg.setPaintColor(Color.white);
        Location location = new Location(x, y);
        int a = grid.getCell(location);
        if (a > 0)
          bg.fillCell(location, Color.lightGray);
        if (a == 1 && propertyPillLocations.size() == 0) { // Pill
          putPill(bg, location);
        } else if (a == 3 && propertyGoldLocations.size() == 0) { // Gold
          putGold(bg, location);
        } else if (a == 4) {
          putIce(bg, location);
        } else if (a == 5) {
          addActor(new Actor("sprites/portalDarkGold.png"), location);
        } else if (a == 6) {
          addActor(new Actor("sprites/portalDarkGray.png"), location);
        } else if (a == 7) {
          addActor(new Actor("sprites/portalYellow.png"), location);
        } else if (a == 8) {
          addActor(new Actor("sprites/portalWhite.png"), location);
        }
      }
    }

    for (Location location : propertyPillLocations) {
      putPill(bg, location);
    }

    for (Location location : propertyGoldLocations) {
      putGold(bg, location);
    }
  }

  private void putPill(GGBackground bg, Location location){
    bg.fillCircle(toPoint(location), 5);
  }

  private void putGold(GGBackground bg, Location location){
    bg.setPaintColor(Color.yellow);
    bg.fillCircle(toPoint(location), 5);
    Actor gold = new Actor("sprites/gold.png");
    this.goldPieces.add(gold);
    addActor(gold, location);
  }

  private void putIce(GGBackground bg, Location location){
    bg.setPaintColor(Color.blue);
    bg.fillCircle(toPoint(location), 5);
    Actor ice = new Actor("sprites/ice.png");
    this.iceCubes.add(ice);
    addActor(ice, location);
  }

  public void removeItem(String type,Location location) {
    if(type.equals("gold")){
      for (Actor item : this.goldPieces){
        if (location.getX() == item.getLocation().getX() && location.getY() == item.getLocation().getY()) {
          item.hide();
        }
      }
    }else if(type.equals("ice")){
      for (Actor item : this.iceCubes){
        if (location.getX() == item.getLocation().getX() && location.getY() == item.getLocation().getY()) {
          item.hide();
        }
      }
    }
  }

  public int getNumHorzCells(){
    return this.nbHorzCells;
  }
  public int getNumVertCells(){
    return this.nbVertCells;
  }
}
