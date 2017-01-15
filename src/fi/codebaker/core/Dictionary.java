package fi.codebaker.core;

import java.util.HashSet;
import java.util.Hashtable;

/**
 * This class is a term container that provides term rewriting functionality on a string. 
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
public class Dictionary
{
	public static final String LEFT_BRACKET="\u2039";
	public static final String RIGHT_BRACKET="\u203A";
	
	private Dictionary original;
	private Hashtable<String,String> dictionary;
	
	private void construct(Dictionary original)
	{
		this.original=original;
		dictionary=new Hashtable<String,String>();
	}
	
	public Dictionary()
	{ construct(null); }
	
	public Dictionary(Dictionary original)
	{ construct(original); }
	
	private String autoextend(String label)
	{ return label.startsWith(LEFT_BRACKET) && label.endsWith(RIGHT_BRACKET) ? label : LEFT_BRACKET+label+RIGHT_BRACKET; }
	
	public String getValue(String label)
	{
		String value=dictionary.get(autoextend(label));
		if (value!=null) return value;
		if (original==null) return "";
		return original.getValue(label);
	}
	
	public void define(String label, String value)
	{ 
		label=autoextend(label);
		dictionary.put(label,value); 
		if (Character.isLowerCase(label.charAt(1)))
			if (value.length()>0)
			{
				label=""+label.charAt(0)+Character.toUpperCase(label.charAt(1))+label.substring(2);
				value=Character.toUpperCase(value.charAt(0))+value.substring(1);
				dictionary.put(label,value);
			}
	}

	private HashSet<String> getLabelSet()
	{
		HashSet<String> termSet=new HashSet<String>();
		termSet.addAll(dictionary.keySet());
		return termSet;
	}
	
	private int getFirstIndex(String string, int at, String[] sortedLabels)
	{
		for (int i=sortedLabels.length-1; i>=0; i--)
			if (string.startsWith(sortedLabels[i],at))
				return i;
		return -1;
	}

	public String rewrite(String string)
	{
		StringBuffer buffer=new StringBuffer();
		String[] labels=getLabelSet().toArray(new String[0]);
		int at=0;
		while (at<string.length())
		{
			int index=getFirstIndex(string,at,labels);
			if (index<0)
				{ buffer.append(string.charAt(at)); at++; }
			else
				{ buffer.append(getValue(labels[index])); at+=labels[index].length(); }
		}
		return buffer.toString();
	}
	
	public String toString()
	{
		String result="";
		String[] labels=getLabelSet().toArray(new String[0]);
		for (int i=0; i<labels.length; i++)
			result+=labels[i]+" : "+getValue(labels[i])+"\n";
		return result;
	}
}