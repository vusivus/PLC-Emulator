/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vts.jplceditor.comms.usb;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;
import purejavahidapi.DeviceRemovalListener;
import purejavahidapi.HidDevice;
import purejavahidapi.HidDeviceInfo;
import purejavahidapi.InputReportListener;
import purejavahidapi.PureJavaHidApi;

/**
 *
 * @author Vusman
 */
public class HidManager implements AutoCloseable {

    private final int vendorId = 0x1234;
    private final int productId = 0x001;
    private final byte inputBuffer = 64;
    private final byte outputBuffer = 64;
    private final String vendorName = "VT_Labs";
    private final String productName = "VT_PLC";
    private HidDeviceInfo devInfo;
    private HidDevice device;
    private boolean deviceFound, connected, ack;
    private ByteBuffer receiveBuf;
    private final ByteBuffer transmitBuf;
    private short receiveLen;
    private List<ByteBuffer> readData;
    private ExecutorService executor;

    /**
     *
     */
    public HidManager() {
        transmitBuf = ByteBuffer.allocate(64);
        receiveBuf = ByteBuffer.allocate(128);
        transmitBuf.order(ByteOrder.BIG_ENDIAN);
        receiveBuf.order(ByteOrder.BIG_ENDIAN);
        executor = Executors.newSingleThreadExecutor();
    }

    /**
     *
     * @param feedback
     * @return
     */
    public boolean locatePlc(StringBuilder feedback) {
        List<HidDeviceInfo> devList = PureJavaHidApi.enumerateDevices();
        for (HidDeviceInfo info : devList) {
            if (info.getVendorId() == (short) vendorId && info.getProductId() == (short) productId) {
                devInfo = info;
                deviceFound = true;
                feedback.append("\nPLC Device found");
                return true;
            }
        }
        feedback.append("\nCould Not find PLC device");
        deviceFound = false;
        return false;
    }

    /**
     *
     * @param feedback
     * @return
     */
    public boolean connectPlc(StringBuilder feedback) {
        if (connected) {
            shutdownAndAwaitTermination(executor);
            executor = Executors.newSingleThreadExecutor();
            return true;
        }
        feedback.append("\n");
        if (deviceFound) {
            try {
                device = PureJavaHidApi.openDevice(devInfo);
                device.setInputReportListener(reportListener);
                device.setDeviceRemovalListener(removalListener);
                //Create Packet to connect to PLC
                PlcMessage connect = new PlcMessage(PlcMessage.cmdCONNECT);
                transmitBuf.clear(); //clear transmit buffer
                PlcFormatter.formatMessage(connect, transmitBuf);
                connected = write64ToPlc(transmitBuf.array());
                feedback.append("\nSent output report requesting connection to ");
                feedback.append(device.getHidDeviceInfo().getProductString());
                feedback.append("\nWaiting for response...");
                byte code = 0;
                if (connected) {
                    feedback.append("\n Received Response from PLC");
                    code = receiveBuf.get(2);
                    feedback.append("\nResponse code: ").append(String.format("0x%02X", code));
                } else {
                    feedback.append("\nDevice Timed Out. Could not get response from PLC");
                }
                connected = code == 0x06;
            } catch (IOException ex) {
                Logger.getLogger(HidManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            feedback.append("Device Not Found Yet. You need to locate PLC first");
        }
        return connected;
    }

    /**
     *
     * @param data
     * @return
     */
    public boolean write64ToPlc(byte[] data) {
        boolean responded = false;
        if (deviceFound) {
            try {
                receiveLen = 0;
                ack = false;
                Future<Boolean> responseResult = executor.submit(new UsbPortResponse());
                device.setOutputReport((byte) 0, data, 64);
                responded = responseResult.get(2000, TimeUnit.MILLISECONDS);
            } catch (InterruptedException | ExecutionException | TimeoutException ex) {
                //shutdownAndAwaitTermination(executor);
                Logger.getLogger(HidManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return responded;
    }

    /**
     *
     * @param message
     * @return
     * @throws Exception
     */
    public boolean sendCmd(PlcMessage message) throws Exception {
        boolean responded = false;
        if (connected) {
            for (int i = 0; i < 64; i++) {
                transmitBuf.put(i, (byte) 0);
            }
            transmitBuf.clear();
            PlcFormatter.formatMessage(message, transmitBuf);
            responded = write64ToPlc(transmitBuf.array());
        }
        return responded;
    }

    /**
     *
     * @param messages
     * @param feedback
     * @return
     */
    public boolean handleBullkTransfer(List<PlcMessage> messages, StringBuilder feedback) {
        boolean responded;
        if (connected) {
            int size = messages.size();
            feedback.append("\nWriting To PLC.");
            for (int k = 0; k < size; k++) {
                try {
                    //first empty the transmit buff
                    for (int i = 0; i < 64; i++) {
                        transmitBuf.put(i, (byte) 0);
                    }
                    transmitBuf.clear();
                    //Encode each message
                    PlcFormatter.formatMessage(messages.get(k), transmitBuf);
                    feedback.append("\nSent packet number :").append(k).append(". Waiting for response");
                    receiveLen = 0;
                    ack = false;
                    //send the packet
                    Future<Boolean> responseResult = executor.submit(new UsbPortResponse());
                    device.setOutputReport((byte) 0, transmitBuf.array(), 64);
                    responded = responseResult.get(5000, TimeUnit.MILLISECONDS);
                    if (!responded) {
                        feedback.append("\nPLC did not acknowldge request. Ending session");
                        return false;
                    }
                    ack = receiveBuf.get(2) == 0x06;
                    if (ack) {
                        feedback.append("\nPLC has acknowldged request.");
                        Thread.sleep(500);
                    } else if (!ack) {
                        feedback.append("\nPLC has responded but did not acknowledge request.");
                        return false;
                    }
                } catch (InterruptedException | ExecutionException | TimeoutException ex) {
                    shutdownAndAwaitTermination(executor);
                    Logger.getLogger(HidManager.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return ack;
    }

    /**
     *
     * @param messages
     * @param feedback
     * @return
     */
    public boolean handleBullkRead(List<PlcMessage> messages, StringBuilder feedback) {
        boolean responded = false;
        readData = new ArrayList<>();
        int size = messages.size();
        feedback.append("\nReading From PLC.");
        for (int k = 0; k < size; k++) {
            try {
                for (int i = 0; i < 64; i++) {
                    transmitBuf.put(i, (byte) 0);
                }
                transmitBuf.clear();
                PlcFormatter.formatMessage(messages.get(k), transmitBuf);
                feedback.append("\nSent packet number :").append(k).append(". Waiting for response");
                receiveLen = 0;
                Future<Boolean> responseResult = executor.submit(new UsbPortResponse());
                device.setOutputReport((byte) 0, transmitBuf.array(), 64);
                responded = responseResult.get(5000, TimeUnit.MILLISECONDS);
                if (!responded) {
                    feedback.append("\nPLC did not acknowldge request. Ending session");
                    break;
                } else {
                    feedback.append("\nPLC has acknowldged request.");
                    readData.add(receiveBuf.duplicate());
                    Thread.sleep(500);
                }
            } catch (InterruptedException | ExecutionException | TimeoutException ex) {
                shutdownAndAwaitTermination(executor);
                Logger.getLogger(HidManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return responded;
    }

    private final DeviceRemovalListener removalListener = new DeviceRemovalListener() {
        @Override
        public void onDeviceRemoval(HidDevice hd) {
            System.out.println("Device Removed");
            if (executor != null) {
                shutdownAndAwaitTermination(executor);
            }
        }
    };

    private final InputReportListener reportListener = new InputReportListener() {
        @Override
        public void onInputReport(HidDevice hd, byte id, byte[] bytes, int len) {
            if (len == 64) {
                receiveBuf.clear();
                System.out.print("\nReceived " + len + " bytes with ID " + id + ": ");
                for (int i = 0; i < len; i++) {
                    receiveBuf.put(i, bytes[i]);
                    System.out.printf("%02X ", bytes[i]);
                }
            }
            receiveLen = (short) len;
        }
    };

    /**
     *
     */
    private void usbShutDown() {
        if (executor != null) {
            shutdownAndAwaitTermination(executor);
        }
        if (connected) {
            device.close();
        }
    }

    private void shutdownAndAwaitTermination(ExecutorService pool) {
        pool.shutdown(); // Disable new tasks from being submitted
        try {
            // Wait a while for existing tasks to terminate
            if (!pool.awaitTermination(60, TimeUnit.SECONDS)) {
                pool.shutdownNow(); // Cancel currently executing tasks
                // Wait a while for tasks to respond to being cancelled
                if (!pool.awaitTermination(60, TimeUnit.SECONDS)) {
                    System.err.println("Pool did not terminate");
                }
            }
        } catch (InterruptedException ie) {
            // (Re-)Cancel if current thread also interrupted
            pool.shutdownNow();
            // Preserve interrupt status
            Thread.currentThread().interrupt();
        }
    }

    /**
     *
     * @return
     */
    public boolean isConnected() {
        return connected;
    }

    /**
     *
     * @return
     */
    public boolean isAck() {
        return ack;
    }

    /**
     *
     * @return
     */
    public short getReceiveLen() {
        return receiveLen;
    }

    /**
     *
     * @return
     */
    public ByteBuffer getTransmitBuf() {
        return transmitBuf;
    }

    /**
     *
     * @return
     */
    public List<ByteBuffer> getReadData() {
        return readData;
    }

    @Override
    public void close() throws Exception {
        System.out.println("Device Closed");
        usbShutDown();
    }

    class UsbPortResponse implements Callable<Boolean> {

        @Override
        public Boolean call() throws Exception {
            receiveLen = 0;
            while (receiveLen == 0) {
                Thread.sleep(100);
            }
            return receiveLen > 0;
        }

    }
}
