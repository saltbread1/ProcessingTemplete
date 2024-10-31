#version 410

precision highp float;

in vec4 position;
in vec3 normal;

out vec3 vPosition;
out vec3 vNormal;

uniform mat4 modelMatrix;
uniform mat4 transformMatrix;

void main()
{
    vPosition = (modelMatrix * position).xyz;
    vNormal = normalize(modelMatrix * vec4(normal, 0.0)).xyz;
    gl_Position = transformMatrix * position;
}
