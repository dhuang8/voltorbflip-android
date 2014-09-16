package com.example.app6;

public class Numbers {
	public static void draw(String s, int x, int y, int type){
		if (type==0){
			for (int n=0;n<s.length();n++){
				Assets.sheet.setBlend(true);
				Assets.sheet.draw(x,y,x+6,y+8,s.charAt(n)+10-48);	
				x+=8;
			}
		}
		else if (type==1){
			for (int n=0;n<s.length();n++){
				Assets.sheet.setBlend(false);
				Assets.sheet.draw(x,y,x+14,y+22,s.charAt(n)+20-48);	
				x+=16;
			}
		}
	}
}
