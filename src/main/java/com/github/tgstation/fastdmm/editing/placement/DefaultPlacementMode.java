package com.github.tgstation.fastdmm.editing.placement;

import java.util.Set;

import javax.swing.JPopupMenu;

import com.github.tgstation.fastdmm.FastDMM;
import com.github.tgstation.fastdmm.dmirender.RenderInstance;
import com.github.tgstation.fastdmm.dmmmap.Location;
import com.github.tgstation.fastdmm.dmmmap.TileInstance;
import com.github.tgstation.fastdmm.objtree.ObjInstance;

public class DefaultPlacementMode implements PlacementMode {

	@Override
	public PlacementHandler getPlacementHandler(FastDMM editor, ObjInstance instance, Location initialLocation) {
		if(instance == null)
			return null;
		
		if(editor.isCtrlPressed)
			return new DirectionalPlacementHandler();
		else if(editor.isShiftPressed)
			return new BlockPlacementHandler();
		else
			return new DefaultPlacementHandler();
	}

	@Override
	public int visualize(Set<RenderInstance> rendInstanceSet, int currCreationIndex) {
		return currCreationIndex;
	}

	@Override
	public void addToTileMenu(FastDMM editor, Location mapLocation, TileInstance instance, JPopupMenu menu) {
	}

	@Override
	public void flush(FastDMM editor) {
	}

}
