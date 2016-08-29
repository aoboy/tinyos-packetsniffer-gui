/*
 * XMLTags.java
 *
 * Created on January 16, 2008, 2:10 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package packetanalyzer;

/**
 *
 * @author gonga
 */
public class XMLTags {
    /**The packet tag - instructs the XML parser of a start of a packet description*/
  
    
    public static final  String PKT_TAG        = "pkt_tag";
    
    public static final  String SRC_INIT       = "src_init";
    public static final  String SRC_END        = "src_end";
    
    public static final  String SEQNO_INIT     = "seqno_init";
    public static final  String SEQNO_END      = "seqno_end";
    
    public static final  String MSGLEN_INIT    = "msglen_init";
    public static final  String MSGLEN_END     = "msglen_end";
    
    public static final  String AMTYPE_INIT    = "amtype_init";
    public static final  String AMTYPE_END     = "amtype_end";
    
    public static final  String GRPID_INIT     = "grp_init";
    public static final  String GRPID_END      = "grp_end";
    
    public static final  String CURRTIME_INIT  = "currT_init";
    public static final  String CURRTIME_END   = "currT_end";
    
    public static final  String DIFFTIME_INIT  = "difT_init";
    public static final  String DIFFTIME_END   = "difT_end";
    
    public static final  String PAYLOAD_INIT   = "payload_init";
    public static final  String PAYLOAD_END    = "payload_end";
    
    public static final  String MFLAG0_INIT    = "mflag0_init";
    public static final  String MFLAG0_END     = "mflag0_end";
    
    public static final  String MTYPE_INIT     = "mtype_init";
    public static final  String MTYPE_END      = "mtype_end";
    
    public static final  String STYPE_INIT     = "stype_init";
    public static final  String STYPE_END      = "stype_end";
    
    public static final  String MFLAG1_INIT    = "mflag1_init";
    public static final  String MFLAG1_END     = "mflag1_end";
    
    public static final  String MTIME_INIT     = "mtime_init";
    public static final  String MTIME_END      = "mtime_end";
    
    public static final  String MSEQNO_INIT    = "mseqno_init";
    public static final  String MSEQNO_END     = "mseqno_end";
    
    public static final  String MNPKTS_INIT    = "mnpkts_init";
    public static final  String MNPKTS_END     = "mnpkts_end";
    
    public static final  String MNBYTES_INIT   = "mnbytes_init";
    public static final  String MNBYTES_END    = "mnbytes_end";
    
    public static final  String SSEQNO_INIT    = "sseqno_init";
    public static final  String SSEQNO_END     = "sseqno_end";
    
    public static final  String SNODEID_INIT   = "snodeid_init";
    public static final  String SNODEID_END    = "snodeid_end";
    
    public static final  String STIME_INIT     = "stime_init";
    public static final  String STIME_END      = "stime_end";
    
    public static final  String FCF_INIT       = "fcf_init";
    public static final  String FCF_END        = "fcf_end";
    
    public static final  String DSN_INIT       = "dsn_init";
    public static final  String DSN_END        = "dsn_end";
    
    public static final  String NETWORK_INIT   = "net_init";    
    public static final  String NETWORK_END    = "net_end";
    
    public static final  String FRLEN_INIT     = "frame_init";
    public static final  String FRLEN_END      = "frame_end";
    
    public static final  String DEST_INIT      = "dest_init";
    public static final  String DEST_END       = "dest_end";
    
    public static final  String HDRTYPE_INIT   = "hdrtype_init";
    public static final  String HDRTYPE_END    = "hdrtype_end";
    
    public static final  String HDRHOPS_INIT   = "hdrhops_init";
    public static final  String HDRHOPS_END    = "hdrhops_end";
    
    public static final  String HDRSEQNO_INIT  = "hdrseqno_init";
    public static final  String HDRSEQNO_END   = "hdrseqno_end";
    
    public static final  String HDRORIG_INIT   = "hdrorig_init";
    public static final  String HDRORIG_END    = "hdrorig_end";
    
   
    
    private static final  XMLTags  instance    = null;
    /** Creates a new instance of XMLTags */
    public XMLTags() {
    }
    
   
    
    /*public static final  XMLTags instance(){
       if(instance == null){
          instance = new XMLTags();
       }
       return instance;
    }*/
    
}
