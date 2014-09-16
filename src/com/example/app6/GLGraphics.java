package com.example.app6;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.Log;

public class GLGraphics implements GLSurfaceView.Renderer{
	Screen screen;
	MainActivity a;
	enum GLGameState {Initialized, Running, Paused, Finished, Idle}
	GLGameState state = GLGameState.Initialized;
	Object stateChanged = new Object();
	long startTime = System.nanoTime();
	private int frames=0;
	float fpsTimer;
	
	public GLGraphics(MainActivity a){
		this.a = a;
	}
	@Override
	public void onDrawFrame(GL10 gl) {
		//Log.d("state",state.toString());
		//GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
		//GLGameState state = null; 
        
		//synchronized(stateChanged) { 
			//state = this.state; 
		//} 
		//if(state == GLGameState.Running) { 
			float deltaTime = (System.nanoTime()-startTime) / 1000000000.0f; 
			startTime = System.nanoTime(); 
			//Log.d("screen","update");
			screen.update(deltaTime); 
			screen.present(deltaTime);  
			frames++; 
	        fpsTimer+=deltaTime;
			if(fpsTimer >= 1) { 
	            Log.d("FPSCounter", "fps: " + frames);
	            frames = 0; 
	            fpsTimer=0;
			} 
			//Log.d("deltaTime", "" + deltaTime);
		//} 
	          
		/*if(state == GLGameState.Paused) { 
			screen.pause();             
			synchronized(stateChanged) { 
				this.state = GLGameState.Idle; 
				stateChanged.notifyAll(); 
			} 
		} 
	          
		if(state == GLGameState.Finished) { 
			screen.pause(); 
			screen.dispose(); 
			synchronized(stateChanged) { 
				this.state = GLGameState.Idle; 
				stateChanged.notifyAll(); 
			}             
		}*/
	}
	
	public MainActivity getMain(){
		return a;
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) { 
		// TODO Auto-generated method stub
		Log.d("onSurfaceChanged","width: "+width + " height: "+ height);
		if (Assets.width!=width || Assets.height!=height){
			GLES20.glClearColor(40f/256f, 160f/256f, 104f/256f, 1.0f);
			Assets.width=width;
			Assets.height=height;
			if (width>height){
		        Assets.targetwidth=356;
		        Assets.targetheight=200;
		        Assets.landscape=true;
			}
			else {
		        Assets.targetwidth=200;
		        Assets.targetheight=356;
		        Assets.landscape=false;
			}
			GLES20.glViewport(0, 0, (int) Assets.width, (int)Assets.height);
			Assets.input.setScale(Assets.targetwidth/(float)width,Assets.targetheight/(float)height);
			AndroidPicture.newScreen();
		}
		if (Assets.reload) {
			AndroidPicture.reload();
			LoadingScreen.load(); 
		}
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		// TODO Auto-generated method stub
		Log.d("GLSurfaceViewTest", "surface created"); 
		synchronized(stateChanged) {  
			if(state == GLGameState.Initialized){
				screen = getStartScreen(); 
			}
			state = GLGameState.Running; 
			screen.resume(); 
			startTime = System.nanoTime(); 
		}
	}
	public Screen getStartScreen() {
		Log.d("once","please");
		return new LoadingScreen(this);
	}
	public void setScreen(Screen screen) {
		this.screen = screen;
	}
}
