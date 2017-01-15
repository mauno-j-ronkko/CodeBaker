import java.io.File;

import fi.codebaker.core.*;

/**
 * This is an optional Main class in case you wish to compile CoeBaker into a command line tool.
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
public class Main {

	public static final String COPYRIGHT=
			 "CodeBaker 1.0.1.5;  Copyright (c) 2013-1016 Mauno Rönkkö\n"+
			 "\n"+
			 "Permission is hereby granted, free of charge, to any person obtaining\n"+
			 "a copy of this software and associated documentation files (the\n"+
			 "\"Software\"), to deal in the Software without restriction, including\n"+
			 "without limitation the rights to use, copy, modify, merge, publish,\n"+
			 "distribute, sublicense, and/or sell copies of the Software, and to\n"+
			 "permit persons to whom the Software is furnished to do so, subject to\n"+
			 "the following conditions:\n"+
			 "\n"+
			 "The above copyright notice and this permission notice shall be included\n"+
			 "in all copies or substantial portions of the Software.\n"+
			 "\n"+
			 "THE SOFTWARE IS PROVIDED \"AS IS\", WITHOUT WARRANTY OF ANY KIND,\n"+
			 "EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF\n"+
			 "MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.\n"+
			 "IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY\n"+
			 "CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,\n"+
			 "TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE\n"+
			 "SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.";
			
	public static void main(String[] args) throws Exception
	{
		System.out.println(COPYRIGHT);
		if (args.length!=2)
			System.out.println("\nSupply two arguments: \"path\" and \"filename\"");
		String path=args[0];
		String filename=args[1];
		if (!path.endsWith(""+File.separatorChar)) path=path+File.separatorChar;
		Log.instance.reset();
		Interpreter interpreter=new Interpreter(path);
		interpreter.interpret(new Parser("[*@"+filename+"]"));
		System.out.println(Log.instance.getContent());
	}

}
