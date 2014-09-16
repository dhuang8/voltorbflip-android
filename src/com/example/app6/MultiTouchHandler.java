package com.example.app6;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.example.app6.Input.TouchEvent;
import com.example.app6.Pool.PoolObjectFactory;

public class MultiTouchHandler implements TouchHandler{
	private static final int MAX_TOUCHPOINTS = 10; 
	   
    boolean[] isTouched = new boolean[MAX_TOUCHPOINTS]; 
    int[] touchX = new int[MAX_TOUCHPOINTS]; 
    int[] touchY = new int[MAX_TOUCHPOINTS]; 
    int[] id = new int[MAX_TOUCHPOINTS]; 
    Pool<TouchEvent> touchEventPool; 
    List<TouchEvent> touchEvents = new ArrayList<TouchEvent>(); 
    List<TouchEvent> touchEventsBuffer = new ArrayList<TouchEvent>(); 
    float scaleX; 
    float scaleY;
    
    public MultiTouchHandler(View view, float scaleX, float scaleY) { 
        PoolObjectFactory<TouchEvent> factory = new PoolObjectFactory<TouchEvent>() { 
            @Override 
            public TouchEvent createObject() { 
                return new TouchEvent(); 
            } 
        }; 
        touchEventPool = new Pool<TouchEvent>(factory, 100); 
        view.setOnTouchListener(this); 
        this.scaleX = scaleX; 
        this.scaleY = scaleY; 
    } 
    public void setScale(float scaleX, float scaleY){
	    this.scaleX = scaleX; 
	    this.scaleY = scaleY; 
	}
    @Override 
    public boolean onTouch(View v, MotionEvent event) {
        synchronized (this) { 
            int action = event.getAction() & MotionEvent.ACTION_MASK; 
            int pointerIndex = (event.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK) 
            		>> MotionEvent.ACTION_POINTER_INDEX_SHIFT; 
            int pointerCount = event.getPointerCount(); 
            //Log.d("pointerCount=",""+pointerCount);
            TouchEvent touchEvent; 
            for (int i = 0; i < MAX_TOUCHPOINTS; i++) { 
                if (i >= pointerCount) { 
                    isTouched[i] = false; 
                    id[i] = -1; 
                    continue; 
                } 
                int pointerId = event.getPointerId(i); 
                if (event.getAction() != MotionEvent.ACTION_MOVE && i != pointerIndex) { 
                    // if it's an up/down/cancel/out event, mask the id to see if we should process it for this touch 
                    // point 
                    continue; 
                } 
                switch (action) { 
                case MotionEvent.ACTION_DOWN: 
                case MotionEvent.ACTION_POINTER_DOWN: 
                    touchEvent = touchEventPool.newObject(); 
                    touchEvent.type = TouchEvent.TOUCH_DOWN; 
                    touchEvent.pointer = pointerId; 
                    touchEvent.x = touchX[i] = (int) (event.getX(i) * scaleX); 
                    touchEvent.y = touchY[i] = (int) (event.getY(i) * scaleY); 
                    isTouched[i] = true; 
                    id[i] = pointerId; 
                    touchEventsBuffer.add(touchEvent); 
                    //touchlist.add(pointerId);
                    break; 
                case MotionEvent.ACTION_UP: 
                case MotionEvent.ACTION_POINTER_UP: 
                case MotionEvent.ACTION_CANCEL: 
                	//Log.d("touch up","1");
                    touchEvent = touchEventPool.newObject(); 
                    touchEvent.type = TouchEvent.TOUCH_UP; 
                    touchEvent.pointer = pointerId; 
                    touchEvent.x = touchX[i] = (int) (event.getX(i) * scaleX); 
                    touchEvent.y = touchY[i] = (int) (event.getY(i) * scaleY); 
                    isTouched[i] = false; 
                    id[i] = -1; 
                    touchEventsBuffer.add(touchEvent); 
                    //touchlist.del(pointerId);
                    break; 
                case MotionEvent.ACTION_MOVE: 
                    touchEvent = touchEventPool.newObject(); 
                    touchEvent.type = TouchEvent.TOUCH_DRAGGED; 
                    touchEvent.pointer = pointerId; 
                    touchEvent.x = touchX[i] = (int) (event.getX(i) * scaleX); 
                    touchEvent.y = touchY[i] = (int) (event.getY(i) * scaleY); 
                    isTouched[i] = true; 
                    id[i] = pointerId; 
                    touchEventsBuffer.add(touchEvent); 
                    break; 
                } 
            } 
            return true; 
        } 
    } 
    public boolean isTouchDown(int pointer) { 
        synchronized (this) { 
            int index = getIndex(pointer); 
            if (index < 0 || index >= MAX_TOUCHPOINTS) {
            	Log.d("pointer:"+pointer,"0:"+id[0]+" 1:"+id[1]+" 2:"+id[2]);
            	//Log.d("index="+index,"index out of bounds");
                return false; }
            else {
            	if (isTouched[index]==false) Log.d("index="+index,"isTouched="+isTouched[index]);
                return isTouched[index]; 
            }
        } 
    } 
    public int getTouchX(int pointer) { 
        synchronized (this) { 
            int index = getIndex(pointer);
            if (index < 0 || index >= MAX_TOUCHPOINTS) 
                return 0; 
            else
                return touchX[index]; 
        } 
    } 
    public int getTouchY(int pointer) { 
        synchronized (this) { 
            int index = getIndex(pointer); 
            if (index < 0 || index >= MAX_TOUCHPOINTS) 
                return 0; 
            else
                return touchY[index]; 
        } 
    } 
    public List<TouchEvent> getTouchEvents() { 
        synchronized (this) { 
            int len = touchEvents.size(); 
            for (int i = 0; i < len; i++) 
                touchEventPool.free(touchEvents.get(i)); 
            touchEvents.clear(); 
            touchEvents.addAll(touchEventsBuffer); 
            touchEventsBuffer.clear(); 
            return touchEvents; 
        } 
    } 
     
    // returns the index for a given pointerId or -1 if no index. 
    private int getIndex(int pointerId) { 
    	synchronized (this) { 
	        for (int i = 0; i < MAX_TOUCHPOINTS; i++) { 
	            if (id[i] == pointerId) { 
	                return i; 
	            } 
	        } 
	        return -1; 
    	}
    } 
}
