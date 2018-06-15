package com.ipn.util;






import java.io.*;

public class Files {
    public static String[] readFile(String fileName) {
        String ret="";
        // The name of the file to open.
        //String fileName = "temp.txt";

        // This will reference one line at a time
        String line = null;
        String text = "";

        try {
            // FileReader reads text files in the default encoding.
            FileReader fileReader = 
                new FileReader(fileName);

            // Always wrap FileReader in BufferedReader.
            BufferedReader bufferedReader = 
                new BufferedReader(fileReader);

            while((line = bufferedReader.readLine()) != null) {
                text+=line;
            }   
            System.out.println(text);
            // Always close files.
            bufferedReader.close();         
        }
        catch(FileNotFoundException ex) {
            
            ret = "Unable to open file '" + fileName + "'";     
            
        }
        catch(IOException ex) {
            ret = "Error reading file '" + fileName + "'";
            // Or we could just do this: 
            // ex.printStackTrace();
        }
        String []f = {ret,text};    //Returns possible error and the text of the file
        return f;
    }
    public static String writeFile(String fileName,String text){    //path and text that we will write
        int i=0;
        // The name of the file to open.
        //String fileName = "temp.txt";
        String ret="",aux="";
        try {
            // Assume default encoding.
            FileWriter fileWriter =
                new FileWriter(fileName);
            System.out.println(fileName);

            // Always wrap FileWriter in BufferedWriter.
            BufferedWriter bufferedWriter =
                new BufferedWriter(fileWriter);
            

            // Note that write() does not automatically
            // append a newline character.
            for(i=0;i<text.length();i++){
                if(text.charAt(i)=='\n'){
                    bufferedWriter.write(aux);
                    aux="";
                }else
                    aux += text.charAt(i);    
            }
            if(aux.length()>0)
                bufferedWriter.write(aux);
            
            
            

            // Always close files.
            bufferedWriter.close();
            
        }
        catch(IOException ex) {
            ret = "Error writing to file '"+ fileName + "'";
            // Or we could just do this:
            // ex.printStackTrace();
        }
        return ret;
    }
}

