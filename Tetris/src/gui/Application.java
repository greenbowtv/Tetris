package gui;
import javax.swing.SwingUtilities;

public class Application {
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			new MainFrame();
		});
	}
}
