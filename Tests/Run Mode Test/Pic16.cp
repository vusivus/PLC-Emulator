#line 1 "C:/Users/ADMIN/Documents/TEE 5000 Projects/Project/Test/Run Mode Test/Test3/Pic16.c"
#line 1 "c:/users/public/documents/mikroelektronika/mikroc pro for pic/include/built_in.h"
#line 3 "C:/Users/ADMIN/Documents/TEE 5000 Projects/Project/Test/Run Mode Test/Test3/Pic16.c"
unsigned int anIn0 ;
unsigned int anIn1 ;
unsigned int anIn2 ;
unsigned int anIn3 ;
unsigned int anIn4 ;
unsigned int anIn5 ;
unsigned int anIn6 ;
unsigned int anIn7 ;
unsigned int anOut;

unsigned short rxByte,txByte;
unsigned short temp,session;

sbit mcp_cs at rc2_bit;
sbit mode at rc1_bit;

sbit mcp_cs_dir at trisc2_bit;
sbit mode_dir at trisc1_bit;


void interrupt(){
 if(pir1.rcif){
 rxByte = rcreg;
 ++session;


 if(session==1){
 txByte=  ((char *)&anIn0)[0] ;
 uart1_write(txByte);
 return;
 }
 else if(session==2){
 txByte= ((char *)&anIn0)[1] ;
 uart1_write(txByte);
 return;
 }
 else if(session==3){
 txByte=  ((char *)&anIn1)[0] ;
 uart1_write(txByte);
 return;
 }
 else if(session==4){
 txByte= ((char *)&anIn1)[1] ;
 uart1_write(txByte);
 return;
 }
 else if(session==5){
 txByte=  ((char *)&anIn2)[0] ;
 uart1_write(txByte);
 return;
 }
 else if(session==6){
 txByte= ((char *)&anIn2)[1] ;
 uart1_write(txByte);
 return;
 }
 else if(session==7){
 txByte=  ((char *)&anIn3)[0] ;
 uart1_write(txByte);
 return;
 }
 else if(session==8){
 txByte= ((char *)&anIn3)[1] ;
 uart1_write(txByte);
 return;
 }
 else if(session==9){
 txByte=  ((char *)&anIn4)[0] ;
 uart1_write(txByte);
 return;
 }
 else if(session==10){
 txByte= ((char *)&anIn4)[1] ;
 uart1_write(txByte);
 return;
 }
 else if(session==11){
 txByte=  ((char *)&anIn5)[0] ;
 uart1_write(txByte);
 return;
 }
 else if(session==12){
 txByte= ((char *)&anIn5)[1] ;
 uart1_write(txByte);
 return;
 }
 else if(session==13){
 txByte=  ((char *)&anIn6)[0] ;
 uart1_write(txByte);
 return;
 }
 else if(session==14){
 txByte= ((char *)&anIn6)[1] ;
 uart1_write(txByte);
 return;
 }
 else if(session==15){
 txByte=  ((char *)&anIn7)[0] ;
 uart1_write(txByte);
 return;
 }
 else if(session==16){
 txByte= ((char *)&anIn7)[1] ;
 uart1_write(txByte);
 return;
 }
 else if(session==17){
 txByte= portd;
 uart1_write(txByte);
 return;
 }


 else if(session==18){
  ((char *)&anOut)[0] =rxByte;
 return;
 }
 else if(session==19){
  ((char *)&anOut)[1] =rxByte;

 return;
 }
 else if(session==20){
 portb=rxByte;
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
 option_reg.F7=1;
 WPUB=0xFF;
 TRISA = 0x2F;
 mode_dir =1;
 PORTD =0;
 TRISD = 0xFF;
 portb = 0;
 trisb =0x00;
 mcp_cs_dir=0;
 mcp_cs=1;


 intcon.gie=1;
 intcon.peie=1;
 pie1.rcie=1;
 UART1_Init(9600);
 delay_ms(100);

 adc_init();
 delay_ms(10);

 SPI1_Init();
 delay_ms(100);

 session=0;
 anOut=0;

 do {
 if(mode){
 anIn0 = ADC_Read(0);
 anIn1 = ADC_Read(1);
 anIn2 = ADC_Read(2);
 anIn3 = ADC_Read(3);
 anIn4 = ADC_Read(4);
 anIn5 = ADC_Read(5);
 anIn6 = ADC_Read(6);
 anIn7 = ADC_Read(7);

 mcp_cs = 0;
 temp =  ((char *)&anOut)[1] &0xF;
 temp |= 0x30;
 SPI1_Write(temp);

 temp =  ((char *)&anOut)[0] ;
 SPI1_Write(temp);
 mcp_cs = 1;
 delay_ms(100);
 }
 } while(1);
}
