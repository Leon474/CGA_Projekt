package CGA.User.DataStructures;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.*;
import org.lwjgl.stb.STBImage;
import CGA.Framework.*;

import static CGA.Framework.GLError.*;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;
import static org.lwjgl.opengles.EXTSparseTexture.GL_TEXTURE_CUBE_MAP;
import static org.lwjgl.opengles.GLES30.GL_TEXTURE_WRAP_R;
import static org.lwjgl.stb.STBImage.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;

/**
 * Created by Fabian on 16.09.2017.
 */
public class Texture2D implements ITexture {
    private int texID;
    private int n;

    public Texture2D(String path, boolean genMipMaps) {
        IntBuffer x = BufferUtils.createIntBuffer(1);
        IntBuffer y = BufferUtils.createIntBuffer(1);
        IntBuffer readChannels = BufferUtils.createIntBuffer(1);
        //flip y coordinate to make OpenGL happy
        stbi_set_flip_vertically_on_load(true);
        ByteBuffer imageData = stbi_load(path, x, y, readChannels, 4);

        try {
            if (imageData == null)
                throw new Exception("Image file \"" + path + "\" couldn't be read:\n" + stbi_failure_reason());
            processTexture(imageData, x.get(), y.get(), genMipMaps);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (imageData != null)
                stbi_image_free(imageData);
        }
    }

    public Texture2D(ByteBuffer data, int width, int height, boolean genMipMaps) {
        try {
            processTexture(data, width, height, genMipMaps);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void processTexture(ByteBuffer imageData, int width, int height, boolean genMipMaps) throws Exception {
        //TODO: Place your code here

        texID=glGenTextures();
        glBindTexture(GL_TEXTURE_2D,texID);
        glTexImage2D(GL_TEXTURE_2D,0,GL_RGBA8,width,height,0,GL_RGBA,GL_UNSIGNED_BYTE,imageData);
        if (genMipMaps==true){
            glGenerateMipmap(GL_TEXTURE_2D);
        }
        unbind();
    }

    //neu für skybox
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

    // texture parameter for the skybox
    public void setTexParamsSkybox() throws Exception {
        glBindTexture(GL_TEXTURE_CUBE_MAP, texID);

        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MIN_FILTER, GL_LINEAR);

        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_R, GL_CLAMP_TO_EDGE);

        unbind();
    }



    public void setTexParams(int wrapS, int wrapT, int minFilter, int magFilter) throws Exception {

        //TODO: Place your code here
        glBindTexture(GL_TEXTURE_2D, texID);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, wrapS);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, wrapT);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, magFilter);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, minFilter);
        unbind();
    }

    public void bind(int textureUnit) {
        //TODO: Place your code here
        glActiveTexture(GL_TEXTURE0+textureUnit);
        glBindTexture(GL_TEXTURE_2D, texID);
    }

    public void unbind() {
        //TODO: Place your code here
        glBindTexture(GL_TEXTURE_2D,0);
    }


    public int getTexID() {
        //TODO: Place your code here
        return texID;
    }

    public void cleanup() {
        unbind();
        if (texID != 0) {
            glDeleteTextures(texID);
            texID = 0;
        }
    }
}