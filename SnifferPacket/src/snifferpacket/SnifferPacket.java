/*
 * SnifferPacket.java
 * Created on February 21, 2008, 8:37 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package snifferpacket;
import packetanalyzer.*;
import snifferGUI.PacketAnalyzerGUI;

/**
 *801.15.4 PACKET SNIFFER(JAVA PARSER)
 *KTH - THE ROYAL INSTITUTE OF TECHNOLOGY - STOCKHOLM - SWEDEN
 *SCHOOL OF TECHNOLOGY AND HEALTH - KTH/STH
 *MASTER PROGRAM IN NETWORK SERVICES AND SYSTEMS
 * @author gonga - ANTÓNIO OLIVEIRA GONGA - gonga@kth.se
 */
public class SnifferPacket {
   PacketAnalyzerGUI pagui;
    /** Creates a new instance of SnifferPacket */
    public SnifferPacket() {       
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                pagui = new PacketAnalyzerGUI();
                pagui.setVisible(true);
            }
        });
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        new SnifferPacket();
    }
    
}
