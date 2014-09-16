package com.example.app6;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.opengl.Matrix;
import android.util.Log;

public class AndroidPicture {
	Bitmap bitmap;
	private float[] mRotationMatrix = new float[16];
	private float[] srmatrix = new float[16];
	private int textureId;
	private float angle;
	private static float[] mProjMatrix = new float[16];
	float[] tMatrix = new float[16];
	final static String vertexShaderCode =
			// This matrix member variable provides a hook to manipulate
			// the coordinates of the objects that use this vertex shader
			"uniform mat4 uMVPMatrix;" +
//			"attribute vec4 vColor;" +
	        "attribute vec4 vPosition;" +
	        "attribute vec2 a_texCoord;" +      // texture coordinate for vertex...
            "varying vec2 v_texCoord;" +        // ...which we forward to the fragment shader
//	        "varying vec4 aColor;" +
			"uniform float op;" +
	        "varying float opf;" +
	        "void main() {" +
	        // the matrix must be included as a modifier of gl_Position
	        //vPosition * 
	        "  gl_Position = vPosition*uMVPMatrix;" +
	        //"  aColor = vColor;" +
	        "  v_texCoord = a_texCoord;" +
	        "  opf = op;" +
	        "}";

	private static final String fragmentShaderCode =
//			"precision mediump float;" +
//	        "varying vec4 aColor;" +
	        "uniform sampler2D u_texture;" + 
	        "varying vec2 v_texCoord;" +   
	        "varying float opf;" + 
	        "void main() {" +
	        //"  gl_FragColor = aColor;" +
	//       "  gl_FragColor = texture2D(u_texture, v_texCoord);" +
	        "  gl_FragColor = vec4(texture2D(u_texture, v_texCoord).xyz, texture2D(u_texture, v_texCoord).w * opf); " +
	        "}";
	private static boolean ready=false;
	private static int mProgram;
	static FloatBuffer vertices;
	static FloatBuffer tvertices;
	static Bitmap curbitmap;
	
	static int mTexCoordLoc;
	static int textureUniformHandle;
	static int mPositionHandle;
	static int mMVPMatrixHandle;
	static int ophandle;
	float opacity=1;
	static int count=0;
	boolean blend=true;	
	
	public AndroidPicture (Bitmap bitmap){
		this.bitmap=bitmap;
        if (!ready){
			Log.d("ready","false");
	    	reload();
		}
    	int[] textureIds = new int[1];
    	GLES20.glGenTextures ( 1, textureIds, 0 );// Generate a texture object
        textureId=textureIds[0];
        GLES20.glBindTexture ( GLES20.GL_TEXTURE_2D, textureId); // Bind the texture object
        GLUtils.texImage2D ( GLES20.GL_TEXTURE_2D, 0, bitmap,0 );   
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST); //pixelated GLES20.GL_NEAREST
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST); //smooth GLES20.GL_LINEAR
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0); 
        bitmap.recycle();
	}
	
	public void load(){
	}
	
	public void setBlend(boolean b){
		if (b) {
			GLES20.glEnable(GLES20.GL_BLEND);
	        //GLES20.glBlendFunc(GLES20.GL_ONE, GLES20.GL_ONE_MINUS_SRC_ALPHA);
			GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
		}
		else GLES20.glDisable(GLES20.GL_BLEND);
		blend=b;
	}
	
	public void setRotate(float d){
		this.angle=d;
    	//bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), mRotationMatrix, true);		
	}
	
	public static void newScreen(){
		Matrix.orthoM (mProjMatrix, 0, -Assets.targetwidth/2, Assets.targetwidth/2, Assets.targetheight/2, -Assets.targetheight/2, -1, 1);
		GLES20.glViewport(0, 0, (int) Assets.width, (int)Assets.height);
	}
	
	protected static void reload(){
		Log.d("GLES20","loading");
		ByteBuffer byteBuffer1 = ByteBuffer.allocateDirect(4 * 8); 
    	byteBuffer1.order(ByteOrder.nativeOrder()); 
    	vertices = byteBuffer1.asFloatBuffer();	    
    	ByteBuffer byteBuffer2 = ByteBuffer.allocateDirect(4 * 8); 
    	byteBuffer2.order(ByteOrder.nativeOrder()); 
    	tvertices = byteBuffer2.asFloatBuffer();
    	tvertices.put(new float[] { 0.0f, 1.0f,  0.0f, 0.0f,  1.0f, 1.0f,  1.0f, 0.0f });
		GLES20.glViewport(0, 0, (int) Assets.width, (int)Assets.height);
		int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);// prepare shaders and OpenGL program
        int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);
		mProgram = GLES20.glCreateProgram(); 
		GLES20.glAttachShader(mProgram, vertexShader);   // add the vertex shader to program
        GLES20.glAttachShader(mProgram, fragmentShader); // add the fragment shader to program
        GLES20.glLinkProgram(mProgram);                  // create OpenGL program executables
		GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
		GLES20.glUseProgram(mProgram);
        mTexCoordLoc = GLES20.glGetAttribLocation(mProgram, "a_texCoord" );
    	checkGlError("glGetAttribLocation");    
    	textureUniformHandle = GLES20.glGetUniformLocation(mProgram, "u_texture");
    	checkGlError("glGetUniformLocation");    
    	mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");// get handle to vertex shader's vPosition member      
    	checkGlError("glGetAttribLocation");    
    	mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");  // get handle to shape's transformation matrix
    	checkGlError("glGetUniformLocation");    
    	ophandle = GLES20.glGetUniformLocation(mProgram, "op");  // get handle to shape's transformation matrix
    	checkGlError("glGetUniformLocation");    
    	
		GLES20.glEnableVertexAttribArray ( mTexCoordLoc );
		GLES20.glEnableVertexAttribArray(mPositionHandle);
		GLES20.glUniform1i(textureUniformHandle, 0);
		       
		GLES20.glDisable(GLES20.GL_DITHER);
        
        ready=true;
	}
	 
    public void dispose() { 
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId); 
        int[] textureIds = { textureId }; 
        GLES20.glDeleteTextures(1, textureIds, 0);
    }
    
    public void setOpacity(float o){
    	opacity=o;
    }
    
    private static int loadShader(int type, String shaderCode){

        // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
        // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
        int shader = GLES20.glCreateShader(type);

        // add the source code to the shader and compile it
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        return shader;
    }
    
    public static void checkGlError(String glOperation) {
        int error;
        while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
            Log.d("TAG", glOperation + ": glError " + error);
            throw new RuntimeException(glOperation + ": glError " + error);
        }
    }
    
    public void draw (int x, int y, int size){
    	draw(x-size/2,y-size/2,x+size/2,y+size/2);
    }
    
    private void bindTex(){
    	//Log.d("id",""+textureId);
    	GLES20.glBindTexture ( GLES20.GL_TEXTURE_2D, textureId); // Bind the texture object
    }
    
    protected void bindTexV(){
  		tvertices.position(0);
    	GLES20.glVertexAttribPointer(mTexCoordLoc, 2 ,GLES20.GL_FLOAT, false, 4*2, tvertices); 
    }
    
    public void draw(float left, float top, float right, float bot){
    	//Log.d("texture id",""+textureId);
    	setBlend(blend);
    	float x=right-left;
    	float y=bot-top;
    	vertices.clear();    	
    	vertices.put(new float[] {  -x/2, y/2, 
    			-x/2,  -y/2,
    			x/2, y/2,
    			x/2,  -y/2, });
    	
    	float[] srtmatrix = new float[16];
    	//Matrix.setIdentityM(mRotationMatrix, 0);
    	//Matrix.setIdentityM(srmatrix, 0);
    	bindTex();// Load the bitmap into the bound texture.	 
        // GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0); 
         //bitmap.recycle();    
    	
		Matrix.setRotateM(mRotationMatrix , 0, angle, 0, 0, -1.0f);
    	Matrix.multiplyMM(srmatrix,0,mRotationMatrix,0,mProjMatrix,0);
    	
    	Matrix.setIdentityM(tMatrix, 0);
		x=right+left;
    	y=bot+top;
		//Matrix.translateM(tMatrix,0, +x, -y, 0);
		Matrix.multiplyMM(srtmatrix,0,srmatrix,0,tMatrix,0);
		srtmatrix[3]=srtmatrix[3]+x/Assets.targetwidth-1;
		srtmatrix[7]=srtmatrix[7]-y/Assets.targetheight+1;
		GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, srtmatrix, 0);

		GLES20.glUniform1f(ophandle, opacity); 
		    
      	
  		vertices.position(0);
  		GLES20.glVertexAttribPointer(mPositionHandle, 2,GLES20.GL_FLOAT, false,4*2, vertices);
  		bindTexV();
  		   
  		GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);    	
    }
    
    public static void drawRect(int left, int top, int right, int bot, int r, int b, int y){
    }
}
