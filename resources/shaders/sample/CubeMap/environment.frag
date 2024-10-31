#version 410

precision highp float;

in vec3 vTexCoord;

out vec4 fragColor;

uniform samplerCube cubeTexture;

void main()
{
    vec3 col = texture(cubeTexture, vTexCoord).rgb;
    fragColor = vec4(col, 1.0);
}
