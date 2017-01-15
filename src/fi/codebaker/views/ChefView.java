package fi.codebaker.views;

import java.io.File;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.ElementTreeSelectionDialog;
import org.eclipse.ui.model.BaseWorkbenchContentProvider;
import org.eclipse.ui.model.WorkbenchLabelProvider;

import fi.codebaker.core.*;

/**
 * This is the Chef subview in the plugin view.
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

public class ChefView implements SelectionListener
{	
	public static final String STATE_STRING = "CodeBaker.CHEFVIEW";
	public static final String DEFAULT_STATE="(filename)\n(path)";

	private static final String INDENT="    ";
	
	private Composite composite;
	private Button chooseButton;
	private Text filenameText;
	private Text pathText;
	private IPath iPath;
		
	public ChefView(Composite parent, String state)
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
	}
	
	public String getStateString()
	{ return filenameText.getText()+"\n"+pathText.getText()+"\n"; }
	
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
		try 
		{
			iPath=((org.eclipse.core.internal.resources.File)result[0]).getLocation();
			return iPath.toFile();
		} catch (Exception e) { return null; }
	}

	private void updateSource(File file)
	{
		if (file==null) return;
		String filename=file.getName();
		filenameText.setText(filename);
		String path=file.getAbsolutePath();
		path=path.substring(0,path.length()-filename.length());
		pathText.setText(path);
	}

	private void updateSource(String path, String filename)
	{ updateSource(new File(path+filename)); }

	public String bake(String text)
	{
		String original=text;
		try
		{
			Log.instance.reset();
			if (!text.startsWith("@"))
			{
				if (!text.startsWith("[")) text="["+text+"]";
				text="@:"+text;
			}
			updateSource(pathText.getText(),filenameText.getText());
			Interpreter interpreter=new Interpreter(pathText.getText());
			String content=interpreter.readFromFile(filenameText.getText());
			interpreter.interpret(new Parser(content));
			interpreter.interpret(new Parser(text));
			return Log.instance.getContent();
		} catch (Exception e) {
			System.out.println(e);
			MessageBox mb=new MessageBox(Display.getDefault().getActiveShell(),SWT.ICON_ERROR);
			mb.setMessage("Failed baking!");
			mb.open();		
			return original;
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
	}
}
