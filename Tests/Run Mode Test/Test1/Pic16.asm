
_interrupt:
	MOVWF      R15+0
	SWAPF      STATUS+0, 0
	CLRF       STATUS+0
	MOVWF      ___saveSTATUS+0
	MOVF       PCLATH+0, 0
	MOVWF      ___savePCLATH+0
	CLRF       PCLATH+0

;Pic16.c,35 :: 		void interrupt(){
;Pic16.c,36 :: 		if(pir1.rcif){ //Uart receive interrupt
	BTFSS      PIR1+0, 5
	GOTO       L_interrupt0
;Pic16.c,37 :: 		rxByte = rcreg;   //Clear interrupt by reading receive register
	MOVF       RCREG+0, 0
	MOVWF      _rxByte+0
;Pic16.c,38 :: 		++session;
	INCF       _session+0, 1
;Pic16.c,39 :: 		if(session==1){
	MOVF       _session+0, 0
	XORLW      1
	BTFSS      STATUS+0, 2
	GOTO       L_interrupt1
;Pic16.c,40 :: 		txByte= portb;
	MOVF       PORTB+0, 0
	MOVWF      _txByte+0
;Pic16.c,41 :: 		uart1_write(txByte);
	MOVF       _txByte+0, 0
	MOVWF      FARG_UART1_Write_data_+0
	CALL       _UART1_Write+0
;Pic16.c,42 :: 		return;
	GOTO       L__interrupt22
;Pic16.c,43 :: 		}
L_interrupt1:
;Pic16.c,44 :: 		else if(session==2){
	MOVF       _session+0, 0
	XORLW      2
	BTFSS      STATUS+0, 2
	GOTO       L_interrupt3
;Pic16.c,45 :: 		txByte= Lo(anIn);
	MOVF       _anIn+0, 0
	MOVWF      _txByte+0
;Pic16.c,46 :: 		uart1_write(txByte);
	MOVF       _anIn+0, 0
	MOVWF      FARG_UART1_Write_data_+0
	CALL       _UART1_Write+0
;Pic16.c,47 :: 		return;
	GOTO       L__interrupt22
;Pic16.c,48 :: 		}
L_interrupt3:
;Pic16.c,49 :: 		else if(session==3){
	MOVF       _session+0, 0
	XORLW      3
	BTFSS      STATUS+0, 2
	GOTO       L_interrupt5
;Pic16.c,50 :: 		txByte=Hi(anIn);
	MOVF       _anIn+1, 0
	MOVWF      _txByte+0
;Pic16.c,51 :: 		uart1_write(txByte);
	MOVF       _anIn+1, 0
	MOVWF      FARG_UART1_Write_data_+0
	CALL       _UART1_Write+0
;Pic16.c,52 :: 		return;
	GOTO       L__interrupt22
;Pic16.c,53 :: 		}
L_interrupt5:
;Pic16.c,54 :: 		else if(session==4){
	MOVF       _session+0, 0
	XORLW      4
	BTFSS      STATUS+0, 2
	GOTO       L_interrupt7
;Pic16.c,55 :: 		dio=rxByte;
	MOVF       _rxByte+0, 0
	MOVWF      PORTB+0
;Pic16.c,56 :: 		return;
	GOTO       L__interrupt22
;Pic16.c,57 :: 		}
L_interrupt7:
;Pic16.c,58 :: 		else if(session==5){
	MOVF       _session+0, 0
	XORLW      5
	BTFSS      STATUS+0, 2
	GOTO       L_interrupt9
;Pic16.c,59 :: 		Lo(anOut)=rxByte;
	MOVF       _rxByte+0, 0
	MOVWF      _anOut+0
;Pic16.c,60 :: 		return;
	GOTO       L__interrupt22
;Pic16.c,61 :: 		}
L_interrupt9:
;Pic16.c,62 :: 		else if(session==6){
	MOVF       _session+0, 0
	XORLW      6
	BTFSS      STATUS+0, 2
	GOTO       L_interrupt11
;Pic16.c,63 :: 		Hi(anOut)=rxByte;
	MOVF       _rxByte+0, 0
	MOVWF      _anOut+1
;Pic16.c,64 :: 		session=0;
	CLRF       _session+0
;Pic16.c,65 :: 		return;
	GOTO       L__interrupt22
;Pic16.c,66 :: 		}
L_interrupt11:
;Pic16.c,67 :: 		return;
	GOTO       L__interrupt22
;Pic16.c,68 :: 		}
L_interrupt0:
;Pic16.c,69 :: 		}
L_end_interrupt:
L__interrupt22:
	MOVF       ___savePCLATH+0, 0
	MOVWF      PCLATH+0
	SWAPF      ___saveSTATUS+0, 0
	MOVWF      STATUS+0
	SWAPF      R15+0, 1
	SWAPF      R15+0, 0
	RETFIE
; end of _interrupt

_main:

;Pic16.c,72 :: 		void main() {
;Pic16.c,74 :: 		CCP1CON = 0;   // Disable comparators
	CLRF       CCP1CON+0
;Pic16.c,76 :: 		ANSEL  = 0xFF;   // Configure AN0- AN7 pins as analog I/O
	MOVLW      255
	MOVWF      ANSEL+0
;Pic16.c,77 :: 		ANSELH = 0;      //configure AN8-AN13 as digital
	CLRF       ANSELH+0
;Pic16.c,78 :: 		C1ON_bit = 0;    // Disable comparators
	BCF        C1ON_bit+0, BitPos(C1ON_bit+0)
;Pic16.c,79 :: 		C2ON_bit = 0;
	BCF        C2ON_bit+0, BitPos(C2ON_bit+0)
;Pic16.c,80 :: 		TRISA.B0  = 1;  // analog pin is input
	BSF        TRISA+0, 0
;Pic16.c,81 :: 		mode_dir =1;    //plc mode pin is input
	BSF        TRISA4_bit+0, BitPos(TRISA4_bit+0)
;Pic16.c,82 :: 		TRISD  = 0;     // lcd port is output
	CLRF       TRISD+0
;Pic16.c,83 :: 		trisb =0x0F;    //portb{0..3}=inputs and portb{4..7} are outputs
	MOVLW      15
	MOVWF      TRISB+0
;Pic16.c,84 :: 		mcp_cs_dir=0;  //MCP Chip Select pin is output
	BCF        TRISA1_bit+0, BitPos(TRISA1_bit+0)
;Pic16.c,85 :: 		mcp_cs=1;      //Disable MCP DAC
	BSF        RA1_bit+0, BitPos(RA1_bit+0)
;Pic16.c,87 :: 		intcon.gie=1;   //globally enable all interrupts
	BSF        INTCON+0, 7
;Pic16.c,88 :: 		intcon.peie=1;  //Enables all unmasked peripheral interrupts
	BSF        INTCON+0, 6
;Pic16.c,89 :: 		pie1.rcie=1;     //Enables the EUSART receive interrupt
	BSF        PIE1+0, 5
;Pic16.c,90 :: 		UART1_Init(9600); //init uart at 9600 bits per second
	MOVLW      103
	MOVWF      SPBRG+0
	BSF        TXSTA+0, 2
	CALL       _UART1_Init+0
;Pic16.c,91 :: 		delay_ms(100);    //wait for uart initialization
	MOVLW      3
	MOVWF      R11+0
	MOVLW      8
	MOVWF      R12+0
	MOVLW      119
	MOVWF      R13+0
L_main12:
	DECFSZ     R13+0, 1
	GOTO       L_main12
	DECFSZ     R12+0, 1
	GOTO       L_main12
	DECFSZ     R11+0, 1
	GOTO       L_main12
;Pic16.c,93 :: 		Lcd_Init();      // Initialize LCD
	CALL       _Lcd_Init+0
;Pic16.c,94 :: 		Lcd_Cmd(_LCD_CLEAR);   // Clear display
	MOVLW      1
	MOVWF      FARG_Lcd_Cmd_out_char+0
	CALL       _Lcd_Cmd+0
;Pic16.c,95 :: 		Lcd_Cmd(_LCD_CURSOR_OFF);  // Cursor off
	MOVLW      12
	MOVWF      FARG_Lcd_Cmd_out_char+0
	CALL       _Lcd_Cmd+0
;Pic16.c,96 :: 		Lcd_Out(1, 1, "ADC Value:"); // Write text in first row
	MOVLW      1
	MOVWF      FARG_Lcd_Out_row+0
	MOVLW      1
	MOVWF      FARG_Lcd_Out_column+0
	MOVLW      ?lstr1_Pic16+0
	MOVWF      FARG_Lcd_Out_text+0
	CALL       _Lcd_Out+0
;Pic16.c,97 :: 		Lcd_Out(2, 1, "DAC Value:"); // Write text in second row
	MOVLW      2
	MOVWF      FARG_Lcd_Out_row+0
	MOVLW      1
	MOVWF      FARG_Lcd_Out_column+0
	MOVLW      ?lstr2_Pic16+0
	MOVWF      FARG_Lcd_Out_text+0
	CALL       _Lcd_Out+0
;Pic16.c,98 :: 		delay_ms(10);
	MOVLW      52
	MOVWF      R12+0
	MOVLW      241
	MOVWF      R13+0
L_main13:
	DECFSZ     R13+0, 1
	GOTO       L_main13
	DECFSZ     R12+0, 1
	GOTO       L_main13
	NOP
	NOP
;Pic16.c,99 :: 		adc_init();      //initialize ADC
	CALL       _ADC_Init+0
;Pic16.c,100 :: 		delay_ms(10);
	MOVLW      52
	MOVWF      R12+0
	MOVLW      241
	MOVWF      R13+0
L_main14:
	DECFSZ     R13+0, 1
	GOTO       L_main14
	DECFSZ     R12+0, 1
	GOTO       L_main14
	NOP
	NOP
;Pic16.c,102 :: 		SPI1_Init();
	CALL       _SPI1_Init+0
;Pic16.c,103 :: 		delay_ms(100);
	MOVLW      3
	MOVWF      R11+0
	MOVLW      8
	MOVWF      R12+0
	MOVLW      119
	MOVWF      R13+0
L_main15:
	DECFSZ     R13+0, 1
	GOTO       L_main15
	DECFSZ     R12+0, 1
	GOTO       L_main15
	DECFSZ     R11+0, 1
	GOTO       L_main15
;Pic16.c,105 :: 		session=0;
	CLRF       _session+0
;Pic16.c,106 :: 		anOut=0;
	CLRF       _anOut+0
	CLRF       _anOut+1
;Pic16.c,108 :: 		do {
L_main16:
;Pic16.c,109 :: 		if(mode){ //Only if we are in run mode
	BTFSS      RA4_bit+0, BitPos(RA4_bit+0)
	GOTO       L_main19
;Pic16.c,110 :: 		anIn = ADC_Read(0); // Get 10-bit result of AD conversion
	CLRF       FARG_ADC_Read_channel+0
	CALL       _ADC_Read+0
	MOVF       R0+0, 0
	MOVWF      _anIn+0
	MOVF       R0+1, 0
	MOVWF      _anIn+1
;Pic16.c,111 :: 		mcp_cs = 0; //Select MCP4921 DAC
	BCF        RA1_bit+0, BitPos(RA1_bit+0)
;Pic16.c,112 :: 		temp = Hi(anOut)&0xF; // Store valueDAC[11..8] to temp[3..0]
	MOVLW      15
	ANDWF      _anOut+1, 0
	MOVWF      R0+0
	MOVF       R0+0, 0
	MOVWF      _temp+0
;Pic16.c,113 :: 		temp |= 0x30; // Define DAC setting, see MCP4921 datasheet
	MOVLW      48
	IORWF      R0+0, 1
	MOVF       R0+0, 0
	MOVWF      _temp+0
;Pic16.c,114 :: 		SPI1_Write(temp); // Send high byte via SPI_Soft
	MOVF       R0+0, 0
	MOVWF      FARG_SPI1_Write_data_+0
	CALL       _SPI1_Write+0
;Pic16.c,116 :: 		temp = Lo(anOut);
	MOVF       _anOut+0, 0
	MOVWF      _temp+0
;Pic16.c,117 :: 		SPI1_Write(temp); // Send low byte via SPI
	MOVF       _anOut+0, 0
	MOVWF      FARG_SPI1_Write_data_+0
	CALL       _SPI1_Write+0
;Pic16.c,118 :: 		mcp_cs = 1; //Disable DAC
	BSF        RA1_bit+0, BitPos(RA1_bit+0)
;Pic16.c,120 :: 		IntToStr(anIn, line1);
	MOVF       _anIn+0, 0
	MOVWF      FARG_IntToStr_input+0
	MOVF       _anIn+1, 0
	MOVWF      FARG_IntToStr_input+1
	MOVLW      Pic16_line1+0
	MOVWF      FARG_IntToStr_output+0
	CALL       _IntToStr+0
;Pic16.c,121 :: 		Lcd_Out(1, 11, line1); // Display analogue input in row1
	MOVLW      1
	MOVWF      FARG_Lcd_Out_row+0
	MOVLW      11
	MOVWF      FARG_Lcd_Out_column+0
	MOVLW      Pic16_line1+0
	MOVWF      FARG_Lcd_Out_text+0
	CALL       _Lcd_Out+0
;Pic16.c,122 :: 		IntToStr(anOut, line2);
	MOVF       _anOut+0, 0
	MOVWF      FARG_IntToStr_input+0
	MOVF       _anOut+1, 0
	MOVWF      FARG_IntToStr_input+1
	MOVLW      Pic16_line2+0
	MOVWF      FARG_IntToStr_output+0
	CALL       _IntToStr+0
;Pic16.c,123 :: 		Lcd_Out(2, 11, line2); // Display analogue output in row2
	MOVLW      2
	MOVWF      FARG_Lcd_Out_row+0
	MOVLW      11
	MOVWF      FARG_Lcd_Out_column+0
	MOVLW      Pic16_line2+0
	MOVWF      FARG_Lcd_Out_text+0
	CALL       _Lcd_Out+0
;Pic16.c,124 :: 		delay_ms(100);
	MOVLW      3
	MOVWF      R11+0
	MOVLW      8
	MOVWF      R12+0
	MOVLW      119
	MOVWF      R13+0
L_main20:
	DECFSZ     R13+0, 1
	GOTO       L_main20
	DECFSZ     R12+0, 1
	GOTO       L_main20
	DECFSZ     R11+0, 1
	GOTO       L_main20
;Pic16.c,125 :: 		}
L_main19:
;Pic16.c,126 :: 		} while(1);
	GOTO       L_main16
;Pic16.c,128 :: 		}
L_end_main:
	GOTO       $+0
; end of _main
