/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import java.util.List;

/**
 * Controls the menu screens of the game. Allows player to start the game, go to
 * the score screen, or quit the game.
 * @author Zack Hunter
 * @version %I% %G%
 * @since 3.0
 */
public class StartScreen extends AbstractAppState implements ScreenController, ActionListener {

   /**
    * copy of main class
    */
    private Main main;
   /**
    * copy of state manager
    */
    private AppStateManager asm;
   /**
    * instance of Game class
    */
    private Game g = null;
   /**
    * controller for the screens and popups
    */
    private Nifty nifty;
   /**
    * controller for the screens
    */
    private Screen screen;
   /**
    * object for the popup displayed when the game is paused
    */
    private Element pausePopup;
   /**
    * object for the popup displayed asking the user whether or not to quit
    */
    private Element quitPopup;
   /**
    * boolean for whether or not a popup is open
    */
    private boolean popupOpen = false;

    /**
     * Determine what happens when a registered key is pressed.
     * @param name a String for the name given to the key pressed
     * @param isPressed a boolean to determine if a key is pressed
     * @param tpf a float containing the time per frame
     */
    public void onAction(String name, boolean isPressed, float tpf) {
        if (isPressed) {
            if (name.equals("PauseQuit")) {
                if (g != null) {
                    if (!g.isRunning()) {
                        closePausePopup("false");
                    } else {
                        showPausePopup();
                    }
                } else {
                    showQuitPopup();
                }
            }
        }
    }

    /**
     * Called when the class is initilized. Creates key mappings.
     * @param stateManager application state manager for the project
     * @param app instance of main
     */
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        main = (Main) app;
        asm = stateManager;
        Main.clearJMonkey(main);

        // keys
        InputManager inputManager = main.getInputManager();
        inputManager.addMapping("PauseQuit", new KeyTrigger(KeyInput.KEY_ESCAPE));
        inputManager.addListener(this, "PauseQuit");
    }

   /**
    * Switch to start menu. Close the pause popup if it is open. Clear the game 
    * object if it is not null.
    * @param nextScreen String designating which nifty screen to switch to
    */
    public void goToStart(String nextScreen) {
        if (popupOpen) {
            closePausePopup("false");
        }
        main.getFlyByCamera().setDragToRotate(true);
        nifty.gotoScreen(nextScreen);
        if (g != null) {
            g.clear();
            g = null;
        }
    }

   /**
    * Switch to score screen. Switch states to score screen.
    * @param nextScreen String designating which nifty screen to switch to
    */
    public void goToScores(String nextScreen) {
        nifty.gotoScreen(nextScreen);
        ScoreScreen s = new ScoreScreen();
        asm.attach(s);
        asm.detach(this);
    }

   /**
    * Display high scores on the score screen for a particular level.
    * @param levelStr String for the level number
    */
    public void showScores(String levelStr) {
        int levelNum = Integer.parseInt(levelStr);
        List<String[]> scores = main.getHighScores(levelNum);
        String scoreStr = "";
        for (int i = 0; i < scores.size(); i++) {
            String[] score = scores.get(i);
            if (Float.parseFloat(score[1]) < 10) {
                scoreStr = String.format("%d.  %d:0%.2f", (i + 1), Integer.parseInt(score[0]), Float.parseFloat(score[1]));
            } else {
                scoreStr = String.format("%d.  %d:%.2f", (i + 1), Integer.parseInt(score[0]), Float.parseFloat(score[1]));
            }
            Element niftyElement = nifty.getCurrentScreen().findElementByName("scoreText" + i);
            niftyElement.getRenderer(TextRenderer.class).setText(scoreStr);
        }
    }

   /**
    * Start the game. Create new game object. Switch to the game state
    * @param nextScreen String designating which nifty screen to switch to
    */
    public void startGame(String nextScreen) {
        main.getFlyByCamera().setDragToRotate(false);
        nifty.gotoScreen(nextScreen);  // switch to another screen
        if (g != null) {
            g.clear();
        }
        g = new Game();
        asm.attach(g);
        asm.detach(this);
    }

   /**
    * Reset the level. Close pause popup if its open.
    * @param nextScreen String designating which nifty screen to switch to
    */
    public void restart(String nextScreen) {
        main.getFlyByCamera().setDragToRotate(false);
        nifty.gotoScreen(nextScreen);
        g.resetLevel();
        if (popupOpen) {
            closePausePopup("false");
        }
    }

   /**
    * Open up the quit confirmation popup.
    */
    public void quitClick() {
        showQuitPopup();
    }

   /**
    * Bind the nifty and screen objects.
    * @param nifty controller for the screens and popups
    * @param screen controller for the screens
    */
    public void bind(Nifty nifty, Screen screen) {
        this.nifty = nifty;
        this.screen = screen;
    }

   /**
    * Called when the screen is switched to
    */
    public void onStartScreen() {
    }

   /**
    * Called when the screen is switched from
    */
    public void onEndScreen() {
    }

   /**
    * Create and display the pause popup if no other popup is open. Pause game.
    */
    public void showPausePopup() {
        if (!popupOpen) {
            main.getFlyByCamera().setDragToRotate(true);
            popupOpen = true;
            pausePopup = nifty.createPopup("popupPause");
            nifty.showPopup(nifty.getCurrentScreen(), pausePopup.getId(), null);
            g.togglePauseGame();
        }
    }

   /**
    * Close the pause popup. Unpause game if necessary.
    * @param quit String for whether or not the quit popup is opened. Controls 
    *             whether or not to unpause the game.
    */
    public void closePausePopup(String quit) {
        main.getFlyByCamera().setDragToRotate(false);
        popupOpen = false;
        nifty.closePopup(pausePopup.getId());
        if (quit.equals("false")) {
            g.togglePauseGame();
        }
    }

   /**
    * Open the quit confirmation popup from the pause popup. Close the pause
    * popup to open the quit popup.
    */
    public void quitFromPause() {
        closePausePopup("true");
        showQuitPopup();
    }

   /**
    * Create and display the quit confirmation popup if no other popup is open.
    */
    public void showQuitPopup() {
        if (!popupOpen) {
            main.getFlyByCamera().setDragToRotate(true);
            popupOpen = true;
            quitPopup = nifty.createPopup("popupQuit");
            nifty.showPopup(nifty.getCurrentScreen(), quitPopup.getId(), null);
        }
    }

   /**
    * Close the quit confirmation popup. Unpause the game if necessary.
    */
    public void closeQuitPopup() {
        main.getFlyByCamera().setDragToRotate(false);
        popupOpen = false;
        nifty.closePopup(quitPopup.getId());
        if (g != null) {
            g.togglePauseGame();
        }
    }

   /**
    * Exit from the program.
    */
    public void quitGame() {
        System.exit(0);
    }

   /**
    * Go to the next level of the game.
    */
    public void nextLevel() {
        main.getFlyByCamera().setDragToRotate(false);
        nifty.gotoScreen("hud");
        g.endLevel();
    }

   /**
    * Update loop for the current state. Does nothing.
    * @param tpf 
    */
    @Override
    public void update(float tpf) {
    }
}
