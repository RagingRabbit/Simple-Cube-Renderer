package xyz.rs.core;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

public class Display {

	private static final long NULL = 0;

	private static long window;
	private static DisplayAdapter display;
	private static InputAdapter input;

	private static int mouseX, mouseY;

	public static void create(int width, int height, String title, DisplayAdapter displayAdapter,
			InputAdapter inputAdapter) {
		display = displayAdapter;
		input = inputAdapter;

		glfwInit();

		window = glfwCreateWindow(width, height, title, NULL, NULL);

		glfwMakeContextCurrent(window);
		GL.createCapabilities();

		glfwSetErrorCallback(GLFWErrorCallback.createPrint(System.err));
		glfwSetFramebufferSizeCallback(window, (long window, int newWidth, int newHeight) -> {
			display.onResize(newWidth, newHeight);
		});
		glfwSetCursorPosCallback(window, (long window, double xpos, double ypos) -> {
			int dx = (int) xpos - mouseX;
			int dy = (int) ypos - mouseY;
			input.onMouseMove(dx, dy);
			mouseX = (int) xpos;
			mouseY = (int) ypos;
		});
		glfwSetKeyCallback(window, (long window, int key, int scancode, int action, int mods) -> {
			input.onKeyEvent(key, action);
		});

		glfwSwapInterval(0);

		double[] xposPtr = new double[1];
		double[] yposPtr = new double[1];
		glfwGetCursorPos(window, xposPtr, yposPtr);
		mouseX = (int) xposPtr[0];
		mouseY = (int) yposPtr[0];
	}

	public static void captureCursor() {
		glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_DISABLED);
	}

	public static void freeCursor() {
		glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_NORMAL);
	}

	public static void update() {
		glfwPollEvents();
		glfwSwapBuffers(window);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	}

	public static boolean isOpen() {
		return !glfwWindowShouldClose(window);
	}

	public static void destroy() {
		glfwDestroyWindow(window);
		glfwTerminate();
	}
}
