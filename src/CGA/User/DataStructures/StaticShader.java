package CGA.User.DataStructures;

public class StaticShader extends ShaderProgram {

    /**
     * Creates a shader object from vertex and fragment shader paths
     *
     * @param vertexShaderPath   vertex shader path
     * @param fragmentShaderPath fragment shader path
     * @throws Exception if shader compilation failed, an exception is thrown
     */

    private static final String VERTEX_FILE = "assets/shaders/tron_vert.glsl";
    private static final String FRAMENT_FILE = "assets/shaders/tron_frag.glsl";

    public StaticShader() throws Exception {
        super(VERTEX_FILE, FRAMENT_FILE);
    }



}
