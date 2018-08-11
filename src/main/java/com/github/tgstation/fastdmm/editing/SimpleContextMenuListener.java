package com.github.tgstation.fastdmm.editing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.github.tgstation.fastdmm.FastDMM;
import com.github.tgstation.fastdmm.dmmmap.Location;
import com.github.tgstation.fastdmm.objtree.ObjInstance;

public abstract class SimpleContextMenuListener implements ActionListener {
	
	protected FastDMM editor;
	protected Location location;
	protected ObjInstance oInstance;
	
	private String oldkey;
	private String newkey;
	
	public SimpleContextMenuListener(FastDMM editor, Location mapLocation, ObjInstance instance) {
		this.editor = editor;
		this.location = mapLocation;
		this.oInstance = instance;
	}
	
	public abstract void doAction();
	
	public void actionPerformed(ActionEvent e) {
		if(editor.dmm == null)
			return;
		doAction();
		editor.addToUndoStack(editor.dmm.popDiffs());
	}
	
}
