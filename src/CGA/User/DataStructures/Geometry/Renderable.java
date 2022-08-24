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

    /*public Renderable(ArrayList<Mesh> meshes)
    {
        super();
        this.meshes = new ArrayList<>();
        this.meshes.addAll(meshes);
    }*/

    Vector3f near = getWorldPosition();
    Vector3f far = getWorldPosition();

    public void setNearBoundingbox(float change) {
        near.z += change;
    }
    public void setFarBoundingbox(float change) {
        far.z -= change;
    }
    public Vector3f getNearPosition() {
        Matrix4f world = getWorldModelMatrix();
        Vector3f position = new Vector3f(world.m30(), world.m31(), world.m32()+near.z);
        return position;
    }
    public Vector3f getFarPosition() {
        Matrix4f world = getWorldModelMatrix();
        Vector3f position = new Vector3f(world.m30(), world.m31(), world.m32()-far.z);
        return position;
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
