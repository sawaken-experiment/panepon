import java.awt.Color;
import java.awt.Image;
import java.awt.Graphics;

public class Gra {

	
	
	public void draw(Graphics g , Image panelImg , SetBlock set1, SetBlock set2) {
		
		int re_num;
		
		//色の設定
		Color gocol = Color.red;
		Color msbgcol = Color.black;
		Color mscol = Color.white;
		
		//背景
		for(int i=0;i<8;i++){for(int j=0;j<5;j++){g.drawImage(panelImg,i*85-5,j*85-5,i*85+80,j*85+80,150,0,235,85,null);}}

			g.setColor(msbgcol);
			g.fillRect(220,20,160,80);
			g.fillRect(220,110,160,270);
		if(!set1.game_running){

			g.setColor(gocol);
			g.drawString("GAME OVER",260,40);
			g.drawString("STARTボタンで戻ります",235,60);
		}


		//1Pのスコア表示
				g.setColor(mscol);
				g.drawString("SCORE",280,125);
				String num_str= ""+set1.total_score;
				if(set1.total_score > 99999){}
				else if(set1.total_score > 9999){num_str="0"+num_str;}
				else if(set1.total_score > 999){num_str="00"+num_str;}
				else if(set1.total_score > 99){num_str="000"+num_str;}
				else if(set1.total_score > 9){num_str="0000"+num_str;}
				else{num_str="00000"+num_str;}

				for(int i=0;i < 6 ;i++){
					re_num = Integer.parseInt(num_str.substring(i,i+1));
					g.drawImage(panelImg,230+i*20,130,230+(i+1)*20-1,150,re_num*20,90,(re_num+1)*20-1,109,null);
				}
				
		
		
		//2Pのスコア表示
		String num_str2= ""+set2.total_score;
		if(set2.total_score > 99999){}
		else if(set2.total_score > 9999){num_str2="0"+num_str2;}
		else if(set2.total_score > 999){num_str2="00"+num_str2;}
		else if(set2.total_score > 99){num_str2="000"+num_str2;}
		else if(set2.total_score > 9){num_str2="0000"+num_str2;}
		else{num_str2="00000"+num_str2;}

		for(int i=0;i < 6 ;i++){
			re_num = Integer.parseInt(num_str2.substring(i,i+1));
			g.drawImage(panelImg,250+i*20,160,250+(i+1)*20-1,180,re_num*20,90,(re_num+1)*20-1,109,null);
		}
		
		
		//スピード表示
		g.drawString("SPEED:"+ (21-set1.scSpeed),275,210);
		g.drawString("TIME:"+ set1.global_time,280,240);
		//g.drawString("dead_stop:"+ set1.dead_stop,280,300);



		
	}
}
