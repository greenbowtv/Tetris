package game;
import java.awt.Dimension;
import java.util.Arrays;

// фабрика диапазонов для методов xxxAll
public class Range {
	
	public static void main(String[] args) {
		var range = new Range(5, 3);
		System.out.println("0 строка");
		System.out.println(Arrays.toString(range.row(0)));
		System.out.println("строки [1, 3)");
		System.out.println(Arrays.toString(range.rows(1, 3)));
		System.out.println("строки [1, 2)");
		System.out.println(Arrays.toString(range.rows(1, 2)));
		System.out.println("строки [1, 1)");
		System.out.println(Arrays.toString(range.rows(1, 1)));
		System.out.println("строки [3, 1)");
		System.out.println(Arrays.toString(range.rows(3, 1)));
		System.out.println("всё");
		System.out.println(Arrays.toString(range.all()));
	} 
	
	private final int rows;
	private final int columns;
	
	public Range(int rows, int columns) {
		this. rows =  rows;
		this.columns = columns;		
	}
	
	public Range(Dimension matrixSize) {
		this.rows = matrixSize.height;
		this.columns = matrixSize.width;
	}
	
	public Cell[] all() {
		return rows(0, rows);
	}
	
	public Cell[] row(int r) {
		var range = new Cell[columns];
		for (int c = 0; c < columns; c++)
			range[c] = new Cell(r, c);
		return range;
	}
	
	// диапазон как всегда [val1, val2)
	// массив будет упорядочен по возрастанию элементов
	public Cell[] rows(int r1, int r2) {
		var range = new Cell[Math.abs(r1 - r2) * columns];
		int minR = Math.min(r1, r2);
		int maxR = Math.max(r1, r2);
		for (int r = minR; r < maxR; r++)
			for (int c = 0; c < columns; c++)
				range[(r - minR) * columns + c] = new Cell(r, c);
		return range;
	}
	
	public Cell[] column(int x) {
		throw new UnsupportedOperationException();
	}
	
	public Cell[] area(int x, int y, int w, int h) {
		throw new UnsupportedOperationException();
	}
}
