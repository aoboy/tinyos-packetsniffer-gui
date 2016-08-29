/*
 * WriteOutputFiles.java
 *
 * Created on January 29, 2008, 1:12 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package packetanalyzer;
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;


/**
 *
 * @author gonga
 */
public class WriteOutputFiles {
    
    private static String monit_fname ="../outputs/";
    private static String all_fname   ="../saveInctraffic/";
    private static String traffic_fname = "../saveInctraffic/";   
    private static WriteOutputFiles  instance = null;
    
    public static final  String SEP1           = " ";
    public static final  String SEP2           = SEP1+SEP1;
    
    private Hashtable<String, OutputStreamWriter> outFileshash = null;
    
    private static final String[] FILE_NAMES = { all_fname+"TrafficNode_",
                                                 monit_fname+"MonitorTrafficNode_" , 
                                                 all_fname+"AllNodesTraffic"
                                               };
    /** Creates a new instance of WriteOutputFiles */
    private WriteOutputFiles() {
        try{ 
               this.outFileshash = new Hashtable<String, OutputStreamWriter>();                                                 
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
    
    public static WriteOutputFiles instance(){
        if(instance == null){
            instance = new WriteOutputFiles();
        }
        return instance;
    }
    /**
     * Write to the Ordinary packet file
     * @param msg -  the string message to be written
     * @throws  - 
     */
    public synchronized void WriteAllTrafficPktFile(String msg){
       WriteSpecificFile("", 2, msg);   
    }
    /**
     * Write to the Monitoring packet file
     * @param msg -  the string to be written
     * @throws - 
     */
    public synchronized  void WriteMonitorTrafficFile(String key, String msg){
       WriteSpecificFile(key+"_", 1, msg);   
    }
    public synchronized  void WriteOrdinaryTrafficFiles(String key, String msg){
       WriteSpecificFile(key, 0, msg);
    }
    /**
     * Close the Output streams    
     */
    public synchronized  void closeFiles(){
        try{
            for(Enumeration<String> en = outFileshash.keys(); en.hasMoreElements(); ){             
                String key = en.nextElement();
                outFileshash.get(key).close();
                outFileshash.remove(key);
            }            
            
        }catch(Exception ex){ex.printStackTrace();  }
    }
    
    public void WriteSpecificFile(String Key, int type, String msg){
        OutputStreamWriter osw = null;    
       
        if(outFileshash.containsKey(Key)){
            try{
                outFileshash.get(Key).write(msg);
                outFileshash.get(Key).flush();
            }catch(IOException ioe){ 
                System.out.println("Error While writing File...");
            }
        }else{        
            try{                
                outFileshash.put(Key, 
                        new OutputStreamWriter(new FileOutputStream(FILE_NAMES[type]+""+Key+".txt"))); 
                        System.out.println("creating "+FILE_NAMES[type]+" file for "+Key);
                switch(type){                    
                    case   0: 
                         outFileshash.get(Key).write("\n\t\tORDINARY TRAFFIC FILE FOR SENSOR_ID:"+Key+"\n\n");
                         outFileshash.get(Key).write(" RDSN"+SEP2+"DSN"+SEP2+" SRC"+SEP2+" DEST"+SEP2+"FL"+SEP2+"PL"+SEP2+
                                 " TSTAMP "+SEP2+"   TSTAMP  "+SEP2+"  PKT_TYPE  "+SEP2+"     PAYLOAD\n");
                         break;
                    case   1:
                        outFileshash.get(Key).write("\n\t\tMONITOR TRAFFIC FILE FOR SENSOR_ID:"+Key+"\n\n");
                        outFileshash.get(Key).write(" RDSN"+SEP2+"DSN"+SEP2+" SDSN"+SEP2+" SRC"+SEP2+                                                                
                                " TSTAMP "+SEP2+"   TSTAMP  "+SEP2+"STSTAMP "+SEP2+"   NBYTES   "+SEP2+"  NPKTS \n");
                        break;
                    case   2: 
                         XMLTags sp2 = new XMLTags();
                        outFileshash.get(Key).write("\n\t\tALL TRAFFIC FILE\n\n");
                        outFileshash.get(Key).write(" RDSN"+SEP2+"DSN"+SEP2+" SRC"+SEP2+"DEST"+SEP2+"FL"+SEP2+"PL"+SEP2+
                                 " TSTAMP "+SEP2+"   TSTAMP  "+SEP2+"  PKT_TYPE  "+SEP2+"     PAYLOAD\n");;
                }
                outFileshash.get(Key).write(msg);
                outFileshash.get(Key).flush();
            }catch(Exception ex2){
                ex2.printStackTrace();
            }
       
        }        
    }
}
