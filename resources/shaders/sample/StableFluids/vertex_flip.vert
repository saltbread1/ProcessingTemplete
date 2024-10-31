#version 410

precision highp float;

in vec4 position;
in vec2 texCoord;

out vec2 vTexCoord;

void main()
{
    vTexCoord = vec2(texCoord.x, 1.0 - texCoord.y);
    gl_Position = position;
}
