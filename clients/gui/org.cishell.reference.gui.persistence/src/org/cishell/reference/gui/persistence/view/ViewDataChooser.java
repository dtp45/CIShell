package org.cishell.reference.gui.persistence.view;

import java.io.File;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.data.Data;
import org.cishell.reference.gui.persistence.save.SaveDataChooser;
import org.cishell.service.conversion.Converter;
import org.eclipse.swt.widgets.Shell;

/* 
 * @author Weixia(Bonnie) Huang (huangb@indiana.edu) 
 * 
 */
public class ViewDataChooser extends SaveDataChooser {
	private File tempFile;
	boolean isSaved = false;
	Data theData;
	
	public ViewDataChooser(String title, File tempFile, Shell parent, 
					Data data, Converter[] converters, CIShellContext context){
		super (data, parent, converters, title, context);
		
		this.tempFile = tempFile;
		this.theData = data;
	}

	protected void selectionMade(int selectedIndex){
		try {
        getShell().setVisible(false);
        final Converter converter = converterArray[selectedIndex];
        Data newData = converter.convert(theData);    
        isSaved = FileView.copy((File)newData.getData(), tempFile);
      	close(true);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public boolean isSaved(){
		return isSaved;
	}
	

}