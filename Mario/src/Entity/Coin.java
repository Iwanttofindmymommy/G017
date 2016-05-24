package Entity;

import java.awt.Graphics;

import Mario.Game;
import Mario.Handler;
import Mario.Id;
import Tile.Tile;

public class Coin extends Entity{


	
	public Coin(int x, int y, int width, int height, Id id, Handler handler) {
		super(x, y, width, height, id, handler);
		// TODO Auto-generated constructor stub
	}


	public void render(Graphics g) {
	     g.drawImage(Game.coin.getBufferedImage(),x,y,width,height,null);
		
	}

	
	public void tick() {
		
		
	}

}
