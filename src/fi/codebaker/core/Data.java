package fi.codebaker.core;

/**
 * This class constructs term definitions from delimited data
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
public class Data 
{
	private String label;
	private String separator;
	private String delimiter;
	
	public Data(String label, String separator, String delimiter)
	{
		this.label=label;
		this.separator=separator;
		this.delimiter=delimiter;
	}
	
	private String getOneDefinition(String string)
	{
		int count=1;
		StringBuffer buffer=new StringBuffer(label+" : {");
		string=string.trim();
		while (!string.isEmpty())
		{
			int at=string.indexOf(delimiter);
			String column=at<0 ? string : string.substring(0,at);
			buffer.append(" "+count+" : {"+column+"}");
			string=at<0 ? "" : string.substring(at+delimiter.length());
			count++;
		}
		buffer.append("}\n");
		return buffer.toString();
	}
	
	public String getDefinitions(String string)
	{
		StringBuffer buffer=new StringBuffer();
		string=string.trim();
		while (!string.isEmpty())
		{
			int at=string.indexOf(separator);
			String row=at<0 ? string : string.substring(0,at);
			buffer.append(getOneDefinition(row));
			string=at<0 ? "" : string.substring(at+separator.length());
		}
		return buffer.toString();
		
	}
}
