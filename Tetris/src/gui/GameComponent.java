package gui;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.Timer;

import game.GameOverException;
import game.tetris.TetrisAlgorithm;
import game.tetris.TetrisMatrix;
import game.tetris.Tetromino;

// компонент-законченная игра
// (создать, добавить в контейнер, yep!)
// рисовальщик визуализирует Матрицу, ее содержимое меняет Алгоритим, он
// им управляется Таймером и пользователем с клавиатуры
@SuppressWarnings("serial")
public class GameComponent extends JComponent {
	
	private TetrisMatrix well;
	private MatrixPainter<Tetromino> pntWell;
	private Timer timer;
	
	public GameComponent() {
		well = new TetrisMatrix();
		well.addListener(e -> repaint());
		var algorithm = new TetrisAlgorithm(well);
		timer = new Timer(1000, event -> {
			try {
				algorithm.onTimerTick();
			} catch (GameOverException e) {
				timer.stop();
				JOptionPane.showMessageDialog(null, e.getMessage());
				System.exit(0);
			}
		});
		timer.start();
		KeyboardRemote.tetris(algorithm, timer, this);
		pntWell = new MatrixPainter<Tetromino>(ColorProfile.tetris());
		
		setName("Тетрис");
		setPreferredSize(new Dimension(
				well.getSize().width * 30, well.getSize().height * 30));
	}
	
	@Override
	public void paint(Graphics g) {
		pntWell.paint(g, getSize(), well);
	}
}
