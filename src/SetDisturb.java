
public class SetDisturb extends EditBlock {

	//落下・生成処理。DownBlock.downCountから呼ばれる
	public static void Obstructive(SetBlock set, int tamtto, int acrt){
		
	
			boolean exist_top = false;
			for(int i=0 ; i < YOKO ; i++) {
				if(set.bl.rb(i, 0, 0) !=0 || set.bl.rb(i, 0, 1) !=0 )exist_top = true;
			}
			if(!exist_top && set.bl.stock > 0){
				
				
				if(tamtto <= set.bl.d_location){
					
						set.bl.d_speed += acrt;
						set.bl.d_location += set.bl.d_speed;
						
						int NBL2[] = set.bl.create_line();
						for(int i=0 ; i < YOKO ; i++) {
							set.bl.wb(i, 0, 0,NBL2[i]);
							set.bl.wb(i, 0, 1,4);
							set.bl.wb(i, 0, 2,set.bl.d_location - tamtto);
							set.bl.wb(i, 0, 4,set.bl.d_speed);
						}
						set.bl.d_location = 0;
						set.bl.d_speed = 0;
						set.bl.stock--;
					
				}
				else{
					set.bl.d_speed += acrt;
					set.bl.d_location += set.bl.d_speed;
				}
				
			}
		
	}
	
	//解凍開始処理
	public static void melt_disturb(int x,int y,SetBlock set){
		if(set.bl.rb(x, y+1, 1) == 4){
			for(int i=0 ; i < YOKO ; i++) {
				set.bl.wb(i, y+1, Blocks.STATE,6);
				set.bl.wb(i, y+1, Blocks.COUNT,0);
				set.bl.wb(i, y+1, Blocks.ORDER,i+1);
			}
		}
		if(set.bl.rb(x, y-1, 1) == 4){
			for(int i=0 ; i < YOKO ; i++) {
				set.bl.wb(i, y-1, Blocks.STATE,6);
				set.bl.wb(i, y-1, Blocks.COUNT,0);
				set.bl.wb(i, y-1, Blocks.ORDER,i+1);
			}
		}

	}
	//解凍カウントの加算、100になったら消滅
	public static void meltCount(SetBlock set){
		
		for(int i=0 ; i < YOKO ; i++) {
			for(int j=0 ; j < TATE ; j++) {
				if(set.bl.rb(i, j, 1) == 6){

					if(set.bl.rb(i, j, 2) == MELTTIME){
						set.bl.wb(i, j, 1,0);
						set.bl.wb(i, j, 2,0);
						set.bl.wb(i, j, 5,0);
					}
					else{set.bl.wb(i, j, 2,set.bl.rb(i, j, 2)+1);}
				} 
			}
		}
	}
}
