
public class DelChecker extends EditBlock {

	static int [][] DBL;

	//受け取ったインスタンスの消滅を判定し、消滅するブロックの持つ潜在連鎖数から確定連鎖数を算出して書き込む
	public static void DeleteSearch(SetBlock set){
		DBL =new int[YOKO][TATE];

		//縦の消滅サーチ
		for(int i=0 ; i < YOKO ; i++) {

			int beforeB = 0;//一つ前のブロック
			int same_count = 0;//連続同ブロックの数

	  		for(int j=0 ; j < TATE ; j++) {

				//上に有効ブロックが無い時
				if(beforeB == 0){
					if(set.bl.rb(i, j, 0) == 0 || set.bl.rb(i, j, 1) != 0){//無効ブロックの時
						beforeB=0;
						same_count=0;
					} else{//有効ブロックの時
						beforeB=set.bl.rb(i, j, 0);
						same_count=1;
					}
				}else{
				//上に有効ブロックが有る時
					if(set.bl.rb(i, j, 0) == 0 || set.bl.rb(i, j, 1) != 0){//無効ブロックの時

						//消滅が成立しているなら登録
						if(same_count > 2){ DelSearchPlus(1,1,i,j,beforeB,same_count,set); }

						beforeB=0;
						same_count=0;
					} else if(set.bl.rb(i, j, 0) == beforeB){//上と同じブロックの時
						same_count++;
					} else{//上とは異なる有効ブロックの場合

						//消滅が成立しているなら登録
						if(same_count > 2){ DelSearchPlus(1,1,i,j,beforeB,same_count,set); }

						beforeB=set.bl.rb(i, j, 0);
						same_count=1;
					}
				}
			}
			//この列の終わりに、消滅が成立しているなら登録
			if(same_count > 2){ DelSearchPlus(1,2,i,0,beforeB,same_count,set); }
		}


		//横の消滅サーチ
	  	for(int j=0 ; j < TATE ; j++) {

			int beforeB = 0;//一つ前のブロック
			int same_count=0;//連続同ブロックの数

			for(int i=0 ; i < YOKO ; i++) {

				//左に有効ブロックが無い時
				if(beforeB == 0){
					if(set.bl.rb(i, j, 0) == 0 || set.bl.rb(i, j, 1) != 0){//無効ブロックの時
						beforeB=0;
						same_count=0;
					} else{//有効ブロックの時
						beforeB=set.bl.rb(i, j, 0);
						same_count=1;
					}
				}else{
				//左に有効ブロックが有る時
					if(set.bl.rb(i, j, 0) == 0 || set.bl.rb(i, j, 1) != 0){//無効ブロックの時

						//消滅が成立しているなら登録
						if(same_count > 2){ DelSearchPlus(2,1,i,j,beforeB,same_count,set); }

						beforeB=0;
						same_count=0;

					} else if(set.bl.rb(i, j, 0) == beforeB){//左と同じブロックの時
						same_count++;
					} else{//左とは異なる有効ブロックの場合

						//消滅が成立しているなら登録
						if(same_count > 2){ DelSearchPlus(2,1,i,j,beforeB,same_count,set); }

						beforeB=set.bl.rb(i, j, 0);
						same_count=1;
					}
				}
			}
			//この行の終わりに、消滅が成立しているなら登録
			if(same_count > 2){ DelSearchPlus(2,2,0,j,beforeB,same_count,set); }


		}

		//潜在連鎖情報の破棄
		DelCounter.CleanChain(set);
		//登録済みパネルをカウント開始
		DelCounter.StartDelCount(DBL, set);
	}


	//消滅登録、分離用メソッド
	public static void DelSearchPlus(int y_or_x,int type,int i ,int j,int beforeB,int same_count,SetBlock set){

		//縦の消滅成立登録
		if(y_or_x == 1){
			if(type == 2)j=TATE;//列の最終確認の場合


				set.each_score[same_count-3]++;//カウント加算
				//潜在連鎖回数(0～99)を取得
				int top_chain=0;
				for(int k=j-1;j-same_count <= k ;k--){ if(set.bl.rb(i, k, 3) > top_chain)top_chain = set.bl.rb(i, k, 3); }
				if(top_chain == 0)top_chain=1;
				//消滅予定登録及び、確定連鎖回数の書き込み
				int now_point= 0;
				for(int k=j-1;  j-same_count <= k ;k--){
					now_point++;//処理中のブロックが何番目のものであるか
					DBL[i][k]=1;//削除登録

					//四方にお邪魔ブロックがあったら解凍
					SetDisturb.melt_disturb(i,k,set);

					//既存の連鎖登録がない場合、そのまま登録
					if(set.bl.rb(i, k, 4) == 0){set.bl.wb(i, k, 4, top_chain); set.bl.wb(i, k, 5, now_point);}

					//既に連鎖登録されている場合、より大きな確定連鎖数、より遅い消滅待機時間にそれぞれ更新する
					else{
								//両者の確定連鎖数を比較し、多い方に更新
								if(top_chain > set.bl.rb(i, k, 4)){set.bl.wb(i, k, 4, top_chain);}
								//両者の消滅待機時間を比較し、遅い方に更新
								if(now_point > set.bl.rb(i, k, 4)){set.bl.wb(i, k, 5, now_point);}
					}

				}
				//連鎖数表示
				if(top_chain > 1){
					int ms_setX = i*PANELSIZE + PANELORIGIN_x;
					int ms_setY = (j-same_count)*PANELSIZE - 10 + PANELORIGIN_y;
					set.push_message(top_chain + "連鎖",ms_setX,ms_setY,DELTIME);
				}
				//スコア加算
				set.push_score(same_count,top_chain);


		}
		//横の消滅成立登録
		if(y_or_x == 2){
			if(type == 2)i=YOKO;//行の最終確認の場合

				set.each_score[same_count-3]++;//カウント加算
				//潜在連鎖回数(0～99)を取得
				int top_chain=0;
				for(int k=i-1;i-same_count <= k ;k--){ if(set.bl.rb(k, j, 3) > top_chain)top_chain = set.bl.rb(k, j, 3); }
				if(top_chain == 0)top_chain=1;

				//消滅予定登録及び、確定連鎖回数の書き込み
				int now_point= 0;
				for(int k=i-1;i-same_count <= k ;k--){
					now_point++;//処理中のブロックが何番目のものであるか
					DBL[k][j]=1;//削除登録

					//四方にお邪魔ブロックがあったら解凍
					SetDisturb.melt_disturb(k,j,set);

					//既存の連鎖登録がない場合、そのまま登録
					if(set.bl.rb(k, j, 4) == 0){set.bl.wb(k, j, 4, top_chain); set.bl.wb(k, j, 5, now_point);}

					//既に連鎖登録されている場合、より大きな確定連鎖数、より遅い消滅待機時間にそれぞれ更新する
					else{
								//両者の確定連鎖数を比較し、多い方に更新
								if(top_chain > set.bl.rb(k, j, 4)){set.bl.wb(k, j, 4, top_chain);}

								//両者の消滅待機時間を比較し、遅い方に更新
								if(now_point > set.bl.rb(k, j, 5)){set.bl.wb(k, j, 5, now_point);}

					}

				}
				//二連鎖以上で連鎖数表示
				if(top_chain > 1){
					int ms_setX = (i-same_count)*PANELSIZE + PANELORIGIN_x;
					int ms_setY = j*PANELSIZE - 10 + PANELORIGIN_y;
					set.push_message(top_chain + "連鎖",ms_setX,ms_setY,DELTIME);
				}
				//スコア加算
				set.push_score(same_count,top_chain);

		}


	}
}
