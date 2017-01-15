package fi.codebaker.views;

import org.eclipse.swt.widgets.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;

import fi.codebaker.core.*;

/**
 * This is the Tag subview in the plugin view.
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

public class TagView implements SelectionListener
{
	private static final String INDENT="    ";
	
	private Composite composite;
	private Text labelText;
	private Text separatorText;
	private Text parametersText;
	private Text tagText;
	private Button generateButton;
		
	public TagView(Composite parent)
	{
		composite=new Composite(parent,SWT.NONE);
		composite.setLayout(new GridLayout(3,false));
		
		GridData gd=new GridData();
		gd.widthHint=100;
		addLabel(INDENT);
		addLabel("Label:");
		labelText=new Text(composite, SWT.BORDER | SWT.LEFT);
		labelText.setText("LABEL");
		labelText.setLayoutData(gd);
		
		addLabel(INDENT);
		addLabel("Separator: ");
		separatorText=new Text(composite, SWT.BORDER | SWT.LEFT);
		separatorText.setText(",");
		separatorText.setLayoutData(gd);

		addLabel(INDENT);
		addLabel("Parameters: ");
		parametersText=new Text(composite, SWT.BORDER | SWT.LEFT);
		parametersText.setText("p1,p2");
		parametersText.setLayoutData(gd);

		gd=new GridData();
		gd.widthHint=300;
		addLabel(INDENT);
		generateButton=new Button(composite,SWT.NONE);
		generateButton.setText("Generate");
		generateButton.addSelectionListener(this);
		tagText=new Text(composite, SWT.BORDER | SWT.LEFT | SWT.READ_ONLY);
		tagText.setText("(to be generated)");
		tagText.setLayoutData(gd);
	}
	
	private void addLabel(String value)
	{
		Label label=new Label(composite,SWT.LEFT);
		label.setText(value);
	}
	
	@Override
	public void widgetDefaultSelected(SelectionEvent event) 
	{ /*no default selection*/ }

	@Override
	public void widgetSelected(SelectionEvent event) 
	{
		String[] parameters=parametersText.getText().split(",");
		String tag=Tag.LEFT_BRACKET+labelText.getText()+Tag.SEPARATOR+separatorText.getText()+Tag.SEPARATOR;
		for (int i=0; i<parameters.length; i++)
			tag+=(i==0?"":" ")+Dictionary.LEFT_BRACKET+parameters[i].trim()+Dictionary.RIGHT_BRACKET;
		tag+=Tag.RIGHT_BRACKET;
		tagText.setText(tag);
		composite.pack();
	}
}
