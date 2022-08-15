package CGA.User.DataStructures.Skybox;
import CGA.Framework.OBJLoader;
import CGA.User.DataStructures.Geometry.*;
import CGA.User.DataStructures.ShaderProgram;
import CGA.User.DataStructures.Texture2D;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.lwjgl.openvr.Texture;

import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengles.EXTSparseTexture.GL_TEXTURE_CUBE_MAP;
import static org.lwjgl.opengles.GLES30.GL_TEXTURE_WRAP_R;


public class SkyBox extends Transformable {

    Matrix4f projectionMatrix;
    Matrix3f modelViewMatrix;
    //texture_sampler texture_sampler;
    //private data
    int skyboxVAO = 1;
    int skyboxVBO = 1;
    int skyboxEBO = 1;
    private int count;
    private Texture2D cubemapTexture;


    public SkyBox(float[] vertexdata, int[] indexdata, VertexAttribute[] attributes) throws Exception {

        count = indexdata.length;

        // binding
        skyboxVAO = glGenVertexArrays();
        glBindVertexArray(skyboxVAO);

        skyboxVBO = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, skyboxVBO);
        glBufferData(GL_ARRAY_BUFFER, vertexdata, GL_STATIC_DRAW);

        skyboxEBO = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, skyboxEBO);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indexdata, GL_STATIC_DRAW);

        glVertexAttribPointer(0, 3, GL_FLOAT, false, 8 * 3, 0);


        // TODO: Generate IDs, bind objects and upload Mesh data

        /*for (int i = 0; i < attributes.length; i++) {
            glVertexAttribPointer(i, attributes[i].n, attributes[i].type, false, attributes[i].stride, attributes[i].offset); //Position
            glEnableVertexAttribArray(i);
        }*/


        // unbinding
        glEnableVertexAttribArray(0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER,0);


        /*int cubemapTexture = 0;
        cubemapTexture = glGenTextures();
        glBindTexture(GL_TEXTURE_CUBE_MAP, cubemapTexture);
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MIN_FILTER, GL_LINEAR);

        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_R, GL_CLAMP_TO_EDGE);*/

    }


    /**
     * renders the mesh
     */
    public void render() {
        //TODO: Call the rendering method every frame
        glBindVertexArray(skyboxVAO);
        glActiveTexture(GL_TEXTURE0);
        //glBindTexture(GL_TEXTURE_CUBE_MAP, cubemapTexture);
        glDrawElements(GL_TRIANGLES, count, GL_UNSIGNED_INT, 0);
        glBindVertexArray(0);
    }

    public void render(ShaderProgram shaderProgram) {
        //material.bind(shaderProgram);
        render();
    }

    /**
     * Deletes the previously allocated OpenGL objects for this mesh
     */
    public void cleanup() {
        if (skyboxEBO != 0)
            glDeleteBuffers(skyboxEBO);
        if (skyboxVBO != 0)
            glDeleteBuffers(skyboxVBO);
        if (skyboxVAO != 0)
            glDeleteVertexArrays(skyboxVAO);
    }


    public void bind(ShaderProgram shaderProgram, String name, Matrix4f viewMatrix) {
        shaderProgram.setUniform(name +"Richtung", getWorldZAxis().negate().mul(new Matrix3f(viewMatrix)));
        //shaderProgram.setUniform();

    }







}
