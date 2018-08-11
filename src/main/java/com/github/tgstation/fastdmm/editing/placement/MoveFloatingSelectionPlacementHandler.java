package com.github.tgstation.fastdmm.editing.placement;

import java.util.Set;

import com.github.tgstation.fastdmm.FastDMM;
import com.github.tgstation.fastdmm.dmirender.RenderInstance;
import com.github.tgstation.fastdmm.dmmmap.Location;
import com.github.tgstation.fastdmm.objtree.ObjInstance;

public class MoveFloatingSelectionPlacementHandler implements PlacementHandler {
	FastDMM editor;
	Location lastLoc;
	@Override
	public void init(FastDMM editor, ObjInstance instance, Location initialLocation) {
		this.editor = editor;
		this.lastLoc = initialLocation;
	}

	@Override
	public void dragTo(Location location) {
		if(!(editor.placementMode instanceof SelectPlacementMode))
			return;
		SelectPlacementMode mode = (SelectPlacementMode)editor.placementMode;
		if(mode.floatSelect == null)
			return;
		mode.floatSelect.x += location.x - lastLoc.x;
		mode.floatSelect.y += location.y - lastLoc.y;
		lastLoc = location;
	}

	@Override
	public void finalizePlacement() {
	}

	@Override
	public int visualize(Set<RenderInstance> rendInstanceSet, int currCreationIndex) {
		return currCreationIndex;
	}

}
