// Team 03
// Rohit Sandeep Ambakkat 1200197
// Thaya Chevaphatrakul 1167144
// Tran Than Han Ha 1472202

package src;

import ch.aplu.jgamegrid.GameGrid;
import src.matachi.mapeditor.editor.LevelCheckFacade;
import src.utility.GameCallback;
import src.utility.PropertiesLoader;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Properties;

import src.matachi.mapeditor.editor.Controller;

public class EditorTesterFactory {

    public void createGameGridObject(String args[], String propertiesPath) throws Exception {
        if(args.length < 1){
            Controller controller=new Controller();
            Properties properties=PropertiesLoader.loadPropertiesFile(propertiesPath);
            controller.setGameProperties(properties);
            return;
        }
        File argument= new File(args[0]);
        if(argument.isDirectory()){

            boolean check;
            GameCallback gameCallback = new GameCallback("level");
            GameCheck gameCheck = new GameCheck(args[0],gameCallback);
            check=gameCheck.performGameCheck();
            if(check==false){
                Controller controller=new Controller();
                Properties properties=PropertiesLoader.loadPropertiesFile(propertiesPath);
                controller.setGameProperties(properties);
                gameCheck.performGameCheck();
                return;
            }
            File[] contents=argument.listFiles();
            ArrayList<File> mapFiles = new ArrayList<File>();
            for(int i=0; i<contents.length;i++){
                File currFile=contents[i];
                if(currFile.getName().endsWith(".xml") && Character.isDigit(currFile.getName().charAt(0))){
                    mapFiles.add(currFile);
                }
            }
            Collections.sort(mapFiles);
            LevelCheckFacade checkLevel = new LevelCheckFacade(mapFiles);
            File errorFile=checkLevel.checkFiles();
            if(errorFile==null) {
                final Properties properties = PropertiesLoader.loadPropertiesFile(propertiesPath);
                new Game(gameCallback, properties,mapFiles);
                return;
            }
            else{
                Controller controller=new Controller(errorFile);
                Properties properties=PropertiesLoader.loadPropertiesFile(propertiesPath);
                controller.setGameProperties(properties);
                return;
            }

        }
        else if(argument.getName().endsWith(".xml")){
            Controller controller=new Controller(argument);
            Properties properties=PropertiesLoader.loadPropertiesFile(propertiesPath);
            controller.setGameProperties(properties);
            return;
        }

    }

}