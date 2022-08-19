package CGA.User.DataStructures.Geometry;

import CGA.User.DataStructures.ShaderProgram;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.ArrayList;

/**
 * Created by Fabian on 19.09.2017.
 */

/**
 * Renders Mesh objects
 */
public class Renderable extends Transformable implements IRenderable {
    /**
     * List of meshes attached to this renderable object
     */
    public ArrayList<Mesh> meshes;
    public Renderable renderable;


    /**
     * creates an empty renderable object with an empty mesh list
     */
    public Renderable() {
        super();
        meshes = new ArrayList<>();
    }

    public Renderable(ArrayList<Mesh> meshes) {
        this.meshes = meshes;
    }

//    public Renderable(ArrayList<Mesh> meshes)
//    {
//        super();
//        this.meshes = new ArrayList<>();
//        this.meshes.addAll(meshes);
//    }

    float front;
    float leftSide;
    float rightSide;
    float back;
    /*public void setBoundingbox(float front, float leftSide, float rightSide, float back) {
        front = getWorldPosition().z += new Vector3f(0,0, front);
        leftSide = getWorldPosition().x += leftSide;
        rightSide = getWorldPosition().x -= rightSide;
        back = getWorldPosition().z += back;
    }*/

    public float setNearBoundingbox(float front) {
        front = getWorldPosition().z += front;
        return front;
    }

    public Vector3f getNearBoundingPosition() {
        Matrix4f world = getWorldModelMatrix();
        Vector3f position = new Vector3f(world.m30()+setNearBoundingbox(front), world.m31(), world.m32());
        return position;
    }

    /*public void setBoundingbox(float front, float leftSide, float rightSide, float back) {
        front = getWorldPosition().z += front;
        leftSide = getWorldPosition().x += leftSide;
        rightSide = getWorldPosition().x -= rightSide;
        back = getWorldPosition().z -= back;
    }*/
    /*public float setNearBoundingbox(float front) {
        front = getWorldPosition().z += front;
        return front;
    }*/


    public float setFarBoundingbox(float back) {
        back = getWorldPosition().z -= back;
        return back;
    }

    public float getLeftSideBoundingbox() {
        return leftSide;
    }

    public float getRightSideBoundingbox() {
        return rightSide;
    }

    public float getBackBoundingbox() {
        return back;
    }



    /**
     * Renders all meshes attached to this Renderable, applying the transformation matrix to
     * each of them
     */
    public void render() {
        for(Mesh m : meshes) {
            m.render();
        }
    }

    @Override
    public void render(ShaderProgram shaderProgram) {
        shaderProgram.setUniform("model_matrix", getWorldModelMatrix(),false);
        for (Mesh m:meshes){
            m.render(shaderProgram);
        }
    }

}
