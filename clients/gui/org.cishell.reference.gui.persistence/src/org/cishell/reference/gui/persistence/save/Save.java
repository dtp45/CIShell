package org.cishell.reference.gui.persistence.save;

import java.io.File;
import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.data.Data;
import org.cishell.service.conversion.Converter;
import org.cishell.service.conversion.DataConversionService;
import org.cishell.service.guibuilder.GUIBuilderService;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

/**
 * Save algorithm for persisting a data object
 * 
 * @author bmarkine
 */
public class Save implements Algorithm {
    Data[] data;
    Dictionary parameters;
    CIShellContext context;
    
    final Shell parentShell;
    
    private GUIBuilderService guiBuilder;    
    private DataConversionService conversionManager;
    
    /**
     * Sets up default services for the algorithm
     * 
     * @param data The data array to persist
     * @param parameters Parameters for the algorithm
     * @param context Provides services to CIShell services
     */
    public Save(Data[] data, Dictionary parameters, CIShellContext context) {
        this.data = data;
        this.parameters = parameters;
        this.context = context;
        
        this.parentShell = PlatformUI.getWorkbench().getWorkbenchWindows()[0].getShell();

        this.conversionManager = (DataConversionService) context.getService(
        		DataConversionService.class.getName());
        
        this.guiBuilder = (GUIBuilderService)context.getService(GUIBuilderService.class.getName());
    }

    /**
     * Executes the algorithm
     * 
     * @return Null for this algorithm
     */
    public Data[] execute() {
    	//This only checks the first Data in the array
    	final Converter[] converters = conversionManager.findConverters(data[0], "file-ext:*");

    	if (converters.length < 1 && !(data[0].getData() instanceof File)) {
    		guiBuilder.showError("No Converters", 
    				"No valid converters for data type: " + 
    				data[0].getData().getClass().getName(), 
    				"Please install a plugin that will save the data type to a file");
    	}
    	else {
    		if (!parentShell.isDisposed()) {
    			guiRun(new Runnable() {
    				public void run() {
    					if (converters.length == 0) {
    						//TODO: finish this
    						//final FileSaver saver = new FileSaver(parentShell, context);
                            //saver.save(null, data[0]);
    					} else if (converters.length == 1) {
                            final FileSaver saver = new FileSaver(parentShell, context);
                            saver.save(converters[0], data[0]);
                        } else {
                            SaveDataChooser sdc = new SaveDataChooser(data[0],
                                    parentShell, converters,
                                    "Save",
                                    context);
                            sdc.createContent(new Shell(parentShell));
                            sdc.open(); 
                        }
    				}});
    		}
    	}
        return null;
    }
    
    private void guiRun(Runnable run) {
        if (Thread.currentThread() == Display.getDefault().getThread()) {
            run.run();
        } else {
            parentShell.getDisplay().syncExec(run);
        }
    }
}