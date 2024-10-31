attribute vec4 position;
attribute vec3 normal;
attribute vec4 color;
attribute vec2 texCoord;

uniform mat4 modelviewMatrix;
uniform mat4 projectionMatrix;
uniform mat3 normalMatrix;
uniform vec4 lightPosition;

varying vec4 vColor;
varying vec2 vTexCoord;
varying vec3 vLightDirection;
varying vec3 vEyeDirection;

void main()
{
    // basis in the tangent vector space represented in the world space
    vec3 n = normalize(normalMatrix * normal); // z axis
    vec3 t = normalize(cross(vec3(0.0, 1.0, 0.0), n)); // x axis
    vec3 b = cross(n, t); // y axis

    // change of basis matrix (world space to tangent vector space)
    mat3 p = mat3(t, b, n);

    // in the world space
    vec4 worldPosition = modelviewMatrix * position;
    vec4 worldLightPosition = lightPosition;
    vec3 worldLightDirection = normalize(worldLightPosition.xyz * worldPosition.w - worldPosition.xyz * worldLightPosition.w);

    // in the tangent vector space
    // [local] = p^{-1} * [world] = p^{T} * [world] = ([world]^{T} * p)^{T}
    vec3 tangentLightDirection = normalize(worldLightDirection * p);
    vec3 tangentEyeDirection = normalize(-worldPosition.xyz * p);

    vColor = color;
    vTexCoord = texCoord;
    vLightDirection = tangentLightDirection;
    vEyeDirection = tangentEyeDirection;

    gl_Position = projectionMatrix * worldPosition;
}
