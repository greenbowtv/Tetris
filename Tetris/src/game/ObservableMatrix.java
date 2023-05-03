package game;
import java.awt.Dimension;
import java.lang.reflect.Array;
import java.util.ArrayList;

import utility.event.Observable;

// двумерный массив, изменения отслеживаются
//базис массива, где перегрузка операций, Java?!
// оптимизация доставки событий
//лучше если E неизменяемый тип
public class ObservableMatrix<E> 
	 extends Observable<MatrixChangeEvent> {
	
	public static void main(String[] args) {

		var matrix = new ObservableMatrix<Integer>(Integer.class, 5, 3);
		matrix.addListener(e -> {
			System.out.println(e.getChange());
			System.out.println(matrix);
		});
		matrix.setSendingEnabled(false);
		matrix.set(0, 0, -1);
		matrix.set(1, 1, 10);
		matrix.set(2, 2, 20);
		matrix.setSendingEnabled(true);
		matrix.set(new Cell(0, 0), 0);
		matrix.set(new Cell(1, 1), 1);
		matrix.set(new Cell(2, 2), 2);
		
	}
	
	private E[][] primary;
	private E[][] backup;

	@SuppressWarnings("unchecked")
	public ObservableMatrix(Class<E> elemType, int rows, int columns) {
		if (rows < 1 || columns < 1 || elemType.isPrimitive())
			throw new IllegalArgumentException();
		primary = (E[][])Array.newInstance(elemType, new int[] {rows, columns});
		backup = (E[][])Array.newInstance(elemType, new int[] {rows, columns});
	}
	
	public Dimension getSize() {
		return new Dimension(primary[0].length, primary.length);
	}
		
	public E get(int r, int c) {
		return primary[r][c];
	}
	
	public E get(Cell cell) {
		return primary[cell.r][cell.c];
	}
	
	public E set(int r, int c, E element) {
		return set( new Cell(r, c), element);
	}
	
	// первичная модифицирующая операция
	public E set(Cell cell, E element) {
		E last = primary[cell.r][cell.c];
		if (last != element) {
			primary[cell.r][cell.c] = element;
			if (isNotifyEnabled()) {
				backup[cell.r][cell.c] = element;
				fireAccept(new MatrixChangeEvent(this, cell));
			}		
		}

		return last;
	}

	@Override
	public void setSendingEnabled(boolean enabled) {
		if ( !isNotifyEnabled() && enabled) {
			var changed = new ArrayList<Cell>();
			for (int i= 0; i < primary.length; i++)
				for (int j= 0; j< primary[0].length; j++)
					if (primary[i][j] != backup[i][j])
						changed.add(new Cell(i, j));
			super.setSendingEnabled(true);
			fireAccept(new MatrixChangeEvent(this, changed));
		}
			
		super.setSendingEnabled(enabled);	
	}
	
	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		for (var row : primary) {
			for (var el : row) 
				str.append(String.format("%5s", String.valueOf(el)));
			str.append("\n");
		}
//		str.deleteCharAt(str.length() - 1);
		return str.toString();
	}
}