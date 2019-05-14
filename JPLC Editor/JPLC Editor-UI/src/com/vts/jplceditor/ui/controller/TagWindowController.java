/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vts.jplceditor.ui.controller;

import com.vts.jplceditor.ld.instruction.DragDataFormatter;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author Vusumuzi_Tshabangu
 */
public class TagWindowController implements Initializable {

    @FXML
    private TextField nameTF;
    @FXML
    private ComboBox<String> typeCB;
    @FXML
    private Button acceptButton;
    @FXML
    private Button cancelButton;
    
    MainWindowController controller;

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        typeCB.getItems().addAll(DragDataFormatter.TagType_BIT,
                DragDataFormatter.TagType_BYTE,
                DragDataFormatter.TagType_INT,
                DragDataFormatter.TagType_FLOAT);
    }    

    @FXML
    private void acceptButtonAction(ActionEvent event) {
        String name = nameTF.getText();
        String type = typeCB.getValue();
        if(name != "" && type != ""){
           controller.addTag(name, type); 
        }        
    }

    @FXML
    private void cancelButtonAction(ActionEvent event) {
        controller.closePopup();
    }

    /**
     *
     * @param controller
     */
    public void setController(MainWindowController controller) {
        this.controller = controller;
    }
       
}
