package fi.codebaker.core;

/**
 * This class provides parsing functionality with string injection on a given string.
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
public class Parser
{
	private static final CodeBakerException NOTHING_TO_PARSE=new CodeBakerException("Nothing to parse");

	private String corpus;
  
	public Parser(String corpus)
	{ this.corpus=corpus.trim(); }
	
	public boolean isAtEnd()
	{ return corpus.isEmpty(); }
	
	private char next()
	{ return isAtEnd() ? 0 : corpus.charAt(0); }
	
	private boolean isNextWhitespace()
	{ return Character.isWhitespace(next()); }
	
	private char getNext()
	{ 
		if (isAtEnd()) return 0;
		char c=corpus.charAt(0);
		corpus=corpus.substring(1);
		return c; 
	}
	
	private String getBlock(char start, char end) throws CodeBakerException 
	{ 
		if (next()!=start) throw new CodeBakerException("\""+start+"\" missing");
		String block="" + getNext();
		int depth=1;
		while (!isAtEnd())
		{
			if (next()==start) depth++;
			if (next()==end) {if (--depth<=0) break;}
			block += getNext();
		}
		if (depth>0) throw new CodeBakerException("\""+end+"\" missing");
		return block+getNext();
	}
	
	private String getLabel()
	{ 
		String label="";
		while (!isAtEnd() && !isNextWhitespace() && next()!=':')
			label +=getNext();
		return label;
	}
	
	public boolean isNotEmpty()
	{ 
		while (!isAtEnd() && isNextWhitespace()) getNext();
		return !isAtEnd();
	}
	
	public String parseNext() throws CodeBakerException
	{ 
		if (!isNotEmpty()) throw NOTHING_TO_PARSE;
		if (next()=='[') return getBlock('[',']');
		if (next()=='{') return getBlock('{','}');
		if (next()==':') return "" +getNext();
		return getLabel();
	}
		
	public void inject(String string)
	{ corpus=string+corpus; }

	public String getCorpus()
	{ return corpus; }
	
	public String getSample()
	{ 
		int len=corpus.length();
		return "\"..."+corpus.substring(0,len>50 ? 50 : len)+"...\"";
	}
}