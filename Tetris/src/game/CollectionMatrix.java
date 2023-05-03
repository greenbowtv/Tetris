package game;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

// Матрица как коллекция (сделать немного похожей на JCF класс)
// null признак пустой ячейки, элементы не должны быть null
// полагаем, что set() выполняется всегда успешно
// add() помещает элемент в пустую ячейку, иначе false
// remove() удаляет элемент из непустой ячейки, иначе false
// count() считает непустые ячейки, вместе с isEmpty/isFull заменяют contains/containstAll
// iterator() для обхода занятых ячеек, но возвращает индексы
public class CollectionMatrix<E> 
	 extends ObservableMatrix<E> 
  implements Iterable<Cell> {
	
	
	public static void main(String[] args) {

		final int W = 5;
		final int H = 3;
		
		var matrix = new CollectionMatrix<Integer>(Integer.class, H, W);
		matrix.addListener(e -> System.out.println(matrix));
		Range range = matrix.getRange();
		
		matrix.addAll(range.row(0), Collections.nCopies(W, 0));
		matrix.setAll(range.row(0), Collections.nCopies(W, 1));
		matrix.removeAll(range.row(0));
		
		
	/*		
		System.out.println("Добавление строки");
		System.out.println( matrix.addAll(range.row(0), Collections.nCopies(W, 10)));
		System.out.println("Добавление элемента");
		System.out.println( matrix.add(0, 0, 1000));
		System.out.println("Удаление элемента");
		System.out.println( matrix.remove(0, 0));
		System.out.println("Удаление элемента");
		System.out.println( matrix.remove(0, 0));
		System.out.println("Удаление строки");
		System.out.println( matrix.removeAll(range.row(0)));
		matrix.setSendingEnabled(false);
		matrix.add(0, 0, 0);
		matrix.add(1, 1, 1);
		matrix.add(2, 2, 2);
		matrix.add(2, 3, 3);
		matrix.add(2, 4, 4);
		matrix.setSendingEnabled(true);
		for (var c : matrix)
			System.out.println(matrix.get(c));
			
	*/
		
	}
	
	private int modificationCount;	// счетчик модификаций
	private Range range;				// создает диапазоны ячеек, например для clear()
	
	public CollectionMatrix(Class<E> elemType, int rows, int columns) {
		super(elemType, rows, columns);
		
		modificationCount = 0;
		range = new Range(rows, columns);
	}

	public Range getRange() {
		return range;
	}
	
	@Override
	public E set(Cell cell, E element) {
		modificationCount++;
		return super.set(cell, element);
	}
	
	public void setAll(Cell[] cells, List<E> elems) {
		setSendingEnabled(false);
		for (int i = 0; i < cells.length; i++)
			set(cells[i], elems.get(i));
		setSendingEnabled(true);
	}
	
	public List<E> getAll(Cell[] cells) {
		var elems = new ArrayList<E>(cells.length);
		for (var c : cells)
			elems.add(get(c));
		return elems;
	}
	
	public boolean add(int r, int c, E elem) {
		return add(new Cell(r, c), elem);
	}
	
	public boolean add(Cell cell, E elem) {
		if (isEmpty(cell)) {
			set(cell, elem);
			return true;
		}
		return false;
	}
	
	public int addAll(Cell[] cells, List<E> elems) {
		setSendingEnabled(false);
		int count = 0;
		for (int i = 0; i < cells.length; i++)
			if (add(cells[i], elems.get(i)))
				count ++;
		setSendingEnabled(true);
		return count;	
	}
	
	public E remove(int r, int c) {
		return remove(new Cell(r, c));
	}
	
	public E remove(Cell cell) {
		E last = get(cell);
		set(cell, null);
		return last;
	}
	
	public List<E> removeAll(Cell[] cells) {
		setSendingEnabled(false);
		var elems = new ArrayList<E>(cells.length);
		for (var cell : cells)
			elems.add(remove(cell));
		setSendingEnabled(true);
		return elems;
	}
	
	public void clear() {
		removeAll(range.all());
	}
	
	public int countAll() {
		return countAll(range.all());
	}
	
	public int countAll(Cell[] cells) {
		int count = 0;
		for (var cell : cells)
			if ( !isEmpty(cell))
				count++;
		return count;
	}
	
	public boolean isEmpty() {
		return countAll(range.all()) == 0;
	}
	
	public boolean isEmpty(int r, int c) {
		return get(r, c) == null;
	}
	
	public boolean isEmpty(Cell cell) {
		return get(cell) == null;
	}
	
	public boolean isEmpty(Cell[] range) {
		return countAll(range) == 0;
	}
	
	public boolean isFull(Cell[] range) {
		return countAll(range) == range.length;		
	}
	
	public Iterator<Cell> iterator() {
		return new Iterator<Cell>() {
			
			int initModificationCount = modificationCount;
			int index = 0;						// сквозной индекс по ячейкам
			int maxIndex = getSize().width * getSize().height;
			int count = 0;						// столько ненулевых элементов обошли
			int maxCount = countAll(range.all());

			@Override
			public boolean hasNext() {
				return count != maxCount;
			}

			@Override
			public Cell next() {
				if (initModificationCount != modificationCount)
					throw new ConcurrentModificationException();					
				for ( ; index < maxIndex; ) {
					Cell cell = new Cell(index / getSize().width, index++ % getSize().width);
					if ( !isEmpty(cell)) {
						count++;
						return cell;
					}	
				}		
				throw new NoSuchElementException();
			}
		};
	}
}