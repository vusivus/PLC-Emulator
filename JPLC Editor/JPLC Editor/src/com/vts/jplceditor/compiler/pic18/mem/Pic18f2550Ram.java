/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vts.jplceditor.compiler.pic18.mem;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Vusivus
 */
public class Pic18f2550Ram extends RamMemoryModel {

    public int TOSU = 0xFFF;
    public int INDF21 = 0xFDF;
    public int CCPR1H = 0xFBF;
    public int IPR1 = 0xF9F;
    public int TOSH = 0xFFE;
    public int POSTINC2 = 0xFDE;
    public int CCPR1L = 0xFBE;
    public int PIR1 = 0xF9E;
    public int UEP14 = 0xF7E;
    public int TOSL = 0xFFD;
    public int POSTDEC2 = 0xFDD;
    public int CCP1CON = 0xFBD;
    public int PIE1 = 0xF9D;
    public int UEP13 = 0xF7D;
    public int STKPTR = 0xFFC;
    public int PREINC2 = 0xFDC;
    public int CCPR2H = 0xFBC;
    public int UEP12 = 0xF7C;
    public int PCLATU = 0xFFB;
    public int PLUSW2 = 0xFDB;
    public int CCPR2L = 0xFBB;
    public int OSCTUNE = 0xF9B;
    public int UEP11 = 0xF7B;
    public int PCLATH = 0xFFA;
    public int FSR2H = 0xFDA;
    public int CCP2CON = 0xFBA;
    public int UEP10 = 0xF7A;
    public int PCL = 0xFF9;
    public int FSR2L = 0xFD9;
    public int UEP9 = 0xF79;
    public int TBLPTRU = 0xFF8;
    public int STATUS = 0xFD8;
    public int BAUDCON = 0xFB8;
    public int UEP8 = 0xF78;
    public int TBLPTRH = 0xFF7;
    public int TMR0H = 0xFD7;
    public int ECCP1DEL = 0xFB7;
    public int UEP7 = 0xF77;
    public int TBLPTRL = 0xFF6;
    public int TMR0L = 0xFD6;
    public int ECCP1AS = 0xFB6;
    public int TRISE = 0xF96;
    public int UEP6 = 0xF76;
    public int TABLAT = 0xFF5;
    public int T0CON = 0xFD5;
    public int CVRCON = 0xFB5;
    public int TRISD = 0xF95;
    public int UEP5 = 0xF75;
    public int PRODH = 0xFF4;
    public int CMCON = 0xFB4;
    public int TRISC = 0xF94;
    public int UEP4 = 0xF74;
    public int PRODL = 0xFF3;
    public int OSCCON = 0xFD3;
    public int TMR3H = 0xFB3;
    public int TRISB = 0xF93;
    public int UEP3 = 0xF73;
    public int INTCON = 0xFF2;
    public int HLVDCON = 0xFD2;
    public int TMR3L = 0xFB2;
    public int TRISA = 0xF92;
    public int UEP2 = 0xF72;
    public int INTCON2 = 0xFF1;
    public int WDTCON = 0xFD1;
    public int T3CON = 0xFB1;
    public int UEP1 = 0xF71;
    public int INTCON3 = 0xFF0;
    public int RCON = 0xFD0;
    public int SPBRGH = 0xFB0;
    public int UEP0 = 0xF70;
    public int INDF0 = 0xFEF;
    public int TMR1H = 0xFCF;
    public int SPBRG = 0xFAF;
    public int UCFG = 0xF6F;
    public int POSTINC0 = 0xFEE;
    public int TMR1L = 0xFCE;
    public int RCREG = 0xFAE;
    public int UADDR = 0xF6E;
    public int POSTDEC0 = 0xFED;
    public int T1CON = 0xFCD;
    public int TXREG = 0xFAD;
    public int LATE = 0xF8D;
    public int UCON = 0xF6D;
    public int PREINC0 = 0xFEC;
    public int TMR2 = 0xFCC;
    public int TXSTA = 0xFAC;
    public int LATD = 0xF8C;
    public int USTAT = 0xF6C;
    public int PLUSW = 0xFEB;
    public int PR2 = 0xFCB;
    public int RCSTA = 0xFAB;
    public int LATC = 0xF8B;
    public int UEIE = 0xF6B;
    public int FSR0H = 0xFEA;
    public int T2CON = 0xFCA;
    public int LATB = 0xF8A;
    public int UEIR = 0xF6A;
    public int FSR0L = 0xFE9;
    public int SSPBUF = 0xFC9;
    public int EEADR = 0xFA9;
    public int LATA = 0xF89;
    public int UIE = 0xF69;
    public int WREG = 0xFE8;
    public int SSPADD = 0xFC8;
    public int EEDATA = 0xFA8;
    public int UIR = 0xF68;
    public int INDF1 = 0xFE7;
    public int SSPSTAT = 0xFC7;
    public int EECON2 = 0xFA7;
    public int UFRMH = 0xF67;
    public int POSTINC1 = 0xFE6;
    public int SSPCON1 = 0xFC6;
    public int EECON1 = 0xFA6;
    public int UFRML = 0xF66;
    public int POSTDEC1 = 0xFE5;
    public int SSPCON2 = 0xFC5;
    public int SPPCON = 0xF65;
    public int PREINC1 = 0xFE4;
    public int ADRESH = 0xFC4;
    public int PORTE = 0xF84;
    public int SPPEPS = 0xF64;
    public int PLUSW1 = 0xFE3;
    public int ADRESL = 0xFC3;
    public int PORTD = 0xF83;
    public int SPPCFG = 0xF63;
    public int FSR1H = 0xFE2;
    public int ADCON0 = 0xFC2;
    public int IPR2 = 0xFA2;
    public int PORTC = 0xF82;
    public int SPPDATA = 0xF62;
    public int FSR1L = 0xFE1;
    public int ADCON1 = 0xFC1;
    public int PIR2 = 0xFA1;
    public int PORTB = 0xF81;
    public int BSR = 0xFE0;
    public int ADCON2 = 0xFC0;
    public int PIE2 = 0xFA0;
    public int PORTA = 0xF80;

    public Pic18f2550Ram(List<MemoryRange> gpRegisters, List<MemoryRange> sfRegisters) {
        super(gpRegisters, sfRegisters);
    }

    public static Pic18f2550Ram create() {
        Pic18f2550Ram ram;
        List<MemoryRange> sfrs, gprs;
        sfrs = new ArrayList<>();
        gprs = new ArrayList<>();

        gprs.add(new MemoryRange(0x60, 0xFF));
        gprs.add(new MemoryRange(0x100, 0x1FF));
        gprs.add(new MemoryRange(0x200, 0x2FF));
        gprs.add(new MemoryRange(0x300, 0x3FF));
        gprs.add(new MemoryRange(0x400, 0x4FF));
        gprs.add(new MemoryRange(0x500, 0x5FF));
        gprs.add(new MemoryRange(0x600, 0x6FF));
        gprs.add(new MemoryRange(0x700, 0x7FF));

        sfrs.add(new MemoryRange(0xF62, 0xF84));
        sfrs.add(new MemoryRange(0xF89, 0xF8D));
        sfrs.add(new MemoryRange(0xF92, 0xF96));
        sfrs.add(new MemoryRange(0xF9B, 0xF9B));
        sfrs.add(new MemoryRange(0xF9D, 0xFA2));
        sfrs.add(new MemoryRange(0xFA6, 0xFA9));
        sfrs.add(new MemoryRange(0xFAB, 0xFB8));
        sfrs.add(new MemoryRange(0xFBA, 0xFD3));
        sfrs.add(new MemoryRange(0xFD5, 0xFFF));

        ram = new Pic18f2550Ram(gprs, sfrs);
        return ram;
    }

    public boolean containsSFR(int address) {
        boolean contains = false;
        for (int i = 0; i < sfRegisters.size(); i++) {
            contains = sfRegisters.get(i).contains(address);
        }
        return contains;
    }

    public boolean containsGPR(int address) {
        boolean contains = false;
        for (int i = 0; i < gpRegisters.size(); i++) {
            contains = gpRegisters.get(i).contains(address);
        }
        return contains;
    }
}
