package xyz.rs.io;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.imageio.ImageIO;

import xyz.rs.core.Texture;

public class FileInput {

	public static String loadText(String filepath) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(new File(filepath)));
			StringBuilder result = new StringBuilder();
			String line = "";

			while ((line = reader.readLine()) != null) {
				result.append(line).append("\n");
			}

			reader.close();

			return result.toString();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return "";
	}

	public static Texture loadTexture(String filepath) {
		try {
			BufferedImage image = ImageIO.read(new File(filepath));
			int[] pixels = image.getRGB(0, 0, image.getWidth(), image.getHeight(), null, 0, image.getWidth());
			Texture texture = new Texture();
			texture.set(image.getWidth(), image.getHeight(), pixels);

			return texture;
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

}
