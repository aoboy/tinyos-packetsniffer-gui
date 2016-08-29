/*
 * ParsePacket.java
 *
 * Created on January 16, 2008, 2:22 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package packetanalyzer;

import java.text.NumberFormat;

/**
 * KTH - THE ROYA INSTITUTE OF TECHNOLOGY -  STOCKHOLM -  SWEDEN
 * @author gonga
 * @e-mail gonga@kth.se
 */
public class ParsePacket extends ParseConfigFile {
    /**sender */
    private long scurrTime;
    private long snpkts;
    private long snbytes;
    private int  sseqno;
    private int  montype;
    private int  ordtype;
    
    private float timestamp;
    private long currTime;
    private long diffTime;
    private int  srcId;
    private int  destId;
    private int  seqNo;
    private int  fcf; 
    private int  network;
    private int msgLen;
    private int frameLen;
    private int  amType;
    private int  grpId;
    private byte[] frame;
    private byte[] payload;
    private boolean control;
    
    public static final  String SEP1           = " ";
    public static final  String SEP2           = SEP1+SEP1;
    public static final  String SEP3           = SEP2+SEP1;
    public static final  String SEP4           = SEP3+SEP1;
    public static final  String SEP5           = SEP4+SEP1;
    public static final  String SEP6           = SEP5+SEP1;
    public static final  String SEP7           = SEP6+SEP1;
    public static final  String SEP8           = SEP7+SEP1;
    public static final  String SEP9           = SEP8+SEP1;
    public static final  String SEP10          = SEP9+SEP1;
    public static final  String SEP11          = SEP10+SEP1;
    public static final  String SEP12          = SEP11+SEP1;
    public static final  String SEP13          = "\t\t";
    
    public static final  String ZEROS1         = "0";
    public static final  String ZEROS2         = ZEROS1+ZEROS1;
    public static final  String ZEROS3         = ZEROS2+ZEROS1;
    public static final  String ZEROS4         = ZEROS3+ZEROS1;
    public static final  String ZEROS5         = ZEROS4+ZEROS1;
    public static final  String ZEROS6         = ZEROS5+ZEROS1;
    public static final  String ZEROS7         = ZEROS6+ZEROS1;
    public static final  String ZEROS8         = ZEROS7+ZEROS1;
    public static final  String ZEROS9         = ZEROS8+ZEROS1;
    public static final  String ZEROS10        = ZEROS9+ZEROS1;
    public static final  String ZEROS11        = ZEROS10+ZEROS1;
    
    public static final  String[] ZEROS = {"",
                                           ZEROS1, ZEROS2, ZEROS3, ZEROS4,ZEROS5,
                                           ZEROS6, ZEROS7, ZEROS8, ZEROS9, ZEROS10,
                                           ZEROS11
                                           }; 
    
    private boolean monitorpkt;
    
    //Routing header
    private int hdrType;    
    private int hdrOrigAddr;
    private int hdrHopCount;
    private int hdrDataSeqno;
            
    private String frm;
    //private String screen0 ="";
    private String screen1 ="";
    private String FoutStr = "";
    
    private static final byte M_DELIMITER_BYTE = 0x7E;
    private static final byte M_STRUCTSIZE     = 0x11;
    private static final byte FRAME_SIZE       = 0x2E;
    private static final int AM_BEACONMSG      = 0xC8;
    private static final int  AM_ETXDATAMSG    = 0xC9;
    private static final byte  SENDER_3        = 0x3;
    private static final String TYPE_ORDINARY  = "ORDINARY_PKT";
    private static final String TYPE_MONITOR   = " MONITOR_PKT";
    private static final String TYPE_BEACON    = "  BEACON_PKT";
   
    private static final String[] MSG_TYPE     = {TYPE_ORDINARY, TYPE_MONITOR, TYPE_BEACON};
    
    private static ParsePacket parsep = null;
    
    public static boolean checkHeader = false;
    
    
    private int pos0, pos1, msgLenOffset, rOffset;
    private static final int  ROUTING_HEADER_OFFSET = 6;
    
    /** Creates a new instance of ParsePacket */
    public ParsePacket() {
        super();
        this.timestamp = 0;
        this.control   = this.monitorpkt = false;
        this.currTime  = this.diffTime = 0; 
        this.scurrTime = this.snbytes  = this.snpkts = 0;         
        this.payload = null;        
    }
    
    public static ParsePacket instance(){
       if(parsep == null){
         parsep = new ParsePacket();
       }
       return parsep;
    }
    /**
     * Set the sender node address
     * @param init -  the initial position of the sender node address in the received frame
     * @param end  - the final position of the sender node address in the received frame
     */
    public void setSourceId(int init, int end){     
       srcId = 0;
      for(int k=init, exp=0; k <= end; k++, exp++){
        srcId += (int)Math.pow(256, exp)*(frame[k] & 0xff);
      }
    }
    public void setDestination(int init, int end){
       destId = 0;
       for(int k=init, exp=0; k <= end; k++, exp++){
        destId += (int)Math.pow(256, exp)*(frame[k] & 0xff);
      } 
    }
    private void frameLength(int init, int end){
      frameLen = (int)(frame[init] &0xff);        
    }
    private void setFCF(int init, int end){
     fcf = 0;
      for(int k=init, exp=0; k <= end; k++, exp++){
        fcf += (int)Math.pow(256, end-k)*(frame[k] & 0xff);
      }
    }    
    
    /**Set the received message length
     * @param init - the initial position of the message length in the received frame
     * @param end  - the final position of the message length in the received frame     
     */
    public void setMsgLength(int init, int end){
        msgLen = (int)(frame[init] &0xff);       
    }
    /**
     * Set the sequence number field
     * @param init - the initial position of the sequence number bytes in the frame
     * @param end -  the end position of the sequence number bytes in the frame
     * @return -  nothing
     */
    public void setSeqNo(int init, int end){
      seqNo = 0; 
      for(int k=init; k <= end; k++){
        seqNo += (int)Math.pow(256, end-k)*(frame[k] & 0xff);
      }   
    }
    /**
     * Set the Actime Messsage Type
     * @param init -  the intial position of the AM Type in the frane
     * @param end -  The final position of the AM Type in the frame.
     **/
    public void setAMType(int init, int end){
      amType = 0; //(frame[init] & 0xff);
      for(int k = init; k <= end; k++){
        amType += (int)Math.pow(256, end-k)*(frame[k] & 0xff);
      }
    }
    public void setGroupID(int init, int end){
       grpId = 0;
      for(int k=init, exp=0; k <= end; k++, exp++){
        grpId += (int)Math.pow(256, exp)*(frame[k] & 0xff);
      }
    
    }
    public void setNetworkByte(int init, int end){
        network = 0;
        for(int k=init, exp=0; k <= end ; k++, exp++ ){
            network +=(int)Math.pow(256, exp)*(frame[k] & 0xff);
        }
    }
    
    /**
     * Set the base node time
     * @param a -  the initial position of the field in the frame
     * @param end -  the final position of the field in the frame
     */
    public void setCurrTime(int a, int end){
      currTime = 0;
     for(int k = a; k <= end; k++){
        currTime += (int)Math.pow(256, end-k)*(frame[k] & 0xff);
      }    
    }
      
    private void setElapsedTime(){
       if(control == false){
         this.timestamp = 0;
         this.control = true;
       }else{
           this.timestamp +=(float)(currTime-diffTime)/1024.0;
       }
       diffTime = currTime;
    }
    
    public void resetElapsedTime(){
      this.timestamp = 0;
      this.diffTime  = 0;
      control = false;
    }
    
    private void setPayload(int init, int end){     
      payload = new byte[end-init];
      System.arraycopy(frame, init, payload, 0, payload.length);
    }
    
    private void setSenderTime(int init, int end){
      scurrTime = 0;
      for(int k=init; k <= end; k++){
        scurrTime += (int)Math.pow(256, end-k)*(frame[k] & 0xff);
      }  
    }
    
    private void setSenderSeqno(int init, int end){
      sseqno = 0;
      for(int k=init; k <= end; k++){
        sseqno += (int)Math.pow(256, end-k)*(frame[k] & 0xff);
      }  
    }
    
    private void setSenderNPackets(int init, int end){
      snpkts = 0;      
      for(int k=init; k <= end; k++){
        snpkts += (int)Math.pow(256, end-k)*(frame[k] & 0xff);
      }  
    }
    
    private void setSenderNBytes(int init, int end){
     snbytes = 0;     
      for(int k=init; k <= end; k++){
        snbytes += (int)Math.pow(256, end-k)*(frame[k] & 0xff);
      }  
    }
    
    private void setMonitType(int init, int end){
       this.montype = 0;     
      for(int k=init; k <= end; k++){
        montype += (int)Math.pow(256, end-k)*(frame[k] & 0xff);
      }  
    }
    
    private void setOrdinaryType(int init, int end){
      this.ordtype = 0;     
      for(int k=init; k <= end; k++){
        ordtype += (int)Math.pow(256, end-k)*(frame[k] & 0xff);
      }  
    }
    
    
    private void setSenderHopCount(int init){
       hdrHopCount = (int)(frame[init] & 0xff);
    }
    
    private void setRoutingHdrOrigAddr(int init, int end){
        this.hdrOrigAddr = 0;
        for(int k = init; k <= end; k++){
            this.hdrOrigAddr += (int)Math.pow(256, end-k)*(frame[k] & 0xff);
        }
    }    
    
    private void setRoutingHdrSeqno(int init, int end){  
        this.hdrDataSeqno = 0;
        for(int k = init; k <= end; k++){
            this.hdrDataSeqno += (int)Math.pow(256, end-k)*(frame[k] & 0xff);
        }    
    }
    
    private void setRoutingHdrType(int init){
            hdrType = (int)(frame[17] & 0xff);
    }
    
    public synchronized String getParsedFrame(byte[] orig){
       frame = new byte[orig.length];
       System.arraycopy(orig, 0, frame, 0, frame.length);
       
       this.setSeqNo(this.seqInit, this.seqEnd);
       this.setMsgLength(this.msglenInit, this.msglenEnd);
       this.setSourceId(this.srcInit, this.srcEnd);
       this.setDestination(this.destInit, this.destEnd);
       this.setFCF(this.fcfInit, this.fcfEnd);
       this.setGroupID(this.grpInit, this.grpEnd);
       this.setAMType(this.amInit, this.amEnd);
       this.setNetworkByte(netwInit, netwEnd);
       this.frameLength(this.frmLenInit, this.frmLenEnd); 
       this.setCurrTime(this.currTinit, this.currTend); 
       //this.setPayload(payloadInit, payloadInit+this.msgLen);
       this.setElapsedTime();
       if(checkHeader){
          pos0 = payloadInit+m_flag0Init+ROUTING_HEADER_OFFSET;
          pos1 = payloadInit+M_STRUCTSIZE-1 +ROUTING_HEADER_OFFSET; 
          msgLenOffset = M_STRUCTSIZE + ROUTING_HEADER_OFFSET;
          rOffset = ROUTING_HEADER_OFFSET;          
       }else{
         pos0 = payloadInit+m_flag0Init;
         pos1 = payloadInit+M_STRUCTSIZE-1;
         msgLenOffset = M_STRUCTSIZE;
         rOffset  = 0;
       }
       
               
       
       if(frame[msglenInit] == msgLenOffset && frame[pos0] == M_DELIMITER_BYTE && frame[pos1] == M_DELIMITER_BYTE){
                
                this.monitorpkt = true;
                
                if(checkHeader){
                    this.setSenderHopCount(payloadInit+rhdrHopsInit);  //to be actualized
                    this.setRoutingHdrSeqno(payloadInit+rhdrSeqnoInit, payloadInit+rhdrSeqnoEnd);
                    this.setRoutingHdrOrigAddr(payloadInit+rhdrOrigInit, payloadInit+rhdrOrigEnd);
                    this.setRoutingHdrType(payloadInit+rhdrTypeInit);
                }                
                this.setSenderTime(m_timeInit+rOffset+payloadInit, m_timeEnd+rOffset+payloadInit);
                this.setSenderSeqno(m_seqnoInit+rOffset+payloadInit, m_seqnoEnd+rOffset+payloadInit);
                this.setSenderNPackets(m_npktInit+rOffset+payloadInit, m_npktEnd+rOffset+payloadInit);
                this.setSenderNBytes(m_nBytesInit+rOffset+payloadInit, m_nBytesEnd+rOffset+payloadInit);
                this.setMonitType(m_typeInit+rOffset+payloadInit, m_typeEnd+rOffset+payloadInit );
                
                String str ="", str2="";
                if(checkHeader){
                    str+= format(getRoutingHdrSeqno(), 5)+SEP2;
                    str2+=format(getRoutingHdrSeqno(), 5)+SEP2;
                }else{
                    str+=SEP7;
                    str2+=SEP7;
                }
                str+=format(this.getSeqNo(), 4)+SEP2;
                str+= format(this.sseqno+"", 5)+SEP2;
                str+= format(this.getSourceId(), 4)+SEP2;
                str+= format(this.currTime+"", 8)+SEP2;
                str+= format(this.getElapsedTime()+"", 11)+SEP2;
                str+= format(this.scurrTime+"", 8)+SEP2;
                str+= format(snbytes+"", 12)+SEP2;
                str+= format(snpkts+"", 8)+"\n";
                
                str2+= format(this.getSeqNo(), 4)+SEP2;                
                str2+= format(this.getSourceId(), 4)+SEP2;
                str2+= format(this.getDestination(), 4)+SEP2;
                str2+= format(this.frame.length+"", 2)+SEP2;
                str2+= format(this.msgLen+"", 2)+SEP2;
                str2+= format(this.currTime+"", 8)+SEP2;
                str2+= format(this.getElapsedTime()+"", 11)+SEP2;
                str2+= MSG_TYPE[1]+SEP2;
                if(checkHeader){
                   str2+=getRoutingHeader2();
                }  
                str2+= this.getPayload()+"\n";               
                
                WriteOutputFiles.instance().WriteMonitorTrafficFile(this.getKeyId(), str);
                WriteOutputFiles.instance().WriteOrdinaryTrafficFiles(this.getKeyId(), str2);
                WriteOutputFiles.instance().WriteAllTrafficPktFile(str2);
                
                return this.formatScreen(0, MSG_TYPE[1].replaceAll("_PKT", ""));
                
       }else{
               this.monitorpkt = false;                       
               
               this.setOrdinaryType(ordtypeInit+payloadInit, ordtypeEnd+payloadInit); 
               if(checkHeader){
                    this.setSenderHopCount(payloadInit+rhdrHopsInit);  //to be actualized
                    this.setRoutingHdrSeqno(payloadInit+rhdrSeqnoInit, payloadInit+rhdrSeqnoEnd);
                    this.setRoutingHdrOrigAddr(payloadInit+rhdrOrigInit, payloadInit+rhdrOrigEnd);
                    this.setRoutingHdrType(payloadInit+rhdrTypeInit);
                }    
                
                String str2="";
                if(checkHeader){                    
                    str2+=format(getRoutingHdrSeqno(), 5)+SEP2;
                }else{                    
                    str2+=SEP7;
                }
                
                str2+=format(this.getSeqNo(), 4)+SEP2;                
                str2+= format(this.getSourceId(), 4)+SEP2;
                str2+= format(this.getDestination(), 4)+SEP2;
                str2+= format(this.frame.length+"", 2)+SEP2;
                str2+= format(this.msgLen+"", 2)+SEP2;
                str2+= format(this.currTime+"", 8)+SEP2;
                str2+= format(this.getElapsedTime()+"", 11)+SEP2;
                
                if(amType == AM_BEACONMSG && destId == 0xFFFF && msgLen == 28){
                    str2+= MSG_TYPE[2]+SEP2;
                }else{
                    str2+= MSG_TYPE[0]+SEP2;
                }
                if(checkHeader){
                   str2+=getRoutingHeader2();
                }    
                str2+= this.getPayload()+"\n";               
                                
                WriteOutputFiles.instance().WriteOrdinaryTrafficFiles(this.getKeyId(), str2);
                WriteOutputFiles.instance().WriteAllTrafficPktFile(str2);
                
               int idx = 0;
               if(amType == AM_BEACONMSG && destId == 0xFFFF && msgLen == 28){
                   idx = 2;
               }
               return this.formatScreen(0, MSG_TYPE[idx].replaceAll("_PKT", ""));          
       }                   
      
    }
    
    private String formatScreen(int type, String append){
       String bar ="------------------------------------------------------------------------------------------------------\n";
       String screen0 = "";
       screen0 += format(this.getElapsedTime()+"", 11);
       screen0 += SEP12+SEP2;
       screen0 += format(this.getSeqNo(),3);
       screen0 += SEP10;              
       screen0 += format(this.getSourceId(),4);
       screen0 += SEP5;
       screen0 += format(this.getDestination(),4);
       screen0 += SEP10;
       screen0 += format(this.msgLen+"",2);
       screen0 += SEP12;
       screen0 += format(this.frame.length+"",2);
       screen0 += SEP10;
       screen0 += format(this.getAMType(),2);
       screen0 += SEP10;
       screen0 += format(this.getGroupId(),4);
       screen0 += SEP6;
       screen0 += format(Integer.toHexString(this.fcf).toUpperCase(),4);
       screen0 += SEP10;       
       screen0 += format(this.getNetworkByte(),2);
       screen0 += SEP12;
       screen0 += append+"\n";          
       
       return screen0;
    }
    
    private String getKeyId(){
        if(this.checkHeader){
            if(srcId != hdrOrigAddr){
                return (this.hdrOrigAddr & 0xff)+"";
            }else{
                return ""+(this.srcId & 0xff)+"";
            }           
        }else{
            return ""+(this.srcId & 0xff)+"";
        }
        
    }
    /**
     * Returns the sender node ID/address
     * return -  the sender node ID
     */
    private String getSourceId(){
        String src = Integer.toHexString(this.srcId & 0xff).toUpperCase();
       
      return src;
    }
    /**
     * returns the Actime Message Type
     * return -  the AMTYPE
     */
    private String getAMType(){
      return Integer.toHexString(this.amType).toUpperCase();
    }
    
    private String getSeqNo(){       
      return this.seqNo+"";
    }
    
    private String getGroupId(){
        String grp = Integer.toHexString(this.grpId).toUpperCase();
      return grp; 
    }
    
    private String getCurrTime(){
      return ""+this.currTime+"";
    }    
    
    private String getSenderTime(){
        return ""+this.scurrTime+"";
    }
    
    private String getSenderSeqno(){
        return "0x"+Integer.toHexString(this.sseqno)+"";
    }
    private String getSenderNPackets(){
        return ""+this.snpkts+"";
    }
    private String getSenderNBytes(){
        return ""+this.snbytes+"";
    }
    
    //Routing Header
    private String getRoutingHdrType(){
        return "0"+Integer.toHexString(hdrType)+"";
    }
    
    private String getRoutingHdrOrigAddr(){
        return Integer.toHexString(hdrOrigAddr);
    }
    
    private String getRoutingHdrSeqno(){
        return hdrDataSeqno+"";
    }
    
    private String getRoutingHdrHopCount(){
        return "0"+Integer.toHexString(hdrHopCount);
    }
    
    private String getRoutingHeader(){
        String str ="ROUTING: ";
        String str1="";
        for(int k = payloadInit; k < (payloadInit+ROUTING_HEADER_OFFSET); k++){           
            str1= Integer.toHexString(frame[k] & 0xff).toUpperCase();
            if(str1.length() == 1) str1=" 0"+str1;
            else str1 = " "+str1;
            str += str1;
        }
        return str;
    }
     private String getRoutingHeader2(){
        String str ="";
        String str1="";
        for(int k = payloadInit; k < (payloadInit+ROUTING_HEADER_OFFSET); k++){           
            str1= Integer.toHexString(frame[k] & 0xff).toUpperCase();
            if(str1.length() == 1) str1=" 0"+str1;
            else str1 = " "+str1;
            str += str1;
        }
        return str+" | ";
    }
    
    public float getElapsedTime(){            
      return this.timestamp;
    }
    
    private String getNetworkByte(){
        return Integer.toHexString(network).toUpperCase();
    }
    
    private String getPayloadLen(){
        return Integer.toHexString((frame.length-payloadInit));
    }
    
    private String getDestination(){
      String str=Integer.toHexString(this.destId).toUpperCase()+"";
      
       return str;
    }
    
    private String getFrameLen(){
      return ""+Integer.toHexString(this.frame.length).toUpperCase();
    }        
    
    private String getPayload(){
      int pldInit = 0;
      if(checkHeader){        
        pldInit  = payloadInit+ROUTING_HEADER_OFFSET;
      }else{        
        pldInit = payloadInit;
      }
      String str="";
      String str1="";
      for(int k = pldInit; k < frame.length ; k++){
        str1= Integer.toHexString(frame[k] & 0xff).toUpperCase();
        if(str1.length() == 1) str1=" 0"+str1;
        else str1 = " "+str1;
        str += str1;
      }
      return str;
    } 
    private String getHeader(){
        String str ="";
        String str1="";
        for(int k = 0; k < (payloadInit-5); k++){  
            if(frame != null){
                str1= Integer.toHexString(frame[k] & 0xff).toUpperCase();
                if(str1.length() == 1) str1=" 0"+str1;
                else str1 = " "+str1;
                str += str1;
            }
        }
        return str;
    }
    
    private String getTimeStamp(){
        String str = "TIMESTAMP: ";
        String str1="";
        for(int k = currTinit; k < msglenInit; k++){            
            str1= Integer.toHexString(frame[k] & 0xff).toUpperCase();
           
            if(str1.length() == 1) str1=" 0"+str1;
            else str1 = " "+str1;
            
            /*if((k == msglenInit)){
                str1 = "\tPLD_LEN: " + (frame[k] & 0xff);
            }*/
            str += str1;
        }
        str += "     PLD_LEN: "+ (frame[msglenInit]&0xff);
        return str;
    }
    
    private String getFrame(){
      String str = "";
      String str1="";
      //for(int k = 0; k < this.frameLen+5; k++){       
      for(int k = 0; k < this.frame.length; k++){       
        str1= Integer.toHexString(frame[k] & 0xff).toUpperCase();
        if(str1.length() == 1) str1 =" 0"+str1;
        else str1 = " "+str1;
        str +=str1;
      }            
      return str;
    }
    
    public String getDisplay(){
        String strb = "";
         strb  = format(this.getElapsedTime()+"", 11)+"\n"+this.getSeqNo()+"\n";         
         strb += this.getHeader()+"\n"+this.getTimeStamp()+"\n"; 
         if(checkHeader){
            strb+= this.getRoutingHeader()+"  DATA: ";
         }else{
            strb+="";
         }
         strb += this.getPayload(); 
         strb += "\tTYPE : ";
         if(amType == AM_BEACONMSG && destId == 0xFFFF && msgLen == 28){
            strb += "BEACON_PKT\n";
         }else if(monitorpkt){
            strb += "MONITOR_PKT\n";
         }else{
            strb += "ORDINARY_PKT\n";
         }
         strb += this.getFrame()+"\n";
         strb+="-----------------------------------------------------------";
        strb+="----------------------------------------------------------------\n\n";
        return strb;
    }
    
    public static  String format(String field, int size){
        
        int offset = size - field.length();
        
        if(offset < 0 || offset >11){
            offset = 0;
        }
        
        return (ZEROS[offset]+field);
    }
    
    public static  String formatInv(String field, int size){
         
        int offset = size - field.length();
         
        if(offset < 0 || offset >11){
            offset = 0;
        }
        
        return (field+ZEROS[offset]);
    }
    public String toString(){
      
          NumberFormat nf = NumberFormat.getInstance();
                    
        return "";
    }              
}
