
public class AI extends Thread {

	public SetBlock Set2_ref;

	public AI(SetBlock set){
		Set2_ref = set;
	}

	int moving[] = new int[100];
	int mp;


	public void run(){

		int count=0;
		//int forward;
		while(true){

			/*
			Set2_ref.bl.rb(0, 0, 0);
			if(count % 30 == 0)Set2_ref.cursor(6);
			if(count % 30 == 10)Set2_ref.cursor(7);
				forward = SetBlock.rand.nextInt(4)+1;
				if(forward == 1)forward = SetBlock.rand.nextInt(4)+1;
			if(count % 30 == 20)Set2_xxref.cursor(forward);
			count++;
			*/

			//1上 2下 3右 4左

			if(count % 5 == 0){
				if(moving[0] == 0)search_move3();
				if(moving[mp] == 0){search_move3();mp=0;}

				if(moving[mp] == 6){
					Set2_ref.cursor(7);
					Set2_ref.cursor(6);
				} else {
					Set2_ref.cursor(moving[mp]);
				}

				mp++;
			}
			count++;

			try{
				Thread.sleep(10);
			}catch(InterruptedException e){
				e.printStackTrace();
			}


		}

	}


	public void search_move2(){

		moving = new int[SetBlock.TATE + SetBlock.YOKO + 1];
		mp = 0;
		int cursory = Set2_ref.cursorPoint_y;

		for(int i=0;i<3;i++){

			do{
			moving[i] = SetBlock.rand.nextInt(4)+1;
			}while(cursory < 8 && moving[i] == 1);
		}
		moving[3] = 6;

	}
	public void search_move3(){

		moving = new int[100];
		mp = 0;

		int cursory = Set2_ref.cursorPoint_y;
		int cursorx = Set2_ref.cursorPoint_x;

		boolean found_flag = false;
		//int founds[];
		//int fc = 0;
		int target_y=0;
		//int exbl[];

		int ccc=0;
		int ddd=0;
		int addaction;

		for(int j=SetBlock.TATE-1 ; 0<=j ; j--){



				for(int nowcol=1;nowcol<SetBlock.PANELTYPE+1;nowcol++){

					ccc=0;ddd=0;
					for(int i=0;i<SetBlock.YOKO;i++){
						if(Set2_ref.bl.rb(i, j, 1) == 0 && Set2_ref.bl.rb(i, j, 0) == nowcol){
							ccc++;
							ddd += Math.pow(2,SetBlock.YOKO-1 - i);
							//ddd += Math.pow(2, i);

						}
					}

					if(ccc > 2){
						found_flag=true;
						target_y=j;



						break;

					}
				}
				if(found_flag)break;
		}

		Set2_ref.ddd=ddd;

		if(found_flag)
		{
			addaction = 0;
			if(target_y  <= (cursory-1)){
				for(int i=0;i<(cursory-1) - target_y;i++){moving[i]=1;addaction++;}
			}
			else{
				for(int i=0;i<target_y - (cursory-1);i++){moving[i]=2;addaction++;}
			}

			for(int i=0;i<5 - cursorx;i++){moving[addaction]=3;addaction++;}

			if((ddd & 1) == 1 && (ddd & 2) == 0){
				moving[addaction]=6;addaction++;
				ddd = ddd -1 + 2;
			}
			moving[addaction]=4;addaction++;
			if((ddd & 2) == 2 && (ddd & 4) == 0){
				moving[addaction]=6;addaction++;
				ddd = ddd -2 + 4;
			}
			moving[addaction]=4;addaction++;
			if((ddd & 4) == 4 && (ddd & 8) == 0){
				moving[addaction]=6;addaction++;
				ddd = ddd -4 + 8;
			}
			moving[addaction]=4;addaction++;
			if((ddd & 8) == 8 && (ddd & 16) == 0){
				moving[addaction]=6;addaction++;
				ddd = ddd -8 + 16;
			}
			moving[addaction]=4;addaction++;
			if((ddd & 16) == 16 && (ddd & 32) == 0){
				moving[addaction]=6;addaction++;
				ddd = ddd -16 + 32;
			}


		}else{
			search_move2();
		}

	}

}
