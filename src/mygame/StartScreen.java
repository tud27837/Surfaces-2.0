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
 * Displays the title of the game. Allows player to start the game.
 *
 * @author Zack Hunter
 * @version %I% %G%
 * @since 2.0
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
    private Game g = null;
    private Nifty nifty;
    private Screen screen;
    private Element pausePopup, quitPopup;
    private boolean popupOpen = false;

    /**
     * Determine what happens when a registered key is pressed.
     *
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
     *
     * @param stateManager
     * @param app
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

    public void goToStart(String nextScreen) {
        if (popupOpen) {
            closePausePopup("false");
        }
        main.getFlyByCamera().setDragToRotate(true);
        nifty.gotoScreen(nextScreen);
        if (g != null) {
            g.clear();
        }
    }

    public void goToScores(String nextScreen) {
        nifty.gotoScreen(nextScreen);
        ScoreScreen s = new ScoreScreen();
        asm.attach(s);
        asm.detach(this);
    }

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

    public void restart(String nextScreen) {
        main.getFlyByCamera().setDragToRotate(false);
        nifty.gotoScreen(nextScreen);
        if (g != null) {
            g.clear();
        }
        g = new Game();
        asm.attach(g);
        asm.detach(this);
        if (popupOpen) {
            closePausePopup("true");
        }
    }

    public void quitClick() {
        showQuitPopup();
    }

    public void bind(Nifty nifty, Screen screen) {
        this.nifty = nifty;
        this.screen = screen;
    }

    public void onStartScreen() {
    }

    public void onEndScreen() {
    }

    public void showPausePopup() {
        if (!popupOpen) {
            main.getFlyByCamera().setDragToRotate(true);
            popupOpen = true;
            pausePopup = nifty.createPopup("popupPause");
            nifty.showPopup(nifty.getCurrentScreen(), pausePopup.getId(), null);
            g.togglePauseGame();
        }
    }

    public void closePausePopup(String quit) {
        main.getFlyByCamera().setDragToRotate(false);
        popupOpen = false;
        nifty.closePopup(pausePopup.getId());
        if (quit.equals("false")) {
            g.togglePauseGame();
        }
    }

    public void quitFromPause() {
        closePausePopup("true");
        showQuitPopup();
    }

    public void showQuitPopup() {
        if (!popupOpen) {
            main.getFlyByCamera().setDragToRotate(true);
            popupOpen = true;
            quitPopup = nifty.createPopup("popupQuit");
            nifty.showPopup(nifty.getCurrentScreen(), quitPopup.getId(), null);
        }
    }

    public void closeQuitPopup() {
        main.getFlyByCamera().setDragToRotate(false);
        popupOpen = false;
        nifty.closePopup(quitPopup.getId());
        if (g != null) {
            g.togglePauseGame();
        }
    }

    public void quitGame() {
        System.exit(0);
    }

    public void nextLevel() {
        main.getFlyByCamera().setDragToRotate(false);
        nifty.gotoScreen("hud");
        asm.detach(asm.getState(EndScreen.class));
        g.setEnabled(true);
        g.endLevel();
    }

    @Override
    public void update(float tpf) {
    }
}
