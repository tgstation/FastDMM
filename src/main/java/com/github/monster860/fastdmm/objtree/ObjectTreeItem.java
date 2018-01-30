package com.github.monster860.fastdmm.objtree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

public class ObjectTreeItem extends ObjInstance implements ListModel<ObjInstance> {
	public ObjectTreeItem(ObjectTreeItem parent, String path)
	{
		path = path.trim();
		this.path = path;
		this.parent = parent;
		vars.put("type", path);
		if(parent != null)
		{
			parent.subtypes.add(this);
			vars.put("parentType", parent.path);
		}
		instances.add(this);
	}
	
	public ObjectTreeItem(String path)
	{
		this.path = path;
		vars.put("type", path);
		instances.add(this);
	}
	
	public boolean istype(String path) {
		if(this.path.equals(path))
			return true;
		if(parent != null)
			return parent.istype(path);
		return false;
	}
	
	public void setVar(String key, String value)
	{
		vars.put(key, value);
	}
	
	public void setVar(String key)
	{
		if(!vars.containsKey(key))
			vars.put(key, "null");
	}
	
	public String getVar(String key)
	{
		if(vars.containsKey(key))
			return vars.get(key);
		if(parent != null)
			return parent.getVar(key);
		return null;
	}
	
	public Map<String, String> getAllVars() {
		Map<String, String> allVars = new TreeMap<>();
		if(parent != null)
			allVars.putAll(parent.getAllVars());
		allVars.putAll(vars);
		return allVars;
	}
	
	public String path = "";
	public ArrayList<ObjectTreeItem> subtypes = new ArrayList<>();
	public ObjectTreeItem parent = null;
	public Map<String, String> vars = new TreeMap<>();
	public List<ObjInstance> instances = new ArrayList<>();
	
	public void addInstance(ObjInstance instance) {
		if(instances.contains(instance))
			return;
		instances.add(instance);
		Collections.sort(instances, (o1, o2) -> {
            if(o1 instanceof ObjectTreeItem)
                return -1;
            if(o2 instanceof ObjectTreeItem)
                return 1;
            return o1.toString().compareToIgnoreCase(o2.toString());
        });
		int index = instances.indexOf(instance);
		ListDataEvent event = new ListDataEvent(this, ListDataEvent.INTERVAL_ADDED, index, index);
		for(ListDataListener l : listeners) {
			l.intervalAdded(event);
		}
	}
	
	public void removeInstance(ObjInstance instance) {
		int index = instances.indexOf(instance);
		if(index == -1)
			return;
		instances.remove(instance);
		ListDataEvent event = new ListDataEvent(this, ListDataEvent.INTERVAL_REMOVED, index, index);
		for(ListDataListener l : listeners) {
			l.intervalRemoved(event);
		}
	}
	
	public String parentlessName() {
		if(path.startsWith(parent.path))
			return path.substring(parent.path.length());
		else
			return path;
	}

	@Override
	public String typeString() {
		return path;
	}
	
	@Override
	public String toString() {
		return path;
	}
	
	@Override
	public String toStringTGM() {
		return path;
	}
	
	private HashSet<ListDataListener> listeners = new HashSet<>();

	@Override
	public void addListDataListener(ListDataListener arg0) {
		listeners.add(arg0);
	}

	@Override
	public ObjInstance getElementAt(int arg0) {
		// TODO Auto-generated method stub
		return instances.get(arg0);
	}

	@Override
	public int getSize() {
		// TODO Auto-generated method stub
		return instances.size();
	}

	@Override
	public void removeListDataListener(ListDataListener arg0) {
		listeners.remove(arg0);
	}

}

