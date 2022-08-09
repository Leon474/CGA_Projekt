package CGA.User.DataStructures.Geometry;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Transformable implements ITransformable {
    private Matrix4f modelMatrix;
    private Transformable parent;

    public Transformable() {
        modelMatrix = new Matrix4f();
    }

    @Override
    public Matrix4f getLocalModelMatrix() {
        return modelMatrix;
    }

    @Override
    public void rotateLocal(float pitch, float yaw, float roll) {
        modelMatrix.rotateX((float)Math.toRadians(pitch));
        modelMatrix.rotateY((float)Math.toRadians(yaw));
        modelMatrix.rotateZ((float)Math.toRadians(roll));
        // ALTERNATIVE modelMatrix.rotateXYZ(new Vector3f((float)Math.toRadians(pitch), (float)Math.toRadians(yaw), (float)Math.toRadians(roll)));
    }

    @Override
    public void rotateAroundPoint(float pitch, float yaw, float roll, Vector3f altMidpoint) {
        Matrix4f temp = new Matrix4f();
        temp.translate(altMidpoint);
        temp.rotateXYZ(new Vector3f((float)Math.toRadians(pitch), (float)Math.toRadians(yaw), (float)Math.toRadians(roll)));
        temp.translate(new Vector3f(altMidpoint).negate());
        temp.mul(modelMatrix, modelMatrix);
    }

    @Override
    public void translateLocal(Vector3f deltaPos) {
        modelMatrix.translate(deltaPos);
    }

    @Override
    public void scaleLocal(Vector3f scale) {
        modelMatrix.scale(scale);
    }

    @Override
    public Vector3f getPosition() {
        Vector3f position = new Vector3f(modelMatrix.m30(), modelMatrix.m31(), modelMatrix.m32());
        return position;
    }

    @Override
    public Vector3f getXAxis() {
        Vector3f xAxis = new Vector3f(modelMatrix.m00(), modelMatrix.m01(), modelMatrix.m02());
        xAxis.normalize();
        return xAxis;
    }

    @Override
    public Vector3f getYAxis() {
        Vector3f yAxis = new Vector3f(modelMatrix.m10(), modelMatrix.m11(), modelMatrix.m12());
        yAxis.normalize();
        return yAxis;
    }

    @Override
    public Vector3f getZAxis() {
        Vector3f zAxis = new Vector3f(modelMatrix.m20(), modelMatrix.m21(), modelMatrix.m22());
        zAxis.normalize();
        return zAxis;
    }

    @Override
    public void setParent(Transformable parent) {
        this.parent = parent;
    }

    @Override
    public Matrix4f getWorldModelMatrix() {
        Matrix4f world = new Matrix4f(modelMatrix);
        if(parent != null){
            parent.getWorldModelMatrix().mul(modelMatrix, world);
        }
        return world;
    }

    @Override
    public Vector3f getWorldPosition() {
        Matrix4f world = getWorldModelMatrix();
        Vector3f position = new Vector3f(world.m30(), world.m31(), world.m32());
        return position;
    }

    @Override
    public Vector3f getWorldXAxis() {
        Matrix4f world = getWorldModelMatrix();
        Vector3f xAxis = new Vector3f(world.m00(), world.m01(), world.m02());
        return xAxis.normalize();
    }

    @Override
    public Vector3f getWorldYAxis() {
        Matrix4f world = getWorldModelMatrix();
        Vector3f yAxis = new Vector3f(world.m10(), world.m11(), world.m12());
        return yAxis.normalize();
    }

    @Override
    public Vector3f getWorldZAxis() {
        Matrix4f world = getWorldModelMatrix();
        Vector3f zAxis = new Vector3f(world.m20(), world.m21(), world.m22());
        return zAxis.normalize();
    }

    @Override
    public void translateGlobal(Vector3f deltaPos) {
        Matrix4f temp = new Matrix4f();
        temp.translate(deltaPos);
        temp.mul(modelMatrix, modelMatrix);
    }
}
