
_main:

;Pic18.c,76 :: 		void main() {
;Pic18.c,77 :: 		ADCON1 |= 0x0F; // Configure all ports with analog function as digital
	MOVLW       15
	IORWF       ADCON1+0, 1 
;Pic18.c,78 :: 		CMCON |= 7; // Disable comparators
	MOVLW       7
	IORWF       CMCON+0, 1 
;Pic18.c,80 :: 		plc_mode_dir = 1;
	BSF         TRISA0_bit+0, BitPos(TRISA0_bit+0) 
;Pic18.c,81 :: 		anIn0=0;
	CLRF        96 
	CLRF        97 
;Pic18.c,82 :: 		anOut=0;
	CLRF        113 
	CLRF        114 
;Pic18.c,83 :: 		portb=0;
	CLRF        PORTB+0 
;Pic18.c,84 :: 		trisb=0;
	CLRF        TRISB+0 
;Pic18.c,85 :: 		Lcd_Init(); // Initialize LCD
	CALL        _Lcd_Init+0, 0
;Pic18.c,86 :: 		Lcd_Cmd(_LCD_CLEAR); // Clear display
	MOVLW       1
	MOVWF       FARG_Lcd_Cmd_out_char+0 
	CALL        _Lcd_Cmd+0, 0
;Pic18.c,87 :: 		Lcd_Cmd(_LCD_UNDERLINE_ON); // Cursor off
	MOVLW       14
	MOVWF       FARG_Lcd_Cmd_out_char+0 
	CALL        _Lcd_Cmd+0, 0
;Pic18.c,88 :: 		Lcd_Out(1, 1, "ADC Value:"); // Write text in first row
	MOVLW       1
	MOVWF       FARG_Lcd_Out_row+0 
	MOVLW       1
	MOVWF       FARG_Lcd_Out_column+0 
	MOVLW       ?lstr1_Pic18+0
	MOVWF       FARG_Lcd_Out_text+0 
	MOVLW       hi_addr(?lstr1_Pic18+0)
	MOVWF       FARG_Lcd_Out_text+1 
	CALL        _Lcd_Out+0, 0
;Pic18.c,89 :: 		Lcd_Out(2, 1, "DAC Value:"); // Write text in first row
	MOVLW       2
	MOVWF       FARG_Lcd_Out_row+0 
	MOVLW       1
	MOVWF       FARG_Lcd_Out_column+0 
	MOVLW       ?lstr2_Pic18+0
	MOVWF       FARG_Lcd_Out_text+0 
	MOVLW       hi_addr(?lstr2_Pic18+0)
	MOVWF       FARG_Lcd_Out_text+1 
	CALL        _Lcd_Out+0, 0
;Pic18.c,90 :: 		delay_ms(500);
	MOVLW       16
	MOVWF       R11, 0
	MOVLW       57
	MOVWF       R12, 0
	MOVLW       13
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
;Pic18.c,91 :: 		Uart1_Init(9600);
	BSF         BAUDCON+0, 3, 0
	MOVLW       2
	MOVWF       SPBRGH+0 
	MOVLW       112
	MOVWF       SPBRG+0 
	BSF         TXSTA+0, 2, 0
	CALL        _UART1_Init+0, 0
;Pic18.c,92 :: 		delay_ms(100);
	MOVLW       4
	MOVWF       R11, 0
	MOVLW       12
	MOVWF       R12, 0
	MOVLW       51
	MOVWF       R13, 0
L_main1:
	DECFSZ      R13, 1, 1
	BRA         L_main1
	DECFSZ      R12, 1, 1
	BRA         L_main1
	DECFSZ      R11, 1, 1
	BRA         L_main1
	NOP
	NOP
;Pic18.c,93 :: 		while (1) {
L_main2:
;Pic18.c,94 :: 		runMode();
	CALL        _runMode+0, 0
;Pic18.c,95 :: 		}
	GOTO        L_main2
;Pic18.c,96 :: 		}
L_end_main:
	GOTO        $+0
; end of _main

_runMode:

;Pic18.c,111 :: 		void runMode() {
;Pic18.c,112 :: 		while(plc_mode){
L_runMode4:
	BTFSS       RA0_bit+0, BitPos(RA0_bit+0) 
	GOTO        L_runMode5
;Pic18.c,114 :: 		for (i = 0; i < 17; i++) { //read 17 bytes worth of input data
	CLRF        _i+0 
L_runMode6:
	MOVLW       128
	XORWF       _i+0, 0 
	MOVWF       R0 
	MOVLW       128
	XORLW       17
	SUBWF       R0, 0 
	BTFSC       STATUS+0, 0 
	GOTO        L_runMode7
;Pic18.c,115 :: 		uart1_write(2); //send request for data to be read
	MOVLW       2
	MOVWF       FARG_UART1_Write_data_+0 
	CALL        _UART1_Write+0, 0
;Pic18.c,116 :: 		delay_ms(10);
	MOVLW       78
	MOVWF       R12, 0
	MOVLW       235
	MOVWF       R13, 0
L_runMode9:
	DECFSZ      R13, 1, 1
	BRA         L_runMode9
	DECFSZ      R12, 1, 1
	BRA         L_runMode9
;Pic18.c,117 :: 		if (uart1_data_ready())
	CALL        _UART1_Data_Ready+0, 0
	MOVF        R0, 1 
	BTFSC       STATUS+0, 2 
	GOTO        L_runMode10
;Pic18.c,118 :: 		io[i] = uart1_read();
	MOVLW       _io+0
	MOVWF       FLOC__runMode+0 
	MOVLW       hi_addr(_io+0)
	MOVWF       FLOC__runMode+1 
	MOVF        _i+0, 0 
	ADDWF       FLOC__runMode+0, 1 
	MOVLW       0
	BTFSC       _i+0, 7 
	MOVLW       255
	ADDWFC      FLOC__runMode+1, 1 
	CALL        _UART1_Read+0, 0
	MOVFF       FLOC__runMode+0, FSR1
	MOVFF       FLOC__runMode+1, FSR1H
	MOVF        R0, 0 
	MOVWF       POSTINC1+0 
L_runMode10:
;Pic18.c,114 :: 		for (i = 0; i < 17; i++) { //read 17 bytes worth of input data
	INCF        _i+0, 1 
;Pic18.c,119 :: 		}
	GOTO        L_runMode6
L_runMode7:
;Pic18.c,121 :: 		userProgram();
	CALL        _userProgram+0, 0
;Pic18.c,123 :: 		for (i = 17; i < 20; i++) {
	MOVLW       17
	MOVWF       _i+0 
L_runMode11:
	MOVLW       128
	XORWF       _i+0, 0 
	MOVWF       R0 
	MOVLW       128
	XORLW       20
	SUBWF       R0, 0 
	BTFSC       STATUS+0, 0 
	GOTO        L_runMode12
;Pic18.c,124 :: 		while (!UART1_Tx_Idle()); //wait for transmission to finish
L_runMode14:
	CALL        _UART1_Tx_Idle+0, 0
	MOVF        R0, 1 
	BTFSS       STATUS+0, 2 
	GOTO        L_runMode15
	GOTO        L_runMode14
L_runMode15:
;Pic18.c,125 :: 		uart1_write(io[i]);
	MOVLW       _io+0
	MOVWF       FSR0 
	MOVLW       hi_addr(_io+0)
	MOVWF       FSR0H 
	MOVF        _i+0, 0 
	ADDWF       FSR0, 1 
	MOVLW       0
	BTFSC       _i+0, 7 
	MOVLW       255
	ADDWFC      FSR0H, 1 
	MOVF        POSTINC0+0, 0 
	MOVWF       FARG_UART1_Write_data_+0 
	CALL        _UART1_Write+0, 0
;Pic18.c,126 :: 		delay_ms(10);
	MOVLW       78
	MOVWF       R12, 0
	MOVLW       235
	MOVWF       R13, 0
L_runMode16:
	DECFSZ      R13, 1, 1
	BRA         L_runMode16
	DECFSZ      R12, 1, 1
	BRA         L_runMode16
;Pic18.c,123 :: 		for (i = 17; i < 20; i++) {
	INCF        _i+0, 1 
;Pic18.c,127 :: 		}
	GOTO        L_runMode11
L_runMode12:
;Pic18.c,128 :: 		IntToStr(anIn0,line1);
	MOVF        96, 0 
	MOVWF       FARG_IntToStr_input+0 
	MOVF        97, 0 
	MOVWF       FARG_IntToStr_input+1 
	MOVLW       _line1+0
	MOVWF       FARG_IntToStr_output+0 
	MOVLW       hi_addr(_line1+0)
	MOVWF       FARG_IntToStr_output+1 
	CALL        _IntToStr+0, 0
;Pic18.c,129 :: 		IntToStr(anOut,line2);
	MOVF        113, 0 
	MOVWF       FARG_IntToStr_input+0 
	MOVF        114, 0 
	MOVWF       FARG_IntToStr_input+1 
	MOVLW       _line2+0
	MOVWF       FARG_IntToStr_output+0 
	MOVLW       hi_addr(_line2+0)
	MOVWF       FARG_IntToStr_output+1 
	CALL        _IntToStr+0, 0
;Pic18.c,130 :: 		lcd_out(1,11,line1);
	MOVLW       1
	MOVWF       FARG_Lcd_Out_row+0 
	MOVLW       11
	MOVWF       FARG_Lcd_Out_column+0 
	MOVLW       _line1+0
	MOVWF       FARG_Lcd_Out_text+0 
	MOVLW       hi_addr(_line1+0)
	MOVWF       FARG_Lcd_Out_text+1 
	CALL        _Lcd_Out+0, 0
;Pic18.c,131 :: 		lcd_out(2,11,line2);
	MOVLW       2
	MOVWF       FARG_Lcd_Out_row+0 
	MOVLW       11
	MOVWF       FARG_Lcd_Out_column+0 
	MOVLW       _line2+0
	MOVWF       FARG_Lcd_Out_text+0 
	MOVLW       hi_addr(_line2+0)
	MOVWF       FARG_Lcd_Out_text+1 
	CALL        _Lcd_Out+0, 0
;Pic18.c,132 :: 		delay_ms(200);
	MOVLW       7
	MOVWF       R11, 0
	MOVLW       23
	MOVWF       R12, 0
	MOVLW       106
	MOVWF       R13, 0
L_runMode17:
	DECFSZ      R13, 1, 1
	BRA         L_runMode17
	DECFSZ      R12, 1, 1
	BRA         L_runMode17
	DECFSZ      R11, 1, 1
	BRA         L_runMode17
	NOP
;Pic18.c,133 :: 		}//else switch to program mode.
	GOTO        L_runMode4
L_runMode5:
;Pic18.c,134 :: 		return;
;Pic18.c,135 :: 		}
L_end_runMode:
	RETURN      0
; end of _runMode

_userProgram:

;Pic18.c,138 :: 		void userProgram() {
;Pic18.c,139 :: 		dio = dii;
	MOVF        112, 0 
	MOVWF       115 
;Pic18.c,140 :: 		anOut = anIn7;
	MOVF        110, 0 
	MOVWF       113 
	MOVF        111, 0 
	MOVWF       114 
;Pic18.c,141 :: 		return;
;Pic18.c,142 :: 		}
L_end_userProgram:
	RETURN      0
; end of _userProgram
