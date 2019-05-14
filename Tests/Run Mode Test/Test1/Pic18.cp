#line 1 "C:/Users/Vusumuzi/Documents/Skul/TEE 5000 Projects/Project/Test/Run Mode Test/Test1/Pic18.c"
#line 1 "c:/program files (x86)/mikroc pro for pic/include/built_in.h"
#line 16 "C:/Users/Vusumuzi/Documents/Skul/TEE 5000 Projects/Project/Test/Run Mode Test/Test1/Pic18.c"
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

void interrupt() {
 myISR();
}

void interrupt_lo() {
 myISR();
}
#line 73 "C:/Users/Vusumuzi/Documents/Skul/TEE 5000 Projects/Project/Test/Run Mode Test/Test1/Pic18.c"
void main() {
 ADCON1 |= 0x0F;
 CMCON |= 7;

 plc_mode_dir = 1;
 ss_dir = 0;
 ss = 1;
 Lcd_Init();
 Lcd_Cmd(_LCD_CLEAR);
 Lcd_Cmd(_LCD_UNDERLINE_ON);
 Lcd_Out(1, 1, "ADC Value:");
 Lcd_Out(2, 1, "DAC Value:");
 delay_ms(500);
 Uart1_Init(9600);
 delay_ms(100);

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
  ((char *)&anIn)[0]  = rxByte;
 }
 if (session == 3) {
  ((char *)&anIn)[1]  = rxByte;
 }
 if (session >= 3) {
 session = 0;
 }
 return;
 }
}
#line 133 "C:/Users/Vusumuzi/Documents/Skul/TEE 5000 Projects/Project/Test/Run Mode Test/Test1/Pic18.c"
void runMode() {
 while (plc_mode) {
 readInputs();
 userProgram();
 delay_ms(50);
 writeOutputs();
 IntToStr(anIn, line1);
 Lcd_Out(1, 11, line1);
 IntToStr(anOut, line2);
 Lcd_Out(2, 11, line2);
 delay_ms(100);
 }
 return;
}
#line 161 "C:/Users/Vusumuzi/Documents/Skul/TEE 5000 Projects/Project/Test/Run Mode Test/Test1/Pic18.c"
void readInputs() {
 uart1_write( 0X0E );
 delay_ms(1);
 uart1_write( 0X0E );
 delay_ms(1);
 uart1_write( 0X0E );
 return;
}
#line 183 "C:/Users/Vusumuzi/Documents/Skul/TEE 5000 Projects/Project/Test/Run Mode Test/Test1/Pic18.c"
void writeOutputs() {
 txByte = dio << 4;
 uart1_write(txByte);
 delay_ms(1);
 txByte =  ((char *)&anOut)[0] ;
 uart1_write(txByte);
 delay_ms(1);
 txByte =  ((char *)&anOut)[1] ;
 uart1_write(txByte);
 return;
}

void userProgram() {
 dio = dii;
 anOut = anIn * 10;
 return;
}
