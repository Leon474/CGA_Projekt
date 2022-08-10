#version 330 core
in vec2 outTexCoord;
in vec3 mvPos;
out vec4 fragColor;

uniform sampler2D texture_sampler;
uniform vec3 ambientLight;  //original
//uniform float ambientLight;


void main()
{
    //ambientLight = 0.20f;
    fragColor = vec4(ambientLight, 1) * texture(texture_sampler, outTexCoord);
}