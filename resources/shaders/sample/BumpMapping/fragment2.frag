// use only height map, create normal map

uniform sampler2D heightMap;
uniform float hScale;
uniform float parallaxScale;
uniform float dx;
uniform float dy;
uniform vec3 kSpec;
uniform float kShi;

varying vec4 vColor;
varying vec2 vTexCoord;
varying vec3 vLightDirection;
varying vec3 vEyeDirection;

// kernel of gaussian filter (sigma = 2)
const mat3 weight = mat3(0.0751, 0.1238, 0.0751, 0.1238, 0.2042, 0.1238, 0.0751, 0.1238, 0.0751);

vec3 getNormal(in vec2 uv)
{
    vec2 uv2 = fract(uv);
    float h = texture2D(heightMap, uv2).r;
    float hxm = texture2D(heightMap, uv2 - vec2(dx, 0.0)).r;
    float hxp = texture2D(heightMap, uv2 + vec2(dx, 0.0)).r;
    float hym = texture2D(heightMap, uv2 - vec2(0.0, dy)).r;
    float hyp = texture2D(heightMap, uv2 + vec2(0.0, dy)).r;
#if 1
    vec3 normal = normalize(vec3(-(hxp - hxm) * 0.5 / dx, -(hyp - hym) * 0.5 / dy, hScale));
#else // other method
    vec3 vxm = vec3(-dx, 0.0, hxm - h);
    vec3 vxp = vec3(dx, 0.0, hxp - h);
    vec3 vym = vec3(0.0, -dy, hym - h);
    vec3 vyp = vec3(0.0, dy, hyp - h);
    vec3 n1 = cross(vxm, vym);
    vec3 n2 = cross(vym, vxp);
    vec3 n3 = cross(vxp, vyp);
    vec3 n4 = cross(vyp, vxm);
    vec3 normal = n1 + n2 + n3 + n4;
    normal.z *= hScale;
    normal = normalize(normal);
#endif

    return normal;
}

vec3 interPolateNormal(in vec2 uv)
{
    // apply gaussian filter
    vec3 n1 = getNormal(uv + vec2(-dx, -dy));
    vec3 n2 = getNormal(uv + vec2(0.0, -dy));
    vec3 n3 = getNormal(uv + vec2(dx, -dy));
    vec3 n4 = getNormal(uv + vec2(-dx, 0.0));
    vec3 n5 = getNormal(uv + vec2(0.0, 0.0));
    vec3 n6 = getNormal(uv + vec2(dx, 0.0));
    vec3 n7 = getNormal(uv + vec2(-dx, dy));
    vec3 n8 = getNormal(uv + vec2(0.0, dy));
    vec3 n9 = getNormal(uv + vec2(dx, dy));

    return n1 * weight[0][0] + n2 * weight[0][1] + n3 * weight[0][2]
            + n4 * weight[1][0] + n5 * weight[1][1] + n6 * weight[1][2]
            + n7 * weight[2][0] + n8 * weight[2][1] + n9 * weight[2][2];
}

void main()
{
    vec3 light = normalize(vLightDirection);
    vec3 eye = normalize(vEyeDirection);

    float h = texture2D(heightMap, vTexCoord).r * parallaxScale;
    vec2 hTexCoord = vTexCoord - h * eye.xy;
    //vec3 normal = texture2D(normalHeight, hTexCoord).xyz * 2.0 - 1.0;

    vec3 normal = interPolateNormal(hTexCoord);
    vec3 halfVec = normalize(light + eye);
    float diffuse = clamp(dot(normal, light), 0.0, 1.0);
    float specular = pow(clamp(dot(normal, halfVec), 0.0, 1.0), kShi);
    vec3 color = vec3(diffuse) * vColor.rgb + vec3(specular) * kSpec;

    gl_FragColor = vec4(color, 1.0);
}
