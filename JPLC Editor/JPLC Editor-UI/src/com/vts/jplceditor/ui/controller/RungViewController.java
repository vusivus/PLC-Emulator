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
 * @author Vusumuzi_Tshabangu
 */
public class RungViewController implements Initializable {

    @FXML
    private Button acceptButton;
    @FXML
    private Button cancelButton;
    @FXML
    private TextField numberTF;

    private MainWindowController controller;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    /**
     *
     * @param controller
     */
    public void setController(MainWindowController controller) {
        this.controller = controller;
    }

    @FXML
    private void acceptButtonAction(ActionEvent event) {
        if (isInteger(numberTF.getText())) {
            int number = Integer.valueOf(numberTF.getText());
            controller.addRung(number);
        }
    }

    @FXML
    private void cancelButtonAction(ActionEvent event) {
        controller.closePopup();
    }

    public static boolean isInteger(String string) {
        if (string.isEmpty()) {
            return false;
        }
        for (int i = 0; i < string.length(); i++) {
            if (i == 0 && string.charAt(i) == '-') {
                if (string.length() == 1) {
                    return false;
                } else {
                    continue;
                }
            }
            if (Character.digit(string.charAt(i), 10) < 0) {
                return false;
            }
        }
        return true;
    }
}
