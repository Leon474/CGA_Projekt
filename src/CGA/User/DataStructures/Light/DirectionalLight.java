package CGA.User.DataStructures.Light;

import CGA.User.DataStructures.Geometry.Transformable;
import CGA.User.DataStructures.ShaderProgram;
import org.joml.Vector3f;

public class DirectionalLight extends Transformable {

    private Vector3f lightColor;

    public DirectionalLight(Vector3f color) {
        this.lightColor = color;
    }

    public void bind(ShaderProgram shaderProgram, String name) {
        shaderProgram.setUniform(name + "Lightcolor", lightColor);
    }

}
