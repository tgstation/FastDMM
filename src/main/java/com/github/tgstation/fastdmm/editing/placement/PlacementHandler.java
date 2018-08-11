package com.github.tgstation.fastdmm.editing.placement;

import java.util.Set;

import com.github.tgstation.fastdmm.FastDMM;
import com.github.tgstation.fastdmm.dmirender.RenderInstance;
import com.github.tgstation.fastdmm.dmmmap.Location;
import com.github.tgstation.fastdmm.objtree.ObjInstance;

public interface PlacementHandler {
	// Called when the mouse button becomes pressed.
	public void init(FastDMM editor, ObjInstance instance, Location initialLocation);
	// Called when mouse moves to a different tile.
	public void dragTo(Location location);
	// Called when the mouse button become depressed.
	public void finalizePlacement();
	// Called every frame when mouse button is pressed to 
	public int visualize(Set<RenderInstance> rendInstanceSet, int currCreationIndex);
}
