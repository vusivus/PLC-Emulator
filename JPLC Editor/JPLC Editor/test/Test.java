
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Vusman
 */
public class Test {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        short adr = 0xA26;
        ByteBuffer buffer = ByteBuffer.allocate(2);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.putShort(adr);
        System.out.println(String.format("0x%02X", (buffer.get(0))));
        System.out.println(String.format("0x%02X", (buffer.get(1))));
    }
    
}
