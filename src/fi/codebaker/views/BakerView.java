package fi.codebaker.views;

import org.eclipse.swt.widgets.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.ui.part.*;
import org.eclipse.ui.texteditor.AbstractTextEditor;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.dialogs.ElementTreeSelectionDialog;
import org.eclipse.ui.model.*;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.jface.viewers.*;
import java.util.Base64;

/**
 * This is the main plugin view.
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
public class BakerView extends ViewPart 
{
	public static final String ID = "codebaker.views.BakerView";

	public static BakerView instance;
	
	private IMemento memento;

	private TagView tagView;
	private ChefView chefView;
	private BakeView bakeView;
	
	public BakerView() 
	{ 
		super(); 
		instance=this;
	}
	
	public void setFocus() 
	{ }
	
	public void init(IViewSite site, IMemento memento) throws PartInitException 
	{
		super.init(site, memento);
		this.memento = memento;
	}

	private void addLabel(Composite parent, String value)
	{
		Label label=new Label(parent,SWT.LEFT);
		label.setText(value);
	}
	
	private String getMemento(String name, String defaultState)
	{
		if (memento==null) return defaultState;
		String selection=memento.getString(name);
		return (selection==null) ? defaultState : selection;
	}
	
	public void createPartControl(Composite parent) 
	{
		Composite composite=new Composite(parent,SWT.NONE);
		composite.setLayout(new RowLayout());
		
		Composite tcComposite=new Composite(composite,SWT.NONE);
		tcComposite.setLayout(new GridLayout(1,false));
		addLabel(tcComposite,"Tag Assistant:");
		tagView=new TagView(tcComposite);
		addLabel(tcComposite," ");
		addLabel(tcComposite,"Chef Assistant:");
		chefView=new ChefView(tcComposite,getMemento(ChefView.STATE_STRING,ChefView.DEFAULT_STATE));
		addLabel(tcComposite, " ");

		Composite bakerComposite=new Composite(composite,SWT.TOP);
		bakerComposite.setLayout(new GridLayout(1,false));
		addLabel(bakerComposite,"Baker Assistant:");
		bakeView=new BakeView(bakerComposite,getMemento(BakeView.STATE_STRING,BakeView.DEFAULT_STATE));
		Label label=new Label(bakerComposite,SWT.RIGHT);
		label.setText("\u00A9 Mauno R\u00F6nkk\u00F6, 2016; Licensed under X11 license.");
		label.setLayoutData(new GridData(SWT.RIGHT,SWT.NONE,true,false));
		
		composite.pack();
	}
	
	public void saveState(IMemento memento) 
	{
		super.saveState(memento);
		memento.putString(BakeView.STATE_STRING, bakeView.getStateString());
		memento.putString(ChefView.STATE_STRING, chefView.getStateString());
	}
	
    public void chefBake(IEditorPart editor) 
    {
    	try
    	{
    		AbstractTextEditor textEditor=(AbstractTextEditor)editor;
    		TextSelection selection=(TextSelection)textEditor.getSelectionProvider().getSelection();
    		if (selection.getLength()==0)
    		{
    			MessageBox mb=new MessageBox(Display.getDefault().getActiveShell(),SWT.ICON_INFORMATION);
    			mb.setMessage("Select text for baking!");
    			mb.open();		
    		} else {
    			String text=selection.getText();
    			String baked=chefView.bake(text);
    			IDocumentProvider dp=textEditor.getDocumentProvider();
    			IDocument doc=dp.getDocument(textEditor.getEditorInput());
    			doc.replace(selection.getOffset(), selection.getLength(), baked);    			
    		}
    	} catch (Exception e) {
    		System.out.println("BAKED EXCEPTION "+e);
    	}
    }
}
