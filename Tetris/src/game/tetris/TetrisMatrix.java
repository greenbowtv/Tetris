package game.tetris;

import game.CollectionMatrix;

public class TetrisMatrix 
extends CollectionMatrix<Tetromino> {

	public TetrisMatrix() {
		super(Tetromino.class, 20, 10);
	}
}
