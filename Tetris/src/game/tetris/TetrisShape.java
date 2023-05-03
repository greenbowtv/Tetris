package game.tetris;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import game.Cell;
import game.CollectionMatrix;
import game.RotatableShape;

/** Фигурка тетриса.
 * фабрика избавляет от необходимости ручного выбора ячеек для помещения блоков,
 * использования рандома или корректировки вращения
 * Область создания квадрат 4x4
 * каждая фигура обязана генерироваться так, чтобы вращаться относительно центра
 * выбранного в этом квадрате
 * 
 * @author greenbowtv
 *
 */
public class TetrisShape 
	 extends RotatableShape<Tetromino> {

	public TetrisShape(CollectionMatrix<Tetromino> matrix, Cell[] cells, List<Tetromino> bricks)
		throws IllegalStateException {
		
		super(matrix, cells, bricks);
	}
	
	public TetrisShape(CollectionMatrix<Tetromino> matrix, Cell[] cells, List<Tetromino> bricks, 
			Map<Integer, DoubleCell> adjustment)
		throws IllegalStateException {
		
		super(matrix, cells, bricks, adjustment);
	}

	@SuppressWarnings({ "serial" })
	static TetrisShape create(CollectionMatrix<Tetromino> matrix)
			throws IllegalStateException
	{
		var brick = Tetromino.values()[ (int)(Math.random() * 7) ];

		switch (brick) {
		case I:
		return new TetrisShape(matrix, 
				new Cell[] { new Cell(0, 5), new Cell(1, 5), new Cell(2, 5), new Cell(3, 5) },
				Collections.nCopies(4, brick),
				new HashMap<>() {{ 
					put(0, new DoubleCell(-1.0, -1.0));
					put(180, new DoubleCell(-1.0, -1.0));
				}});
		case J:
		return new TetrisShape(matrix, 
				new Cell[] { new Cell(0, 5), new Cell(1, 5), new Cell(2, 5), new Cell(2, 4) },
				Collections.nCopies(4, brick),
				new HashMap<>() {{ 
					put(90, new DoubleCell(0, 1.0));
					put(180, new DoubleCell(-1.0, 0));
				}});
		case L:
		return new TetrisShape(matrix, 
				new Cell[] { new Cell(0, 4), new Cell(1, 4), new Cell(2, 4), new Cell(2, 5) },
				Collections.nCopies(4, brick),
				new HashMap<>() {{ 
					put(0, new DoubleCell(-1.0, -1.0));
					put(90, new DoubleCell(0, -1.0));
					put(180, new DoubleCell(0, -1.0));
				}});
		case O:
		return new TetrisShape(matrix, 
				new Cell[] { new Cell(0, 4), new Cell(0, 5), new Cell(1, 5), new Cell(1, 4) },
				Collections.nCopies(4, brick));
		case S:
		return new TetrisShape(matrix, 
				new Cell[] { new Cell(0, 5), new Cell(0, 4), new Cell(1, 4), new Cell(1, 3) },
				Collections.nCopies(4, brick),
				new HashMap<>() {{ 
					put(0, new DoubleCell(-1.0, 0));
					put(180, new DoubleCell(-1.0, 0));
				}});
		case T:
		return new TetrisShape(matrix, 
				new Cell[] { new Cell(0, 4), new Cell(0, 5), new Cell(0, 6), new Cell(1, 5) },
				Collections.nCopies(4, brick),
				new HashMap<>() {{ 
					put(0, new DoubleCell(-1.0, 0));
					put(90, new DoubleCell(-1.0, 0));
					put(180, new DoubleCell(-1.0, -1.0));
				}});
		case Z:
		return new TetrisShape(matrix, 
				new Cell[] { new Cell(0, 4), new Cell(0, 5), new Cell(1, 5), new Cell(1, 6) },
				Collections.nCopies(4, brick),
				new HashMap<>() {{ 
					put(0, new DoubleCell(-1.0, 0));
					put(180, new DoubleCell(-1.0, 0));
				}});
		}
		
		throw new RuntimeException("невозможно");
	}
}
