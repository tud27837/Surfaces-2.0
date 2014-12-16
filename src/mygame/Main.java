package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.shadow.DirectionalLightShadowRenderer;
import com.jme3.system.AppSettings;
import de.lessvoid.nifty.Nifty;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Main class of the project. Initializes basic elements, creates and starts a
 * simple app, and starts the game, controls high score saving.
 *
 * @author Zack Hunter
 * @version %I% %G%
 * @see SimpleApplication
 * @since 1.0
 */
public class Main extends SimpleApplication {

    /**
     * gold colored material. Can be applied to many geometries.
     */
    public static Material gold;
    /**
     * magenta colored material. Can be applied to many geometries.
     */
    public static Material magenta;
    /**
     * red colored material. Can be applied to many geometries.
     */
    public static Material red;
    /**
     * green colored material. Can be applied to many geometries.
     */
    public static Material green;
    /**
     * white colored material. Can be applied to many geometries.
     */
    public static Material white;
    /**
     * yellow colored material. Can be applied to many geometries.
     */
    public static Material yellow;
    /**
     * Map of Integers to a list of string arrays of size 2. Integers for level
     * number, list for 10 high scores, string arrays for minutes and seconds.
     */
    private Map<Integer, List<String[]>> highscores;
    /**
     * File for saving the highscores.
     */
    private File file;
    /**
     * Controller for screens and popups
     */
    private Nifty nifty;

    /**
     * Main method of the project. Sets up the app.
     *
     * @param args array of Strings containing default arguments
     */
    public static void main(String[] args) {
        AppSettings settings = new AppSettings(true);
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        screen.width *= 0.75;
        screen.height *= 0.75;
        settings.setResolution(screen.width, screen.height);
        settings.setVSync(true);
        Main app = new Main();
        app.setShowSettings(false);
        app.setSettings(settings);
        app.start();
    }

    /**
     * Returns the settings object of the current app.
     *
     * @return an AppSettings object containing the settings of the app
     */
    public AppSettings getSettings() {
        return (settings);
    }

    /**
     * Initialize the application. Create materials, light and shadows, camera,
     * GUI properties, and load high scores. Initialize NiftyGui and StartScreen
     */
    @Override
    public void simpleInitApp() {
        initMaterials();
        initLightandShadow();
        initCam();
        initGui();
        this.inputManager.clearMappings(); // clear default mappings

        // Highscores and Save file
        highscores = new HashMap();
        try {
            file = new File("highscores.txt");
            if (!readPreviousScores()) {
                for (int i = 0; i < 8; i++) {
                    List<String[]> time = new ArrayList();
                    for (int j = 0; j < 10; j++) {
                        time.add(new String[]{"999", "59.99"});
                    }
                    highscores.put(i, time);
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        // This starts the game
        StartScreen s = new StartScreen();
        stateManager.attach(s);
        NiftyJmeDisplay niftyDisplay = new NiftyJmeDisplay(assetManager, inputManager, audioRenderer, viewPort);
        nifty = niftyDisplay.getNifty();
        guiViewPort.addProcessor(niftyDisplay);
        nifty.fromXml("Interface/niftyScreens.xml", "start", s);
        flyCam.setDragToRotate(true);
    }

    /**
     * Detaches all children from both the rootNode and the guiNode of the Main
     * m argument.
     *
     * @param m a Main class instance
     */
    protected static void clearJMonkey(Main m) {
        m.guiNode.detachAllChildren();
        m.rootNode.detachAllChildren();
    }

    /**
     * Update loop for the project. Nothing happens here.
     *
     * @param tpf float for time per frame
     */
    @Override
    public void simpleUpdate(float tpf) {
        // nothing
    }

    // -------------------------------------------------------------------------
    // Initialization Methods
    // -------------------------------------------------------------------------
    /**
     * Initialize all materials to be used throughout the project. The
     * individual materials are stored in public global variables in this class
     *
     * @see Material
     */
    private void initMaterials() {
        gold = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        gold.setBoolean("UseMaterialColors", true);
        gold.setColor("Ambient", ColorRGBA.Red);
        gold.setColor("Diffuse", ColorRGBA.Green);
        gold.setColor("Specular", ColorRGBA.Gray);
        gold.setFloat("Shininess", 4f); // shininess from 1-128

        magenta = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        magenta.setBoolean("UseMaterialColors", true);
        magenta.setColor("Ambient", ColorRGBA.Gray);
        magenta.setColor("Diffuse", ColorRGBA.Blue);
        magenta.setColor("Specular", ColorRGBA.Red);
        magenta.setFloat("Shininess", 2f); // shininess from 1-128

        red = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        red.setBoolean("UseMaterialColors", true);
        red.setColor("Ambient", ColorRGBA.Red);
        red.setColor("Diffuse", ColorRGBA.Red);
        red.setColor("Specular", ColorRGBA.Red);
        red.setFloat("Shininess", 2f);

        green = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        green.setBoolean("UseMaterialColors", true);
        green.setColor("Ambient", ColorRGBA.Green);
        green.setColor("Diffuse", ColorRGBA.Green);
        green.setColor("Specular", ColorRGBA.Green);
        green.setFloat("Shininess", 2f);

        white = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        white.setBoolean("UseMaterialColors", true);
        white.setColor("Ambient", ColorRGBA.White);
        white.setColor("Diffuse", ColorRGBA.White);
        white.setColor("Specular", ColorRGBA.Gray);
        white.setFloat("Shininess", 2f);

        yellow = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        yellow.setBoolean("UseMaterialColors", true);
        yellow.setColor("Ambient", new ColorRGBA(255, 255, 0, 0));
        yellow.setColor("Diffuse", new ColorRGBA(255, 255, 0, 0));
        yellow.setColor("Specular", new ColorRGBA(255, 255, 0, 0));
        yellow.setFloat("Shininess", 2f);
    }

    /**
     * Initialize aspects of the GUI. Sets to display the frames per second and
     * hide other stats.
     *
     * @see SimpleApplication#setDisplayFps(boolean)
     * @see SimpleApplication#setDisplayStatView(boolean)
     */
    private void initGui() {
        setDisplayFps(true);
        setDisplayStatView(false);
    }

    /**
     * Initialize light elements and shadow elements. Add them to the root node
     * and view port, respectively.
     */
    private void initLightandShadow() {
        // Light 1: white, directional
        DirectionalLight sun1 = new DirectionalLight();
        sun1.setDirection((new Vector3f(-0.7f, -1.3f, -0.9f)).normalizeLocal());
        sun1.setColor(ColorRGBA.Gray);
        rootNode.addLight(sun1);

        // Light 2: white, directional
        DirectionalLight sun2 = new DirectionalLight();
        sun2.setDirection((new Vector3f(0.7f, -1.3f, -0.9f)).normalizeLocal());
        sun2.setColor(ColorRGBA.Gray);
        rootNode.addLight(sun2);

        // Light 3: Ambient, gray
        AmbientLight ambient = new AmbientLight();
        ambient.setColor(new ColorRGBA(0.7f, 0.7f, 0.7f, 1.0f));
        rootNode.addLight(ambient);

        // SHADOW
        // the second parameter is the resolution. Experiment with it! (Must be a power of 2)
        DirectionalLightShadowRenderer dlsr = new DirectionalLightShadowRenderer(assetManager, 512, 1);
        dlsr.setLight(sun1);
        dlsr.setLight(sun2);
        viewPort.addProcessor(dlsr);
    }

    /**
     * Initialize elements affecting the state of the camera. Set the camera's
     * starting position, move speed, and look direction as well as the
     * background color of the scene.
     *
     * @see SimpleApplication#cam
     * @see SimpleApplication#flyCam
     */
    private void initCam() {
        flyCam.setEnabled(true);
        flyCam.setMoveSpeed(100f);
        cam.setLocation(new Vector3f(3f, 15f, 15f));
        cam.lookAt(Vector3f.ZERO, Vector3f.UNIT_Y);
        viewPort.setBackgroundColor(new ColorRGBA(0.7f, 0.8f, 1f, 1f));
    }

    /**
     * Return NiftyGui for the project.
     *
     * @return Nifty instance
     */
    public Nifty getNifty() {
        return nifty;
    }

    /**
     * Read in scores from the save file if the file exists.
     *
     * @return true if the file exists and the scores were read, otherwise false
     * @throws IOException
     */
    private boolean readPreviousScores() throws IOException {
        if (!file.exists()) {
            file.createNewFile();
            return false;
        } else {
            BufferedReader br = new BufferedReader(new FileReader(file.getAbsoluteFile()));
            String line;
            for (int i = 0; i < 8; i++) {
                List<String[]> timeList = new ArrayList();
                for (int j = 0; j < 10; j++) {
                    line = br.readLine();
                    String[] time = new String[2];
                    int colon = line.indexOf(':');
                    time[0] = line.substring(0, colon);
                    time[1] = line.substring(colon + 1);
                    timeList.add(time);
                }
                highscores.put(i, timeList);
            }
            br.close();
            return true;
        }
    }

    /**
     * Write the high scores to the save file.
     *
     * @throws IOException
     */
    private void writeScoresToFile() throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter(file.getAbsoluteFile()));
        for (int i = 0; i < 8; i++) {
            List<String[]> timeList = highscores.get(i);
            for (int j = 0; j < 10; j++) {
                String[] time = timeList.get(j);
                bw.write(String.format("%s:%s", time[0], time[1]));
                bw.newLine();
            }
        }
        bw.close();
    }

    /**
     * Return the high scores for the specified level.
     *
     * @param level Integer value for the level number
     * @return List of String arrays of length 2 containing the 10 high scores
     */
    public List<String[]> getHighScores(int level) {
        return highscores.get(level - 1);
    }

    /**
     * Set the current high scores. If the score (minutes, seconds) is lower
     * than any of the scores in the list, that score is put into the list at
     * the correct place and the last score is removed. The scores are then
     * written to the save file.
     *
     * @param level Integer value for the level
     * @param minutes Integer value for the minutes of the score
     * @param seconds Float value for the seconds of the score
     */
    public void setHighScores(int level, int minutes, float seconds) {
        List<String[]> timeList = highscores.remove(level - 1);
        int index = -1;
        for (int i = 0; i < 10; i++) {
            String[] time = timeList.get(i);
            int min = Integer.parseInt(time[0]);
            float sec = Float.parseFloat(time[1]);
            if (minutes < min) {
                index = i;
                break;
            } else if ((minutes == min) && (seconds <= sec)) {
                index = i;
                break;
            }
        }
        if (index != -1) {
            String[] high = new String[2];
            high[0] = Integer.toString(minutes);
            high[1] = String.format("%.2f", seconds);
            timeList.add(index, high);
            timeList.remove(10);
        }
        highscores.put(level - 1, timeList);
        try {
            writeScoresToFile();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}