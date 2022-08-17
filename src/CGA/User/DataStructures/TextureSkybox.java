package CGA.User.DataStructures;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL13;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;
import static org.lwjgl.opengles.EXTSparseTexture.GL_TEXTURE_CUBE_MAP;
import static org.lwjgl.opengles.GLES30.GL_TEXTURE_WRAP_R;
import static org.lwjgl.stb.STBImage.*;
import static org.lwjgl.stb.STBImage.stbi_image_free;

public class TextureSkybox {

    private int texID;

    /*public TextureSkybox(String facesCubemap[]) {


        texID = glGenTextures();                        // von mir
        glBindTexture(GL_TEXTURE_CUBE_MAP, texID);      // von mir


        for (int i = 0; i < facesCubemap.length; i++) {
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
    }*/

    public int loadCubemap(String facesCubemap[]) {

        texID = glGenTextures();                        // von mir
        glBindTexture(GL_TEXTURE_CUBE_MAP, texID);      // von mir

        for (int i = 0; i < facesCubemap.length; i++) {
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




    /*public void skyBoxTexture(String facesCubemap[]){
        for (int i = 0; i < 6; i++) {
            IntBuffer width = BufferUtils.createIntBuffer(1);
            IntBuffer height = BufferUtils.createIntBuffer(1);
            IntBuffer nrChannels = BufferUtils.createIntBuffer(1);

            ByteBuffer imageData = stbi_load(facesCubemap[i], width, height, nrChannels, 0);

            if (imageData != null) {
                stbi_set_flip_vertically_on_load(false);
                glTexImage2D(GL_TEXTURE_CUBE_MAP_POSITIVE_X + i, 0, GL_RGB, width.get(), height.get(), 0, GL_RGB, GL_UNSIGNED_BYTE, imageData);
                stbi_image_free(imageData);
            } else {
                System.out.println("Failed to load Texture: "+ facesCubemap[i]);
                stbi_image_free(imageData);
            }
        }
    }*/

    public void setTexParamsSkybox() throws Exception {
        glBindTexture(GL_TEXTURE_CUBE_MAP, texID);

        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MIN_FILTER, GL_LINEAR);

        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_R, GL_CLAMP_TO_EDGE);

        unbind();
    }

    /*public void bind(int textureUnit) {
        //TODO: Place your code here
        glActiveTexture(GL_TEXTURE0 + textureUnit);
        glBindTexture(GL_TEXTURE_2D, texID);
    }*/

    public void unbind() {
        //TODO: Place your code here
        //glBindTexture(GL_TEXTURE_2D,0);
        glBindTexture(GL_TEXTURE_CUBE_MAP,0);

    }

    public void cleanup() {
        unbind();
        if (texID != 0) {
            glDeleteTextures(texID);
            texID = 0;
        }
    }
}
