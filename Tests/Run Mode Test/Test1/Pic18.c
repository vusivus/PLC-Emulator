#define DLE 0x10
#define STX 0x02
#define ENQ 0x05
#define ACK 0x06
#define NAK 0x0F
#define ETX 0x03
#define READ 0X0E
#define WRITE 0xE0
#define ACK 0x06
#define NAK 0x0F
#define ENQ 0x05
#define DONE 0x0B
#define CONNECT 0xFF
#define FREE 0x00
#include <built_in.h>
void runMode();
void readInputs();
void writeOutputs();
void userProgram();
void testMode();
void myISR();

unsigned short temp, count;
int anIn absolute 0x200;
int anOut absolute 0x202;
short dii absolute 0x205;
short dio absolute 0x206;
char line1[8];
char line2[8];
short rxByte, session, txByte;
bit dataReady;

sbit ss at rb2_bit;
sbit plc_mode at ra0_bit;

sbit ss_dir at trisb2_bit;
sbit plc_mode_dir at trisa0_bit;

// Lcd pinout settings
sbit LCD_RS at RB5_bit;
sbit LCD_EN at RB4_bit;
sbit LCD_D7 at RB3_bit;
sbit LCD_D6 at RB2_bit;
sbit LCD_D5 at RB1_bit;
sbit LCD_D4 at RB0_bit;

// Pin direction
sbit LCD_RS_Direction at TRISB5_bit;
sbit LCD_EN_Direction at TRISB.b4;
sbit LCD_D7_Direction at TRISB.B3;
sbit LCD_D6_Direction at TRISB.B2;
sbit LCD_D5_Direction at TRISB.B1;
sbit LCD_D4_Direction at TRISB.B0;

void interrupt() {
    myISR();
}

void interrupt_lo() {
    myISR();
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
    ADCON1 |= 0x0F; // Configure all ports with analog function as digital
    CMCON |= 7; // Disable comparators

    plc_mode_dir = 1;
    ss_dir = 0;
    ss = 1;
    Lcd_Init(); // Initialize LCD
    Lcd_Cmd(_LCD_CLEAR); // Clear display
    Lcd_Cmd(_LCD_UNDERLINE_ON); // Cursor off
    Lcd_Out(1, 1, "ADC Value:"); // Write text in first row
    Lcd_Out(2, 1, "DAC Value:"); // Write text in first row
    delay_ms(500);
    Uart1_Init(9600);
    delay_ms(100);
    //configure Uart interrupt
    intcon = 0;
    intcon.peie = 1;
    pie1.rcie = 1;
    intcon.gie = 1;

    delay_ms(100);
    while (1) {
        runMode();
    }
}

void myISR() {
    if (pir1.rcif) {
        rxByte = uart1_read();
        ++session;
        if (session == 1) {
            dii = rxByte & 0x0F;
        }
        if (session == 2) {
            Lo(anIn) = rxByte;
        }
        if (session == 3) {
            Hi(anIn) = rxByte;
        }
        if (session >= 3) {
            session = 0;
        }
        return;
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
 *  Requirements: BootMode has to be called first to read PLC Status          *
 *                                                                            *
 ******************************************************************************/
void runMode() {
    while (plc_mode) {
        readInputs();
        userProgram();
        delay_ms(50);
        writeOutputs();
        IntToStr(anIn, line1);
        Lcd_Out(1, 11, line1); // Display analogue input in row1
        IntToStr(anOut, line2);
        Lcd_Out(2, 11, line2); // Display analogue output in row2
        delay_ms(100);
    }
    return;
}

/******************************************************************************
 *                                                                            *
 *  Function:     static void readInputs()                                    *
 *                                                                            *
 *  Description:  Reads all I/O and Staus byte from I/O PIC                   *
 *                                                                            *
 *  Parameters:   None.                                                       *
 *                                                                            *
 *  Return Value: None.                                                       *
 *                                                                            *
 *  Requirements: PLC must be in Run Mode                                     *
 *                                                                            *
 ******************************************************************************/
void readInputs() {
    uart1_write(READ);
    delay_ms(1);
    uart1_write(READ);
    delay_ms(1);
    uart1_write(READ);
    return;
}

/******************************************************************************
 *                                                                            *
 *  Function:     static void writeOutputs()                                  *
 *                                                                            *
 *  Description:  Writes all I/O Bytes to I/O PIC                             *
 *                                                                            *
 *  Parameters:   None.                                                       *
 *                                                                            *
 *  Return Value: None.                                                       *
 *                                                                            *
 *  Requirements: PLC Must be in Run Mode
 *                                                                            *
 ******************************************************************************/
void writeOutputs() {
    txByte = dio << 4;
    uart1_write(txByte);
    delay_ms(1);
    txByte = Lo(anOut);
    uart1_write(txByte);
    delay_ms(1);
    txByte = Hi(anOut);
    uart1_write(txByte);
    return;
}

void userProgram() {
    dio = dii;
    anOut = anIn * 10;
    return;
}