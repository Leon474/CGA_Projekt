package CGA.User.DataStructures.Light;

import CGA.User.DataStructures.Geometry.Transformable;
import CGA.User.DataStructures.ShaderProgram;
import org.joml.Vector3f;

// QUELLE: --> https://www.youtube.com/watch?v=9rV8lsaXF9c

public class DirectionalLight extends Transformable {

    private Vector3f lightColor;

    public DirectionalLight(Vector3f color) {
        this.lightColor = color;
    }

    public void bind(ShaderProgram shaderProgram, String name) {
        shaderProgram.setUniform(name + "Lightcolor", lightColor);
    }

}
