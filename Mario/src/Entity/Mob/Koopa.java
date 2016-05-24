package Entity.Mob;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

import Entity.Entity;
import Mario.Handler;
import Mario.Id;
import States.KoopaState;
import Tile.Tile;

public class Koopa extends Entity {
	
	private Random random;

	private int shellCount;
	
	public Koopa(int x, int y, int width, int height, Id id, Handler handler) {
		super(x, y, width, height, id, handler);
		
		random = new Random();
		
        int dir = random.nextInt(2);
		
		switch(dir){
		case 0:
			setVelX(-2);
			facing =0;
			break;
		case 1:
			setVelX(2);
			facing =1;
			break;	
		}

		koopaState = KoopaState.WALKING;
	}

	public void render(Graphics g) {
		if(koopaState==KoopaState.WALKING) g.setColor(Color.green);
		else g.setColor(Color.PINK);
		g.fillRect(getX(),getY(),width,height);
	}


	public void tick() {

		x+=velX;
		y+=velY;
		
		if(koopaState==KoopaState.SHELL){
			setVelX(0);
			
			shellCount++;
			
			if(shellCount>=300){
				shellCount = 0;
				
				koopaState = KoopaState.WALKING;
			}
		}
		if(koopaState==KoopaState.WALKING||koopaState==KoopaState.SPINNING){
			shellCount=0;
			
			if(velX==0){

			    int dir = random.nextInt(2);
				
				switch(dir){
				case 0:
					setVelX(-2);
					break;
				case 1:
					setVelX(2);
					break;
				}
			}
		}
		
		for(Tile t:handler.tile){
			if(!t.solid) break;
			if(t.getId()==Id.wall){
				if(getBoundsBottom().intersects(t.getBounds())){
					setVelY(0);
					if(falling) falling  = false;
				}else {
					if(!falling){
						gravity = 0.8;
						falling = true;
					}
				}
				if(getBoundsLeft().intersects(t.getBounds())){
					if(koopaState==KoopaState.SPINNING) setVelX(10);
					else setVelX(2);
				}
				if(getBoundsRight().intersects(t.getBounds())){
					if(koopaState==KoopaState.SPINNING) setVelX(-10);
					else setVelX(-2);
				
				}
			}
		}
		if(falling){
			 gravity+=0.1;
			 setVelY((int)gravity);
		 }
		 
	
		
	}

}
