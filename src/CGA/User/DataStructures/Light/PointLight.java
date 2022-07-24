package CGA.User.DataStructures.Light;

import CGA.User.DataStructures.Geometry.Transformable;
import CGA.User.DataStructures.ShaderProgram;
import org.joml.Vector3f;


public class PointLight extends Transformable implements IPointLight {

    private Vector3f lightColor;
    private float Kc;
    private float Kl;
    private float Kq;

    public PointLight(Vector3f position, Vector3f lightColor, float Kc, float Kl, float Kq){

        this.lightColor = lightColor;
        translateGlobal(position);
        this.Kc = Kc;
        this.Kl = Kl;
        this.Kq = Kq;

    }
    @Override
    public void bind(ShaderProgram shaderProgram, String name) {
        shaderProgram.setUniform(name +"Position", getWorldPosition());
        shaderProgram.setUniform(name +"Lightcolor", lightColor);
        shaderProgram.setUniform(name +"Kc", Kc);
        shaderProgram.setUniform(name +"Kt", Kl);
        shaderProgram.setUniform(name +"Kq", Kq);
    }

    @Override
    public void setLightColor(Vector3f lightColor) {
        this.lightColor= lightColor;
    }
}
