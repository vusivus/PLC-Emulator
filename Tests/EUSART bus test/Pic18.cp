#line 1 "E:/Users/Vusman/Documents/TEE 5000 Projects/Project/Test/EUSART bus test/Pic18.c"
#line 1 "e:/program files (x86)/mikroc pro for pic/include/built_in.h"
#line 3 "E:/Users/Vusman/Documents/TEE 5000 Projects/Project/Test/EUSART bus test/Pic18.c"
sbit LCD_RS at RB5_bit;
sbit LCD_EN at RB4_bit;
sbit LCD_D4 at RB0_bit;
sbit LCD_D5 at RB1_bit;
sbit LCD_D6 at RB2_bit;
sbit LCD_D7 at RB3_bit;

sbit LCD_RS_Direction at TRISB5_bit;
sbit LCD_EN_Direction at TRISB.B4;
sbit LCD_D4_Direction at TRISB.B0;
sbit LCD_D5_Direction at TRISB.B1;
sbit LCD_D6_Direction at TRISB.B2;
sbit LCD_D7_Direction at TRISB.B3;


static char txt[16];

void loop();
sbit ss at rb2_bit;
sbit ss_dir at trisb2_bit;
char temp;

void main() {
 ADCON1 = 7;
 CMCON = 0b00000111;
 TRISC = 0;
 PORTC = 0;
 TRISA.B0 =0;
 Lcd_Init();
 Lcd_Cmd(_LCD_CLEAR);
 Lcd_Cmd(_LCD_UNDERLINE_ON);
 UART1_Init(9600);
 Delay_ms(100);

 ss_dir=0;
 while (1) {
 ss=0;
 while(!uart1_tx_idle);
 uart1_write_text("pic18fx");
 while(!uart1_data_ready());
 uart1_read_text(txt,'x',1);
 ss=1;
 Lcd_Out(2,1,txt);
 delay_ms(100);
 }
}
