package fi.codebaker.core;

/**
 * This class performs term rewriting on a given content by using a Dictionary and Tags for rewriting.
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
public class Constructor
{
	private String content;
	
	public Constructor(String content)
	{ this.content=content; }

	public void rewrite(String label, Dictionary dictionary) throws CodeBakerException 
	{
		String header=Tag.LEFT_BRACKET+label;
		String old=content;
		content="";
		while (!old.isEmpty())
		{
			int at=old.indexOf(header);
			if (at<0)
				{ content+=old; old=""; }
			else 
			{
				if (at>0) {content+=old.substring(0,at); old=old.substring(at);}
				Tag tag=new Tag(old);
				old=old.substring(tag.getLength());
				content+=tag.rewrite(dictionary);
			} 
		}
	}
	
	public void removeTags() throws CodeBakerException 
	{
		String old=content;
		content="";
		while (!old.isEmpty())
		{
			int at=old.indexOf(Tag.LEFT_BRACKET);
			if (at<0)
				{ content+=old; old=""; }
			else 
			{
				if (at>0) {content+=old.substring(0,at); old=old.substring(at);}
				old=old.substring(new Tag(old).getLength());
			}
		}
	}

	public String getContent()
	{ return content; }
}