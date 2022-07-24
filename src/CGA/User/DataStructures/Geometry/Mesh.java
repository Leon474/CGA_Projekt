package CGA.User.DataStructures.Geometry;

import CGA.User.DataStructures.ShaderProgram;
import CGA.User.DataStructures.Texture2D;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.*;

/**
 * Created by Fabian on 16.09.2017.
 */
public class Mesh {

    //private data
    private int vao = 0;
    private int vbo = 0;
    private int ibo = 0;
    private int count;
    private Material material;
    private Texture2D texture;

    /**
     * Creates a Mesh object from vertexdata, intexdata and a given set of vertex attributes
     *
     * @param vertexdata plain float array of vertex data
     * @param indexdata  index data
     * @param attributes vertex attributes contained in vertex data
     * @throws Exception If the creation of the required OpenGL objects fails, an exception is thrown
     */
    public Mesh(float[] vertexdata, int[] indexdata, VertexAttribute[] attributes, Material material) throws Exception {
        this(vertexdata,indexdata,attributes);
        this.material = material;
    }

    private Mesh(float[] vertexdata, int[] indexdata, VertexAttribute[] attributes) throws Exception {

        // TODO: Generate IDs, bind objects and upload Mesh data
        count = indexdata.length;

        // TODO: CREATE VAOs
        vao = glGenVertexArrays();
        glBindVertexArray(vao);

        // TODO: store data in attributeList VBO
        vbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, vertexdata, GL_STATIC_DRAW);

        for (int i = 0; i < attributes.length; i++) {
            glVertexAttribPointer(i, attributes[i].n, attributes[i].type, false, attributes[i].stride, attributes[i].offset); //Position
            glEnableVertexAttribArray(i);
        }
        // TODO: unbind vbos
        glBindBuffer(GL_ARRAY_BUFFER, 0);

        // TODO: IBOS
        ibo = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indexdata, GL_STATIC_DRAW);

        // TODO: UNBIND VAOs
        glBindVertexArray(0);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
    }


    /**
     * renders the mesh
     */
    public void render() {
        //TODO: Call the rendering method every frame
        glBindVertexArray(vao);
        glDrawElements(GL_TRIANGLES, count, GL_UNSIGNED_INT, 0);
        glBindVertexArray(0);
    }

    public void render(ShaderProgram shaderProgram) {
        material.bind(shaderProgram);
        render();
    }

    /**
     * Deletes the previously allocated OpenGL objects for this mesh
     */
    public void cleanup() {
        if (ibo != 0)
            glDeleteBuffers(ibo);
        if (vbo != 0)
            glDeleteBuffers(vbo);
        if (vao != 0)
            glDeleteVertexArrays(vao);
    }
}
