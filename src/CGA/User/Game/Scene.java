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
    private Renderable bicycle;
    private Renderable cubeOBJ;
    private Renderable bigWallOBJ;
    private Renderable stallOBJ;
    private Renderable cubeColor;
    private Renderable computer;


    // light:
    private PointLight pointLight;
    private SpotLight spotLight;
    private SpotLight sun;

    public Scene(GameWindow window) {
        this.window = window;
    }

    //scene setup
    public boolean init() {
        try {

            // TODO: Load StaticShader
            //simpleShader = new ShaderProgram("assets/shaders/simple_vert.glsl", "assets/shaders/simple_frag.glsl");
            simpleShader = new ShaderProgram("assets/shaders/tron_vert.glsl", "assets/shaders/tron_frag.glsl");

            // TODO: GROUND
            int stride = 8 * 4;
            VertexAttribute[] vertexAttributes = new VertexAttribute[3];

            vertexAttributes[0] = new VertexAttribute(3, GL_FLOAT, stride, 0);          //position attribute
            vertexAttributes[1] = new VertexAttribute(2, GL_FLOAT, stride, 3 * 4);      //texture attribute
            vertexAttributes[2] = new VertexAttribute(3, GL_FLOAT, stride, 5 * 4);      //normal attribute

            //OBJLoader.OBJResult bRes = OBJLoader.loadOBJ("assets/models/sphere.obj", false, false);
            OBJLoader.OBJResult bRes = OBJLoader.loadOBJ("assets/models/ground.obj", false, false);

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
            bodenRend.meshes.add(bodenMesh);


            // TODO: TRON BIKE / OBJECTS IN WORLD
            bicycle = new Renderable();
            bicycle = loader.loadModel("assets/Light Cycle/Light Cycle/HQ_Movie cycle.obj",(float) Math.toRadians(-90.0f),(float) Math.toRadians(90.0f),0);  //original
            bicycle.scaleLocal(new Vector3f(0.8f)); // original       // --> size of the object

            computer = new Renderable();
            computer = loader.loadModel("assets/Light Cycle/free-datsun-280z/source/Datsun_280Z/Datsun_280Z.obj",(float) Math.toRadians(0.0f),(float) Math.toRadians(90.0f),0);
            computer.scaleLocal(new Vector3f(2.0f)); // original       // --> size of the object

            // TODO: MULTIPLE TEST OBJECTS
            cubeOBJ = loader.loadModel("assets/Light Cycle/mauerCube/mauerCube.obj", (float) Math.toRadians(0.0f),(float) Math.toRadians(90.0f),0);   //test von mir mit einer mauer
            cubeOBJ.scaleLocal(new Vector3f(0.8f));                   // --> size of object
            cubeOBJ.translateGlobal(new Vector3f(10,1,0));       // --> position of object

            bigWallOBJ = loader.loadModel("assets/Light Cycle/grosseMauer/grosseMauer.obj", (float) Math.toRadians(0.0f),(float) Math.toRadians(90.0f),0);   //test von mir mit einem stall
            bigWallOBJ.scaleLocal(new Vector3f(0.8f));
            bigWallOBJ.translateGlobal(new Vector3f(-5,0,0));

            stallOBJ = loader.loadModel("assets/Light Cycle/stall/stall.obj", (float) Math.toRadians(0.0),(float) Math.toRadians(90.0f),0);   //test von mir mit einem stall
            stallOBJ.scaleLocal(new Vector3f(0.8f));
            stallOBJ.translateGlobal(new Vector3f(0,0,-15));

            cubeColor = loader.loadModel("assets/Light Cycle/multipleCubeColor2.obj", (float) Math.toRadians(0.0),(float) Math.toRadians(90.0f),0);   //test von mir mit einem stall
            cubeColor.scaleLocal(new Vector3f(0.8f));
            cubeColor.translateGlobal(new Vector3f(0,1,-3));

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


            // TODO: CAMERA (FIRST PERSON)
            cam = new TronCam();
            cam.setParent(bicycle);
            cam.translateLocal(new Vector3f(0.0f,2.0f,0.2f)); // z ist die entfernung zum object --> test von mir
            cam.rotateLocal(0, (float)Math.toRadians(10.0f), 0);

            // m4Boden.identity().rotateX((float) Math.toRadians(90)).scaleLocal(0.03f);
            // m4Boden.identity().rotate((float) Math.toRadians(90), new Vector3f(1.0f, 0, 0)).scaleLocal(0.03f);



            // TODO: BACKGROUNDCOLOR -->  r, g, b
            glClearColor(0.0f, 0.0f, 0.0f, 0.0f);  //--> original (schwarz)

            glDisable(GL_CULL_FACE);
            glFrontFace(GL_CCW);
            glCullFace(GL_BACK);
            glEnable(GL_DEPTH_TEST);
            glDepthFunc(GL_LESS);

            //characterMovement();
            collisionDetection();

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

        pointLight.setLightColor(new Vector3f((float)Math.sin(t),(float)Math.cos(t),0.5f));
        pointLight.bind(simpleShader,"Eins");
        spotLight.bind(simpleShader,"Zwei", cam.calculateViewMatrix());
        //sun.bind(simpleShader,"Zwei", cam.calculateViewMatrix());

        simpleShader.setUniform("Farbe", new Vector3f(0f,1f,0f));
        bodenRend.render(simpleShader);

        simpleShader.setUniform("Farbe", new Vector3f((float)Math.sin(t),(float)Math.cos(t),0.5f));
        bicycle.render(simpleShader);

        cubeOBJ.render(simpleShader);
        bigWallOBJ.render(simpleShader);
        stallOBJ.render(simpleShader);
        cubeColor.render(simpleShader);
        computer.render(simpleShader);

        simpleShader.cleanup();
        //  simpleMesh.render();
        //  mesh.render();
        //  rend.render();
    }

    public void update(float dt, float t) {
        float rotationMultiplier = 90.0f;
        float translationMultiplier = 5.0f;

        // TODO: BIKE ACCELERATION
        bicycle.translateLocal(new Vector3f(0.0f,0.0f,(-translationMultiplier/3) * dt));

        // TODO: CAMERA CHANGE
        changeCamera();

        // TODO: COLLISION DETECTION
        collisionDetection();
    }

    public void characterMovement() {

        boolean keyA = false;
        boolean keyD = false;

        Vector3f leftBorder = new Vector3f(-5, bicycle.getWorldPosition().y, bicycle.getWorldPosition().z);
        Vector3f rightBorder = new Vector3f(5, bicycle.getWorldPosition().y, bicycle.getWorldPosition().z);
        Vector3f center = new Vector3f(0, bicycle.getWorldPosition().y, bicycle.getWorldPosition().z);

        // TODO: CENTER CHECK
        if (bicycle.getWorldPosition().x == center.x) {
            keyA = true;
            keyD = true;
            System.out.println("-- center!"+ keyA+", "+ keyD);
        }

        // TODO: BORDER CHECK
        // LEFT BORDER:
        if (bicycle.getWorldPosition().x == leftBorder.x){
            // if bicycle is on left border
            System.out.println("-- left border");
            keyA = false;
            keyD = true;

            System.out.println("keyA: "+keyA);
            System.out.println("keyD: "+keyD);
        }
        // RIGHT BORDER:
        if (bicycle.getWorldPosition().x == rightBorder.x){
            // if bicycle is on right border
            System.out.println("-- right border");
            keyA = true;
            keyD = false;

            System.out.println("keyD: "+keyD);
            System.out.println("keyA: "+keyA);
        }

        // TODO: CHARACTER MOVEMENT
        // CHARACTER MOVES LEFT AND RIGHT
        if (window.getKeyState(GLFW_KEY_A) && keyA == true) {
            bicycle.translateGlobal(new Vector3f(-5, 0.0f,0.0f));
            System.out.println(keyA+", "+keyD);
            //bicycle.rotateLocal(0, 45.0f, 0);
        }
        if (window.getKeyState(GLFW_KEY_D) && keyD == true) {
            bicycle.translateGlobal(new Vector3f(5, 0.0f,0.0f));
            System.out.println(keyA+", "+keyD);
            //bicycle.rotateLocal(0, -45.0f, 0);
        }

        // CHARACTER IS NOT ABLE TO MOVE OVER BORDER
        if (window.getKeyState(GLFW_KEY_A) && keyA == false) {
            bicycle.translateGlobal(new Vector3f(0.0f, 0.0f, 0.0f));
            keyD = true;
        }
        if (window.getKeyState(GLFW_KEY_D) && keyD == false) {
            bicycle.translateGlobal(new Vector3f(0.0f, 0.0f,0.0f));
            keyA = true;
        }

    }

    public void changeCamera() {

         if (window.getKeyState(GLFW_KEY_C)){
            // TODO: FIRST PERSON
            cam = new TronCam();
            cam.setParent(bicycle);
            cam.translateLocal(new Vector3f(0.0f,2.0f,0.2f)); // z ist die entfernung zum object --> test von mir
            cam.rotateLocal(0, (float)Math.toRadians(10.0f), 0);
        }
        else if (window.getKeyState(GLFW_KEY_X)) {
            // TODO: BirdeyeView
            cam = new TronCam();
            cam.setParent(bicycle);
            cam.translateLocal(new Vector3f(0.0f, 7.0f, 0.0f));   // original!
            //cam.translateLocal(new Vector3f(2.0f,5.0f,10.0f)); // z ist die entfernung zum object --> test von mir
            cam.rotateLocal(-90, (float) Math.toRadians(0), 0);

            //bigWallOBJ.scaleLocal(new Vector3f(0.1f));
        }

    }
    public void onKey(int key, int scancode, int action, int mode) {
            characterMovement();
    }

    public void collisionDetection(){
        if (bicycle.getWorldXAxis() == bigWallOBJ.getWorldXAxis()){
            System.out.println("COLLISION!!!");
        }
        //System.out.println(bigWallOBJ.getPosition());
        //System.out.println("x achse: "+bigWallOBJ.getXAxis());
    }
    public void onMouseMove(double xpos, double ypos) {
        float rotationMultiplier = 0.00005f;
        float translationMultiplier = 0.000005f;
        //lightcycle.rotateLocal(0.0f, (float) (rotationMultiplier*xpos), 0.0f);
    }
    public void cleanup() {}
}
