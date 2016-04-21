
public class DownBlock extends EditBlock {
	
	//空白を検知して落下準備をさせる
	public static void downSearch(SetBlock  set){

		for(int i=0 ; i < YOKO ; i++) {
			for(int j=TATE-1 ; 0 < j ; j--) {
				
				boolean aboveit;
				
				//検索対象の一個上がお邪魔ブロックだった場合
				if(set.bl.rb(i, j, 0) != 0){aboveit=false;}
				else if(set.bl.rb(i, j-1, 1) == 4){
					aboveit = true;
					for(int k=0 ; k < YOKO ; k++) {
						if(set.bl.rb(k, j, 0) != 0)aboveit = false;
					}
				}
				//そうでない場合
				else {
					aboveit =(set.bl.rb(i, j-1, 0) != 0 && set.bl.rb(i, j-1, 1) == 0);
				}
			
				//検索対象が空白かつ一個上のブロックが通常なら処理する
				if(aboveit){
					//落下予定ブロック郡の一つ下の空白(=検索対象)をロック
					set.bl.wb(i, j, 1,3);
					//落下予定ブロック郡(=上に乗っかっている全ブロック)のブロックIDを[落下中]に書き換え
					int maxspeed = 1000;
					boolean isBackOjyama = false;
					for(int k=j-1 ; 0 <= k ; k--) {
						if(set.bl.rb(i, k, 0) != 0 && set.bl.rb(i, k, 1) == 0){
							
							if(set.bl.rb(i, k, 4) > maxspeed){
								set.bl.wb(i, k, 4,maxspeed);
								set.bl.wb(i, k, 2,set.bl.rb(i, k+1, 2));
								
							}
							else{maxspeed = set.bl.rb(i, k, 4);}
							
							set.bl.wb(i, k, 1,2);
							
							isBackOjyama = false;
							
						}else if((k == j-1 || isBackOjyama) && set.bl.rb(i, k, 1) == 4){
							if(set.bl.rb(i, k, 4) > maxspeed){
								set.bl.wb(i, k, 4,maxspeed);
								set.bl.wb(i, k, 2,set.bl.rb(i, k+1, 2));
							}
							else{maxspeed = set.bl.rb(i, k, 4);}
							
							set.bl.wb(i, k, 1,5);
							if(k == j-1)set.bl.wb(i, k+1, 1,3);
							
							isBackOjyama = true;
						}
					
						else{break;}
					}
				}
				
				//落下途中ではないのに速度情報が残っているものは削除
				if(set.bl.rb(i, j, 1) == 0 && set.bl.rb(i,j,4) != 0){
					set.bl.wb(i, j, 4, 0);

				
				}
				//落下途中ではないのにズレ情報が残っているものは削除
				if(set.bl.rb(i, j, 1) == 0 && set.bl.rb(i,j,2) != 0){
					set.bl.wb(i, j, 2, 0);

				
				}
			}
		}
		
	}
	
	//落下カウントの加 算、31になったら一個下に実移動
	public static void downCount(SetBlock set){
		
		int blSpeed;//落下スピード格納用
		int acrt=1;//落下の加速度
		int tamtto=30;
		
		for(int i=0 ; i < YOKO ; i++) {
			for(int j=TATE-2 ; 0 <= j ; j--) {
				if(set.bl.rb(i, j, 1) == 2 || set.bl.rb(i, j, 1) == 5){
						
							
							
								
							//落下カウントが30を超えたらブロックの情報を移す	
							if(tamtto <= set.bl.rb(i, j, 2)){
								
								//スムーズな落下＆着地時にタムっとさせるために一回分カウント加算しておく
								blSpeed = set.bl.rb(i, j, 4);
								blSpeed += acrt; 
								set.bl.wb(i, j, 2,set.bl.rb(i, j, 2)+blSpeed);
								set.bl.wb(i, j, 4, blSpeed);
								
								//書き換え
								set.bl.wb(i, j+1, 0,set.bl.rb(i, j, 0));
								set.bl.wb(i, j, 0,0);
								
								if(set.bl.rb(i, j, 1) == 5)set.bl.wb(i, j+1, 1,4);
								else{set.bl.wb(i, j+1, 1,0);}
								set.bl.wb(i, j, 1,0);
								
								set.bl.wb(i, j+1, 2,set.bl.rb(i, j, 2)-tamtto);
								set.bl.wb(i, j, 2,0);
								
								set.bl.wb(i, j+1, 3,set.bl.rb(i, j, 3));
								set.bl.wb(i, j, 3,0);
								
								set.bl.wb(i, j+1, 4,set.bl.rb(i, j, 4));
								set.bl.wb(i, j, 4,0);

							}
							//それ以外ならカウント加算
							else{
								blSpeed = set.bl.rb(i, j, 4);
								blSpeed += acrt; 
								set.bl.wb(i, j, 2,set.bl.rb(i, j, 2)+blSpeed);
								set.bl.wb(i, j, 4, blSpeed);
							}

				} 
			}
		}

		//お邪魔処理
		SetDisturb.Obstructive(set, tamtto, acrt);
		
		
	}
}
