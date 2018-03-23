package xyz.rs;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import xyz.rs.core.Display;
import xyz.rs.core.DisplayAdapter;
import xyz.rs.core.InputAdapter;
import xyz.rs.core.Shader;
import xyz.rs.core.Texture;
import xyz.rs.io.FileInput;
import xyz.rs.model.MeshBuilder;

public class Main implements DisplayAdapter, InputAdapter {

	public static float delta;
	public static int width = 1280;
	public static int height = 720;
	public static final String title = "";

	private static final float FOV = 90.0f;
	private static final float NEAR = 0.1f;
	private static final float FAR = 1000.0f;

	private static final float SENSITIVITY = 0.001f;

	private static final float MOVE_SPEED = 1.0f;

	private int cube;
	private Shader shader;
	private Texture texture;

	private Vector3f cameraPosition;
	private Vector3f cameraRotation;

	private Matrix4f projectionMatrix;
	private Matrix4f viewMatrix;

	private boolean cursorCaptured;
	private boolean forward, backward, left, right, up, down;

	private void init() {
		Display.captureCursor();

		glEnable(GL_DEPTH_TEST);
		glEnable(GL_CULL_FACE);
		glCullFace(GL_BACK);

		cube = MeshBuilder.createCubeMesh(new Vector3f(-0.5f, -0.5f, -0.5f), new Vector3f(0.5f, 0.5f, 0.5f));

		String vertexSrc = FileInput.loadText("res/quad.vs.glsl");
		String fragmentSrc = FileInput.loadText("res/quad.fs.glsl");
		shader = new Shader(vertexSrc, fragmentSrc);

		texture = FileInput.loadTexture("res/image.png");

		cameraPosition = new Vector3f(0.0f, 0.0f, 1.0f);
		cameraRotation = new Vector3f();

		projectionMatrix = new Matrix4f();
		viewMatrix = new Matrix4f();

		updateProjectionMatrix();
		updateViewMatrix();

		cursorCaptured = false;
		toggleCursor();
	}

	private void toggleCursor() {
		if (cursorCaptured) {
			Display.freeCursor();
			cursorCaptured = false;
		} else {
			Display.captureCursor();
			cursorCaptured = true;
		}
	}

	@Override
	public void onResize(int width, int height) {
		glViewport(0, 0, width, height);
		updateProjectionMatrix();

		System.out.println("Window resized to " + width + " x " + height);
	}

	@Override
	public void onMouseMove(int dx, int dy) {
		if (!cursorCaptured) {
			return;
		}
		cameraRotation.y -= dx * SENSITIVITY;
		cameraRotation.x -= dy * SENSITIVITY;
	}

	@Override
	public void onKeyEvent(int key, int action) {
		switch (key) {
		case GLFW_KEY_ESCAPE:
			if (action == GLFW_PRESS) {
				toggleCursor();
			}
			break;
		case GLFW_KEY_W:
			if (action == GLFW_PRESS) {
				forward = true;
			} else if (action == GLFW_RELEASE) {
				forward = false;
			}
			break;
		case GLFW_KEY_S:
			if (action == GLFW_PRESS) {
				backward = true;
			} else if (action == GLFW_RELEASE) {
				backward = false;
			}
			break;
		case GLFW_KEY_A:
			if (action == GLFW_PRESS) {
				left = true;
			} else if (action == GLFW_RELEASE) {
				left = false;
			}
			break;
		case GLFW_KEY_D:
			if (action == GLFW_PRESS) {
				right = true;
			} else if (action == GLFW_RELEASE) {
				right = false;
			}
			break;
		case GLFW_KEY_SPACE:
			if (action == GLFW_PRESS) {
				up = true;
			} else if (action == GLFW_RELEASE) {
				up = false;
			}
			break;
		case GLFW_KEY_LEFT_SHIFT:
			if (action == GLFW_PRESS) {
				down = true;
			} else if (action == GLFW_RELEASE) {
				down = false;
			}
			break;
		}
	}

	private void updateProjectionMatrix() {
		projectionMatrix.identity();
		projectionMatrix.perspective((float) Math.toRadians(FOV), (float) width / height, NEAR, FAR);
	}

	private void updateViewMatrix() {
		viewMatrix.identity();
		viewMatrix.rotateX(-cameraRotation.x);
		viewMatrix.rotateY(-cameraRotation.y);
		viewMatrix.translate(-cameraPosition.x, -cameraPosition.y, -cameraPosition.z);
	}

	public void update() {

		Vector3f delta = new Vector3f();
		float verticalDt = 0.0f;
		if (forward) {
			delta.z--;
		}
		if (backward) {
			delta.z++;
		}
		if (left) {
			delta.x--;
		}
		if (right) {
			delta.x++;
		}
		if (up) {
			verticalDt++;
		}
		if (down) {
			verticalDt--;
		}
		if (delta.x != 0.0f || delta.z != 0.0f) {
			delta.normalize(MOVE_SPEED * Main.delta);
			delta.rotateAxis(cameraRotation.y, 0.0f, 1.0f, 0.0f);
			cameraPosition.add(delta);
		}
		if (verticalDt != 0.0f) {
			verticalDt *= MOVE_SPEED * Main.delta;
			cameraPosition.add(0.0f, verticalDt, 0.0f);
		}

		updateViewMatrix();

		shader.start();

		shader.setMatrix4f("un_Projection", projectionMatrix);
		shader.setMatrix4f("un_View", viewMatrix);

		texture.bind(0);

		glBindVertexArray(cube);

		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		glEnableVertexAttribArray(2);

		glDrawArrays(GL_QUADS, 0, 24);
	}

	public void close() {
		glDeleteVertexArrays(cube);
	}

	public static void main(String[] args) {

		Main main = new Main();
		Display.create(width, height, title, main, main);

		main.init();

		long lastFrame = System.nanoTime();
		long lastSecond = System.nanoTime();
		int frames = 0;

		while (Display.isOpen()) {
			long now = System.nanoTime(); // current time
			if (now - lastSecond >= 1e9) {
				System.out.println(frames + " FPS");
				lastSecond = now;
				frames = 0;
			}

			delta = (now - lastFrame) / 1e9f;
			lastFrame = now;

			main.update();

			Display.update();
			frames++;
		}

		main.close();

		Display.destroy();

	}

}
