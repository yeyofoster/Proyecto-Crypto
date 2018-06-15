package paquete;


import com.ipn.util.Files;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;

public class File2Hex
{
    public static String convertToHex(File file) throws IOException {
	InputStream is = new FileInputStream(file);
	 
	int bytesCounter =0;		
	int value = 0;
	StringBuilder sbHex = new StringBuilder();
	StringBuilder sbText = new StringBuilder();
	StringBuilder sbResult = new StringBuilder();
			
	while ((value = is.read()) != -1) {    
	    //convert to hex value with "X" formatter
            sbHex.append(String.format("%X", value));
	             
	    //If the chracater is not convertable, just print a dot symbol "." 
	    if (!Character.isISOControl(value)) {
	      	sbText.append((char)value);
	    }else {
	        sbText.append(".");
	    }
	    
	    //if 16 bytes are read, reset the counter, 
            //clear the StringBuilder for formatting purpose only.
	    if(bytesCounter==15){
	      	sbResult.append(sbHex).append("      ").append(sbText).append("\n");
                
	        sbText.setLength(0);
	       	bytesCounter=0;
	    }else{
	        bytesCounter++;
	    }
            
       }
			
	//if still got content
	if(bytesCounter!=0){			
	     //add spaces more formatting purpose only
	    sbResult.append(sbHex).append("      ").append(sbText).append("\n");
            
        }

        is.close();
       return sbHex.toString();
  }

   public static void main(String[] args) throws IOException
   {	
    	//display output to console
    	String res = convertToHex(new File("c:/fi.txt"));
    	System.out.println(res);
    	//write the output into a file
    	//res = convertToHex(new PrintStream("c:/Users/Master/Documents/fil.hex"), new File("c:/fil.txt"));
        String err_Write = Files.writeFile("c:/Users/Master/Documents/fil.hex",res);
        System.out.println(err_Write);
    } 
}
