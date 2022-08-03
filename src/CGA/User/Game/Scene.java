package CGA.User.Game;

import CGA.Framework.GameWindow;
import CGA.Framework.ModelLoader;
import CGA.User.DataStructures.Camera.TronCam;
import CGA.User.DataStructures.Geometry.Material;
import CGA.User.DataStructures.Geometry.Mesh;
import CGA.User.DataStructures.Geometry.Renderable;
import CGA.User.DataStructures.Geometry.VertexAttribute;
import CGA.User.DataStructures.Light.PointLight;
import CGA.User.DataStructures.Light.SpotLight;
import CGA.User.DataStructures.Light.Sunlight;
import CGA.User.DataStructures.ShaderProgram;
import CGA.Framework.OBJLoader;
import CGA.User.DataStructures.Texture2D;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
//import org.apache.commons.io.FileUtils;


import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

/**
 * Created by Fabian on 16.09.2017.
 */
public class Scene {
    private ShaderProgram simpleShader;
    private ShaderProgram sunShader;
    private Mesh kugelMesh;
    private Mesh bodenMesh;
    private Renderable bodenRend;
    private Renderable kugelRend;
    private Matrix4f m4Boden = new Matrix4f().identity();
    private Matrix4f m4Kugel = new Matrix4f().identity();
    private GameWindow window;
    private TronCam cam;
    private TronCam newCam;

    private ModelLoader loader;

    // objects:
    private Renderable city;
    private Renderable bicycle;
    private Renderable pinkCar;
    private Renderable cubeOBJ;
    private Renderable bigWallOBJ;
    private Renderable stallOBJ;
    private Renderable cubeColor;
    private Renderable gasstation;
    private Renderable trashcans;


    // light:
    private PointLight pointLight;
    private SpotLight spotLight;
    private Sunlight sunLight;

    public Scene(GameWindow window) {
        this.window = window;
    }

    //scene setup
    public boolean init() {
        try {

            // TODO: Load StaticShader
            //simpleShader = new ShaderProgram("assets/shaders/simple_vert.glsl", "assets/shaders/simple_frag.glsl");
            simpleShader = new ShaderProgram("assets/shaders/tron_vert.glsl", "assets/shaders/tron_frag.glsl");
            sunShader = new ShaderProgram("assets/shaders/tron_vert.glsl", "assets/shaders/tron_frag.glsl");


            // TODO: GROUND
            // mit dem boden vllt die straße bauen???
            // ansonsten fehlt dieser teil komplett?!?!? --> ist das schlecht?
            int stride = 8 * 4;
            VertexAttribute[] vertexAttributes = new VertexAttribute[3];

            vertexAttributes[0] = new VertexAttribute(3, GL_FLOAT, stride, 0);          //position attribute
            vertexAttributes[1] = new VertexAttribute(2, GL_FLOAT, stride, 3 * 4);      //texture attribute
            vertexAttributes[2] = new VertexAttribute(3, GL_FLOAT, stride, 5 * 4);      //normal attribute

            //OBJLoader.OBJResult bRes = OBJLoader.loadOBJ("assets/models/sphere.obj", false, false);
            OBJLoader.OBJResult bRes = OBJLoader.loadOBJ("assets/models/ground.obj", false, false);
            OBJLoader.OBJResult street = OBJLoader.loadOBJ("assets/models/ground.obj", false, false);

            Texture2D diff = new Texture2D("assets/textures/ground_diff.png",true);
            Texture2D emit = new Texture2D("assets/textures/ground_emit.png",true);
            Texture2D spec = new Texture2D("assets/textures/ground_spec.png",true);
            Vector2f tcMultiplier = new Vector2f(60.0f,60.0f);

            diff.setTexParams(GL_REPEAT, GL_REPEAT, GL_LINEAR_MIPMAP_LINEAR, GL_LINEAR);
            emit.setTexParams(GL_REPEAT, GL_REPEAT, GL_LINEAR_MIPMAP_LINEAR, GL_LINEAR);
            spec.setTexParams(GL_REPEAT, GL_REPEAT, GL_LINEAR_MIPMAP_LINEAR, GL_LINEAR);

            Material boden = new Material(diff, emit, spec, 60.0f, tcMultiplier);

            OBJLoader.OBJMesh bObjMesh = bRes.objects.get(0).meshes.get(0);
            bodenMesh = new Mesh(bObjMesh.getVertexData(), bObjMesh.getIndexData(), vertexAttributes, boden);
            bodenRend = new Renderable();
            //bodenRend.scaleLocal(new Vector3f(3,4,4));  // größe des Bodens kann hier angepasst werden
            bodenRend.meshes.add(bodenMesh);

        // test

            // TODO: BICYCLE / PLAYER
            bicycle = new Renderable();
            bicycle = loader.loadModel("assets/Light Cycle/Light Cycle/HQ_Movie cycle.obj",(float) Math.toRadians(-90.0f),(float) Math.toRadians(90.0f),0);  //original
            //bicycle = loader.loadModel("assets/Objects/Bicycle/bicycle.obj",(float) Math.toRadians(0.0f),(float) Math.toRadians(180.0f),0);
            bicycle.scaleLocal(new Vector3f(0.7f));                    // --> size of the object
            bicycle.translateGlobal((new Vector3f(3,0,23)));     // --> starting position for the bike

            // TODO: CITY
            city = new Renderable();
            city = loader.loadModel("assets/Objects/City/city/city.obj",(float) Math.toRadians(0.0f),(float) Math.toRadians(-90.0f),0);
            city.scaleLocal(new Vector3f(0.1f));
            city.translateGlobal((new Vector3f(-28.2f,0,-31)));

            // TODO: OBSTACLES
            pinkCar = new Renderable();
            pinkCar = loader.loadModel("assets/Objects/PinkCar/pinkCarr.obj",(float) Math.toRadians(0.0f),(float) Math.toRadians(-70.0f),0);
            pinkCar.scaleLocal(new Vector3f(1.7f));
            pinkCar.translateGlobal(new Vector3f(-2.5f,0,2));

            trashcans = new Renderable();
            trashcans = loader.loadModel("assets/Objects/Trashcan/neustadt_an_der_aisch_mulltonnen/trashcan.obj",(float) Math.toRadians(0.0f),(float) Math.toRadians(-45.0f),0);
            trashcans.scaleLocal(new Vector3f(1.28f));
            trashcans.translateGlobal(new Vector3f(3,0,13));

            bigWallOBJ = loader.loadModel("assets/Light Cycle/grosseMauer/grosseMauer.obj", (float) Math.toRadians(0.0f),(float) Math.toRadians(90.0f),0);   //test von mir mit einem stall
            bigWallOBJ.scaleLocal(new Vector3f(0.3f));
            bigWallOBJ.translateGlobal(new Vector3f(-4,0,3));

            cubeOBJ = loader.loadModel("assets/Light Cycle/mauerCube/mauerCube.obj", (float) Math.toRadians(0.0f),(float) Math.toRadians(90.0f),0);   //test von mir mit einer mauer
            cubeOBJ.scaleLocal(new Vector3f(3f));
            cubeOBJ.translateGlobal(new Vector3f(0,0,0));

            stallOBJ = loader.loadModel("assets/Light Cycle/stall2/stallNEU2.obj", (float) Math.toRadians(0.0),(float) Math.toRadians(90.0f),0);   //test von mir mit einem stall
            stallOBJ.scaleLocal(new Vector3f(0.5f));
            stallOBJ.translateGlobal(new Vector3f(4.5f,0,-12));

            gasstation = loader.loadModel("assets/Light Cycle/tankstelleNEU/energy_station_street_assets_vol._03/tankstelle.obj", (float) Math.toRadians(0.0),(float) Math.toRadians(90.0f),0);   //test von mir mit einem stall
            gasstation.scaleLocal(new Vector3f(1.0f));
            gasstation.translateGlobal(new Vector3f(-4.5f,0,-12));


            // TODO: LIGHT -->
            // TODO: COLORFUL LIGHT (Unterbodenbeleuchtung)
            pointLight = new PointLight(bicycle.getPosition(), new Vector3f(1.0f,1.0f,1.0f),1.0f,0.5f,0.1f);
            pointLight.setParent(bicycle);
            //pointLight.translateLocal(new Vector3f(0f,1f,0f));

            // TODO: SPOTLIGHT (Scheinwerfer)
            spotLight = new SpotLight(bicycle.getPosition(), new Vector3f(1f,1f,1.0f), 0.5f,0.05f, 0.01f, 50f, 70f);
            spotLight.setParent(bicycle);
            spotLight.translateLocal(new Vector3f(0f,1.0f,-2.0f));

            //sun = new SpotLight(new Vector3f(0,10,0), new Vector3f(1f,1f,1.0f), 0.5f,0.05f, 0.01f, 50f, 70f);
            //sun.translateLocal(new Vector3f(0f,1.0f,-2.0f));

            //spotLight_Test = new SpotLight(lightcycle.getPosition(), new Vector3f(1f,1f,1.0f), 0.5f,0.05f, 0.01f, 50f, 70f);
            //spotLight_Test.translateLocal(new Vector3f(0f,1.0f,-2.0f));

            // TODO: CAMERA (FIRST PERSON) --> DEFAULT
            cam = new TronCam();
            cam.setParent(bicycle);
            cam.translateLocal(new Vector3f(0,2.3f,-0.2f)); // z ist die entfernung zum object --> test von mir
            //cam.rotateLocal(0, (float)Math.toRadians(10.0f), 0);
            cam.rotateLocal(-15, 0, 0);

            // m4Boden.identity().rotateX((float) Math.toRadians(90)).scaleLocal(0.03f);
            // m4Boden.identity().rotate((float) Math.toRadians(90), new Vector3f(1.0f, 0, 0)).scaleLocal(0.03f);

            // TODO: COLLISION DETECTION
            collisionDetection();

            // TODO: BACKGROUNDCOLOR -->  r, g, b
            glClearColor(0.3f, 0.6f, 0.70f, 0.0f);  //--> original (schwarz)

            glDisable(GL_CULL_FACE);
            glFrontFace(GL_CCW);
            glCullFace(GL_BACK);
            glEnable(GL_DEPTH_TEST);
            glDepthFunc(GL_LESS);

            return true;
        } catch (Exception ex) {
            System.err.println("Scene initialization failed:\n" + ex.getMessage() + "\n");
            return false;
        }
    }


    public void render(float dt, float t) {
        //TODO: Place your code here. Call the rendering of the created mesh every frame. Specify the used shader first.
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        simpleShader.use();
        cam.bind(simpleShader);

        //simpleShader.setUniform("model_matrix", m4Kugel, false);
        //kugelRend.render(simpleShader);
        //simpleShader.setUniform("model_matrix", m4Boden, false);

        pointLight.setLightColor(new Vector3f((float)Math.sin(t), (float)Math.cos(t), 0.5f));
        pointLight.bind(simpleShader,"Eins");
        spotLight.bind(simpleShader,"Zwei", cam.calculateViewMatrix());
        //sun.bind(simpleShader,"Zwei", cam.calculateViewMatrix());

        simpleShader.setUniform("Farbe", new Vector3f(0f,1f,0f));
        //bodenRend.render(simpleShader);

        simpleShader.setUniform("Farbe", new Vector3f((float)Math.sin(t), (float)Math.cos(t), 0.5f));

        bicycle.render(simpleShader);
        city.render(simpleShader);

        // obstacles:
        /*cubeOBJ.render(simpleShader);
        bigWallOBJ.render(simpleShader);
        stallOBJ.render(simpleShader);
        gasstation.render(simpleShader);*/
        //pinkCar.render(simpleShader);
        //trashcans.render(simpleShader);

        simpleShader.cleanup();
        //  simpleMesh.render();
        //  mesh.render();
        //  rend.render();
    }

    public void update(float dt, float t) {
        float rotationMultiplier = 90.0f;
        float translationMultiplier = 5.0f;

        // TODO: BIKE ACCELERATION
        //bicycle.translateLocal(new Vector3f(0.0f,0.0f,(-translationMultiplier/3) * dt));

        Vector3f collisionObject = new Vector3f(0, 0, -10.311183f);

        if (bicycle.getWorldPosition().z == collisionObject.z) {
            System.out.println("COLLISION!!!");
            bicycle.translateLocal(new Vector3f(0.0f,0.0f,0)); // Bike stops!
        } else {
            bicycle.translateLocal(new Vector3f(0.0f,0.0f,(-translationMultiplier/1.0f) * dt));
        }

        //System.out.println("tankstelle: ("+gasstation.getXAxis().x +", "+ gasstation.getYAxis().y+", " + gasstation.getZAxis().z+")");

        // TODO: CAMERA CHANGE
        changeCamera();

        // TODO: COLLISION DETECTION
        /*if (collisionDetection() == false) {
            bicycle.translateLocal(new Vector3f(0.0f,0.0f,(-translationMultiplier/3) * dt));
        }*/

        //System.out.println("position: "+bicycle.getWorldPosition().x);
        //System.out.println("position: "+bicycle.getWorldPosition().z);
        //System.out.println("Collisionsobjekt: "+collisionObject.z);

        // TODO: BIKE RESET
        //resetBike();
    }

    /*public void resetBike() {
        if (window.getKeyState(GLFW_KEY_R)){
            bicycle.translateGlobal((new Vector3f(0,0,23)));     // --> starting position for the bike

        }
    }*/

        public void characterWorkingMovement() {
            // hin und her wackeln vom fahrradfahrer während er fährt
        }

        public void characterMovement() {

        boolean keyA = true;
        boolean keyD = true;

        Vector3f leftBorder = new Vector3f(-3, bicycle.getWorldPosition().y, bicycle.getWorldPosition().z);
        Vector3f rightBorder = new Vector3f(3, bicycle.getWorldPosition().y, bicycle.getWorldPosition().z);
        Vector3f center = new Vector3f(0, bicycle.getWorldPosition().y, bicycle.getWorldPosition().z);

        // TODO: CENTER CHECK
        /*if (bicycle.getWorldPosition().x == center.x) {
            //keyA = true;
            //keyD = true;
            System.out.println("-- center!"+ keyA+", "+ keyD);
        }*/

        // TODO: BORDER CHECK
        // LEFT BORDER:
        if (bicycle.getWorldPosition().x == leftBorder.x) {
            // if bicycle is on left border
            //System.out.println("-- left border");
            keyA = false;
            /*System.out.println("keyA: "+keyA);
            System.out.println("keyD: "+keyD);*/
            //bicycle.rotateLocal(0,-45,0);

        }
        // RIGHT BORDER:
        if (bicycle.getWorldPosition().x == rightBorder.x) {
            // if bicycle is on right border
            //System.out.println("-- right border");
            keyD = false;
            //bicycle.rotateLocal(0,45,0);
            /*System.out.println("keyD: "+keyD);
            System.out.println("keyA: "+keyA);*/
        }

        // TODO: CHARACTER MOVEMENT
        // CHARACTER MOVES LEFT AND RIGHT
        if (window.getKeyState(GLFW_KEY_A) && keyA == true) {
            bicycle.translateGlobal(new Vector3f(-3, 0.0f,0.0f));
            //System.out.println(keyA+", "+keyD);
            //bicycle.rotateLocal(0, 45.0f, 0);
        }
        if (window.getKeyState(GLFW_KEY_D) && keyD == true) {
            bicycle.translateGlobal(new Vector3f(3, 0.0f,0.0f));
            System.out.println(keyA+", "+keyD);
            //bicycle.rotateLocal(0, -45.0f, 0);
        }

        // CHARACTER IS NOT ABLE TO MOVE OVER BORDER
        if (window.getKeyState(GLFW_KEY_A) && keyA == false) {
            bicycle.translateGlobal(new Vector3f(0.0f, 0.0f, 0.0f));
            //bicycle.rotateLocal(0, 0.0f, 0);
            keyD = true;
        }
        if (window.getKeyState(GLFW_KEY_D) && keyD == false) {
            bicycle.translateGlobal(new Vector3f(0.0f, 0.0f,0.0f));
            //bicycle.rotateLocal(0, 0.0f, 0);
            keyA = true;
        }

            //System.out.println(leftBorder.x);
            //System.out.println(bicycle.getWorldPosition().x);

            // nur ein TEST
            /*if (window.getKeyState(GLFW_KEY_A)) {
                bicycle.rotateLocal(0,45,0);
            }
            if (window.getKeyState(GLFW_KEY_D)) {
                bicycle.rotateLocal(0,-45,0);
            }



            if (bicycle.getWorldPosition().x == leftBorder.x){
                System.out.println("ich rotiere!!!");
                //bicycle.rotateLocal(0,-45,0);
            }
            if (bicycle.getWorldPosition().x == rightBorder.x){
                bicycle.rotateLocal(0,45,0);
                System.out.println("ich rotiere!!!");
            }*/



    }

    public void changeCamera() {

         if (window.getKeyState(GLFW_KEY_C)) {
            // TODO: FIRST PERSON
            cam = new TronCam();
            cam.setParent(bicycle);
            cam.translateLocal(new Vector3f(0.0f,2.3f,-0.2f));
            cam.rotateLocal(-15, 0, 0);
        }
        else if (window.getKeyState(GLFW_KEY_X)) {
            // TODO: BirdeyeView
            cam = new TronCam();
            cam.setParent(bicycle);
            cam.translateLocal(new Vector3f(0.0f, 10.0f, 0.0f));
            //cam.translateLocal(new Vector3f(2.0f,5.0f,10.0f));
            cam.rotateLocal(-90, 0, 0);

            //bigWallOBJ.scaleLocal(new Vector3f(0.1f));
        }else if (window.getKeyState(GLFW_KEY_Q)) {
            // NUR OPTIONAL
            // TODO: LOOK LEFT
            cam = new TronCam();
            cam.setParent(bicycle);
            cam.translateLocal(new Vector3f(-1.0f,2.0f,0.2f));
            cam.rotateLocal(0, (float)Math.toRadians(10.0f), 0);
        }
        else if (window.getKeyState(GLFW_KEY_E)) {
            // TODO: LOOK RIGHT
            cam = new TronCam();
            cam.setParent(bicycle);
            cam.translateLocal(new Vector3f(1.0f,2.0f,0.2f));
            cam.rotateLocal(0, (float)Math.toRadians(10.0f), 0);
        }

    }
    public void onKey(int key, int scancode, int action, int mode) {
        characterMovement();
    }

    public boolean collisionDetection(){

        /*Vector3f collisionObject = new Vector3f(0, 0, -10.311183f);

        if (bicycle.getWorldPosition().z == collisionObject.z) {
            System.out.println("COLLISION!!!");
            bicycle.translateLocal(new Vector3f(0.0f,0.0f,0));
            return true;
        } else {
            //bicycle.translateLocal(new Vector3f(0.0f,0.0f,(-translationMultiplier/3) * dt));
            return false;
        }*/

        return false;
    }
    public void onMouseMove(double xpos, double ypos) {
        float rotationMultiplier = 0.00005f;
        float translationMultiplier = 0.000005f;
        //lightcycle.rotateLocal(0.0f, (float) (rotationMultiplier*xpos), 0.0f);
    }
    public void cleanup() {}
}
