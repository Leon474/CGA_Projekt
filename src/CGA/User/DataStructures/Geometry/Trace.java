package CGA.User.DataStructures.Geometry;

import CGA.User.DataStructures.ShaderProgram;
import CGA.User.DataStructures.Texture2D;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.ArrayList;

import static org.joml.Intersectionf.testPointTriangle;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.*;

/*public class Trace
{
    //data
    private int vao = 0;
    private int vbo = 0;
    private int ibo = 0;

    //max number of floats that fit into vertexdata
    private int maxvbuffersize;
    //max number of indices
    private int maxibuffersize;

    //current number of floats in vertexdata
    private int vbuffersize;
    //current number of indices
    private int ibuffersize;

    //current number of vertices
    private int numVertices;
    //current number of indices
    private int numIndices;

    //current number of segments
    private int stepcount = 0;
    //maximum allowed segments
    private int maxSteps;

    //some geometric parameters
    private float height;
    private float width;
    private float minDist;

    //to keep track of the last submitted point
    private Vector3f lastTracePoint;
    //rate of change of the texture coordinates' u-component w.r.t. distance between two segments
    private float texURate;
    //to keep track of the last assigned u-coordinate
    private float lastTexU;

    //material
    private Material material;

    // for the collision stuff later on we might want to hold a list of the submitted points.

    private ArrayList<Vector3f> traceVertices;

    public Trace(float height, float width, float mindist, int maxSteps, float textureURate) throws Exception
    {

        material = new Material();
        material.diff = new Texture2D("assets/textures/ground_diff.png", true);
        material.diff.setTexParams(GL_REPEAT, GL_REPEAT, GL_LINEAR_MIPMAP_LINEAR, GL_LINEAR);
        material.specular = new Texture2D("assets/textures/ground_spec.png", true);
        material.specular.setTexParams(GL_REPEAT, GL_REPEAT, GL_LINEAR_MIPMAP_LINEAR, GL_LINEAR);
        material.emit = new Texture2D("assets/textures/trace.png", true);
        material.emit.setTexParams(GL_REPEAT, GL_REPEAT, GL_LINEAR_MIPMAP_LINEAR, GL_LINEAR);
        material.shininess = 30.0f;
        material.tcMultiplier = new Vector2f(1.0f, 1.0f);

        vao = glGenVertexArrays();
        if (vao == 0)
        {
            throw new Exception("Vertex array object creation failed.");
        }
        vbo = glGenBuffers();
        if (vbo == 0)
        {
            glDeleteVertexArrays(vao);
            throw new Exception("Vertex buffer creation failed.");
        }
        ibo = glGenBuffers();
        if (ibo == 0)
        {
            glDeleteVertexArrays(vao);
            glDeleteBuffers(vbo);
            throw new Exception("Index buffer object creation failed.");
        }

        stepcount = 0;
        this.maxSteps = maxSteps;
        this.height = height;
        this.width = width;
        this.maxvbuffersize = 9 * 3 * maxSteps;
        this.maxibuffersize = 12 * (maxSteps - 1) + 3;
        //vertexdata = BufferUtils.createFloatBuffer(maxvbuffersize);
        //indices = BufferUtils.createIntBuffer(maxibuffersize);
        vbuffersize = 0;
        ibuffersize = 0;
        this.texURate = textureURate;
        this.lastTexU = 0.0f;
        numVertices = 0;
        numIndices = 0;
        minDist = mindist;


        glBindVertexArray(vao);
        //---------------------- VAO state setup start ----------------------
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, maxvbuffersize * 4, GL_STREAM_DRAW);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, maxibuffersize * 4, GL_STREAM_DRAW);

        glEnableVertexAttribArray(0);
        glVertexAttribPointer(
                0,
                3,
                GL_FLOAT,
                false,
                36,
                0
        );

        glEnableVertexAttribArray(1);
        glVertexAttribPointer(
                1,
                2,
                GL_FLOAT,
                false,
                36,
                12
        );

        glEnableVertexAttribArray(2);
        glVertexAttribPointer(
                2,
                3,
                GL_FLOAT,
                false,
                36,
                20
        );

        glEnableVertexAttribArray(3);
        glVertexAttribPointer(
                3,
                1,
                GL_FLOAT,
                false,
                36,
                32
        );
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        //--------------------- VAO state setup end --------------------
        glBindVertexArray(0);

        traceVertices = new ArrayList<>();
    }

    public void update(Vector3f newPos, Vector3f bikeDirection, float t)
    {
        if (stepcount >= maxSteps)
            return; // maybe we can throw away the oldest segments?

        Vector3f direction = new Vector3f(bikeDirection);
        float[] newVertexData = null;
        int[] newIndexData = null;
        boolean datachanged = false;
        if(stepcount == 0)
        {
            Vector3f traceNormal = new Vector3f(direction).cross(new Vector3f(0.0f, 1.0f, 0.0f)).normalize();

            Vector3f pos1 = new Vector3f(newPos.x, 0.0f, newPos.z).sub(new Vector3f(traceNormal).mul(width / 2.0f));
            Vector3f pos2 = new Vector3f(newPos.x, height, newPos.z);
            Vector3f pos3 = new Vector3f(newPos.x, 0.0f, newPos.z).add(new Vector3f(traceNormal).mul(width / 2.0f));

            Vector3f normal2 = new Vector3f(0.0f, 1.0f, 0.0f);
            Vector3f normal1 = new Vector3f(pos2).sub(new Vector3f(pos1)).cross(new Vector3f(direction)).normalize();
            Vector3f normal3 = new Vector3f(pos2).sub(new Vector3f(pos3)).cross(new Vector3f(direction)).normalize().negate();

            Vector2f uv1 = new Vector2f(0.0f, 0.0f);
            Vector2f uv2 = new Vector2f(0.0f, 0.5f);
            Vector2f uv3 = new Vector2f(0.0f, 1.0f);

            newVertexData = new float[27];
            newIndexData = new int[3];

            int vidx = 0;
            newVertexData[vidx++] = pos1.x; newVertexData[vidx++] = pos1.y; newVertexData[vidx++] = pos1.z;
            newVertexData[vidx++] = uv1.x; newVertexData[vidx++] = uv1.y;
            newVertexData[vidx++] = normal1.x; newVertexData[vidx++] = normal1.y; newVertexData[vidx++] = normal1.z;
            newVertexData[vidx++] = t;

            newVertexData[vidx++] = pos2.x; newVertexData[vidx++] = pos2.y; newVertexData[vidx++] = pos2.z;
            newVertexData[vidx++] = uv2.x; newVertexData[vidx++] = uv2.y;
            newVertexData[vidx++] = normal2.x; newVertexData[vidx++] = normal2.y; newVertexData[vidx++] = normal2.z;
            newVertexData[vidx++] = t;

            newVertexData[vidx++] = pos3.x; newVertexData[vidx++] = pos3.y; newVertexData[vidx++] = pos3.z;
            newVertexData[vidx++] = uv3.x; newVertexData[vidx++] = uv3.y;
            newVertexData[vidx++] = normal3.x; newVertexData[vidx++] = normal3.y; newVertexData[vidx++] = normal3.z;
            newVertexData[vidx++] = t;

            int iidx = 0;
            newIndexData[iidx++] = 0; newIndexData[iidx++] = 2; newIndexData[iidx++] = 1;

            //store last submitted position
            lastTracePoint = newPos;
            //store last assigned u-tex-coord
            lastTexU = 0.0f;
            datachanged = true;

            numVertices = 3;
            numIndices = 3;

            stepcount++;


            //For Collision Detection: Fill traceVertices-List for later Usa
            traceVertices.add(pos1);
            traceVertices.add(pos2);
            traceVertices.add(pos3);
        }
        else
        {
            // viewing direction of the bike is actually meaningless here. We need the direction of the vector from the last point
            // to the new point
            direction = new Vector3f(newPos).sub(lastTracePoint);
            float distance = direction.length();
            // range constraint
            if(distance > minDist)
            {
                Vector3f traceNormal = new Vector3f(direction).cross(new Vector3f(0.0f, 1.0f, 0.0f)).normalize();

                Vector3f pos1 = new Vector3f(newPos.x, 0.0f, newPos.z).sub(new Vector3f(traceNormal).mul(width / 2.0f));
                Vector3f pos2 = new Vector3f(newPos.x, height, newPos.z);
                Vector3f pos3 = new Vector3f(newPos.x, 0.0f, newPos.z).add(new Vector3f(traceNormal).mul(width / 2.0f));

                Vector3f normal2 = new Vector3f(0.0f, 1.0f, 0.0f);
                Vector3f normal1 = new Vector3f(pos2).sub(new Vector3f(pos1)).cross(new Vector3f(direction)).normalize();
                Vector3f normal3 = new Vector3f(pos2).sub(new Vector3f(pos3)).cross(new Vector3f(direction)).normalize().negate();

                // calculate u component of the texture coordinate
                float u = lastTexU + distance * texURate;
                // store last assigned u component
                lastTexU = u;

                // assemble the texture coordinates
                Vector2f uv1 = new Vector2f(u, 0.0f);
                Vector2f uv2 = new Vector2f(u, 0.5f);
                Vector2f uv3 = new Vector2f(u, 1.0f);

                newVertexData = new float[27];
                newIndexData = new int[12];

                int vidx = 0;
                newVertexData[vidx++] = pos1.x; newVertexData[vidx++] = pos1.y; newVertexData[vidx++] = pos1.z;
                newVertexData[vidx++] = uv1.x; newVertexData[vidx++] = uv1.y;
                newVertexData[vidx++] = normal1.x; newVertexData[vidx++] = normal1.y; newVertexData[vidx++] = normal1.z;
                newVertexData[vidx++] = t;

                newVertexData[vidx++] = pos2.x; newVertexData[vidx++] = pos2.y; newVertexData[vidx++] = pos2.z;
                newVertexData[vidx++] = uv2.x; newVertexData[vidx++] = uv2.y;
                newVertexData[vidx++] = normal2.x; newVertexData[vidx++] = normal2.y; newVertexData[vidx++] = normal2.z;
                newVertexData[vidx++] = t;

                newVertexData[vidx++] = pos3.x; newVertexData[vidx++] = pos3.y; newVertexData[vidx++] = pos3.z;
                newVertexData[vidx++] = uv3.x; newVertexData[vidx++] = uv3.y;
                newVertexData[vidx++] = normal3.x; newVertexData[vidx++] = normal3.y; newVertexData[vidx++] = normal3.z;
                newVertexData[vidx++] = t;

                // increase the vertex count
                numVertices += 3;

                int iidx = 0;
                newIndexData[iidx++] = numVertices - 6; newIndexData[iidx++] = numVertices - 5; newIndexData[iidx++] = numVertices - 3;
                newIndexData[iidx++] = numVertices - 5; newIndexData[iidx++] = numVertices - 2; newIndexData[iidx++] = numVertices - 3;
                newIndexData[iidx++] = numVertices - 4; newIndexData[iidx++] = numVertices - 2; newIndexData[iidx++] = numVertices - 5;
                newIndexData[iidx++] = numVertices - 4; newIndexData[iidx++] = numVertices - 1; newIndexData[iidx++] = numVertices - 2;

                // increase index count accordinglyww
                numIndices += 12;

                datachanged = true;
                stepcount++;
                //store position of last submitted point
                lastTracePoint = newPos;

                //For Collision Detection: Fill traceVertices-List for later Usa
                traceVertices.add(pos1);
                traceVertices.add(pos2);
                traceVertices.add(pos3);

            }
        }

        // update the gpu buffers only if something has changed
        if(datachanged)
        {
            glBindBuffer(GL_ARRAY_BUFFER, vbo);
            // upload new data
            glBufferSubData(GL_ARRAY_BUFFER, vbuffersize * 4, newVertexData);
            vbuffersize += newVertexData.length;
            glBindBuffer(GL_ARRAY_BUFFER, 0);

            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
            // upload new data
            glBufferSubData(GL_ELEMENT_ARRAY_BUFFER, ibuffersize * 4, newIndexData);
            ibuffersize += newIndexData.length;
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);


        }
    }

    private void render()
    {
        if(numVertices >= 4)
        {
            glBindVertexArray(vao);
            glDrawElements(GL_TRIANGLES, numIndices, GL_UNSIGNED_INT, 0);
            glBindVertexArray(0);
        }
    }

    public void render(ShaderProgram shaderProgram)
    {
        if(numVertices >= 4)
        {

            material.bind(shaderProgram);
            // make sure the model matrix in the vertex shader is identity. All the vertex positions are already in world space.
            shaderProgram.setUniform("model_matrix", new Matrix4f(), false);
            render();

        }
    }



    public boolean checkCollision(Vector3f pos, Vector3f dir){

        //TODO: Place your code here!

    }

    public void cleanup()
    {
        if (vbo != 0)
            glDeleteBuffers(vbo);
        if (vao != 0)
            glDeleteVertexArrays(vao);
    }
}
*/
