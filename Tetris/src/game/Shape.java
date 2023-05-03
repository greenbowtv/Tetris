package game;

import java.util.Arrays;
import java.util.List;

/** Управление группой элементов (brick) в ячейках (cell) матрицы.
 * Аналогия: треугольная рамка на бильярдном столе. 
 * В нее кладутся элементы, их можно группой перемещать.
 * А если рамку убрать, они становятся частью кучи (heap).
 * Если нет места, генерируется исключение, это может означать конец игры
 * 
 * @author greenbowtv
 *
 * @param <Br> "блок, элемент фигуры/матрицы
 */
public class Shape<Br> {
	
	public static void main(String[] args) {
		var matrix = new CollectionMatrix<Integer>(Integer.class, 5, 4);
		matrix.addListener(e -> System.out.println(matrix));
		
		Cell[] cells = {new Cell(1, 1), new Cell(1, 2), new Cell(1, 3), new Cell(2, 1)};
		var shape = new Shape<Integer>(matrix, cells, Arrays.asList(1, 2, 3, 4));
		System.out.println(shape);
		
		shape.move(Direction.LEFT);
		shape.move(Direction.LEFT);
	}
	

	
	public static class Geometry {
		public final Cell min;
		public final Cell max;

		public Geometry(Cell min, Cell max) {
			this.min = min;
			this.max = max;
		}
		
		@Override
		public String toString() {
			return "Info[min="+min+",max="+max+"]";
		}
	}
	
	private CollectionMatrix<Br> matrix;
	protected Cell[] 			cells;

	public Shape(CollectionMatrix<Br> matrix, Cell[] cells, List<Br> bricks) 
			throws IllegalStateException {
		
		if (cells.length  == 0)
			throw new IllegalArgumentException();	
		if (matrix.addAll(cells, bricks) != cells.length)
			throw new IllegalStateException("в матрице нет места под все блоки");
		
		this.matrix = matrix;
		this.cells = cells;
	}

	public Geometry getGeometry() {
		int minR, maxR, minC, maxC;
		minR = maxR = cells[0].r;
		minC = maxC = cells[0].c;
		for (int i = 1; i < cells.length; i++) {
			if (cells[i].r < minR) minR = cells[i].r;
			if (cells[i].r > maxR) maxR = cells[i].r;
			if (cells[i].c < minC) minC = cells[i].c;
			if (cells[i].c > maxC) maxC = cells[i].c;
		}
		return new Geometry(new Cell(minR, minC), new Cell(maxR, maxC));
	}
	
	protected Cell[] calcFor(Direction dir, int offset) {
		Cell[] newCells = new Cell[cells.length];
	
		for (int i = 0; i < cells.length; i++) {
			switch (dir) {
			case LEFT:
				newCells[i] = new Cell(cells[i].r, cells[i].c - offset);
				break;
			case RIGHT:
				newCells[i] = new Cell(cells[i].r, cells[i].c + offset);
				break;
			case UP:
				newCells[i] = new Cell(cells[i].r - offset, cells[i].c);
				break;
			case DOWN:
				newCells[i] = new Cell(cells[i].r + offset, cells[i].c);
				break;
			}
		}
		return newCells;
	}
	
	protected boolean canTransfer(Cell[] newCells) {
		for (var cell : newCells) {
			if (cell.r < 0 || cell.r >= matrix.getSize().height ||
					cell.c < 0 || cell.c >= matrix.getSize().width)
				return false;
			if ( !matrix.isEmpty(cell) && !Arrays.asList(cells).contains(cell))
				return false;
		}
		return true;
	}
	
	protected void transferTo(Cell[] newCells) {
		var bricks = matrix.removeAll(cells);
		matrix.addAll(newCells, bricks);
		cells = newCells;;
	}
	
	public boolean move(Direction dir) {
		Cell[] newCells = calcFor(dir, 1);
		if (canTransfer(newCells)) {
			transferTo(newCells);
			return true;
		}
		return false;
	}

	@Override
	public String toString() {
		return Arrays.toString(cells);
	}
}