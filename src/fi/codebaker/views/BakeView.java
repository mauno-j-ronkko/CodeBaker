package fi.codebaker.views;

import org.eclipse.swt.widgets.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.ui.part.*;
import org.eclipse.ui.dialogs.ElementTreeSelectionDialog;
import org.eclipse.ui.model.*;

import java.io.File;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.viewers.*;

import fi.codebaker.core.*;

/**
 * This is the Bake subview in the plugin view.
 * 
 * Copyright (c) 2013-1016 Mauno Rönkkö
 * 
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
 * CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 * 
 * @author Mauno Rönkkö
 *
 */
public class BakeView implements SelectionListener
{	
	public static final String STATE_STRING = "CodeBaker.BAKEVIEW";
	public static final String DEFAULT_STATE="(filename)\n(path)";
	
	private static final String INDENT="    ";
	
	private Composite composite;
	private Button chooseButton;
	private Text filenameText;
	private Text pathText;
	private Text resultText;
	private Button bakeButton;
		
	public BakeView(Composite parent, String state)
	{
		String field[]=state.split("\n");
		
		composite=new Composite(parent,SWT.NONE);
		composite.setLayout(new GridLayout(3,false));
		
		GridData gd=new GridData();
		gd.widthHint=100;
		addLabel(INDENT);
		chooseButton=new Button(composite,SWT.NONE);
		chooseButton.setText("Choose ");
		chooseButton.addSelectionListener(this);
		filenameText=new Text(composite, SWT.BORDER | SWT.LEFT | SWT.READ_ONLY);
		filenameText.setText(field[0].trim());
		filenameText.setLayoutData(gd);
		
		gd=new GridData();
		gd.widthHint=300;
		addLabel(INDENT);
		addLabel(INDENT);
		pathText=new Text(composite, SWT.BORDER | SWT.LEFT | SWT.READ_ONLY);
		pathText.setText(field[1].trim());
		pathText.setLayoutData(gd);

		gd=new GridData();
		gd.widthHint=300;
		gd.heightHint=150;
		addLabel(INDENT);
		bakeButton=new Button(composite,SWT.LEFT);
		bakeButton.setText("Bake");
		bakeButton.addSelectionListener(this);
		bakeButton.setLayoutData(new GridData(SWT.FILL,SWT.TOP,false,false));
		resultText=new Text(composite, SWT.BORDER | SWT.LEFT | SWT.READ_ONLY | SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);
		resultText.setText("(to be generated)");
		resultText.setLayoutData(gd);
	}
	
	public String getStateString()
	{ return filenameText.getText()+"\n"+pathText.getText(); }
	
	private void addLabel(String value)
	{
		Label label=new Label(composite,SWT.LEFT);
		label.setText(value);
	}
	
	private File chooseFile()
	{
		ElementTreeSelectionDialog dialog = new ElementTreeSelectionDialog(null /*window.getShell(),*/, new WorkbenchLabelProvider(), new BaseWorkbenchContentProvider());
		dialog.setTitle("Code Bakery");
		dialog.setMessage("Select the source file:");
		dialog.addFilter(new BakeryFilter()); 		
		dialog.setInput(ResourcesPlugin.getWorkspace().getRoot());
		dialog.open();
		Object[] result=dialog.getResult();
		try { return ((org.eclipse.core.internal.resources.File)result[0]).getLocation().toFile(); } 
		catch (Exception e) { return null; }
	}

	private void updateSource(File file)
	{
		if (file==null) return;
		String name=file.getName();
		filenameText.setText(name);
		String path=file.getAbsolutePath();
		pathText.setText(path.substring(0,path.length()-name.length()));
	}
	
	private String bakeIt()
	{
		try
		{
			Log.instance.reset();
			String path=pathText.getText();
			String filename=filenameText.getText();
			IPath iPath=Path.fromOSString(path+filename);
			Interpreter interpreter=new Interpreter(path);
			String content=interpreter.readFromFile(filename);
			interpreter.interpret(new Parser(content));
			ResourcesPlugin.getWorkspace().getRoot().getContainerForLocation(iPath).getProject().refreshLocal(IResource.DEPTH_INFINITE, null);
			return Log.instance.getContent();
		} catch (Exception e) {
			MessageBox mb=new MessageBox(Display.getDefault().getActiveShell(),SWT.ICON_ERROR);
			mb.setMessage("Failed baking!");
			mb.open();		
			return "*** ERROR ***\n"+e.getMessage()+"\n"+Log.instance.getContent();
		}
	}
	
	@Override
	public void widgetDefaultSelected(SelectionEvent event) 
	{ /*no default selection*/ }

	@Override
	public void widgetSelected(SelectionEvent event) 
	{
		if (event.getSource()==chooseButton)
			updateSource(chooseFile());
		else if (event.getSource()==bakeButton)
			resultText.setText(bakeIt());
	}
}
