package xyz.rs.model;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import org.joml.Vector3f;

public class MeshBuilder {

	private static final int NULL = 0;

	public static int createQuadMesh(Vector3f min, Vector3f max) {
		int vao = createVAO();

		float[] positions = { min.x, min.y, min.z, max.x, min.y, min.z, max.x, max.y, min.z, min.x, max.y, min.z };
		float[] colors = { 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 1.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f,
				1.0f };
		int[] indices = { 0, 1, 2, 2, 3, 0 };

		addArrayBuffer(0, 3, positions);
		addArrayBuffer(1, 4, colors);
		addElementBuffer(indices);

		glBindVertexArray(NULL);

		return vao;
	}

	public static int createCubeMesh(Vector3f min, Vector3f max) {
		int vao = createVAO();

		float[] positions = {
				// Front face
				min.x, min.y, max.z, //
				max.x, min.y, max.z, //
				max.x, max.y, max.z, //
				min.x, max.y, max.z, //

				// Back face
				max.x, min.y, min.z, //
				min.x, min.y, min.z, //
				min.x, max.y, min.z, //
				max.x, max.y, min.z, //

				// Left face
				min.x, min.y, min.z, //
				min.x, min.y, max.z, //
				min.x, max.y, max.z, //
				min.x, max.y, min.z, //

				// Right face
				max.x, min.y, max.z, //
				max.x, min.y, min.z, //
				max.x, max.y, min.z, //
				max.x, max.y, max.z, //

				// Top face
				min.x, max.y, min.z, //
				min.x, max.y, max.z, //
				max.x, max.y, max.z, //
				max.x, max.y, min.z, //

				// Bottom face
				min.x, min.y, max.z, //
				min.x, min.y, min.z, //
				max.x, min.y, min.z, //
				max.x, min.y, max.z, //
		};
		float[] normals = {
				// Front face
				0.0f, 0.0f, 1.0f, //
				0.0f, 0.0f, 1.0f, //
				0.0f, 0.0f, 1.0f, //
				0.0f, 0.0f, 1.0f, //

				// Back face
				0.0f, 0.0f, -1.0f, //
				0.0f, 0.0f, -1.0f, //
				0.0f, 0.0f, -1.0f, //
				0.0f, 0.0f, -1.0f, //

				// Left face
				-1.0f, 0.0f, 0.0f, //
				-1.0f, 0.0f, 0.0f, //
				-1.0f, 0.0f, 0.0f, //
				-1.0f, 0.0f, 0.0f, //

				// Right face
				1.0f, 0.0f, 0.0f, //
				1.0f, 0.0f, 0.0f, //
				1.0f, 0.0f, 0.0f, //
				1.0f, 0.0f, 0.0f, //

				// Top face
				0.0f, 1.0f, 0.0f, //
				0.0f, 1.0f, 0.0f, //
				0.0f, 1.0f, 0.0f, //
				0.0f, 1.0f, 0.0f, //

				// Bottom face
				0.0f, -1.0f, 0.0f, //
				0.0f, -1.0f, 0.0f, //
				0.0f, -1.0f, 0.0f, //
				0.0f, -1.0f, 0.0f, //
		};
		float[] uvs = {
				// Front face
				0.0f, 1.0f, //
				1.0f, 1.0f, //
				1.0f, 0.0f, //
				0.0f, 0.0f, //

				// Back face
				0.0f, 1.0f, //
				1.0f, 1.0f, //
				1.0f, 0.0f, //
				0.0f, 0.0f, //

				// Left face
				0.0f, 1.0f, //
				1.0f, 1.0f, //
				1.0f, 0.0f, //
				0.0f, 0.0f, //

				// Right face
				0.0f, 1.0f, //
				1.0f, 1.0f, //
				1.0f, 0.0f, //
				0.0f, 0.0f, //

				// Top face
				0.0f, 1.0f, //
				1.0f, 1.0f, //
				1.0f, 0.0f, //
				0.0f, 0.0f, //

				// Bottom face
				0.0f, 1.0f, //
				1.0f, 1.0f, //
				1.0f, 0.0f, //
				0.0f, 0.0f, //
		};

		addArrayBuffer(0, 3, positions);
		addArrayBuffer(1, 3, normals);
		addArrayBuffer(2, 2, uvs);

		glBindVertexArray(NULL);

		return vao;
	}

	private static void addArrayBuffer(int index, int size, float[] data) {
		int vbo = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vbo);
		glBufferData(GL_ARRAY_BUFFER, data, GL_STATIC_DRAW);
		glVertexAttribPointer(index, size, GL_FLOAT, false, 0, 0);
	}

	private static void addElementBuffer(int[] data) {
		int ibo = glGenBuffers();
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, data, GL_STATIC_DRAW);
	}

	private static int createVAO() {
		int vao = glGenVertexArrays();
		glBindVertexArray(vao);
		return vao;
	}

}
