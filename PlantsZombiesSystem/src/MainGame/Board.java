package MainGame;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import Character.Items;
import ControlCenter.MouseControl;
import Graphics.ImgAssetAndCrop;
public class Board implements Runnable {
	private BackgroundDisplay backgroundDisplay ;
	private Thread thread ;
	private Boolean running = false ;
	private BufferStrategy bs ; // Hidden Screen  
	private Graphics g ; // pen
	private Level1 level1 ; 
	private MouseControl mouse ;
	private Items items ;
	private HighScore_SunPrice highScore_SunPrice;

	public void Init() {
		backgroundDisplay = new BackgroundDisplay();
		ImgAssetAndCrop.RunImages();
		items = new Items() ;
		mouse = new MouseControl(); 
		backgroundDisplay.getJFrame().addMouseListener(mouse);
		level1 = new Level1();
		highScore_SunPrice = new HighScore_SunPrice();
}
	public void Update() 
	{
		
		level1.update();
	}
	
	public void Render() {
		bs = backgroundDisplay.getCanvas().getBufferStrategy();
		if (bs == null) {
			backgroundDisplay.getCanvas().createBufferStrategy(5); // Create 3 hidden screen
			return ;
		}
		 g = bs.getDrawGraphics();
		 //Clear Screen 
		 g.clearRect(0, 0, backgroundDisplay.getWidth(), backgroundDisplay.getHeight());
		 //Draw
		 
		 g.drawImage(ImgAssetAndCrop.getBackground(),0,0,backgroundDisplay.getWidth(),backgroundDisplay.getHeight(),null);
		 
		 
		 items.render(g);
		 highScore_SunPrice.render(g);
		 level1.render(g);

		 bs.show();
		 
		 g.dispose();
		 
	}
	public void run() {
		Init();
		int fps = 20 ; 
		double timePerFrame = 1000000000 / fps;
		double delta = 0 ; 
		long now ;
		long last = System.nanoTime();
		long timer = 0 ;
		while(running) {
			now   = System.nanoTime();
			delta =  delta + (now - last) /timePerFrame;
			timer = timer + ( now - last ) ; 
			last  = now  ;
			if(delta >= 1) {
				Update();
				Render();
				delta--;
			}
			if(timer >= 1000000000) { 
				timer = 0 ; 
			}
		}
		stop();
	}
	public synchronized void start() {
		if(running) {
			return ;}
		running = true ;
		thread = new Thread(this);
		thread.start();
		
	}
	public synchronized void stop() {
		if(!running)
			return ; 
		running  = false ;
		 try {
			thread.join();
		} catch (InterruptedException e) {	
			e.printStackTrace();
		}
	}
}