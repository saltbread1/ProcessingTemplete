#version 410

in vec4 position;
in vec4 color;

out vec4 vColor;

void main()
{
    vColor = color;
    gl_Position = position;
}
