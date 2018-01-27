/*
 * KeyboardAdapter. This keeps track of key state and needs to be updated every tick in the Update() method.
 * 
 */

package com.github.monster860.fastdmm;

import java.util.HashMap;

import org.lwjgl.input.Keyboard;

public class KeyboardAdapter {
	public static HashMap<Integer, Boolean> keyState = new HashMap<Integer, Boolean>();
	public static HashMap<Integer, Boolean> prevKeyState = new HashMap<Integer, Boolean>();
	
	public static void updateKeys() {
		prevKeyState = keyState;
		keyState = (HashMap<Integer, Boolean>) keyState.clone();
		while (Keyboard.next()) {
			if (keyState.containsKey(Keyboard.getEventKey()))
			{
				if (Keyboard.getEventKeyState())
					keyState.replace(Keyboard.getEventKey(), true);
				else
					keyState.replace(Keyboard.getEventKey(), false);
			}
			else
			{
				keyState.put(Keyboard.getEventKey(), true);
			}
		}
	}
	
	// Key is pressed down.
	public static boolean isKeyDown(int keyCode)
	{
		return keyState.getOrDefault(keyCode, false);
	}
	
	//Key is not in action.
	public static boolean isKeyUp(int keyCode)
	{
		return !keyState.getOrDefault(keyCode, false);
	}
	
	//Triggers when a key is pressed and wasn't held before
	public static boolean isKeyPressed(int keyCode)
	{
		return keyState.getOrDefault(keyCode, false) && !prevKeyState.getOrDefault(keyCode,false);
	}
	
	//Was Pressed now is released
	public static boolean isKeyDePressed(int keyCode)
	{
		return !keyState.getOrDefault(keyCode, false) && prevKeyState.getOrDefault(keyCode,false);
	}
}
