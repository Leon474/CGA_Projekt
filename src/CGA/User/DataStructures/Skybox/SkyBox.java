package CGA.User.DataStructures.Skybox;
import CGA.Framework.OBJLoader;
import CGA.User.DataStructures.Geometry.Material;
import CGA.User.DataStructures.Geometry.Mesh;
import CGA.User.DataStructures.Geometry.Renderable;
import CGA.User.DataStructures.Geometry.Transformable;
import CGA.User.DataStructures.ShaderProgram;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.lwjgl.openvr.Texture;

import java.util.ArrayList;


public class SkyBox extends Transformable {

    Matrix4f projectionMatrix;
    Matrix3f modelViewMatrix;
    //texture_sampler texture_sampler;


    /*public SkyBox(Matrix4f projectionMatrix, Matrix3f modelViewMatrix, texture_sampler texture_sampler) {
        this.projectionMatrix = projectionMatrix;
        this.modelViewMatrix = modelViewMatrix;
        this.texture_sampler = texture_sampler;
    }*/


    public void bind(ShaderProgram shaderProgram, String name, Matrix4f viewMatrix) {
        shaderProgram.setUniform(name +"Richtung", getWorldZAxis().negate().mul(new Matrix3f(viewMatrix)));

        //shaderProgram.setUniform(name+"modelViewMatrix", modelViewMatrix);
        //shaderProgram.setUniform("ambientLight", scene.getSceneLight().getAmbientLight());

       /* shaderProgram.setUniform(name+"projectionMatrix", );
        shaderProgram.setUniform(name+"modelViewMatrix", );
        shaderProgram.setUniform(name+"texture_sampler", );*/
        //shaderProgram.setUniform("ambientLight");
    }


        /*
        public SkyBox(String objModel, String textureFile) throws Exception {
            super();
            Mesh skyBoxMesh = OBJLoader.loadOBJ(objModel);
            Texture skyBoxtexture = new Texture(textureFile);
            skyBoxMesh.setMaterial(new Material(skyBoxtexture, 0.0f));
            setMesh(skyBoxMesh);
            setPosition(0, 0, 0);
        }*/


        /*public SkyBox(String objModel, String textureFile) throws Exception {
            super();
            Mesh skyBoxMesh = OBJLoader.loadMesh(objModel);
            Texture skyBoxtexture = new Texture(textureFile);
            skyBoxMesh.setMaterial(new Material(skyBoxtexture, 0.0f));
            setMesh(skyBoxMesh);
            setPosition(0, 0, 0);
        }
    }*/





}
