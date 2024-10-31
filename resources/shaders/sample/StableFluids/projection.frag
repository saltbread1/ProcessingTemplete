#version 410

precision highp float;

uniform sampler2D velocityField;
uniform sampler2D newVelocityField;
uniform float deltaX;

in vec2 vTexCoord;

out vec4 fragment;

void main()
{
    vec2 w3 = texture(velocityField, vTexCoord).xy;
    float wxm = texture(velocityField, vTexCoord - vec2(deltaX, 0.0)).x;
    float wxp = texture(velocityField, vTexCoord + vec2(deltaX, 0.0)).x;
    float wym = texture(velocityField, vTexCoord - vec2(0.0, deltaX)).y;
    float wyp = texture(velocityField, vTexCoord + vec2(0.0, deltaX)).y;
    float div = -0.5 * deltaX * (wxp - wxm + wyp - wym);

    float pxm = texture(newVelocityField, vTexCoord - vec2(deltaX, 0.0)).z;
    float pxp = texture(newVelocityField, vTexCoord + vec2(deltaX, 0.0)).z;
    float pym = texture(newVelocityField, vTexCoord - vec2(0.0, deltaX)).z;
    float pyp = texture(newVelocityField, vTexCoord + vec2(0.0, deltaX)).z;
    float p = 0.25 * (div + pxm + pxp + pym + pyp);

    vec2 gradp = 0.5 * vec2(pxp - pxm, pyp - pym) / deltaX;
    vec2 w4 = w3 - gradp;

    fragment = vec4(w4, p, 0.0);
}
