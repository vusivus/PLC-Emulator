#line 1 "C:/Users/Vusumuzi/Documents/Skul/TEE 5000 Projects/Project/Test/Run Mode Test/Test1/Pic16.c"
#line 1 "c:/program files (x86)/mikroc pro for pic/include/built_in.h"
#line 3 "C:/Users/Vusumuzi/Documents/Skul/TEE 5000 Projects/Project/Test/Run Mode Test/Test1/Pic16.c"
unsigned int anIn ;
unsigned int anOut;
char dio at portb;

unsigned short rxByte,txByte;
unsigned short temp,session;

static char line1[8];
static char line2[8];

sbit LCD_RS at RD5_bit;
sbit LCD_EN at RD4_bit;
sbit LCD_D7 at RD3_bit;
sbit LCD_D6 at RD2_bit;
sbit LCD_D5 at RD1_bit;
sbit LCD_D4 at RD0_bit;


sbit LCD_RS_Direction at TRISD5_bit;
sbit LCD_EN_Direction at TRISD4_bit;
sbit LCD_D7_Direction at TRISD3_bit;
sbit LCD_D6_Direction at TRISD2_bit;
sbit LCD_D5_Direction at TRISD1_bit;
sbit LCD_D4_Direction at TRISD0_bit;

sbit mcp_cs at ra1_bit;
sbit mode at ra4_bit;

sbit mcp_cs_dir at trisa1_bit;
sbit mode_dir at trisa4_bit;


void interrupt(){
 if(pir1.rcif){
 rxByte = rcreg;
 ++session;
 if(session==1){
 txByte= portb;
 uart1_write(txByte);
 return;
 }
 else if(session==2){
 txByte=  ((char *)&anIn)[0] ;
 uart1_write(txByte);
 return;
 }
 else if(session==3){
 txByte= ((char *)&anIn)[1] ;
 uart1_write(txByte);
 return;
 }
 else if(session==4){
 dio=rxByte;
 return;
 }
 else if(session==5){
  ((char *)&anOut)[0] =rxByte;
 return;
 }
 else if(session==6){
  ((char *)&anOut)[1] =rxByte;
 session=0;
 return;
 }
 return;
 }
 }


void main() {

 CCP1CON = 0;

 ANSEL = 0xFF;
 ANSELH = 0;
 C1ON_bit = 0;
 C2ON_bit = 0;
 TRISA.B0 = 1;
 mode_dir =1;
 TRISD = 0;
 trisb =0x0F;
 mcp_cs_dir=0;
 mcp_cs=1;

 intcon.gie=1;
 intcon.peie=1;
 pie1.rcie=1;
 UART1_Init(9600);
 delay_ms(100);

 Lcd_Init();
 Lcd_Cmd(_LCD_CLEAR);
 Lcd_Cmd(_LCD_CURSOR_OFF);
 Lcd_Out(1, 1, "ADC Value:");
 Lcd_Out(2, 1, "DAC Value:");
 delay_ms(10);
 adc_init();
 delay_ms(10);

 SPI1_Init();
 delay_ms(100);

 session=0;
 anOut=0;

 do {
 if(mode){
 anIn = ADC_Read(0);
 mcp_cs = 0;
 temp =  ((char *)&anOut)[1] &0xF;
 temp |= 0x30;
 SPI1_Write(temp);

 temp =  ((char *)&anOut)[0] ;
 SPI1_Write(temp);
 mcp_cs = 1;

 IntToStr(anIn, line1);
 Lcd_Out(1, 11, line1);
 IntToStr(anOut, line2);
 Lcd_Out(2, 11, line2);
 delay_ms(100);
 }
 } while(1);

}
