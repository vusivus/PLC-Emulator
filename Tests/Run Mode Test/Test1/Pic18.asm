
_interrupt:

;Pic18.c,55 :: 		void interrupt() {
;Pic18.c,56 :: 		myISR();
	CALL        _myISR+0, 0
;Pic18.c,57 :: 		}
L_end_interrupt:
L__interrupt19:
	RETFIE      1
; end of _interrupt

_interrupt_lo:

;Pic18.c,59 :: 		void interrupt_lo() {
;Pic18.c,60 :: 		myISR();
	CALL        _myISR+0, 0
;Pic18.c,61 :: 		}
L_end_interrupt_lo:
	RETURN      0
; end of _interrupt_lo

_main:

;Pic18.c,73 :: 		void main() {
;Pic18.c,74 :: 		ADCON1 |= 0x0F; // Configure all ports with analog function as digital
	MOVLW       15
	IORWF       ADCON1+0, 1 
;Pic18.c,75 :: 		CMCON |= 7; // Disable comparators
	MOVLW       7
	IORWF       CMCON+0, 1 
;Pic18.c,77 :: 		plc_mode_dir = 1;
	BSF         TRISA0_bit+0, BitPos(TRISA0_bit+0) 
;Pic18.c,78 :: 		ss_dir = 0;
	BCF         TRISB2_bit+0, BitPos(TRISB2_bit+0) 
;Pic18.c,79 :: 		ss = 1;
	BSF         RB2_bit+0, BitPos(RB2_bit+0) 
;Pic18.c,80 :: 		Lcd_Init(); // Initialize LCD
	CALL        _Lcd_Init+0, 0
;Pic18.c,81 :: 		Lcd_Cmd(_LCD_CLEAR); // Clear display
	MOVLW       1
	MOVWF       FARG_Lcd_Cmd_out_char+0 
	CALL        _Lcd_Cmd+0, 0
;Pic18.c,82 :: 		Lcd_Cmd(_LCD_UNDERLINE_ON); // Cursor off
	MOVLW       14
	MOVWF       FARG_Lcd_Cmd_out_char+0 
	CALL        _Lcd_Cmd+0, 0
;Pic18.c,83 :: 		Lcd_Out(1, 1, "ADC Value:"); // Write text in first row
	MOVLW       1
	MOVWF       FARG_Lcd_Out_row+0 
	MOVLW       1
	MOVWF       FARG_Lcd_Out_column+0 
	MOVLW       ?lstr1_Pic18+0
	MOVWF       FARG_Lcd_Out_text+0 
	MOVLW       hi_addr(?lstr1_Pic18+0)
	MOVWF       FARG_Lcd_Out_text+1 
	CALL        _Lcd_Out+0, 0
;Pic18.c,84 :: 		Lcd_Out(2, 1, "DAC Value:"); // Write text in first row
	MOVLW       2
	MOVWF       FARG_Lcd_Out_row+0 
	MOVLW       1
	MOVWF       FARG_Lcd_Out_column+0 
	MOVLW       ?lstr2_Pic18+0
	MOVWF       FARG_Lcd_Out_text+0 
	MOVLW       hi_addr(?lstr2_Pic18+0)
	MOVWF       FARG_Lcd_Out_text+1 
	CALL        _Lcd_Out+0, 0
;Pic18.c,85 :: 		delay_ms(500);
	MOVLW       13
	MOVWF       R11, 0
	MOVLW       175
	MOVWF       R12, 0
	MOVLW       182
	MOVWF       R13, 0
L_main0:
	DECFSZ      R13, 1, 1
	BRA         L_main0
	DECFSZ      R12, 1, 1
	BRA         L_main0
	DECFSZ      R11, 1, 1
	BRA         L_main0
	NOP
;Pic18.c,86 :: 		Uart1_Init(9600);
	BSF         BAUDCON+0, 3, 0
	MOVLW       2
	MOVWF       SPBRGH+0 
	MOVLW       8
	MOVWF       SPBRG+0 
	BSF         TXSTA+0, 2, 0
	CALL        _UART1_Init+0, 0
;Pic18.c,87 :: 		delay_ms(100);
	MOVLW       3
	MOVWF       R11, 0
	MOVLW       138
	MOVWF       R12, 0
	MOVLW       85
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
;Pic18.c,89 :: 		intcon = 0;
	CLRF        INTCON+0 
;Pic18.c,90 :: 		intcon.peie = 1;
	BSF         INTCON+0, 6 
;Pic18.c,91 :: 		pie1.rcie = 1;
	BSF         PIE1+0, 5 
;Pic18.c,92 :: 		intcon.gie = 1;
	BSF         INTCON+0, 7 
;Pic18.c,94 :: 		delay_ms(100);
	MOVLW       3
	MOVWF       R11, 0
	MOVLW       138
	MOVWF       R12, 0
	MOVLW       85
	MOVWF       R13, 0
L_main2:
	DECFSZ      R13, 1, 1
	BRA         L_main2
	DECFSZ      R12, 1, 1
	BRA         L_main2
	DECFSZ      R11, 1, 1
	BRA         L_main2
	NOP
	NOP
;Pic18.c,95 :: 		while (1) {
L_main3:
;Pic18.c,96 :: 		runMode();
	CALL        _runMode+0, 0
;Pic18.c,97 :: 		}
	GOTO        L_main3
;Pic18.c,98 :: 		}
L_end_main:
	GOTO        $+0
; end of _main

_myISR:

;Pic18.c,100 :: 		void myISR() {
;Pic18.c,101 :: 		if (pir1.rcif) {
	BTFSS       PIR1+0, 5 
	GOTO        L_myISR5
;Pic18.c,102 :: 		rxByte = uart1_read();
	CALL        _UART1_Read+0, 0
	MOVF        R0, 0 
	MOVWF       _rxByte+0 
;Pic18.c,103 :: 		++session;
	INCF        _session+0, 1 
;Pic18.c,104 :: 		if (session == 1) {
	MOVF        _session+0, 0 
	XORLW       1
	BTFSS       STATUS+0, 2 
	GOTO        L_myISR6
;Pic18.c,105 :: 		dii = rxByte & 0x0F;
	MOVLW       15
	ANDWF       _rxByte+0, 0 
	MOVWF       517 
;Pic18.c,106 :: 		}
L_myISR6:
;Pic18.c,107 :: 		if (session == 2) {
	MOVF        _session+0, 0 
	XORLW       2
	BTFSS       STATUS+0, 2 
	GOTO        L_myISR7
;Pic18.c,108 :: 		Lo(anIn) = rxByte;
	MOVF        _rxByte+0, 0 
	MOVWF       512 
;Pic18.c,109 :: 		}
L_myISR7:
;Pic18.c,110 :: 		if (session == 3) {
	MOVF        _session+0, 0 
	XORLW       3
	BTFSS       STATUS+0, 2 
	GOTO        L_myISR8
;Pic18.c,111 :: 		Hi(anIn) = rxByte;
	MOVF        _rxByte+0, 0 
	MOVWF       513 
;Pic18.c,112 :: 		}
L_myISR8:
;Pic18.c,113 :: 		if (session >= 3) {
	MOVLW       128
	XORWF       _session+0, 0 
	MOVWF       R0 
	MOVLW       128
	XORLW       3
	SUBWF       R0, 0 
	BTFSS       STATUS+0, 0 
	GOTO        L_myISR9
;Pic18.c,114 :: 		session = 0;
	CLRF        _session+0 
;Pic18.c,115 :: 		}
L_myISR9:
;Pic18.c,116 :: 		return;
	GOTO        L_end_myISR
;Pic18.c,117 :: 		}
L_myISR5:
;Pic18.c,118 :: 		}
L_end_myISR:
	RETURN      0
; end of _myISR

_runMode:

;Pic18.c,133 :: 		void runMode() {
;Pic18.c,134 :: 		while (plc_mode) {
L_runMode10:
	BTFSS       RA0_bit+0, BitPos(RA0_bit+0) 
	GOTO        L_runMode11
;Pic18.c,135 :: 		readInputs();
	CALL        _readInputs+0, 0
;Pic18.c,136 :: 		userProgram();
	CALL        _userProgram+0, 0
;Pic18.c,137 :: 		delay_ms(50);
	MOVLW       2
	MOVWF       R11, 0
	MOVLW       69
	MOVWF       R12, 0
	MOVLW       169
	MOVWF       R13, 0
L_runMode12:
	DECFSZ      R13, 1, 1
	BRA         L_runMode12
	DECFSZ      R12, 1, 1
	BRA         L_runMode12
	DECFSZ      R11, 1, 1
	BRA         L_runMode12
	NOP
	NOP
;Pic18.c,138 :: 		writeOutputs();
	CALL        _writeOutputs+0, 0
;Pic18.c,139 :: 		IntToStr(anIn, line1);
	MOVF        512, 0 
	MOVWF       FARG_IntToStr_input+0 
	MOVF        513, 0 
	MOVWF       FARG_IntToStr_input+1 
	MOVLW       _line1+0
	MOVWF       FARG_IntToStr_output+0 
	MOVLW       hi_addr(_line1+0)
	MOVWF       FARG_IntToStr_output+1 
	CALL        _IntToStr+0, 0
;Pic18.c,140 :: 		Lcd_Out(1, 11, line1); // Display analogue input in row1
	MOVLW       1
	MOVWF       FARG_Lcd_Out_row+0 
	MOVLW       11
	MOVWF       FARG_Lcd_Out_column+0 
	MOVLW       _line1+0
	MOVWF       FARG_Lcd_Out_text+0 
	MOVLW       hi_addr(_line1+0)
	MOVWF       FARG_Lcd_Out_text+1 
	CALL        _Lcd_Out+0, 0
;Pic18.c,141 :: 		IntToStr(anOut, line2);
	MOVF        514, 0 
	MOVWF       FARG_IntToStr_input+0 
	MOVF        515, 0 
	MOVWF       FARG_IntToStr_input+1 
	MOVLW       _line2+0
	MOVWF       FARG_IntToStr_output+0 
	MOVLW       hi_addr(_line2+0)
	MOVWF       FARG_IntToStr_output+1 
	CALL        _IntToStr+0, 0
;Pic18.c,142 :: 		Lcd_Out(2, 11, line2); // Display analogue output in row2
	MOVLW       2
	MOVWF       FARG_Lcd_Out_row+0 
	MOVLW       11
	MOVWF       FARG_Lcd_Out_column+0 
	MOVLW       _line2+0
	MOVWF       FARG_Lcd_Out_text+0 
	MOVLW       hi_addr(_line2+0)
	MOVWF       FARG_Lcd_Out_text+1 
	CALL        _Lcd_Out+0, 0
;Pic18.c,143 :: 		delay_ms(100);
	MOVLW       3
	MOVWF       R11, 0
	MOVLW       138
	MOVWF       R12, 0
	MOVLW       85
	MOVWF       R13, 0
L_runMode13:
	DECFSZ      R13, 1, 1
	BRA         L_runMode13
	DECFSZ      R12, 1, 1
	BRA         L_runMode13
	DECFSZ      R11, 1, 1
	BRA         L_runMode13
	NOP
	NOP
;Pic18.c,144 :: 		}
	GOTO        L_runMode10
L_runMode11:
;Pic18.c,145 :: 		return;
;Pic18.c,146 :: 		}
L_end_runMode:
	RETURN      0
; end of _runMode

_readInputs:

;Pic18.c,161 :: 		void readInputs() {
;Pic18.c,162 :: 		uart1_write(READ);
	MOVLW       14
	MOVWF       FARG_UART1_Write_data_+0 
	CALL        _UART1_Write+0, 0
;Pic18.c,163 :: 		delay_ms(1);
	MOVLW       7
	MOVWF       R12, 0
	MOVLW       125
	MOVWF       R13, 0
L_readInputs14:
	DECFSZ      R13, 1, 1
	BRA         L_readInputs14
	DECFSZ      R12, 1, 1
	BRA         L_readInputs14
;Pic18.c,164 :: 		uart1_write(READ);
	MOVLW       14
	MOVWF       FARG_UART1_Write_data_+0 
	CALL        _UART1_Write+0, 0
;Pic18.c,165 :: 		delay_ms(1);
	MOVLW       7
	MOVWF       R12, 0
	MOVLW       125
	MOVWF       R13, 0
L_readInputs15:
	DECFSZ      R13, 1, 1
	BRA         L_readInputs15
	DECFSZ      R12, 1, 1
	BRA         L_readInputs15
;Pic18.c,166 :: 		uart1_write(READ);
	MOVLW       14
	MOVWF       FARG_UART1_Write_data_+0 
	CALL        _UART1_Write+0, 0
;Pic18.c,167 :: 		return;
;Pic18.c,168 :: 		}
L_end_readInputs:
	RETURN      0
; end of _readInputs

_writeOutputs:

;Pic18.c,183 :: 		void writeOutputs() {
;Pic18.c,184 :: 		txByte = dio << 4;
	MOVLW       4
	MOVWF       R1 
	MOVF        518, 0 
	MOVWF       R0 
	MOVF        R1, 0 
L__writeOutputs26:
	BZ          L__writeOutputs27
	RLCF        R0, 1 
	BCF         R0, 0 
	ADDLW       255
	GOTO        L__writeOutputs26
L__writeOutputs27:
	MOVF        R0, 0 
	MOVWF       _txByte+0 
;Pic18.c,185 :: 		uart1_write(txByte);
	MOVF        R0, 0 
	MOVWF       FARG_UART1_Write_data_+0 
	CALL        _UART1_Write+0, 0
;Pic18.c,186 :: 		delay_ms(1);
	MOVLW       7
	MOVWF       R12, 0
	MOVLW       125
	MOVWF       R13, 0
L_writeOutputs16:
	DECFSZ      R13, 1, 1
	BRA         L_writeOutputs16
	DECFSZ      R12, 1, 1
	BRA         L_writeOutputs16
;Pic18.c,187 :: 		txByte = Lo(anOut);
	MOVF        514, 0 
	MOVWF       _txByte+0 
;Pic18.c,188 :: 		uart1_write(txByte);
	MOVF        514, 0 
	MOVWF       FARG_UART1_Write_data_+0 
	CALL        _UART1_Write+0, 0
;Pic18.c,189 :: 		delay_ms(1);
	MOVLW       7
	MOVWF       R12, 0
	MOVLW       125
	MOVWF       R13, 0
L_writeOutputs17:
	DECFSZ      R13, 1, 1
	BRA         L_writeOutputs17
	DECFSZ      R12, 1, 1
	BRA         L_writeOutputs17
;Pic18.c,190 :: 		txByte = Hi(anOut);
	MOVF        515, 0 
	MOVWF       _txByte+0 
;Pic18.c,191 :: 		uart1_write(txByte);
	MOVF        515, 0 
	MOVWF       FARG_UART1_Write_data_+0 
	CALL        _UART1_Write+0, 0
;Pic18.c,192 :: 		return;
;Pic18.c,193 :: 		}
L_end_writeOutputs:
	RETURN      0
; end of _writeOutputs

_userProgram:

;Pic18.c,195 :: 		void userProgram() {
;Pic18.c,196 :: 		dio = dii;
	MOVF        517, 0 
	MOVWF       518 
;Pic18.c,197 :: 		anOut = anIn * 10;
	MOVF        512, 0 
	MOVWF       R0 
	MOVF        513, 0 
	MOVWF       R1 
	MOVLW       10
	MOVWF       R4 
	MOVLW       0
	MOVWF       R5 
	CALL        _Mul_16X16_U+0, 0
	MOVF        R0, 0 
	MOVWF       514 
	MOVF        R1, 0 
	MOVWF       515 
;Pic18.c,198 :: 		return;
;Pic18.c,199 :: 		}
L_end_userProgram:
	RETURN      0
; end of _userProgram
