#include <built_in.h>
unsigned char readbuff[80] absolute 0x500;   // Buffers should be in USB RAM, please consult datasheet
unsigned char writebuff[80] absolute 0x550;
unsigned char progData[64];
unsigned char dataCount,cmd,usbCount,k,kk,i;
unsigned long address;

unsigned char rxData;
sbit ss at rb2_bit;
sbit ss_dir at trisb2_bit;

char decodeData();
void fillData();
void sendSPI();

#define DLE 0x10
#define STX 0x02
#define ENQ 0x05
#define ACK 0x06
#define NAK 0x0F
#define ETX 0x03
#define READ 0X0E
#define WRITE 0xE0
#define ACK 0x06
#define NAK 0x0F
#define ENQ 0x05
#define DONE 0x0B
#define CONNECT 0xFF
#define FREE 0x00

void interrupt(){
   USB_Interrupt_Proc();                   // USB servicing is done inside the interrupt
}


void main() {
   ADCON1 |= 0x0F;                         // Configure all ports with analog function as digital
   CMCON  |= 7;                            // Disable comparators

  ss_dir=0;
  ss = 1;
  SPI1_Init_Advanced(_SPI_MASTER_OSC_DIV64,_SPI_DATA_SAMPLE_MIDDLE,_SPI_CLK_IDLE_LOW,_SPI_HIGH_2_LOW);
  delay_ms(100);

  HID_Enable(&readbuff,&writebuff);       // Enable HID communication
  while(1){
   Usb_Polling_Proc();
   rxData= Hid_Read();
   if(rxData!=0){
       fillData();
       if(dataCount==63){
        sendSPI();
       }
   }
   delay_ms(200);
  }
}
//Protocol Description:
//<DLE><STX><CMD><ADDRESS><COUNT><DATA><DLE><ETX>
char decodeData(){
   if(readBuff[0]!=DLE) return 1;
   if(readBuff[1]!=STX) return 1;
   cmd = readBuff[2];
   if(cmd == WRITE){
     Higher(address)=readBuff[3];
     Hi(address)=readBuff[4];
     Lo(address)=readBuff[5];
     usbCount=readBuff[6];
     return 0;
   }else return 1;
}

void fillData(){
    dataCount = sizeof(progData);
    if(dataCount>0) dataCount-=1;
    kk=64-dataCount;
    i=7;
    if(!decodeData() && kk>0){
    if(kk>usbCount) kk=usbCount;
      for(k=dataCount;k<kk;k++){
       progData[k]=readBuff[i];
       i++;
      }
    }
}

void sendSPI(){
    ss=0;
    for(k=0;k<64;k++){
     kk = spi_read(progData[k]);
    }
    ss=1;
}