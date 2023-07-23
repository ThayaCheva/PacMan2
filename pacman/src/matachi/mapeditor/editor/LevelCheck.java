// Team 03
// Rohit Sandeep Ambakkat 1200197
// Thaya Chevaphatrakul 1167144
// Tran Than Han Ha 1472202
package src.matachi.mapeditor.editor;

import java.io.File;
import java.util.ArrayList;
import src.utility.GameCallback;

public interface LevelCheck {
    boolean levelCheck(File xmlFile, GameCallback gameCallback);
}
