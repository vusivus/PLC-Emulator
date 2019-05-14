/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vts.jplceditor.project;

import com.vts.jplceditor.JPlcEditor;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.prefs.Preferences;

/**
 *
 * @author Electronics
 */
public class XmlFile {
    
/**
     * The character set. UTF-8 works good for windows, mac and Umlaute.
     */
    private static final Charset CHARSET = Charset.forName("UTF-8");
    private static final String xmlHeader="<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
    /**
     * Reads the specified file and returns the content as a String.
     * 
     * @param file
     * @return
     * @throws IOException thrown if an I/O error occurs opening the file
     */
    public static String readFile(File file) throws IOException {
        StringBuffer stringBuffer = new StringBuffer();

        BufferedReader reader = Files.newBufferedReader(file.toPath(), CHARSET);

        String line = null;
        while ((line = reader.readLine()) != null) {
            stringBuffer.append(line);
        }

        reader.close();

        return stringBuffer.toString();
    }

    /**
     * Saves the content String to the specified file.
     * 
     * @param content
     * @param file
     * @throws IOException thrown if an I/O error occurs opening or creating the file
     */
    public static void saveFile(String content, File file) throws IOException {
        BufferedWriter writer = Files.newBufferedWriter(file.toPath(), CHARSET);
        writer.write(xmlHeader);
        writer.write("\n");
        writer.write(content, 0, content.length());
        writer.close();
    }    
  /**
 * Returns the person file preference, i.e. the file that was last opened.
 * The preference is read from the OS specific registry. If no such
 * preference can be found, null is returned.
 * 
 * @return
 */
public File getPersonFilePath() {
  Preferences prefs = Preferences.userNodeForPackage(JPlcEditor.class);
  String filePath = prefs.get("filePath", null);
  if (filePath != null) {
    return new File(filePath);
  } else {
    return null;
  }
}

/**
 * Sets the file path of the currently loaded file.
 * The path is persisted in the OS specific registry.
 * 
 * @param file the file or null to remove the path
 */
public void setPersonFilePath(File file) {
  Preferences prefs = Preferences.userNodeForPackage(JPlcEditor.class);
  if (file != null) {
    prefs.put("filePath", file.getPath());

}  
}
}
