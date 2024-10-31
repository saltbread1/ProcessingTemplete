// use normal height map (rgb: normal, a: height)

uniform sampler2D normalHeightMap;
uniform float parallaxScale;
uniform vec3 kSpec;
uniform float kShi;

varying vec4 vColor;
varying vec2 vTexCoord;
varying vec3 vLightDirection;
varying vec3 vEyeDirection;

void main()
{
    vec3 light = normalize(vLightDirection);
    vec3 eye = normalize(vEyeDirection);

    float h = texture2D(normalHeightMap, vTexCoord).a * parallaxScale;
    vec2 hTexCoord = fract(vTexCoord - h * eye.xy);
    vec3 normal = texture2D(normalHeightMap, hTexCoord).rgb * 2.0 - 1.0;

    vec3 halfVec = normalize(light + eye);
    float diffuse = clamp(dot(normal, light), 0.0, 1.0);
    float specular = pow(clamp(dot(normal, halfVec), 0.0, 1.0), kShi);
    vec3 color = vec3(diffuse) * vColor.rgb + vec3(specular) * kSpec;

    gl_FragColor = vec4(color, 1.0);
}
