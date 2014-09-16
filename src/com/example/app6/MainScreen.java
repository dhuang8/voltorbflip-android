package com.example.app6;

import java.util.List;

import android.util.Log;

import com.example.app6.Input.TouchEvent;

public class MainScreen extends Screen{
	
	Board board;
	int totalscore=0;
	int difficulty=0;
	int retrytimer=0;
	int lefttimer=0;
	int righttimer=0;
	boolean play=true;
	int playtimer=0;
	boolean tag[]= new boolean[4]; 
	
	public MainScreen(GLGraphics game) {
		super(game);
		board = new Board(difficulty);
		for (int n=0;n<4;n++){
			tag[n]=false;
		}
		// TODO Auto-generated constructor stub
	}
 
	@Override
	public void update(float deltaTime) {
		// TODO Auto-generated method stub
		List<TouchEvent> touchEvents = Assets.input.getTouchEvents();
		int len = touchEvents.size(); 
		if (Assets.landscape){
			for(int i = 0; i < len; i++) { 
				TouchEvent event = touchEvents.get(i); 
				if(event.type == TouchEvent.TOUCH_DOWN) {
					if (play){
						if (inBounds(event,106,6,133+4*32,33+4*32)){
							Log.d("x "+event.x,"y "+event.y);
							play=board.touch(event,tag);
							if (play==false) board.end();
						}
					}
					if (inBounds(event,293,171,350,194)){					
						totalscore+=board.move(deltaTime);
						board.newGame(difficulty);
						retrytimer=10;
						play=true;
						return;
					}
					else if (inBounds(event,42,159,65,178)){
						if (difficulty>0) difficulty--;
						lefttimer=7;
					}
					else if (inBounds(event,69,159,92,178)){					
						if (difficulty<9) difficulty++;
						righttimer=7;
					}
					else {
						for (int n=0;n<4;n++){
							if (inBounds(event,312,36+24*n,335,59+24*n)){
								tag[n]=!tag[n];
							}
						}						
					}
				}
			}
		}
		else {
			for(int i = 0; i < len; i++) { 
				TouchEvent event = touchEvents.get(i); 
				if(event.type == TouchEvent.TOUCH_DOWN) {
					if (play){
						if (inBounds(event,6,162,161,317)){
							Log.d("x "+event.x,"y "+event.y);
							play=board.touch(event,tag);
							if (play==false) board.end();
						}
					}					
					if (inBounds(event,136,130,193,153)){					
						totalscore+=board.move(deltaTime);
						board.newGame(difficulty);
						retrytimer=10;
						play=true;
						return;
					}
					else if (inBounds(event,137,97,160,116)){
						if (difficulty>0) difficulty--;
						lefttimer=7;
					}
					else if (inBounds(event,164,97,187,116)){					
						if (difficulty<9) difficulty++;
						righttimer=7;
					}
					else {
						for (int n=0;n<4;n++){
							if (inBounds(event,9+24*n,130,32+24*n,153)){
								tag[n]=!tag[n];
							}
						}						
					}
				}
			}
		}
		board.move(deltaTime);
	}
	
	private boolean inBounds(TouchEvent event, int left, int top, int right, int bot) { 
		if(event.x > left && event.x < right &&  
				event.y > top && event.y < bot)  
			return true; 
		else 
			return false; 
	}

	@Override
	public void present(float deltaTime) {
		// TODO Auto-generated method stub
		Assets.bg.setBlend(false);
		if (Assets.landscape) Assets.bg.draw(0, 0, Assets.targetwidth , Assets.targetheight);
		else {
			Assets.bgp.draw(0, 0, Assets.targetwidth , Assets.targetheight);
			//Log.d(""+Assets.targetwidth , ""+Assets.targetheight);
		}
		board.draw();
		String sscore = ""+totalscore;
		int len=sscore.length();
		for (int n=0;n<(5-len);n++){
			sscore="0"+sscore;
		}				
		if (Assets.landscape) {
			Numbers.draw(sscore,15,31,1);
			Numbers.draw(""+difficulty,15,158,1);
			if (retrytimer>0){
				retrytimer-=deltaTime*60;
				Assets.sheet.draw(293, 171, 350+1,194+1,32);
			}
			if (lefttimer>0){
				lefttimer-=deltaTime*60;
				Assets.bg.setBlend(true);
				Assets.sheet.draw(42,159,65+1,178+1,33);
			}
			if (righttimer>0){
				righttimer-=deltaTime*60;
				Assets.bg.setBlend(true);
				Assets.sheet.draw(69,159,92+1,178+1,34);
			}
			for (int n=0;n<4;n++){
				if (tag[n]){
					Assets.sheet.draw(312,36+24*n,336,60+24*n,44+n);
				}
			}
		}
		else {
			Numbers.draw(sscore,110,16,1);
			Numbers.draw(""+difficulty,110,96,1);
			if (retrytimer>0){
				retrytimer-=deltaTime*60;
				Assets.sheet.draw(136, 130, 193+1,153+1,32);
			}
			if (lefttimer>0){
				lefttimer-=deltaTime*60;
				Assets.sheet.draw(137,97,161,116+1,33);
			}
			if (righttimer>0){
				righttimer-=deltaTime*60;
				Assets.sheet.draw(164,97,187+1,116+1,34);
			}
			for (int n=0;n<4;n++){
				if (tag[n]){
					Assets.sheet.draw(9+24*n,130,33+24*n,154,n+44);
				}
			}
		}
		//Numbers.draw("02", 117, 168, 0); 
		//Assets.sheet.draw(117, 168, 117+6, 168+8, 10); 
		//Assets.sheet.draw(125, 168, 125+6, 168+8, 11);
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}
	
}