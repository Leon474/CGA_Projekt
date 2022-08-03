#version 330 core

//input from vertex shader
in struct VertexData
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


//fragment shader output
out vec4 color;

uniform sampler2D emit;
uniform sampler2D diff;
uniform sampler2D specular;
uniform vec3 EinsLightcolor;
uniform mat3 matSpecular;
uniform float shininess;

uniform float EinsKc;
uniform float EinsKl;
uniform float EinsKq;

uniform vec3 ZweiLightcolor;
uniform vec3 ZweiRichtung;
uniform float ZweiWinkelInnen;
uniform float ZweiWinkelAussen;
uniform float ZweiKc;
uniform float ZweiKl;
uniform float ZweiKq;

uniform vec3 Farbe;

const float levels = 3.0;

void main(){

    float d = sqrt(pow(vertexData.LightDir.x,2)+pow(vertexData.LightDir.y,2)+pow(vertexData.LightDir.z,2));
    float fatt = (1/(EinsKc + (EinsKl * d) + (EinsKq * pow(d,2))));

    float ds = sqrt(pow(vertexData.toLight.x,2)+pow(vertexData.toLight.y,2)+pow(vertexData.toLight.z,2));
    float fatts = (1/(ZweiKc + (ZweiKl * ds) + (ZweiKq * pow(ds,2))));


    // TODO: Pointlight_________________________________________________

    vec3 N = normalize(vertexData.Normal);
    vec3 L = normalize(vertexData.LightDir);

    vec4 diffcolor = texture(diff, vertexData.tc);

    float cosa = max(0.0,dot(N,L));
    vec4 DiffuseTerm = diffcolor * vec4(EinsLightcolor, 1.0f);
    vec4 diffuse = DiffuseTerm * cosa;


    vec3 V = normalize(vertexData.ViewDir);
    vec3 R = normalize(reflect(-L,N));

    vec4 speccolor = texture(specular, vertexData.tc);

    float cosBeta = max(0.0,dot(R,V));
    float cosBetak = pow(cosBeta, shininess);
    vec4 SpecularTerm = speccolor * vec4(EinsLightcolor, 1.0f);
    vec4 spec = SpecularTerm * cosBetak;

    vec4 summePoint = (diffuse + spec)*fatt;

    // TODO: Spotlight____________________________________________________

    vec3 N_spot = normalize(vertexData.Normal);
    vec3 L_spot = normalize(vertexData.toLight);

    vec4 diffcolor_spot = texture(diff, vertexData.tc);

    float cosa_spot = max(0.0,dot(N_spot,-L_spot));
    vec4 DiffuseTerm_spot = diffcolor_spot * vec4(ZweiLightcolor, 1.0f);
    vec4 diffuse_spot = DiffuseTerm_spot * cosa_spot;


    vec3 V_spot = normalize(vertexData.ViewDir);
    vec3 R_spot = normalize(reflect(L_spot,N_spot));

    vec4 speccolor_spot = texture(specular, vertexData.tc);

    float cosBeta_spot = max(0.0,dot(R_spot,V_spot));
    float cosBetak_spot = pow(cosBeta_spot, shininess);
    vec4 SpecularTerm_spot = speccolor_spot * vec4(ZweiLightcolor, 1.0f);
    vec4 spec_spot = SpecularTerm_spot * cosBetak_spot;

    //vec4 spotcolor = texture(diff, vertexData.tc);
    //vec4 spot = spotcolor * vec4(ZweiLightcolor, 1.0f);

    vec4 summeSpot = (diffuse_spot + spec_spot)*fatts;



    float theta = max(0.0,dot(normalize(ZweiRichtung),L_spot));

    float I;

    if (theta > cos(ZweiWinkelInnen)){
        I = 1;
    } else if (theta > cos(ZweiWinkelAussen)){
        I = ((theta - cos(ZweiWinkelAussen)) / (cos(ZweiWinkelInnen) - cos(ZweiWinkelAussen)));
    } else {
        I = 0;
    }

    color = vec4(texture(emit, vertexData.tc) * vec4(Farbe,1) + summePoint + summeSpot*I);

}
