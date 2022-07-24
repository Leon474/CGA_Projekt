package CGA.User.DataStructures.Camera;

import CGA.User.DataStructures.ShaderProgram;
import org.joml.Matrix4f;

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
        this.nearPlane = 0.01f;
        this.farPlane = 100.0f;
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
    }
}
