#version 410

#define MAX_SOLUTIONS 16
#define MAX_ITERATIONS 32
#define PI 3.141592653589793

precision highp float;

uniform vec2 sol[MAX_SOLUTIONS];
uniform int degree;
uniform vec2 radius;
uniform vec2 alpha;

in vec2 vTexCoord;

out vec4 fragment;

vec2 conjugate(vec2 a)
{
    return vec2(a.x, -a.y);
}

vec2 mul(vec2 a, vec2 b)
{
    return vec2(a.x * b.x - a.y * b.y, a.x * b.y + a.y * b.x);
}

vec2 div(vec2 a, vec2 b)
{
    return mul(a, conjugate(b)) / dot(b, b);
}

vec2 func(vec2 z)
{
    vec2 ret = vec2(1);
    for (int i = 0; i < degree; i++)
    {
        ret = mul(ret, z - sol[i]);
    }
    return ret;
}

vec2 deri(vec2 z)
{
    vec2 ret = vec2(0);
    for (int i = 0; i < degree; i++)
    {
        vec2 tmp = vec2(1);
        for (int j = 0; j < degree; j++)
        {
            if (j == i) { continue; }
            tmp = mul(tmp, z - sol[j]);
        }
        ret += tmp;
    }
    return ret;
}

vec3 hsb2rgb(vec3 hsb)
{
    return ((clamp(abs(fract(hsb.x+vec3(0,2,1)/3.)*6.-3.)-1.,0.,1.)-1.)*hsb.y+1.)*hsb.z;
}

vec3 dye(vec2 z)
{
    float h = 0.0f;
    if (abs(z.x) > 0.0001)
    {
        h = fract((atan(z.y, z.x) / (PI * 2.0) + 1.0));
    }
    float s = 0.8;
    float b = 0.8;
    return hsb2rgb(vec3(h, s, b));
}

void main()
{
    vec2 z = (vTexCoord * 2.0 - 1.0) * radius;

#if 0 // debug
    for (int i = 0; i < degree; i++)
    {
        vec2 s = (sol[i] / radius + 1.0) * 0.5;
        if (dot(vTexCoord - s, vTexCoord - s) < 0.001)
        {
            fragment = vec4(1.0, 1.0, 1.0, 1.0);
            return;
        }
    }
#endif

    // newton method
    int flag = 0;
    for (int i = 0; i < MAX_ITERATIONS; i++)
    {
        z -= mul(alpha, div(func(z), deri(z)));

        for (int j = 0; j < degree; j++)
        {
            vec2 d = z - sol[j];
            if (dot(d, d) < 0.01)
            {
                flag = 1;
                z = sol[j];
                break;
            }
        }
        if (flag != 0) { break; }
    }

    // minimum distance
//    float minDisSq = 10000.0;
//    for (int i = 0; i < degree; i++)
//    {
//        float d = dot(z - sol[i], z - sol[i]);
//        if (d < minDisSq)
//        {
//            z = sol[i];
//            minDisSq = d;
//        }
//    }

    fragment = vec4(dye(z), 1.0);
}
