// Team 03
// Rohit Sandeep Ambakkat 1200197
// Thaya Chevaphatrakul 1167144
// Tran Than Han Ha 1472202
package src;

public class Driver {
    public static final String DEFAULT_PROPERTIES_PATH = "properties/test1.properties";

    /**
     * Starting point
     * @param args the command line arguments
     */

    public static void main(String args[]) throws Exception {
        String propertiesPath = DEFAULT_PROPERTIES_PATH;

        String test_args[] = new String[1];
        test_args[0] = "src/Game/sample_map2.xml";
        EditorTesterFactory factory = new EditorTesterFactory();
        factory.createGameGridObject(args, propertiesPath);
    }
}
