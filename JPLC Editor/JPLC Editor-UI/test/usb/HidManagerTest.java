/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package usb;

import com.vts.jplceditor.comms.usb.HidManager;
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
public class HidManagerTest {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        //connectPlcTest();
       bulkTransferTest();
    }

    private static void connectPlcTest() {
        HidManager hid = new HidManager();
        StringBuilder feedback = new StringBuilder();
        if (hid.locatePlc(feedback)) {
            hid.connectPlc(feedback);
           // hid.usbShutDown();
        }
        System.out.println(feedback);
    }

    private static void bulkTransferTest() {
        HidManager hid = new HidManager();
        StringBuilder feedback = new StringBuilder();
        if (hid.locatePlc(feedback)) {
            if (hid.connectPlc(feedback)) {
                byte packet[] = new byte[72];
                for (int k = 0; k < 72; k++) {
                    packet[k] = (byte) (k + 1);
                }
                List<PlcMessage> messages = new ArrayList<>();
                SessionManager session = new SessionManager();
                session.createFlashWritePackets(packet, messages);
                hid.handleBullkTransfer(messages, feedback);
            }

           // hid.usbShutDown();
        }
        System.out.println(feedback);
    }
}
