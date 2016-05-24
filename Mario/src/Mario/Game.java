package Mario;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import Entity.Entity;
import Entity.Mob.Player;
import GFX.Sprite;
import GFX.SpriteSheet;
import GFX.GUI.Launcher;
import Input.KeyInput;
import Input.MouseInput;
import Tile.Wall;

public class Game extends Canvas implements Runnable{

	public static final int WIDTH = 320;
	public static final int HEIGHT = 180;
	public static final int SCALE = 4 ;
	public static final String TITLE = "½X¯ï£²";
	
	private Thread thread;
	private boolean running = false;
	private BufferedImage image;
	
	private static int playerX,playerY;
	
	public static int coins =0;
	public static int lives = 5;
	public static int deathScreenTime = 0;
	
	public static boolean showDeathScreen = true;
	public static boolean gameOver = false;
	public static boolean playing = false;
	
	public static Handler handler;
	public static SpriteSheet sheet;
	public static Camera cam;
	public static Launcher launcher;
	public static MouseInput mouse;
	
	public static Sprite grass;
	public static Sprite powerUp;
	public static Sprite usedPowerUp;
	
	public static Sprite mushroom;
	public static Sprite lifeMushroom;
	public static Sprite coin;
	public static Sprite player[] = new Sprite[10];
	public static Sprite[] goomba; 
	
	private void init(){
		handler = new Handler();
		sheet = new SpriteSheet("/spritesheet.png");
		cam = new Camera();
		launcher = new Launcher();
		mouse = new MouseInput();
		
		addKeyListener(new KeyInput());
		addMouseListener(mouse);
		addMouseMotionListener(mouse);
		
		grass = new Sprite(sheet,1,1);
		powerUp = new Sprite(sheet,4,1);
		usedPowerUp = new Sprite(sheet,5,1);
		
		mushroom = new Sprite(sheet,3,1);
		coin = new Sprite(sheet,6,1);
		lifeMushroom = new Sprite(sheet,7,1);
		goomba = new Sprite[8];
		
		for(int i=0;i<player.length;i++){
			player[i]=new Sprite(sheet,i+1,16);
		}
		for(int i=0;i<goomba.length;i++){
			goomba[i]=new Sprite(sheet,i+1,15);
		}
		
		try {
			image= ImageIO.read(getClass().getResource("/level.png"));
		} catch (IOException e) {
			e.printStackTrace();
		} 
	
		
		
	}
	public Game(){
		Dimension size  = new Dimension(WIDTH*SCALE,HEIGHT*SCALE);
		setPreferredSize(size);
		setMaximumSize(size);
		setMinimumSize(size);
	}
	private synchronized void start(){
		if(running == true) return;
		running = true;
		thread = new Thread(this,"Thread");
		thread.start();
	}
	private synchronized void stop(){
		if(!running) return;
		running = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
    public void run() {
    	init();
    	requestFocus();
    	long lasttime = System.nanoTime();
    	long timer = System.currentTimeMillis();
    	double delta = 0.0;
    	double ns = 1000000000/60.0;
    	int frames = 0;
    	int ticks = 0;
		while(running){
			long now = System.nanoTime();
			delta+=(now-lasttime)/ns;
			lasttime= now;
			while(delta>-1){
				tick();
				ticks++;
				delta--;
			}
			render();
			frames++;
			if(System.currentTimeMillis()-timer>1000){
				timer+=1000;
				System.out.println(frames + "Frame Per Seconds   " + ticks +"Updates Per Seconds");
				frames = 0;
				ticks = 0;
			}
		}
		stop();
	}
    public void render(){
    	BufferStrategy bs = getBufferStrategy();
    	if(bs == null){
    		createBufferStrategy(3);
    		return ;
    	}
    	Graphics g = bs.getDrawGraphics();
    	g.setColor(Color.BLACK);
    	g.fillRect(0, 0, getWidth(), getHeight());
    	if(!showDeathScreen){
    		g.drawImage(Game.coin.getBufferedImage(), 20, 20,75,75,null);
    		g.setColor(Color.WHITE);
        	g.setFont(new Font("Courier:",Font.BOLD,20));
        	g.drawString("x" + coins ,100, 95);
    	}
    	if(showDeathScreen){
    		if(!gameOver){
    			g.setColor(Color.WHITE);
            	g.setFont(new Font("Courier:",Font.BOLD,50));
            	g.drawImage(Game.player[0].getBufferedImage(), 500, 300,100,100,null);
            	g.drawString("x" + lives ,610, 400);
    		}else{
    			g.setColor(Color.WHITE);
    			g.setFont(new Font("Courier:",Font.BOLD,50));
            	g.drawString("Game Over!       ­ú­ú³á"  ,300, 400);
    		}
    		
    	}
    	
    	if(playing) g.translate(cam.getX(), cam.getY());
    	
    	if(!showDeathScreen&&playing) handler.render(g);
    	else if(!playing) launcher.render(g);
    	g.dispose();
    	bs.show();
    }
	public void tick(){
		if(playing) handler.tick();
		
		  for(int i=0;i<handler.entity.size();i++) {
			   Entity e = handler.entity.get(i);
			   if(e.getId()==Id.player) {
			    if(!e.goingDownPipe) cam.tick(e);
			   }
			  }
		if(showDeathScreen&&!gameOver&&playing) deathScreenTime++;
		if(deathScreenTime>=180){
			if(!gameOver){
				showDeathScreen = false;
				deathScreenTime = 0;
				handler.clearLevel();
				handler.createLevel(image);
			}else if(gameOver){
				showDeathScreen = false;
				deathScreenTime = 0;
				playing = false;
				gameOver = false;
			}
			
		}
	}
	public static int getFrameWidth(){
		return WIDTH*SCALE;
	}
	public static int getFrameHeight(){
		return HEIGHT*SCALE;
	}
	
	public static Rectangle getVisibleArea(){
		for(int i=0;i<handler.entity.size();i++){
			Entity e = handler.entity.get(i);
			if(e.getId()==Id.player) {
				if(!e.goingDownPipe){
					playerX=e.getX();
					playerY=e.getY();
					
					return new Rectangle(playerX-(getFrameWidth()/2-5),playerY-(getFrameHeight()/2-5),getFrameWidth()+10,getFrameHeight()+10);
				}
				
			}
		}
		return new Rectangle(playerX-(getFrameWidth()/2-5),playerY-(getFrameHeight()/2-5),getFrameWidth()+10,getFrameHeight()+10);
	}
	
	public static void main(String[] args){
		Game game = new Game();
		JFrame frame = new JFrame(TITLE);
		frame.add(game);
		frame.pack();
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		game.start();
	}
	
	
}
