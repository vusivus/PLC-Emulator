#include <built_in.h>
//I/O varaibles
unsigned int anIn ;
unsigned int anOut;
char dio at portb;
//uart variables
unsigned short rxByte,txByte;
unsigned short temp,session;
//lcd display variables
static char line1[8];
static char line2[8];
// Lcd pinout settings
sbit LCD_RS at RD5_bit;
sbit LCD_EN at RD4_bit;
sbit LCD_D7 at RD3_bit;
sbit LCD_D6 at RD2_bit;
sbit LCD_D5 at RD1_bit;
sbit LCD_D4 at RD0_bit;

// Pin direction
sbit LCD_RS_Direction at TRISD5_bit;
sbit LCD_EN_Direction at TRISD4_bit;
sbit LCD_D7_Direction at TRISD3_bit;
sbit LCD_D6_Direction at TRISD2_bit;
sbit LCD_D5_Direction at TRISD1_bit;
sbit LCD_D4_Direction at TRISD0_bit;
//pin variables
sbit mcp_cs at ra1_bit;
sbit mode at ra4_bit;

sbit mcp_cs_dir at trisa1_bit;
sbit mode_dir at trisa4_bit;

//ISR
void interrupt(){
   if(pir1.rcif){ //Uart receive interrupt
     rxByte = rcreg;   //Clear interrupt by reading receive register
     ++session;
     if(session==1){
       txByte= portb;
       uart1_write(txByte);
       return;
     }
     else if(session==2){
       txByte= Lo(anIn);
       uart1_write(txByte);
       return;
     }
     else if(session==3){
       txByte=Hi(anIn);
       uart1_write(txByte);
       return;
     }
     else if(session==4){
       dio=rxByte;
       return;
     }
     else if(session==5){
       Lo(anOut)=rxByte;
       return;
     }
     else if(session==6){
       Hi(anOut)=rxByte;
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
  TRISA.B0  = 1;  // analog pin is input
  mode_dir =1;    //plc mode pin is input
  TRISD  = 0;     // lcd port is output
  trisb =0x0F;    //portb{0..3}=inputs and portb{4..7} are outputs
   mcp_cs_dir=0;  //MCP Chip Select pin is output
   mcp_cs=1;      //Disable MCP DAC
  //Uart configuration
  intcon.gie=1;   //globally enable all interrupts
  intcon.peie=1;  //Enables all unmasked peripheral interrupts
  pie1.rcie=1;     //Enables the EUSART receive interrupt
  UART1_Init(9600); //init uart at 9600 bits per second
  delay_ms(100);    //wait for uart initialization
 //LCD Initialization
  Lcd_Init();      // Initialize LCD
  Lcd_Cmd(_LCD_CLEAR);   // Clear display
  Lcd_Cmd(_LCD_CURSOR_OFF);  // Cursor off
  Lcd_Out(1, 1, "ADC Value:"); // Write text in first row
  Lcd_Out(2, 1, "DAC Value:"); // Write text in second row
  delay_ms(10);
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
            anIn = ADC_Read(0); // Get 10-bit result of AD conversion
            mcp_cs = 0; //Select MCP4921 DAC
            temp = Hi(anOut)&0xF; // Store valueDAC[11..8] to temp[3..0]
            temp |= 0x30; // Define DAC setting, see MCP4921 datasheet
            SPI1_Write(temp); // Send high byte via SPI_Soft
            // Send Low Byte
            temp = Lo(anOut);
            SPI1_Write(temp); // Send low byte via SPI
            mcp_cs = 1; //Disable DAC
            //Display analogue values on LCD
            IntToStr(anIn, line1);
            Lcd_Out(1, 11, line1); // Display analogue input in row1
            IntToStr(anOut, line2);
            Lcd_Out(2, 11, line2); // Display analogue output in row2
            delay_ms(100);
    }
  } while(1);

}