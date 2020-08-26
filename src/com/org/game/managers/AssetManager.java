package com.org.game.managers;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.GdxRuntimeException;

public class AssetManager {

	private HashMap<String, Texture> texture;

	public AssetManager() {
		texture = new HashMap<String, Texture>();
	}

	public void loadTexture(String path) {
		String key;
		key = path.substring(0, path.length());

		loadTexture(path, key);
	}

	public void loadTexture(String... path) {
		String key;
		for (String text_path : path) {
			key = text_path.substring(0, text_path.length());
			loadTexture(text_path, key);
		}
	}

	public void loadTexture(String path, String key) {
		if (texture.containsKey(key))
			throw new GdxRuntimeException("Asset already loaded: " + key);
		Texture img = new Texture(Gdx.files.internal(path));
		texture.put(key, img);
	}

	public Texture getTexture(String key) {
		if (key != null) {
			return texture.get(key);
		}
		return null;
	}

	public void removeTexture(String key) {
		Texture img = texture.get(key);
		if (img != null) {
			texture.remove(key);
			img.dispose();
		}
	}
}