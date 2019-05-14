/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vts.jplceditor.ui.controller;

import com.vts.jplceditor.resource.R;
import com.vts.jplceditor.compiler.ld.LDCompiler;
import com.vts.jplceditor.compiler.plc.PLCDataType;
import com.vts.jplceditor.ld.LadderDiagram;
import com.vts.jplceditor.ld.instruction.DragDataFormatter;
import com.vts.jplceditor.ld.instruction.InstructionUtil;
import com.vts.jplceditor.project.JPLCProject;
import com.vts.jplceditor.project.SavableProject;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import com.vts.jplceditor.ld.instruction.Instruction;
import javafx.scene.control.TextArea;

/**
 *
 * @author Vusumuzi_Tshabangu
 */
public class MainWindowController implements Initializable {

    @FXML
    private Button newLadderButton;
    @FXML
    private Button saveButton;
    @FXML
    private Button undoButton;
    @FXML
    private Button redoButton;
    @FXML
    private Button verifyButton;
    @FXML
    private Button downloadButton;
    @FXML
    private Button settingsButton;
    @FXML
    private Button redoButton1;
    @FXML
    private ScrollPane ladderPane;
    @FXML
    private TreeView<String> programTree;
    @FXML
    private TreeView<String> instructionTree;
    @FXML
    private TextArea status;

    private Stage popupStage;
    private URL url = null;
    private Parent root = null;
    private HashMap<String, String> tags;
    private TreeItem<String> tagList;
    private TreeItem<String> ioList;
    private TreeItem<String> timerList;
    private TreeItem<String> ladderList;
    private TreeItem<String> instructionList;
    private TreeItem<String> programTreeRoot;
    private Dragboard dragBoard;
    private ClipboardContent dragContent;
    private JPLCProject project;
    private LadderDiagram ladderDiagram;
    private LDCompiler compiler;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        ImageView rootIcon = new ImageView(new Image(R.ICON_FOLDER));
        programTreeRoot = new TreeItem<>("Ladder Program", rootIcon);
        programTree.setRoot(programTreeRoot);

        ImageView instructionRoot = new ImageView(new Image(R.ICON_FOLDER));
        ImageView tagRoot = new ImageView(new Image(R.ICON_TAG_FOLDER));
        Image programRoot = new Image(R.ICON_LADDER_FOLDER);

        tagList = new TreeItem<>("Tags", tagRoot);
        ioList = UIUtil.buildIOLIst();
        timerList = UIUtil.buildTimerList();
        ladderList = new TreeItem<>("Programs", new ImageView(programRoot));
        instructionList = UIUtil.buildInstructionList();

        programTreeRoot.getChildren().add(ladderList);
        programTreeRoot.getChildren().add(tagList);
        programTreeRoot.getChildren().add(ioList);
        programTreeRoot.getChildren().add(timerList);
        programTreeRoot.setExpanded(true);

        instructionTree.setRoot(instructionList);
        instructionList.setExpanded(true);
        popupStage = new Stage();

        tags = new HashMap<>();
        UIUtil.buildIoList(tags);
    }

    @FXML
    private void saveProgram(ActionEvent event) {
    }

    @FXML
    private void onExit(ActionEvent event) {
        System.exit(0);
    }

    @FXML
    private void redoAction(ActionEvent event) {
    }

    @FXML
    private void verifyProgram(ActionEvent event) {
    }

    @FXML
    private void createTag(ActionEvent event) {
        url = getClass().getResource(R.WINDOW_TAG);
        showPopup(Popup.TagWindow);
    }

    @FXML
    private void createRung(ActionEvent event) {
        url = getClass().getResource(R.WINDOW_RUNG);
        showPopup(Popup.RungWindow);
    }

    @FXML
    private void undoAction(ActionEvent event) {
    }

    @FXML
    private void downloadProgram(ActionEvent event) {
        if (project != null) {
            status.appendText(project.download().toString());
        } else {
            status.appendText("\nYou need to create a Project and Compile it First!!!!!!!!!!\n");
        }
    }

    @FXML
    private void settingsAction(ActionEvent event) {
    }

    @FXML
    private void helpAction(ActionEvent event) {
    }

    @FXML
    private void createNewProject(ActionEvent event) {
        url = getClass().getResource(R.WINDOW_PROJECT);
        showPopup(Popup.ProjectWindow);
    }

    @FXML
    private void openProject(ActionEvent event) {
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Project File", "*.xml")
        );
        final File projFile = chooser.showOpenDialog(null);
        if (projFile != null) {
            SavableProject pro = JPLCProject.readInProject(projFile);
            project = new JPLCProject(pro);
            ladderDiagram = project.getDefaultLD();
            ladderPane.setContent(ladderDiagram);
        }
    }

    @FXML
    private void saveProject(ActionEvent event) {
        if (project != null) {
            try {
                status.appendText(project.saveProject().toString());
            } catch (IOException ex) {
                Logger.getLogger(MainWindowController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @FXML
    private void deleteAction(ActionEvent event) {
    }

    @FXML
    private void compileProgram(ActionEvent event) {
        if (project != null) {
            status.clear();
            status.appendText(project.compileProject().toString());
        } else {
            status.appendText("\nYou need to create a Project First !!!!!!!!!!\n");
        }
    }

    @FXML
    private void commSettingsAction(ActionEvent event) {
    }

    @FXML
    private void documentationAction(ActionEvent event) {
    }

    @FXML
    private void createNewLadder(ActionEvent event) {
        url = getClass().getResource(R.WINDOW_PROGRAM);
        showPopup(Popup.ProgramWindow);
    }

    @FXML
    private void onProgramDragDetected(MouseEvent event) {
        dragBoard = programTree.startDragAndDrop(TransferMode.COPY);
        dragContent = new ClipboardContent();
        String selected = programTree.getSelectionModel().getSelectedItem().getValue();
        String parent = programTree.getSelectionModel().getSelectedItem().getParent().getValue();
        boolean isTag = parent.equals("Tags") || parent.equals("Digital Inputs")
                || parent.equals("Digital Outputs") || parent.equals("Analog Inputs") || parent.equals("Analog Outputs");
        if (isTag) {
            String type = tags.get(selected);
            String dragData = DragDataFormatter.formatTag(selected, type);
            dragContent.putString(dragData);
            dragBoard.setContent(dragContent);
        }
        if (parent.equals("Timer") || parent.equals("Counter")) {
            String type;
            if (selected.contains("TT") || selected.contains("DN")) {
                type = DragDataFormatter.TagType_BIT;
            } else {
                type = DragDataFormatter.TagType_INT;
            }
            String dragData = DragDataFormatter.formatTag(selected, type);
            dragContent.putString(dragData);
            dragBoard.setContent(dragContent);
        }

        event.consume();
    }

    @FXML
    private void onInstructionDragDetected(MouseEvent event) {

        String selected = instructionTree.getSelectionModel().getSelectedItem().getValue();
        String parent = instructionTree.getSelectionModel().getSelectedItem().getParent().getValue();
        Instruction i = InstructionUtil.getInstruction(selected);
        if (i != null) {
            dragBoard = instructionTree.startDragAndDrop(TransferMode.COPY);
            dragContent = new ClipboardContent();
            dragContent.putString(selected);
            dragBoard.setContent(dragContent);
        } else {
            System.out.println("Null Instruction:" + selected);
        }
        event.consume();
    }

    private void showPopup(Popup popup) {
        if (url != null) {
            try {

                FXMLLoader load = new FXMLLoader(url);
                root = load.load();
                if (popup == Popup.TagWindow) {
                    TagWindowController con = (TagWindowController) load.getController();
                    if (con != null) {
                        con.setController(this);
                    }
                } else if (popup == Popup.RungWindow) {
                    RungViewController con = (RungViewController) load.getController();
                    if (con != null) {
                        con.setController(this);
                    }
                } else if (popup == Popup.ProgramWindow) {
                    ProgramViewController con = (ProgramViewController) load.getController();
                    if (con != null) {
                        con.setController(this);
                    }
                } else if (popup == Popup.ProjectWindow) {
                    ProjectController con = (ProjectController) load.getController();
                    if (con != null) {
                        con.setController(this);
                    }
                }
                popupStage.setScene((new Scene(root)));
                popupStage.show();
            } catch (IOException ex) {
                Logger.getLogger(MainWindowController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     *
     */
    public void closePopup() {
        popupStage.close();
    }

    /**
     *
     * @param tagName
     * @param tagType
     */
    public void addTag(String tagName, String tagType) {
        ImageView tagIcon = new ImageView(new Image(R.ICON_TAG_ICON));
        tagList.getChildren().add(new TreeItem<>(tagName, tagIcon));
        tags.put(tagName, tagType);
        PLCDataType type = UIUtil.typeFromString(tagType);
        project.addTag(type, tagName);
        closePopup();
    }

    /**
     *
     * @param rungNumber
     */
    public void addRung(int rungNumber) {
        ladderDiagram.addRung(rungNumber);
        closePopup();
    }

    /**
     *
     * @param programName
     */
    public void addProgram(String programName) {
        ImageView rungIcon = new ImageView(new Image(R.ICON_LADDER_ICON));
        ladderList.getChildren().add(new TreeItem<>(programName, rungIcon));
        closePopup();
    }

    /**
     *
     * @param name
     * @param directory
     */
    public void createProject(String name, String directory) {
        project = new JPLCProject(directory, name);
        ladderDiagram = project.getDefaultLD();
        ladderPane.setContent(ladderDiagram);
        closePopup();
    }

    /**
     * Generic method for putting element running on a non-JavaFX thread on the
     * JavaFX thread, to properly update the UI
     *
     * @param property a {@link ObjectProperty}
     * @param value the value to set for the given {@link ObjectProperty}
     */
    private <T> void onFXThread(final ObjectProperty<T> property, final T value) {
        Platform.runLater(new Runnable() {

            @Override
            public void run() {
                property.set(value);
            }
        });
    }

}
