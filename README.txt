/**
 * This project implements CodeBaker as an Eclipse plugin.
 * Thus, this is a plugin project; however, an optional Main.java is included
 * in case one wishes to compile CodeBaker into a command line tool.
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
 */

The grammar for CodeBaker is

<corpus>       ::= <element> <corpus> 
                 | <element> 
<element>      ::= <construction> 
                 | <definition> 
<construction> ::= "["<label> <corpus>"]" 
                 | "[*"<label> <corpus>"]" 
                 | "[@"<label> <corpus>"]" 
<definition>   ::= <label> ":" <value> 
                 | "@ : " <value> 
                 | "@"<label> ":" <value>
                 | "# : { separator:" <value> " delimiter:" <value> "}" 
                 | "#"<label> ":" <value>
<value>        ::= <label> | <construction> 
<label>        ::= "{" <text> "}" | <word>

NOTES:
1) definition with a label starting with a lower case letter is auto-duplicated to a definition starting with 
   an upper case letter, eg. "name : age" is auto-duplicated to "Name : Age"
2) tags are of form "«LABEL|separator| ...body‹tag›body...»"
3) construction of type "[*label corpus]" retains tags in corpus
4) label "@" in definition redirects the output to Baker result field (console)
5) label "@filename" in definition redirects the input from, or output to, a file with given filename
6) "# : {separator:{;} delimiter:{,}}" defines a data parser with ";" as line separator and "," as field delimiter
7) "#label : <value>" applies data parser to value with current data separator and delimiter
   resulting with definitions "label : { ... }  label : { ... } ..."