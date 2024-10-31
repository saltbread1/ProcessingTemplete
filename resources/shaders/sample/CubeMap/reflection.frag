#version 410

precision highp float;

in vec3 vPosition;
in vec3 vNormal;

out vec4 fragColor;

uniform samplerCube cubeTexture;
uniform vec3 eyePosition;

void main()
{
    vec3 ref = reflect(vPosition - eyePosition, vNormal);
    vec3 col = texture(cubeTexture, ref).rgb;
    fragColor = vec4(col, 1.0);
}
