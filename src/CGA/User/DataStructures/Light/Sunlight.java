package CGA.User.DataStructures.Light;

import CGA.User.DataStructures.ShaderProgram;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Sunlight implements ISunlight{

    // diffuse lighting
    // directional light

    private float WinkelInnen;
    private float WinkelAussen;

    public Sunlight(Vector3f position, Vector3f lightColor, float Kc, float Kl, float Kq, float WinkelInnen, float WinkelAussen) {
        //super(position, lightColor, Kc, Kl, Kq);
        this.WinkelInnen = (float) Math.toRadians(WinkelInnen);
        this.WinkelAussen = (float) Math.toRadians(WinkelAussen);
    }

    @Override
    public void bind(ShaderProgram shaderProgram, String name, Matrix4f viewMatrix) {
        //super.bind(shaderProgram, name);
        //shaderProgram.setUniform(name +"Richtung", getWorldZAxis().negate().mul(new Matrix3f(viewMatrix)));
        shaderProgram.setUniform(name +"WinkelAussen", WinkelAussen);
        shaderProgram.setUniform(name +"WinkelInnen", WinkelInnen);
    }

}

