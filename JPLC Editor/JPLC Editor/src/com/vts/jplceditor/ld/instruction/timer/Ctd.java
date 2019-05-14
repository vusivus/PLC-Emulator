/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vts.jplceditor.ld.instruction.timer;

import com.vts.jplceditor.compiler.plc.PLCDataType;
import com.vts.jplceditor.compiler.plc.PLCInstruction;
import com.vts.jplceditor.compiler.plc.PLCOperand;
import com.vts.jplceditor.compiler.plc.PLCOperator;
import com.vts.jplceditor.ld.instruction.IR;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import com.vts.jplceditor.ld.instruction.Instruction;

/**
 * FXML Controller class
 *
 * @author Vusivus
 */
public class Ctd extends AnchorPane implements Initializable, Instruction {

    @FXML
    private ComboBox<String> timerCB;
    @FXML
    private TextField countTF;
    private boolean entered;
    private List<PLCOperand> parameters;
    private EventHandler<ActionEvent> cbAction;
    private EventHandler<ActionEvent> tfAction;

    public Ctd() {
        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource(IR.Path_CTD));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException exception) {

        }
    }

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ObservableList<String> options
                = FXCollections.observableArrayList("Counter1",
                        "Counter2"
                );
        timerCB.setItems(options);
        parameters = new ArrayList<>();
        parameters.add(new PLCOperand(PLCDataType.BIT, "CD"));
        parameters.add(new PLCOperand(PLCDataType.BIT, "UN"));
        parameters.add(new PLCOperand(PLCDataType.BIT, "DN"));
        parameters.add(new PLCOperand(PLCDataType.BIT, "R"));
        parameters.add(new PLCOperand(PLCDataType.BYTE, "Counter1"));
        parameters.add(new PLCOperand(PLCDataType.INT, "PRE"));
        parameters.add(new PLCOperand(PLCDataType.INT, "ACC"));
        buildHandlers();
    }

    @Override
    public AnchorPane getInstance() {
        return this;
    }

    public void relocateTo(Point2D p) {
        Point2D point = getParent().sceneToLocal(p);
        relocate(point.getX(), point.getY());
    }

   @Override
    public PLCInstruction getPLCInstruction() {
        parameters.get(4).setIdentity(timerCB.getSelectionModel().getSelectedItem());
        parameters.get(5).setIdentity(countTF.getText());
        PLCInstruction instruction = new PLCInstruction(PLCOperator.CTU);
        instruction.setOperands(parameters);
        return instruction;
    }

    private void buildHandlers() {
        cbAction = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (timerCB.getSelectionModel().getSelectedIndex() >= 0) {
                    countTF.setDisable(false);
                    parameters.get(4).setIdentity((String) timerCB.getSelectionModel().getSelectedItem());
                }
            }
        };
        tfAction = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String text = countTF.getText();
                int value = 0;
                try {
                    value = Integer.parseInt(text);
                } catch (NumberFormatException ex) {

                };
                countTF.setText(String.valueOf(value));
                parameters.get(5).setIdentity(countTF.getText());
            }
        };

        timerCB.setOnAction(cbAction);
        countTF.setOnAction(tfAction);
    }
    
    @Override
    public void setParameters(List<PLCOperand> parameters) {
       this.parameters = parameters; 
       countTF.setText(parameters.get(5).getIdentity());
    }

}
