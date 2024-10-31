#version 410

precision highp float;

uniform sampler2D sampler0;

in vec2 vTexCoord;

out vec4 fragment;

void main()
{
    vec3 srcColor = texture(sampler0, vTexCoord).rgb;

    fragment = vec4(srcColor, 1.0);
}
