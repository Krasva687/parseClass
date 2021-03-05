# parseHTMLClass  
class to parse html file by tag section  
You can create class with file name(it can be String or File type) and do not set the tag values by which you want to parse file or  
you can set tag name when you create class, after file name  
examples:  
_________1______________    
**ParseFileByTag pfbt = new ParseFileByTag("C:\\\\test\\\\test.html")  
pfbt.parseAllFile();**  
\\after that in console you will see first tag sections, which was be in that file  
________________________  
________2_______________  
  
**ParseFileByTag pfbt = new ParseFileByTag("C:\\\\test\\\\test.html", "div")  
pfbt.parseAllFile();**  
\\after that in console you will see sections of tag "**div**", which was be in that file  
  
Also you can return ArrayList<String> with that lines by:  
  **ArrayList<String> myNewArray = pfbt.getResultArray();**  
