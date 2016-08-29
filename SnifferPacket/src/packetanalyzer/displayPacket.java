/*
 * displayPacket.java
 *
 * Created on February 19, 2008, 1:17 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package packetanalyzer;
import net.tinyos.message.*;
import net.tinyos.packet.*;
import net.tinyos.util.*;
import java.util.*;
import java.io.*;

/**
 *
 * @author gonga
 */
public class displayPacket implements Runnable{
    private PacketSource reader;                    //java reader source
    private int pktCounter;                         //packet counter?
    private boolean openFlag;                       //open flag
    private int counter = 0;
    private String filename = "";                   //filename string*
    private static final byte TSMSG_SIZE  = 0xA;
    private static final int  TSMSG_POS0  = 8;
    private static final int  TSMSG_POS1  = 17;    
    private static final byte FRAMESIZE   = 0x26;
    private static final byte TSMSG_DELIMITER = 0x4F;    
    private static final byte[] frame = new byte[FRAMESIZE];
    private static String path = "/home/gonga/Projects/SnifferPacket/";
    public ArrayList<ReceivedBytes> listOfPackets = new ArrayList<ReceivedBytes>();
    
    /** Creates a new instance of displayPacket */
    public displayPacket() {
         openFlag = false;
    }
    public void run(){
         reader = BuildSource.makePacketSource();
         if(reader != null){
                readPacketInBytes();
         }else {
             System.out.println("UNABLE TO OPEN SERIAL PORT");
         }
       }
       public void readPacketInBytes(){
           boolean syncFlag = false; 
           String inStr="";
         try{
              if(openFlag == false){
                if(reader == null){
                  reader = BuildSource.makePacketSource();
                }
                reader.open(PrintStreamMessenger.err);
                openFlag = true;
              }
              
              ParsePacket.instance().resetElapsedTime();
              pktCounter = 0;
            
            
            for(;;){
                          
                byte[] packet = reader.readPacket();

                if(++pktCounter == 100){
                   inStr = "";
                };
                
                System.arraycopy(packet, TSMSG_POS0, frame, 0, packet.length-8);
                inStr  = ParsePacket.instance().getParsedFrame(frame)+"\n";
                
            }
         }catch(Exception ex){ System.err.println(ex); }
       }
       
       public synchronized void FileSaveWriteToFile() {
          try{
                ObjectOutputStream oos = null;
                oos = new ObjectOutputStream(new FileOutputStream(filename));
                try{
                    
                    oos.writeObject(this.listOfPackets); 
                    
                }catch(Exception ex){ex.printStackTrace();}
                oos.flush();                
                oos.close();
          }catch (IOException io){
                System.err.println("\tunpossible to write the Object\n\t" + io.getMessage());       }
       }
    
       private synchronized void FileOpenReadFromFile(){
          try{
                ObjectInputStream ois = null;                
                ois = new ObjectInputStream(new FileInputStream(filename));
                try{
                    this.listOfPackets = (ArrayList<ReceivedBytes>)ois.readObject();
                }catch(Exception ex){ ex.printStackTrace(); }
                ois.close();                
          }catch (IOException io){
                System.err.println("\tunpossible to read the Object\n\t" + io.getMessage());
          }
       }
    
        public void parseFile(){           
           ReceivedBytes newPkt;
           String str="", strb="";
           try{
                System.out.println("sizeofList="+this.listOfPackets.size());
               for(int k = 0 ; k < this.listOfPackets.size()  ; k++ ){
                    byte[] packet = this.listOfPackets.get(k).returnReceivedBytes();                      
                    if(k == 30){
                      strb=str = "";                      
                    }
                   str = ParsePacket.instance().getParsedFrame(packet)+str;
                   strb = "      "+k+"            "+ParsePacket.instance().getDisplay()+ strb;    
                   
                   //disp.dispJTextPane.setText(str);                    
                   //inputJTextPane.setText(strb);                
               }
            }catch(Exception ex){ System.err.println(ex); }
           
        } //end of ParseFile
    
    }
    
