
public class BlockChecker extends EditBlock {

	//消滅中ならスクロールをストップさせる
	public static boolean isScrollStop(SetBlock set){
		
		if(set.del_count == 0)return false;
		return true;
	}
	
	//トップラインにブロックが有るか判定()
	public static boolean isTopBlock(SetBlock set){

		for(int i=0 ; i < YOKO ; i++) {
			if(set.bl.rb(i, 0, 0) != 0 && set.bl.rb(i, 0, 1) != 2 && set.bl.rb(i, 0, 1) != 3 && set.bl.rb(i, 0, 1) != 5){return true;}
		}
		
		return false;
	}
	
}
