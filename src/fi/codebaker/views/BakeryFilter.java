package fi.codebaker.views;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

/**
 * This is a filter class for .bkr files.
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
public class BakeryFilter extends ViewerFilter
{
	 public boolean select(Viewer viewer, Object parentElement, Object element)
	 {
		 if (element instanceof org.eclipse.core.internal.resources.Project)
			 return true;
		 else if (element instanceof org.eclipse.core.internal.resources.Folder)
			 return true;
		 else if (element instanceof org.eclipse.core.internal.resources.File)
			 return ((org.eclipse.core.internal.resources.File)element).getName().endsWith(".bkr");
		 else
			 return false;
	 }
}
