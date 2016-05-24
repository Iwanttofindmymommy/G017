package GFX.GUI;

import java.awt.Color;
import java.awt.Graphics;

import Mario.Game;

public class Launcher {
	
	public Button[] buttons;
	
	public Launcher(){
		buttons = new Button[2];
		
		buttons[0] = new Button(500,250,300,100,"Start Game");
		buttons[1] = new Button(600,350,300,100,"Exit Game");
	}

	public void render(Graphics g){
		g.setColor(Color.MAGENTA);
		g.fillRect(0, 0, Game.getFrameWidth(), Game.getFrameHeight());
		
		for(int i=0;i<buttons.length;i++){
			buttons[i].render(g);
		}
	}
	
}
