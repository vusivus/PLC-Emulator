/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vts.jplceditor.ui.controller;

import com.vts.jplceditor.compiler.plc.PLCDataType;
import com.vts.jplceditor.ld.instruction.DragDataFormatter;
import com.vts.jplceditor.resource.R;
import java.util.HashMap;
import javafx.scene.control.TreeItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 *
 * @author Vusumuzi_Tshabangu
 */
public class UIUtil {

    /**
     *
     * @return
     */
    public static TreeItem<String> buildInstructionList() {
        Image rootIcon = new Image(R.ICON_LADDER_FOLDER);
        Image elementIcon = new Image(R.ICON_INSTR_ICON);
        TreeItem<String> root = new TreeItem<>("Instructions", new ImageView(rootIcon));
        TreeItem<String> relay = new TreeItem<>("Relay", new ImageView(rootIcon));
        for (String name : R.LIST_INSTR_RELAY) {
            relay.getChildren().add(new TreeItem<>(name, new ImageView(elementIcon)));
        }
        TreeItem<String> timer = new TreeItem<>("Timer", new ImageView(rootIcon));
        for (String name : R.LIST_INSTR_TIMER) {
            timer.getChildren().add(new TreeItem<>(name, new ImageView(elementIcon)));
        }

        TreeItem<String> compare = new TreeItem<>("Compare", new ImageView(rootIcon));
        for (String name : R.LIST_INSTR_COMPARE) {
            compare.getChildren().add(new TreeItem<>(name, new ImageView(elementIcon)));
        }
        TreeItem<String> math = new TreeItem<>("Math", new ImageView(rootIcon));
        for (String name : R.LIST_INSTR_MATH) {
            math.getChildren().add(new TreeItem<>(name, new ImageView(elementIcon)));
        }
        

        TreeItem<String> logical = new TreeItem<>("Logical", new ImageView(rootIcon));
        for (String name : R.LIST_INSTR_LOGICAL) {
            logical.getChildren().add(new TreeItem<>(name, new ImageView(elementIcon)));
        }

        TreeItem<String> file = new TreeItem<>("File", new ImageView(rootIcon));
        for (String name : R.LIST_INSTR_FILE) {
            file.getChildren().add(new TreeItem<>(name, new ImageView(elementIcon)));
        }

        root.getChildren().add(relay);
        root.getChildren().add(timer);
        root.getChildren().add(math);
        root.getChildren().add(compare);
        root.getChildren().add(file);
        root.getChildren().add(logical);

        return root;
    }

    /**
     *
     * @return
     */
    public static TreeItem<String> buildTimerList() {
        Image rootIcon = new Image(R.ICON_TAG_FOLDER);
        Image elementIcon = new Image(R.ICON_TAG_ICON);
        TreeItem<String> root = new TreeItem<>("Timers", new ImageView(rootIcon));

        TreeItem<String> timer = new TreeItem<>("Timer", new ImageView(rootIcon));
        for (String name : R.LIST_DATA_TIMER) {
            timer.getChildren().add(new TreeItem<>(name, new ImageView(elementIcon)));
        }

        TreeItem<String> counter = new TreeItem<>("Counter", new ImageView(rootIcon));
        for (String name : R.LIST_DATA_COUNTER) {
            counter.getChildren().add(new TreeItem<>(name, new ImageView(elementIcon)));
        }

        root.getChildren().add(timer);
        root.getChildren().add(counter);

        return root;
    }

    /**
     *
     * @return
     */
    public static TreeItem<String> buildIOLIst() {
        Image rootIcon = new Image(R.ICON_TAG_FOLDER);
        Image diiIcon = new Image(R.ICON_TAG_FOLDER);
        Image dioIcon = new Image(R.ICON_TAG_FOLDER);
        Image anInIcon = new Image(R.ICON_TAG_FOLDER);
        Image anOutIcon = new Image(R.ICON_TAG_FOLDER);
        Image elementIcon = new Image(R.ICON_TAG_ICON);
        
        TreeItem<String> root = new TreeItem<>("I/O", new ImageView(rootIcon));
        TreeItem<String> diiItems = new TreeItem<>("Digital Inputs", new ImageView(rootIcon));
        TreeItem<String> dioItems = new TreeItem<>("Digital Outputs", new ImageView(rootIcon));
        TreeItem<String> anInItems = new TreeItem<>("Analog Inputs", new ImageView(rootIcon));
        TreeItem<String> anOutItems = new TreeItem<>("Analog Outputs", new ImageView(rootIcon));
        
        String[] diodata = new String[]{
            "DO0", "DO1", "DO2", "DO3", "DO4", "DO5", "DO6", "DO7"
        };
        String[] diidata = new String[]{
            "DI0", "DI1", "DI2", "DI3", "DI4", "DI5", "DI6", "DI7"
        };
        String[] anIndata = new String[]{
            "AI0","AI1","AI2","AI3","AI4","AI5","AI6","AI7",
        };
        String[] anOutdata = new String[]{
            "AO0"
        };
        for (String name : diidata) {
            diiItems.getChildren().add(new TreeItem<>(name, new ImageView(elementIcon)));
        }
        for (String name : diodata) {
            dioItems.getChildren().add(new TreeItem<>(name, new ImageView(elementIcon)));
        }
        for (String name : anIndata) {
            anInItems.getChildren().add(new TreeItem<>(name, new ImageView(elementIcon)));
        }
        for (String name : anOutdata) {
            anOutItems.getChildren().add(new TreeItem<>(name, new ImageView(elementIcon)));
        }
        root.getChildren().add(diiItems);
        root.getChildren().add(dioItems);
        root.getChildren().add(anInItems);
        root.getChildren().add(anOutItems);
        return root;
    }

    /**
     *
     * @param type
     * @return
     */
    public static PLCDataType typeFromString(String type) {
        if (type.equals(DragDataFormatter.TagType_BIT)) {
            return PLCDataType.BIT;
        } else if (type.equals(DragDataFormatter.TagType_BYTE)) {
            return PLCDataType.BYTE;
        } else if (type.equals(DragDataFormatter.TagType_FLOAT)) {
            return PLCDataType.FLOAT;
        } else if (type.equals(DragDataFormatter.TagType_INT)) {
            return PLCDataType.INT;
        } else {
            return null;
        }
    }
    
    public static void buildIoList(HashMap<String,String> tags){
       tags.put("DI0", DragDataFormatter.TagType_BIT);
       tags.put("DI1", DragDataFormatter.TagType_BIT);
       tags.put("DI2", DragDataFormatter.TagType_BIT);
       tags.put("DI3", DragDataFormatter.TagType_BIT);
       tags.put("DI4", DragDataFormatter.TagType_BIT);
       tags.put("DI5", DragDataFormatter.TagType_BIT);
       tags.put("DI6", DragDataFormatter.TagType_BIT);
       tags.put("DI7", DragDataFormatter.TagType_BIT);
       
       tags.put("DO0", DragDataFormatter.TagType_BIT);
       tags.put("DO1", DragDataFormatter.TagType_BIT);
       tags.put("DO2", DragDataFormatter.TagType_BIT);
       tags.put("DO3", DragDataFormatter.TagType_BIT);
       tags.put("DO4", DragDataFormatter.TagType_BIT);
       tags.put("DO5", DragDataFormatter.TagType_BIT);
       tags.put("DO6", DragDataFormatter.TagType_BIT);
       tags.put("DO7", DragDataFormatter.TagType_BIT);
       
       tags.put("AI0",DragDataFormatter.TagType_INT);
       tags.put("AI1",DragDataFormatter.TagType_INT);
       tags.put("AI2",DragDataFormatter.TagType_INT);
       tags.put("AI3",DragDataFormatter.TagType_INT);
       tags.put("AI4",DragDataFormatter.TagType_INT);
       tags.put("AI5",DragDataFormatter.TagType_INT);
       tags.put("AI6",DragDataFormatter.TagType_INT);
       tags.put("AI7",DragDataFormatter.TagType_INT);
       tags.put("AO0",DragDataFormatter.TagType_INT);
    }
}
