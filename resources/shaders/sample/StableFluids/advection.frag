#version 410

precision highp float;

uniform sampler2D velocityField;
uniform float deltaTime;

in vec2 vTexCoord;

out vec4 fragment;

void main()
{
    vec2 w1 = texture(velocityField, vTexCoord).xy;
    vec2 w2 = texture(velocityField, vTexCoord - deltaTime * w1).xy;
    fragment = vec4(w2, 0.0, 0.0);
}
