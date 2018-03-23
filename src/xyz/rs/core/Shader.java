package xyz.rs.core;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

import java.util.HashMap;
import java.util.Map;

import org.joml.Matrix4f;

public class Shader {

	private int program;
	private int vertex;
	private int fragment;

	private final Map<String, Integer> uniformCache;

	public Shader(String vertexSrc, String fragmentSrc) {

		uniformCache = new HashMap<String, Integer>();

		program = glCreateProgram();
		vertex = glCreateShader(GL_VERTEX_SHADER);
		fragment = glCreateShader(GL_FRAGMENT_SHADER);

		{
			glShaderSource(vertex, vertexSrc);
			glCompileShader(vertex);
			int status = glGetShaderi(vertex, GL_COMPILE_STATUS);
			if (status == GL_FALSE) {
				String log = glGetShaderInfoLog(vertex);
				System.err.println("Failed to compile vertex shader:\n" + log);
				return;
			}
		}

		{
			glShaderSource(fragment, fragmentSrc);
			glCompileShader(fragment);
			int status = glGetShaderi(fragment, GL_COMPILE_STATUS);
			if (status == GL_FALSE) {
				String log = glGetShaderInfoLog(fragment);
				System.err.println("Failed to compile fragment shader:\n" + log);
				return;
			}
		}

		glAttachShader(program, vertex);
		glAttachShader(program, fragment);

		glLinkProgram(program);
		glValidateProgram(program);

		glDeleteShader(vertex);
		glDeleteShader(fragment);

	}

	public void setMatrix4f(String name, Matrix4f mat) {
		float[] buffer = new float[16];
		mat.get(buffer);
		glUniformMatrix4fv(getUniformLocation(name), false, buffer);
	}

	private int getUniformLocation(String name) {
		if (!uniformCache.containsKey(name)) {
			uniformCache.put(name, glGetUniformLocation(program, name));
		}
		return uniformCache.get(name);
	}

	public void start() {
		glUseProgram(program);
	}

	public void destroy() {
		glDeleteProgram(program);
	}
}
