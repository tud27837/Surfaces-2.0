/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Cylinder;
import java.util.ArrayList;

/**
 *
 * @author Zack
 */
public class Level {

    private Game game;
    private Spatial model;
    private int levelNum;
    private final int NUMBEROFLEVELS = 7;
    private int listIndex = 0;
    private int upDownIndex = 0;
    private int upIndex = 0;
    private int downIndex = 0;
    private Node nodeUp, nodeDown, nodeUpDown;
    private Vector3f playerStartPos, ballStartPos;
    private Geometry geom;
    private RigidBodyControl landscape, phys, upDownPhys, upPhys, downPhys;
    private final Cylinder hoop = new Cylinder(30, 30, 1.5f, 0.1f, true);
    private ArrayList<Vector3f> upDownPositionList = new ArrayList<Vector3f>();
    private ArrayList<Vector3f> upPositionList = new ArrayList<Vector3f>();
    private ArrayList<Vector3f> downPositionList = new ArrayList<Vector3f>();
    private ArrayList<RigidBodyControl> physList = new ArrayList<RigidBodyControl>();
    private ArrayList<RigidBodyControl> upDownPhysList = new ArrayList<RigidBodyControl>();
    private ArrayList<RigidBodyControl> upPhysList = new ArrayList<RigidBodyControl>();
    private ArrayList<RigidBodyControl> downPhysList = new ArrayList<RigidBodyControl>();
    private ArrayList<Geometry> geomList = new ArrayList<Geometry>();

    public Level(Game game) {
        this.game = game;
        nodeUp = new Node();
        nodeDown = new Node();
        nodeUpDown = new Node();
        game.getMain().getRootNode().attachChild(nodeUp);
        game.getMain().getRootNode().attachChild(nodeDown);
        game.getMain().getRootNode().attachChild(nodeUpDown);
        levelNum=1;
        loadLevel();
    }

    public void loadLevel() {
        //System.out.println(levelNum);
        switch (levelNum) {
            case 1:
                //Level 1
                model = game.getMain().getAssetManager().loadModel("Scenes/LevelOne.j3o");
                playerStartPos = new Vector3f(20, 3, -35);
                game.getPlayer().setStart(playerStartPos);
                ballStartPos = new Vector3f(50.0f, 13.0f, -15.0f);
                game.getBall().setPhysicsLocation(ballStartPos);
                createHoop(89.0f, 13.0f, -65.0f, 0.0f);
                createDownBlock(15.0f, 16.0f, 15.0f, 17.0f, -5.0f, -10.0f);
                break;
            case 2:
                //Level 2
                model = game.getMain().getAssetManager().loadModel("Scenes/Level2.mesh.xml");
                playerStartPos = new Vector3f(0.0f, 10.0f, 40.0f);
                game.getPlayer().setStart(playerStartPos);
                ballStartPos = new Vector3f(0.0f, 5.0f, 30.0f);
                game.getBall().setPhysicsLocation(ballStartPos);
                createHoop(0.0f, 2.0f, -50.0f, 0.0f);
                createGlassBlock(1.0f, 0.2f, 16.2f, 0.0f, 0.5f, 0.0f);
                createLavaBlock(7.5f, 1.0f, 16.2f, 0.0f, -1.0f, 0.0f);
                break;
            case 3:
                //Level 3
                model = game.getMain().getAssetManager().loadModel("Scenes/Level3.mesh.xml");
                playerStartPos = new Vector3f(0, 0, 0);
                game.getPlayer().setStart(playerStartPos);
                ballStartPos = new Vector3f(0f, 10f, -115f);
                game.getBall().setPhysicsLocation(ballStartPos);
                createRevGravSwitch(0f, 5f, -126f, 0f);
                createNormGravSwitch(0f, 110f, -238f, 0f);
                createHoop(0f, -4f, -248f, 0f);
                break;
            case 4:
                //Level 4
                model = game.getMain().getAssetManager().loadModel("Scenes/Level4.mesh.xml");
                playerStartPos = new Vector3f(0, 0, 0);
                game.getPlayer().setStart(playerStartPos);
                ballStartPos = new Vector3f(-33.6f, -3f, -130.0f);
                game.getBall().setPhysicsLocation(ballStartPos);
                createHoop(-33.6f, -4f, -170.0f, 0.0f);
                createLavaBlock(200f, 1.0f, 200f, 0.0f, -10.0f, 0.0f);
                break;
            case 5:
                //Level 5
                model = game.getMain().getAssetManager().loadModel("Scenes/Level5.j3o");
                playerStartPos = new Vector3f(30, 15, -20);
                game.getPlayer().setStart(playerStartPos);
                ballStartPos = new Vector3f(30.0f, 15.0f, -25.0f);
                game.getBall().setPhysicsLocation(ballStartPos);
                createHoop(35f, 15f, 18f, 90f);
                createUpBlock(5f, .1f, 15f, -70f, 8.4f, -50f);  
                createDownBlock(3f, .1f, 25f, 25f, 17f, 24.5f);  
                createUpDownBlock(5f, .1f, 20f, -69f, 2f, 23f);  
                createUpBlock(5f, .1f, 12f, -40f, -5f, 37f);      
                createUpDownBlock(5f, .1f, 12f, -22f, 9f, 37f);  
                createNormGravSwitch(15f, 33f, 18f, 90f);
                createRevGravSwitch(0f, 13f, 18f, 90f);
                createLavaBlock(12f, .2f, 12f, -50f, 4f, 12f);
                createLavaBlock(17f, .2f, 25f, 20f, 0f, 24.5f);
                break;
            case 6:
                //Level 6
                model = game.getMain().getAssetManager().loadModel("Scenes/Level7.mesh.xml");
                playerStartPos = new Vector3f(10, 2, -10);
                game.getPlayer().setStart(playerStartPos);
                ballStartPos = new Vector3f(10f, 2f, -20f);
                game.getBall().setPhysicsLocation(ballStartPos);
                createLavaBlock(300.0f, 1.0f, 300.0f, 50.0f, 31.0f, 50.0f);
                createGlassBlock(0.2f, 15.0f, 25.0f, 86.7f, 25.0f, -19.0f);
                createGlassBlock(0.2f, 30.0f, 21.8f, 86.7f, 5.0f, -65.6f);
                createGlassBlock(0.2f, 15.0f, 25.0f, 86.7f, 5.0f, -112.5f);
                createGlassBlock(0.2f, 15.0f, 25.0f, 43.4f, 5.0f, -19.0f);
                createGlassBlock(0.2f, 30.0f, 21.8f, 43.4f, 5.0f, -65.6f);
                createGlassBlock(0.2f, 15.0f, 25.0f, 43.4f, 5.0f, -112.5f);
                createGlassBlock(25.0f, 15.0f, 0.2f, 18.5f, 5.0f, -87.3f);
                createGlassBlock(21.5f, 15.0f, 0.2f, 65.0f, 5.0f, -87.3f);
                createGlassBlock(25.0f, 15.0f, 0.2f, 112.0f, 25.0f, -87.3f);
                createGlassBlock(25.0f, 15.0f, 0.2f, 18.5f, 25.0f, -43.5f);
                createGlassBlock(21.5f, 30.0f, 0.2f, 65.0f, 5.0f, -43.5f);
                createGlassBlock(25.0f, 15.0f, 0.2f, 112.0f, 5.0f, -43.5f);
                createRevGravSwitch(20.0f, 1.5f, -85.0f, 0.0f);
                createRevGravSwitch(20.0f, 1.5f, -90.0f, 0.0f);
                createNormGravSwitch(40.0f, 27.5f, -100.0f, 90.0f);
                createRevGravSwitch(40.0f, 1.5f, -20.0f, 90.0f);
                createNormGravSwitch(65.0f, 27.5f, -20.0f, 90.0f);
                createRevGravSwitch(127.0f, 1.5f, -35.0f, 90.0f);
                createNormGravSwitch(100.0f, 27.5f, -60.0f, 90.0f);
                createRevGravSwitch(110.0f, 1.5f, -126.0f, 0.0f);
                createNormGravSwitch(65.0f, 27.5f, -67.0f, 0.0f);
                createHoop(70.0f, 1.5f, -50.0f, 180.0f);
                createRevGravSwitch(72.0f, 1.5f, -51.0f, 30.0f);
                createRevGravSwitch(68.0f, 1.5f, -51.0f, 150.0f);
                createRevGravSwitch(72.0f, 1.5f, -49.5f, 90.0f);
                createRevGravSwitch(68.0f, 1.5f, -49.5f, 90.0f);
                createRevGravSwitch(70.0f, 1.5f, -49.5f, 0.0f);
                createLavaBlock(21.5f, 10.0f, 32.6f, 65.0f, -9.0f, -120.0f);
                createNormGravSwitch(61.0f, 27.5f, -125.5f, 90.0f);
                createNormGravSwitch(61.0f, 27.5f, -121.5f, 90.0f);
                break;
            case 7:
                //Level 7
                model = game.getMain().getAssetManager().loadModel("Scenes/Level6.mesh.xml");
                playerStartPos = new Vector3f(3.5f, 55f, -3.5f);
                game.getPlayer().setStart(playerStartPos);
                ballStartPos = new Vector3f(22f, 55f, -5f);
                game.getBall().setPhysicsLocation(ballStartPos);
                createLavaBlock(300.0f, 1.0f, 300.0f, 50.0f, -5.0f, 50.0f);
                createDownBlock(10.0f, 4.0f, 7.0f, 28.0f, 48.0f, -77.5f);
                createUpBlock(10.0f, 4.0f, 8.0f, 28.0f, 25.4f, -46.5f);
                createHoop(22.0f, 8.0f, -115.0f, 0.0f);
                createGlassBlock(0.3f, 62.0f, 59.0f, 18.0f, 0.0f, -60.0f);
                createLavaBlock(10.0f, 0.4f, 6.5f, 28.01f, 29.3f, -77.0f);
                createLavaBlock(10.0f, 0.4f, 8.0f, 28.01f, 28.3f, -46.5f);
                break;
             case 8:
                 //Level 8
                model=game.getMain().getAssetManager().loadModel("Scenes/level8.j3o");
                playerStartPos = new Vector3f(-60f,5f,-10f);
                game.getPlayer().setStart(playerStartPos);
                ballStartPos = new Vector3f(-60.0f, 1.0f, -8.0f);
                game.getBall().setPhysicsLocation(ballStartPos);
                createHoop(-20.0f, 2.0f, 15f,90f);
                createGlassBlock(.2f,10f,10f,-42f,5f,21f);
                createDownBlock(4.5f, .1f, 10f,-28f,15f,-15f);
                createLavaBlock(4.7f, .1f, 32.5f,-28f,-3f,-2f);
                break;    
            default:
                //test level
                model = game.getMain().getAssetManager().loadModel("Scenes/TestScene.j3o");
                playerStartPos = new Vector3f(0, 5, 20);
                game.getPlayer().setStart(playerStartPos);
                ballStartPos = new Vector3f(0, 5, 10);
                game.getBall().setPhysicsLocation(ballStartPos);
                createHoop(0.0f, 5.0f, 0.0f, 0.0f);
                createNormGravSwitch(15.0f, 5.0f, 0.0f, 0.0f);
                createNormGravSwitch(15.0f, 60.0f, 0.0f, 0.0f);
                createHighGravSwitch(5.0f, 5.0f, 0.0f, 0.0f);
                createLowGravSwitch(10.0f, 5.0f, 0.0f, 0.0f);
                createRevGravSwitch(20.0f, 5.0f, 0.0f, 0.0f);
                createGlassBlock(100.0f, 0.3f, 100.0f, 0.0f, 65.0f, 0.0f);
                createLavaBlock(25.0f, 10.0f, 25.0f, -80.0f, -7.0f, 75.0f);
                createDownBlock(5f, .5f, 10f, -20.0f, 20.0f, -20.0f);
                createDownBlock(5f, .5f, 10f, -5.0f, 20.0f, -20.0f);
                createUpDownBlock(5f, .5f, 10f, 10.0f, 5.0f, -20.0f);
                createUpDownBlock(5f, .5f, 10f, 25.0f, 5.0f, -20.0f);
                createUpBlock(5f, .5f, 10f, 40.0f, 5.0f, -20.0f);
                createUpBlock(5f, .5f, 10f, 55.0f, 5.0f, -20.0f);
                break;
        }
        model.setName("Level");
        model.setLocalScale(5f);
        CollisionShape sceneShape = CollisionShapeFactory.createMeshShape((Node) model);
        game.getMain().getRootNode().attachChild(model);
        landscape = new RigidBodyControl(sceneShape, 0);
        model.addControl(landscape);
        game.getBulletAppState().getPhysicsSpace().add(landscape);
    }

    public boolean nextLevel() {
        
        if (++levelNum <= NUMBEROFLEVELS) {
            clearLevel();
            loadLevel();
            return true;
        } else {
            return false;
        }
    }

    public void resetLevel() {
        clearLevel();
        loadLevel();
    }

    //checks each of the possible added geometry's and if they are there destroys them and the physics frame
    public void clearLevel() {
        if (game.getMain().getRootNode().hasChild(model)) {
            game.getMain().getRootNode().detachChild(model);
            game.getBulletAppState().getPhysicsSpace().removeCollisionObject(landscape);
        }
        clearMovableSurfaces();
        clearLists();
        game.getBall().setLinearVelocity(Vector3f.ZERO);
        game.getBall().setAngularVelocity(Vector3f.ZERO);
    }

    /*creates a completion hoop at specified coordinates
     For the rotation field just fill in 90.0f or 0.0.f, one makes the hoop face north/south the other east/west*/
    public void createHoop(float x, float y, float z, float rotation) {
        listIndex++;
        geomList.add(geom = new Geometry("Hoop", hoop));
        geom.setMaterial(game.getMain().magenta);
        geom.setLocalTranslation(x, y, z);
        geom.rotate(0.0f, rotation * FastMath.DEG_TO_RAD, 0.0f);
        game.getMain().getRootNode().attachChild(geom);
        physList.add(phys = new RigidBodyControl(0.0f));
        geom.addControl(phys);
        game.getBulletAppState().getPhysicsSpace().add(phys);
    }

    //creates a high gravity switch at specified coordinates
    public void createHighGravSwitch(float x, float y, float z, float rotation) {
        listIndex++;
        geomList.add(geom = new Geometry("HighGravSwitch", hoop));
        geom.setMaterial(game.getMain().yellow);
        geom.setLocalTranslation(x, y, z);
        geom.rotate(0.0f, rotation * FastMath.DEG_TO_RAD, 0.0f);
        game.getMain().getRootNode().attachChild(geom);
        physList.add(phys = new RigidBodyControl(0.0f));
        geom.addControl(phys);
        game.getBulletAppState().getPhysicsSpace().add(phys);
    }

    //creates a low gravity switch at specified coordinates
    public void createLowGravSwitch(float x, float y, float z, float rotation) {
        listIndex++;
        geomList.add(geom = new Geometry("LowGravSwitch", hoop));
        geom.setMaterial(game.getMain().red);
        geom.setLocalTranslation(x, y, z);
        geom.rotate(0.0f, rotation * FastMath.DEG_TO_RAD, 0.0f);
        game.getMain().getRootNode().attachChild(geom);
        physList.add(phys = new RigidBodyControl(0.0f));
        geom.addControl(phys);
        game.getBulletAppState().getPhysicsSpace().add(phys);
    }

    //creates a normal gravity switch at specified coordinates
    public void createNormGravSwitch(float x, float y, float z, float rotation) {
        listIndex++;
        geomList.add(geom = new Geometry("NormGravSwitch", hoop));
        geom.setMaterial(game.getMain().green);
        geom.setLocalTranslation(x, y, z);
        geom.rotate(0.0f, rotation * FastMath.DEG_TO_RAD, 0.0f);
        game.getMain().getRootNode().attachChild(geom);
        physList.add(phys = new RigidBodyControl(0.0f));
        geom.addControl(phys);
        game.getBulletAppState().getPhysicsSpace().add(phys);
    }

    //creates a reverse gravity switch at specified coordinates
    public void createRevGravSwitch(float x, float y, float z, float rotation) {
        listIndex++;
        geomList.add(geom = new Geometry("RevGravSwitch", hoop));
        geom.setMaterial(game.getMain().white);
        geom.setLocalTranslation(x, y, z);
        geom.rotate(0.0f, rotation * FastMath.DEG_TO_RAD, 0.0f);
        game.getMain().getRootNode().attachChild(geom);
        physList.add(phys = new RigidBodyControl(0.0f));
        geom.addControl(phys);
        game.getBulletAppState().getPhysicsSpace().add(phys);
    }

    //creates a glass block at specified coordinates
    public void createGlassBlock(float l, float h, float w, float x, float y, float z) {
        listIndex++;
        geomList.add(geom = new Geometry("GlassCeiling", new Box(l, h, w)));
        Material glassMat = new Material(game.getMain().getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        glassMat.setTexture("ColorMap", game.getMain().getAssetManager().loadTexture("Materials/glass.png"));
        glassMat.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        geom.setQueueBucket(RenderQueue.Bucket.Transparent);
        geom.setLocalTranslation(new Vector3f(x, y, z));
        geom.setMaterial(glassMat);
        game.getMain().getRootNode().attachChild(geom);
        physList.add(phys = new RigidBodyControl(0.0f));
        geom.addControl(phys);
        game.getBulletAppState().getPhysicsSpace().add(phys);
    }

    //creates a lava block at specified coordinates
    public void createLavaBlock(float l, float h, float w, float x, float y, float z) {
        listIndex++;
        geomList.add(geom = new Geometry("Lava", new Box(l, h, w)));
        Material lavaMat = new Material(game.getMain().getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        lavaMat.setTexture("ColorMap", game.getMain().getAssetManager().loadTexture("Materials/lava.jpg"));
        geom.setMaterial(lavaMat);
        geom.setLocalTranslation(new Vector3f(x, y, z));
        game.getMain().getRootNode().attachChild(geom);
        physList.add(phys = new RigidBodyControl(0.0f));
        geom.addControl(phys);
        game.getBulletAppState().getPhysicsSpace().add(phys);
    }

    public void createUpDownBlock(float l, float h, float w, float x, float y, float z) {
        Vector3f currentUpDownPosition;
        upDownIndex++;
        geom = new Geometry("SurfUpDown", new Box(l, h, w));
        geom.setMaterial(game.getMain().white);
        upDownPositionList.add(currentUpDownPosition = new Vector3f(x, y, z));
        geom.setLocalTranslation(currentUpDownPosition);
        nodeUpDown.attachChild(geom);
        upDownPhysList.add(upDownPhys = new RigidBodyControl(0.0f));
        geom.addControl(upDownPhys);
        game.getBulletAppState().getPhysicsSpace().add(upDownPhys);
    }

    public void createUpBlock(float l, float h, float w, float x, float y, float z) {
        Vector3f currentUpPosition;
        upIndex++;
        geom = new Geometry("SurfUp", new Box(l, h, w));
        geom.setMaterial(game.getMain().red);
        upPositionList.add(currentUpPosition = new Vector3f(x, y, z));
        geom.setLocalTranslation(currentUpPosition);
        nodeUp.attachChild(geom);
        upPhysList.add(upPhys = new RigidBodyControl(0.0f));
        geom.addControl(upPhys);
        game.getBulletAppState().getPhysicsSpace().add(upPhys);
    }

    public void createDownBlock(float l, float h, float w, float x, float y, float z) {
        Vector3f currentDownPosition;
        downIndex++;
        geom = new Geometry("SurfDown", new Box(l, h, w));
        geom.setMaterial(game.getMain().yellow);
        downPositionList.add(currentDownPosition = new Vector3f(x, y, z));
        geom.setLocalTranslation(currentDownPosition);
        nodeDown.attachChild(geom);
        downPhysList.add(downPhys = new RigidBodyControl(0.0f));
        geom.addControl(downPhys);
        game.getBulletAppState().getPhysicsSpace().add(downPhys);
    }

    public void moveSurfaceUp() {
        if (!upDownPositionList.isEmpty()) {
            nodeUpDown.move(0f, .5f, 0f);
            for (int i = 0; i < upDownIndex; i++) {
                upDownPositionList.get(i).setY(upDownPositionList.get(i).getY() + 0.5f);
                upDownPhysList.get(i).setPhysicsLocation(upDownPositionList.get(i));
            }
        }
        if (!upPositionList.isEmpty()) {
            nodeUp.move(0f, .5f, 0f);
            for (int i = 0; i < upIndex; i++) {
                upPositionList.get(i).setY(upPositionList.get(i).getY() + 0.5f);
                upPhysList.get(i).setPhysicsLocation(upPositionList.get(i));
            }
        }
    }

    public void moveSurfaceDown() {
        if (!upDownPositionList.isEmpty()) {
            nodeUpDown.move(0f, -.5f, 0f);
            for (int i = 0; i < upDownIndex; i++) {
                upDownPositionList.get(i).setY((upDownPositionList.get(i).getY() - 0.5f));
                upDownPhysList.get(i).setPhysicsLocation(upDownPositionList.get(i));
            }
        }
        if (!downPositionList.isEmpty()) {
            nodeDown.move(0f, -.5f, 0f);
            for (int i = 0; i < downIndex; i++) {
                downPositionList.get(i).setY((downPositionList.get(i).getY() - 0.5f));
                downPhysList.get(i).setPhysicsLocation(downPositionList.get(i));
            }
        }
    }

    public void clearMovableSurfaces() {
        nodeUpDown.detachAllChildren();
        nodeUp.detachAllChildren();
        nodeDown.detachAllChildren();
        nodeUpDown.setLocalTranslation(0f, 0f, 0f);
        nodeUp.setLocalTranslation(0f, 0f, 0f);
        nodeDown.setLocalTranslation(0f, 0f, 0f);
        while (upDownIndex > 0) {
            upDownIndex--;
            game.getBulletAppState().getPhysicsSpace().removeCollisionObject(upDownPhysList.get(upDownIndex));
        }
        while (upIndex > 0) {
            upIndex--;
            game.getBulletAppState().getPhysicsSpace().removeCollisionObject(upPhysList.get(upIndex));
        }
        while (downIndex > 0) {
            downIndex--;
            game.getBulletAppState().getPhysicsSpace().removeCollisionObject(downPhysList.get(downIndex));
        }
        while (listIndex > 0) {
            listIndex--;
            game.getMain().getRootNode().detachChild(geomList.get(listIndex));
            game.getBulletAppState().getPhysicsSpace().removeCollisionObject(physList.get(listIndex));
        }
    }

    public void clearLists() {
        upDownPositionList.clear();
        upPositionList.clear();
        downPositionList.clear();
        physList.clear();
        upDownPhysList.clear();
        upPhysList.clear();
        downPhysList.clear();
        geomList.clear();
    }

    public Spatial getModel() {
        return model;
    }

    public Vector3f getPlayerStart() {
        return playerStartPos;
    }

    public Vector3f getBallStart() {
        return ballStartPos;
    }

    public int getLevelNum() {
        return levelNum;
    }
}
