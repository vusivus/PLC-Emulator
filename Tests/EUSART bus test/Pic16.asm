
_interrupt:
	MOVWF      R15+0
	SWAPF      STATUS+0, 0
	CLRF       STATUS+0
	MOVWF      ___saveSTATUS+0
	MOVF       PCLATH+0, 0
	MOVWF      ___savePCLATH+0
	CLRF       PCLATH+0

;Pic16.c,34 :: 		void interrupt(){
;Pic16.c,35 :: 		myISR();
	CALL       _myISR+0
;Pic16.c,36 :: 		}
L_end_interrupt:
L__interrupt8:
	MOVF       ___savePCLATH+0, 0
	MOVWF      PCLATH+0
	SWAPF      ___saveSTATUS+0, 0
	MOVWF      STATUS+0
	SWAPF      R15+0, 1
	SWAPF      R15+0, 0
	RETFIE
; end of _interrupt

_interrupt_low:

;Pic16.c,38 :: 		void interrupt_low(){
;Pic16.c,39 :: 		myISR();
	CALL       _myISR+0
;Pic16.c,40 :: 		}
L_end_interrupt_low:
	RETURN
; end of _interrupt_low

_main:

;Pic16.c,41 :: 		void main() {
;Pic16.c,42 :: 		ANSEL  = 0x04;              // Configure AN2 pin as analog
	MOVLW      4
	MOVWF      ANSEL+0
;Pic16.c,43 :: 		ANSELH = 0;                 // Configure other AN pins as digital I/O
	CLRF       ANSELH+0
;Pic16.c,44 :: 		C1ON_bit = 0;               // Disable comparators
	BCF        C1ON_bit+0, BitPos(C1ON_bit+0)
;Pic16.c,45 :: 		C2ON_bit = 0;
	BCF        C2ON_bit+0, BitPos(C2ON_bit+0)
;Pic16.c,46 :: 		TRISA.B2  = 1;              // analog pin is input
	BSF        TRISA+0, 2
;Pic16.c,47 :: 		TRISB  = 0;                 // PORTD is output
	CLRF       TRISB+0
;Pic16.c,48 :: 		PORTB =0;
	CLRF       PORTB+0
;Pic16.c,49 :: 		option_reg.F7=1; //enable pull up resistors bit
	BSF        OPTION_REG+0, 7
;Pic16.c,50 :: 		WPUB=0xFF; //enable pull up resistors pins
	MOVLW      255
	MOVWF      WPUB+0
;Pic16.c,52 :: 		Lcd_Init();                        // Initialize LCD
	CALL       _Lcd_Init+0
;Pic16.c,54 :: 		Lcd_Cmd(_LCD_CLEAR);               // Clear display
	MOVLW      1
	MOVWF      FARG_Lcd_Cmd_out_char+0
	CALL       _Lcd_Cmd+0
;Pic16.c,55 :: 		Lcd_Cmd(_LCD_UNDERLINE_ON);          // Cursor off
	MOVLW      14
	MOVWF      FARG_Lcd_Cmd_out_char+0
	CALL       _Lcd_Cmd+0
;Pic16.c,56 :: 		delay_ms(10);
	MOVLW      65
	MOVWF      R12+0
	MOVLW      238
	MOVWF      R13+0
L_main0:
	DECFSZ     R13+0, 1
	GOTO       L_main0
	DECFSZ     R12+0, 1
	GOTO       L_main0
	NOP
;Pic16.c,57 :: 		session=0;
	CLRF       _session+0
;Pic16.c,58 :: 		configureSPI();
	CALL       _configureSPI+0
;Pic16.c,59 :: 		uart1_init(9600);
	MOVLW      129
	MOVWF      SPBRG+0
	BSF        TXSTA+0, 2
	CALL       _UART1_Init+0
;Pic16.c,60 :: 		delay_ms(100);
	MOVLW      3
	MOVWF      R11+0
	MOVLW      138
	MOVWF      R12+0
	MOVLW      85
	MOVWF      R13+0
L_main1:
	DECFSZ     R13+0, 1
	GOTO       L_main1
	DECFSZ     R12+0, 1
	GOTO       L_main1
	DECFSZ     R11+0, 1
	GOTO       L_main1
	NOP
	NOP
;Pic16.c,61 :: 		do {
L_main2:
;Pic16.c,62 :: 		Lcd_Out(2,1,txt);                 // Write text in first row
	MOVLW      2
	MOVWF      FARG_Lcd_Out_row+0
	MOVLW      1
	MOVWF      FARG_Lcd_Out_column+0
	MOVLW      Pic16_txt+0
	MOVWF      FARG_Lcd_Out_text+0
	CALL       _Lcd_Out+0
;Pic16.c,63 :: 		delay_ms(100);
	MOVLW      3
	MOVWF      R11+0
	MOVLW      138
	MOVWF      R12+0
	MOVLW      85
	MOVWF      R13+0
L_main5:
	DECFSZ     R13+0, 1
	GOTO       L_main5
	DECFSZ     R12+0, 1
	GOTO       L_main5
	DECFSZ     R11+0, 1
	GOTO       L_main5
	NOP
	NOP
;Pic16.c,64 :: 		} while(1);
	GOTO       L_main2
;Pic16.c,66 :: 		}
L_end_main:
	GOTO       $+0
; end of _main

_configureSPI:

;Pic16.c,68 :: 		void configureSPI(){
;Pic16.c,69 :: 		intcon=0;
	CLRF       INTCON+0
;Pic16.c,70 :: 		pie1.rcie=1;
	BSF        PIE1+0, 5
;Pic16.c,71 :: 		intcon.peie=1;
	BSF        INTCON+0, 6
;Pic16.c,72 :: 		intcon.gie=1;
	BSF        INTCON+0, 7
;Pic16.c,73 :: 		return;
;Pic16.c,74 :: 		}
L_end_configureSPI:
	RETURN
; end of _configureSPI

_myISR:

;Pic16.c,76 :: 		void myISR(){
;Pic16.c,77 :: 		if (PIR1.RCIF){
	BTFSS      PIR1+0, 5
	GOTO       L_myISR6
;Pic16.c,78 :: 		pir1.rcif=0;
	BCF        PIR1+0, 5
;Pic16.c,79 :: 		uart1_read_text(txt,'x',1);
	MOVLW      Pic16_txt+0
	MOVWF      FARG_UART1_Read_Text_Output+0
	MOVLW      120
	MOVWF      FARG_UART1_Read_Text_Delimiter+0
	MOVLW      1
	MOVWF      FARG_UART1_Read_Text_Attempts+0
	CALL       _UART1_Read_Text+0
;Pic16.c,80 :: 		uart1_write_text("pic16fx");
	MOVLW      ?lstr1_Pic16+0
	MOVWF      FARG_UART1_Write_Text_uart_text+0
	CALL       _UART1_Write_Text+0
;Pic16.c,81 :: 		return;
	GOTO       L_end_myISR
;Pic16.c,82 :: 		}
L_myISR6:
;Pic16.c,83 :: 		}
L_end_myISR:
	RETURN
; end of _myISR
