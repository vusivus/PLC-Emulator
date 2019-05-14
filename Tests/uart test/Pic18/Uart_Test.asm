
_interrupt:

;Uart_Test.c,22 :: 		void interrupt(){
;Uart_Test.c,23 :: 		if(pir1.rcif){
	BTFSS       PIR1+0, 5 
	GOTO        L_interrupt0
;Uart_Test.c,24 :: 		pir1.rcif=0;
	BCF         PIR1+0, 5 
;Uart_Test.c,25 :: 		rxByte=uart1_read();
	CALL        _UART1_Read+0, 0
	MOVF        R0, 0 
	MOVWF       _rxByte+0 
;Uart_Test.c,26 :: 		if(rxByte=='5'){
	MOVF        R0, 0 
	XORLW       53
	BTFSS       STATUS+0, 2 
	GOTO        L_interrupt1
;Uart_Test.c,27 :: 		counter=0;
	CLRF        _counter+0 
;Uart_Test.c,28 :: 		dataReady=1;
	BSF         _dataReady+0, BitPos(_dataReady+0) 
;Uart_Test.c,29 :: 		}
	GOTO        L_interrupt2
L_interrupt1:
;Uart_Test.c,31 :: 		txt[counter]=rxByte;
	MOVLW       _txt+0
	MOVWF       FSR1 
	MOVLW       hi_addr(_txt+0)
	MOVWF       FSR1H 
	MOVF        _counter+0, 0 
	ADDWF       FSR1, 1 
	MOVLW       0
	BTFSC       _counter+0, 7 
	MOVLW       255
	ADDWFC      FSR1H, 1 
	MOVF        _rxByte+0, 0 
	MOVWF       POSTINC1+0 
;Uart_Test.c,32 :: 		++counter;
	INCF        _counter+0, 1 
;Uart_Test.c,33 :: 		}
L_interrupt2:
;Uart_Test.c,34 :: 		}
L_interrupt0:
;Uart_Test.c,35 :: 		}
L_end_interrupt:
L__interrupt8:
	RETFIE      1
; end of _interrupt

_main:

;Uart_Test.c,36 :: 		void main() {
;Uart_Test.c,37 :: 		ADCON1 |= 0x0F;                         // Configure all ports with analog function as digital
	MOVLW       15
	IORWF       ADCON1+0, 1 
;Uart_Test.c,38 :: 		CMCON  |= 7;                            // Disable comparators
	MOVLW       7
	IORWF       CMCON+0, 1 
;Uart_Test.c,40 :: 		Lcd_Init();                        // Initialize LCD
	CALL        _Lcd_Init+0, 0
;Uart_Test.c,41 :: 		Lcd_Cmd(_LCD_CLEAR);               // Clear display
	MOVLW       1
	MOVWF       FARG_Lcd_Cmd_out_char+0 
	CALL        _Lcd_Cmd+0, 0
;Uart_Test.c,42 :: 		Lcd_Cmd(_LCD_CURSOR_OFF);          // Cursor off
	MOVLW       12
	MOVWF       FARG_Lcd_Cmd_out_char+0 
	CALL        _Lcd_Cmd+0, 0
;Uart_Test.c,43 :: 		dataReady=0;
	BCF         _dataReady+0, BitPos(_dataReady+0) 
;Uart_Test.c,45 :: 		pie1.rcie=1;
	BSF         PIE1+0, 5 
;Uart_Test.c,46 :: 		intcon.gie=1;
	BSF         INTCON+0, 7 
;Uart_Test.c,47 :: 		intcon.peie=1;
	BSF         INTCON+0, 6 
;Uart_Test.c,48 :: 		uart1_init(9600);
	BSF         BAUDCON+0, 3, 0
	MOVLW       2
	MOVWF       SPBRGH+0 
	MOVLW       8
	MOVWF       SPBRG+0 
	BSF         TXSTA+0, 2, 0
	CALL        _UART1_Init+0, 0
;Uart_Test.c,49 :: 		while(1){
L_main3:
;Uart_Test.c,50 :: 		if(dataReady){
	BTFSS       _dataReady+0, BitPos(_dataReady+0) 
	GOTO        L_main5
;Uart_Test.c,51 :: 		lcd_cmd(_lcd_clear);
	MOVLW       1
	MOVWF       FARG_Lcd_Cmd_out_char+0 
	CALL        _Lcd_Cmd+0, 0
;Uart_Test.c,52 :: 		lcd_out(1,1,txt);
	MOVLW       1
	MOVWF       FARG_Lcd_Out_row+0 
	MOVLW       1
	MOVWF       FARG_Lcd_Out_column+0 
	MOVLW       _txt+0
	MOVWF       FARG_Lcd_Out_text+0 
	MOVLW       hi_addr(_txt+0)
	MOVWF       FARG_Lcd_Out_text+1 
	CALL        _Lcd_Out+0, 0
;Uart_Test.c,53 :: 		dataReady=0;
	BCF         _dataReady+0, BitPos(_dataReady+0) 
;Uart_Test.c,54 :: 		}
L_main5:
;Uart_Test.c,55 :: 		delay_ms(100);
	MOVLW       3
	MOVWF       R11, 0
	MOVLW       138
	MOVWF       R12, 0
	MOVLW       85
	MOVWF       R13, 0
L_main6:
	DECFSZ      R13, 1, 1
	BRA         L_main6
	DECFSZ      R12, 1, 1
	BRA         L_main6
	DECFSZ      R11, 1, 1
	BRA         L_main6
	NOP
	NOP
;Uart_Test.c,56 :: 		}
	GOTO        L_main3
;Uart_Test.c,57 :: 		}
L_end_main:
	GOTO        $+0
; end of _main
