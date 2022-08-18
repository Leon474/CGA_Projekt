package CGA.User.DataStructures.Skybox;
import CGA.Framework.OBJLoader;
import CGA.User.DataStructures.Geometry.*;
import CGA.User.DataStructures.ShaderProgram;
import CGA.User.DataStructures.Texture2D;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.AMDSeamlessCubemapPerTexture;
import org.lwjgl.openvr.Texture;
import org.lwjgl.stb.STBImage;

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

    public int skyboxVAO = glGenVertexArrays();
    int skyboxVBO = glGenBuffers();
    int skyboxIBO = glGenBuffers();
    int texID = glGenTextures();

    float[] vertexdata;
    int[] indexdata;

   public SkyBox(float[] vertexdata, int[] indexdata) {
        this.vertexdata = vertexdata;
        this.indexdata = indexdata;
    }

    public int loadCubemap(String facesCubemap[]) {

        // Binding VAO, VBO and IBO
        glBindVertexArray(skyboxVAO);
        glBindBuffer(GL_ARRAY_BUFFER, skyboxVBO);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, skyboxIBO);

        glBufferData(GL_ARRAY_BUFFER, vertexdata, GL_STATIC_DRAW);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indexdata, GL_STATIC_DRAW);

        glEnableVertexAttribArray(0);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
        glBindTexture(GL_TEXTURE_CUBE_MAP, texID);

        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_R, GL_CLAMP_TO_EDGE);
        glEnable(AMDSeamlessCubemapPerTexture.GL_TEXTURE_CUBE_MAP_SEAMLESS);

        // Load a Face for each side of the cubemap (6 sides) to create the textured skybox
        for (int i = 0; i < facesCubemap.length; i++) {
            //System.out.println("load map aufruf"+i);
            IntBuffer width = BufferUtils.createIntBuffer(1);
            IntBuffer height = BufferUtils.createIntBuffer(1);
            IntBuffer nrChannels = BufferUtils.createIntBuffer(1);

            ByteBuffer imageData = STBImage.stbi_load(facesCubemap[i], width, height, nrChannels, 4);

            if (imageData != null) {

                STBImage.stbi_set_flip_vertically_on_load(false); // vllt auch true
                glTexImage2D(GL_TEXTURE_CUBE_MAP_POSITIVE_X + i, 0, GL_RGBA, width.get(), height.get(), 0, GL_RGBA, GL_UNSIGNED_BYTE, imageData);
                STBImage.stbi_image_free(imageData);

                //unbind(); // von mir hinzugefügt
            } else {
                System.out.println("Failed to load Texture: "+ facesCubemap[i]);
                stbi_image_free(imageData);
            }
        }

        return texID;
    }


}

















