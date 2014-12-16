/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

/**
 * Controls the High Score Screen.
 *
 * @author Zack Hunter
 * @version %I% %G%
 * @since 3.0
 */
public class ScoreScreen extends AbstractAppState implements ScreenController {

    /**
     * copy of main class
     */
    private Main main;
    /**
     * copy of state manager
     */
    private AppStateManager asm;
    /**
     * controller for the screens and popups
     */
    private Nifty nifty;
    /**
     * controller for the screens
     */
    private Screen screen;

    /**
     * Called when the class is initialized.
     *
     * @param stateManager application state manager for the project
     * @param app instance of main
     */
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        main = (Main) app;
        asm = stateManager;
    }

    /**
     * Bind the nifty and screen objects.
     *
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
}
