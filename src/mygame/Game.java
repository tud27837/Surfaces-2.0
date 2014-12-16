/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.audio.AudioNode;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Sphere;
import com.jme3.system.AppSettings;
import com.jme3.util.SkyFactory;

/**
 * Spawns the player, ball, goal hoop, switches, surfaces, and level. Contains
 * game logic and states.
 *
 * @author Zack Hunter
 * @author Jake DiIenno
 * @author Eric Bullock
 * @author Michael Fatal
 * @version %I% %G%
 * @see AbstractAppState
 * @since 2.0
 */
class Game extends AbstractAppState implements ActionListener {

    /**
     * copy of main class
     */
    private Main main;
    /**
     * copy of state manager
     */
    private AppStateManager asm;
    /**
     * the bullet physics engine
     */
    private BulletAppState bulletAppState;
    /**
     * collision control to detect physical collisions
     */
    private CollisionControl collCon;
    /**
     * Level object for control of the levels
     */
    private Level level;
    /**
     * Geometry for the ball
     */
    private Geometry geomBall;
    /**
     * physics of the ball Geometry
     */
    private RigidBodyControl ballPhys;
    /**
     * text object for the display of countdown text at the start of the level
     */
    private BitmapText waitText;
    /**
     * text object for the display of the text for the level timer
     */
    private BitmapText timeText;
    /**
     * boolean for whether the goal has been reached or not
     */
    private boolean goalReached;
    /**
     * Player object containing information about the player
     */
    private Player player;
    /**
     * direction the player is walking in a Vector3f
     */
    private Vector3f walkDirection = new Vector3f();
    /**
     * boolean for whether or not the player is walking left
     */
    private boolean left = false;
    /**
     * boolean for whether or not the player is walking right
     */
    private boolean right = false;
    /**
     * boolean for whether or not the player is walking forward
     */
    private boolean up = false;
    /**
     * boolean for whether or not the player is walking backward
     */
    private boolean down = false;
    /**
     * boolean for whether or not the surfaces are moving up
     */
    private boolean shiftUp = false;
    /**
     * boolean for whether or not the surfaces are moving down
     */
    private boolean shiftDown = false;
    /**
     * float for the wait time remaining before the timer starts and the player
     * can move
     */
    private float waitTime;
    /**
     * integer for the initial wait time in seconds
     */
    private final int INITIALWAITTIME = 3; // seconds
    /**
     * float for the seconds value of the level timer
     */
    private float levelTimeSeconds;
    /**
     * integer for the minutes value of the level timer
     */
    private int levelTimeMinutes;
    /**
     * String for the level timer
     */
    private String levelTimeString;
    /**
     * boolean for whether or not the wait text is being displayed
     */
    private boolean waitTextVisible;
    /**
     * boolea for whether the game is running(true) or paused(false)
     */
    private boolean isRunning;
    /**
     * AudioNode for the background music
     */
    public AudioNode backgroundMusic;
    /**
     * AudioNode for the sound of the ball
     */
    public AudioNode soundBall;
    /**
     * AudioNode for the sound of the reverse gravity switch
     */
    public AudioNode soundSwitchUp;
    /**
     * AudioNode for the sound of the normal gravity switch
     */
    public AudioNode soundSwitchDown;
    // states
    /**
     * the current state
     */
    private int state;
    /**
     * waiting for the game to start
     */
    private final int WAIT = 0;
    /**
     * the game is running
     */
    private final int RUNNING = 1;
    /**
     * the game is paused
     */
    private final int PAUSED = 2;
    /**
     * the game has ended
     */
    private final int END = 3;

    /**
     * Constructor. Initializes the state to WAIT.
     */
    public Game() {
        waitTextVisible = false;
        isRunning = true;
        state = WAIT;
    }

    /**
     * Initializes the game mechanics. Key mappings and text. Called when the
     * class is initialized.
     *
     * @param stateManager application state manager for the project
     * @param app instance of main
     */
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        main = (Main) app;
        asm = stateManager;
        waitTime = INITIALWAITTIME;
        levelTimeSeconds = 0;
        levelTimeMinutes = 0;
        goalReached = false;

        // keys
        main.getInputManager().addMapping("Left", new KeyTrigger(KeyInput.KEY_A), new KeyTrigger(KeyInput.KEY_LEFT));
        main.getInputManager().addMapping("Right", new KeyTrigger(KeyInput.KEY_D), new KeyTrigger(KeyInput.KEY_RIGHT));
        main.getInputManager().addMapping("Up", new KeyTrigger(KeyInput.KEY_W), new KeyTrigger(KeyInput.KEY_UP));
        main.getInputManager().addMapping("Down", new KeyTrigger(KeyInput.KEY_S), new KeyTrigger(KeyInput.KEY_DOWN));
        main.getInputManager().addMapping("Jump", new KeyTrigger(KeyInput.KEY_SPACE));
        main.getInputManager().addMapping("ShiftUp", new KeyTrigger(KeyInput.KEY_Q));
        main.getInputManager().addMapping("ShiftDown", new KeyTrigger(KeyInput.KEY_E));
        main.getInputManager().addListener(this, "Left", "Right", "Up", "Down", "Jump", "ShiftUp", "ShiftDown");

        // text
        main.getGuiNode().detachAllChildren();
        BitmapFont bmf = main.getAssetManager().loadFont("Interface/Fonts/Default.fnt");
        main.getGuiNode().detachAllChildren();
        waitText = new BitmapText(bmf);
        waitText.setSize(bmf.getCharSet().getRenderedSize() * 10);
        waitText.setColor(ColorRGBA.White);
        waitText.setText("");
        AppSettings s = main.getSettings();
        waitText.setLocalTranslation((s.getWidth() + waitText.getLineWidth()) / 2 - 50,
                (s.getHeight() + waitText.getLineHeight()) / 2 + 150, 0f);
        timeText = new BitmapText(bmf);
        timeText.setSize(bmf.getCharSet().getRenderedSize() * 3);
        timeText.setColor(ColorRGBA.Cyan);
        timeText.setText("0:00.00");
        timeText.setLocalTranslation((s.getWidth() - timeText.getLineWidth()) / 2,
                s.getHeight(), 0f);

        // inits
        initGeometries();
        initPhysics();
        initPlayer();
        initAudio();
        initSky();

        // load first level
        level = new Level(this);
    }

    /**
     * Determine what happens when a registered key is pressed. Controls player
     * movement and surface movement.
     *
     * @param name a String for the name given to the key pressed
     * @param isPressed a boolean to determine if a key is pressed
     * @param tpf a float containing the time per frame
     */
    public void onAction(String name, boolean isPressed, float tpf) {
        if (state == RUNNING) {
            if (name.equals("Left")) {
                if (isPressed) {
                    left = true;
                } else {
                    left = false;
                }
            } else if (name.equals("Right")) {
                if (isPressed) {
                    right = true;
                } else {
                    right = false;
                }
            } else if (name.equals("Up")) {
                if (isPressed) {
                    up = true;
                } else {
                    up = false;
                }
            } else if (name.equals("Down")) {
                if (isPressed) {
                    down = true;
                } else {
                    down = false;
                }
            } else if (name.equals("Jump")) {
                player.getControl().jump();
            } else if (name.equals("ShiftUp")) {
                if (isPressed) {
                    shiftUp = true;
                }
            } else if (name.equals("ShiftDown")) {
                if (isPressed) {
                    shiftDown = true;
                }
            }
        }
    }

    /**
     * Update loop for the game state.
     *
     * @param tpf float for time per frame
     */
    @Override
    public void update(float tpf) {
        switch (state) {
            case WAIT:
                if (!waitTextVisible) {
                    waitTextVisible = true;
                    main.getGuiNode().attachChild(waitText);
                }
                waitTime -= tpf;
                if (waitTime <= 0f) {
                    state = RUNNING;
                    main.getGuiNode().attachChild(timeText);
                    if (waitTextVisible) {
                        waitTextVisible = false;
                        main.getGuiNode().detachChild(waitText);
                    }
                } else {
                    waitText.setText("" + ((int) waitTime + 1));
                }
                main.getCamera().setLocation(player.getWorldTranslation().add(0, 3, 0));
                break;
            case RUNNING:
                // timer
                levelTimeSeconds += tpf;
                if (levelTimeSeconds < 10) {
                    levelTimeString = String.format("%d:0%.2f", levelTimeMinutes, levelTimeSeconds);
                } else if (levelTimeSeconds < 60) {
                    levelTimeString = String.format("%d:%.2f", levelTimeMinutes, levelTimeSeconds);
                } else {
                    levelTimeSeconds -= 60;
                    ++levelTimeMinutes;
                }
                timeText.setText(levelTimeString);
                timeText.setLocalTranslation((main.getSettings().getWidth() - timeText.getLineWidth()) / 2,
                        main.getSettings().getHeight(), 0f);

                // walking
                Vector3f camDir = main.getCamera().getDirection().clone().multLocal(10f);
                camDir.setY(0);
                Vector3f camLeft = main.getCamera().getLeft().clone().multLocal(10f);
                walkDirection.set(0, 0, 0);
                if (left) {
                    walkDirection.addLocal(camLeft);
                }
                if (right) {
                    walkDirection.addLocal(camLeft.negate());
                }
                if (up) {
                    walkDirection.addLocal(camDir);
                }
                if (down) {
                    walkDirection.addLocal(camDir.negate());
                }
                player.getControl().setWalkDirection(walkDirection);
                main.getCamera().setLocation(player.getWorldTranslation().add(0, 3, 0));
                if (shiftUp) {
                    shiftUp = false;
                    level.moveSurfaceUp();

                }

                //shift down
                if (shiftDown) {
                    shiftDown = false;
                    level.moveSurfaceDown();

                }

                // end game
                if (goalReached) {
                    state = END;
                    timeText.setLocalTranslation((main.getSettings().getWidth() - timeText.getLineWidth()) / 2,
                            main.getSettings().getHeight() - (main.getSettings().getHeight() / 10), 0f);
                    main.setHighScores(level.getLevelNum(), levelTimeMinutes, levelTimeSeconds);
                    main.getFlyByCamera().setDragToRotate(true);
                    EndScreen e = new EndScreen();
                    main.getNifty().gotoScreen("end");
                    asm.attach(e);
                    this.setEnabled(false);
                }
                break;
        }
    }

    /**
     * Initialize general geometries for game elements. Creates ball.
     */
    public void initGeometries() {
        // create ball
        Sphere ball = new Sphere(20, 20, 1.0f);
        geomBall = new Geometry("Ball", ball);
        geomBall.setMaterial(main.gold);
        main.getRootNode().attachChild(geomBall);
    }

    /**
     * Initialize the player using the Player class and add it to the root node.
     *
     * @see Player
     */
    public void initPlayer() {
        player = new Player(this);
        main.getRootNode().attachChild(player);
    }

    /**
     * Initialize the bullet physics engine and all physical elements. Starts
     * the bullet engine and adds appropriate physics to each geometry.
     *
     * @see com.jme3.bullet.control.RigidBodyControl
     * @see com.jme3.bullet.BulletAppState
     */
    public void initPhysics() {
        bulletAppState = new BulletAppState();
        asm.attach(bulletAppState);
        //bulletAppState.setDebugEnabled(true);

        // set ball physics
        ballPhys = new RigidBodyControl(1.0f);
        geomBall.addControl(ballPhys);
        bulletAppState.getPhysicsSpace().add(ballPhys);

        // set collision control
        collCon = new CollisionControl(this);
        bulletAppState.getPhysicsSpace().addCollisionListener(collCon);
    }

    /**
     * Initialize Audio by adding background music and sounds for gravity
     * switches.
     */
    private void initAudio() {
        backgroundMusic = new AudioNode(main.getAssetManager(), "Sound/background.wav", true);
        backgroundMusic.setLooping(true);
        backgroundMusic.setVolume(3);
        main.getRootNode().attachChild(backgroundMusic);
        backgroundMusic.play();

        soundBall = new AudioNode(main.getAssetManager(), "Sound/Ball Rolling.wav", false);
        soundBall.setVolume(4);
        main.getRootNode().attachChild(backgroundMusic);

        soundSwitchUp = new AudioNode(main.getAssetManager(), "Sound/Switch Up.wav", false);
        soundSwitchUp.setVolume(4);
        main.getRootNode().attachChild(soundSwitchUp);

        soundSwitchDown = new AudioNode(main.getAssetManager(), "Sound/Switch Down.wav", false);
        soundSwitchDown.setVolume(4);
        main.getRootNode().attachChild(soundSwitchDown);
    }

    /**
     * Returns true if the goal is reached, false if it is not.
     *
     * @return boolean for whether or not the goal has been reached
     */
    public boolean isGoalReached() {
        return goalReached;
    }

    /**
     * Sets the goalReached boolean to true.
     */
    public void goalReached() {
        goalReached = true;
        ballPhys.clearForces();
        ballPhys.setPhysicsLocation(new Vector3f(1000.0f, 1000.0f, 1000.0f));
    }

    /**
     * Returns the current instance of Main.
     *
     * @return the Main currently used
     */
    public Main getMain() {
        return main;
    }

    /**
     * Returns the bulletAppState
     *
     * @return the instance of the bullet engine in use
     */
    public BulletAppState getBulletAppState() {
        return bulletAppState;
    }

    /**
     * Returns the Player class object.
     *
     * @return the Player class object
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Returns the ball physics
     *
     * @return RigidBodyControl for the ball
     */
    public RigidBodyControl getBall() {
        return ballPhys;
    }

    /**
     * Resets all elements of the level to starting positions
     */
    public void resetLevel() {
        // reset player
        player.reset();
        // reset ball
        ballPhys.setPhysicsLocation(new Vector3f(level.getBallStart()));
        ballPhys.setLinearVelocity(Vector3f.ZERO);
        ballPhys.setAngularVelocity(Vector3f.ZERO);
        // reset normal gravity
        bulletAppState.getPhysicsSpace().setGravity(new Vector3f(0, -9.81f, 0));
        level.resetLevel();
    }

    /**
     * Adds a skybox.
     */
    private void initSky() {
        Spatial sky = SkyFactory.createSky(main.getAssetManager(), "Textures/BrightSky.dds", false);
        main.getRootNode().attachChild(sky);
    }

    /**
     * Returns whether or not the game is running.
     *
     * @return true is the game is running, false if it is paused
     */
    public boolean isRunning() {
        return isRunning;
    }

    /**
     * Toggles the game between running and paused. If the game is running,
     * pauses game and changes state to paused. If the game is paused, unpauses
     * game and changes state to running.
     */
    public void togglePauseGame() {
        if (isRunning) {
            isRunning = false;
            state = PAUSED;
            bulletAppState.setSpeed(0);
        } else {
            isRunning = true;
            state = RUNNING;
            bulletAppState.setSpeed(1);
        }
    }

    /**
     * Removes all objects from the world, physics space, and GUI Node.
     */
    public void clear() {
        bulletAppState.getPhysicsSpace().removeAll(main.getRootNode());
        Main.clearJMonkey(main);
    }

    /**
     * End the current level. If it is not the final level, move to the next
     * level, otherwise go to the start screen.
     */
    public void endLevel() {
        if (level.nextLevel()) {
            goalReached = false;
            state = WAIT;
            waitTime = INITIALWAITTIME;
            levelTimeSeconds = 0;
            levelTimeMinutes = 0;
            levelTimeString = "";
        } else {
            main.getNifty().gotoScreen("start");
        }
    }
}
