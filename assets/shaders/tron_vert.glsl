#version 330 core

layout(location = 0) in vec3 position;
layout(location = 1) in vec2 texcoords;
layout(location = 2) in vec3 normale;

//uniforms
uniform mat4 model_matrix;
uniform mat4 viewMat;
uniform mat4 projMat;
uniform vec2 tcMultiplier;
//uniform vec3 EinsLightposition;
uniform vec3 EinsPosition;
uniform vec3 ZweiPosition;
//uniform vec3 DreiPosition;

out struct VertexData
{
    vec3 position;
    vec2 tc;
    vec3 normale;

    vec3 Normal;
    vec3 LightDir;
    vec3 ViewDir;

    vec3 toLight;
    vec3 test;
} vertexData;
/*out vec4 fColor;
uniform mat4 uViewMat;
uniform mat4 uProjMat;*/

void main(){

    mat4 modelView = viewMat * model_matrix;
    vec4 pos = projMat * modelView * vec4(position, 1.0f);
    vec4 norm = inverse(transpose(modelView)) * vec4(normale, 1.0f);

    vec4 n = vec4(normale, 0.0f);
    mat4 normalMat = transpose(inverse(modelView));
    vertexData.Normal = (normalMat * n).xyz;


    vec4 lp = viewMat * vec4(EinsPosition, 1.0f);
    vec4 v = vec4(position, 1.0f);
    vec4 p = (modelView * v);
    vertexData.LightDir = (lp - p).xyz;
    vertexData.ViewDir = -p.xyz;

    vec4 lp2 = viewMat * vec4(ZweiPosition, 1.0f);
    vec4 v2 = vec4(position, 1.0f);
    vec4 p2 = (modelView * v2);
    vertexData.toLight = -(lp2 - p2).xyz;


    vertexData.tc = texcoords * tcMultiplier;
    gl_Position = pos;
    vertexData.position = pos.xyz;
    vertexData.normale = norm.xyz;

    vertexData.test = -lp2.xyz;

    //fColor = uProjMat * uViewMat * vec4(position, 1.0);
}