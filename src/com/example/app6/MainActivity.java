package com.example.app6;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Point;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends Activity{
	
	GLSurfaceView glView;
	AndroidAudio audio; 
	AndroidInput input;
	Screen screen;
	long startTime = System.nanoTime(); 
	private static Display display;
	private boolean set=false;
	
	public void onCreate(Bundle savedInstanceState) { 
		Assets.reload=true;
        requestWindowFeature(Window.FEATURE_NO_TITLE); 
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //full screen
        super.onCreate(savedInstanceState); 
        glView = new MyGLSurfaceView(this,this); 
        Assets.audio = new AndroidAudio(this); 
        setContentView(glView); 
        Assets.targetwidth=356;
        Assets.targetheight=200;
        Assets.assets=getAssets();
        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        display = wm.getDefaultDisplay();
        setInput();
    }
	
	@Override 
	public void onResume() { 
		super.onResume(); 
		glView.onResume(); 
		if (Assets.music!=null){
			if (Assets.music.isPlaying()){
				//something
			}
			else {
				Assets.music.play();
			}
		}
	}
	
	public AndroidInput getInput(){
		return input;
	}
	
	@Override 
	public void onPause() { 
		super.onPause();
		glView.onPause();
		if (Assets.music!=null)	Assets.music.pause();
	/*	synchronized(stateChanged) { 
			if(isFinishing())             
				state = GLGameState.Finished; 
			else 
				state = GLGameState.Paused; 
			while(true) { 
				try { 
					stateChanged.wait(); 
					break; 
				} catch(InterruptedException e) {  
				} 
			} 
		} 
		//wakeLock.release(); 
		glView.onPause();   
		super.onPause(); */
	} 
	
	/*@SuppressWarnings("deprecation")
	static void setDimensions(){
		if (android.os.Build.VERSION.SDK_INT >= 13){
        	Point size = new Point();
        	display.getSize(size);
        	Assets.width = (float) size.x;
        	Assets.height = (float) size.y;
        }
		else{
			Assets.width = display.getWidth();  // deprecated
			Assets.height = display.getHeight();  // deprecated
		}
		Log.d("width",""+Assets.width); 
	}*/
	
	public void setInput(){
		Assets.input = new AndroidInput(this, glView, 1, 1); 
	}
	
	public void onStop(){
		super.onStop();
		Log.d("Main Activity", "onStop");
	}
	
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		Log.d("Main Activity", "onConfigurationChanged");
	}
}

/*
class MySurfaceView extends SurfaceView{

	Bitmap framebuffer;

	public MySurfaceView(Context context, Bitmap framebuffer, SurfaceHolder h) {
		super(context);

        h = getHolder();
		// Set the Renderer for drawing on the GLSurfaceView
	        
		// Render the view only when there is a change in the drawing data
		//setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
	}
}*/

class MyGLSurfaceView extends GLSurfaceView {

	private final GLGraphics mRenderer;

	public MyGLSurfaceView(Context context, MainActivity a) {
		super(context);
		Assets.reload=true;
		// Create an OpenGL ES 2.0 context.
		setEGLContextClientVersion(2);

		// Set the Renderer for drawing on the GLSurfaceView
		mRenderer = new GLGraphics(a);
		setRenderer(mRenderer);
		if (android.os.Build.VERSION.SDK_INT >= 11){
			setPreserveEGLContextOnPause (true);
		}
		// Render the view only when there is a change in the drawing data
		//setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);			
	}
	public void onResume(){
		super.onResume(); 
		Log.d("MyGLSurfaceView", "onResume");
		//AndroidPicture.reload();
		Assets.reload=true;
	}
	
	public void onPause (){
		super.onPause(); 
		Log.d("MyGLSurfaceView", "onPause");		
	}
}
