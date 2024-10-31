#version 410

precision highp float;

uniform sampler2D velocityField;
uniform vec2 forcePos;
uniform vec2 forceVec;
uniform float deltaTime;
uniform float exponent;

in vec2 vTexCoord;

out vec4 fragment;

void main()
{
    vec2 w0 = texture(velocityField, vTexCoord).xy;
    vec2 force = forceVec * exp(-exponent * distance(forcePos, vTexCoord));
    vec2 w1 = w0 + force * deltaTime;
    fragment = vec4(w1, 0.0, 0.0);
}
