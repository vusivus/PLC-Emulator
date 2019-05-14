#include <built_in.h>
//I/O varaibles
unsigned int anIn0 ;
unsigned int anIn1 ;
unsigned int anIn2 ;
unsigned int anIn3 ;
unsigned int anIn4 ;
unsigned int anIn5 ;
unsigned int anIn6 ;
unsigned int anIn7 ;
unsigned int anOut;
//uart variables
unsigned short rxByte,txByte;
unsigned short temp,session;
//DAC SPI pins
sbit mcp_cs at rc2_bit;
sbit mode at rc1_bit;

sbit mcp_cs_dir at trisc2_bit;
sbit mode_dir at trisc1_bit;

//ISR
void interrupt(){
   if(pir1.rcif){ //Uart receive interrupt
     rxByte = rcreg;   //Clear interrupt by reading receive register
     ++session;
     //The first USART session is reading of inputs

     if(session==1){//next session if reading of analog inputs
       txByte= Lo(anIn0);
       uart1_write(txByte);
       return;
     }
     else if(session==2){
       txByte=Hi(anIn0);
       uart1_write(txByte);
       return;
     }
      else if(session==3){//next session if reading of analog inputs
       txByte= Lo(anIn1);
       uart1_write(txByte);
       return;
     }
     else if(session==4){
       txByte=Hi(anIn1);
       uart1_write(txByte);
       return;
     }
      else if(session==5){//next session if reading of analog inputs
       txByte= Lo(anIn2);
       uart1_write(txByte);
       return;
     }
     else if(session==6){
       txByte=Hi(anIn2);
       uart1_write(txByte);
       return;
     }
      else if(session==7){//next session if reading of analog inputs
       txByte= Lo(anIn3);
       uart1_write(txByte);
       return;
     }
     else if(session==8){
       txByte=Hi(anIn3);
       uart1_write(txByte);
       return;
     }
      else if(session==9){//next session if reading of analog inputs
       txByte= Lo(anIn4);
       uart1_write(txByte);
       return;
     }
     else if(session==10){
       txByte=Hi(anIn4);
       uart1_write(txByte);
       return;
     }
      else if(session==11){//next session if reading of analog inputs
       txByte= Lo(anIn5);
       uart1_write(txByte);
       return;
     }
     else if(session==12){
       txByte=Hi(anIn5);
       uart1_write(txByte);
       return;
     }
      else if(session==13){//next session if reading of analog inputs
       txByte= Lo(anIn6);
       uart1_write(txByte);
       return;
     }
     else if(session==14){
       txByte=Hi(anIn6);
       uart1_write(txByte);
       return;
     }
      else if(session==15){//next session if reading of analog inputs
       txByte= Lo(anIn7);
       uart1_write(txByte);
       return;
     }
     else if(session==16){
       txByte=Hi(anIn7);
       uart1_write(txByte);
       return;
     }
     else if(session==17){  //Digital inputs are read first
       txByte= portd;
       uart1_write(txByte);
       return;
     }
     //reading sessions ends here. Then comes writing session

     else if(session==18){
       Lo(anOut)=rxByte;
       return;
     }
     else if(session==19){
       Hi(anOut)=rxByte;

       return;
     }
     else if(session==20){ //first data to be written is digital output
       portb=rxByte;
       session=0;
       return;
     }
      return;
    }
   }


void main() {
  //I/O Configuration
  CCP1CON = 0;   // Disable comparators
  //Configure channels 0 to 7 as analog, the rest as digital
  ANSEL  = 0xFF;   // Configure AN0- AN7 pins as analog I/O
  ANSELH = 0;      //configure AN8-AN13 as digital
  C1ON_bit = 0;    // Disable comparators
  C2ON_bit = 0;
  option_reg.F7=1; //enable pull up resistors bit
  WPUB=0xFF; //enable pull up resistors pins
  TRISA  = 0x2F;  // analog pins as inputs
  mode_dir =1;    //plc mode pin is input
  PORTD =0;
  TRISD  = 0xFF;   //Digital Inputs port as inputs
  portb = 0;
  trisb =0x00;    //Digital outputs port as outputs
  mcp_cs_dir=0;  //MCP Chip Select pin is output
  mcp_cs=1;      //Disable MCP DAC

  //Uart configuration
  intcon.gie=1;   //globally enable all interrupts
  intcon.peie=1;  //Enables all unmasked peripheral interrupts
  pie1.rcie=1;     //Enables the EUSART receive interrupt
  UART1_Init(9600); //init uart at 9600 bits per second
  delay_ms(100);    //wait for uart initialization

  adc_init();      //initialize ADC
  delay_ms(10);
  //SPI Initialization
  SPI1_Init();
  delay_ms(100);
 //reset some counters
  session=0;
  anOut=0;
  //the loop code:
  do {
  if(mode){ //Only if we are in run mode
            anIn0 = ADC_Read(0); // Get 10-bit result of AD conversion
            anIn1 = ADC_Read(1); // Get 10-bit result of AD conversion
            anIn2 = ADC_Read(2); // Get 10-bit result of AD conversion
            anIn3 = ADC_Read(3); // Get 10-bit result of AD conversion
            anIn4 = ADC_Read(4); // Get 10-bit result of AD conversion
            anIn5 = ADC_Read(5); // Get 10-bit result of AD conversion
            anIn6 = ADC_Read(6); // Get 10-bit result of AD conversion
            anIn7 = ADC_Read(7); // Get 10-bit result of AD conversion

            mcp_cs = 0; //Select MCP4921 DAC
            temp = Hi(anOut)&0xF; // Store valueDAC[11..8] to temp[3..0]
            temp |= 0x30; // Define DAC setting, see MCP4921 datasheet
            SPI1_Write(temp); // Send high byte via SPI_Soft
            // Send Low Byte
            temp = Lo(anOut);
            SPI1_Write(temp); // Send low byte via SPI
            mcp_cs = 1; //Disable DAC
            delay_ms(100);
    }
  } while(1);
}