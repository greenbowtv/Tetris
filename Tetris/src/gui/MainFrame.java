package gui;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;

@SuppressWarnings("serial")
public class MainFrame extends JFrame {
	MainFrame() {
		var comp = new GameComponent();
		add(comp);
		pack();
			
		Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((size.width - getWidth()) / 2, 0);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setTitle(comp.getName());
		setVisible(true);	
	}
}
