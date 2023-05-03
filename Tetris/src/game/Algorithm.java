package game;

public interface Algorithm {
	void onTimerTick() throws GameOverException;
	
	void moveLeft();
	void moveRight();
	void moveUp();
	void moveDown();
	void optional();
	
	void setPaused(boolean flag);
}
