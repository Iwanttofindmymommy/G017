package Tile;

import java.awt.Color;
import java.awt.Graphics;

import Mario.Handler;
import Mario.Id;

public class Pipe extends Tile {

	public Pipe(int x, int y, int width, int height, boolean solid, Id id, Handler handler,int facing) {
		super(x, y, width, height, solid, id, handler);
		this.facing = facing;
	}

	
	public void render(Graphics g) {
		g.setColor(Color.GREEN);
		g.fillRect(x, y, width, height);
	}


	public void tick() {
		
		
	}

}
