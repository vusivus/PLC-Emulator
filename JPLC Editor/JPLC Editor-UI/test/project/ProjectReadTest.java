/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project;

import com.vts.jplceditor.project.JPLCProject;
import com.vts.jplceditor.project.SavableProject;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Vusivus
 */
public class ProjectReadTest {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String filedir = "C:\\Users\\Vusivus\\Documents\\Test\\Test.xml";
        File file = new File(filedir);
        SavableProject proj = JPLCProject.readInProject(file);
        JPLCProject jproject = new JPLCProject(proj);
        try {
            jproject.saveProject();
        } catch (IOException ex) {
            Logger.getLogger(ProjectReadTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
