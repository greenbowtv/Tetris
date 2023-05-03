package gui;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Map;

import game.CollectionMatrix;

// тривиальный многоцветный рисовальщик фигур
public class MatrixPainter<Br> {
	
	private Map<Br, Color> colors;
	
	public MatrixPainter(Map<Br, Color> colors) {
		this.colors = colors;
	}
	
	public void paint(Graphics g, Dimension size, CollectionMatrix<Br> well) {
		Graphics2D g2d = (Graphics2D)g.create();
		
		int w = well.getSize().width;
		int h = well.getSize().height;
		Dimension brick = new Dimension(
			size.width / w,
			size.height / h);
		for (var cell : well)
			if ( !well.isEmpty(cell.r, cell.c)) {
				g2d.setColor( colors.get(well.get(cell.r, cell.c)));
				g2d.fillRect(0 + cell.c * brick.width, 0 + cell.r * brick.height, 
						brick.width, brick.height);
			}
					
		g2d.dispose();
	}
}
