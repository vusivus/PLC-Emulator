/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project;

import com.vts.jplceditor.project.JPLCProject;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Vusivus
 */
public class ProjectTest {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String name = "myProject";
        String url = "C:\\Users\\Vusivus\\Documents\\Adlm\\";
        JPLCProject project = new JPLCProject(url,name);
        try {
            project.saveProject();
        } catch (IOException ex) {
            Logger.getLogger(ProjectTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
