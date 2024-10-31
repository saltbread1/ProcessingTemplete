varying vec2 vTexCoord;

uniform sampler2D sampler0;
uniform sampler2D sampler1;
uniform int frag;

void main()
{
    if (frag == 0)
    {
        gl_FragColor = texture(sampler0, vTexCoord);
    }
    else
    {
        gl_FragColor = texture(sampler1, vTexCoord);
    }
}
