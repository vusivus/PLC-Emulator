
// LCD module connections
sbit LCD_RS at RB5_bit;
sbit LCD_EN at RB4_bit;
sbit LCD_D4 at RB0_bit;
sbit LCD_D5 at RB1_bit;
sbit LCD_D6 at RB2_bit;
sbit LCD_D7 at RB3_bit;

sbit LCD_RS_Direction at TRISB5_bit;
sbit LCD_EN_Direction at TRISB4_bit;
sbit LCD_D4_Direction at TRISB0_bit;
sbit LCD_D5_Direction at TRISB1_bit;
sbit LCD_D6_Direction at TRISB2_bit;
sbit LCD_D7_Direction at TRISB3_bit;
// End LCD module connections
short rxByte;
short counter;
char txt[16];
bit dataReady;

void interrupt(){
 if(pir1.rcif){

     rxByte=rcreg;
      if(rxByte==0x35){
         counter=0;
         dataReady=1;
         return;
        }
       else{
       txt[counter]=rxByte;
       ++counter;
       if(counter>10)counter=0;
       return;
      }

      }
}
void main() {
  ANSEL  = 0;                        // Configure AN pins as digital I/O
  ANSELH = 0;
  C1ON_bit = 0;                      // Disable comparators
  C2ON_bit = 0;
  TRISB  = 0;              // analog pin is input
  PORTB=0;

  Lcd_Init();                        // Initialize LCD
  Lcd_Cmd(_LCD_CLEAR);               // Clear display
  Lcd_Cmd(_LCD_CURSOR_OFF);          // Cursor off
  dataReady=0;
  counter=0;
  
  intcon.gie=1;
  intcon.peie=1;
  pie1.rcie=1;

  uart1_init(9600);
  delay_ms(100);

  while(1){
   if(dataReady){
        lcd_cmd(_lcd_clear);
        lcd_out(1,1,txt);
        dataReady=0;
   }
   delay_ms(100);
  }
}