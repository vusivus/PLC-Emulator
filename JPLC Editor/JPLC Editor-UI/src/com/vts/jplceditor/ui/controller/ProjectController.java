/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vts.jplceditor.ui.controller;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;

/**
 * FXML Controller class
 *
 * @author Vusivus
 */
public class ProjectController implements Initializable {
    
    @FXML
    private TextField nameTF;
    @FXML
    private Button browseButton;
    @FXML
    private TextField directoryTF;
    private MainWindowController controller;
    @FXML
    private Button cancelButton;
    @FXML
    private Button acceptButton;

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    @FXML
    private void browseButtonAction(ActionEvent event) {
        DirectoryChooser chooser = new DirectoryChooser();
        final File directory = chooser.showDialog(null);
        if (directory != null) {
            directoryTF.setText(directory.getAbsolutePath()+"\\");
        }
    }
    
    void setController(MainWindowController con) {
        controller = con;
    }
    
    @FXML
    private void cancelButtonAction(ActionEvent event) {
        controller.closePopup();
    }
    
    @FXML
    private void acceptButtonAction(ActionEvent event) {
        String name, dir;
        name = nameTF.getText();
        dir = directoryTF.getText();
        
        if (!"".equals(name) && !"".equals(dir)) {
            controller.createProject(name, dir);
        }
    }
    
}
