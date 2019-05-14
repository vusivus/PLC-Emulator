/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vts.jplceditor.resource;

/**
 *
 * @author Vusumuzi_Tshabangu
 */
public class R {

    /**
     *
     */
    public static String WINDOW_MAIN = "/com/vts/jplceditor/ui/fxml/MainWindow.fxml";

    /**
     *
     */
    public static String WINDOW_PROJECT = "/com/vts/jplceditor/ui/fxml/Project.fxml";

    /**
     *
     */
    public static String WINDOW_TAG = "/com/vts/jplceditor/ui/fxml/TagWindow.fxml";

    /**
     *
     */
    public static String WINDOW_RUNG = "/com/vts/jplceditor/ui/fxml/RungView.fxml";

    /**
     *
     */
    public static String WINDOW_PROGRAM = "/com/vts/jplceditor/ui/fxml/ProgramView.fxml";

    /**
     *
     */
    public static String OBJECT_CONNECTION = "/com/vts/jplceditor/ui/ladder/Connection.fxml";

    /**
     *
     */
    public static String OBJECT_RAIL = "/com/vts/jplceditor/ui/ladder/Rail.fxml";
    
    /**
     *
     */
    public static String ICON_TAG_FOLDER = "/com/vts/jplceditor/resource/icon/tag_folder.png";

    /**
     *
     */
    public static String ICON_INSTR_ICON = "/com/vts/jplceditor/resource/icon/pencil-icon.png";

    /**
     *
     */
    public static String ICON_TAG_ICON = "/com/vts/jplceditor/resource/icon/tag_icon.png";

    /**
     *
     */
    public static String ICON_LADDER_ICON = "/com/vts/jplceditor/resource/icon/ladder_icon.png";

    /**
     *
     */
    public static String ICON_LADDER_FOLDER = "/com/vts/jplceditor/resource/icon/program_folder.png";

    /**
     *
     */
    public static String ICON_FOLDER = "/com/vts/jplceditor/resource/icon/folder-icon.png";
    
    /**
     *
     */
    public static String[] LIST_INSTR_RELAY = new String[]{
        "BRN","XIC","XIO","OTE","OTL","OTU",
    };

    /**
     *
     */
    public static String[] LIST_INSTR_TIMER = new String[]{
        "TON","CTU","CTD"
    };

    /**
     *
     */
    public static String[] LIST_INSTR_COMPARE = new String[]{
        "EQU","GRT","LES","LIM"
    };

    /**
     *
     */
    public static String[] LIST_INSTR_MATH = new String[]{
        "ADD","SUB","MUL","DIV","SQR"
    };

    /**
     *
     */
    public static String[] LIST_INSTR_LOGICAL = new String[]{
        "AND","OR","XOR",
    };

    /**
     *
     */
    public static String[] LIST_INSTR_FILE = new String[]{
        "MOV","MVM"
    };

    /**
     *
     */
    public static String[] LIST_DATA_TIMER = new String[]{
        "T-DN","T-TT","T-COUNT"
    };

    /**
     *
     */
    public static String[] LIST_DATA_COUNTER = new String[]{
        "C-DN","C-COUNT"
    };
}
