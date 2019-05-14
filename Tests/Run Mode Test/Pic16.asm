
_interrupt:
	MOVWF      R15+0
	SWAPF      STATUS+0, 0
	CLRF       STATUS+0
	MOVWF      ___saveSTATUS+0
	MOVF       PCLATH+0, 0
	MOVWF      ___savePCLATH+0
	CLRF       PCLATH+0

;Pic16.c,23 :: 		void interrupt(){
;Pic16.c,24 :: 		if(pir1.rcif){ //Uart receive interrupt
	BTFSS      PIR1+0, 5
	GOTO       L_interrupt0
;Pic16.c,25 :: 		rxByte = rcreg;   //Clear interrupt by reading receive register
	MOVF       RCREG+0, 0
	MOVWF      _rxByte+0
;Pic16.c,26 :: 		++session;
	INCF       _session+0, 1
;Pic16.c,29 :: 		if(session==1){//next session if reading of analog inputs
	MOVF       _session+0, 0
	XORLW      1
	BTFSS      STATUS+0, 2
	GOTO       L_interrupt1
;Pic16.c,30 :: 		txByte= Lo(anIn0);
	MOVF       _anIn0+0, 0
	MOVWF      _txByte+0
;Pic16.c,31 :: 		uart1_write(txByte);
	MOVF       _txByte+0, 0
	MOVWF      FARG_UART1_Write_data_+0
	CALL       _UART1_Write+0
;Pic16.c,32 :: 		return;
	GOTO       L__interrupt49
;Pic16.c,33 :: 		}
L_interrupt1:
;Pic16.c,34 :: 		else if(session==2){
	MOVF       _session+0, 0
	XORLW      2
	BTFSS      STATUS+0, 2
	GOTO       L_interrupt3
;Pic16.c,35 :: 		txByte=Hi(anIn0);
	MOVF       _anIn0+1, 0
	MOVWF      _txByte+0
;Pic16.c,36 :: 		uart1_write(txByte);
	MOVF       _txByte+0, 0
	MOVWF      FARG_UART1_Write_data_+0
	CALL       _UART1_Write+0
;Pic16.c,37 :: 		return;
	GOTO       L__interrupt49
;Pic16.c,38 :: 		}
L_interrupt3:
;Pic16.c,39 :: 		else if(session==3){//next session if reading of analog inputs
	MOVF       _session+0, 0
	XORLW      3
	BTFSS      STATUS+0, 2
	GOTO       L_interrupt5
;Pic16.c,40 :: 		txByte= Lo(anIn1);
	MOVF       _anIn1+0, 0
	MOVWF      _txByte+0
;Pic16.c,41 :: 		uart1_write(txByte);
	MOVF       _txByte+0, 0
	MOVWF      FARG_UART1_Write_data_+0
	CALL       _UART1_Write+0
;Pic16.c,42 :: 		return;
	GOTO       L__interrupt49
;Pic16.c,43 :: 		}
L_interrupt5:
;Pic16.c,44 :: 		else if(session==4){
	MOVF       _session+0, 0
	XORLW      4
	BTFSS      STATUS+0, 2
	GOTO       L_interrupt7
;Pic16.c,45 :: 		txByte=Hi(anIn1);
	MOVF       _anIn1+1, 0
	MOVWF      _txByte+0
;Pic16.c,46 :: 		uart1_write(txByte);
	MOVF       _txByte+0, 0
	MOVWF      FARG_UART1_Write_data_+0
	CALL       _UART1_Write+0
;Pic16.c,47 :: 		return;
	GOTO       L__interrupt49
;Pic16.c,48 :: 		}
L_interrupt7:
;Pic16.c,49 :: 		else if(session==5){//next session if reading of analog inputs
	MOVF       _session+0, 0
	XORLW      5
	BTFSS      STATUS+0, 2
	GOTO       L_interrupt9
;Pic16.c,50 :: 		txByte= Lo(anIn2);
	MOVF       _anIn2+0, 0
	MOVWF      _txByte+0
;Pic16.c,51 :: 		uart1_write(txByte);
	MOVF       _txByte+0, 0
	MOVWF      FARG_UART1_Write_data_+0
	CALL       _UART1_Write+0
;Pic16.c,52 :: 		return;
	GOTO       L__interrupt49
;Pic16.c,53 :: 		}
L_interrupt9:
;Pic16.c,54 :: 		else if(session==6){
	MOVF       _session+0, 0
	XORLW      6
	BTFSS      STATUS+0, 2
	GOTO       L_interrupt11
;Pic16.c,55 :: 		txByte=Hi(anIn2);
	MOVF       _anIn2+1, 0
	MOVWF      _txByte+0
;Pic16.c,56 :: 		uart1_write(txByte);
	MOVF       _txByte+0, 0
	MOVWF      FARG_UART1_Write_data_+0
	CALL       _UART1_Write+0
;Pic16.c,57 :: 		return;
	GOTO       L__interrupt49
;Pic16.c,58 :: 		}
L_interrupt11:
;Pic16.c,59 :: 		else if(session==7){//next session if reading of analog inputs
	MOVF       _session+0, 0
	XORLW      7
	BTFSS      STATUS+0, 2
	GOTO       L_interrupt13
;Pic16.c,60 :: 		txByte= Lo(anIn3);
	MOVF       _anIn3+0, 0
	MOVWF      _txByte+0
;Pic16.c,61 :: 		uart1_write(txByte);
	MOVF       _txByte+0, 0
	MOVWF      FARG_UART1_Write_data_+0
	CALL       _UART1_Write+0
;Pic16.c,62 :: 		return;
	GOTO       L__interrupt49
;Pic16.c,63 :: 		}
L_interrupt13:
;Pic16.c,64 :: 		else if(session==8){
	MOVF       _session+0, 0
	XORLW      8
	BTFSS      STATUS+0, 2
	GOTO       L_interrupt15
;Pic16.c,65 :: 		txByte=Hi(anIn3);
	MOVF       _anIn3+1, 0
	MOVWF      _txByte+0
;Pic16.c,66 :: 		uart1_write(txByte);
	MOVF       _txByte+0, 0
	MOVWF      FARG_UART1_Write_data_+0
	CALL       _UART1_Write+0
;Pic16.c,67 :: 		return;
	GOTO       L__interrupt49
;Pic16.c,68 :: 		}
L_interrupt15:
;Pic16.c,69 :: 		else if(session==9){//next session if reading of analog inputs
	MOVF       _session+0, 0
	XORLW      9
	BTFSS      STATUS+0, 2
	GOTO       L_interrupt17
;Pic16.c,70 :: 		txByte= Lo(anIn4);
	MOVF       _anIn4+0, 0
	MOVWF      _txByte+0
;Pic16.c,71 :: 		uart1_write(txByte);
	MOVF       _txByte+0, 0
	MOVWF      FARG_UART1_Write_data_+0
	CALL       _UART1_Write+0
;Pic16.c,72 :: 		return;
	GOTO       L__interrupt49
;Pic16.c,73 :: 		}
L_interrupt17:
;Pic16.c,74 :: 		else if(session==10){
	MOVF       _session+0, 0
	XORLW      10
	BTFSS      STATUS+0, 2
	GOTO       L_interrupt19
;Pic16.c,75 :: 		txByte=Hi(anIn4);
	MOVF       _anIn4+1, 0
	MOVWF      _txByte+0
;Pic16.c,76 :: 		uart1_write(txByte);
	MOVF       _txByte+0, 0
	MOVWF      FARG_UART1_Write_data_+0
	CALL       _UART1_Write+0
;Pic16.c,77 :: 		return;
	GOTO       L__interrupt49
;Pic16.c,78 :: 		}
L_interrupt19:
;Pic16.c,79 :: 		else if(session==11){//next session if reading of analog inputs
	MOVF       _session+0, 0
	XORLW      11
	BTFSS      STATUS+0, 2
	GOTO       L_interrupt21
;Pic16.c,80 :: 		txByte= Lo(anIn5);
	MOVF       _anIn5+0, 0
	MOVWF      _txByte+0
;Pic16.c,81 :: 		uart1_write(txByte);
	MOVF       _txByte+0, 0
	MOVWF      FARG_UART1_Write_data_+0
	CALL       _UART1_Write+0
;Pic16.c,82 :: 		return;
	GOTO       L__interrupt49
;Pic16.c,83 :: 		}
L_interrupt21:
;Pic16.c,84 :: 		else if(session==12){
	MOVF       _session+0, 0
	XORLW      12
	BTFSS      STATUS+0, 2
	GOTO       L_interrupt23
;Pic16.c,85 :: 		txByte=Hi(anIn5);
	MOVF       _anIn5+1, 0
	MOVWF      _txByte+0
;Pic16.c,86 :: 		uart1_write(txByte);
	MOVF       _txByte+0, 0
	MOVWF      FARG_UART1_Write_data_+0
	CALL       _UART1_Write+0
;Pic16.c,87 :: 		return;
	GOTO       L__interrupt49
;Pic16.c,88 :: 		}
L_interrupt23:
;Pic16.c,89 :: 		else if(session==13){//next session if reading of analog inputs
	MOVF       _session+0, 0
	XORLW      13
	BTFSS      STATUS+0, 2
	GOTO       L_interrupt25
;Pic16.c,90 :: 		txByte= Lo(anIn6);
	MOVF       _anIn6+0, 0
	MOVWF      _txByte+0
;Pic16.c,91 :: 		uart1_write(txByte);
	MOVF       _txByte+0, 0
	MOVWF      FARG_UART1_Write_data_+0
	CALL       _UART1_Write+0
;Pic16.c,92 :: 		return;
	GOTO       L__interrupt49
;Pic16.c,93 :: 		}
L_interrupt25:
;Pic16.c,94 :: 		else if(session==14){
	MOVF       _session+0, 0
	XORLW      14
	BTFSS      STATUS+0, 2
	GOTO       L_interrupt27
;Pic16.c,95 :: 		txByte=Hi(anIn6);
	MOVF       _anIn6+1, 0
	MOVWF      _txByte+0
;Pic16.c,96 :: 		uart1_write(txByte);
	MOVF       _txByte+0, 0
	MOVWF      FARG_UART1_Write_data_+0
	CALL       _UART1_Write+0
;Pic16.c,97 :: 		return;
	GOTO       L__interrupt49
;Pic16.c,98 :: 		}
L_interrupt27:
;Pic16.c,99 :: 		else if(session==15){//next session if reading of analog inputs
	MOVF       _session+0, 0
	XORLW      15
	BTFSS      STATUS+0, 2
	GOTO       L_interrupt29
;Pic16.c,100 :: 		txByte= Lo(anIn7);
	MOVF       _anIn7+0, 0
	MOVWF      _txByte+0
;Pic16.c,101 :: 		uart1_write(txByte);
	MOVF       _txByte+0, 0
	MOVWF      FARG_UART1_Write_data_+0
	CALL       _UART1_Write+0
;Pic16.c,102 :: 		return;
	GOTO       L__interrupt49
;Pic16.c,103 :: 		}
L_interrupt29:
;Pic16.c,104 :: 		else if(session==16){
	MOVF       _session+0, 0
	XORLW      16
	BTFSS      STATUS+0, 2
	GOTO       L_interrupt31
;Pic16.c,105 :: 		txByte=Hi(anIn7);
	MOVF       _anIn7+1, 0
	MOVWF      _txByte+0
;Pic16.c,106 :: 		uart1_write(txByte);
	MOVF       _txByte+0, 0
	MOVWF      FARG_UART1_Write_data_+0
	CALL       _UART1_Write+0
;Pic16.c,107 :: 		return;
	GOTO       L__interrupt49
;Pic16.c,108 :: 		}
L_interrupt31:
;Pic16.c,109 :: 		else if(session==17){  //Digital inputs are read first
	MOVF       _session+0, 0
	XORLW      17
	BTFSS      STATUS+0, 2
	GOTO       L_interrupt33
;Pic16.c,110 :: 		txByte= portd;
	MOVF       PORTD+0, 0
	MOVWF      _txByte+0
;Pic16.c,111 :: 		uart1_write(txByte);
	MOVF       _txByte+0, 0
	MOVWF      FARG_UART1_Write_data_+0
	CALL       _UART1_Write+0
;Pic16.c,112 :: 		return;
	GOTO       L__interrupt49
;Pic16.c,113 :: 		}
L_interrupt33:
;Pic16.c,116 :: 		else if(session==18){
	MOVF       _session+0, 0
	XORLW      18
	BTFSS      STATUS+0, 2
	GOTO       L_interrupt35
;Pic16.c,117 :: 		Lo(anOut)=rxByte;
	MOVF       _rxByte+0, 0
	MOVWF      _anOut+0
;Pic16.c,118 :: 		return;
	GOTO       L__interrupt49
;Pic16.c,119 :: 		}
L_interrupt35:
;Pic16.c,120 :: 		else if(session==19){
	MOVF       _session+0, 0
	XORLW      19
	BTFSS      STATUS+0, 2
	GOTO       L_interrupt37
;Pic16.c,121 :: 		Hi(anOut)=rxByte;
	MOVF       _rxByte+0, 0
	MOVWF      _anOut+1
;Pic16.c,123 :: 		return;
	GOTO       L__interrupt49
;Pic16.c,124 :: 		}
L_interrupt37:
;Pic16.c,125 :: 		else if(session==20){ //first data to be written is digital output
	MOVF       _session+0, 0
	XORLW      20
	BTFSS      STATUS+0, 2
	GOTO       L_interrupt39
;Pic16.c,126 :: 		portb=rxByte;
	MOVF       _rxByte+0, 0
	MOVWF      PORTB+0
;Pic16.c,127 :: 		session=0;
	CLRF       _session+0
;Pic16.c,128 :: 		return;
	GOTO       L__interrupt49
;Pic16.c,129 :: 		}
L_interrupt39:
;Pic16.c,130 :: 		return;
	GOTO       L__interrupt49
;Pic16.c,131 :: 		}
L_interrupt0:
;Pic16.c,132 :: 		}
L_end_interrupt:
L__interrupt49:
	MOVF       ___savePCLATH+0, 0
	MOVWF      PCLATH+0
	SWAPF      ___saveSTATUS+0, 0
	MOVWF      STATUS+0
	SWAPF      R15+0, 1
	SWAPF      R15+0, 0
	RETFIE
; end of _interrupt

_main:

;Pic16.c,135 :: 		void main() {
;Pic16.c,137 :: 		CCP1CON = 0;   // Disable comparators
	CLRF       CCP1CON+0
;Pic16.c,139 :: 		ANSEL  = 0xFF;   // Configure AN0- AN7 pins as analog I/O
	MOVLW      255
	MOVWF      ANSEL+0
;Pic16.c,140 :: 		ANSELH = 0;      //configure AN8-AN13 as digital
	CLRF       ANSELH+0
;Pic16.c,141 :: 		C1ON_bit = 0;    // Disable comparators
	BCF        C1ON_bit+0, BitPos(C1ON_bit+0)
;Pic16.c,142 :: 		C2ON_bit = 0;
	BCF        C2ON_bit+0, BitPos(C2ON_bit+0)
;Pic16.c,143 :: 		option_reg.F7=1; //enable pull up resistors bit
	BSF        OPTION_REG+0, 7
;Pic16.c,144 :: 		WPUB=0xFF; //enable pull up resistors pins
	MOVLW      255
	MOVWF      WPUB+0
;Pic16.c,145 :: 		TRISA  = 0x2F;  // analog pins as inputs
	MOVLW      47
	MOVWF      TRISA+0
;Pic16.c,146 :: 		mode_dir =1;    //plc mode pin is input
	BSF        TRISC1_bit+0, BitPos(TRISC1_bit+0)
;Pic16.c,147 :: 		PORTD =0;
	CLRF       PORTD+0
;Pic16.c,148 :: 		TRISD  = 0xFF;   //Digital Inputs port as inputs
	MOVLW      255
	MOVWF      TRISD+0
;Pic16.c,149 :: 		portb = 0;
	CLRF       PORTB+0
;Pic16.c,150 :: 		trisb =0x00;    //Digital outputs port as outputs
	CLRF       TRISB+0
;Pic16.c,151 :: 		mcp_cs_dir=0;  //MCP Chip Select pin is output
	BCF        TRISC2_bit+0, BitPos(TRISC2_bit+0)
;Pic16.c,152 :: 		mcp_cs=1;      //Disable MCP DAC
	BSF        RC2_bit+0, BitPos(RC2_bit+0)
;Pic16.c,155 :: 		intcon.gie=1;   //globally enable all interrupts
	BSF        INTCON+0, 7
;Pic16.c,156 :: 		intcon.peie=1;  //Enables all unmasked peripheral interrupts
	BSF        INTCON+0, 6
;Pic16.c,157 :: 		pie1.rcie=1;     //Enables the EUSART receive interrupt
	BSF        PIE1+0, 5
;Pic16.c,158 :: 		UART1_Init(9600); //init uart at 9600 bits per second
	MOVLW      103
	MOVWF      SPBRG+0
	BSF        TXSTA+0, 2
	CALL       _UART1_Init+0
;Pic16.c,159 :: 		delay_ms(100);    //wait for uart initialization
	MOVLW      3
	MOVWF      R11+0
	MOVLW      8
	MOVWF      R12+0
	MOVLW      119
	MOVWF      R13+0
L_main40:
	DECFSZ     R13+0, 1
	GOTO       L_main40
	DECFSZ     R12+0, 1
	GOTO       L_main40
	DECFSZ     R11+0, 1
	GOTO       L_main40
;Pic16.c,161 :: 		adc_init();      //initialize ADC
	CALL       _ADC_Init+0
;Pic16.c,162 :: 		delay_ms(10);
	MOVLW      52
	MOVWF      R12+0
	MOVLW      241
	MOVWF      R13+0
L_main41:
	DECFSZ     R13+0, 1
	GOTO       L_main41
	DECFSZ     R12+0, 1
	GOTO       L_main41
	NOP
	NOP
;Pic16.c,164 :: 		SPI1_Init();
	CALL       _SPI1_Init+0
;Pic16.c,165 :: 		delay_ms(100);
	MOVLW      3
	MOVWF      R11+0
	MOVLW      8
	MOVWF      R12+0
	MOVLW      119
	MOVWF      R13+0
L_main42:
	DECFSZ     R13+0, 1
	GOTO       L_main42
	DECFSZ     R12+0, 1
	GOTO       L_main42
	DECFSZ     R11+0, 1
	GOTO       L_main42
;Pic16.c,167 :: 		session=0;
	CLRF       _session+0
;Pic16.c,168 :: 		anOut=0;
	CLRF       _anOut+0
	CLRF       _anOut+1
;Pic16.c,170 :: 		do {
L_main43:
;Pic16.c,171 :: 		if(mode){ //Only if we are in run mode
	BTFSS      RC1_bit+0, BitPos(RC1_bit+0)
	GOTO       L_main46
;Pic16.c,172 :: 		anIn0 = ADC_Read(0); // Get 10-bit result of AD conversion
	CLRF       FARG_ADC_Read_channel+0
	CALL       _ADC_Read+0
	MOVF       R0+0, 0
	MOVWF      _anIn0+0
	MOVF       R0+1, 0
	MOVWF      _anIn0+1
;Pic16.c,173 :: 		anIn1 = ADC_Read(1); // Get 10-bit result of AD conversion
	MOVLW      1
	MOVWF      FARG_ADC_Read_channel+0
	CALL       _ADC_Read+0
	MOVF       R0+0, 0
	MOVWF      _anIn1+0
	MOVF       R0+1, 0
	MOVWF      _anIn1+1
;Pic16.c,174 :: 		anIn2 = ADC_Read(2); // Get 10-bit result of AD conversion
	MOVLW      2
	MOVWF      FARG_ADC_Read_channel+0
	CALL       _ADC_Read+0
	MOVF       R0+0, 0
	MOVWF      _anIn2+0
	MOVF       R0+1, 0
	MOVWF      _anIn2+1
;Pic16.c,175 :: 		anIn3 = ADC_Read(3); // Get 10-bit result of AD conversion
	MOVLW      3
	MOVWF      FARG_ADC_Read_channel+0
	CALL       _ADC_Read+0
	MOVF       R0+0, 0
	MOVWF      _anIn3+0
	MOVF       R0+1, 0
	MOVWF      _anIn3+1
;Pic16.c,176 :: 		anIn4 = ADC_Read(4); // Get 10-bit result of AD conversion
	MOVLW      4
	MOVWF      FARG_ADC_Read_channel+0
	CALL       _ADC_Read+0
	MOVF       R0+0, 0
	MOVWF      _anIn4+0
	MOVF       R0+1, 0
	MOVWF      _anIn4+1
;Pic16.c,177 :: 		anIn5 = ADC_Read(5); // Get 10-bit result of AD conversion
	MOVLW      5
	MOVWF      FARG_ADC_Read_channel+0
	CALL       _ADC_Read+0
	MOVF       R0+0, 0
	MOVWF      _anIn5+0
	MOVF       R0+1, 0
	MOVWF      _anIn5+1
;Pic16.c,178 :: 		anIn6 = ADC_Read(6); // Get 10-bit result of AD conversion
	MOVLW      6
	MOVWF      FARG_ADC_Read_channel+0
	CALL       _ADC_Read+0
	MOVF       R0+0, 0
	MOVWF      _anIn6+0
	MOVF       R0+1, 0
	MOVWF      _anIn6+1
;Pic16.c,179 :: 		anIn7 = ADC_Read(7); // Get 10-bit result of AD conversion
	MOVLW      7
	MOVWF      FARG_ADC_Read_channel+0
	CALL       _ADC_Read+0
	MOVF       R0+0, 0
	MOVWF      _anIn7+0
	MOVF       R0+1, 0
	MOVWF      _anIn7+1
;Pic16.c,181 :: 		mcp_cs = 0; //Select MCP4921 DAC
	BCF        RC2_bit+0, BitPos(RC2_bit+0)
;Pic16.c,182 :: 		temp = Hi(anOut)&0xF; // Store valueDAC[11..8] to temp[3..0]
	MOVLW      15
	ANDWF      _anOut+1, 0
	MOVWF      _temp+0
;Pic16.c,183 :: 		temp |= 0x30; // Define DAC setting, see MCP4921 datasheet
	MOVLW      48
	IORWF      _temp+0, 1
;Pic16.c,184 :: 		SPI1_Write(temp); // Send high byte via SPI_Soft
	MOVF       _temp+0, 0
	MOVWF      FARG_SPI1_Write_data_+0
	CALL       _SPI1_Write+0
;Pic16.c,186 :: 		temp = Lo(anOut);
	MOVF       _anOut+0, 0
	MOVWF      _temp+0
;Pic16.c,187 :: 		SPI1_Write(temp); // Send low byte via SPI
	MOVF       _temp+0, 0
	MOVWF      FARG_SPI1_Write_data_+0
	CALL       _SPI1_Write+0
;Pic16.c,188 :: 		mcp_cs = 1; //Disable DAC
	BSF        RC2_bit+0, BitPos(RC2_bit+0)
;Pic16.c,189 :: 		delay_ms(100);
	MOVLW      3
	MOVWF      R11+0
	MOVLW      8
	MOVWF      R12+0
	MOVLW      119
	MOVWF      R13+0
L_main47:
	DECFSZ     R13+0, 1
	GOTO       L_main47
	DECFSZ     R12+0, 1
	GOTO       L_main47
	DECFSZ     R11+0, 1
	GOTO       L_main47
;Pic16.c,190 :: 		}
L_main46:
;Pic16.c,191 :: 		} while(1);
	GOTO       L_main43
;Pic16.c,192 :: 		}
L_end_main:
	GOTO       $+0
; end of _main
