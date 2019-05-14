
_interrupt:

;ProgramMode_Test.c,31 :: 		void interrupt(){
;ProgramMode_Test.c,32 :: 		USB_Interrupt_Proc();                   // USB servicing is done inside the interrupt
	CALL        _USB_Interrupt_Proc+0, 0
;ProgramMode_Test.c,33 :: 		}
L_end_interrupt:
L__interrupt23:
	RETFIE      1
; end of _interrupt

_main:

;ProgramMode_Test.c,36 :: 		void main() {
;ProgramMode_Test.c,37 :: 		ADCON1 |= 0x0F;                         // Configure all ports with analog function as digital
	MOVLW       15
	IORWF       ADCON1+0, 1 
;ProgramMode_Test.c,38 :: 		CMCON  |= 7;                            // Disable comparators
	MOVLW       7
	IORWF       CMCON+0, 1 
;ProgramMode_Test.c,40 :: 		ss_dir=0;
	BCF         TRISB2_bit+0, BitPos(TRISB2_bit+0) 
;ProgramMode_Test.c,41 :: 		ss = 1;
	BSF         RB2_bit+0, BitPos(RB2_bit+0) 
;ProgramMode_Test.c,42 :: 		SPI1_Init_Advanced(_SPI_MASTER_OSC_DIV64,_SPI_DATA_SAMPLE_MIDDLE,_SPI_CLK_IDLE_LOW,_SPI_HIGH_2_LOW);
	MOVLW       2
	MOVWF       FARG_SPI1_Init_Advanced_master+0 
	CLRF        FARG_SPI1_Init_Advanced_data_sample+0 
	CLRF        FARG_SPI1_Init_Advanced_clock_idle+0 
	CLRF        FARG_SPI1_Init_Advanced_transmit_edge+0 
	CALL        _SPI1_Init_Advanced+0, 0
;ProgramMode_Test.c,43 :: 		delay_ms(100);
	MOVLW       3
	MOVWF       R11, 0
	MOVLW       138
	MOVWF       R12, 0
	MOVLW       85
	MOVWF       R13, 0
L_main0:
	DECFSZ      R13, 1, 1
	BRA         L_main0
	DECFSZ      R12, 1, 1
	BRA         L_main0
	DECFSZ      R11, 1, 1
	BRA         L_main0
	NOP
	NOP
;ProgramMode_Test.c,45 :: 		HID_Enable(&readbuff,&writebuff);       // Enable HID communication
	MOVLW       _readbuff+0
	MOVWF       FARG_HID_Enable_readbuff+0 
	MOVLW       hi_addr(_readbuff+0)
	MOVWF       FARG_HID_Enable_readbuff+1 
	MOVLW       _writebuff+0
	MOVWF       FARG_HID_Enable_writebuff+0 
	MOVLW       hi_addr(_writebuff+0)
	MOVWF       FARG_HID_Enable_writebuff+1 
	CALL        _HID_Enable+0, 0
;ProgramMode_Test.c,46 :: 		while(1){
L_main1:
;ProgramMode_Test.c,47 :: 		Usb_Polling_Proc();
	CALL        _USB_Polling_Proc+0, 0
;ProgramMode_Test.c,48 :: 		rxData= Hid_Read();
	CALL        _HID_Read+0, 0
	MOVF        R0, 0 
	MOVWF       _rxData+0 
;ProgramMode_Test.c,49 :: 		if(rxData!=0){
	MOVF        R0, 0 
	XORLW       0
	BTFSC       STATUS+0, 2 
	GOTO        L_main3
;ProgramMode_Test.c,50 :: 		fillData();
	CALL        _fillData+0, 0
;ProgramMode_Test.c,51 :: 		if(dataCount==63){
	MOVF        _dataCount+0, 0 
	XORLW       63
	BTFSS       STATUS+0, 2 
	GOTO        L_main4
;ProgramMode_Test.c,52 :: 		sendSPI();
	CALL        _sendSPI+0, 0
;ProgramMode_Test.c,53 :: 		}
L_main4:
;ProgramMode_Test.c,54 :: 		}
L_main3:
;ProgramMode_Test.c,55 :: 		delay_ms(200);
	MOVLW       6
	MOVWF       R11, 0
	MOVLW       19
	MOVWF       R12, 0
	MOVLW       173
	MOVWF       R13, 0
L_main5:
	DECFSZ      R13, 1, 1
	BRA         L_main5
	DECFSZ      R12, 1, 1
	BRA         L_main5
	DECFSZ      R11, 1, 1
	BRA         L_main5
	NOP
	NOP
;ProgramMode_Test.c,56 :: 		}
	GOTO        L_main1
;ProgramMode_Test.c,57 :: 		}
L_end_main:
	GOTO        $+0
; end of _main

_decodeData:

;ProgramMode_Test.c,60 :: 		char decodeData(){
;ProgramMode_Test.c,61 :: 		if(readBuff[0]!=DLE) return 1;
	MOVF        1280, 0 
	XORLW       16
	BTFSC       STATUS+0, 2 
	GOTO        L_decodeData6
	MOVLW       1
	MOVWF       R0 
	GOTO        L_end_decodeData
L_decodeData6:
;ProgramMode_Test.c,62 :: 		if(readBuff[1]!=STX) return 1;
	MOVF        1281, 0 
	XORLW       2
	BTFSC       STATUS+0, 2 
	GOTO        L_decodeData7
	MOVLW       1
	MOVWF       R0 
	GOTO        L_end_decodeData
L_decodeData7:
;ProgramMode_Test.c,63 :: 		cmd = readBuff[2];
	MOVF        1282, 0 
	MOVWF       _cmd+0 
;ProgramMode_Test.c,64 :: 		if(cmd == WRITE){
	MOVF        1282, 0 
	XORLW       224
	BTFSS       STATUS+0, 2 
	GOTO        L_decodeData8
;ProgramMode_Test.c,65 :: 		Higher(address)=readBuff[3];
	MOVF        1283, 0 
	MOVWF       _address+2 
;ProgramMode_Test.c,66 :: 		Hi(address)=readBuff[4];
	MOVF        1284, 0 
	MOVWF       _address+1 
;ProgramMode_Test.c,67 :: 		Lo(address)=readBuff[5];
	MOVF        1285, 0 
	MOVWF       _address+0 
;ProgramMode_Test.c,68 :: 		usbCount=readBuff[6];
	MOVF        1286, 0 
	MOVWF       _usbCount+0 
;ProgramMode_Test.c,69 :: 		return 0;
	CLRF        R0 
	GOTO        L_end_decodeData
;ProgramMode_Test.c,70 :: 		}else return 1;
L_decodeData8:
	MOVLW       1
	MOVWF       R0 
;ProgramMode_Test.c,71 :: 		}
L_end_decodeData:
	RETURN      0
; end of _decodeData

_fillData:

;ProgramMode_Test.c,73 :: 		void fillData(){
;ProgramMode_Test.c,74 :: 		dataCount = sizeof(progData);
	MOVLW       64
	MOVWF       _dataCount+0 
;ProgramMode_Test.c,75 :: 		if(dataCount>0) dataCount-=1;
	DECF        _dataCount+0, 1 
;ProgramMode_Test.c,76 :: 		kk=64-dataCount;
	MOVF        _dataCount+0, 0 
	SUBLW       64
	MOVWF       _kk+0 
;ProgramMode_Test.c,77 :: 		i=7;
	MOVLW       7
	MOVWF       _i+0 
;ProgramMode_Test.c,78 :: 		if(!decodeData() && kk>0){
	CALL        _decodeData+0, 0
	MOVF        R0, 1 
	BTFSS       STATUS+0, 2 
	GOTO        L_fillData13
	MOVF        _kk+0, 0 
	SUBLW       0
	BTFSC       STATUS+0, 0 
	GOTO        L_fillData13
L__fillData21:
;ProgramMode_Test.c,79 :: 		if(kk>usbCount) kk=usbCount;
	MOVF        _kk+0, 0 
	SUBWF       _usbCount+0, 0 
	BTFSC       STATUS+0, 0 
	GOTO        L_fillData14
	MOVF        _usbCount+0, 0 
	MOVWF       _kk+0 
L_fillData14:
;ProgramMode_Test.c,80 :: 		for(k=dataCount;k<kk;k++){
	MOVF        _dataCount+0, 0 
	MOVWF       _k+0 
L_fillData15:
	MOVF        _kk+0, 0 
	SUBWF       _k+0, 0 
	BTFSC       STATUS+0, 0 
	GOTO        L_fillData16
;ProgramMode_Test.c,81 :: 		progData[k]=readBuff[i];
	MOVLW       _progData+0
	MOVWF       FSR1 
	MOVLW       hi_addr(_progData+0)
	MOVWF       FSR1H 
	MOVF        _k+0, 0 
	ADDWF       FSR1, 1 
	BTFSC       STATUS+0, 0 
	INCF        FSR1H, 1 
	MOVLW       _readbuff+0
	MOVWF       FSR0 
	MOVLW       hi_addr(_readbuff+0)
	MOVWF       FSR0H 
	MOVF        _i+0, 0 
	ADDWF       FSR0, 1 
	BTFSC       STATUS+0, 0 
	INCF        FSR0H, 1 
	MOVF        POSTINC0+0, 0 
	MOVWF       POSTINC1+0 
;ProgramMode_Test.c,82 :: 		i++;
	INCF        _i+0, 1 
;ProgramMode_Test.c,80 :: 		for(k=dataCount;k<kk;k++){
	INCF        _k+0, 1 
;ProgramMode_Test.c,83 :: 		}
	GOTO        L_fillData15
L_fillData16:
;ProgramMode_Test.c,84 :: 		}
L_fillData13:
;ProgramMode_Test.c,85 :: 		}
L_end_fillData:
	RETURN      0
; end of _fillData

_sendSPI:

;ProgramMode_Test.c,87 :: 		void sendSPI(){
;ProgramMode_Test.c,88 :: 		ss=0;
	BCF         RB2_bit+0, BitPos(RB2_bit+0) 
;ProgramMode_Test.c,89 :: 		for(k=0;k<64;k++){
	CLRF        _k+0 
L_sendSPI18:
	MOVLW       64
	SUBWF       _k+0, 0 
	BTFSC       STATUS+0, 0 
	GOTO        L_sendSPI19
;ProgramMode_Test.c,90 :: 		kk = spi_read(progData[k]);
	MOVLW       _progData+0
	MOVWF       FSR0 
	MOVLW       hi_addr(_progData+0)
	MOVWF       FSR0H 
	MOVF        _k+0, 0 
	ADDWF       FSR0, 1 
	BTFSC       STATUS+0, 0 
	INCF        FSR0H, 1 
	MOVF        POSTINC0+0, 0 
	MOVWF       FARG_SPI_Read_buffer+0 
	CALL        _SPI_Read+0, 0
	MOVF        R0, 0 
	MOVWF       _kk+0 
;ProgramMode_Test.c,89 :: 		for(k=0;k<64;k++){
	INCF        _k+0, 1 
;ProgramMode_Test.c,91 :: 		}
	GOTO        L_sendSPI18
L_sendSPI19:
;ProgramMode_Test.c,92 :: 		ss=1;
	BSF         RB2_bit+0, BitPos(RB2_bit+0) 
;ProgramMode_Test.c,93 :: 		}
L_end_sendSPI:
	RETURN      0
; end of _sendSPI
