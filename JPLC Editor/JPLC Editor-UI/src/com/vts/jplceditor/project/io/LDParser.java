/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vts.jplceditor.project.io;

import com.vts.jplceditor.ld.LadderDiagram;
import com.vts.jplceditor.ld.component.LDInstructionManager;
import com.vts.jplceditor.ld.component.LDRung;
import com.vts.jplceditor.ld.component.LDInstruction;
import com.vts.jplceditor.ld.component.node.SNode;
import java.util.ArrayList;
import java.util.List;
import javafx.geometry.Point2D;

/**
 *
 * @author Vusivus
 */
public class LDParser {

    /**
     *
     * @param ld
     * @return
     */
    public static IOLD fromLadderDiagram(LadderDiagram ld) {
        IOLD ioladder = new IOLD();
        //Create IONode list
        List<SNode> sNodes = ld.getSourceNodes();
        List<IONode> nodes = new ArrayList<>();
        sNodes.stream().forEach((sNode) -> {
            nodes.add(fromSNode(sNode));
        });
        ioladder.setsNodes(nodes);
        //Create IORungList
        List<LDRung> rungs = ld.getRungList();
        List<IORung> rList = new ArrayList<>();
        rungs.stream().map((rung) -> {
            IORung ior = new IORung(fromSNode(rung.getsNode()));
            ior.setSize(rung.getSize());
            List<LDInstruction> inodes = rung.getInstructionList();
            List<IOInstruction> iList = new ArrayList<>();
            inodes.stream().map((inode) -> {
                IOInstruction i = new IOInstruction(inode.getStartPoint().getX(), inode.getStartPoint().getY());
                i.setSize(inode.getSize());
                if (inode.hasInstruction()) {
                    i.setInstruction(inode.getInstruction().getPLCInstruction().getOperator());
                    i.setParameters(inode.getInstruction().getPLCInstruction().getOperands());
                }
                return i;
            }).forEach((i) -> {
                iList.add(i);
            });
            ior.setInstructions(iList);
            return ior;
        }).forEach((ior) -> {
            rList.add(ior);
        });
        ioladder.setRungs(rList);
        return ioladder;
    }

    private static IONode fromSNode(SNode sNode) {
        return new IONode(sNode.getNodeType(), sNode.getCenterX(), sNode.getCenterY());
    }

    /**
     *
     * @param ld
     * @return
     */
    public static LadderDiagram fromIOLD(IOLD ld) {
        LadderDiagram ladder = new LadderDiagram();

        //Create SourceNode List
        List<IONode> nodes = ld.getsNodes();
        List<SNode> sourceNodes = new ArrayList<>();
        nodes.stream().forEach((node) -> {
            sourceNodes.add(new SNode(new Point2D(node.getLocationX(), node.getLocationY())));
        });
        //Create LDRung List

        List<IORung> rungs = ld.getRungs();
        matchRungs(rungs, ladder);
        List<LDRung> ldRungs = ladder.getRungList();

        createRungList(rungs, ldRungs);
        return ladder;
    }

    private static void createRungList(List<IORung> rungs, List<LDRung> ldRungs) {
        for (int i = 0; i < rungs.size(); i++) {
            List<IOInstruction> ins = rungs.get(i).getInstructions();
            LDInstructionManager manager = ldRungs.get(i).getInstructions();
            manager.clearAll();
            List<LDInstruction> inodes = new ArrayList<>();
            for (IOInstruction ioIns : ins) {
                LDInstruction inode = new LDInstruction(new Point2D(ioIns.getLocationX(), ioIns.getLocationY()));
                if (ioIns.getInstruction() != null) {
                    inode.setInstruction(ioIns.getInstruction());
                    inode.getInstruction().setParameters(ioIns.getParameters());
                }
                inode.registerChangeListener(ldRungs.get(i));
                inodes.add(inode);
            }

            for (int k = 0; k < inodes.size(); k++) {
                manager.addInstruction(k, inodes.get(k));
            }
            ldRungs.get(i).redrawRung();
        }
    }

    private static void matchRungs(List<IORung> rungs, LadderDiagram ladder) {
        if (rungs.size() == 1) {
            return;
        } else {
            for (int i = 1; i < rungs.size(); i++) {
                ladder.addRung(i);
            }
        }
    }
}
