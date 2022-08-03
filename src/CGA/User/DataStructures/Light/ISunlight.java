package CGA.User.DataStructures.Light;

import CGA.User.DataStructures.ShaderProgram;
import org.joml.Matrix4f;

public interface ISunlight {

    void bind(ShaderProgram shaderProgram, String name, Matrix4f viewMatrix);

}
