
_interrupt:
	MOVWF      R15+0
	SWAPF      STATUS+0, 0
	CLRF       STATUS+0
	MOVWF      ___saveSTATUS+0
	MOVF       PCLATH+0, 0
	MOVWF      ___savePCLATH+0
	CLRF       PCLATH+0

;Uart_Test.c,22 :: 		void interrupt(){
;Uart_Test.c,23 :: 		if(pir1.rcif){
	BTFSS      PIR1+0, 5
	GOTO       L_interrupt0
;Uart_Test.c,25 :: 		rxByte=rcreg;
	MOVF       RCREG+0, 0
	MOVWF      _rxByte+0
;Uart_Test.c,26 :: 		if(rxByte==0x35){
	MOVF       _rxByte+0, 0
	XORLW      53
	BTFSS      STATUS+0, 2
	GOTO       L_interrupt1
;Uart_Test.c,27 :: 		counter=0;
	CLRF       _counter+0
;Uart_Test.c,28 :: 		dataReady=1;
	BSF        _dataReady+0, BitPos(_dataReady+0)
;Uart_Test.c,29 :: 		return;
	GOTO       L__interrupt10
;Uart_Test.c,30 :: 		}
L_interrupt1:
;Uart_Test.c,32 :: 		txt[counter]=rxByte;
	MOVF       _counter+0, 0
	ADDLW      _txt+0
	MOVWF      FSR
	MOVF       _rxByte+0, 0
	MOVWF      INDF+0
;Uart_Test.c,33 :: 		++counter;
	INCF       _counter+0, 1
;Uart_Test.c,34 :: 		if(counter>10)counter=0;
	MOVLW      128
	XORLW      10
	MOVWF      R0+0
	MOVLW      128
	XORWF      _counter+0, 0
	SUBWF      R0+0, 0
	BTFSC      STATUS+0, 0
	GOTO       L_interrupt3
	CLRF       _counter+0
L_interrupt3:
;Uart_Test.c,35 :: 		return;
	GOTO       L__interrupt10
;Uart_Test.c,38 :: 		}
L_interrupt0:
;Uart_Test.c,39 :: 		}
L_end_interrupt:
L__interrupt10:
	MOVF       ___savePCLATH+0, 0
	MOVWF      PCLATH+0
	SWAPF      ___saveSTATUS+0, 0
	MOVWF      STATUS+0
	SWAPF      R15+0, 1
	SWAPF      R15+0, 0
	RETFIE
; end of _interrupt

_main:

;Uart_Test.c,40 :: 		void main() {
;Uart_Test.c,41 :: 		ANSEL  = 0;                        // Configure AN pins as digital I/O
	CLRF       ANSEL+0
;Uart_Test.c,42 :: 		ANSELH = 0;
	CLRF       ANSELH+0
;Uart_Test.c,43 :: 		C1ON_bit = 0;                      // Disable comparators
	BCF        C1ON_bit+0, BitPos(C1ON_bit+0)
;Uart_Test.c,44 :: 		C2ON_bit = 0;
	BCF        C2ON_bit+0, BitPos(C2ON_bit+0)
;Uart_Test.c,45 :: 		TRISB  = 0;              // analog pin is input
	CLRF       TRISB+0
;Uart_Test.c,46 :: 		PORTB=0;
	CLRF       PORTB+0
;Uart_Test.c,48 :: 		Lcd_Init();                        // Initialize LCD
	CALL       _Lcd_Init+0
;Uart_Test.c,49 :: 		Lcd_Cmd(_LCD_CLEAR);               // Clear display
	MOVLW      1
	MOVWF      FARG_Lcd_Cmd_out_char+0
	CALL       _Lcd_Cmd+0
;Uart_Test.c,50 :: 		Lcd_Cmd(_LCD_CURSOR_OFF);          // Cursor off
	MOVLW      12
	MOVWF      FARG_Lcd_Cmd_out_char+0
	CALL       _Lcd_Cmd+0
;Uart_Test.c,51 :: 		dataReady=0;
	BCF        _dataReady+0, BitPos(_dataReady+0)
;Uart_Test.c,52 :: 		counter=0;
	CLRF       _counter+0
;Uart_Test.c,54 :: 		intcon.gie=1;
	BSF        INTCON+0, 7
;Uart_Test.c,55 :: 		intcon.peie=1;
	BSF        INTCON+0, 6
;Uart_Test.c,56 :: 		pie1.rcie=1;
	BSF        PIE1+0, 5
;Uart_Test.c,58 :: 		uart1_init(9600);
	MOVLW      129
	MOVWF      SPBRG+0
	BSF        TXSTA+0, 2
	CALL       _UART1_Init+0
;Uart_Test.c,59 :: 		delay_ms(100);
	MOVLW      3
	MOVWF      R11+0
	MOVLW      138
	MOVWF      R12+0
	MOVLW      85
	MOVWF      R13+0
L_main4:
	DECFSZ     R13+0, 1
	GOTO       L_main4
	DECFSZ     R12+0, 1
	GOTO       L_main4
	DECFSZ     R11+0, 1
	GOTO       L_main4
	NOP
	NOP
;Uart_Test.c,61 :: 		while(1){
L_main5:
;Uart_Test.c,62 :: 		if(dataReady){
	BTFSS      _dataReady+0, BitPos(_dataReady+0)
	GOTO       L_main7
;Uart_Test.c,63 :: 		lcd_cmd(_lcd_clear);
	MOVLW      1
	MOVWF      FARG_Lcd_Cmd_out_char+0
	CALL       _Lcd_Cmd+0
;Uart_Test.c,64 :: 		lcd_out(1,1,txt);
	MOVLW      1
	MOVWF      FARG_Lcd_Out_row+0
	MOVLW      1
	MOVWF      FARG_Lcd_Out_column+0
	MOVLW      _txt+0
	MOVWF      FARG_Lcd_Out_text+0
	CALL       _Lcd_Out+0
;Uart_Test.c,65 :: 		dataReady=0;
	BCF        _dataReady+0, BitPos(_dataReady+0)
;Uart_Test.c,66 :: 		}
L_main7:
;Uart_Test.c,67 :: 		delay_ms(100);
	MOVLW      3
	MOVWF      R11+0
	MOVLW      138
	MOVWF      R12+0
	MOVLW      85
	MOVWF      R13+0
L_main8:
	DECFSZ     R13+0, 1
	GOTO       L_main8
	DECFSZ     R12+0, 1
	GOTO       L_main8
	DECFSZ     R11+0, 1
	GOTO       L_main8
	NOP
	NOP
;Uart_Test.c,68 :: 		}
	GOTO       L_main5
;Uart_Test.c,69 :: 		}
L_end_main:
	GOTO       $+0
; end of _main
