#version 330 core

#define LIGHT_POS vec3(10.0)

layout (location = 0) in vec3 in_Position;
layout (location = 1) in vec3 in_Normal;
layout (location = 2) in vec2 in_Uv;

out data {
	vec2 uv;
	vec3 normal;
	vec3 toLight;
	vec3 toCamera;
} pass;

uniform mat4 un_Projection;
uniform mat4 un_View;

void main() {
	gl_Position = un_Projection * un_View * vec4(in_Position, 1.0);
	
	pass.uv = in_Uv;
	pass.normal = in_Normal;
	pass.toLight = LIGHT_POS - in_Position;
	pass.toCamera = (inverse(un_View) * vec4(0.0, 0.0, 0.0, 1.0)).xyz - in_Position;
}
