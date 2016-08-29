/*
 * ParseConfigFile.java
 *
 * Created on January 16, 2008, 2:48 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package packetanalyzer;
import java.io.*;
import java.util.*;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.*;
import org.xml.sax.helpers.*;
import javax.xml.parsers.*;

/**
 *
 * @author gonga
 * @e-mail: gonga@kth.se
 */
public class ParseConfigFile{
    /**The initial position of the source address in the packet*/
    public int srcInit;
    /**The final position of the source address in the packet*/
    public int srcEnd;
    /**The initial position of the Active Message Type in the packet*/
    public int amInit;
    /**The final position of the Active Message Type in the packet*/
    public int amEnd;
    /**The initial position of the Group ID in the packet*/
    public int grpInit;
    /**The final position of the Group ID in the packet*/
    public int grpEnd;
    /**The initial position of the packet sequence number in the packet*/
    public int seqInit;
    /**The final position of the packet sequence number in the packet*/
    public int seqEnd;
    /**The initial position of the payload length in the packet*/
    public int msglenInit;
    /**The final position of the payload length in the packet*/
    public int msglenEnd;
    /**The initial position of the current time in the packet*/
    public int currTinit;
    /**The final position of the current time in the packet*/
    public int currTend;
    /**The initial position of the time difference in the packet*/
    public int diffTinit;
    /**The final position of the time difference in the packet*/
    public int diffTend;
    /**The initial position of the payload in the packet(the transmitted message)*/
    public int payloadInit;
    /**The final position of the payload in the packet(the transmitted message)*/
    public int payloadEnd;    
    
    /**The start position of the delimiter byte*/
    public int m_flag0Init;
    /**The final position os the delimiter byte*/
    public int m_flag0End;
    
    /***/
    public int m_flag1Init;
    public int m_flag1End;
    
    public int m_typeInit;
    public int m_typeEnd;
    
    public int ordtypeInit;
    public int ordtypeEnd;
    
    public int m_timeInit;
    public int m_timeEnd;
    
    public int m_seqnoInit;
    public int m_seqnoEnd;
    
    public int m_npktInit;
    public int m_npktEnd;
    
    public int m_nBytesInit;
    public int m_nBytesEnd;   
    
    public int destInit;
    public int destEnd;
    
    public int fcfInit;
    public int fcfEnd;
    
    public int frmLenInit;
    public int frmLenEnd;
    
    public int dsnInit;
    public int dsnEnd;
    
    public int netwInit;
    public int netwEnd;       
    
    public int rhdrTypeInit;
    public int rhdrTypeEnd;
    
    public int rhdrSeqnoInit;
    public int rhdrSeqnoEnd;
    
    public int rhdrHopsInit;
    public int rhdrHopsEnd;
    
    public int rhdrOrigInit;
    public int rhdrOrigEnd;
    
    private FileInputStream fis      = null;    
    private InputStream istr         = null;
    private XMLTags       _tags;
    private static String filename ="../config/config.txt";
    /** Creates a new instance of ParseConfigFile */
    public ParseConfigFile() {   
        try{
            _tags  = new XMLTags();
            fis    = new FileInputStream(filename);
            istr   = fis;
            parseConfParameters();
            dispParameters();
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
    /**
     * The main method used to invoke the parser Inner Class.
     * An input stream is passed as an input parameter to the parser class
     *
     */
    private void parseConfParameters(){
        try{
             SAXParserFactory factory = SAXParserFactory.newInstance();
             SAXParser saxParser = factory.newSAXParser();                
             saxParser.parse(istr, new ParseFile(this));             
             istr.close();
             fis.close();
        }catch(Exception ex){
          ex.printStackTrace();
        }
    }
    /**
     *Test method. It's used just to display if the parsed parameters are correct or not.
     * returns void
     */
    private void dispParameters(){
      System.out.println("srcin=["+this.srcInit+" , "+this.srcEnd+"]");
      System.out.println("seqno=["+this.seqInit+" , "+this.seqEnd+"]");
      System.out.println("msgLen=["+this.msglenInit+" , "+this.msglenEnd+"]");
      System.out.println("amtype=["+this.amInit+" , "+this.amEnd+"]");
      System.out.println("grpId=["+this.grpInit+" , "+this.grpEnd+"]");
      System.out.println("diffTime=["+this.diffTinit+" , "+this.diffTend+"]");
      System.out.println("currTime=["+this.currTinit+" , "+this.currTend+"]");
      System.out.println("payload=["+this.payloadInit+" , "+this.payloadEnd+"]"); 
      
      System.out.println("flag0=["+this.m_flag0Init+" , "+this.m_flag0End+"]");
      System.out.println("flag1=["+this.m_flag1Init+" , "+this.m_flag1End+"]");
      System.out.println("montype=["+this.m_typeInit+" , "+this.m_typeEnd+"]");
      System.out.println("ordtype=["+this.ordtypeInit+" , "+this.ordtypeEnd+"]");
      System.out.println("stime=["+this.m_timeInit+" , "+this.m_timeEnd+"]");
      System.out.println("sseqno=["+this.m_seqnoInit+" , "+this.m_seqnoEnd+"]");
      System.out.println("npkts=["+this.m_npktInit+" , "+this.m_npktEnd+"]");
      System.out.println("nBytes=["+this.m_nBytesInit+" , "+this.m_nBytesEnd+"]");    
      
      System.out.println("frmlen=["+this.frmLenInit+" , "+this.frmLenEnd+"]");
      System.out.println("dsn   =["+this.dsnInit+" , "+this.dsnEnd+"]");
      System.out.println("fcf   =["+this.fcfInit+" , "+this.fcfEnd+"]");
      System.out.println("dest  =["+this.destInit+" , "+this.destEnd+"]");
      System.out.println("netwrk=["+this.netwInit+" , "+this.netwEnd+"]");
      System.out.println("rhdrOrig["+this.rhdrOrigInit+" , "+this.rhdrOrigEnd+"]");
      System.out.println("rhdrHops["+this.rhdrHopsInit+" , "+this.rhdrHopsEnd+"]");
      System.out.println("rhdrSeqno["+this.rhdrSeqnoInit+" , "+this.rhdrSeqnoEnd+"]");
      System.out.println("rhdrType["+this.rhdrTypeInit+" , "+this.rhdrTypeEnd+"]");
    }
    /**
     *Inner class used to parse out the configuration file. This is invoked by 
     * through the main class
     */
    class ParseFile extends DefaultHandler{
         ParseConfigFile parser;
         private Stack tagStack     = new Stack();
         
         public ParseFile(ParseConfigFile main){
            this.parser = main;
         }
         
         /**
          * Called when the Parser starts parsing the Current XML File.
          */
          public void startDocument() throws SAXException {}
          /**
           * Called when the starting of the Element is reached. For Example if we have Tag
    	   * called <player> ... </player>, then this method is called when <Title> tag is
    	   * Encountered while parsing the Current XML File. The AttributeList Parameter has
    	   * the list of all Attributes declared for the Current Element in the XML File.
           * @param uri
           * @param localName
           * @param qName
           * @param attrs
    	   */
           public void startElement(String uri, String localName, String qName, Attributes attrs)throws SAXException {
                    if(qName.equals(_tags.PKT_TAG)){
                            ;
                    }
                    tagStack.push(qName); 
           }
          /**
           * While Parsing the XML file, if extra characters like space or enter Character
    	   * are encountered then this method is called. If you don't want to do anything
    	   * special with these characters, then you can normally leave this method blank.
    	   * @param ch
           * @param start
           * @param length
    	   */
           public void characters(char[] ch, int start, int length) throws SAXException{
                    String chars = new String(ch, start, length).trim();
                                       
                    if(chars.length() > 0){
                          String qName = (String)tagStack.peek();                         
                                                                              
                          if(qName.trim().equals(_tags.SRC_INIT)){
                              try{
                                  parser.srcInit = Integer.parseInt(chars.trim());
                              }catch(Exception ex){ ex.printStackTrace(); }                              
                          }else if(qName.trim().equals(_tags.SRC_END)){
                               try{
                                   parser.srcEnd = Integer.parseInt(chars.trim());
                               }catch(Exception ex){ ex.printStackTrace(); }
                          }
                          
                          if(qName.trim().equals(_tags.MSGLEN_INIT)){
                              try{
                                   parser.msglenInit = Integer.parseInt(chars.trim());
                               }catch(Exception ex){ ex.printStackTrace(); }
                          }else if(qName.trim().equals(_tags.MSGLEN_END)){
                               try{
                                   parser.msglenEnd = Integer.parseInt(chars.trim());
                               }catch(Exception ex){ ex.printStackTrace(); }
                          }
                          
                          if(qName.trim().equals(_tags.AMTYPE_INIT)){
                              try{
                                  parser.amInit = Integer.parseInt(chars.trim());
                               }catch(Exception ex){ ex.printStackTrace(); }
                          }else if(qName.trim().equals(_tags.AMTYPE_END)){
                              try{
                                  parser.amEnd = Integer.parseInt(chars.trim());
                              }catch(Exception ex){ ex.printStackTrace(); }
                          }
                          
                          if(qName.trim().equals(_tags.GRPID_INIT)){
                              try{
                                 parser.grpInit = Integer.parseInt(chars.trim());
                               }catch(Exception ex){ ex.printStackTrace(); }  
                          }else if(qName.trim().equals(_tags.GRPID_END)){
                              try{
                                  parser.grpEnd = Integer.parseInt(chars.trim());
                               }catch(Exception ex){ ex.printStackTrace(); }
                          }
                          
                          if(qName.trim().equals(_tags.SEQNO_INIT)){
                              try{
                                  parser.seqInit = Integer.parseInt(chars.trim());
                              }catch(Exception ex){ ex.printStackTrace(); }
                          }else if(qName.trim().equals(_tags.SEQNO_END)){
                              try{
                                  parser.seqEnd = Integer.parseInt(chars.trim());
                               }catch(Exception ex){ ex.printStackTrace(); }   
                          }
                          
                          if(qName.trim().equals(_tags.CURRTIME_INIT)){
                              try{
                                 parser.currTinit = Integer.parseInt(chars.trim());
                               }catch(Exception ex){ ex.printStackTrace(); }  
                          }else if(qName.trim().equals(_tags.CURRTIME_END)){
                              try{
                                  parser.currTend = Integer.parseInt(chars.trim());
                               }catch(Exception ex){ ex.printStackTrace(); }
                          }
                          
                          if(qName.trim().equals(_tags.DIFFTIME_INIT)){
                             try{
                                 parser.diffTinit = Integer.parseInt(chars.trim());
                               }catch(Exception ex){ ex.printStackTrace(); }
                          }else if(qName.trim().equals(_tags.DIFFTIME_END)){
                             try{
                                 parser.diffTend = Integer.parseInt(chars.trim());
                               }catch(Exception ex){ ex.printStackTrace(); }
                          }
                          
                          if(qName.trim().equals(_tags.PAYLOAD_INIT)){
                             try{
                                 parser.payloadInit = Integer.parseInt(chars.trim());
                               }catch(Exception ex){ ex.printStackTrace(); }
                          }else if(qName.trim().equals(_tags.PAYLOAD_END)){
                             try{
                                 parser.payloadEnd = Integer.parseInt(chars.trim());
                               }catch(Exception ex){ ex.printStackTrace(); }
                          }
                          
                          if(qName.trim().equals(_tags.MFLAG0_INIT)){
                               try{
                                 parser.m_flag0Init = Integer.parseInt(chars.trim());
                               }catch(Exception ex){ ex.printStackTrace(); }
                          }else if(qName.trim().equals(_tags.MFLAG0_END)){
                              try{
                                 parser.m_flag0End = Integer.parseInt(chars.trim());
                               }catch(Exception ex){ ex.printStackTrace(); }                          
                          }
                          
                          if(qName.trim().equals(_tags.MFLAG1_INIT)){
                            try{
                                 parser.m_flag1Init = Integer.parseInt(chars.trim());
                               }catch(Exception ex){ ex.printStackTrace(); }
                          }else if(qName.trim().equals(_tags.MFLAG1_END)){
                             try{
                                 parser.m_flag1End = Integer.parseInt(chars.trim());
                               }catch(Exception ex){ ex.printStackTrace(); }
                          }
                          
                           if(qName.trim().equals(_tags.MTYPE_INIT)){
                            try{
                                 parser.m_typeInit = Integer.parseInt(chars.trim());
                               }catch(Exception ex){ ex.printStackTrace(); }
                          }else if(qName.trim().equals(_tags.MTYPE_END)){
                             try{
                                 parser.m_typeEnd = Integer.parseInt(chars.trim());
                               }catch(Exception ex){ ex.printStackTrace(); }
                          }
                          
                          if(qName.trim().equals(_tags.STYPE_INIT)){
                            try{
                                 parser.ordtypeInit = Integer.parseInt(chars.trim());
                               }catch(Exception ex){ ex.printStackTrace(); }
                          }else if(qName.trim().equals(_tags.STYPE_END)){
                             try{
                                 parser.ordtypeEnd = Integer.parseInt(chars.trim());
                               }catch(Exception ex){ ex.printStackTrace(); }
                          }
                          
                          if(qName.trim().equals(_tags.MTIME_INIT)){
                            try{
                                 parser.m_timeInit = Integer.parseInt(chars.trim());
                               }catch(Exception ex){ ex.printStackTrace(); }
                          }else if(qName.trim().equals(_tags.MTIME_END)){
                            try{
                                 parser.m_timeEnd = Integer.parseInt(chars.trim());
                               }catch(Exception ex){ ex.printStackTrace(); }
                          }
                          
                          if(qName.trim().equals(_tags.MSEQNO_INIT)){
                            try{
                                 parser.m_seqnoInit = Integer.parseInt(chars.trim());
                               }catch(Exception ex){ ex.printStackTrace(); }
                          }else if(qName.trim().equals(_tags.MSEQNO_END)){
                            try{
                                 parser.m_seqnoEnd = Integer.parseInt(chars.trim());
                               }catch(Exception ex){ ex.printStackTrace(); }
                          }
                          
                          if(qName.trim().equals(_tags.MNPKTS_INIT)){
                            try{
                                 parser.m_npktInit = Integer.parseInt(chars.trim());
                               }catch(Exception ex){ ex.printStackTrace(); }
                          }else if(qName.trim().equals(_tags.MNPKTS_END)){
                            try{
                                 parser.m_npktEnd = Integer.parseInt(chars.trim());
                               }catch(Exception ex){ ex.printStackTrace(); }
                          }
                          
                          if(qName.trim().equals(_tags.MNBYTES_INIT)){
                            try{
                                 parser.m_nBytesInit = Integer.parseInt(chars.trim());
                               }catch(Exception ex){ ex.printStackTrace(); }
                          }else if(qName.trim().equals(_tags.MNBYTES_END)){
                            try{
                                 parser.m_nBytesEnd = Integer.parseInt(chars.trim());
                               }catch(Exception ex){ ex.printStackTrace(); }
                          }
                     
                          try{
                                if(qName.trim().equals(_tags.DEST_INIT)){
                                       parser.destInit = Integer.parseInt(chars.trim());
                                }else if(qName.trim().equals(_tags.DEST_END)){
                                       parser.destEnd = Integer.parseInt(chars.trim());
                                }
                          
                                if(qName.trim().equals(_tags.FCF_INIT)){
                                    parser.fcfInit = Integer.parseInt(chars.trim());
                                }else if(qName.trim().equals(_tags.FCF_END)){
                                    parser.fcfEnd  = Integer.parseInt(chars.trim());
                                }
                         
                                if(qName.trim().equals(_tags.FRLEN_INIT)){
                                    parser.frmLenInit = Integer.parseInt(chars.trim());
                                }else if(qName.trim().equals(_tags.FRLEN_END)){
                                    parser.frmLenEnd  = Integer.parseInt(chars.trim());
                                }
                          
                                if(qName.trim().equals(_tags.DSN_INIT)){
                                   parser.dsnInit = Integer.parseInt(chars.trim());
                                }else if(qName.trim().equals(_tags.DSN_END)){
                                    parser.dsnEnd = Integer.parseInt(chars.trim());
                                }
                          
                                if(qName.trim().equals(_tags.NETWORK_INIT)){
                                    parser.netwInit = Integer.parseInt(chars.trim());
                                }else if(qName.trim().equals(_tags.NETWORK_END)){
                                    parser.netwEnd  = Integer.parseInt(chars.trim());
                                }
                                
                                /** ROUTING HEADER**/
                                if(qName.trim().equals(_tags.HDRTYPE_INIT)){
                                    parser.rhdrTypeInit = Integer.parseInt(chars.trim());
                                }else if(qName.trim().equals(_tags.HDRTYPE_END)){
                                    parser.rhdrTypeEnd  = Integer.parseInt(chars.trim());
                                }
                                
                                if(qName.trim().equals(_tags.HDRHOPS_INIT)){
                                    parser.rhdrHopsInit = Integer.parseInt(chars.trim());
                                }else if(qName.trim().equals(_tags.HDRHOPS_END)){
                                    parser.rhdrHopsEnd  = Integer.parseInt(chars.trim());
                                }
                                
                                if(qName.trim().equals(_tags.HDRSEQNO_INIT)){
                                    parser.rhdrSeqnoInit = Integer.parseInt(chars.trim());
                                }else if(qName.trim().equals(_tags.HDRSEQNO_END)){
                                    parser.rhdrSeqnoEnd  = Integer.parseInt(chars.trim());
                                }
                                
                                if(qName.trim().equals(_tags.HDRORIG_INIT)){
                                    parser.rhdrOrigInit  = Integer.parseInt(chars.trim());
                                }else if(qName.trim().equals(_tags.HDRORIG_END)){
                                    parser.rhdrOrigEnd   = Integer.parseInt(chars.trim());
                                }
                                
                          }catch(Exception ex){ ex.printStackTrace();  }
                    } //end of if(chars.length() > 0)
           }
           
           /**
            * Called when the Ending of the current Element is reached. For example in the
            * above explanation, this method is called when </Title> tag is reached
            */
             public void endElement(String uri, String localName, String qName) throws SAXException{                    
                    tagStack.pop();
             }
            /**
             *Called when the Parser Completes parsing the Current XML File.
             */
            public void endDocument() throws SAXException{}
    }
    /** The main method, but it's not currently being used
     * @param args - the input parameters if any.
     * return
     */
    public static void main(String[] arsgs){
        ParseConfigFile p = new ParseConfigFile();        
    }
}
