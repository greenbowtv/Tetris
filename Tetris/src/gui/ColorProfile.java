package gui;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import game.tetris.Tetromino;

// фабрика цветовых настроек для рисовальщика
public class ColorProfile {
	public static Map<Tetromino, Color> tetris() {
		var colors = new HashMap<Tetromino, Color>();
		colors.put(Tetromino.I, new Color(0, 240, 240));
		colors.put(Tetromino.J, new Color(0, 0, 240));
		colors.put(Tetromino.L, new Color(240, 160, 0));
		colors.put(Tetromino.O, new Color(240, 240, 0));
		colors.put(Tetromino.S, new Color(0, 240, 0));
		colors.put(Tetromino.T, new Color(160, 0, 240));
		colors.put(Tetromino.Z, new Color(240, 0, 0));
		return colors;
	}
}
