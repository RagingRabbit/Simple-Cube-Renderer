#version 330 core

#define LIGHT_COL vec3(1.0)
#define AMBIENT 0.1

in data {
	vec2 uv;
	vec3 normal;
	vec3 toLight;
	vec3 toCamera;
} pass;

out vec4 out_Color;

uniform sampler2D un_Texture;

void main() {

	vec4 textureColor = texture(un_Texture, pass.uv);
	
	vec3 normal = normalize(pass.normal);
	vec3 toLight = normalize(pass.toLight);
	float diffuseFactor = max(dot(normal, toLight) * 0.5 + 0.5, AMBIENT);
	
	float specularFactor = 0.0;
	if (dot(normal, toLight) > 0.0) {
		vec3 toCamera = normalize(pass.toCamera);
		vec3 reflection = reflect(-toLight, normal);
		specularFactor = max(dot(reflection, toCamera), 0.0);
		specularFactor = pow(specularFactor, 10.0);
	}
	
	vec3 diffuse = textureColor.xyz * diffuseFactor;
	vec3 specular = LIGHT_COL * specularFactor;
	
	out_Color = vec4(diffuse + specular, 1.0);
}
