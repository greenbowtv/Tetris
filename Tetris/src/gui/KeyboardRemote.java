package gui;
import java.awt.Component;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import javax.swing.Timer;

import game.Algorithm;

// клавиатурное управление Таймером (пауза) и Алгоритмом (перемещение фигуры)
public class KeyboardRemote {
	
	@SuppressWarnings("serial")
	private Map<Integer, Consumer<Algorithm>> algorithmOperations = new HashMap<>() {{
			put(KeyEvent.VK_W, Algorithm::moveUp);
			put(KeyEvent.VK_A, Algorithm::moveLeft);
			put(KeyEvent.VK_S, Algorithm::moveDown);
			put(KeyEvent.VK_D, Algorithm::moveRight);
			put(KeyEvent.VK_SPACE, Algorithm::optional);
	}};
	private int timerOperationCode = KeyEvent.VK_CONTROL;
	
	public KeyboardRemote(Algorithm algorithm, Timer timer, Component source) {
		source.setFocusable(true);
		source.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == timerOperationCode) {
					if (timer.isRunning()) {
						timer.stop();
						algorithm.setPaused(true);
					}
					else {
						algorithm.setPaused(false);
						timer.start();
					}	
				}
				else {
					var op = algorithmOperations.get(e.getKeyCode());
					if (op != null)
						op.accept(algorithm);
				}
			}
		});
	}
	
	public static KeyboardRemote tetris(Algorithm algorithm, Timer timer, Component source) {
		var remote = new KeyboardRemote(algorithm, timer, source);
		remote.algorithmOperations.clear();
		remote.algorithmOperations.put(KeyEvent.VK_LEFT, Algorithm::moveLeft);
		remote.algorithmOperations.put(KeyEvent.VK_RIGHT, Algorithm::moveRight);
		remote.algorithmOperations.put(KeyEvent.VK_UP, Algorithm::optional);
		remote.algorithmOperations.put(KeyEvent.VK_DOWN, Algorithm::moveDown);
		remote.timerOperationCode = KeyEvent.VK_SPACE;
		return remote;
	}
}
