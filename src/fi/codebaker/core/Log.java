package fi.codebaker.core;

/**
 * This class implements an internal logger that is used to substitute console functionality.
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
public class Log 
{
	public static Log instance=new Log();
	
	private StringBuffer log;
	private String files;
	
	private Log()
	{ reset(); }
	
	public void reset()
	{ 
		log=new StringBuffer();
		files="";
	}
	
	public String getContent()
	{ 
		String result=log.toString();
		if (!result.isEmpty()) result+="\n\n";
		if (!files.isEmpty()) result+="WROTE FILES: "+files;
		if (result.isEmpty()) result="(no output)";
		return result;
	}
	
	public void add(String string)
	{ log.append(string); }
	
	public void addFilename(String filename)
	{ files+="\n  "+filename; }
}
