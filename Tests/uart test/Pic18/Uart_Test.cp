#line 1 "E:/Users/Vusman/Documents/TEE 5000 Projects/Project/Test/uart test/Pic18/Uart_Test.c"


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

char rxByte;
short counter;
char txt[16];
bit dataReady;

void interrupt(){
 if(pir1.rcif){
 pir1.rcif=0;
 rxByte=uart1_read();
 if(rxByte=='5'){
 counter=0;
 dataReady=1;
 }
 else{
 txt[counter]=rxByte;
 ++counter;
 }
 }
}
void main() {
 ADCON1 |= 0x0F;
 CMCON |= 7;

 Lcd_Init();
 Lcd_Cmd(_LCD_CLEAR);
 Lcd_Cmd(_LCD_CURSOR_OFF);
 dataReady=0;

 pie1.rcie=1;
 intcon.gie=1;
 intcon.peie=1;
 uart1_init(9600);
 while(1){
 if(dataReady){
 lcd_cmd(_lcd_clear);
 lcd_out(1,1,txt);
 dataReady=0;
 }
 delay_ms(100);
 }
}
