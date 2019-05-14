#line 1 "C:/Users/ADMIN/Documents/TEE 5000 Projects/Project/Test/Run Mode Test/Test3/Pic18.c"
#line 1 "c:/users/public/documents/mikroelektronika/mikroc pro for pic/include/built_in.h"
#line 16 "C:/Users/ADMIN/Documents/TEE 5000 Projects/Project/Test/Run Mode Test/Test3/Pic18.c"
void runMode();
void readInputs();
void writeOutputs();
void userProgram();
void testMode();
void myISR();


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

unsigned short io[20] absolute 0x60;

char line1[8];
char line2[8];
short rxByte, i, txByte;
bit dataReady;

sbit ss at rb2_bit;
sbit plc_mode at ra0_bit;

sbit ss_dir at trisb2_bit;
sbit plc_mode_dir at trisa0_bit;


sbit LCD_RS at RB5_bit;
sbit LCD_EN at RB4_bit;
sbit LCD_D7 at RB3_bit;
sbit LCD_D6 at RB2_bit;
sbit LCD_D5 at RB1_bit;
sbit LCD_D4 at RB0_bit;


sbit LCD_RS_Direction at TRISB5_bit;
sbit LCD_EN_Direction at TRISB.b4;
sbit LCD_D7_Direction at TRISB.B3;
sbit LCD_D6_Direction at TRISB.B2;
sbit LCD_D5_Direction at TRISB.B1;
sbit LCD_D4_Direction at TRISB.B0;
#line 76 "C:/Users/ADMIN/Documents/TEE 5000 Projects/Project/Test/Run Mode Test/Test3/Pic18.c"
void main() {
 ADCON1 |= 0x0F;
 CMCON |= 7;

 plc_mode_dir = 1;
 anIn0=0;
 anOut=0;
 portb=0;
 trisb=0;
 Lcd_Init();
 Lcd_Cmd(_LCD_CLEAR);
 Lcd_Cmd(_LCD_UNDERLINE_ON);
 Lcd_Out(1, 1, "ADC Value:");
 Lcd_Out(2, 1, "DAC Value:");
 delay_ms(500);
 Uart1_Init(9600);
 delay_ms(100);
 while (1) {
 runMode();
 }
}
#line 111 "C:/Users/ADMIN/Documents/TEE 5000 Projects/Project/Test/Run Mode Test/Test3/Pic18.c"
void runMode() {
while(plc_mode){

 for (i = 0; i < 17; i++) {
 uart1_write(2);
 delay_ms(10);
 if (uart1_data_ready())
 io[i] = uart1_read();
 }

 userProgram();

 for (i = 17; i < 20; i++) {
 if (UART1_Tx_Idle())
 uart1_write(io[i]);
 delay_ms(10);
 }
 IntToStr(anIn0,line1);
 IntToStr(anOut,line2);
 lcd_out(1,11,line1);
 lcd_out(2,11,line2);
 delay_ms(200);
 }
 return;
}


void userProgram() {
 dio = dii;
 anOut = anIn0;
 return;
}
