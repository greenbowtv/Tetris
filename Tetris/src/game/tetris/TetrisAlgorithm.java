package game.tetris;

import java.awt.Toolkit;

import game.Algorithm;
import game.Direction;
import game.GameOverException;
import game.Range;

// "сердце" игры Пажитнова
public class TetrisAlgorithm 
  implements Algorithm {

	private TetrisMatrix well;
	private TetrisShape shape;
	private Range range;
	private boolean newGame;
	private boolean paused;

	public TetrisAlgorithm(TetrisMatrix well) {
		this.well = well;
		shape = null;
		range = well.getRange();
		newGame = true;
		paused = false;
	}

	@Override
	public void moveLeft() {
		if (paused) {
			Toolkit.getDefaultToolkit().beep();
		} else if (shape != null)
			shape.move(Direction.LEFT);
	}

	@Override
	public void moveRight() {
		if (paused) {
			Toolkit.getDefaultToolkit().beep();
		} else if (shape != null)
			shape.move(Direction.RIGHT);
	}

	@Override
	public void moveUp() {
		Toolkit.getDefaultToolkit().beep();
	}

	@Override
	public void optional() {
		if (paused) { 
			Toolkit.getDefaultToolkit().beep(); 
		} else if (shape != null)
			shape.rotate();
	}

	@Override
	public void moveDown() {
		if (paused) {
			Toolkit.getDefaultToolkit().beep();
			return;
		}

		if (shape == null)
			return;

		if (!shape.move(Direction.DOWN)) {
			int minY = shape.getGeometry().min.r;
			int maxY = shape.getGeometry().max.r;
			shape = null;

			int removed = 0;
			for (int y = maxY; y >= minY + removed;) {
				if (well.isFull(range.row(y))) {
					well.removeAll(range.row(y));
					var bricks = well.removeAll(range.rows(0 + removed, y));
					well.addAll(range.rows(1 + removed, 1 + y), bricks);
					removed++;
				} else
					y--;
			}
			// если линии не сокращались, то 2 тика таймера для игрока ничего 
			// не происходило, что выглядит как зависание
			// можем обнаружить конец игры здесь - ничего сделать нельзя
			// потребуется 2я попытка создания в помеченном методе
			// это надежно, т.к. при любая фигура при любом угле поворота
			// содержит блок в ячейке-центре для каждой фигуре
			// потребуется второй раз неудачного создания (будут фрагменты 2х
//			цветов но это уже не важно), которое надежно - у фигур есть
//			как минимум 1 общая для всех ячейка-центр
			if (removed == 0) {
				try {
					shape = TetrisShape.create(well);
				} catch (IllegalStateException e) { }
			}
		}
	}

	@Override
	public void onTimerTick() throws GameOverException {
		if (newGame) {
			well.clear();
			newGame = false;
		}
		
		if (shape == null) {
			try {
				shape = TetrisShape.create(well);
			} catch (IllegalStateException e) {
				newGame = true;
				throw new GameOverException();
			}
		} else
			moveDown();
	}

	@Override
	public void setPaused(boolean flag) {
		this.paused = flag;
	}
}