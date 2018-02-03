package com.github.monster860.fastdmm.gui.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Objects;

import javax.swing.JOptionPane;

import org.apache.commons.io.FileUtils;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.github.monster860.fastdmm.FastDMM;
import com.google.gson.Gson;

public class FastDMMOptionsModel {
	private static String optionsPath = System.getProperty("user.home") + File.separator + ".fastdmm" + File.separator
			+ "options.json";
	
	public boolean autoSave = false;
	
	//Restriction constructor to createOrLoadOptions.
	private FastDMMOptionsModel() {	}
	
	
	public static FastDMMOptionsModel createOrLoadOptions() {
		File optionsFile = new File(optionsPath);
		
		if (optionsFile.exists())
		{
			String json = "";
			try {
				json = FileUtils.readFileToString(optionsFile);
			} catch (FileNotFoundException e) {
				StringWriter sw = new StringWriter();
				PrintWriter pw = new PrintWriter(sw);
				e.printStackTrace(pw);
				JOptionPane.showMessageDialog(FastDMM.getFastDMM(), sw.getBuffer(), "Error", JOptionPane.ERROR_MESSAGE);
			} catch (IOException e) {
				StringWriter sw = new StringWriter();
				PrintWriter pw = new PrintWriter(sw);
				e.printStackTrace(pw);
				JOptionPane.showMessageDialog(FastDMM.getFastDMM(), sw.getBuffer(), "Error", JOptionPane.ERROR_MESSAGE);
			}
			Gson gson = new Gson();
			FastDMMOptionsModel options = gson.fromJson(json, FastDMMOptionsModel.class);
			return options;
		}
		else
		{
			FastDMMOptionsModel options = new FastDMMOptionsModel();
			//Save Options to file.
			options.saveOptions();
			return options;			
		}		
	}
	
	public void saveOptions()
	{
		File optionsFile = new File(optionsPath);
		Gson gson = new Gson();
		String optionsJson = gson.toJson(this);
		try {
			FileUtils.writeStringToFile(optionsFile, optionsJson);
		} catch (IOException e) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			JOptionPane.showMessageDialog(FastDMM.getFastDMM(), sw.getBuffer(), "Error", JOptionPane.ERROR_MESSAGE);
		}	
	}
}
