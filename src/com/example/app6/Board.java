package com.example.app6;

import android.util.Log;

import com.example.app6.Input.TouchEvent;

public class Board {
	int[] board2 = new int[25];
	Panel[] board = new Panel[25];
	String[] total = new String[20];
	int score=0;
	boolean first=true;
	int endtimer=-1;
	int maxscore;
	int start=-1;
	int difficulty;
	
	/*public Board(){
		for (int n=0;n<25;n++){
			double rand=Math.random();
			int val;
			if (rand<.4) val=0;
			else if (rand<.7) val=1;
			else if (rand<.85) val=2;
			else val=3;
			board[n]=new Panel(val,n);
		}
		
		int count=0;
		String s="";
		for (int m=0;m<5;m++){
			count=0;
			s="";
			for (int n=0;n<5;n++){
				count+=board[m+n*5].getValue();
			}
			
			s+=count;
			if (count<10) s="0"+s;
			total[m]=s;
		}
		
		//count num col
		for (int m=0;m<5;m++){
			count=0;
			s="";
			for (int n=0;n<5;n++){
				count+=board[m*5+n].getValue();
			}
			
			s+=count;
			if (count<10) s="0"+s;
			total[m+5]=s;
		}
		
		//count 0 row
		for (int m=0;m<5;m++){
			count=0;
			s="";
			for (int n=0;n<5;n++){
				if (board[m+n*5].getValue()==0) count++;
			}
			
			s+=count;
			total[m+10]=s;
		}
		//count 0 col
		for (int m=0;m<5;m++){
			count=0;
			s="";
			for (int n=0;n<5;n++){
				if (board[m*5+n].getValue()==0) count++;
			}			
			s+=count;
			total[m+15]=s;
		}
	}*/
	
	public Board(int d){
		difficulty=d;
		newBoard();
	}
	
	public void newGame(int d){
		if (start==-1){
			start=0;
			difficulty=d;
			boolean close=true;
			for (int n=0;n<25;n++){
				close=(board[n].close() && close);
			}
			if (close) start=20;
		}
	}
	
	public void newBoard(){
		score=0;
		first=true;
		endtimer=-1;
		int d=difficulty;
		double rand=Math.random();
		int three;
		int two;
		int one;
		int zero;
		int maxtwo;
		maxtwo=d+3;
		zero=d+3;
		double two2=(rand*(maxtwo+1));
		two=(int)two2;

		three=(int)((maxtwo-two)*2/3);
		rand=Math.random();
		if (rand<(two2-two)) three++;
		one=25-three-two-zero;
		
		maxscore=(int) (Math.pow(2,two)*Math.pow(3,three));
		Log.d("maxscore",""+maxscore);
		genBoard(zero,one,two,three);
		calcTotal();
	}
	
	private void calcTotal(){	
		int count=0;
		String s="";
		for (int m=0;m<5;m++){
			count=0;
			s="";
			for (int n=0;n<5;n++){
				count+=board[m+n*5].getValue();
			}	
			
			s+=count;
			if (count<10) s="0"+s;
			total[m]=s;
		}
			
		//count num col
		for (int m=0;m<5;m++){
			count=0;
			s="";
			for (int n=0;n<5;n++){
				count+=board[m*5+n].getValue();
			}		
				
			s+=count;
			if (count<10) s="0"+s;
			total[m+5]=s;
		}
			
		//count 0 row
		for (int m=0;m<5;m++){
			count=0;
			s="";
			for (int n=0;n<5;n++){
				if (board[m+n*5].getValue()==0) count++;
			}		
				
			s+=count;
			total[m+10]=s;
		}		
		//count 0 col
		for (int m=0;m<5;m++){
			count=0;
			s="";
			for (int n=0;n<5;n++){
				if (board[m*5+n].getValue()==0) count++;
			}			
			s+=count;
			total[m+15]=s;
		}
	}
	
	//a=num 0s b=num 1s c=num 2s d=num 3s
	private void genBoard(int a, int b, int c, int d){
		int[] val=new int[25];
		int count=0;
		for (int n=0;n<a;n++){
			val[count]=0;
			count++;
		}
		for (int n=0;n<b;n++){
			val[count]=1;
			count++;
		}
		for (int n=0;n<c;n++){
			val[count]=2;
			count++;
		}
		for (int n=0;n<d;n++){
			val[count]=3;
			count++;
		}
		
		int rand; 
		for (int n=24;n>0;n--){ //the shuffle
			rand=(int)(Math.random()*(n+1));
			board[n]=new Panel(val[rand],n);
			val[rand]=val[n];
		}
		board[0]=new Panel(val[0],0);
	}
	
	public int move(float deltaTime){
		for (int n=0;n<25;n++){
			board[n].move(deltaTime);
		}
		if (endtimer>-1) endtimer++;
		if (endtimer>120){
			endtimer=-1;
			for (int n=0;n<25;n++){
				board[n].end();
			}
		}
		if (start>-1) start++;
		if (start>20){
			start=-1;
			newBoard();
			return 0;
		}
		if (start>-1) return 0;
		return score;
	}
	
	public void end(){
		Log.d("end","game over");
		endtimer=0;
		if (maxscore==score) endtimer=80;
	}
	
	public boolean touch(TouchEvent e, boolean[] tag){
		if (Assets.landscape){
			for (int m=0;m<5;m++){
				for (int n=0;n<5;n++){
					if (inBounds (e,106+m*32,6+n*32,133+m*32,33+n*32)){
						if (board[n*5+m].click(tag)){
							if (first) score=1;
							first=false;
							score*=board[n*5+m].getValue();
							if (score==0) return false;
							else if (score==maxscore) return false;
						}
					}
				}
			}
		}
		else {
			for (int m=0;m<5;m++){
				for (int n=0;n<5;n++){
					if (inBounds (e,6+m*32,162+n*32,33+m*32,189+n*32)){
						if (board[n*5+m].click(tag)){
							if (first) score=1;
							first=false;
							score*=board[n*5+m].getValue();
							if (score==0) return false;
							else if (score==maxscore) return false;
						}
					}
				}
			}
		}
		return true;
	}
	
	private boolean inBounds(TouchEvent event, int left, int top, int right, int bot) { 
		if(event.x > left && event.x < right &&  
				event.y > top && event.y < bot)  
			return true; 
		else 
			return false; 
	}
	
	public void draw(){
		Assets.sheet.setBlend(true);
		if (Assets.landscape){
			for (int m=0;m<5;m++){
				Numbers.draw(total[m], 117+m*32, 168, 0);
				Numbers.draw(total[m+5], 277, 8+m*32, 0);
				Numbers.draw(total[m+10], 125+m*32, 181, 0);
				Numbers.draw(total[m+15], 285, 21+m*32, 0);
			}
		}
		else { 
			for (int m=0;m<5;m++){
				Numbers.draw(total[m], 17+m*32, 324, 0);
				Numbers.draw(total[m+5], 177, 164+m*32, 0);
				Numbers.draw(total[m+10], 25+m*32, 337, 0);
				Numbers.draw(total[m+15], 185, 177+m*32, 0);
			}
		}
		String sscore = ""+score;
		int len=sscore.length();
		for (int n=0;n<(5-len);n++){
			sscore="0"+sscore;
		}
		if (Assets.landscape) Numbers.draw(sscore,15,100,1);
		else Numbers.draw(sscore,110,57,1);	
		
		int bomb=-1;
		for (int m=0;m<25;m++){
			if(board[m].draw()) bomb=m;
		}
		if (bomb>-1) board[bomb].draw2();
	}
}
