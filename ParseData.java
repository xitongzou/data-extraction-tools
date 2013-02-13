import java.io.*;
import java.net.*;
import java.util.*;
import javax.swing.text.*;
import javax.swing.text.html.*;
import org.htmlparser.*;
import org.htmlparser.util.*;
import org.htmlparser.visitors.*;

public class ParseData{
	static String proxyHost = "192.168.1.80";
	static String proxyPort = "8118";
	static ArrayList  rightFormat = new ArrayList();
	static ArrayList existingFormat = new ArrayList();

	public static void main(String[] args) throws IOException,BadLocationException,ParserException
	{
 
            System.out.println("Starting...");
            String s ="http://www.autodealerdirectory.us/toyota_m45m_madd.html";
            s = getContentAd(s).replaceAll("&nbsp;|\t |ID&#58;","");
            s = s.replaceAll("&#44;            ",",");
            String[] array = s.split("<hr />");
            for (int i = 1; i < array.length; i++) {
                String temp = "";
                String[] subarray = array[i].split("<br />");
                BufferedWriter output2 = new BufferedWriter(new FileWriter(subarray[0].replaceAll("[ /.*-;]+","") + ".txt"));
                for (int j = 0; j < subarray.length; j++) { 

                 temp = subarray[j].replaceAll("^[ ]+","");
                 temp = temp.replaceAll("[0-9]{5}+","");
                 if (j == 2) {
                 temp = temp.replaceAll("[ ]{1}","");
                 output2.write(temp.replaceAll("[0-9]*",""));
                 } else {
                 output2.write(temp);
                 }
                 
                 output2.newLine();
                }
                output2.close();
            }
          //  String temp = s + "1";
            //get how many pages there are
          //  int k = Integer.parseInt(parsePage(getContentAd(temp)));
         /*   Parser parser = new Parser (t);
         //   NodeList list = parser.parse (null);
            TextExtractingVisitor visitor = new TextExtractingVisitor();
            parser.visitAllNodesWith(visitor);
            String textInPage = visitor.getExtractedText(); 
            output2.write(textInPage); ; */
            
            //Write descriptions to file
            /*
            for(int i = 1; i <= k; i++) {
                temp = s;
                temp += i;
                get(temp,true,output2);
            } */
            
            
            /*
            BufferedWriter output3 = new BufferedWriter(new FileWriter("results2.txt"));
            BufferedReader output = new BufferedReader(new FileReader("results.txt"));
            temp = output.readLine();
            while(temp != null) {
                
                //Replace all the non number and non space/paren characters
                temp = temp.replaceAll("[^0-9() ]","");
                //Split the numbers up by spaces
                String[] array = temp.split(" ");
                String t = "";
                
                //Write each number that is valid in the resulting txt file
                for (int i = 0; i < array.length; i++) {
                
                t = array[i];
                
                if (t.length() >= 7) {   
                
                if (t.length() > 10) {
                t = t.substring(0,10);    
                } 
                
                output3.write(t);
                output3.newLine();
                
                } 
                
                }
                
                temp = output.readLine();
            } 
            
            output3.close();
             * */

	}
        
        static Reader getReader(String uri) throws IOException
	{
		// Retrieve from Internet.
		if (uri.startsWith("http:"))
		{
			URLConnection conn = new URL(uri).openConnection();
			return new InputStreamReader(conn.getInputStream());
		}
		// Retrieve from file.
		else
		{
			return new FileReader(uri);
		}
	}


	//Here is being used tor to hide ip address
	private static void setupProxy() {
			Properties systemSettings = System.getProperties();
			systemSettings.put("proxySet", "true");
			systemSettings.put("proxyHost", proxyHost);
			systemSettings.put("proxyPort", proxyPort);
			systemSettings.put("http.proxySet", "true");
			systemSettings.put("http.proxyHost", proxyHost);
			systemSettings.put("http.proxyPort", proxyPort);
			System.setProperties(systemSettings);
			//parsePage();
		}
        
        private static void parseTxt(String s,BufferedWriter bw) throws IOException {
            
            String pattern = "<strong>Community Description</strong>";
            String pattern2 = "<br><font face=\"Arial\" size=1>Source:";
                int index = s.indexOf(pattern);
                
                while (index > 0) {
         bw.write(s.substring(index+pattern.length()+12,s.indexOf(pattern2)));
         bw.newLine();
         index = s.indexOf(pattern,index+pattern.length()+12);
                } 
            
        }
        
        private static void parseMain(String s, BufferedWriter bw) throws IOException {
            
            String pattern3 = "SummaryClad.aspx?";
                int index2 = s.indexOf(pattern3);
                String temp ="";
                
                //temp is the location of the more info url
                while (index2 > 0) {
               temp = "http://www.apartments.com/";
               temp += s.substring(index2,s.indexOf(">",index2));
               //Parsetxt parses the new site that has been translated into html
               parseTxt(getContentAd(temp),bw);
               
               index2 = s.indexOf(pattern3,index2+pattern3.length());
                }
            
            
        }
        
        private static String parsePage(String s) {
            String pattern = "Page 1 of ";
            int index = s.indexOf(pattern);
            String result = s.substring(index+pattern.length(),s.indexOf(" ",index+pattern.length()));
            return result;
        }

	public static void get(String urlStr, boolean useProxy,BufferedWriter bw) throws IOException {
 
            try {
                
                	if (useProxy) {
                            setupProxy();
			} 
                        
                EditorKit kit = new HTMLEditorKit();
		Document doc = kit.createDefaultDocument();
 
		// The Document class does not yet handle charset's properly.
		doc.putProperty("IgnoreCharsetDirective", Boolean.TRUE);
 
		// Create a reader on the HTML content.
		Reader rd = getReader(urlStr);
 
		// Parse the HTML.
		kit.read(rd, doc, 0);
 
		//  The HTML text is now stored in the document
                String str = (doc.getText(0, doc.getLength()));
                str = str.replaceAll("[^\\p{ASCII}]+","");
             //   parseTxt(str);
                String s = getContentAd(urlStr);
             //   bw.write(s);
                parseMain(s,bw);
                
            } catch (Exception e) {
                System.err.println(e);
            } finally {
                System.out.println("finished");
            }
              
	}
        
        	//This method get the whole page as String 
	public static String getContentAd(String path) throws IOException{
			java.net.URL url;
			java.net.URLConnection urlconn;
			java.io.BufferedReader br=null;
			DataInputStream dis=null;
			String line = null;
		
			try{
				url = new java.net.URL(path);
				urlconn = url.openConnection();
				urlconn.setDoInput(true); 
				
				dis = new java.io.DataInputStream(urlconn.getInputStream());
				try {
                                    
                                    br = new BufferedReader(new InputStreamReader(dis));
                                    String s = br.readLine(); 
                                    while(s != null) {
                                         line += s;
                                         s = br.readLine();
                                    }

				} catch (Exception e) {
				}

			}catch(MalformedURLException me){
				System.out.println(me.toString());
			}catch (IOException ioe) {
				System.out.println(ioe.toString());
			}catch (Exception e) {
				System.out.println(e.toString());
			}finally{
				dis.close();
                                br.close();
			}
			return line;
		}
        
}



