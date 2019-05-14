/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vts.jplceditor.project;

import com.thoughtworks.xstream.XStream;
import com.vts.jplceditor.comms.usb.SessionManager;
import com.vts.jplceditor.compiler.ld.LDCompiler;
import com.vts.jplceditor.compiler.pic18.Pic18Prog;
import com.vts.jplceditor.compiler.plc.PLCDataType;
import com.vts.jplceditor.compiler.plc.PLCOperand;
import com.vts.jplceditor.ld.LadderDiagram;
import com.vts.jplceditor.project.io.IOLD;
import com.vts.jplceditor.project.io.LDParser;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Vusivus
 */
public class JPLCProject {

    private String path;
    private String projectName;
    private List<LadderDiagram> ladderDiagrams;
    private List<PLCOperand> tags;
    private File projectFile, hexFile, asmFile, ldFile, programFile;
    private LDCompiler compiler;
    private static final Charset CHARSET = Charset.forName("UTF-8");
    private SessionManager plcSession;
    private boolean compiled;

    /**
     *
     * @param path
     * @param projectName
     */
    public JPLCProject(String path, String projectName) {
        this.path = path + projectName + "\\";
        this.projectName = projectName;
        File dir = new File(this.path);
        dir.mkdir();
        ladderDiagrams = new ArrayList<>();
        tags = new ArrayList<>();
        createDefault();
    }

    /**
     *
     * @param plcProject
     */
    public JPLCProject(SavableProject plcProject) {
        path = plcProject.directory;
        projectName = plcProject.getProjectTitle();
        tags = plcProject.getTagList();
        ladderDiagrams = fromIOLDList(plcProject.getLadderDiagramList());
        projectFile = new File(path + projectName + ".xml");
        createDefault();
    }

    private List<IOLD> fromLadderDiagramList() {
        List<IOLD> list = new ArrayList<>();
        ladderDiagrams.stream().forEach((ld) -> {
            list.add(LDParser.fromLadderDiagram(ld));
        });
        return list;
    }

    private List<LadderDiagram> fromIOLDList(List<IOLD> iolds) {
        List<LadderDiagram> list = new ArrayList<>();
        iolds.stream().forEach((ld) -> {
            list.add(LDParser.fromIOLD(ld));
        });
        return list;
    }

    private void createDefault() {
        ladderDiagrams.add(new LadderDiagram());
        projectFile = new File(path + projectName + ".xml");
        asmFile = new File(path + projectName + ".asm");
        hexFile = new File(path + projectName + ".hex");
        ldFile = new File(path + projectName + ".ld");
        programFile = new File(path + projectName + ".pgm");
        compiler = new LDCompiler();
    }

    /**
     *
     * @return
     */
    public LadderDiagram getDefaultLD() {
        return ladderDiagrams.get(0);
    }

    /**
     *
     */
    public void addLD() {
        ladderDiagrams.add(new LadderDiagram());
    }

    /**
     *
     * @param type
     * @param name
     */
    public void addTag(PLCDataType type, String name) {
        tags.add(new PLCOperand(type, name));
    }

    /**
     *
     * @param tag
     */
    public void addTag(PLCOperand tag) {
        tags.add(tag);
    }

    /**
     *
     * @return
     */
    public String getPath() {
        return path;
    }

    /**
     *
     * @return
     */
    public List<LadderDiagram> getLadderDiagrams() {
        return ladderDiagrams;
    }

    /**
     *
     * @return
     */
    public List<PLCOperand> getTags() {
        return tags;
    }

    /**
     *
     * @param path
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     *
     * @return
     */
    public String getProjectName() {
        return projectName;
    }

    /**
     *
     * @param projectName
     */
    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    /**
     *
     * @return @throws IOException
     */
    public StringBuilder saveProject() throws IOException {
        StringBuilder status = new StringBuilder();
        status.append("\n");
        XStream xs = XmlAlias.projectAlias();
        List<IOLD> ldList = fromLadderDiagramList();
        SavableProject p = new SavableProject();
        p.setDirectory(path);
        p.setLadderDiagramList(ldList);
        p.setProjectTitle(projectName);
        p.setTagList(tags);
        String content = xs.toXML(p);
        XmlFile.saveFile(content, projectFile);
        status.append("--------------------------------------------------------");
        status.append("\n");
        status.append("Saved project ").append(projectName).append(" at ").append(path);
        status.append("\n");
        return status;
    }

    /**
     *
     * @return
     */
    public StringBuilder compileProject() {
        StringBuilder status = new StringBuilder();
        // if (!compiled) {
        if (compiler.compile(ladderDiagrams.get(0), tags)) {
            try {
                status.append("\n");
                status.append("=========================================================");
                status.append("\n");
                status.append("Compiled Project ").append(projectName);
                status.append("\n");
                saveFile(compiler.getLDStatement(), ldFile);
                status.append("Generated Ladder Diagram File at :").append(ldFile.getAbsolutePath());
                status.append("\n");
                saveFile(compiler.getPicAsm(), asmFile);
                status.append("Generated asm File at :").append(asmFile.getAbsolutePath());
                status.append("\n");
                saveFile(compiler.getPicHex(), hexFile);
                status.append("Generated Hex File at :").append(hexFile.getAbsolutePath());
                status.append("\n");
                saveFile(compiler.getPicProgramMap().toString(), programFile);
                status.append("Generated Program File at :").append(programFile.getAbsolutePath());

            } catch (IOException ex) {
                Logger.getLogger(JPLCProject.class.getName()).log(Level.SEVERE, null, ex);
            }
            compiled = true;
        } else {
            status.append("\n");
            status.append("=========================================================");
            status.append("\nError compiling program");
            compiled =false;
        }

        // }
        return status;
    }

    /**
     *
     * @param file
     * @return
     */
    public static SavableProject readInProject(File file) {
        XStream xs = XmlAlias.projectAlias();
        SavableProject project = (SavableProject) xs.fromXML(file);
        return project;
    }

    /**
     *
     * @param content
     * @param file
     * @throws IOException
     */
    public static void saveFile(String content, File file) throws IOException {
        BufferedWriter writer = Files.newBufferedWriter(file.toPath(), CHARSET);
        writer.write(content, 0, content.length());
        writer.close();
    }

    /**
     *
     * @return
     */
    public StringBuilder download() {
        StringBuilder status = new StringBuilder();
        status.append("\n");
        if (compiled) {
            plcSession = new SessionManager();
            Pic18Prog program = compiler.getPicProgram();
            status.append(program.getProgramInformation());
            status.append(plcSession.writeToPlc(program.getCodeHex().array()));
        } else {
            status.append("=======     ERROR !!!!!!     ===========");
            status.append("\n");
            status.append("Project ").append(this.projectName).append(" is not compiled yet !!!!");
            status.append("\n");
        }
        return status;
    }
}
