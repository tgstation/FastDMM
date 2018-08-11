package com.github.tgstation.fastdmm.objtree;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

public class ObjectTree implements TreeModel {
	public static String macroRegex = "([\\d\\.]+)[ \\t]*(\\+|\\-)[ \\t]*([\\d\\.]+)";
	public HashMap<String,ObjectTreeItem> items = new HashMap<>();
	public String dmePath;

	// List of all FILE_DIR definitions.
	// Linked list because it only really gets used for iteration and I'm too lazy to estimate the directory count
	// So it doesn't reallocate the array a billion times.
	public LinkedList<Path> fileDirs = new LinkedList<>();

	public int icon_size;


	public ObjectTree()
	{
		// Default datums
		
		ObjectTreeItem datum = new ObjectTreeItem(null, "/datum");
		datum.setVar("tag","null");
		addItem(datum);
		
		ObjectTreeItem atom = new ObjectTreeItem(datum, "/atom");
		atom.setVar("alpha", "255");
		atom.setVar("appearance_flags", "0");
		atom.setVar("blend_mode", "0");
		atom.setVar("color", "null");
		atom.setVar("density", "0");
		atom.setVar("desc", "null");
		atom.setVar("dir", "2");
		atom.setVar("gender", "neuter");
		atom.setVar("icon", "null");
		atom.setVar("icon_state", "null");
		atom.setVar("infra_luminosity", "0");
		atom.setVar("invisibility", "0");
		atom.setVar("layer", "1");
		atom.setVar("luminosity", "0");
		atom.setVar("maptext", "null");
		atom.setVar("maptext_width", "32");
		atom.setVar("maptext_height", "32");
		atom.setVar("maptext_x", "0");
		atom.setVar("maptext_y", "0");
		atom.setVar("mouse_drag_pointer", "0");
		atom.setVar("mouse_drop_pointer", "1");
		atom.setVar("mouse_drop_zone", "0");
		atom.setVar("mouse_opacity", "1");
		atom.setVar("mouse_over_pointer", "0");
		atom.setVar("name", "null");
		atom.setVar("opacity", "0");
		atom.setVar("overlays", "list()");
		atom.setVar("override", "0");
		atom.setVar("pixel_x", "0");
		atom.setVar("pixel_y", "0");
		atom.setVar("pixel_z", "0");
		atom.setVar("plane", "0");
		atom.setVar("suffix", "null");
		atom.setVar("transform", "null");
		atom.setVar("underlays", "list()");
		atom.setVar("verbs", "list()");
		addItem(atom);
		
		ObjectTreeItem movable = new ObjectTreeItem(atom, "/atom/movable");
		movable.setVar("animate_movement", "1");
		movable.setVar("bound_x", "0");
		movable.setVar("bound_y", "0");
		movable.setVar("bound_width", "32");
		movable.setVar("bound_height", "32");
		movable.setVar("glide_size", "0");
		movable.setVar("screen_loc", "null");
		movable.setVar("step_size", "32");
		movable.setVar("step_x", "0");
		movable.setVar("step_y", "0");
		addItem(movable);
		
		ObjectTreeItem area = new ObjectTreeItem(atom, "/area");
		area.setVar("layer", "1");
		area.setVar("luminosity", "1");
		addItem(area);
		
		ObjectTreeItem turf = new ObjectTreeItem(atom, "/turf");
		turf.setVar("layer", "2");
		addItem(turf);
		
		ObjectTreeItem obj = new ObjectTreeItem(movable, "/obj");
		obj.setVar("layer", "3");
		addItem(obj);
		
		ObjectTreeItem mob = new ObjectTreeItem(movable, "/mob");
		mob.setVar("ckey", "null");
		mob.setVar("density", "1");
		mob.setVar("key", "null");
		mob.setVar("layer", "4");
		mob.setVar("see_in_dark", "2");
		mob.setVar("see_infrared", "0");
		mob.setVar("see_invisible", "0");
		mob.setVar("sight", "0");
		addItem(mob);
		
		ObjectTreeItem world = new ObjectTreeItem(datum, "/world");
		world.setVar("turf", "/turf");
		world.setVar("mob", "/mob");
		world.setVar("area", "/area");

		// Empty path, this will be resolved as project root by filePath.
		fileDirs.add(Paths.get(""));
	}

	public ObjectTreeItem getOrCreate(String path) {
		if(items.containsKey(path))
			return items.get(path);
		
		String parentPath;
		if(path.indexOf("/") != path.lastIndexOf("/"))
			parentPath = path.substring(0, path.lastIndexOf("/"));
		else
			parentPath = "/datum";
		ObjectTreeItem parentItem = getOrCreate(parentPath);
		ObjectTreeItem item = new ObjectTreeItem(parentItem, path);
		items.put(path, item);
		return item;
	}
	
	public ObjectTreeItem get(String path) {
		if(items.containsKey(path))
			return items.get(path);
		else
			return null;
	}
	
	public void addItem(ObjectTreeItem item)
	{
		items.put(item.path, item);
	}
	
	public void dumpTree(PrintStream ps)
	{
		for(ObjectTreeItem item : items.values())
		{
			ps.println(item.path);
			for(Entry<String, String> var : item.vars.entrySet())
			{
				ps.println("\t" + var.getKey() + " = " + var.getValue());
			}
		}
	}
	
	public ObjectTreeItem getGlobal() {
		return items.get("");
	}


	public void completeTree() {
		// Clear children and parse expressions
		
		ObjectTreeItem global = getGlobal();
		
		System.gc();
		
		for(ObjectTreeItem i : items.values()) {
			i.subtypes.clear();
			
			for(Entry<String, String> e : i.vars.entrySet()) {
				String val = e.getValue();
				String origVal = "";
				try {
				while(!origVal.equals(val)) {
					origVal = val;
					// Trust me, this is the fastest way to parse the macros.
					Matcher m = Pattern.compile("(?<![\\d\\w\"/])\\w+(?![\\d\\w\"/])").matcher(val);
					StringBuffer outVal = new StringBuffer();
					while(m.find()) {
						if(global.vars.containsKey(m.group(0)))
							m.appendReplacement(outVal, global.vars.get(m.group(0)));
						else
							m.appendReplacement(outVal, m.group(0));
					}
					m.appendTail(outVal);
					val = outVal.toString();
					
					// Parse additions/subtractions.
					m = Pattern.compile(macroRegex).matcher(val);
					outVal = new StringBuffer();
					while(m.find()) 
					{
						switch(m.group(2)) 
						{						
							// If group1 or group3 is a period then this is definitely not a macro and just an eager match.
							// Didn't feel like fixing the regex above. So this is a temporary fix. -Rockdtben
							case "+":
								if (!m.group(1).equals(".") && !m.group(3).equals("."))
									m.appendReplacement(outVal, (Float.parseFloat(m.group(1)) + Float.parseFloat(m.group(3)))+"");
								break;
							case "-":
								if (!m.group(1).equals(".") && !m.group(3).equals("."))
									m.appendReplacement(outVal, (Float.parseFloat(m.group(1)) - Float.parseFloat(m.group(3)))+"");
								break;
						}
					}
					m.appendTail(outVal);
					val = outVal.toString();
					
					// Parse parentheses
					m = Pattern.compile("\\(([\\d\\.]+)\\)").matcher(val);
					outVal = new StringBuffer();
					while(m.find()) {
						m.appendReplacement(outVal, m.group(1));
					}
					m.appendTail(outVal);
					val = outVal.toString();
				}
				} catch (OutOfMemoryError ex) {
					System.err.println("OUT OF MEMORY PROCESSING ITEM " + i.typeString() + " VAR " + e.getKey() + " = " + e.getValue());
					throw ex;
				}
				
				i.setVar(e.getKey(), val);
			}
		}
		System.gc();
		// Assign parents/children
		for(ObjectTreeItem i : items.values()) {
			ObjectTreeItem parent = get(i.getVar("parentType"));
			if(parent != null) {
				i.parent = parent;
				parent.subtypes.add(i);
			}
		}
		System.gc();
		// Sort children
		for(ObjectTreeItem i : items.values()) {
			i.subtypes.sort((arg0, arg1) -> arg0.path.compareToIgnoreCase(arg1.path));
		}
		
		try {
			icon_size = Integer.parseInt(get("/world").getVar("icon_size"));
		} catch(NumberFormatException e) {
			icon_size = 32;
		}
	}
	
	@Override
	public void addTreeModelListener(TreeModelListener arg0) {
		// We don't change.
	}

	@Override
	public Object getChild(Object arg0, int arg1) {
		if(arg0 == this) {
			switch(arg1) {
			case 0:
				return get("/area");
			case 1:
				return get("/mob");
			case 2:
				return get("/obj");
			case 3:
				return get("/turf");
			}
		} else if (arg0 instanceof ObjectTreeItem) {
			ObjectTreeItem item = (ObjectTreeItem)arg0;
			return item.subtypes.get(arg1);
		}
		return null;
	}

	@Override
	public int getChildCount(Object arg0) {
		if(arg0 == this)
			return 4;
		if(arg0 instanceof ObjectTreeItem) {
			return ((ObjectTreeItem)arg0).subtypes.size();
		}
		return 0;
	}

	@Override
	public int getIndexOfChild(Object arg0, Object arg1) {
		if(!(arg1 instanceof ObjectTreeItem))
			return 0;
		ObjectTreeItem item = (ObjectTreeItem)arg1;
		if(arg0 == this) {
			switch(item.path) {
			case "/area":
				return 0;
			case "/mob":
				return 1;
			case "/obj":
				return 2;
			default:
				return 3;
			}
		}
		if(arg0 instanceof ObjectTreeItem)
			return ((ObjectTreeItem)arg0).subtypes.indexOf(arg1);
		return 0;
	}

	@Override
	public Object getRoot() {
		return this;
	}

	@Override
	public boolean isLeaf(Object arg0) {
		if(arg0 == this)
			return false;
		if(arg0 instanceof ObjectTreeItem)
			return ((ObjectTreeItem)arg0).subtypes.size() == 0;
		return true;
	}

	@Override
	public void removeTreeModelListener(TreeModelListener arg0) {
		// We don't change
	}

	@Override
	public void valueForPathChanged(TreePath arg0, Object arg1) {
		// Nope
	}
	
	public String toString() {
		return dmePath;
	}

	/**
	 * Gets a file name, taking the project's FILE_DIR into account.
	 * @param fileName The relative name of the file, which should be found with FILE_DIR definitions.
	 * @return The relative file path from the root project folder (folder with the opened code file).
	 * @exception FileNotFoundException Thrown if the file name could not be resolved.
	 */
	public String filePath(String filePath) throws FileNotFoundException {
		for(Path path : fileDirs) {
			Path newPath = path.resolve(filePath);
			Path rootPath = Paths.get(dmePath).getParent();
			File newFile = rootPath.resolve(newPath).toFile();

			// Ding ding ding we got a winner!
			if(newFile.exists() && newFile.canRead()) {
				return newPath.toString();
			}
		}
		throw new FileNotFoundException();
	}
}
