public class ScoreAttack {

	public SetBlock Set = new SetBlock(1);
	public SetBlock Set2 = new SetBlock(2);
	public SetBlock Set_ref;

	public AI ai;


	public int Main(int run_type,int ref){

			if(ref == 1)Set_ref = Set;
			if(ref == 2)Set_ref = Set2;

			if(Set_ref.loop_count % 100 == 0){Set_ref.global_time++;}
			if(Set_ref.loop_count > 999){
				Set_ref.loop_count = 0;
				if(Set_ref.scSpeed > 1)Set_ref.scSpeed--;//スクロールスピードup
			}
			Set_ref.loop_count++;

			//スペースが押されていたら強制スクロール

			if(Set_ref.scrollOn) {
				if(Set_ref.dead_stop) Set_ref.dead_count = 1000;
				else Set_ref.counter++;
			}

			//zキーが押されたらブロックを入れ替える
			if(Set_ref.changeOn == 1) {
				Set_ref.changeOn = 2;
				Set_ref.Change_block();
			}
			//スタートボタンフラグ解除
			if(Set_ref.startKeyIsOn == 1)Set_ref.startKeyIsOn = 2;


			//0.1秒ごとに自動スクロール
			if(!Set_ref.dead_stop && !BlockChecker.isScrollStop(Set_ref) && Set_ref.loop_count % Set_ref.scSpeed == 0) Set_ref.counter++;

			//落下中ブロックのIDを加算する
			//DelBlock_ob.downCount(Set_ref);
			DownBlock.downCount(Set_ref);

			//落下可能ブロックがないかチェック
			//DelBlock_ob.downSearch(Set_ref);
			DownBlock.downSearch(Set_ref);

			//削除可能なブロックがないかチェック
			//DelBlock_ob.deleteSearch(Set_ref);
			DelChecker.DeleteSearch(Set_ref);

			//削除待機ブロックのIDを加算する
			//DelBlock_ob.deleteCount(Set_ref);
			DelCounter.deleteCount(Set_ref);

			//解凍待機ブロックのIDを加算する
			//DelBlock_ob.meltCount(Set_ref);
			SetDisturb.meltCount(Set_ref);

			//トップラインにブロックがなければフラグ解除
			if(Set_ref.dead_stop && !BlockChecker.isTopBlock(Set_ref)){
				Set_ref.dead_stop =false;
				Set_ref.dead_count=0;
			}
			//死亡フラグが立っていたら、カウントを加算
			if(Set_ref.dead_stop && !BlockChecker.isScrollStop(Set_ref))Set_ref.dead_count++;

			//スクロールが30以上になったら行を追加してリセット
			if(Set_ref.counter > 29) {
				Set_ref.counter = 0;
				if(!Set_ref.dead_stop){
					//死亡フラグを立てる
					if(Set_ref.bl.pushBlock()){
						Set_ref.dead_stop =true;
						Set_ref.bl.create_newline();
						Set_ref.cursorPoint_y--;
					}
					else{
						Set_ref.bl.create_newline();
						if(Set_ref.cursorPoint_y > 2) {Set_ref.cursorPoint_y--;}
					}
				}
			}

			//お邪魔ブロック用に、リロード時(スクロールが30になったとき)以外でも死亡フラグを立てる
			if(!Set_ref.dead_stop && BlockChecker.isTopBlock(Set_ref)){
				Set_ref.dead_stop =true;

			}

			//死亡フラグが立っていて、カウントも超過していたらゲームオーバー
			if(Set_ref.dead_stop && Set_ref.dead_count > 100){
				Set_ref.game_running = false;
				run_type=2;
				Set_ref.del_count = 0;

			}

			//定期ダメージ計算
			if(ref == 2 && Set_ref.global_time != 0 && Set_ref.global_time % 10 == 0)damage();

			return run_type;
	}

	public void damage(){
		
		if(Set.attack >= Set2.attack){
			//Set2.damage += Set.attack - Set2.attack;
			Set.attack = 0;
			Set2.attack = 0;
		}
		else{
			Set.damage += Set2.attack - Set.attack;
			Set2.attack = 0;
			Set.attack = 0;
		}
		Set.bl.stock += Set.damage/10;
		Set2.bl.stock += Set2.damage/10;
		Set.damage = 0;
		Set2.damage = 0;
	}
	

}
