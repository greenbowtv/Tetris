package game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Objects;

// индексы ячейки 2D массива
// раз нельзя проверить верхние границы, нет проверки и на 0
public class Cell implements Comparable<Cell>, Cloneable {
	
	public static void main(String[] args) {
		
		System.out.println("\"abc\".compareTo(\"abc\")");
		System.out.println("abc".compareTo("abc"));
		System.out.println("\"abc\".compareTo(\"abb\")");
		System.out.println("abc".compareTo("abb"));
		System.out.println("\"abc\".compareTo(\"abd\")");
		System.out.println("abc".compareTo("abd"));
		System.out.println("\"abc\".compareTo(\"abcd\")");
		System.out.println("abc".compareTo("abcd"));
		
		Cell[] cells = {new Cell(1, 1), new Cell(1, 1), new Cell(0, 1), 
			new Cell(2, 1), new Cell(1, 0), new Cell(1, 2)};
		for (int i = 1; i < cells.length; i++) {
			System.out.printf("%s.compareTo(%s) = ", cells[0].toString(), cells[i].toString());
			System.out.println(cells[0].compareTo(cells[i]));
		}
		
		var coll = new ArrayList<Cell>(Arrays.asList(cells));
		Collections.shuffle(coll);
		System.out.println("случайный порядок");
		System.out.println(coll);
		System.out.println("сортированный порядок");
		Collections.sort(coll);
		System.out.println(coll);
	}

	public final int r;
	public final int c;
	
	public Cell() {
		r = c = 0;
	}
	
	public Cell(int r, int c) {
		this.r = r;
		this.c = c;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (obj instanceof Cell) {
			var other = (Cell)obj;
			return r == other.r && c == other.c;
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(r, c);
	}
	
	@Override
	public String toString() {
		return "("+r+", "+c+")";
	}

	public Integer getRow() {return r;}
	public Integer getColumn() {return c;} 
	
	@Override
	public int compareTo(Cell o) {
		return Comparator.comparing(Cell::getRow)
				.thenComparing(Cell::getColumn).compare(this, o);
	}
	
	@Override
	protected Cell clone() {
		try {
			return (Cell)super.clone();	
		
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException(e);
		}
	}
}
