#version 330 core

layout(location = 0) in vec3 position;
layout(location = 1) in vec3 color;
layout(location = 2) in vec2 texrureCoordinatesIn;

uniform mat4 m4Boden;
out vec3 col;
out vec2 textureCoordinates;

void main(){

    gl_Position = vec4(position, 1.0f);
    col =  color;
    textureCoordinates = texrureCoordinatesIn;

//    m4Boden.vec4(posistion,1.0f);

}