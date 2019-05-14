#line 1 "C:/Users/ADMIN/Documents/TEE 5000 Projects/Project/MikroC/User Program PIC/PlcPic.c"
#line 1 "c:/users/public/documents/mikroelektronika/mikroc pro for pic/include/built_in.h"
#line 68 "C:/Users/ADMIN/Documents/TEE 5000 Projects/Project/MikroC/User Program PIC/PlcPic.c"
const DLE = 0x10;
const STX = 0x02;
const ACK = 0x06;
const NAK = 0x0F;
const ETX = 0x03;


enum Command {
 cmdFREE, cmdWRITE_FLASH, cmdREAD_FLASH, cmdWRITE_RAM, cmdREAD_RAM, cmdERASE, cmdCONNECT
};

void runMode();
void programMode();
void decodeData();
void sendAck();
void userProgram() org 0x2000;
void checkUsb();
void serviceRunCmd();


unsigned int anIn0 absolute 0x60;
unsigned int anIn1 absolute 0x62;
unsigned int anIn2 absolute 0x64;
unsigned int anIn3 absolute 0x66;
unsigned int anIn4 absolute 0x68;
unsigned int anIn5 absolute 0x6A;
unsigned int anIn6 absolute 0x6C;
unsigned int anIn7 absolute 0x6E;
char dii absolute 0x70;
unsigned int anOut absolute 0x71;
char dio absolute 0x73;

unsigned short io[20] absolute 0x60;

unsigned char readbuff[64] absolute 0x500;
unsigned char writebuff[64] absolute 0x540;
unsigned char Reserve5thBankForUSB[256] absolute 0x400;

unsigned short length absolute 0x74;
unsigned int address absolute 0x75;
char flags absolute 0x77;
unsigned short state absolute 0x78;
unsigned int transactionID absolute 0x79;
sbit programming at flags.b0;

short session absolute 0x7B;
short tempByte absolute 0x7C;
short i absolute 0x7D, k absolute 0x7E;
short ramAddress absolute 0x7F;
short ramByte absolute 0x80;
char rxByte;
enum Command cmd;


sbit LCD_RS at RB5_bit;
sbit LCD_EN at RB4_bit;
sbit LCD_D7 at RB3_bit;
sbit LCD_D6 at RB2_bit;
sbit LCD_D5 at RB1_bit;
sbit LCD_D4 at RB0_bit;


sbit LCD_RS_Direction at TRISB5_bit;
sbit LCD_EN_Direction at TRISB4_bit;
sbit LCD_D7_Direction at TRISB3_bit;
sbit LCD_D6_Direction at TRISB2_bit;
sbit LCD_D5_Direction at TRISB1_bit;
sbit LCD_D4_Direction at TRISB0_bit;

sbit connected at rc0_bit;
sbit programmed at rc2_bit;
sbit plc_mode at ra0_bit;

sbit connected_dir at trisc0_bit;
sbit programmed_dir at trisc2_bit;
sbit plc_mode_dir at trisa0_bit;

const unsigned int OS_SIZE = 8022;
const unsigned int PROGRAM_START = 8192;

unsigned char Reserved1stBankForOS[256] absolute 0x00;
unsigned char Reserved2ndBankForUser[256] absolute 0x100;
unsigned char Reserved3rdBankForUser[256] absolute 0x200;
#line 162 "C:/Users/ADMIN/Documents/TEE 5000 Projects/Project/MikroC/User Program PIC/PlcPic.c"
void interrupt() {

 USB_Interrupt_Proc();
 return;
}
#line 178 "C:/Users/ADMIN/Documents/TEE 5000 Projects/Project/MikroC/User Program PIC/PlcPic.c"
void main() {
 uart1_init(9600);
 delay_ms(100);

 ADCON1 |= 0x0F;
 CMCON |= 7;

 plc_mode_dir = 1;
 cmd = cmdFREE;

 portb = 0;
 trisb = 0;


 connected_dir = 0;
 programmed_dir = 0;
 connected = 0;
 for (k = 0; k < 2; k++) {
 if (flash_read(0x2007 + k) == 0xFF)
 programmed = 0;
 else
 programmed = 1;
 }



 Lcd_Init();
 Lcd_Cmd(_LCD_CLEAR);
 Lcd_Cmd(_LCD_CURSOR_OFF);

 HID_Enable(&readbuff, &writebuff);
 delay_ms(100);

 while (1) {
 lcd_cmd(_lcd_clear);
 if (plc_mode) {
 lcd_out(1, 1, "RUN MODE");
 runMode();
 } else {
 lcd_out(1, 1, "PROGRAM MODE");
 programMode();
 }
 }
}
#line 236 "C:/Users/ADMIN/Documents/TEE 5000 Projects/Project/MikroC/User Program PIC/PlcPic.c"
void runMode() {
run_loop:

 for (session = 0; session < 17; session++) {
 uart1_write(cmdREAD_RAM);
 delay_ms(5);
 if (uart1_data_ready())
 io[session] = uart1_read();
 }

 if (programmed)
 userProgram();

 for (session = 17; session < 20; session++) {
 if (UART1_Tx_Idle())
 uart1_write(io[session]);
 delay_ms(5);
 }

 lcd_cmd(_lcd_shift_right);
 checkUsb();
 if (rxByte)
 serviceRunCmd();
 delay_ms(100);
 if (plc_mode) goto run_loop;

 return;
}
#line 278 "C:/Users/ADMIN/Documents/TEE 5000 Projects/Project/MikroC/User Program PIC/PlcPic.c"
void programMode() {
program_loop:
 checkUsb();
 if (rxByte) {
 switch (cmd) {
 case cmdERASE:
 {
 state = ACK;
 for (k = 0; k < length; k++) {
 if (address >= PROGRAM_START) {
 Flash_Erase_64(address);
 address += 64;
 } else {
 state = NAK;
 }
 }
 ++transactionID;
 sendAck();
 cmd = cmdFREE;
 break;
 }
 case cmdCONNECT:
 {
 connected = 1;
 lcd_out(2, 1, "CONNECTED TO PC.");
 state = ACK;
 transactionID = 0;
 sendAck();
 cmd = cmdFREE;
 break;
 }
 case cmdWRITE_FLASH:
 {
 state = NAK;
 if (address >= PROGRAM_START) {
 FLASH_Write_32(address, &readbuff[6]);
 lcd_out(2, 1, "PROGRAMMING.....");
 state = ACK;
 programmed = 1;
 }
 ++transactionID;
 sendACk();
 cmd = cmdFREE;
 break;
 }
 case cmdREAD_FLASH:
 {
 if (address >= PROGRAM_START) {
 FLASH_Read_N_Bytes(address, &writebuff[0], 64);
 HID_Write(&writebuff[0], 64);
 } else {
 state = NAK;
 ++transactionID;
 sendAck();
 }
 cmd = cmdFREE;
 break;
 }
 }
 }
 delay_ms(100);
 if (!plc_mode) goto program_loop;
 return;
}

void serviceRunCmd() {
 switch (cmd) {
 case cmdREAD_RAM:
 {
 asm movff _address, fsr0;
 for (i = 0; i < length; i++) {
 asm movff indf0, _ramByte;
 writebuff[i] = ramByte;
 asm incf fsr0;
 }
 HID_Write(&writebuff[0], 64);
 cmd = cmdFREE;
 break;
 }
 case cmdWRITE_RAM:
 {
 asm movff _address, fsr0;
 for (i = 0; i < length; i++) {
 ramByte = readbuff[i+6];
 asm movff _ramByte, indf0;
 asm incf fsr0;
 }
 state = ACK;
 ++transactionID;
 sendAck();
 cmd = cmdFREE;
 break;
 }
 }
 return;
}

void checkUsb() {
 USB_Polling_Proc();
 rxByte = HID_Read();
 if (rxByte && cmd == cmdFREE) {


 if (readbuff[0] != DLE) {
 return;
 }
 if (readbuff[1] != STX) {
 return;
 }


 cmd = readbuff[2];
  ((char *)&address)[1]  = readbuff[3];
  ((char *)&address)[0]  = readbuff[4];
 length = readBuff[5];
 }
 return;
}
#line 410 "C:/Users/ADMIN/Documents/TEE 5000 Projects/Project/MikroC/User Program PIC/PlcPic.c"
void sendAck() {

 writebuff[0] = DLE;
 writebuff[1] = cmd;
 writebuff[2] = state;
 writebuff[3] =  ((char *)&transactionID)[1] ;
 writebuff[4] =  ((char *)&transactionID)[0] ;
 for (i = 5; i < 64; i++) {
 writebuff[i] = 0;
 }
 HID_Write(&writebuff[0], 64);
 return;
}

void userProgram() org 0x2000 {
}
