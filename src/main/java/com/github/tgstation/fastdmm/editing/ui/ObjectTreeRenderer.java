package com.github.tgstation.fastdmm.editing.ui;

import java.awt.Component;

import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

import com.github.tgstation.fastdmm.FastDMM;
import com.github.tgstation.fastdmm.dmirender.DMI;
import com.github.tgstation.fastdmm.dmirender.IconSubstate;
import com.github.tgstation.fastdmm.objtree.ObjectTreeItem;

public class ObjectTreeRenderer extends DefaultTreeCellRenderer {
	private static final long serialVersionUID = 931493078348635512L;
	public FastDMM fdmm;
	
	public ObjectTreeRenderer(FastDMM fdmm) {
		this.fdmm = fdmm;
	}
	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
		super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
		if(value instanceof ObjectTreeItem) {
			ObjectTreeItem item = (ObjectTreeItem)value;
			setText(item.parentlessName());
			setToolTipText(item.path);
			
			DMI dmi = fdmm.getDmi(item.getIcon(), false);
			if(dmi != null) {
				String iconState = item.getIconState();
				IconSubstate substate = dmi.getIconState(iconState).getSubstate(item.getDir());
				setIcon(substate.getScaled());
			} else {
				setIcon(null);
			}
		}
		
		return this;
	}
}
