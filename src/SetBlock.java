import java.util.Random;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Color;

public class SetBlock {

		//乱数取得クラスのインスタンスを作成
		public static Random rand = new Random();
		public Blocks bl;


		//不動設定
		public static final int YOKO = 6;
		public static final int TATE = 12;
		public static final int PANELSIZE = 30;
		public static final int PANELORIGIN_x = 20;
		public static final int PANELORIGIN_y = 30;
		public static final int PANELTYPE = 5;
		public static final int DELTIME = 80;
		public static final int MELTTIME = 110;

		//メッセージ用変数の宣言
		public  String[] ms_str;
		public  int[][] ms_con;
		public  int ms_point;

		//ゲーム進行変数

			//Player固有
			public int cursorPoint_x;
			public int cursorPoint_y;
			private int cur_g_count=0;

			//キー入力フラグ
			public boolean scrollOn;
			public int changeOn;
			public int startKeyIsOn;

			public int each_score[];
			public int total_score;
			public int scSpeed;//初期スピード(小さいほど速い)

			public int player;


			//ゲーム進行フラグ類(プレイヤー別)

				//ブロックが頂点に達したときの死亡フラグ
				public boolean dead_stop;
				//ブロックが頂上に達している時間
				public int dead_count;

				//ブロック消滅中はスクロールがストップする機能を実装するため
				//消滅中ブロックの数をカウントする
				public int del_count;

				//プレイヤーが生きているか否か
				public boolean game_running;


			//ゲーム固有
			public int counter;
			public int loop_count;
			public int global_time=0;

			//テスト用
			public int ddd=100;

			//対戦用
			public int damage;
			public int attack;

		//処理用変数の宣言
		//public int[] NBL;




	//ゲームの初期化
	public  SetBlock(int plmode) {

		//ブロック生成
		bl = new Blocks(1,0,plmode);

		//ゲーム設定の初期化
		game_running = true;

		counter=0;
		cursorPoint_x = 2;
		cursorPoint_y = 8;
		scrollOn = false;
		changeOn = 0;
		startKeyIsOn = 0;
		loop_count =0;
		scSpeed = 20;
		player = plmode;
		dead_count=0;
		dead_stop = false;
		del_count=0;

		//スコアを初期化
		total_score=0;
		each_score = new int[4];

		//ブロックと連鎖情報の初期化
		//blocks = new  int[YOKO][TATE][6];

		//メッセージの初期化
		ms_str = new String[10];
		ms_con = new int[10][3];//(X座標,Y座標,表示時間時間)
		ms_point = 0;

		//対戦用
		damage =0;
		attack =0;


		//新ラインも生成しておく
		bl.create_newline();

	}


	//グラフィックメソッド
	public void draw(Graphics g , Image panelImg) {
		//処理変数
		//int re_num;
		int st_x, st_y, en_x, en_y, img_st_x, img_en_x, img_st_y, img_en_y, drawPanelType, thisID=0, downEffect;
		int now_time,now_limit;
		boolean meltflag;
		//色の設定
		Color bgcol = new Color(0,180,255);
		//Color gocol = Color.red;
		Color msbgcol = Color.black;
		Color mscol = Color.white;

		//パネル表示
		int slide = 0;
		if(player == 2){slide = 380;}
		g.setColor(bgcol);
		g.fillRect(PANELORIGIN_x + slide,PANELORIGIN_y,PANELSIZE*YOKO,PANELSIZE*TATE);

		for(int i=0 ; i < YOKO ; i++) {
		    for(int j=0 ; j < TATE ; j++) {
			if(bl.rb(i,j,0) != 0 && bl.rb(i,j,1) != 3){

				//ずらし値初期化(落下中ブロック用)
				downEffect = 0;
				meltflag = false;
				//削除待機ブロックの描写設定
				if(bl.rb(i,j,1) == 1){

					thisID = bl.rb(i,j,0);
					now_limit = bl.rb(i,j,5);
					now_time = bl.rb(i,j,2);

					//描写しないブロックの選定
					if(now_limit > 5 && now_time > DELTIME-50)continue;
					if(now_limit > 4 && now_time > DELTIME-40)continue;
					if(now_limit > 3 && now_time > DELTIME-30)continue;
					if(now_limit > 2 && now_time > DELTIME-20)continue;
					if(now_limit > 1 && now_time > DELTIME-10)continue;
				}
				//解凍待機ブロックの描写設定
				else if(bl.rb(i,j,1) == 6){

					now_limit = bl.rb(i,j,5);
					now_time = bl.rb(i,j,2);
					meltflag=true;

					if(now_limit > 5 && now_time > MELTTIME-80){thisID = bl.rb(i,j,0);meltflag=false;}
					if(now_limit > 4 && now_time > MELTTIME-70){thisID = bl.rb(i,j,0);meltflag=false;}
					if(now_limit > 3 && now_time > MELTTIME-60){thisID = bl.rb(i,j,0);meltflag=false;}
					if(now_limit > 2 && now_time > MELTTIME-50){thisID = bl.rb(i,j,0);meltflag=false;}
					if(now_limit > 1 && now_time > MELTTIME-40){thisID = bl.rb(i,j,0);meltflag=false;}
					if(now_limit > 0 && now_time > MELTTIME-30){thisID = bl.rb(i,j,0);meltflag=false;}

					if(meltflag){
						if(i == 0)thisID = 1;
						else if(i == 5)thisID = 3;
						else{thisID = 2;}
					}

				}
				//落下中ブロックの描写設定
				else if(bl.rb(i,j,1) == 2){
					thisID = bl.rb(i,j,0);
					downEffect = bl.rb(i,j,2);
				}
				//お邪魔ブロックの描写設定
				else if(game_running && bl.rb(i,j,1) == 4){
					if(i == 0)thisID = 1;
					else if(i == 5)thisID = 3;
					else{thisID = 2;}

				}
				//落下中のお邪魔ブロックの描写設定
				else if(game_running && bl.rb(i,j,1) == 5){
					if(i == 0)thisID = 1;
					else if(i == 5)thisID = 3;
					else{thisID = 2;}
					downEffect = bl.rb(i,j,2);
				}
				//その他のブロックの描写設定
				else{thisID = bl.rb(i,j,0);}


				//設定が終わったので描写する
				st_x = i*PANELSIZE + PANELORIGIN_x + slide;
				en_x = i*PANELSIZE + PANELSIZE + PANELORIGIN_x + slide;

				st_y = j*PANELSIZE - counter + downEffect + PANELORIGIN_y;
				en_y = j*PANELSIZE + PANELSIZE - counter + downEffect + PANELORIGIN_y;

				img_st_x = (thisID-1)*PANELSIZE;
				img_en_x = (thisID-1)*PANELSIZE+PANELSIZE-1;

				if(!game_running){drawPanelType=1 *PANELSIZE;}
				else if(bl.rb(i,j,1) == 1){drawPanelType=2 *PANELSIZE;}
				else if(bl.rb(i,j,1) == 4 || bl.rb(i,j,1) == 5 || meltflag){drawPanelType=260;}
				else{drawPanelType=0;}

				img_st_y = drawPanelType;
				img_en_y = drawPanelType +PANELSIZE   -1;

				//パネル表示
				g.drawImage(panelImg,st_x,st_y,en_x,en_y,img_st_x,img_st_y,img_en_x,img_en_y,null);

			}
		    }
		}
		//待機ブロック表示
		if(counter != 0){
			for(int i=0 ; i < YOKO ; i++) {

				st_x = i*PANELSIZE + PANELORIGIN_x + slide;
				en_x = i*PANELSIZE + PANELSIZE + PANELORIGIN_x + slide;

				st_y = 12*PANELSIZE - counter + PANELORIGIN_y;
				en_y = 12*PANELSIZE + PANELORIGIN_y;

				img_st_x = (bl.readNBL(i) -1)*PANELSIZE;
				img_en_x = (bl.readNBL(i) -1)*PANELSIZE+PANELSIZE-1;

				g.drawImage(panelImg,st_x,st_y,en_x,en_y,
					img_st_x,PANELSIZE,img_en_x,PANELSIZE+counter,null);
			}
		}

		//カーソル表示
		st_x = (cursorPoint_x-1)*PANELSIZE - 1 + PANELORIGIN_x + slide;
		en_x = (cursorPoint_x+1)*PANELSIZE + PANELORIGIN_x + slide;
		st_y = (cursorPoint_y-1)*PANELSIZE - 1 - counter + PANELORIGIN_y;
		en_y = cursorPoint_y*PANELSIZE - counter + PANELORIGIN_y;

		if(loop_count % 20 == 0 && cur_g_count == 0){cur_g_count=1;}
		else if(loop_count % 20 == 0 && cur_g_count == 1){cur_g_count=0;}

		if(cur_g_count == 0 && game_running){g.drawImage(panelImg,st_x,st_y,en_x,en_y,60,110,120,139,null);}
		else{g.drawImage(panelImg,st_x,st_y,en_x,en_y,0,110,60,139,null);}

		//メッセージ表示
		for(int i=0 ; i<10 ;i++){
			if(ms_con[i][2] != 0){
				g.setColor(msbgcol);
				g.fillRect(ms_con[i][0] + slide,ms_con[i][1]-11,30,15);
				g.setColor(mscol);
				g.drawString(ms_str[i],ms_con[i][0] + slide,ms_con[i][1]);ms_con[i][2]--;
			}
		}

		//テスト用
		//if(player == 2)g.drawString("ddd:"+ ddd,280,320);

	}

	/** カーソル処理 **/
	public void cursor(int keytype) {

		//カーソルの移動
		if(keytype == 1 && cursorPoint_y > 2) {cursorPoint_y--;}
		else if(keytype == 2 && cursorPoint_y < 12) {cursorPoint_y++;}
		else if(keytype == 3 && cursorPoint_x < 5) {cursorPoint_x++;}
		else if(keytype == 4 && cursorPoint_x > 1) {cursorPoint_x--;}
		//最上行までブロックが存在する時のみ、一番上までカーソルを動かせる
		else if(keytype == 1 && cursorPoint_y > 1 && BlockChecker.isTopBlock(this)) {cursorPoint_y--;}

		//スクロールの早送り
		else if(keytype == 5 && game_running) { scrollOn = true; }

		//パネルの入れ替えON
		else if(keytype == 6) { if(changeOn == 0) changeOn = 1; }
		//パネルの入れ替えOFF
		else if(keytype == 7) { changeOn = 0; }

		//pushstart ON
		else if(keytype == 8) { if(startKeyIsOn == 0) startKeyIsOn = 1; }
		//pushstart OFF
		else if(keytype == 9) { startKeyIsOn = 0; }



	}

	/** ブロック入れ替え **/
	public void Change_block(){

			int panel_ca;
			//左右のパネルが異なる場合のみ入れ替え処理を行う
			if(bl.rb(cursorPoint_x-1 , cursorPoint_y-1 , 0) != bl.rb(cursorPoint_x, cursorPoint_y-1, 0)){

				//通常ブロックの場合のみ動かす
				if(bl.rb(cursorPoint_x-1 , cursorPoint_y-1 , 1) == 0 &&
						bl.rb(cursorPoint_x , cursorPoint_y-1 , 1) == 0){

					panel_ca = bl.rb(cursorPoint_x-1 , cursorPoint_y-1 , 0);
					bl.wb(cursorPoint_x-1, cursorPoint_y-1, 0, bl.rb(cursorPoint_x, cursorPoint_y-1, 0));
					bl.wb(cursorPoint_x, cursorPoint_y-1, 0, panel_ca);

				}
			}


	}
	//メッセージ管理
	public void push_message(String mess, int g_x, int g_y, int lim){
		ms_str[ms_point] = mess;
		ms_con[ms_point][0] = g_x;
		ms_con[ms_point][1] = g_y;
		ms_con[ms_point][2] = lim;
		ms_point++;if(ms_point == 10)ms_point=0;
	}
	//スコア加算
	public void push_score(int get_num,int get_chain){
		total_score = total_score+ get_num*get_chain;
		if(total_score > 999999){total_score = 999999;}
		
		if(player == 1)attack += get_num*get_chain;
		else attack += get_num*get_chain*2;

	}

}