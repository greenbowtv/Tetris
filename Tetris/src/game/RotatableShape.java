package game;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** Поворачиваемая фигура
 * есть простые формулы расчет координат (индексов)
 * если ширина или высота имеют четную длину, то координата будет дробной
 * а координаты ячеек только целые, поэтому на углах 90 и 270 будет ошибка
 * в какую сторону округлять? центр рассчитан или точка поворота явно задана?
 * короче, результат поворота достоверный с точки зрения геометрии может
 * быть неудобным в игре, поэтому можно задать корректировку для каждого угла

 * @author greenbowtv
 *
 */
public class RotatableShape<Br>
	 extends Shape<Br> {
	
	public static void main(String[] args) {
		var matrix = new CollectionMatrix<Integer>(Integer.class, 5, 4);
		matrix.addListener(e -> System.out.println(matrix));
		
		Cell[] cells = {new Cell(1, 1), new Cell(1, 2), new Cell(1, 3), new Cell(2, 1)};
		var shape = new RotatableShape<Integer>(matrix, cells, Arrays.asList(1, 2, 3, 4));
		System.out.println(shape);
		
		shape.rotate();
		shape.rotate();
		shape.rotate();
		shape.rotate();
	}
	
	public static class DoubleCell {
		private double r, c;
		
		public DoubleCell(double r, double c) {
			this.r = r;
			this.c = c;
		}

		public String toString() {
			return "("+r+", "+c+")";
		}
	}
	
	private DoubleCell center;	// правильные координаты центра
	private DoubleCell error;	// расхождение координат блока с фактическими
	private int 		angle;	// угол поворота для нужд коррекции
	private Map<Integer, DoubleCell> adjustment;

	@SuppressWarnings("serial")
	public RotatableShape(CollectionMatrix<Br> matrix, Cell[] cells, List<Br> bricks)
		throws IllegalStateException {
		
		this(matrix, cells, bricks, new HashMap<>() {{
			put(0, new DoubleCell(0, 0));	// для 90*
			put(90, new DoubleCell(0, 0));	// для 180*
			put(180, new DoubleCell(0, 0));	// для 270*
		}});
	}
	
	public RotatableShape(CollectionMatrix<Br> matrix, Cell[] cells, List<Br> bricks, 
			Map<Integer, DoubleCell> adjustment) 
		throws IllegalStateException {
		
		super(matrix, cells, bricks);
		
		int minR, maxR, minC, maxC;
		minR = maxR = cells[0].r;
		minC = maxC = cells[0].c;
		for (int i = 1; i < cells.length; i++) {
			if (cells[i].r < minR) minR = cells[i].r;
			if (cells[i].r > maxR) maxR = cells[i].r;
			if (cells[i].c < minC) minC = cells[i].c;
			if (cells[i].c > maxC) maxC = cells[i].c;
		}
		center = new DoubleCell(0.5 * (minR + maxR), 0.5 * (minC + maxC));
		error = new DoubleCell(0.0, 0.0);
		angle = 0;
		this.adjustment = adjustment;
	}
	
	protected Cell[] calcFor(int angle) {
		assert angle == 90 || angle == -90;
		
		Cell[] newCells = new Cell[cells.length];
		
		int cos = (int) Math.round( Math.cos( Math.toRadians(angle)));
		int sin = (int) Math.round( Math.sin( Math.toRadians(angle)));
		double r = 0, c = 0;
		for (int i = 0; i < cells.length; i++) {
			r = -((cells[i].c + error.c) - center.c)* sin 
				+ ((cells[i].r + error.r) - center.r)* cos + center.r;
			c = ((cells[i].c + error.c) - center.c)* cos 
				+ ((cells[i].r + error.r) - center.r)* sin + center.c;
			
			var adj = adjustment.getOrDefault(this.angle, new DoubleCell(0, 0));
			newCells[i] = new Cell((int)Math.ceil(r + adj.r), 
								(int)Math.ceil(c + + adj.c));
		}
		error = new DoubleCell(
				r - newCells[cells.length - 1].r, 
				c - newCells[cells.length - 1].c);
		this.angle = (this.angle + angle) % 360;

		return newCells;
	}
	
	public boolean rotate() {
		Cell[] newCells = calcFor(90);
		if (canTransfer(newCells)) {
			transferTo(newCells);
			return true;
		}
		return false;
	}

	@Override
	public boolean move(Direction dir) {
		boolean success = super.move(dir);
		if (success) {
			switch(dir) {
			case LEFT:
				center.c -= 1;
				break;
			case RIGHT:
				center.c += 1;
				break;
			case UP:
				center.r -= 1;
				break;
			case DOWN:
				center.r += 1;
				break;
			}
		}
		return success;
	}
	
	@Override
	public String toString() {
		return String.format("[center=%s,error=%s,angle=%d,cells=%s",
			center, error, angle, Arrays.toString(cells));
	}
}
