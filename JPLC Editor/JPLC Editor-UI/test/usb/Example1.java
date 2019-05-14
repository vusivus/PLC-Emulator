package usb;

import java.util.List;

import purejavahidapi.*;

/**
 *
 * @author Vusumuzi
 */
public class Example1 {

    /**
     *
     * @param args
     */
    public static void main(String[] args) {
        try {
            List<HidDeviceInfo> devList = PureJavaHidApi.enumerateDevices();
            for (HidDeviceInfo info : devList) {
                System.out.printf("VID = 0x%04X PID = 0x%04X Manufacturer = %s Product = %s Path = %s\n", //
                        info.getVendorId(), //
                        info.getProductId(), //
                        info.getManufacturerString(), //
                        info.getProductString(), //
                        info.getPath());

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
