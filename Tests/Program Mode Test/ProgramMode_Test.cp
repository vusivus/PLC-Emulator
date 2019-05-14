#line 1 "E:/Users/Vusman/Documents/TEE 5000 Projects/Project/Test/Program Mode Test/ProgramMode_Test.c"
#line 1 "e:/program files (x86)/mikroc pro for pic/include/built_in.h"
#line 2 "E:/Users/Vusman/Documents/TEE 5000 Projects/Project/Test/Program Mode Test/ProgramMode_Test.c"
unsigned char readbuff[80] absolute 0x500;
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
#line 31 "E:/Users/Vusman/Documents/TEE 5000 Projects/Project/Test/Program Mode Test/ProgramMode_Test.c"
void interrupt(){
 USB_Interrupt_Proc();
}


void main() {
 ADCON1 |= 0x0F;
 CMCON |= 7;

 ss_dir=0;
 ss = 1;
 SPI1_Init_Advanced(_SPI_MASTER_OSC_DIV64,_SPI_DATA_SAMPLE_MIDDLE,_SPI_CLK_IDLE_LOW,_SPI_HIGH_2_LOW);
 delay_ms(100);

 HID_Enable(&readbuff,&writebuff);
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


char decodeData(){
 if(readBuff[0]!= 0x10 ) return 1;
 if(readBuff[1]!= 0x02 ) return 1;
 cmd = readBuff[2];
 if(cmd ==  0xE0 ){
  ((char *)&address)[2] =readBuff[3];
  ((char *)&address)[1] =readBuff[4];
  ((char *)&address)[0] =readBuff[5];
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
