package CGA.User.DataStructures.Skybox;
import CGA.Framework.OBJLoader;
import CGA.User.DataStructures.Geometry.*;
import CGA.User.DataStructures.ShaderProgram;
import CGA.User.DataStructures.Texture2D;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.openvr.Texture;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengles.EXTSparseTexture.GL_TEXTURE_CUBE_MAP;
import static org.lwjgl.opengles.GLES30.GL_TEXTURE_WRAP_R;
import static org.lwjgl.stb.STBImage.*;
import static org.lwjgl.stb.STBImage.stbi_image_free;


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
    private int texID;


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

        //glVertexAttribPointer(0, 3, GL_FLOAT, false, 8 * 3, 0);

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


    public int loadCubemap(String facesCubemap[]) {

        texID = glGenTextures();                        // von mir
        glBindTexture(GL_TEXTURE_CUBE_MAP, texID);      // von mir  (bei texid kommt cubemapTexture????

        for (int i = 0; i < facesCubemap.length; i++) {
            System.out.println("load map aufruf"+i);
            IntBuffer width = BufferUtils.createIntBuffer(1);
            IntBuffer height = BufferUtils.createIntBuffer(1);
            IntBuffer nrChannels = BufferUtils.createIntBuffer(1);

            ByteBuffer imageData = stbi_load(facesCubemap[i], width, height, nrChannels, 0);

            if (imageData != null) {

                stbi_set_flip_vertically_on_load(true); // vllt auch true
                glTexImage2D(GL_TEXTURE_CUBE_MAP_POSITIVE_X + i, 0, GL_RGB, width.get(), height.get(), 0, GL_RGB, GL_UNSIGNED_BYTE, imageData);
                stbi_image_free(imageData);

                //unbind(); // von mir hinzugefügt
            } else {
                System.out.println("Failed to load Texture: "+ facesCubemap[i]);
                stbi_image_free(imageData);
            }
        }

        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MIN_FILTER, GL_LINEAR);

        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_R, GL_CLAMP_TO_EDGE);

        return texID;
    }


    /**
     * renders the mesh
     */
    public void render(ShaderProgram shaderProgram, int cubemapTexture) {
        //TODO: Call the rendering method every frame
        //glDepthFunc(GL_LEQUAL);
        glBindVertexArray(skyboxVAO);
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_CUBE_MAP, cubemapTexture);
        //glDrawElements(GL_TRIANGLES, count, GL_UNSIGNED_INT, 0);
        glDrawArrays(GL_TRIANGLES, 0, 36); // im anderen tutorial
        glBindVertexArray(0);
        glDepthFunc(GL_LESS); // im anderen tutorial
    }

    /*public void render(ShaderProgram shaderProgram) {
        //material.bind(shaderProgram);
        render();
    }*/

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

    public void bind(ShaderProgram shaderProgram, String name) {
        /*shaderProgram.setUniform(name + "view", getLocalModelMatrix());
        shaderProgram.setUniform(name+ "projection", projectionMatrix);


        view = glm::mat4(glm::mat3(glm::lookAt(camera.Position, camera.Position + camera.Orientation, camera.Up)));
        projection = glm::perspective(glm::radians(45.0f), (float)width / height, 0.1f, 100.0f);
        glUniformMatrix4fv(glGetUniformLocation(skyboxShader.ID, "view"), 1, GL_FALSE, glm::value_ptr(view));
        glUniformMatrix4fv(glGetUniformLocation(skyboxShader.ID, "projection"), 1, GL_FALSE, glm::value_ptr(projection));*/
    }








    }
