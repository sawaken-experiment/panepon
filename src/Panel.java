import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.Image;


import javax.swing.JPanel;
import javax.swing.ImageIcon;



public class Panel extends JPanel implements Runnable, KeyListener {

	private static final long serialVersionUID = 1L;
	private static final int WIDTH = 610;
	private static final int HEIGHT = 400;

	//0=メニュー画面 1=プレイ中 2=ゲームオーバー 
	public static int run_type= 0;
	public static int game_mode = 0;
	
	private Thread thread;
	private Image img_ob;

	//public static SetBlock[] Set = new SetBlock[2]; 
	private ScoreAttack game;
	//private VSCPU game2;
	
	//private DelBlock DelBlock_ob = new DelBlock();
	private Gra commonGraphic;

	
	public Panel() {
		//パネルサイズ設定
		setPreferredSize(new Dimension(WIDTH, HEIGHT));

		//キーボード入力を認識する
		setFocusable(true);
		addKeyListener(this);

		loadImage("img/tri.png");
		
		//パネル初期化
		//SetBlock_ob.SetPanel();
		if(game_mode == 0)game = new ScoreAttack();
		
		thread = new Thread(this);
		thread.start();

	}

	//ゲーム進行
	public void run() {

		//SetBlock_ob = new SetBlock(1);//SetBlockのインスタンスを初期化
		//game.Set = new SetBlock(1);
		//Set[1] = new SetBlock(2);
		
		//SetBlock_ob.SetPanel(1);
		//時刻変数
		long startTime,endTime,waitTime;
		long overTime = 0;

		
		commonGraphic = new Gra();

		while(true){
			
			startTime = System.nanoTime();

			if(game_mode == 0){
				if(run_type == 1){
	
					run_type = game.Main(run_type,1);
					run_type = game.Main(run_type,2);
					
				}else{
					//プッシュ・リスタート
					if(run_type == 2 && game.Set.startKeyIsOn == 1) {
						game.Set.startKeyIsOn = 2;
						run_type=0;
						
					}else if(run_type == 0 && game.Set.startKeyIsOn == 1){
						game.Set.startKeyIsOn = 2;
						run_type=1;
						game.Set = new SetBlock(1);//SetBlockのインスタンスを初期化
						game.Set2 = new SetBlock(2);//SetBlockのインスタンスを初期化
					
						game.ai = new AI(game.Set2);
						game.ai.start();
						
					}
	
				}
			}
			repaint();

			//メイン処理にかかった時間(ms)
			endTime = System.nanoTime();
			long spentTime = (endTime - startTime)/1000000;

			//sleep時間計算
			waitTime = 10 - spentTime - overTime;
			if(waitTime < 2)waitTime = 2;
			try {
				Thread.sleep(waitTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			overTime = (System.nanoTime() - startTime)/1000000  - 10;
		}
	}

	//画像ロード
	public void loadImage(String imgPass){
		ImageIcon img_icon = new ImageIcon(getClass().getClassLoader().getResource(imgPass));
		img_ob = img_icon.getImage(); 
	}

	//グラフィック処理中枢
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		if(run_type == 1 || run_type == 2){
			if(game_mode == 0){
				commonGraphic.draw(g,img_ob,game.Set ,game.Set2);
				game.Set.draw(g,img_ob);
				game.Set2.draw(g,img_ob);
			}
		}
		else{draw_menu(g,img_ob);}
	}

	//キーボード操作中枢
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();

		if(game_mode == 0){

			
			
			if(key == KeyEvent.VK_UP) {game.Set.cursor(1);}
			else if(key == KeyEvent.VK_DOWN) {game.Set.cursor(2);}
			else if(key == KeyEvent.VK_RIGHT) {game.Set.cursor(3);}
			else if(key == KeyEvent.VK_LEFT) {game.Set.cursor(4);}
			else if(key == KeyEvent.VK_SPACE && run_type == 1) {game.Set.cursor(5);}
			else if(key == KeyEvent.VK_Z && run_type == 1) {game.Set.cursor(6);}
			else if(key == KeyEvent.VK_ENTER) {game.Set.cursor(8);}
			
			/*if(key == KeyEvent.VK_UP) {game.Set2.cursor(1);}
			else if(key == KeyEvent.VK_DOWN) {game.Set2.cursor(2);}
			else if(key == KeyEvent.VK_RIGHT) {game.Set2.cursor(3);}
			else if(key == KeyEvent.VK_LEFT) {game.Set2.cursor(4);}
			else if(key == KeyEvent.VK_Z && run_type == 1) {game.Set2.cursor(6);}
			*/
			
			
}

	}
	public void keyTyped(KeyEvent e) {}

	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();
		
		if(game_mode == 0){
			if(key == KeyEvent.VK_SPACE) {game.Set.scrollOn = false;}
			else if(key == KeyEvent.VK_Z && run_type == 1) {game.Set.cursor(7);}
			else if(key == KeyEvent.VK_ENTER) {game.Set.cursor(9);}
			
			//if(key == KeyEvent.VK_Z && run_type == 1) {game.Set2.cursor(7);}
		}
	}
	//メニューグラフィック
	public void draw_menu(Graphics g, Image panelImg) {

			for(int i=0;i<8;i++){for(int j=0;j<5;j++){g.drawImage(panelImg,i*85-5,j*85-5,i*85+80,j*85+80,150,0,235,85,null);}}
			g.drawImage(panelImg,160,100,460,180,0,140,300,220,null);
			g.drawImage(panelImg,230,220,390,260,0,220,160,260,null);

	}

}