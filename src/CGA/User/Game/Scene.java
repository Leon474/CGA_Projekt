package CGA.User.Game;

import CGA.Framework.GameWindow;
import CGA.Framework.ModelLoader;
import CGA.User.DataStructures.Camera.TronCam;
import CGA.User.DataStructures.Geometry.Material;
import CGA.User.DataStructures.Geometry.Mesh;
import CGA.User.DataStructures.Geometry.Renderable;
import CGA.User.DataStructures.Geometry.VertexAttribute;
import CGA.User.DataStructures.Light.DirectionalLight;
import CGA.User.DataStructures.Light.PointLight;
import CGA.User.DataStructures.Light.SpotLight;
import CGA.User.DataStructures.ShaderProgram;
import CGA.Framework.OBJLoader;
import CGA.User.DataStructures.Skybox.SkyBox;
import CGA.User.DataStructures.Texture2D;
import CGA.User.DataStructures.TextureSkybox;
import org.joml.*;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.CallbackI;
//import org.apache.commons.io.FileUtils;


import java.lang.Math;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengles.EXTSparseTexture.GL_TEXTURE_CUBE_MAP;
//import static org.lwjgl.opengles.GLES20.*;
import static org.lwjgl.opengles.GLES30.GL_TEXTURE_WRAP_R;
import static org.lwjgl.stb.STBImage.*;

/**
 * Created by Fabian on 16.09.2017.
 */
public class Scene {

    /*private Mesh kugelMesh;
    private Renderable kugelRend;
    private Matrix4f m4Boden = new Matrix4f().identity();
    private Matrix4f m4Kugel = new Matrix4f().identity();*/

    private ShaderProgram simpleShader;
    private ShaderProgram skyboxShader;
    private Mesh bodenMesh;
    private Renderable bodenRend;
    private GameWindow window;
    private TronCam cam;

    private ModelLoader loader;

    // main objects:
    private Renderable city;
    private Renderable bicycle;

    // obstacles:
    private Renderable pinkCar;
    private Renderable stallOBJ;
    private Renderable gasstation;
    private Renderable trashcans;
    private Renderable americanTrashcan;
    private Renderable policeCar;
    private Renderable taxi;
    private Renderable trafficCones;
    private Renderable bus;
    private Renderable football;
    private Renderable finish;
    private Renderable winnerCup;

    // light:
    private PointLight pointLight;
    private SpotLight spotLight;
    private DirectionalLight sunlight;

    private SkyBox skybox;
    private int cubeMapTexture;

    //
    private boolean triggerPoint = false;

    public Scene(GameWindow window) {
        this.window = window;
    }

    //scene setup
    public boolean init() {
        try {

            // TODO: LOAD SHADER
            simpleShader = new ShaderProgram("assets/shaders/tron_vert.glsl", "assets/shaders/tron_frag.glsl");
            skyboxShader = new ShaderProgram("assets/shaders/skyboxShader/skybox_vert.glsl", "assets/shaders/skyboxShader/skybox_frag.glsl");

            // TODO: GROUND
            // mit dem boden vllt die straße bauen???
            // ansonsten fehlt dieser teil komplett?!?!? --> ist das schlecht?
           /* int stride = 8 * 4;
            VertexAttribute[] vertexAttributes = new VertexAttribute[3];

            vertexAttributes[0] = new VertexAttribute(3, GL_FLOAT, stride, 0);          //position attribute
            vertexAttributes[1] = new VertexAttribute(2, GL_FLOAT, stride, 3 * 4);      //texture attribute
            vertexAttributes[2] = new VertexAttribute(3, GL_FLOAT, stride, 5 * 4);      //normal attribute

            //OBJLoader.OBJResult bRes = OBJLoader.loadOBJ("assets/models/sphere.obj", false, false);
            OBJLoader.OBJResult bRes = OBJLoader.loadOBJ("assets/models/ground.obj", false, false);
            //OBJLoader.OBJResult street = OBJLoader.loadOBJ("assets/models/ground.obj", false, false);

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
            bodenRend.meshes.add(bodenMesh);*/


            // TODO: SKYBOX
            float SIZE = 150.0f;
            float skyboxVertices[] = {
                    -SIZE, -SIZE, SIZE,
                    SIZE, -SIZE, SIZE,
                    SIZE, -SIZE, -SIZE,
                    -SIZE, -SIZE, -SIZE,
                    -SIZE, SIZE, SIZE,
                    SIZE, SIZE, SIZE,
                    SIZE, SIZE, -SIZE,
                    -SIZE, SIZE, -SIZE
            };

            int skyboxIndices[] = {
                    1,2,6,
                    6,5,1,
                    0,4,7,
                    7,3,0,
                    4,5,6,
                    6,7,4,
                    0,3,2,
                    2,1,0,
                    0,1,5,
                    5,4,0,
                    3,7,6,
                    6,2,3
            };

            skybox = new SkyBox(skyboxVertices, skyboxIndices);
            cubeMapTexture = glGenTextures();

            /*String facesCubemap [] = {
                    "assets/textures/skybox/right.jpg",
                    "assets/textures/skybox/left.png",
                    "assets/textures/skybox/top.png",
                    "assets/textures/skybox/bottom.png",
                    "assets/textures/skybox/front.png",
                    "assets/textures/skybox/back.png"
            };*/

            String facesCubemap [] = {
                    "assets/textures/skyBoxTest/tag_rechts.png",
                    "assets/textures/skyBoxTest/tag_links.png",
                    "assets/textures/skyBoxTest/tag_oben.png",
                    "assets/textures/skyBoxTest/tag_unten.png",
                    "assets/textures/skyBoxTest/tag_vorne.png",
                    "assets/textures/skyBoxTest/tag_hinten.png"
            };

            /*String facesCubemap [] = {
                    "assets/textures/skyboxEigen/sky.right.png",
                    "assets/textures/skyboxEigen/sky.left.png",
                    "assets/textures/skyboxEigen/sky.up.png",
                    "assets/textures/skyboxEigen/sky.down.png",
                    "assets/textures/skyboxEigen/sky.front.png",
                    "assets/textures/skyboxEigen/sky.back.png"
            };*/

            cubeMapTexture = skybox.loadCubemap(facesCubemap);

            // TODO: CREATE OBJECTS
            /** BICYCLE / PLAYER **/
            bicycle = new Renderable();
            //bicycle = loader.loadModel("assets/Light Cycle/Light Cycle/HQ_Movie cycle.obj",(float) Math.toRadians(-90.0f),(float) Math.toRadians(90.0f),0);  //original
            bicycle = loader.loadModel("assets/Objects/Bicycle/bicycle/bicycle.obj",(float) Math.toRadians(0.0f),(float) Math.toRadians(180.0f),0);
            bicycle.scaleLocal(new Vector3f(0.85f));                    // --> size of the object
            bicycle.translateGlobal((new Vector3f(3,0,23)));      // --> starting position for the bike
            //bicycle.setBoundingbox(2,1,1,2);
            bicycle.setNearBoundingbox(0.0f);

            /** CITY **/
            city = new Renderable();
            city = loader.loadModel("assets/Objects/City/city/city.obj",(float) Math.toRadians(0.0f),(float) Math.toRadians(-90.0f),0);
            city.scaleLocal(new Vector3f(0.1f));
            city.translateGlobal((new Vector3f(-28.2f,0,-31)));

            /** OBSTACLES **/
            pinkCar = new Renderable();
            pinkCar = loader.loadModel("assets/Objects/PinkCar/pinkCar/pinkCar.obj",(float) Math.toRadians(0.0f),(float) Math.toRadians(-90.0f),0);
            pinkCar.scaleLocal(new Vector3f(1.3f));
            pinkCar.translateGlobal(new Vector3f(-2.5f,0,2));
            //pinkCar.setNearBoundingbox(2.0f);

            trashcans = new Renderable();
            trashcans = loader.loadModel("assets/Objects/Trashcan/neustadt_an_der_aisch_mulltonnen/trashcan.obj",(float) Math.toRadians(0.0f),(float) Math.toRadians(70.0f),0);
            trashcans.scaleLocal(new Vector3f(1.28f));
            trashcans.translateGlobal(new Vector3f(-2,0,-13.5f));

            americanTrashcan = new Renderable();
            americanTrashcan = loader.loadModel("assets/Objects/Trashcan/american_trashcan/americanTrashcan.obj",(float) Math.toRadians(0.0f),(float) Math.toRadians(-45.0f),0);
            americanTrashcan.scaleLocal(new Vector3f(1.28f));
            americanTrashcan.translateGlobal(new Vector3f(2.5f,0.69f,13));

            policeCar = new Renderable();
            policeCar = loader.loadModel("assets/Objects/policeCar/ford_mondeo_police_ver.1(1)/policeCar.obj",(float) Math.toRadians(0.0f),(float) Math.toRadians(-70.0f),0);
            policeCar.scaleLocal(new Vector3f(0.74f));
            policeCar.translateGlobal(new Vector3f(20f,0.7f,-26));

            taxi = new Renderable();
            taxi = loader.loadModel("assets/Objects/Taxi/taxi/taxi.obj",(float) Math.toRadians(0.0f),(float) Math.toRadians(90.0f),0);
            taxi.scaleLocal(new Vector3f(0.75f));
            taxi.translateGlobal(new Vector3f(-35f,0.07f,-23));

            trafficCones = new Renderable();
            trafficCones = loader.loadModel("assets/Objects/trafficCones/traffic_cone/trafficCones.obj",(float) Math.toRadians(0.0f),(float) Math.toRadians(0.0f),0);
            trafficCones.scaleLocal(new Vector3f(0.60f));
            trafficCones.translateGlobal(new Vector3f(2.0f,0.0f,-7));

            bus = new Renderable();
            bus = loader.loadModel("assets/Objects/bus/hcr2_bus/buss.obj",(float) Math.toRadians(0.0f),(float) Math.toRadians(0.0f),0);
            bus.scaleLocal(new Vector3f(1.0f));
            bus.translateGlobal(new Vector3f(-2.0f,0.0f,-30.0f));

            football = new Renderable();
            football = loader.loadModel("assets/Objects/football/common_soccer_ball/football.obj",(float) Math.toRadians(0.0f),(float) Math.toRadians(0.0f),0);
            football.scaleLocal(new Vector3f(1.0f));
            //football.translateGlobal(new Vector3f(-85.0f,0.0f,-55.0f));
            football.translateGlobal(new Vector3f(0.0f,0.0f,10.0f));

            finish = new Renderable();
            finish = loader.loadModel("assets/Objects/Finishline/finish/finish.obj",(float) Math.toRadians(0.0f),(float) Math.toRadians(0.0f),0);
            finish.scaleLocal(new Vector3f(2.1f));
            finish.translateGlobal(new Vector3f(0.0f,0.0f,-85.4f));

            winnerCup = new Renderable();
            winnerCup = loader.loadModel("assets/Objects/winnerCup/kings_cup/winnerCup.obj",(float) Math.toRadians(0.0f),(float) Math.toRadians(0.0f),0);
            winnerCup.scaleLocal(new Vector3f(2.0f));
            winnerCup.translateGlobal(new Vector3f(0.0f,-5.0f,-92.0f));

            stallOBJ = loader.loadModel("assets/Light Cycle/stall2/stallNEU2.obj", (float) Math.toRadians(0.0),(float) Math.toRadians(90.0f),0);   //test von mir mit einem stall
            stallOBJ.scaleLocal(new Vector3f(0.5f));
            stallOBJ.translateGlobal(new Vector3f(4.5f,0,-12));

            gasstation = loader.loadModel("assets/Light Cycle/tankstelleNEU/energy_station_street_assets_vol._03/tankstelle.obj", (float) Math.toRadians(0.0),(float) Math.toRadians(90.0f),0);   //test von mir mit einem stall
            gasstation.scaleLocal(new Vector3f(1.0f));
            gasstation.translateGlobal(new Vector3f(-4.5f,0,-12));


            // TODO: LIGHT -->
            /** POINTLIGHT **/
            pointLight = new PointLight(bicycle.getPosition(), new Vector3f(1.0f,1.0f,1.0f),1.0f,0.5f,0.1f); // original
            //pointLight = new PointLight(city.getPosition(), new Vector3f(1.0f,1.0f,1.0f),1.0f,0.5f,0.1f);
            pointLight.setParent(bicycle);
            pointLight.translateLocal(new Vector3f(0,1,-2));

            //pointLight = new PointLight(new Vector3f(0,2,0), new Vector3f(1.0f,1.0f,1.0f),1.0f,0.5f,0.1f);

            //pointLight = new PointLight(bicycle.getPosition(), new Vector3f(1.0f,0.0f,0.0f),1.0f,0.5f,0.1f);
            //pointLight.setParent(bicycle);
            //pointLight.translateGlobal((new Vector3f(3,0,23)));      // --> starting position for the bike

            /** SPOTLIGHT (Scheinwerfer) **/
            spotLight = new SpotLight(bicycle.getPosition(), new Vector3f(1.0f,1.0f,1.0f), 0.5f,0.05f, 0.01f, 50f, 70f);
            spotLight.setParent(bicycle);
            spotLight.translateLocal(new Vector3f(0f,1.0f,-2.0f)); // original
            //spotLight.translateGlobal((new Vector3f(3,0,23)));      // --> starting position for the bike

            /** SUNLIGHT **/
            Vector3f lightColor = new Vector3f(1.0f,1.0f,1.0f);
            sunlight = new DirectionalLight(lightColor);

            // TODO: CAMERA (FIRST PERSON) --> DEFAULT
            cam = new TronCam();
            cam.setParent(bicycle);
            cam.translateLocal(new Vector3f(0,1.8f,-0.1f)); // z ist die entfernung zum object --> test von mir
            cam.rotateLocal(-15, 0, 0);

            // TODO: COLLISION DETECTION
            collisionDetection();

            // TODO: BACKGROUNDCOLOR -->  r, g, b
            glClearColor(0.3f, 0.7f, 0.78f, 0.0f);  //--> original (schwarz)

            glDisable(GL_CULL_FACE);
            glFrontFace(GL_CCW);
            glCullFace(GL_BACK);
            glEnable(GL_DEPTH_TEST);
            glDepthFunc(GL_LESS);

            return true;

            // m4Boden.identity().rotateX((float) Math.toRadians(90)).scaleLocal(0.03f);
            // m4Boden.identity().rotate((float) Math.toRadians(90), new Vector3f(1.0f, 0, 0)).scaleLocal(0.03f);

        } catch (Exception ex) {
            System.err.println("Scene initialization failed:\n" + ex.getMessage() + "\n");
            return false;
        }
    }


    public void render(float dt, float t) {
        //TODO: Place your code here. Call the rendering of the created mesh every frame. Specify the used shader first.
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        // TODO: SKYBOX
        glDepthFunc(GL_LEQUAL);
        skyboxShader.use();

        skyboxShader.setUniform("view", cam.calculateViewMatrix(), false);
        skyboxShader.setUniform("projection", cam.calculateProjectionMatrix(), false);

        glBindVertexArray(skybox.skyboxVAO);
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_CUBE_MAP, cubeMapTexture);
        glDrawElements(GL_TRIANGLES, 36, GL_UNSIGNED_INT, 0);

        glBindVertexArray(0);
        glDepthFunc(GL_LESS);

        simpleShader.use();
        cam.bind(simpleShader);

        // TODO: LIGHTS
        // POINTLIGHT:
        pointLight.setLightColor(new Vector3f((float)Math.sin(t), (float)Math.cos(t), 0.5f));  // original
        pointLight.bind(simpleShader,"Eins");
        simpleShader.setUniform("Farbe", new Vector3f((float)Math.sin(t),(float)Math.cos(t),0.5f));

        // SPOTLIGHT:
        spotLight.bind(simpleShader,"Zwei", cam.calculateViewMatrix());

        // SUNLIGHT:
        sunlight.bind(simpleShader,"Sun");

        // TODO: OBJECTS
        /** player **/
        bicycle.render(simpleShader);
        /** city **/
        city.render(simpleShader);
        /** obstacles **/
        pinkCar.render(simpleShader);
        policeCar.render(simpleShader);
        taxi.render(simpleShader);
        trafficCones.render(simpleShader);
        bus.render(simpleShader);
        //football.render(simpleShader);
        finish.render(simpleShader);
        winnerCup.render(simpleShader);


        trashcans.render(simpleShader);
        americanTrashcan.render(simpleShader);


        //simpleShader.cleanup();
        //skyboxShader.cleanup();


        //bodenRend.render(simpleShader);

        //simpleShader.cleanup();

        // simpleShader.setUniform("model_matrix", m4Kugel, false);
        // kugelRend.render(simpleShader);
        // simpleShader.setUniform("model_matrix", m4Boden, false);

        // simpleMesh.render();
        // mesh.render();
        // rend.render();
    }

    public void update(float dt, float t) {
        float rotationMultiplier = 90.0f;
        float translationMultiplier = 5.0f;

        // TODO: BIKE MOVEMENT
        float finishPosition = -88.49058f;
        float cupFinishPosition = 0.08333333f;


       /* if (bicycle.getNearBoundingPosition().z <= pinkCar.getNearBoundingPosition().z && bicycle.getWorldPosition().x == pinkCar.getWorldPosition().x)

        {
            System.out.println("koooolisoi");
        }*/



       /* if (bicycle.getWorldPosition().z == pinkCar.getWorldPosition().z && bicycle.getWorldPosition().x == pinkCar.getWorldPosition().x) {
            System.out.println("COLLISION!!!");
            bicycle.translateLocal(new Vector3f(0.0f,0.0f,0)); // Bike stops!


*/
        /*} else*/

        /*if (bicycle.getWorldPosition().z < pinkCar.getWorldPosition().z + 3){
            System.out.println("collision");
            bicycle.translateLocal(new Vector3f(0.0f,0.0f,0.0f));
        }
        else*/

        //System.out.println(bicycle.setNearBoundingbox(-2.0f));

        /*if (bicycle.setNearBoundingbox(-2.0f) <= pinkCar.setNearBoundingbox(1.0f) && bicycle.getWorldPosition().x == pinkCar.getWorldPosition().x
        && bicycle.setFarBoundingbox(2.0f) >= pinkCar.setFarBoundingbox(1.0f)) {
            System.out.println("ich kollidiere");
            bicycle.translateLocal(new Vector3f(0.0f,0.0f,0.0f));
            //bicycle.rotateLocal(0.0f,0.0f,4.0f);
        }*/

        /*if (bicycle.getNearBoundingPosition().z <= pinkCar.getNearBoundingPosition().z && bicycle.getWorldPosition().x == pinkCar.getWorldPosition().x
                && bicycle.setFarBoundingbox(2.0f) >= pinkCar.setFarBoundingbox(1.0f)) {
            System.out.println("ich kollidiere");
            bicycle.translateLocal(new Vector3f(0.0f,0.0f,0.0f));
            //bicycle.rotateLocal(0.0f,0.0f,4.0f);
        }*/




        /*else*/ if (bicycle.getWorldPosition().z == finishPosition) {
            /** bike stops at finsishpoint **/
            bicycle.translateLocal(new Vector3f(0.0f,0.0f,0.0f));
            winnerCup.rotateLocal(0, (rotationMultiplier/1.0f) * dt,0);
            /** winner cup movement **/
            if (winnerCup.getWorldPosition().y == cupFinishPosition){
                winnerCup.translateLocal(new Vector3f(0.0f,0.0f,0.0f));
            } else {
                winnerCup.translateLocal(new Vector3f(0.0f,(translationMultiplier/1.0f) * dt,0.0f));
            }
        } else {
            /** bike movement **/
            bicycle.translateLocal(new Vector3f(0.0f,0.0f,(-translationMultiplier/1.0f) * dt));
            //bicycle.rotateLocal(0, 0,(rotationMultiplier/1.0f) * dt);
        }


        // TODO: OBSTACLE MOVEMENT
        /** policecar **/
        float policeCarSTOP = -20.29932f;
        if (policeCar.getWorldPosition().x == policeCarSTOP) {
            policeCar.translateLocal(new Vector3f(0.0f,0.0f, 0.0f));
        } else {
            policeCar.translateLocal(new Vector3f((-translationMultiplier/0.8f) * dt,0.0f, 0.0f));
        }

        /** taxi **/
        float taxiCarSTOP = 3.2209985f;
        if (taxi.getWorldPosition().x == taxiCarSTOP) {
            taxi.translateLocal(new Vector3f(0.0f,0.0f, 0.0f));
        } else {
            taxi.translateLocal(new Vector3f((translationMultiplier/1.0f) * dt,0.0f, 0.0f));
        }

        /** football **/
        //System.out.println("Football: "+football.getWorldPosition().x);
        //float footballSTOP = 0.3744994f;
        float footballSTOP = 0.0853083f;


        /*if (football.getWorldPosition().x == footballSTOP) {
            football.translateLocal(new Vector3f(0.0f,0.0f,0.0f));
        } else {
            football.translateLocal(new Vector3f(0.0f,0.0f,(-translationMultiplier/1.0f) * dt));
            football.rotateLocal(0, 0,(rotationMultiplier/1.0f) * dt);
        }*/




       /* if (football.getWorldPosition().x == footballSTOP) {
            football.translateLocal(new Vector3f(0.0f,0.0f,0.0f));
        } else {
            football.translateLocal(new Vector3f((-translationMultiplier/1.0f) * dt,0.0f,0.0f));
            //football.rotateLocal((rotationMultiplier/1.0f) * dt, 0,0);
            football.rotateAroundPoint(0.0f,(rotationMultiplier/1.0f) * dt,0, football.getWorldPosition());
        }+7







        // TODO: CAMERA CHANGE
        changeCamera();

        // TODO: COLLISION DETECTION
        Vector3f collisionObject = new Vector3f(0, 0, 16.589422f);

        /*System.out.println(bicycle.setFrontBoundingbox(-2.0f));

        if (bicycle.setFrontBoundingbox(-2.0f) < pinkCar.setFrontBoundingbox(2.0f)){
            System.out.println("ich kollidiere");
            bicycle.translateLocal(new Vector3f(0.0f,0.0f,0.0f));
        }*/


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

        public void cyclingMovement() {
            // hin und her wackeln vom fahrradfahrer während er fährt
        }


        public void characterMovement() {

        boolean keyA = true;
        boolean keyD = true;

        Vector3f leftBorder = new Vector3f(-3, bicycle.getWorldPosition().y, bicycle.getWorldPosition().z);
        Vector3f rightBorder = new Vector3f(3, bicycle.getWorldPosition().y, bicycle.getWorldPosition().z);


        // TODO: BORDER CHECK
        // LEFT BORDER:
        if (bicycle.getWorldPosition().x == leftBorder.x) {
            // if bicycle is on left border
            keyA = false;
            //bicycle.rotateLocal(0,-45,0);
        }
        // RIGHT BORDER:
        if (bicycle.getWorldPosition().x == rightBorder.x) {
            // if bicycle is on right border
            keyD = false;
            //bicycle.rotateLocal(0,45,0);
        }

        // TODO: CHARACTER MOVEMENT
        // CHARACTER MOVES LEFT AND RIGHT
        if (window.getKeyState(GLFW_KEY_A) && keyA == true) {
            bicycle.translateGlobal(new Vector3f(-3f, 0.0f,0.0f));
            //bicycle.rotateLocal(0, 45.0f, 0);
        }
        if (window.getKeyState(GLFW_KEY_D) && keyD == true) {
            bicycle.translateGlobal(new Vector3f(3f, 0.0f,0.0f));
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
            //cam.translateLocal(new Vector3f(0.0f,2.3f,-0.2f));
             cam.translateLocal(new Vector3f(0,1.8f,-0.1f)); // z ist die entfernung zum object --> test von mir
             cam.rotateLocal(-15, 0, 0);
        } else if (window.getKeyState(GLFW_KEY_X)) {
            // TODO: BirdeyeView
            cam = new TronCam();
            cam.setParent(bicycle);
            cam.translateLocal(new Vector3f(0.0f, 5.0f, -2.0f));
            //cam.translateLocal(new Vector3f(2.0f,5.0f,10.0f));
            cam.rotateLocal(-90, 0, 0);
            cam.adjustProjection();

            //bigWallOBJ.scaleLocal(new Vector3f(0.1f));
        } else if (window.getKeyState(GLFW_KEY_Q)) {
            // NUR OPTIONAL
            // TODO: LOOK LEFT
            cam = new TronCam();
            cam.setParent(bicycle);
            cam.translateLocal(new Vector3f(-1.0f,2.0f,0.2f));
            cam.rotateLocal(0, (float)Math.toRadians(10.0f), 0);
        } else if (window.getKeyState(GLFW_KEY_E)) {
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
    public void cleanup() {
    }
}
