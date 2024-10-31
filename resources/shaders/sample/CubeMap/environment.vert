#version 410

precision highp float;

in vec4 position;

out vec3 vTexCoord;

uniform mat4 transformMatrix;

void main()
{
    vTexCoord = position.xyz;
    gl_Position = transformMatrix * position;
}
