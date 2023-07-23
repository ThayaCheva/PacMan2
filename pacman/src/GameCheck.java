// Team 03
// Rohit Sandeep Ambakkat 1200197
// Thaya Chevaphatrakul 1167144
// Tran Than Han Ha 1472202
package src;
import java.io.*;
import java.util.*;
import src.utility.GameCallback;
import java.lang.*;

public class GameCheck {
    private String gameFolder;
    private GameCallback gameCallBack;

    public GameCheck(String gameFolder, GameCallback gameCallBack){
        this.gameFolder=gameFolder;
        this.gameCallBack=gameCallBack;
    }

    public boolean performGameCheck(){
        boolean MapCheckFail;
        boolean MapSequenceFail;
        MapCheckFail=checkMapFile();
        MapSequenceFail=checkMapSequence();

        if(MapCheckFail==false || MapSequenceFail==false){
            return false;
        }
        return true;
    }

    // Go through the folder and check validity of game files
    private boolean checkMapFile(){
        File gameFolder = new File(this.gameFolder);
        File[] contents = gameFolder.listFiles();
        boolean found = false;

        for(int i = 0; i < contents.length; i++){
            if(Character.isDigit(contents[i].getName().charAt(0))){
                if(contents[i].getName().endsWith(".xml")){
                    found = true;
                    break;
                }
            }
        }

        if(found == false){
            gameCallBack.writeString("[Game " + gameFolder.getName() + " - no maps found]");
            return false;
        }
        return true;
    }

    // Check if maps have the same level
    private boolean checkMapSequence(){
        File gameFolder= new File(this.gameFolder);
        File[] contents= gameFolder.listFiles();
        ArrayList<String> mapFiles = new ArrayList<String>();
        ArrayList<String> sequence= new ArrayList<String>();

        for(int i = 0 ; i < contents.length ; i++){
            if(Character.isDigit(contents[i].getName().charAt(0))){
                if(contents[i].getName().endsWith(".xml")){
                    mapFiles.add(contents[i].getName());
                }
            }
        }

        Collections.sort(mapFiles);

        // Compare the first letter of each map file
        for(int i = 0; i < mapFiles.size(); i++){
            boolean multiple = false;
            sequence.add(mapFiles.get(i));
            for(int j = i + 1; j < mapFiles.size(); j++){
                char c1 = mapFiles.get(i).charAt(0);
                char c2 = mapFiles.get(j).charAt(0);
                if(c1 == c2){
                    sequence.add(mapFiles.get(j));
                    multiple = true;
                }
            }
            if (multiple == true) {
                break;
            }
            sequence.clear();
        }

        if(!sequence.isEmpty()){
            String output=String.join("; ",sequence);
            GameCallback gameCallback=this.gameCallBack;
            gameCallback.writeString("[Game " + gameFolder.getName() + " - multiple maps at same level: " + output+"]\n");
            return false;
        }
        return true;
    }
}
