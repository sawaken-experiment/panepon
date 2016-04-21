
public class DelCounter extends EditBlock {

	//消滅登録リストを受け取り、カウントを開始する。
	public static void StartDelCount(int [][] DBL, SetBlock set){
		for(int i=0 ; i < YOKO ; i++) {
			for(int j=0 ; j < TATE ; j++) {
				if(DBL[i][j] == 1){ set.bl.wb(i, j, 1,1);set.bl.wb(i, j, 2,0); set.del_count++;}
			}
		}
	}
	//空中にいないブロックの潜在連鎖数を破棄する
	public static void CleanChain(SetBlock set){
		
		for(int i=0 ; i < YOKO ; i++) {
			for(int j=0 ; j < TATE ; j++) {
				if(set.bl.rb(i, j, Blocks.STATE) != 2) set.bl.wb(i, j, Blocks.POTENTIAL,0);
			}
		}

	}
	//消滅カウントの加算、100になったら消滅
	public static void deleteCount(SetBlock set){
		
		for(int i=0 ; i < YOKO ; i++) {
			for(int j=0 ; j < TATE ; j++) {
				if(set.bl.rb(i, j, 1) == 1){

					if(set.bl.rb(i, j, 2) == DELTIME){
						
						//消滅ブロックの確定連鎖回数
						int convert_chain = set.bl.rb(i, j, 4);

						//上に乗っているブロックに潜在連鎖回数の書き込み
						if(j != 0){for(int k=j-1;0 <= k;k--){
							if(set.bl.rb(i, k, 0) != 0 && set.bl.rb(i, k, 1) == 0){
								set.bl.wb(i, k, 3,convert_chain + 1);
							}else{break;}
						}}
						//ブロックの実消滅
						set.bl.wb(i, j, 0,0);
						set.bl.wb(i, j, 1,0);
						set.bl.wb(i, j, 4,0);//確定連鎖数情報の破棄
						set.bl.wb(i, j, 5,0);
						set.del_count--;
					}
					else{set.bl.wb(i, j, 2,set.bl.rb(i, j, 2)+1);}
					
					if(set.bl.rb(i, j, 2) == DELTIME/2){SetDisturb.melt_disturb(i,j,set);}
				} 
			}
		}
	}
}
