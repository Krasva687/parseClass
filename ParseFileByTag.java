package com.example.demo.model;


/*
This class can parse you HTML code, by first tagin document or which you set up
You can use it to find index of needed section or check value into special openTagsCount
Created by krasva687@
*/

import java.io.*;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParseFileByTag {
    private BufferedReader rd;
    private String ht;//all file Lines in one String
    private StringBuilder html;
    private char[] htArray;//all chracter from file [^\n\r]
    private String firstTag;//tag for parsing
    private int openTagsCount = 0;
    private int closeTagsCount = 0;
    private String recString = ""; //this is need for record line characters from file, to check are they match with our tag?
    private int startLineTagsIndex = 0;// start need to move indexes into cycle
    private int endLineTagsIndex = 0;//end need to finish into cycle
    private boolean closeTag = false;
    private ArrayList<String> resultArray = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        ParseFileByTag sol = new ParseFileByTag("C:\\demo\\1.txt");
        sol.parseAllFile();
    }

    public ParseFileByTag (File fileReader) throws FileNotFoundException, IOException {
        try {
            this.rd = new BufferedReader(new FileReader(fileReader));
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        } catch (IOException ee) {
            System.out.println(ee + " Exception occurred");
        }
    }
    public ParseFileByTag (String fileReader) throws FileNotFoundException, IOException {
        try {
            this.rd = new BufferedReader(new FileReader(fileReader));
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        } catch (IOException ee) {
            System.out.println(ee + " Exception occurred");
        }
    }

    public ParseFileByTag (File fileReader, String firstTag) throws FileNotFoundException, IOException {
        setFirstTag(firstTag);
        try {
            this.rd = new BufferedReader(new FileReader(fileReader));
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        } catch (IOException ee) {
            System.out.println(ee + " Exception occurred");
        }
    }

    public ParseFileByTag (String fileReader, String firstTag) throws FileNotFoundException, IOException {
        setFirstTag(firstTag);
        try {
            this.rd = new BufferedReader(new FileReader(fileReader));
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        } catch (IOException ee) {
            System.out.println(ee + " Exception occurred");
        }
    }


    public void parseAllFile() throws IOException {
        readALLLinesFromFileAndGiveCharArray();
        if (firstTag == null){setFirstTag();}
        parseFile();
        printResultArray();
    }

    private void readALLLinesFromFileAndGiveCharArray() throws IOException {
        if(rd != null) {
            html = new StringBuilder();
            while (rd.ready()) {
                html = html.append(rd.readLine());
            }
            rd.close();
            ht = html.toString().replaceAll("\r\n", "");
            htArray = ht.toCharArray();
        }
    }

    public String getFirstTag() {
        return this.firstTag;
    }
    private void setFirstTag(){
        Pattern pat = Pattern.compile("<[^/> ]+");
        Matcher matcher = pat.matcher(ht);
        while (matcher.find()) {
            String[] firstTagArray = matcher.group(0).split("<");//we take first match and remove first character "<"
            firstTag = firstTagArray[1];// we have first tag
            break;
        }
    }

    private void setFirstTag(String firstTag) {
        this.firstTag = firstTag;
    }

    private void parseFile() {
        for (int i = 0; i < htArray.length; i++) {

            if (closeTagsCount == openTagsCount && openTagsCount > 0) {
                int innerOpenTagsCounter = 0;
                int innerCloseTagsCounter = 0;
                String lineForRecordInResultArray = "";
                if (innerCloseTagsCounter == innerCloseTagsCounter && innerOpenTagsCounter == 1) {
                    openTagsCount = innerOpenTagsCounter;
                }

                for (int t = startLineTagsIndex; t < endLineTagsIndex + 1; t++) {
                    lineForRecordInResultArray = lineForRecordInResultArray + htArray[t];
                }
                if (openTagsCount == 1) {
                    resultArray.add(lineForRecordInResultArray);
                } else if (openTagsCount > 1) {
                    while (openTagsCount > 0) {

                        String[] checkTags = lineForRecordInResultArray.split("</" + firstTag + ">");

                        Pattern forCheckTags = Pattern.compile("<" + firstTag + "([^>]+)?>");
                        Matcher matCheckTags = forCheckTags.matcher(checkTags[0]);
                        int countOfTagInString = 0;

                        while (matCheckTags.find()) {
                            countOfTagInString++;
                        }

                        if (countOfTagInString == 1) {
                            Pattern patRInner = Pattern.compile("<" + firstTag + "([^>]*?)>(.*)?</" + firstTag + ">");
                            Matcher matopInnder = patRInner.matcher(checkTags[0] + "</" + firstTag + ">");

                            while (matopInnder.find()) {
                                resultArray.add(matopInnder.group(0));
                            }
                            lineForRecordInResultArray = "";

                            for (int c = 1; c < checkTags.length; c++) {
                                lineForRecordInResultArray = lineForRecordInResultArray + checkTags[c] + "</" + firstTag + ">";
                            }
                        }

                        Pattern patR = Pattern.compile("<" + firstTag + "([^>]*?)>(.*)?</" + firstTag + ">");
                        Matcher matop = patR.matcher(lineForRecordInResultArray);

                        while (matop.find()) {
                            resultArray.add(matop.group(0));
                            lineForRecordInResultArray = matop.group(2);
                        }
                        openTagsCount--;
                    }
                }
                openTagsCount = 0;
                closeTagsCount = 0;
            }

            if ((htArray[i] + "").matches("<") && i + firstTag.length() < htArray.length) {
                int ss = i + firstTag.length() + 1;
                for (int j = i + 1; j < ss; j++) {
                    if (j == i + 1 && (htArray[j] + "").matches("/")) {
                        closeTag = true;
                        j++;
                        ss++;
                    }
                    recString = recString + htArray[j];
                }
                if (closeTag && recString.matches(firstTag)) {
                    endLineTagsIndex = ss;//---check if </ will be not all
                    closeTagsCount++;
                } else if (!closeTag && recString.matches(firstTag)) {
                    if (openTagsCount == 0) {
                        startLineTagsIndex = i;
                        openTagsCount++;
                    } else {
                        openTagsCount++;
                    }
                }
                recString = "";
                closeTag = false;
            }
        }
    }

    private void printResultArray() {
        for (String ss : resultArray) {
            System.out.println(ss);
        }
    }

    public ArrayList<String> getResultArray(){
        return this.resultArray;
    }
}
