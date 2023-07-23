// Team 03
// Rohit Sandeep Ambakkat 1200197
// Thaya Chevaphatrakul 1167144
// Tran Than Han Ha 1472202
package src;

import src.matachi.mapeditor.editor.Controller;
import src.utility.GameCallback;
import src.utility.PropertiesLoader;

import javax.swing.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Properties;

public class GameAdapter extends SwingWorker<Game, Void> implements ProgramAdapter {
    private ArrayList <File> maps = new ArrayList<File>();
    private Properties properties;

    // Open the map with the editor
    public void loadMap(File map, Properties properties) throws Exception {
        maps.add(map);
        this.properties=properties;
        this.execute();
        this.done();

    }

    @Override
    protected Game doInBackground() throws Exception{
        GameCallback gameCallback = new GameCallback("log");
        Game game = new Game(gameCallback,properties,maps);
        return game;
    }

}
