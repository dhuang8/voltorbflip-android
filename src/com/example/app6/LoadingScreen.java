package com.example.app6;

import java.io.IOException;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Bitmap.Config;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.util.Log;

public class LoadingScreen extends Screen{
	AndroidPicture loading;

	public LoadingScreen(GLGraphics game) {
		super(game);
		/*Paint paint = new Paint();
		paint.setColor(Color.BLACK); 
    	Bitmap b= Bitmap.createBitmap(400, 100, Config.ARGB_8888);
    	Canvas c = new Canvas (b);
    	//paint.setStyle(Style.STROKE);
    	//paint.setStrokeWidth(10);
        //c.drawRect(0, 0, 99, 499, paint); 
    	paint.setColor(Color.WHITE);
    	paint.setTextSize(50);
    	paint.setTextAlign(Align.CENTER);
    	paint.setStrokeWidth(5);
    	paint.setStyle(Style.FILL);
        c.drawText("Loading...",200,50,paint);
        loading = new AndroidPicture(b);*/
	}

	@Override
	public void update(float deltaTime) {
        //loading.draw((int)Assets.targetwidth/2-800, (int)Assets.targetheight/2-200,(int)Assets.targetwidth/2+800, (int)Assets.targetheight/2+200);
	}

	@Override
	public void present(float deltaTime) {
    	load();
		/*Paint paint = new Paint();
		paint.setColor(Color.BLACK); 
    	Bitmap b= Bitmap.createBitmap(800, 60, Config.ARGB_8888);
    	Canvas c = new Canvas (b);
    	//paint.setStyle(Style.STROKE);
    	//paint.setStrokeWidth(10);
        //c.drawRect(0, 0, 99, 499, paint);
    	paint.setColor(Color.WHITE);
    	paint.setTextSize(50);
    	paint.setTextAlign(Align.CENTER);
    	paint.setStrokeWidth(5);
    	paint.setStyle(Style.FILL);
        c.drawText("Version 0.01a",400,60,paint);
        Assets.version = new AndroidPicture(b);*/
        
		game.setScreen(new MainScreen(game));
		
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
	
	public static void load(){
		AssetManager assets = Assets.assets;
		try {
			if (Assets.music==null)	Assets.music = Assets.audio.newMusic("music.wav");
			Assets.music.setLooping(true);
	        if (Assets.music.isPlaying()){
				//something
			}
			else {
				Assets.music.play();
			}
			/*String[] s = new String[] {
					"vfbg.png"};
			AndroidPicture[] p = new AndroidPicture[] {
					Assets.bg};*/
	        Assets.bg=new AndroidPicture(BitmapFactory.decodeStream(assets.open("vfbg.png")));
	        Assets.bgp=new AndroidPicture(BitmapFactory.decodeStream(assets.open("vfbgp.png")));
			Assets.sheet=new SpriteSheet(BitmapFactory.decodeStream(assets.open("sheet.png")),52);
			for (int n=0;n<2;n++){
				for (int m=0;m<5;m++){
					Assets.sheet.add(n*23, m*23, n*23+22, m*23+22); //0-9
				}
			}
			for (int n=0;n<10;n++){
				Assets.sheet.add(n*7, 115, n*7+6, 123); //10-19 small num
			}
			for (int n=0;n<5;n++){
				Assets.sheet.add(n*16, 124, n*16+14, 146); //20-24 big num
			}
			for (int n=0;n<5;n++){
				Assets.sheet.add(n*16, 147, n*16+14, 169); //25-29 big num
			}
			Assets.sheet.add(2*23, 4*23, 2*23+22, 4*23+22); //30 halfway open
			Assets.sheet.add(46, 0, 74, 28); //31 red selection square
			Assets.sheet.add(75, 0, 133, 24); //32 retry button
			Assets.sheet.add(70, 79, 94, 99); //33 left button
			Assets.sheet.add(70, 100, 94, 120); //34 right button
			Assets.sheet.add(0, 171, 22, 193); //35 explosions
			Assets.sheet.add(23, 171, 45, 193); //36
			Assets.sheet.add(46, 171, 68, 193); //37
			Assets.sheet.add(0, 194, 34, 228,34); //38
			Assets.sheet.add(0, 229, 42, 271,42); //39
			Assets.sheet.add(0, 272, 56, 328,56); //40
			Assets.sheet.add(0, 330, 60, 390,60); //41
			Assets.sheet.add(43, 194, 105, 256,105-43); //42
			Assets.sheet.add(57, 257, 121, 321,121-57); //43
			for (int n=0;n<2;n++){
				for (int m=0;m<2;m++){
					Assets.sheet.add(46+m*25, 29+n*25, 70+m*25, 53+n*25); //44-47
				}
			}
			for (int n=0;n<2;n++){
				for (int m=0;m<2;m++){
					Assets.sheet.add(96+m*6, 25+n*6, 101+m*6, 30+n*6); //48-51
				}
			}
			Assets.reload=false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}