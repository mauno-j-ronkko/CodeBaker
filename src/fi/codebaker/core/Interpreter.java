package fi.codebaker.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;

/**
 * This class performs the actual term rewriting and interpretation by a Constructor.
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
public class Interpreter
{
	private String path;
	private Dictionary dictionary;
	
	private Interpreter(String path, Dictionary dictionary)
	{ 
		this.path=path;
		this.dictionary=new Dictionary(dictionary); 
	}
	
	public Interpreter(String path)
	{ 
		this.path=path;
		this.dictionary=new Dictionary(); 
	}
	
	private String cleanLabel(String label)
	{ return label.startsWith("{") ? label.substring(1,label.length()-1) : label; }
	
	public String readFromFile(String filename) throws Exception
	{
		File file=new File(path+filename);
		int length=(int)file.length();
		if (length==0) return "";
		byte[] buffer = new byte[length];
		FileInputStream fis = new FileInputStream(file);
		fis.read(buffer);
		fis.close();
		return new String(buffer);
	}

	private String constructDefinitions(String label, String content) throws Exception
	{
		String separator="";
		String delimiter="";
		String separators=dictionary.getValue("#");
		if (!separators.isEmpty())
		{
			Interpreter interpreter=new Interpreter(path,dictionary);
			interpreter.interpret(new Parser(separators));
			separator=interpreter.dictionary.getValue("separator");
			delimiter=interpreter.dictionary.getValue("delimiter");
		}
		if (separator.isEmpty()) separator="\n";
		if (delimiter.isEmpty()) delimiter=" ";
		Data data=new Data(label,separator,delimiter);
		if (content.startsWith("[")) 
			content=interpretBlock(content).getContent();
		else
			content=cleanLabel(content);
		return data.getDefinitions(content);
	}

	private Constructor interpretBlock(String block) throws Exception
	{
		String content = block.substring(1,block.length()-1);
		boolean keepTags=content.startsWith("*");
		if (keepTags) content=content.substring(1);
		Parser parser=new Parser(content);
		String sample=parser.getSample();
		String label=cleanLabel(parser.parseNext());
		String body;
		if (label.startsWith("@"))
			body=readFromFile(cleanLabel(label.substring(1)));
		else
			body=dictionary.getValue(label);
		Constructor constructor=new Constructor(body);
		try
		{
			while (parser.isNotEmpty())
			{
				String element=parser.parseNext();
				if (element.startsWith("["))
					parser.inject(interpretBlock(element).getContent());
				else 
				{
					Interpreter interpreter=new Interpreter(path,dictionary);
					if (!parser.parseNext().equals(":"))
						throw new CodeBakerException("\":\" missing after label {"+element+"}\n");
					if (element.startsWith("#"))
						parser.inject(constructDefinitions(element.substring(1),parser.parseNext()));
					else
					{
						interpreter.interpret(new Parser(cleanLabel(parser.parseNext())));
						constructor.rewrite(cleanLabel(element), interpreter.dictionary);
					}
				} 
			}
			if (!keepTags) constructor.removeTags();
		} catch (Exception e) {
			throw new CodeBakerException(e.getMessage()+" during ["+sample+"]\n");
		}
		return constructor;
	}
	
	private void writeToFile(String filename, String value) throws Exception
	{
		if (filename.isEmpty())
			Log.instance.add(value);
		else 
		{
			FileWriter writer = new FileWriter(path+filename);
			writer.write(value);
			writer.close();
			Log.instance.addFilename(filename);
		} 
	}
	
	private void interpretTerm(String label, Parser parser) throws Exception 
	{
		if (!parser.parseNext().equals(":"))
			throw new CodeBakerException("\":\" missing after label {"+label+"}\n");
		String value=cleanLabel(parser.parseNext());
		Constructor constructor;
		if (value.startsWith("["))
			constructor=interpretBlock(value);
		else
			constructor=new Constructor(value);
		if (label.startsWith("@"))
			writeToFile(label.substring(1),constructor.getContent());
		else
			dictionary.define(label,constructor.getContent());
	}

	public void interpret(Parser parser) throws Exception
	{
		String sample=parser.getSample();
		try
		{
			while (parser.isNotEmpty())
			{
				String element=parser.parseNext();
				if (element.startsWith("["))
					parser.inject(interpretBlock(element).getContent());
				else
					interpretTerm(cleanLabel(element), parser);
			}
		} catch (Exception e) {
			throw new CodeBakerException(e.getMessage()+" during "+sample);
		} 
	}
}