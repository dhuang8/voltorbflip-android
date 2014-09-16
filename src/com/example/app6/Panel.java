package com.example.app6;

public class Panel {
	int state=0;
	int value;
	int pos;
	float timer;
	boolean end=false;
	boolean tags[]= new boolean[4];
	
	public Panel (int value, int pos){
		this.value=value;
		this.pos=pos;
		for (int x=0;x<4;x++){
			tags[x]=false;
		}
	}
	
	public int getValue(){
		return value;
	}
	
	public boolean click(){
		if (state==0) {
			state=1;
			return true;
		}
		return false;
	}
	
	public boolean click(boolean[] button){
		if (button[0] || button[1] || button[2] || button[3]){
			tag(button);
		}
		else {
			if (state==0) {
				state=1;
				return true;
			}
		}
		return false;
	}
	public void tag(boolean[] button){
		for (int n=0;n<4;n++){
			if (tags[n]==button[n]) tags[n]=false;
			else this.tags[n]=true;
		}
	}
	
	public void move(float deltaTime){
		if ((state>0 && state<4) || state<0){
			if (timer<4) {
				timer+=deltaTime*60;
				return;
			}
			timer=0;
			state++;
		}
		else if (state>3 && value==0 && state<14 && end==false){
			if (timer<6) {
				timer+=deltaTime*60;
				return;
			}
			timer=0;
			state++;
		}
	}
	
	public void end(){
		end=true;
		click();
	}
	
	public boolean close(){
		if (state!=0) state=-3;
		return (state==0);
	}
	
	public boolean draw(){
		int n=pos%5;
		int m=pos/5;
		if (state>7) Assets.sheet.setBlend(true);
		else Assets.sheet.setBlend(false);
		if (Assets.landscape){
			if (state==4 || state==14) Assets.sheet.draw(109+n*32, 9+m*32, 109+22+n*32, 9+22+m*32, value);
			else if (state==0) {
				Assets.sheet.draw(109+n*32, 9+m*32, 109+22+n*32, 9+22+m*32, 4);
				for (int x=0;x<4;x++){
					if (tags[x]) Assets.sheet.draw(110+n*32+(x%2)*15, 10+m*32+(x/2)*15, 110+5+n*32+(x%2)*15, 10+5+m*32+(x/2)*15, x+48);
				}
			}
			else if (Math.abs(state)==3) Assets.sheet.draw(109+n*32, 9+m*32, 109+22+n*32, 9+22+m*32, value+5);
			else if (Math.abs(state)==2) Assets.sheet.draw(109+n*32, 9+m*32, 109+22+n*32, 9+22+m*32, 30);
			else if (Math.abs(state)==1) Assets.sheet.draw(109+n*32, 9+m*32, 109+22+n*32, 9+22+m*32, 9);
		}
		else{
			if (state==4 || state==14) Assets.sheet.draw(9+n*32, 165+m*32, 9+22+n*32, 165+22+m*32, value);
			else if (state==0) {
				Assets.sheet.draw(9+n*32, 165+m*32, 9+22+n*32, 165+22+m*32,  4);
				for (int x=0;x<4;x++){
					if (tags[x]) Assets.sheet.draw(10+n*32+(x%2)*15, 166+m*32+(x/2)*15, 10+5+n*32+(x%2)*15, 166+5+m*32+(x/2)*15, x+48);
				}
			}
			else if (Math.abs(state)==3) Assets.sheet.draw(9+n*32, 165+m*32, 9+22+n*32, 165+22+m*32, value+5);
			else if (Math.abs(state)==2) Assets.sheet.draw(9+n*32, 165+m*32, 9+22+n*32, 165+22+m*32, 30);
			else if (Math.abs(state)==1) Assets.sheet.draw(9+n*32, 165+m*32, 9+22+n*32, 165+22+m*32, 9);
		}
		if (state>4 && state!=14) return true;
		return false;
	}
	
	public void draw2(){
		int n=pos%5;
		int m=pos/5;
		if (state>7) Assets.sheet.setBlend(true);
		else Assets.sheet.setBlend(false);
		if (Assets.landscape){
			if (state==5) Assets.sheet.draw(109+n*32, 9+m*32, 109+22+n*32, 9+22+m*32, 35);
			else if (state==6) Assets.sheet.draw(109+n*32, 9+m*32, 109+22+n*32, 9+22+m*32, 36);
			else if (state==7) Assets.sheet.draw(109+n*32, 9+m*32, 109+22+n*32, 9+22+m*32, 37);
			else if (state==8) Assets.sheet.drawCenter(109+11+n*32, 9+11+m*32, 38);
			else if (state==9) Assets.sheet.drawCenter(109+11+n*32, 9+11+m*32, 39);
			else if (state==10) Assets.sheet.drawCenter(109+11+n*32, 9+11+m*32, 40);
			else if (state==11) Assets.sheet.drawCenter(109+11+n*32, 9+11+m*32, 41);
			else if (state==12) Assets.sheet.drawCenter(109+11+n*32, 9+11+m*32, 42);
			else if (state==13) Assets.sheet.drawCenter(109+11+n*32, 9+11+m*32, 43);
		}
		else{
			if (state==5) Assets.sheet.draw(9+n*32, 165+m*32, 9+22+n*32, 165+22+m*32, 35);
			else if (state==6) Assets.sheet.draw(9+n*32, 165+m*32, 9+22+n*32, 165+22+m*32, 36);
			else if (state==7) Assets.sheet.draw(9+n*32, 165+m*32, 9+22+n*32, 165+22+m*32, 37);
			else if (state==8) Assets.sheet.drawCenter(9+11+n*32, 165+11+m*32, 38);
			else if (state==9) Assets.sheet.drawCenter(9+11+n*32, 165+11+m*32, 39);
			else if (state==10) Assets.sheet.drawCenter(9+11+n*32, 165+11+m*32, 40);
			else if (state==11) Assets.sheet.drawCenter(9+11+n*32, 165+11+m*32, 41);
			else if (state==12) Assets.sheet.drawCenter(9+11+n*32, 165+11+m*32, 42);
			else if (state==13) Assets.sheet.drawCenter(9+11+n*32, 165+11+m*32, 43);
		}		
	}
}
