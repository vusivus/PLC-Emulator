/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package usb;

import com.vts.jplceditor.comms.usb.PlcFormatter;
import com.vts.jplceditor.comms.usb.PlcMessage;
import com.vts.jplceditor.comms.usb.SessionManager;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Vusumuzi
 */
public class SessionManagerTest {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        //createFlashWritePacketsTest();
        //createFlashReadPacketsTest();
        verifyProgramTest();
    }
    
    private static void createFlashWritePacketsTest(){
      byte packet[]=new byte[72];
      for(int k=0;k<72;k++){
          packet[k] = (byte) (k+1); 
      }
      List<PlcMessage> messages= new ArrayList<>();
      SessionManager session = new SessionManager();
      session.createFlashWritePackets(packet, messages);
      ByteBuffer buffer = ByteBuffer.allocate(64);
      buffer.order(ByteOrder.BIG_ENDIAN);
      
        for (PlcMessage message : messages) {
            buffer.clear();
            PlcFormatter.formatMessage(message, buffer);         
            for(int i=0;i<64;i++){
                System.out.printf(String.format("0x%02X ",buffer.get(i)));
            }
            System.out.println();
        }
    }
    
    private static void createFlashReadPacketsTest(){
      byte packet[]=new byte[72];
      for(int k=0;k<72;k++){
          packet[k] = (byte) (k+1); 
      }
      List<PlcMessage> messages= new ArrayList<>();
      SessionManager session = new SessionManager();
      session.createFlashReadPackets(packet, messages);
      ByteBuffer buffer = ByteBuffer.allocate(64);
      buffer.order(ByteOrder.BIG_ENDIAN);
      
        for (PlcMessage message : messages) {
            buffer.clear();
            PlcFormatter.formatMessage(message, buffer);         
            for(int i=0;i<64;i++){
                System.out.printf(String.format("0x%02X ",buffer.get(i)));
            }
            System.out.println();
        }
    }
    
    private static void verifyProgramTest(){
      byte packet[]=new byte[72];
      for(int k=0;k<72;k++){
          packet[k] = (byte) (k+1); 
      }
      ByteBuffer buffer1 = ByteBuffer.allocate(64);
      buffer1.order(ByteOrder.BIG_ENDIAN);
      
      ByteBuffer buffer2 = ByteBuffer.allocate(64);
      buffer2.order(ByteOrder.BIG_ENDIAN);
      
      for(int i=0;i<64;i++){
          buffer1.put(i, packet[i]);
      }
      for(int i=64;i<72;i++){
          buffer2.put(i-64, (byte) (packet[i]));
      }
      StringBuilder feedback = new StringBuilder();
      
      SessionManager session = new SessionManager();
      List<ByteBuffer> buffers = new ArrayList<>();
      buffers.add(buffer1);
      buffers.add(buffer2);
      session.verifyProgram(buffers, packet, feedback);
      System.out.println(feedback.toString());
    }
}
