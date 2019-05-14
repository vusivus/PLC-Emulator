/* VT Labs USB Protocol Description.

    <DLE><STX><CMD><ADDRESS><LENGTH> <DATA[LENGTH]><DLE><ETX>
    |--1-|-1 -|-1--|-- 2 ---|---1---|----LENGTH--- |--1-|-1 -|

    DLE      - DF1 Protocol start delimiter.
               Length: 1 byte. Mandatory.
    STX      - Message start delimiter.
               Length: 1 byte. Mandatory.
    CMD      - Command index (Cmd).
               Length: 1 byte. Mandatory.
    ADDRESS  - Address field. Flash start address for
               CMD command operation.
               Length: 2 bytes. Optional (command specific).
    LENGTH    - Length field. Amount of data/blocks for
               CMD command operation.
               Length: 1 byte. Optional (command specific).
    DATA     - Data array.
               Length: Length bytes. Optional (command specific).
    ETX      - Message end delimiter.
               Length: 1 byte. Mandatory.
  Some commands do not utilize all of these fields.
  See 'Command Table' below for details on specific command's format.

 * Command Table.
   --------------------------+---------------------------------------------------
  |       Description        |                      Format                       |
  |--------------------------+---------------------------------------------------|
  | Connect with PC          |         <DLE><STX><cmd><DLE><ETX>                 |
  |--------------------------+---------------------------------------------------|
  | Flash Erase              |     <DLE><STX><cmd><address><length><DLE><ETX>    |
  |--------------------------+---------------------------------------------------|
  | Read From PLC            |     <DLE><STX><cmd><address><length><DLE><ETX>    |
  |--------------------------+---------------------------------------------------|
  | Write To PLC             | <DLE><STX><cmd><address><length><data><DLE><ETX>  |
  |--------------------------+---------------------------------------------------|
 * Acknowledge format.

    <DLE><CMD_CODE><STATUS><TRANACTIOD ID>
    |-- 1|---- 1 -|-- 1 ---|---- 2 -------|

    DLE            - Response start delimiter (for future upgrades).
                     Length: 1 byte. Mandatory.
    CMD_CODE       - The Command we want to acknowledge.
                     Length: 1 byte. Mandatory.
    STATUS         - The status of the command execution.
                     Length: 1 byte. Mandatory.
    TRANSACTION ID - Command execution number. Field is incremented
                     Every time an acknowledgement is sent.
                     Length: 2 bytes. Mandatory.
 *    See 'Acknowledgement Table' below for details on specific command's
  acknowledgement status codes.

 * Acknowledgement Table.
   --------------------+-----------------------------------------|
  | Status Code        |                   Description           |
  |--------------------+-----------------------------------------|
  |     0x06           |       Command successfully executed     |
  |--------------------+-----------------------------------------|
  |     0x0F           |       Command not executed              |
  |--------------------+-----------------------------------------|
  |     0x00           |       Unsupported Command               |
  |--------------------+-----------------------------------------|
   ---------------------------------------------------------------------------*/
#include <built_in.h>
//=== DF! Protocol Constants ===================================================
//Protocol is currently not fully implemented
const DLE = 0x10;
const STX = 0x02;
const ACK = 0x06;
const NAK = 0x0F;
const ETX = 0x03;
//=== Encapsulation Protocol commands ==========================================

enum Command {
    cmdFREE, cmdWRITE_FLASH, cmdREAD_FLASH, cmdWRITE_RAM, cmdREAD_RAM, cmdERASE, cmdCONNECT
};
//==== all the functions =======================================================
void runMode();
void programMode();
void decodeData();
void sendAck();
void userProgram() org 0x2000;
void checkUsb();
void serviceRunCmd();
//Variables will start at 0x60
//===== Local I/O image variables ==============================================
unsigned int anIn0 absolute 0x60;
unsigned int anIn1 absolute 0x62;
unsigned int anIn2 absolute 0x64;
unsigned int anIn3 absolute 0x66;
unsigned int anIn4 absolute 0x68;
unsigned int anIn5 absolute 0x6A;
unsigned int anIn6 absolute 0x6C;
unsigned int anIn7 absolute 0x6E;
char dii absolute 0x70;
unsigned int anOut absolute 0x71;
char dio absolute 0x73;
//array with the order of all Inputs and Outputs
unsigned short io[20] absolute 0x60;
//==== USB variables and buffer ================================================
unsigned char readbuff[64] absolute 0x500; // USB Buffers in Bank 6
unsigned char writebuff[64] absolute 0x540;
unsigned char Reserve5thBankForUSB[256] absolute 0x400; // Dummy allocation of 5th bank
// (used by USB module internaly),to prevent compiler from allocating it
unsigned short length absolute 0x74; //length of data section of protocol
unsigned int address absolute 0x75;
char flags absolute 0x77;
unsigned short state absolute 0x78;
unsigned int transactionID absolute 0x79;
sbit programming at flags.b0;
//==== Other variables =========================================================
short session absolute 0x7B;
short tempByte absolute 0x7C;
short i absolute 0x7D, k absolute 0x7E;
short ramAddress absolute 0x7F;
short ramByte absolute 0x80;
char rxByte;
enum Command cmd;
//======= LCD Initialization ===================================================
// Lcd pinout settings
sbit LCD_RS at RB5_bit;
sbit LCD_EN at RB4_bit;
sbit LCD_D7 at RB3_bit;
sbit LCD_D6 at RB2_bit;
sbit LCD_D5 at RB1_bit;
sbit LCD_D4 at RB0_bit;

// Pin direction
sbit LCD_RS_Direction at TRISB5_bit;
sbit LCD_EN_Direction at TRISB4_bit;
sbit LCD_D7_Direction at TRISB3_bit;
sbit LCD_D6_Direction at TRISB2_bit;
sbit LCD_D5_Direction at TRISB1_bit;
sbit LCD_D4_Direction at TRISB0_bit;
//===== I/O connections ========================================================
sbit connected at rc0_bit;
sbit programmed at rc2_bit;
sbit plc_mode at ra0_bit;

sbit connected_dir at trisc0_bit;
sbit programmed_dir at trisc2_bit;
sbit plc_mode_dir at trisa0_bit;
//==== Other constants =========================================================
const unsigned int OS_SIZE = 8022; //byte size of the operationg system
const unsigned int PROGRAM_START = 8192; //0x2000
//=== Reserved Banks for user program to prevent compiler from using them ======
unsigned char Reserved1stBankForOS[256] absolute 0x00;
unsigned char Reserved2ndBankForUser[256] absolute 0x100;
unsigned char Reserved3rdBankForUser[256] absolute 0x200;

/******************************************************************************
 *                                                                            *
 *  Function:     static void interrupt()                                          *
 *                                                                            *
 *  Description:  Interrupt routine.                                              *
 *                                                                            *
 *  Parameters:   None.                                                       *
 *                                                                            *
 *  Return Value: None.                                                       *
 ******************************************************************************/
void interrupt() {
    //USB interrupt procedure.
    USB_Interrupt_Proc();
    return;
}

/******************************************************************************
 *                                                                            *
 *  Function:     static void main()                                          *
 *                                                                            *
 *  Description:  Main Function.                                              *
 *                                                                            *
 *  Parameters:   None.                                                       *
 *                                                                            *
 *  Return Value: None.                                                       *
 ******************************************************************************/
void main() {
    uart1_init(9600);
    delay_ms(100);
    //----- I/O configuration ------------------------------------------------------
    ADCON1 |= 0x0F; // Configure all ports with analog function as digital
    CMCON |= 7; // Disable comparators

    plc_mode_dir = 1; //plc mode pin is input
    cmd = cmdFREE; //free cmd

    portb = 0;
    trisb = 0; //lcd port

    //---- Check whether PLC is programmed ----------------------------------------
    connected_dir = 0; //connected pin is output
    programmed_dir = 0;
    connected = 0;
    for (k = 0; k < 2; k++) {
        if (flash_read(0x2007 + k) == 0xFF)
            programmed = 0;
        else
            programmed = 1;
    }


    //---- Modules Initialization --------------------------------------------------------------------
    Lcd_Init(); // Initialize LCD
    Lcd_Cmd(_LCD_CLEAR); // Clear display
    Lcd_Cmd(_LCD_CURSOR_OFF); // Cursor off
    //USB Initialization
    HID_Enable(&readbuff, &writebuff); // Enable HID communication
    delay_ms(100); //wait for initialization to complete
    //Program loop
    while (1) {
        lcd_cmd(_lcd_clear);
        if (plc_mode) {
            lcd_out(1, 1, "RUN MODE");
            runMode();
        } else {
            lcd_out(1, 1, "PROGRAM MODE");
            programMode();
        }
    }
}

/******************************************************************************
 *                                                                            *
 *  Function:     static void runMode()                                       *
 *                                                                            *
 *  Description:  Puts PLC in Run Mode.                                       *
 *                                                                            *
 *  Parameters:   None.                                                       *
 *                                                                            *
 *  Return Value: None.                                                       *
 *                                                                            *
 *  Requirements: PLC must be programmed.                                     *
 *                                                                            *
 ******************************************************************************/
void runMode() {
run_loop:
    //Read Inputs
    for (session = 0; session < 17; session++) { //read 17 bytes worth of input data
        uart1_write(cmdREAD_RAM); //send request for data to be read
        delay_ms(5);
        if (uart1_data_ready())
            io[session] = uart1_read();
    }
    //Run program
    if (programmed)
        userProgram();
    //Write outputs
    for (session = 17; session < 20; session++) {
        if (UART1_Tx_Idle()) //if transmission to finished
            uart1_write(io[session]);
        delay_ms(5);
    }
    //LCD function
    lcd_cmd(_lcd_shift_right);
    checkUsb();
    if (rxByte)
        serviceRunCmd();
    delay_ms(100);
    if (plc_mode) goto run_loop;
    //else switch to program mode.
    return;
}

/******************************************************************************
 *                                                                            *
 *  Function:     static void programMode()                                   *
 *                                                                            *
 *  Description:  Puts PLC in Program Mode.                                   *
 *                                                                            *
 *  Parameters:   None.                                                       *
 *                                                                            *
 *  Return Value: None.                                                       *
 *                                                                            *
 *  Requirements: None                                                        *
 *                                                                            *
 ******************************************************************************/
void programMode() {
program_loop:// program mode loop starts here
    checkUsb();
    if (rxByte) { // Do we have an incoming?
        switch (cmd) { // Process command.
            case cmdERASE: // Cmd: Disconnect PLC and PC app.
            {
                state = ACK;
                for (k = 0; k < length; k++) {
                    if (address >= PROGRAM_START) {
                        Flash_Erase_64(address);
                        address += 64;
                    } else {
                        state = NAK;
                    }
                }
                ++transactionID;
                sendAck();
                cmd = cmdFREE;
                break;
            }
            case cmdCONNECT: // Cmd: Connect PLC and PC app.
            {
                connected = 1;
                lcd_out(2, 1, "CONNECTED TO PC.");
                state = ACK;
                transactionID = 0;
                sendAck();
                cmd = cmdFREE; // Set 'Idle' command code.
                break;
            }
            case cmdWRITE_FLASH:
            { // Cmd: Write to program memory.
                state = NAK;
                if (address >= PROGRAM_START) { //If received address is inside OS
                    FLASH_Write_32(address, &readbuff[6]); //write data to Flash
                    lcd_out(2, 1, "PROGRAMMING.....");
                    state = ACK;
                    programmed = 1;
                }
                ++transactionID;
                sendACk();
                cmd = cmdFREE;
                break;
            }
            case cmdREAD_FLASH:
            { // Cmd: Read from program memory.
                if (address >= PROGRAM_START) { //only read user program, not OS
                    FLASH_Read_N_Bytes(address, &writebuff[0], 64); // read 64 bytes starting from address
                    HID_Write(&writebuff[0], 64); // Send data.
                } else {
                    state = NAK;
                    ++transactionID;
                    sendAck();
                }
                cmd = cmdFREE; // Set 'Idle' command code
                break;
            }
        }
    }
    delay_ms(100);
    if (!plc_mode) goto program_loop; //else disable HID device and go back to run mode
    return;
}

void serviceRunCmd() {
    switch (cmd) { // Process command.
        case cmdREAD_RAM:
        { // Cmd: Read from program memory.
            asm movff _address, fsr0;
            for (i = 0; i < length; i++) {
                asm movff indf0, _ramByte;
                writebuff[i] = ramByte;
                asm incf fsr0;
            }
            HID_Write(&writebuff[0], 64); // Send data.
            cmd = cmdFREE; // Set 'Idle' command code
            break;
        }
        case cmdWRITE_RAM:
        { // Cmd: Read from program memory.
            asm movff _address, fsr0;
            for (i = 0; i < length; i++) {
                ramByte = readbuff[i+6];
                asm movff _ramByte, indf0;
                asm incf fsr0;
            }
            state = ACK;
            ++transactionID;
            sendAck();
            cmd = cmdFREE; // Set 'Idle' command code
            break;
        }
    }
    return;
}

void checkUsb() {
    USB_Polling_Proc(); // Check USB for new data.
    rxByte = HID_Read();
    if (rxByte && cmd == cmdFREE) { // Do we have an incoming?
        // Check received packet for new command.
        // Only decode if we in 'Idle' mode
        if (readbuff[0] != DLE) { // Do we have a 'DLE' at start?
            return;
        }
        if (readbuff[1] != STX) { // Do we have a 'STX'?
            return; // received data is not protocol, return.
        }

        // Process received command if first two bytes are protocol.
        cmd = readbuff[2]; // Get command code.
        Hi(address) = readbuff[3]; // Get address hi byte.
        Lo(address) = readbuff[4]; // Get address low byte.
        length = readBuff[5]; // Get counter byte
    }
    return;
}

/******************************************************************************
 *                                                                            *
 *  Function:     static void sendAck()                                       *
 *                                                                            *
 *  Description:  Sends response to PLC via usb                               *
 *                                                                            *
 *  Parameters:   status, the response code                                   *
 *                                                                            *
 *  Return Value: None.                                                       *
 *                                                                            *
 *  Requirements: None                                                        *
 *                                                                            *
 ******************************************************************************/
void sendAck() {
    // Make acknowledgment packet.
    writebuff[0] = DLE; // Start of packet identifier.
    writebuff[1] = cmd; // Command code to acknowledge.
    writebuff[2] = state; //acknowledgment status
    writebuff[3] = Hi(transactionID); //message number
    writebuff[4] = Lo(transactionID);
    for (i = 5; i < 64; i++) {
        writebuff[i] = 0;
    }
    HID_Write(&writebuff[0], 64); // Send acknowledgment packet..
    return;
}

void userProgram() org 0x2000 {
}