import java.util.Random;

class Blocks {

	//設定読み込み
	private int YOKO = SetBlock.YOKO;
	private int TATE = SetBlock.TATE;
	private int PANELTYPE = SetBlock.PANELTYPE;

	//ブロック管理データ
/*	[][][0] 種類(0～PANELTYPE 100=邪魔ブロック)
	[][][1] 状態(0=通常 1=削除待機 2=落下中 3=落下確保 4=邪魔ブロック 5=落下邪魔 6=解凍待機)
	[][][2] カウント(削除待機と落下中のみ)
	[][][3] 潜在連鎖数
	[][][4] 現在連鎖数
	[][][5] 消滅順(グラフィック用)		*/
	
	public static final int 
		KIND=0,
		STATE=1,
		COUNT=2,
		POTENTIAL=3,
		CHAIN=4,
		ORDER=5;

	//データセット
	private int[][][] blocks;
	private int header;
	private int[] NBL = new int[YOKO];
	
	//public int[] ABL = new int[YOKO];
	public int stock = 0;//ストックされているお邪魔ブロックの数
	public int d_location =0;//0～30+α
	public int d_speed = 0;
	
	public boolean death = false;
	
	public static Random random = new Random();
	
	//初期化・生成
	public Blocks(int ini_type, int option,int player) {
		blocks = new int[YOKO][TATE][6];
		header = 12;

		//if(player == 1){stock += 3;}
		//標準初期化
		if(ini_type == 1){

					int rand_ca, df, df2;
					if(option == 0) option = 6;
					
					
					for(int i=0 ; i < YOKO ; i++) {

						for(int j=0 ; j < TATE ; j++) {
							if(j < option) {blocks[i][j][0] = 0;}
							else {
								df = 0; df2 = 0;
								if(blocks[i][j-1][0] == blocks[i][j-2][0]) df = blocks[i][j-1][0];
								if(i >1 && blocks[i-1][j][0] == blocks[i-2][j][0]) df2 = blocks[i-1][j][0];
								do {rand_ca = random.nextInt(PANELTYPE)+1;}while(rand_ca == df || rand_ca == df2);
								blocks[i][j][0] = rand_ca;
							}
						}
					}
					//穴掘り
					for(int i=0 ; i < YOKO ; i++) {
						for(int j=0 ; j < TATE ; j++) {
							if(blocks[i][j][0] == 0){continue;}
							else{
								if(random.nextInt(2) == 0){blocks[i][j][0]=0;}
								else{break;}
							}
						}
					}
					
					/*
					// 1=△　2=○　3=◇　4=☆　5=ハート
					int inputBlocks[][]={

						{1,2,3,4,5,1},
						{2,3,4,5,1,2},
						{3,4,5,1,2,3},
						{0,0,0,0,0,0},
						{0,0,0,0,0,0},
						{0,0,0,0,0,0},
						{0,0,0,0,0,0},
						{0,0,0,0,0,0},
						{0,0,0,0,0,0},
						{0,0,0,0,0,0},
						{0,0,0,3,0,0},
						{0,0,0,3,0,0},
						{2,2,3,2,3,3}};

					for(int i=0;i<6;i++){
						for(int j=0;j<12;j++)blocks[i][j][0]=inputBlocks[j][i];
					}
					int inputBlocks2[][]={

							{4,4,4,4,4,4},
							{4,4,4,4,4,4},
							{4,4,4,4,4,4},
							{0,0,0,0,0,0},
							{0,0,0,0,0,0},
							{0,0,0,0,0,0},
							{0,0,0,0,0,0},
							{0,0,0,0,0,0},
							{0,0,0,0,0,0},
							{0,0,0,0,0,0},
							{0,0,0,0,0,0},
							{0,0,0,0,0,0}};

						for(int i=0;i<6;i++){
							for(int j=0;j<12;j++)blocks[i][j][1]=inputBlocks2[j][i];
						}
					*/
					

		}

	}

	//ブロック読み込み
	public int rb(int read_x, int read_y, int read_d){
			//領域外参照ははじく
			if(read_x < 0 || read_x >= YOKO || read_y < 0 || read_y >= TATE)return -1;
			
			return blocks[read_x][(read_y + header) % TATE][read_d];
	}

	//ブロック書き込み
	public void wb(int write_x,int write_y,int write_d,int write_val){
		blocks[write_x][(write_y + header) % TATE][write_d] = write_val;

	}
	//キーワード式書き込み
	public void wb(int write_x,int write_y, String key, String value){
		
	}
	//ストックされた新行を挿入する
	public boolean pushBlock(){

			int overWritePoint = header % TATE;

			header++;
			if(header == TATE * 100){header = TATE;}

			int topOfPanel = header % TATE;


			for(int i=0;i<YOKO;i++){
				blocks[i][overWritePoint][0]=NBL[i];
			}
			//死亡判定
			for(int i=0;i<YOKO;i++){if(blocks[i][topOfPanel][0] != 0)return true;}
			
			return false;
	}

	//せり上がるを新行を生成してストック
	public void create_newline(){

			int rand_ca, df, df2;
			
			int bottom1 = (header -1) % TATE;
			int bottom2 = (header -2) % TATE;

			for(int i=0;i<YOKO;i++){

				df=0;df2=0;
				if(blocks[i][bottom2][0] == blocks[i][bottom1][0]) df=blocks[i][bottom1][0];
				if(i > 1 && NBL[i-1] == NBL[i-2]) df2=NBL[i-1];

				do {rand_ca = random.nextInt(PANELTYPE)+1;}while(rand_ca == df || rand_ca == df2);
				NBL[i] = rand_ca;

			}
			
	
	}
	//ストックのゲットメソッド
	public int readNBL(int num){
		
		return NBL[num];
	}
	//完全な一行を生成して渡すメソッド
	public int[] create_line(){

		int NBL2[] =new int[YOKO];
		int rand_ca, df;

		for(int i=0;i<YOKO;i++){

			df=0;
			if(i > 1 && NBL2[i-1] == NBL2[i-2]) df=NBL2[i-1];

			do {rand_ca = random.nextInt(PANELTYPE)+1;}while(rand_ca == df);
			NBL2[i] = rand_ca;

		}
		return NBL2;
	}	
		
}
	
