/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vts.jplceditor.compiler.pic18;

/**
 *
 * @author Vusivus
 */
public interface Pic18Instruction {
    public void Assemble();
    public short get16BitCode();
    public short getSecond16BitCode();
    public int getWordCount();
    public void setFile(int file);
    public void setFile2(int file);
}
