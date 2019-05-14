
_main:

;Pic18.c,25 :: 		void main() {
;Pic18.c,26 :: 		ADCON1 = 7; // Configure all ports with analog function as digital
	MOVLW       7
	MOVWF       ADCON1+0 
;Pic18.c,27 :: 		CMCON = 0b00000111; // Disable comparators
	MOVLW       7
	MOVWF       CMCON+0 
;Pic18.c,28 :: 		TRISC = 0;
	CLRF        TRISC+0 
;Pic18.c,29 :: 		PORTC = 0;
	CLRF        PORTC+0 
;Pic18.c,30 :: 		TRISA.B0 =0;
	BCF         TRISA+0, 0 
;Pic18.c,31 :: 		Lcd_Init();
	CALL        _Lcd_Init+0, 0
;Pic18.c,32 :: 		Lcd_Cmd(_LCD_CLEAR);               // Clear display
	MOVLW       1
	MOVWF       FARG_Lcd_Cmd_out_char+0 
	CALL        _Lcd_Cmd+0, 0
;Pic18.c,33 :: 		Lcd_Cmd(_LCD_UNDERLINE_ON);          // Cursor off
	MOVLW       14
	MOVWF       FARG_Lcd_Cmd_out_char+0 
	CALL        _Lcd_Cmd+0, 0
;Pic18.c,34 :: 		UART1_Init(9600);               // Initialize UART module at 9600 bps
	BSF         BAUDCON+0, 3, 0
	CLRF        SPBRGH+0 
	MOVLW       207
	MOVWF       SPBRG+0 
	BSF         TXSTA+0, 2, 0
	CALL        _UART1_Init+0, 0
;Pic18.c,35 :: 		Delay_ms(100);                  // Wait for UART module to stabilize
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
;Pic18.c,37 :: 		ss_dir=0;
	BCF         TRISB2_bit+0, BitPos(TRISB2_bit+0) 
;Pic18.c,38 :: 		while (1) {
L_main1:
;Pic18.c,39 :: 		ss=0;
	BCF         RB2_bit+0, BitPos(RB2_bit+0) 
;Pic18.c,40 :: 		while(!uart1_tx_idle);
L_main3:
	MOVLW       _UART1_Tx_Idle+0
	IORLW       hi_addr(_UART1_Tx_Idle+0)
	IORLW       0
	IORLW       0
	BTFSS       STATUS+0, 2 
	GOTO        L_main4
	GOTO        L_main3
L_main4:
;Pic18.c,41 :: 		uart1_write_text("pic18fx");
	MOVLW       ?lstr1_Pic18+0
	MOVWF       FARG_UART1_Write_Text_uart_text+0 
	MOVLW       hi_addr(?lstr1_Pic18+0)
	MOVWF       FARG_UART1_Write_Text_uart_text+1 
	CALL        _UART1_Write_Text+0, 0
;Pic18.c,42 :: 		while(!uart1_data_ready());
L_main5:
	CALL        _UART1_Data_Ready+0, 0
	MOVF        R0, 1 
	BTFSS       STATUS+0, 2 
	GOTO        L_main6
	GOTO        L_main5
L_main6:
;Pic18.c,43 :: 		uart1_read_text(txt,'x',1);
	MOVLW       Pic18_txt+0
	MOVWF       FARG_UART1_Read_Text_Output+0 
	MOVLW       hi_addr(Pic18_txt+0)
	MOVWF       FARG_UART1_Read_Text_Output+1 
	MOVLW       120
	MOVWF       FARG_UART1_Read_Text_Delimiter+0 
	MOVLW       0
	MOVWF       FARG_UART1_Read_Text_Delimiter+1 
	MOVLW       1
	MOVWF       FARG_UART1_Read_Text_Attempts+0 
	CALL        _UART1_Read_Text+0, 0
;Pic18.c,44 :: 		ss=1;
	BSF         RB2_bit+0, BitPos(RB2_bit+0) 
;Pic18.c,45 :: 		Lcd_Out(2,1,txt);                 // Write text in first row
	MOVLW       2
	MOVWF       FARG_Lcd_Out_row+0 
	MOVLW       1
	MOVWF       FARG_Lcd_Out_column+0 
	MOVLW       Pic18_txt+0
	MOVWF       FARG_Lcd_Out_text+0 
	MOVLW       hi_addr(Pic18_txt+0)
	MOVWF       FARG_Lcd_Out_text+1 
	CALL        _Lcd_Out+0, 0
;Pic18.c,46 :: 		delay_ms(100);
	MOVLW       2
	MOVWF       R11, 0
	MOVLW       4
	MOVWF       R12, 0
	MOVLW       186
	MOVWF       R13, 0
L_main7:
	DECFSZ      R13, 1, 1
	BRA         L_main7
	DECFSZ      R12, 1, 1
	BRA         L_main7
	DECFSZ      R11, 1, 1
	BRA         L_main7
	NOP
;Pic18.c,47 :: 		}
	GOTO        L_main1
;Pic18.c,48 :: 		}
L_end_main:
	GOTO        $+0
; end of _main
