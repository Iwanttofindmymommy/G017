package Mario;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

import Entity.Coin;
import Entity.Entity;
import Entity.Mob.Goomba;
import Entity.Mob.Koopa;
import Entity.Mob.Player;
import Entity.Mob.TowerBoss;
import Entity.Powerup.Mushroom;
import Tile.Pipe;
import Tile.PowerUpBlock;
import Tile.Tile;
import Tile.Wall;

public class Handler {

	public LinkedList<Entity> entity = new LinkedList<Entity>();
	public LinkedList<Tile> tile = new LinkedList<Tile>();
	
	public void render(Graphics g){
		for(int i=0;i<entity.size();i++){
			Entity e = entity.get(i);
			if(Game.getVisibleArea()!=null&&e.getBounds().intersects(Game.getVisibleArea())) e.render(g);
		}
		for(int i=0;i<tile.size();i++){
			Tile t = tile.get(i);
			if(Game.getVisibleArea()!=null&&t.getBounds().intersects(Game.getVisibleArea())) t.render(g);
		}
	}
	public void tick(){
		for(int i=0;i<entity.size();i++){
			Entity e = entity.get(i);
			e.tick();
		}
		for(int i=0;i<tile.size();i++){
			Tile t = tile.get(i);
			if(Game.getVisibleArea()!=null&&t.getBounds().intersects(Game.getVisibleArea())) t.tick();
		}
	}
	public void addEntity(Entity en){
		entity.add(en);
	}
	public void removeEntity(Entity en){
		entity.remove(en);
	}
	public void addTile(Tile ti){
		tile.add(ti);
	}
	public void removeTile(Tile ti){
		tile.remove(ti);
	}
	public void createLevel(BufferedImage level){
		int width = level.getWidth();
		int height = level.getHeight();
		
		for(int y=0;y<height;y++){
			for(int x=0;x<width;x++){
				int pixel = level.getRGB(x, y);
				
				int red = (pixel >> 16) & 0xff;
				int green = (pixel >> 8) & 0xff;
				int blue = (pixel) & 0xff;
				
				if(red==0&&green==0&&blue==0) addTile(new Wall(x*64,y*64,64,64,true,Id.wall,this));
				if(red==0&&green==0&&blue==255) addEntity(new Player(x*64,y*64,48,48,Id.player,this));
				if(red==255&&green==119&&blue==0) addEntity(new Koopa(x*64,y*64,64,64,Id.koopa,this));
				if(red==255&&green==255&&blue==0) addTile(new PowerUpBlock(x*64,y*64,64,64,true,Id.powerUp,this,Game.mushroom,0));
				if(red==0&&(green>123&&green<129)&&blue==0) addTile(new Pipe(x*64,y*64,64,64*15,true,Id.pipe,this,128-green));
				if(red==255&&green==250&&blue==0) addEntity(new Coin(x*64,y*64,64,64,Id.coin,this));
				//if(red==255&&green==0&&blue==255) addEntity(new TowerBoss(x*64,y*64,64,64,Id.towerBoss,this,3));		
				}
		}
	}
	
	public void clearLevel(){
		entity.clear();
		tile.clear();
	}
}
