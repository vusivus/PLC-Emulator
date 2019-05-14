/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vts.jplceditor.ld;

import com.vts.jplceditor.ld.component.LDLine;
import com.vts.jplceditor.ld.component.LDRung;
import com.vts.jplceditor.ld.component.node.SNode;
import com.vts.jplceditor.ld.layout.RailLayout;
import com.vts.jplceditor.ld.layout.Size;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.Group;
import javafx.scene.layout.Pane;

/**
 *
 * @author Vusumuzi_Tshabangu
 */
public class LadderDiagram extends Pane {

    private final int width = 640;
    private final int height = 720;
    private RailLayout rungLayout;
    private List<LDRung> rungList;
    private LDLine sRail, tRail;
    private Group rungs;
    private Group nodes;
    private List<SNode> sourceNodes;

    /**
     *
     */
    public LadderDiagram() {
        super.setWidth(width);
        super.setHeight(height);
        sourceNodes = new ArrayList<>();
        rungs = new Group();
        nodes = new Group();
        getChildren().add(rungs);
        getChildren().add(nodes);
        rungList = new ArrayList<>();
        createRail();
        createDefaultRung();
    }

    public LadderDiagram(List<LDRung> rungList, List<SNode> sourceNodes) {
        this.rungList = rungList;
        this.sourceNodes = sourceNodes;
        super.setWidth(width);
        super.setHeight(height);
        rungs = new Group();
        nodes = new Group();
        getChildren().add(rungs);
        getChildren().add(nodes);
        createRail();
        for (LDRung rung : rungList) {
            rungs.getChildren().add(rung);
            rungList.add(rung);
            getChildren().add(rung.getInstructions());
        }
        for (SNode snode : sourceNodes) {
            nodes.getChildren().add(snode);
        }
    }

    /**
     *
     */
    public void addRung(int position) {
        rungLayout.addNewSourceNode();
        List<SNode> sNodes = rungLayout.getSourceNodes();
        int count = sNodes.size();
        SNode sNode = rungLayout.getSourceNodes().get(count - 1);
        LDRung r = new LDRung(sNode, new Size(width, RailLayout.RUNG_SIZE), 2);
        nodes.getChildren().add(sNode);
        rungList.add( r);
        rungs.getChildren().add(r);
        getChildren().add(r.getInstructionGroup());
        
       // for(int i=0;i<count;i++){
         //   rungList.get(i).relocateToPoint(sNodes.get(i).getLocation());
       // }
    }

    private void createRail() {
        rungLayout = new RailLayout(width, height);
        sRail = new LDLine(rungLayout.getRailStart(), rungLayout.getRailEnd());
        sRail.setStrokeWidth(4);
        getChildren().add(sRail);
    }

    public List<SNode> getSourceNodes() {
        return sourceNodes;
    }

    private void createDefaultRung() {
        SNode point = rungLayout.getSourceNodes().get(0);
        LDRung r = new LDRung(point, new Size(width, RailLayout.RUNG_SIZE), 2);
        nodes.getChildren().add(r.getsNode());
        rungList.add(r);
        rungs.getChildren().add(r);
        getChildren().add(r.getInstructions());
    }

    /**
     *
     * @return
     */
    public List<LDRung> getRungList() {
        return rungList;
    }

}
