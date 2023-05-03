package game;

import java.util.Arrays;
import java.util.Collections;
import java.util.EventObject;
import java.util.List;

//событие изменения элементов 2D массива
//несет индексы измененных ячеек (оптимизация)
//безопасно расшаривать событие
@SuppressWarnings("serial")
public class MatrixChangeEvent extends EventObject {

	public MatrixChangeEvent(Object source, Cell... cells) {
		super(source);
		change = Collections.unmodifiableList(Arrays.asList(cells));
	}
	
	public MatrixChangeEvent(Object source, List<Cell> cells) {
		super(source);
		change = Collections.unmodifiableList(cells);
	}
	
	private List<Cell> change;

	public List<Cell> getChange() {
		return change;
	}
}
