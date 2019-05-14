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

char line1[8];
char line2[8];
short rxByte, i, txByte;
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
    anIn0=0;
    anOut=0;
    portb=0;
    trisb=0;
    Lcd_Init(); // Initialize LCD
    Lcd_Cmd(_LCD_CLEAR); // Clear display
    Lcd_Cmd(_LCD_UNDERLINE_ON); // Cursor off
    Lcd_Out(1, 1, "ADC Value:"); // Write text in first row
    Lcd_Out(2, 1, "DAC Value:"); // Write text in first row
    delay_ms(500);
    Uart1_Init(9600);
    delay_ms(100);
    while (1) {
        runMode();
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
while(plc_mode){
    //Read Inputs
    for (i = 0; i < 17; i++) { //read 17 bytes worth of input data
        uart1_write(2); //send request for data to be read
        delay_ms(10);
        if (uart1_data_ready())
            io[i] = uart1_read();
    }
    //Run program
        userProgram();
    //Write outputs
    for (i = 17; i < 20; i++) {
        if (UART1_Tx_Idle()) //wait for transmission to finish
        uart1_write(io[i]);
        delay_ms(10);
    }
    IntToStr(anIn0,line1);
    IntToStr(anOut,line2);
    lcd_out(1,11,line1);
    lcd_out(2,11,line2);
    delay_ms(200);
    }//else switch to program mode.
    return;
}


void userProgram() {
    dio = dii;
    anOut = anIn0;
    return;
}