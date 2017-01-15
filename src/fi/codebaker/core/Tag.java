package fi.codebaker.core;

/**
 * This class captures a tag with rewriting functionality by using a given Dictionary.
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
public class Tag
{
	public static final String LEFT_BRACKET="\u00AB";
	public static final String SEPARATOR="|";
	public static final String RIGHT_BRACKET="\u00BB";
	
	private static final CodeBakerException LEFT_BRACKET_MISSING=new CodeBakerException("\""+LEFT_BRACKET+"\" missing");
	private static final CodeBakerException RIGHT_BRACKET_MISSING=new CodeBakerException("\""+RIGHT_BRACKET+"\" missing");
	private static final CodeBakerException SEPARATOR_MISSING=new CodeBakerException("\""+SEPARATOR+"\" missing");
	
	private String label;
	private String separator;
	private String body;
	
	public Tag(String string) throws CodeBakerException  
	{
		String tag=getBlock(string);
		tag=tag.substring(LEFT_BRACKET.length(),tag.length()-RIGHT_BRACKET.length());
		int sep=tag.indexOf(SEPARATOR);
		if (sep<0) throw new CodeBakerException("\""+SEPARATOR+"\" missing");
		label=tag.substring(0,sep);
		tag=tag.substring(sep);
		separator=getSeparator(tag);
		body=tag.substring(separator.length());
		separator=separator.substring(SEPARATOR.length(),separator.length()-SEPARATOR.length());
	}

	private String getBlock(String text) throws CodeBakerException  
	{
		StringBuilder block=new StringBuilder();
		if (!text.startsWith(LEFT_BRACKET)) throw LEFT_BRACKET_MISSING;
		int depth=0;
		int at=0;
		while (at<text.length())
		{
			if (text.startsWith(LEFT_BRACKET,at)) depth++;
			if (text.startsWith(RIGHT_BRACKET,at)) {if (--depth<=0) break;}
			block.append(text.charAt(at));
			at++;
		}
		if (depth>0) throw RIGHT_BRACKET_MISSING;
		block.append(RIGHT_BRACKET);
		return block.toString();
	}

	private String getSeparator(String text) throws CodeBakerException
	{
		if (!text.startsWith(SEPARATOR) || text.length()<2) throw SEPARATOR_MISSING;
		int end=text.indexOf(SEPARATOR, 1);
		if (end<0) throw SEPARATOR_MISSING;
		return text.substring(0, end+1);
	}

	public int getLength()
	{ return LEFT_BRACKET.length()+label.length()+2*SEPARATOR.length()+separator.length()+body.length()+RIGHT_BRACKET.length(); }
	
	public String rewrite(Dictionary dictionary)
	{
		String result=dictionary.rewrite(body);
		body=separator+body;
		separator="";
		return result+toString();
	}
	
	public String toString()
	{ return LEFT_BRACKET+label+SEPARATOR+separator+SEPARATOR+body+RIGHT_BRACKET; }
}