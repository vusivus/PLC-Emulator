/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vts.jplceditor.ui.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author Vusivus
 */
public class ProgramViewController implements Initializable {

    @FXML
    private Button acceptButton;
    @FXML
    private Button cancelButton;
    @FXML
    private TextField nameTF;

    private MainWindowController controller;

    
    /**
     * Initializes the controller class.
     * @param controller
     */
    
   public void setController(MainWindowController controller) {
        this.controller = controller;
    } 
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void acceptButtonAction(ActionEvent event) {
        String name = nameTF.getText();
        controller.addProgram(name);
    }

    @FXML
    private void cancelButtonAction(ActionEvent event) {
          controller.closePopup();
    }
    
}
