package com.example.app6;

public abstract class Screen{
	protected final GLGraphics game; 
	public Screen(GLGraphics game) { 
		this.game = game; 
	} 
	public abstract void update(float deltaTime); 
	public abstract void present(float deltaTime); 
	public abstract void pause(); 
	public abstract void resume(); 
	public abstract void dispose(); 
} 