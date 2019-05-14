#include <built_in.h>
unsigned int temp_res;
static char txt[16];
unsigned short session;
char temp,response;
void configureSOI();
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
 sbit ss at ra5_bit;
sbit sdi at rc4_bit;
sbit sdo at rc5_bit;
sbit sck at rc3_bit;
sbit ss_dir at trisa5_bit;
sbit sdi_dir at trisc4_bit;
sbit sdo_dir at trisc5_bit;
sbit sck_dir at trisc3_bit;

void myISR();
void configureSPI();

void interrupt(){
myISR();
      }

void interrupt_low(){
 myISR();
}
void main() {
   ANSEL  = 0x04;              // Configure AN2 pin as analog
  ANSELH = 0;                 // Configure other AN pins as digital I/O
  C1ON_bit = 0;               // Disable comparators
  C2ON_bit = 0;
  TRISA.B2  = 1;              // analog pin is input
  TRISB  = 0;                 // PORTD is output
  PORTB =0;
  option_reg.F7=1; //enable pull up resistors bit
  WPUB=0xFF; //enable pull up resistors pins

  Lcd_Init();                        // Initialize LCD

  Lcd_Cmd(_LCD_CLEAR);               // Clear display
  Lcd_Cmd(_LCD_UNDERLINE_ON);          // Cursor off
  delay_ms(10);
  session=0;
  configureSPI();
  uart1_init(9600);
  delay_ms(100);
  do {
    Lcd_Out(2,1,txt);                 // Write text in first row
    delay_ms(100);
  } while(1);

}

void configureSPI(){
   intcon=0;
   pie1.rcie=1;
   intcon.peie=1;
   intcon.gie=1;
   return;
}

void myISR(){
   if (PIR1.RCIF){
   pir1.rcif=0;
        uart1_read_text(txt,'x',1);
        uart1_write_text("pic16fx");
        return;
   }
}