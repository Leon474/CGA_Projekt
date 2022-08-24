package CGA.User.DataStructures.Camera;

import CGA.User.DataStructures.ShaderProgram;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_C;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_X;

public class TronCam extends Camera {
    private float fieldOfView;
    private float seitenverhaeltnis;
    private float nearPlane;
    private float farPlane;

    // TODO: FLY TROUGH CAMERA

    public TronCam(float fieldOfView, float seitenverhaeltnis, float nearPlane, float farPlane) {
        super();
        this.fieldOfView = fieldOfView;
        this.seitenverhaeltnis = seitenverhaeltnis;
        this.nearPlane = nearPlane;
        this.farPlane = farPlane;
    }

    public TronCam() {
        super();
        this.fieldOfView = 90.0f;
        this.seitenverhaeltnis = 16.0f / 9;
        //this.nearPlane = 0.01f;  // original
        this.nearPlane = 0.01f;
        this.farPlane = 100.0f; // original
        //this.farPlane = 4000.0f;
    }


    public void adjustProjection(){
        // QUELLE: https://www.youtube.com/watch?v=r857cbEtEY8
        projMat.identity();
        projMat.ortho(0.0f, 32.0f * 40.0f, 0.0f, 32.0f * 21.0f, 0.0f, 100.0f);
    }

    public Matrix4f getViewMatrixx() {
        // defines how the camera is looking
        Vector3f cameraFront = new Vector3f(0.0f, 0.0f, -1.0f);
        Vector3f cameraUp = new Vector3f(0.0f, 1.0f, 0.0f);
        this.viewMat.identity();
        viewMat.lookAt(new Vector3f(getPosition().x, getPosition().y, 20.0f), cameraFront.add(getPosition().x, getPosition().y, 0.0f), cameraUp);
        return this.viewMat;
    }

    public Matrix4f getProjectionMatrixx() {
        return this.projMat;
    }


        @Override
    public Matrix4f calculateViewMatrix() {
        viewMat.identity();
        viewMat = new Matrix4f().lookAt(getWorldPosition(), getWorldPosition().sub(getWorldZAxis()), getWorldYAxis());
        return viewMat;
    }

    @Override
    public Matrix4f calculateProjectionMatrix() {
        projMat = new Matrix4f().perspective(fieldOfView, seitenverhaeltnis, nearPlane, farPlane);
        return projMat;
    }

    @Override
    public void bind(ShaderProgram shaderProgram) {
        shaderProgram.setUniform("viewMat", calculateViewMatrix(), false);
        shaderProgram.setUniform("projMat", calculateProjectionMatrix(), false);
        shaderProgram.setUniform("uViewMat", getViewMatrixx(), false);
        shaderProgram.setUniform("uProjMat", getProjectionMatrixx(), false);
    }

    @Override
    public void bind(ShaderProgram shaderProgram, String name) {
        shaderProgram.setUniform(name + "cameraPosition", getWorldPosition());
    }
}



