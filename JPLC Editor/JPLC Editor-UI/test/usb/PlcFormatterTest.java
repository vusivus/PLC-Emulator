/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package usb;

import com.vts.jplceditor.comms.usb.PlcFormatter;
import com.vts.jplceditor.comms.usb.PlcMessage;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 *
 * @author Electronix
 */
public class PlcFormatterTest {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        writeFlash();
    }
    
    private static void writeFlash(){
       // TODO code application logic here
        PlcMessage msg = new PlcMessage(PlcMessage.cmdWRITE_FLASH);
        msg.setAddress(0x2000);
        msg.setLength((short)15);
        byte[] data = new byte[32];
        for(int i=0;i<32;i++){
            data[i]=(byte) (i*3 +2);
        }
        msg.setData(data);
        
        ByteBuffer buffer = ByteBuffer.allocate(64);
        buffer.order(ByteOrder.BIG_ENDIAN);
        PlcFormatter.formatMessage(msg, buffer);
        for(int i=0;i<64;i++){
            System.out.println(String.format("0x%02X",buffer.get(i)));
        } 
    }
    
}
