package com.github.tgstation.fastdmm.editing;

public interface Undoable {
	public boolean undo();
	public boolean redo();
}
