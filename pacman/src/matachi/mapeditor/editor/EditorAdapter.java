// Team 03
// Rohit Sandeep Ambakkat 1200197
// Thaya Chevaphatrakul 1167144
// Tran Than Han Ha 1472202
package src.matachi.mapeditor.editor;

import src.ProgramAdapter;

import javax.swing.*;
import java.io.File;
import java.util.Properties;

public class EditorAdapter extends SwingWorker<Controller, Void> implements ProgramAdapter {
    private File map;
    Properties properties;
    @Override
    protected Controller doInBackground() throws Exception {
        Controller controller;
        if (map==null){
            controller=new Controller();
            controller.setGameProperties(properties);
        }
        else{
            controller=new Controller(map);
            controller.setGameProperties(properties);
        }
        return controller;
    }

    @Override
    public void loadMap(File map, Properties propertiesFile) throws Exception {
        this.map=map;
        this.properties=propertiesFile;
        this.execute();
        this.done();


    }

}