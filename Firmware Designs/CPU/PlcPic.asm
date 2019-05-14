
_interrupt:

;PlcPic.c,162 :: 		void interrupt() {
;PlcPic.c,164 :: 		USB_Interrupt_Proc();
	CALL        _USB_Interrupt_Proc+0, 0
;PlcPic.c,165 :: 		return;
;PlcPic.c,166 :: 		}
L_end_interrupt:
L__interrupt62:
	RETFIE      1
; end of _interrupt

_main:

;PlcPic.c,178 :: 		void main() {
;PlcPic.c,179 :: 		uart1_init(9600);
	BSF         BAUDCON+0, 3, 0
	CLRF        SPBRGH+0 
	MOVLW       207
	MOVWF       SPBRG+0 
	BSF         TXSTA+0, 2, 0
	CALL        _UART1_Init+0, 0
;PlcPic.c,180 :: 		delay_ms(100);
	MOVLW       2
	MOVWF       R11, 0
	MOVLW       4
	MOVWF       R12, 0
	MOVLW       186
	MOVWF       R13, 0
L_main0:
	DECFSZ      R13, 1, 1
	BRA         L_main0
	DECFSZ      R12, 1, 1
	BRA         L_main0
	DECFSZ      R11, 1, 1
	BRA         L_main0
	NOP
;PlcPic.c,182 :: 		ADCON1 |= 0x0F; // Configure all ports with analog function as digital
	MOVLW       15
	IORWF       ADCON1+0, 1 
;PlcPic.c,183 :: 		CMCON |= 7; // Disable comparators
	MOVLW       7
	IORWF       CMCON+0, 1 
;PlcPic.c,185 :: 		plc_mode_dir = 1; //plc mode pin is input
	BSF         TRISA0_bit+0, BitPos(TRISA0_bit+0) 
;PlcPic.c,186 :: 		cmd = cmdFREE; //free cmd
	CLRF        _cmd+0 
;PlcPic.c,188 :: 		portb = 0;
	CLRF        PORTB+0 
;PlcPic.c,189 :: 		trisb = 0; //lcd port
	CLRF        TRISB+0 
;PlcPic.c,192 :: 		connected_dir = 0; //connected pin is output
	BCF         TRISC0_bit+0, BitPos(TRISC0_bit+0) 
;PlcPic.c,193 :: 		programmed_dir = 0;
	BCF         TRISC2_bit+0, BitPos(TRISC2_bit+0) 
;PlcPic.c,194 :: 		connected = 0;
	BCF         RC0_bit+0, BitPos(RC0_bit+0) 
;PlcPic.c,195 :: 		for (k = 0; k < 2; k++) {
	CLRF        126 
L_main1:
	MOVLW       128
	XORWF       126, 0 
	MOVWF       R0 
	MOVLW       128
	XORLW       2
	SUBWF       R0, 0 
	BTFSC       STATUS+0, 0 
	GOTO        L_main2
;PlcPic.c,196 :: 		if (flash_read(0x2007 + k) == 0xFF)
	MOVLW       7
	MOVWF       FARG_FLASH_Read_address+0 
	MOVLW       32
	MOVWF       FARG_FLASH_Read_address+1 
	MOVLW       0
	MOVWF       FARG_FLASH_Read_address+2 
	MOVWF       FARG_FLASH_Read_address+3 
	MOVF        126, 0 
	ADDWF       FARG_FLASH_Read_address+0, 1 
	MOVLW       0
	BTFSC       126, 7 
	MOVLW       255
	ADDWFC      FARG_FLASH_Read_address+1, 1 
	MOVLW       0
	BTFSC       FARG_FLASH_Read_address+1, 7 
	MOVLW       255
	MOVWF       FARG_FLASH_Read_address+2 
	MOVWF       FARG_FLASH_Read_address+3 
	CALL        _FLASH_Read+0, 0
	MOVF        R0, 0 
	XORLW       255
	BTFSS       STATUS+0, 2 
	GOTO        L_main4
;PlcPic.c,197 :: 		programmed = 0;
	BCF         RC2_bit+0, BitPos(RC2_bit+0) 
	GOTO        L_main5
L_main4:
;PlcPic.c,199 :: 		programmed = 1;
	BSF         RC2_bit+0, BitPos(RC2_bit+0) 
L_main5:
;PlcPic.c,195 :: 		for (k = 0; k < 2; k++) {
	INCF        126, 1 
;PlcPic.c,200 :: 		}
	GOTO        L_main1
L_main2:
;PlcPic.c,204 :: 		Lcd_Init(); // Initialize LCD
	CALL        _Lcd_Init+0, 0
;PlcPic.c,205 :: 		Lcd_Cmd(_LCD_CLEAR); // Clear display
	MOVLW       1
	MOVWF       FARG_Lcd_Cmd_out_char+0 
	CALL        _Lcd_Cmd+0, 0
;PlcPic.c,206 :: 		Lcd_Cmd(_LCD_CURSOR_OFF); // Cursor off
	MOVLW       12
	MOVWF       FARG_Lcd_Cmd_out_char+0 
	CALL        _Lcd_Cmd+0, 0
;PlcPic.c,208 :: 		HID_Enable(&readbuff, &writebuff); // Enable HID communication
	MOVLW       _readbuff+0
	MOVWF       FARG_HID_Enable_readbuff+0 
	MOVLW       hi_addr(_readbuff+0)
	MOVWF       FARG_HID_Enable_readbuff+1 
	MOVLW       _writebuff+0
	MOVWF       FARG_HID_Enable_writebuff+0 
	MOVLW       hi_addr(_writebuff+0)
	MOVWF       FARG_HID_Enable_writebuff+1 
	CALL        _HID_Enable+0, 0
;PlcPic.c,209 :: 		delay_ms(100); //wait for initialization to complete
	MOVLW       2
	MOVWF       R11, 0
	MOVLW       4
	MOVWF       R12, 0
	MOVLW       186
	MOVWF       R13, 0
L_main6:
	DECFSZ      R13, 1, 1
	BRA         L_main6
	DECFSZ      R12, 1, 1
	BRA         L_main6
	DECFSZ      R11, 1, 1
	BRA         L_main6
	NOP
;PlcPic.c,211 :: 		while (1) {
L_main7:
;PlcPic.c,212 :: 		lcd_cmd(_lcd_clear);
	MOVLW       1
	MOVWF       FARG_Lcd_Cmd_out_char+0 
	CALL        _Lcd_Cmd+0, 0
;PlcPic.c,213 :: 		if (plc_mode) {
	BTFSS       RA0_bit+0, BitPos(RA0_bit+0) 
	GOTO        L_main9
;PlcPic.c,214 :: 		lcd_out(1, 1, "RUN MODE");
	MOVLW       1
	MOVWF       FARG_Lcd_Out_row+0 
	MOVLW       1
	MOVWF       FARG_Lcd_Out_column+0 
	MOVLW       ?lstr1_PlcPic+0
	MOVWF       FARG_Lcd_Out_text+0 
	MOVLW       hi_addr(?lstr1_PlcPic+0)
	MOVWF       FARG_Lcd_Out_text+1 
	CALL        _Lcd_Out+0, 0
;PlcPic.c,215 :: 		runMode();
	CALL        _runMode+0, 0
;PlcPic.c,216 :: 		} else {
	GOTO        L_main10
L_main9:
;PlcPic.c,217 :: 		lcd_out(1, 1, "PROGRAM MODE");
	MOVLW       1
	MOVWF       FARG_Lcd_Out_row+0 
	MOVLW       1
	MOVWF       FARG_Lcd_Out_column+0 
	MOVLW       ?lstr2_PlcPic+0
	MOVWF       FARG_Lcd_Out_text+0 
	MOVLW       hi_addr(?lstr2_PlcPic+0)
	MOVWF       FARG_Lcd_Out_text+1 
	CALL        _Lcd_Out+0, 0
;PlcPic.c,218 :: 		programMode();
	CALL        _programMode+0, 0
;PlcPic.c,219 :: 		}
L_main10:
;PlcPic.c,220 :: 		}
	GOTO        L_main7
;PlcPic.c,221 :: 		}
L_end_main:
	GOTO        $+0
; end of _main

_runMode:

;PlcPic.c,236 :: 		void runMode() {
;PlcPic.c,237 :: 		run_loop:
___runMode_run_loop:
;PlcPic.c,239 :: 		for (session = 0; session < 17; session++) { //read 17 bytes worth of input data
	CLRF        123 
L_runMode11:
	MOVLW       128
	XORWF       123, 0 
	MOVWF       R0 
	MOVLW       128
	XORLW       17
	SUBWF       R0, 0 
	BTFSC       STATUS+0, 0 
	GOTO        L_runMode12
;PlcPic.c,240 :: 		uart1_write(cmdREAD_RAM); //send request for data to be read
	MOVLW       4
	MOVWF       FARG_UART1_Write_data_+0 
	CALL        _UART1_Write+0, 0
;PlcPic.c,241 :: 		delay_ms(5);
	MOVLW       13
	MOVWF       R12, 0
	MOVLW       251
	MOVWF       R13, 0
L_runMode14:
	DECFSZ      R13, 1, 1
	BRA         L_runMode14
	DECFSZ      R12, 1, 1
	BRA         L_runMode14
	NOP
	NOP
;PlcPic.c,242 :: 		if (uart1_data_ready())
	CALL        _UART1_Data_Ready+0, 0
	MOVF        R0, 1 
	BTFSC       STATUS+0, 2 
	GOTO        L_runMode15
;PlcPic.c,243 :: 		io[session] = uart1_read();
	MOVLW       _io+0
	MOVWF       FLOC__runMode+0 
	MOVLW       hi_addr(_io+0)
	MOVWF       FLOC__runMode+1 
	MOVF        123, 0 
	ADDWF       FLOC__runMode+0, 1 
	MOVLW       0
	BTFSC       123, 7 
	MOVLW       255
	ADDWFC      FLOC__runMode+1, 1 
	CALL        _UART1_Read+0, 0
	MOVFF       FLOC__runMode+0, FSR1
	MOVFF       FLOC__runMode+1, FSR1H
	MOVF        R0, 0 
	MOVWF       POSTINC1+0 
L_runMode15:
;PlcPic.c,239 :: 		for (session = 0; session < 17; session++) { //read 17 bytes worth of input data
	INCF        123, 1 
;PlcPic.c,244 :: 		}
	GOTO        L_runMode11
L_runMode12:
;PlcPic.c,246 :: 		if (programmed)
	BTFSS       RC2_bit+0, BitPos(RC2_bit+0) 
	GOTO        L_runMode16
;PlcPic.c,247 :: 		userProgram();
	CALL        8192, 0
L_runMode16:
;PlcPic.c,249 :: 		for (session = 17; session < 20; session++) {
	MOVLW       17
	MOVWF       123 
L_runMode17:
	MOVLW       128
	XORWF       123, 0 
	MOVWF       R0 
	MOVLW       128
	XORLW       20
	SUBWF       R0, 0 
	BTFSC       STATUS+0, 0 
	GOTO        L_runMode18
;PlcPic.c,250 :: 		if (UART1_Tx_Idle()) //if transmission to finished
	CALL        _UART1_Tx_Idle+0, 0
	MOVF        R0, 1 
	BTFSC       STATUS+0, 2 
	GOTO        L_runMode20
;PlcPic.c,251 :: 		uart1_write(io[session]);
	MOVLW       _io+0
	MOVWF       FSR0 
	MOVLW       hi_addr(_io+0)
	MOVWF       FSR0H 
	MOVF        123, 0 
	ADDWF       FSR0, 1 
	MOVLW       0
	BTFSC       123, 7 
	MOVLW       255
	ADDWFC      FSR0H, 1 
	MOVF        POSTINC0+0, 0 
	MOVWF       FARG_UART1_Write_data_+0 
	CALL        _UART1_Write+0, 0
L_runMode20:
;PlcPic.c,252 :: 		delay_ms(5);
	MOVLW       13
	MOVWF       R12, 0
	MOVLW       251
	MOVWF       R13, 0
L_runMode21:
	DECFSZ      R13, 1, 1
	BRA         L_runMode21
	DECFSZ      R12, 1, 1
	BRA         L_runMode21
	NOP
	NOP
;PlcPic.c,249 :: 		for (session = 17; session < 20; session++) {
	INCF        123, 1 
;PlcPic.c,253 :: 		}
	GOTO        L_runMode17
L_runMode18:
;PlcPic.c,255 :: 		lcd_cmd(_lcd_shift_right);
	MOVLW       28
	MOVWF       FARG_Lcd_Cmd_out_char+0 
	CALL        _Lcd_Cmd+0, 0
;PlcPic.c,256 :: 		checkUsb();
	CALL        _checkUsb+0, 0
;PlcPic.c,257 :: 		if (rxByte)
	MOVF        _rxByte+0, 1 
	BTFSC       STATUS+0, 2 
	GOTO        L_runMode22
;PlcPic.c,258 :: 		serviceRunCmd();
	CALL        _serviceRunCmd+0, 0
L_runMode22:
;PlcPic.c,259 :: 		delay_ms(100);
	MOVLW       2
	MOVWF       R11, 0
	MOVLW       4
	MOVWF       R12, 0
	MOVLW       186
	MOVWF       R13, 0
L_runMode23:
	DECFSZ      R13, 1, 1
	BRA         L_runMode23
	DECFSZ      R12, 1, 1
	BRA         L_runMode23
	DECFSZ      R11, 1, 1
	BRA         L_runMode23
	NOP
;PlcPic.c,260 :: 		if (plc_mode) goto run_loop;
	BTFSS       RA0_bit+0, BitPos(RA0_bit+0) 
	GOTO        L_runMode24
	GOTO        ___runMode_run_loop
L_runMode24:
;PlcPic.c,262 :: 		return;
;PlcPic.c,263 :: 		}
L_end_runMode:
	RETURN      0
; end of _runMode

_programMode:

;PlcPic.c,278 :: 		void programMode() {
;PlcPic.c,279 :: 		program_loop:// program mode loop starts here
___programMode_program_loop:
;PlcPic.c,280 :: 		checkUsb();
	CALL        _checkUsb+0, 0
;PlcPic.c,281 :: 		if (rxByte) { // Do we have an incoming?
	MOVF        _rxByte+0, 1 
	BTFSC       STATUS+0, 2 
	GOTO        L_programMode25
;PlcPic.c,282 :: 		switch (cmd) { // Process command.
	GOTO        L_programMode26
;PlcPic.c,283 :: 		case cmdERASE: // Cmd: Disconnect PLC and PC app.
L_programMode28:
;PlcPic.c,285 :: 		state = ACK;
	MOVLW       6
	MOVWF       120 
;PlcPic.c,286 :: 		for (k = 0; k < length; k++) {
	CLRF        126 
L_programMode29:
	MOVLW       128
	BTFSC       126, 7 
	MOVLW       127
	MOVWF       R0 
	MOVLW       128
	SUBWF       R0, 0 
	BTFSS       STATUS+0, 2 
	GOTO        L__programMode66
	MOVF        116, 0 
	SUBWF       126, 0 
L__programMode66:
	BTFSC       STATUS+0, 0 
	GOTO        L_programMode30
;PlcPic.c,287 :: 		if (address >= PROGRAM_START) {
	MOVLW       32
	SUBWF       118, 0 
	BTFSS       STATUS+0, 2 
	GOTO        L__programMode67
	MOVLW       0
	SUBWF       117, 0 
L__programMode67:
	BTFSS       STATUS+0, 0 
	GOTO        L_programMode32
;PlcPic.c,288 :: 		Flash_Erase_64(address);
	MOVF        117, 0 
	MOVWF       FARG_FLASH_Erase_64_address+0 
	MOVF        118, 0 
	MOVWF       FARG_FLASH_Erase_64_address+1 
	MOVLW       0
	MOVWF       FARG_FLASH_Erase_64_address+2 
	MOVWF       FARG_FLASH_Erase_64_address+3 
	CALL        _FLASH_Erase_64+0, 0
;PlcPic.c,289 :: 		address += 64;
	MOVLW       64
	ADDWF       117, 1 
	MOVLW       0
	ADDWFC      118, 1 
;PlcPic.c,290 :: 		} else {
	GOTO        L_programMode33
L_programMode32:
;PlcPic.c,291 :: 		state = NAK;
	MOVLW       15
	MOVWF       120 
;PlcPic.c,292 :: 		}
L_programMode33:
;PlcPic.c,286 :: 		for (k = 0; k < length; k++) {
	INCF        126, 1 
;PlcPic.c,293 :: 		}
	GOTO        L_programMode29
L_programMode30:
;PlcPic.c,294 :: 		++transactionID;
	INFSNZ      121, 1 
	INCF        122, 1 
;PlcPic.c,295 :: 		sendAck();
	CALL        _sendAck+0, 0
;PlcPic.c,296 :: 		cmd = cmdFREE;
	CLRF        _cmd+0 
;PlcPic.c,297 :: 		break;
	GOTO        L_programMode27
;PlcPic.c,299 :: 		case cmdCONNECT: // Cmd: Connect PLC and PC app.
L_programMode34:
;PlcPic.c,301 :: 		connected = 1;
	BSF         RC0_bit+0, BitPos(RC0_bit+0) 
;PlcPic.c,302 :: 		lcd_out(2, 1, "CONNECTED TO PC.");
	MOVLW       2
	MOVWF       FARG_Lcd_Out_row+0 
	MOVLW       1
	MOVWF       FARG_Lcd_Out_column+0 
	MOVLW       ?lstr3_PlcPic+0
	MOVWF       FARG_Lcd_Out_text+0 
	MOVLW       hi_addr(?lstr3_PlcPic+0)
	MOVWF       FARG_Lcd_Out_text+1 
	CALL        _Lcd_Out+0, 0
;PlcPic.c,303 :: 		state = ACK;
	MOVLW       6
	MOVWF       120 
;PlcPic.c,304 :: 		transactionID = 0;
	CLRF        121 
	CLRF        122 
;PlcPic.c,305 :: 		sendAck();
	CALL        _sendAck+0, 0
;PlcPic.c,306 :: 		cmd = cmdFREE; // Set 'Idle' command code.
	CLRF        _cmd+0 
;PlcPic.c,307 :: 		break;
	GOTO        L_programMode27
;PlcPic.c,309 :: 		case cmdWRITE_FLASH:
L_programMode35:
;PlcPic.c,311 :: 		state = NAK;
	MOVLW       15
	MOVWF       120 
;PlcPic.c,312 :: 		if (address >= PROGRAM_START) { //If received address is inside OS
	MOVLW       32
	SUBWF       118, 0 
	BTFSS       STATUS+0, 2 
	GOTO        L__programMode68
	MOVLW       0
	SUBWF       117, 0 
L__programMode68:
	BTFSS       STATUS+0, 0 
	GOTO        L_programMode36
;PlcPic.c,313 :: 		FLASH_Write_32(address, &readbuff[6]); //write data to Flash
	MOVF        117, 0 
	MOVWF       FARG_FLASH_Write_32_address+0 
	MOVF        118, 0 
	MOVWF       FARG_FLASH_Write_32_address+1 
	MOVLW       0
	MOVWF       FARG_FLASH_Write_32_address+2 
	MOVWF       FARG_FLASH_Write_32_address+3 
	MOVLW       _readbuff+6
	MOVWF       FARG_FLASH_Write_32_data_+0 
	MOVLW       hi_addr(_readbuff+6)
	MOVWF       FARG_FLASH_Write_32_data_+1 
	CALL        _FLASH_Write_32+0, 0
;PlcPic.c,314 :: 		lcd_out(2, 1, "PROGRAMMING.....");
	MOVLW       2
	MOVWF       FARG_Lcd_Out_row+0 
	MOVLW       1
	MOVWF       FARG_Lcd_Out_column+0 
	MOVLW       ?lstr4_PlcPic+0
	MOVWF       FARG_Lcd_Out_text+0 
	MOVLW       hi_addr(?lstr4_PlcPic+0)
	MOVWF       FARG_Lcd_Out_text+1 
	CALL        _Lcd_Out+0, 0
;PlcPic.c,315 :: 		state = ACK;
	MOVLW       6
	MOVWF       120 
;PlcPic.c,316 :: 		programmed = 1;
	BSF         RC2_bit+0, BitPos(RC2_bit+0) 
;PlcPic.c,317 :: 		}
L_programMode36:
;PlcPic.c,318 :: 		++transactionID;
	INFSNZ      121, 1 
	INCF        122, 1 
;PlcPic.c,319 :: 		sendACk();
	CALL        _sendAck+0, 0
;PlcPic.c,320 :: 		cmd = cmdFREE;
	CLRF        _cmd+0 
;PlcPic.c,321 :: 		break;
	GOTO        L_programMode27
;PlcPic.c,323 :: 		case cmdREAD_FLASH:
L_programMode37:
;PlcPic.c,325 :: 		if (address >= PROGRAM_START) { //only read user program, not OS
	MOVLW       32
	SUBWF       118, 0 
	BTFSS       STATUS+0, 2 
	GOTO        L__programMode69
	MOVLW       0
	SUBWF       117, 0 
L__programMode69:
	BTFSS       STATUS+0, 0 
	GOTO        L_programMode38
;PlcPic.c,326 :: 		FLASH_Read_N_Bytes(address, &writebuff[0], 64); // read 64 bytes starting from address
	MOVF        117, 0 
	MOVWF       FARG_FLASH_Read_N_Bytes_address+0 
	MOVF        118, 0 
	MOVWF       FARG_FLASH_Read_N_Bytes_address+1 
	MOVLW       0
	MOVWF       FARG_FLASH_Read_N_Bytes_address+2 
	MOVWF       FARG_FLASH_Read_N_Bytes_address+3 
	MOVLW       _writebuff+0
	MOVWF       FARG_FLASH_Read_N_Bytes_data_+0 
	MOVLW       hi_addr(_writebuff+0)
	MOVWF       FARG_FLASH_Read_N_Bytes_data_+1 
	MOVLW       64
	MOVWF       FARG_FLASH_Read_N_Bytes_N+0 
	MOVLW       0
	MOVWF       FARG_FLASH_Read_N_Bytes_N+1 
	CALL        _FLASH_Read_N_Bytes+0, 0
;PlcPic.c,327 :: 		HID_Write(&writebuff[0], 64); // Send data.
	MOVLW       _writebuff+0
	MOVWF       FARG_HID_Write_writebuff+0 
	MOVLW       hi_addr(_writebuff+0)
	MOVWF       FARG_HID_Write_writebuff+1 
	MOVLW       64
	MOVWF       FARG_HID_Write_len+0 
	CALL        _HID_Write+0, 0
;PlcPic.c,328 :: 		} else {
	GOTO        L_programMode39
L_programMode38:
;PlcPic.c,329 :: 		state = NAK;
	MOVLW       15
	MOVWF       120 
;PlcPic.c,330 :: 		++transactionID;
	INFSNZ      121, 1 
	INCF        122, 1 
;PlcPic.c,331 :: 		sendAck();
	CALL        _sendAck+0, 0
;PlcPic.c,332 :: 		}
L_programMode39:
;PlcPic.c,333 :: 		cmd = cmdFREE; // Set 'Idle' command code
	CLRF        _cmd+0 
;PlcPic.c,334 :: 		break;
	GOTO        L_programMode27
;PlcPic.c,336 :: 		}
L_programMode26:
	MOVF        _cmd+0, 0 
	XORLW       5
	BTFSC       STATUS+0, 2 
	GOTO        L_programMode28
	MOVF        _cmd+0, 0 
	XORLW       6
	BTFSC       STATUS+0, 2 
	GOTO        L_programMode34
	MOVF        _cmd+0, 0 
	XORLW       1
	BTFSC       STATUS+0, 2 
	GOTO        L_programMode35
	MOVF        _cmd+0, 0 
	XORLW       2
	BTFSC       STATUS+0, 2 
	GOTO        L_programMode37
L_programMode27:
;PlcPic.c,337 :: 		}
L_programMode25:
;PlcPic.c,338 :: 		delay_ms(100);
	MOVLW       2
	MOVWF       R11, 0
	MOVLW       4
	MOVWF       R12, 0
	MOVLW       186
	MOVWF       R13, 0
L_programMode40:
	DECFSZ      R13, 1, 1
	BRA         L_programMode40
	DECFSZ      R12, 1, 1
	BRA         L_programMode40
	DECFSZ      R11, 1, 1
	BRA         L_programMode40
	NOP
;PlcPic.c,339 :: 		if (!plc_mode) goto program_loop; //else disable HID device and go back to run mode
	BTFSC       RA0_bit+0, BitPos(RA0_bit+0) 
	GOTO        L_programMode41
	GOTO        ___programMode_program_loop
L_programMode41:
;PlcPic.c,340 :: 		return;
;PlcPic.c,341 :: 		}
L_end_programMode:
	RETURN      0
; end of _programMode

_serviceRunCmd:

;PlcPic.c,343 :: 		void serviceRunCmd() {
;PlcPic.c,344 :: 		switch (cmd) { // Process command.
	GOTO        L_serviceRunCmd42
;PlcPic.c,345 :: 		case cmdREAD_RAM:
L_serviceRunCmd44:
;PlcPic.c,347 :: 		asm movff _address, fsr0;
	MOVFF       117, FSR0
;PlcPic.c,348 :: 		for (i = 0; i < length; i++) {
	CLRF        125 
L_serviceRunCmd45:
	MOVLW       128
	BTFSC       125, 7 
	MOVLW       127
	MOVWF       R0 
	MOVLW       128
	SUBWF       R0, 0 
	BTFSS       STATUS+0, 2 
	GOTO        L__serviceRunCmd71
	MOVF        116, 0 
	SUBWF       125, 0 
L__serviceRunCmd71:
	BTFSC       STATUS+0, 0 
	GOTO        L_serviceRunCmd46
;PlcPic.c,349 :: 		asm movff indf0, _ramByte;
	MOVFF       INDF0+0, 128
;PlcPic.c,350 :: 		writebuff[i] = ramByte;
	MOVLW       _writebuff+0
	MOVWF       FSR1 
	MOVLW       hi_addr(_writebuff+0)
	MOVWF       FSR1H 
	MOVF        125, 0 
	ADDWF       FSR1, 1 
	MOVLW       0
	BTFSC       125, 7 
	MOVLW       255
	ADDWFC      FSR1H, 1 
	MOVF        128, 0 
	MOVWF       POSTINC1+0 
;PlcPic.c,351 :: 		asm incf fsr0;
	INCF        FSR0, 1, 1
;PlcPic.c,348 :: 		for (i = 0; i < length; i++) {
	INCF        125, 1 
;PlcPic.c,352 :: 		}
	GOTO        L_serviceRunCmd45
L_serviceRunCmd46:
;PlcPic.c,353 :: 		HID_Write(&writebuff[0], 64); // Send data.
	MOVLW       _writebuff+0
	MOVWF       FARG_HID_Write_writebuff+0 
	MOVLW       hi_addr(_writebuff+0)
	MOVWF       FARG_HID_Write_writebuff+1 
	MOVLW       64
	MOVWF       FARG_HID_Write_len+0 
	CALL        _HID_Write+0, 0
;PlcPic.c,354 :: 		cmd = cmdFREE; // Set 'Idle' command code
	CLRF        _cmd+0 
;PlcPic.c,355 :: 		break;
	GOTO        L_serviceRunCmd43
;PlcPic.c,357 :: 		case cmdWRITE_RAM:
L_serviceRunCmd48:
;PlcPic.c,359 :: 		asm movff _address, fsr0;
	MOVFF       117, FSR0
;PlcPic.c,360 :: 		for (i = 0; i < length; i++) {
	CLRF        125 
L_serviceRunCmd49:
	MOVLW       128
	BTFSC       125, 7 
	MOVLW       127
	MOVWF       R0 
	MOVLW       128
	SUBWF       R0, 0 
	BTFSS       STATUS+0, 2 
	GOTO        L__serviceRunCmd72
	MOVF        116, 0 
	SUBWF       125, 0 
L__serviceRunCmd72:
	BTFSC       STATUS+0, 0 
	GOTO        L_serviceRunCmd50
;PlcPic.c,361 :: 		ramByte = readbuff[i+6];
	MOVLW       6
	ADDWF       125, 0 
	MOVWF       R0 
	MOVLW       0
	BTFSC       125, 7 
	MOVLW       255
	MOVWF       R1 
	MOVLW       0
	ADDWFC      R1, 1 
	MOVLW       _readbuff+0
	ADDWF       R0, 0 
	MOVWF       FSR0 
	MOVLW       hi_addr(_readbuff+0)
	ADDWFC      R1, 0 
	MOVWF       FSR0H 
	MOVF        POSTINC0+0, 0 
	MOVWF       128 
;PlcPic.c,362 :: 		asm movff _ramByte, indf0;
	MOVFF       128, INDF0+0
;PlcPic.c,363 :: 		asm incf fsr0;
	INCF        FSR0, 1, 1
;PlcPic.c,360 :: 		for (i = 0; i < length; i++) {
	INCF        125, 1 
;PlcPic.c,364 :: 		}
	GOTO        L_serviceRunCmd49
L_serviceRunCmd50:
;PlcPic.c,365 :: 		state = ACK;
	MOVLW       6
	MOVWF       120 
;PlcPic.c,366 :: 		++transactionID;
	INFSNZ      121, 1 
	INCF        122, 1 
;PlcPic.c,367 :: 		sendAck();
	CALL        _sendAck+0, 0
;PlcPic.c,368 :: 		cmd = cmdFREE; // Set 'Idle' command code
	CLRF        _cmd+0 
;PlcPic.c,369 :: 		break;
	GOTO        L_serviceRunCmd43
;PlcPic.c,371 :: 		}
L_serviceRunCmd42:
	MOVF        _cmd+0, 0 
	XORLW       4
	BTFSC       STATUS+0, 2 
	GOTO        L_serviceRunCmd44
	MOVF        _cmd+0, 0 
	XORLW       3
	BTFSC       STATUS+0, 2 
	GOTO        L_serviceRunCmd48
L_serviceRunCmd43:
;PlcPic.c,372 :: 		return;
;PlcPic.c,373 :: 		}
L_end_serviceRunCmd:
	RETURN      0
; end of _serviceRunCmd

_checkUsb:

;PlcPic.c,375 :: 		void checkUsb() {
;PlcPic.c,376 :: 		USB_Polling_Proc(); // Check USB for new data.
	CALL        _USB_Polling_Proc+0, 0
;PlcPic.c,377 :: 		rxByte = HID_Read();
	CALL        _HID_Read+0, 0
	MOVF        R0, 0 
	MOVWF       _rxByte+0 
;PlcPic.c,378 :: 		if (rxByte && cmd == cmdFREE) { // Do we have an incoming?
	MOVF        _rxByte+0, 1 
	BTFSC       STATUS+0, 2 
	GOTO        L_checkUsb54
	MOVF        _cmd+0, 0 
	XORLW       0
	BTFSS       STATUS+0, 2 
	GOTO        L_checkUsb54
L__checkUsb60:
;PlcPic.c,381 :: 		if (readbuff[0] != DLE) { // Do we have a 'DLE' at start?
	MOVLW       0
	XORLW       0
	BTFSS       STATUS+0, 2 
	GOTO        L__checkUsb74
	MOVLW       16
	XORWF       1280, 0 
L__checkUsb74:
	BTFSC       STATUS+0, 2 
	GOTO        L_checkUsb55
;PlcPic.c,382 :: 		return;
	GOTO        L_end_checkUsb
;PlcPic.c,383 :: 		}
L_checkUsb55:
;PlcPic.c,384 :: 		if (readbuff[1] != STX) { // Do we have a 'STX'?
	MOVLW       0
	XORLW       0
	BTFSS       STATUS+0, 2 
	GOTO        L__checkUsb75
	MOVLW       2
	XORWF       1281, 0 
L__checkUsb75:
	BTFSC       STATUS+0, 2 
	GOTO        L_checkUsb56
;PlcPic.c,385 :: 		return; // received data is not protocol, return.
	GOTO        L_end_checkUsb
;PlcPic.c,386 :: 		}
L_checkUsb56:
;PlcPic.c,389 :: 		cmd = readbuff[2]; // Get command code.
	MOVF        1282, 0 
	MOVWF       _cmd+0 
;PlcPic.c,390 :: 		Hi(address) = readbuff[3]; // Get address hi byte.
	MOVF        1283, 0 
	MOVWF       118 
;PlcPic.c,391 :: 		Lo(address) = readbuff[4]; // Get address low byte.
	MOVF        1284, 0 
	MOVWF       117 
;PlcPic.c,392 :: 		length = readBuff[5]; // Get counter byte
	MOVF        1285, 0 
	MOVWF       116 
;PlcPic.c,393 :: 		}
L_checkUsb54:
;PlcPic.c,394 :: 		return;
;PlcPic.c,395 :: 		}
L_end_checkUsb:
	RETURN      0
; end of _checkUsb

_sendAck:

;PlcPic.c,410 :: 		void sendAck() {
;PlcPic.c,412 :: 		writebuff[0] = DLE; // Start of packet identifier.
	MOVLW       16
	MOVWF       1344 
;PlcPic.c,413 :: 		writebuff[1] = cmd; // Command code to acknowledge.
	MOVF        _cmd+0, 0 
	MOVWF       1345 
;PlcPic.c,414 :: 		writebuff[2] = state; //acknowledgment status
	MOVF        120, 0 
	MOVWF       1346 
;PlcPic.c,415 :: 		writebuff[3] = Hi(transactionID); //message number
	MOVF        122, 0 
	MOVWF       1347 
;PlcPic.c,416 :: 		writebuff[4] = Lo(transactionID);
	MOVF        121, 0 
	MOVWF       1348 
;PlcPic.c,417 :: 		for (i = 5; i < 64; i++) {
	MOVLW       5
	MOVWF       125 
L_sendAck57:
	MOVLW       128
	XORWF       125, 0 
	MOVWF       R0 
	MOVLW       128
	XORLW       64
	SUBWF       R0, 0 
	BTFSC       STATUS+0, 0 
	GOTO        L_sendAck58
;PlcPic.c,418 :: 		writebuff[i] = 0;
	MOVLW       _writebuff+0
	MOVWF       FSR1 
	MOVLW       hi_addr(_writebuff+0)
	MOVWF       FSR1H 
	MOVF        125, 0 
	ADDWF       FSR1, 1 
	MOVLW       0
	BTFSC       125, 7 
	MOVLW       255
	ADDWFC      FSR1H, 1 
	CLRF        POSTINC1+0 
;PlcPic.c,417 :: 		for (i = 5; i < 64; i++) {
	INCF        125, 1 
;PlcPic.c,419 :: 		}
	GOTO        L_sendAck57
L_sendAck58:
;PlcPic.c,420 :: 		HID_Write(&writebuff[0], 64); // Send acknowledgment packet..
	MOVLW       _writebuff+0
	MOVWF       FARG_HID_Write_writebuff+0 
	MOVLW       hi_addr(_writebuff+0)
	MOVWF       FARG_HID_Write_writebuff+1 
	MOVLW       64
	MOVWF       FARG_HID_Write_len+0 
	CALL        _HID_Write+0, 0
;PlcPic.c,421 :: 		return;
;PlcPic.c,422 :: 		}
L_end_sendAck:
	RETURN      0
; end of _sendAck

_userProgram:

;PlcPic.c,424 :: 		void userProgram() org 0x2000 {
;PlcPic.c,425 :: 		}
L_end_userProgram:
	RETURN      0
; end of _userProgram
