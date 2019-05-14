#include <built_in.h>
// LCD module connections
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

// End LCD module connections
static char txt[16];

void loop();
sbit ss at rb2_bit;
sbit ss_dir at trisb2_bit;
char temp;

void main() {
    ADCON1 = 7; // Configure all ports with analog function as digital
    CMCON = 0b00000111; // Disable comparators
    TRISC = 0;
    PORTC = 0;
    TRISA.B0 =0;
    Lcd_Init();
    Lcd_Cmd(_LCD_CLEAR);               // Clear display
    Lcd_Cmd(_LCD_UNDERLINE_ON);          // Cursor off
    UART1_Init(9600);               // Initialize UART module at 9600 bps
    Delay_ms(100);                  // Wait for UART module to stabilize

    ss_dir=0;
    while (1) {
       ss=0; 
       while(!uart1_tx_idle);
       uart1_write_text("pic18fx");
       while(!uart1_data_ready());
       uart1_read_text(txt,'x',1);
       ss=1;
    Lcd_Out(2,1,txt);                 // Write text in first row
    delay_ms(100);
    }
}