attribute vec4 position;
attribute vec2 texCoord;

uniform mat4 transformMatrix;

varying vec2 vTexCoord;

void main()
{
    vTexCoord = texCoord;
    gl_Position = transformMatrix * position;
}
