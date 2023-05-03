package game;

@SuppressWarnings("serial")
// почему Exception? подобно IOExceptin, 
// действия игрока для программы независящее обстоятельство
public class GameOverException extends Exception {
	public GameOverException(String msg) {
		super(msg);
	}
	
	public GameOverException() {
		super("Game over!");
	}
}