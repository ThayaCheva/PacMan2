// Team 03
// Rohit Sandeep Ambakkat 1200197
// Thaya Chevaphatrakul 1167144
// Tran Than Han Ha 1472202
package src.matachi.mapeditor.editor;

import java.io.File;
import java.util.ArrayList;
import src.utility.GameCallback;

public class LevelCheckFacade {
    private ArrayList<File> files;
    public LevelCheckFacade(ArrayList<File> files){
        this.files=files;
    }

    public File checkFiles(){
        ArrayList<LevelCheck> levelChecks = new ArrayList<>();
        levelChecks.add(new PacManCheck());
        levelChecks.add(new GoldCheck());
        levelChecks.add(new PortalCheck());
        levelChecks.add(new PillCheck());
        GameCallback gameCallback = new GameCallback("level");
        boolean check;
        boolean error=false;

        for(int i=0; i<this.files.size();i++){
            File curr=this.files.get(i);
            for (LevelCheck checks : levelChecks) {
                check = checks.levelCheck(curr, gameCallback);
                if(check == false){
                    error=true;
                }
            }
            if(error==true){
                return curr;
            }
        }

        return null;

    }

}
