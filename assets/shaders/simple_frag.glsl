#version 330 core

in vec3 col;

//fragment shader output
out vec4 color;
in vec2 textureCoordinates;

void main(){
    //color = vec4(col, 1.0f);
    color=vec4(textureCoordinates,0.0,1.0);
}
