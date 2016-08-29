/*
 * ReceivedBytes.java
 *
 * Created on January 9, 2008, 10:34 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package packetanalyzer;

import java.io.Serializable;

/**
 * KTH - THE ROYAL INSTITUTE OF TECHNOLOGY - STOCKHOLM -  SWEDEN
 * @author gonga - gonga@kth.se
 * @e-mail gonga@kth.se
 */
public class ReceivedBytes implements Serializable {
    private byte[] packet;
    /** Creates a new instance of ReceivedBytes */
    public ReceivedBytes(byte[] b) {
        packet = new byte[b.length];
        System.arraycopy(b, 0, packet, 0, packet.length);
    }
    /**returns an array of bytes*/
    public byte[] returnReceivedBytes(){
        return this.packet;
    }   
}
