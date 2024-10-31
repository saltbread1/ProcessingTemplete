#version 410

precision highp float;

in vec4 position;
in vec2 texCoord;

out vec2 vTexCoord;

void main()
{
    vTexCoord = texCoord;
    gl_Position = position;
}
