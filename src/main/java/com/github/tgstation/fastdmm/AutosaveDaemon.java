package com.github.tgstation.fastdmm;

import javax.swing.*;
import java.io.FileNotFoundException;

public class AutosaveDaemon implements Runnable
{
    public AutosaveDaemon()
    {
    }

    @Override
    public void run()
    {
    	FastDMM editor = FastDMM.getFastDMM();
        while(true)
        {
            try
            {
                Thread.sleep(60000);
                
                //Autosave only if enabled.
                if (editor.options.autoSave)
                {
	                synchronized(editor)
	                {
	                    editor.loadedMaps.forEach((dmm) ->
	                    {
	                        try
	                        {
	                            dmm.save();
	                            
	                            //TODO: Add some Console like panel to report stuff like this to the user. "Map AutoSaved"
	                        }
	                        catch(FileNotFoundException e)
	                        {
	                            JOptionPane.showMessageDialog(editor, "Got FileNotFoundException while saving " + dmm.file.getName(), "Error", JOptionPane.ERROR_MESSAGE);
	                        }
	                    });
	                }
                }
            }
            catch(InterruptedException e)
            {
                Thread.currentThread().interrupt();
            }
        }
    }
}
