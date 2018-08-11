package com.github.tgstation.fastdmm.editing;

import com.github.tgstation.fastdmm.FastDMM;
import com.github.tgstation.fastdmm.dmmmap.Location;
import com.github.tgstation.fastdmm.dmmmap.TileInstance;
import com.github.tgstation.fastdmm.objtree.ObjInstance;

public class DeleteListener extends SimpleContextMenuListener {
	
	public DeleteListener(FastDMM editor, Location mapLocation, ObjInstance instance) {
		super(editor, mapLocation, instance);
	}
	
	@Override
	public void doAction() {
		synchronized(editor) {
			TileInstance ti = editor.dmm.instances.get(editor.dmm.map.get(location));
			if(ti == null)
				return;
			
			String newKey = ti.removeObject(oInstance);
			
			editor.dmm.putMap(location, newKey);
		}
	}
}
