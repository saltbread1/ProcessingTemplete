#version 410

precision highp float;

uniform sampler2D velocityField;
uniform sampler2D newVelocityField;
uniform float deltaX;
uniform float deltaTime;
uniform float viscosity; // kinematic viscosity

in vec2 vTexCoord;

out vec4 fragment;

void main()
{
    vec2 w2 = texture(velocityField, vTexCoord).xy;
    float a = viscosity / (deltaX * deltaX) * deltaTime;
    vec2 wxm = texture(newVelocityField, vTexCoord - vec2(deltaX, 0.0)).xy;
    vec2 wxp = texture(newVelocityField, vTexCoord + vec2(deltaX, 0.0)).xy;
    vec2 wym = texture(newVelocityField, vTexCoord - vec2(0.0, deltaX)).xy;
    vec2 wyp = texture(newVelocityField, vTexCoord + vec2(0.0, deltaX)).xy;
    // one step of Jacobi method
    vec2 w3 = (w2 + a * (wxm + wxp + wym + wyp)) / (1.0 + 4.0 * a);

    fragment = vec4(w3, 0.0, 0.0);
}
