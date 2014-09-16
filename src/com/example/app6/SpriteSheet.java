package com.example.app6;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.util.Log;

public class SpriteSheet extends AndroidPicture{
	public int width;
	public int height;
	int[] spritex;
	int[] spritey;
	int[] spritesize;
	int counter=0;
	boolean ready2=false;
	private FloatBuffer tverticesmap;
	
	public SpriteSheet(Bitmap bitmap, int size) {
		super(bitmap);
		spritex = new int [size*2];
		spritey = new int [size*2];
		spritesize = new int [size];
		width=bitmap.getWidth();
		height=bitmap.getHeight();
		if (!ready2){
			ByteBuffer byteBuffer3 = ByteBuffer.allocateDirect(4 * 8); 
			byteBuffer3.order(ByteOrder.nativeOrder()); 
			tverticesmap = byteBuffer3.asFloatBuffer();
			tverticesmap.put(new float[] { 0.0f, 1.0f,  0.0f, 0.0f,  1.0f, 1.0f,  1.0f, 0.0f });
			ready2=true;
		}
	}
		
	public void add(int left, int top, int right, int bot){
		spritex[counter]=left;
		spritey[counter]=top;
		counter++;
		spritex[counter]=right;
		spritey[counter]=bot;
		counter++;
	}
	
	public void add(int left, int top, int right, int bot, int size){
		spritesize[counter/2]=size;
		spritex[counter]=left;
		spritey[counter]=top;
		counter++;
		spritex[counter]=right;
		spritey[counter]=bot;
		counter++;
	}
	
	protected void bindTexV(){
		tverticesmap.position(0);
    	GLES20.glVertexAttribPointer(mTexCoordLoc, 2 ,GLES20.GL_FLOAT, false, 4*2, tverticesmap); 
    }
	
	public void draw(int x, int y, int size, int count, boolean t){
		draw(x-size/2,y-size/2,x+size/2,y+size/2,count);  
	}
	
	public void drawCenter(int x, int y, int count){
		draw(x-spritesize[count]/2,y-spritesize[count]/2,x+spritesize[count]/2,y+spritesize[count]/2,count);  
	}
	
	public void draw(float left, float top, float right, float bot, int count){ 
		float sleft=(float)spritex[count*2]/width;
		float stop=(float)spritey[count*2]/height; 
		float sright=(float)spritex[count*2+1]/width;
		float sbot=(float)spritey[count*2+1]/height;
		tverticesmap.clear();    
		tverticesmap.put(new float[] { sleft, sbot,  sleft, stop,  sright, sbot, sright, stop});
		draw(left, top, right, bot);
	}
}