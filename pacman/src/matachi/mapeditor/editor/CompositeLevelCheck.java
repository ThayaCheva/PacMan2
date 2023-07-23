package src.matachi.mapeditor.editor;

import java.io.File;
import java.util.ArrayList;
import src.utility.GameCallback;

public abstract class CompositeLevelCheck implements LevelCheck {
    private ArrayList<LevelCheck> levelChecks = new ArrayList<>();

    public abstract boolean levelCheck(File xmlFile, GameCallback gameCallback);

    public void add(LevelCheck checks) {
        levelChecks.add(checks);
    }

    public void remove(LevelCheck checks) {
        levelChecks.remove(checks);
    }
}
