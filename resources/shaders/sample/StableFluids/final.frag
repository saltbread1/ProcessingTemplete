#version 410

precision highp float;

uniform sampler2D sampler0;
uniform sampler2D velocityField;
uniform float deltaTime;

in vec2 vTexCoord;

out vec4 fragment;

void main()
{
    vec2 v = texture(velocityField, vTexCoord).xy;
    vec3 srcColor = texture(sampler0, vTexCoord).rgb;
    vec3 advectColor = texture(sampler0, vTexCoord - deltaTime * v).rgb;

    fragment = vec4(mix(srcColor, advectColor, 1.0), 1.0);
}
