package com.protectline.tojsproject;

import com.protectline.bpmndocument.BpmnDocument;
import com.protectline.jsproject.model.block.Block;
import org.javatuples.Pair;
import org.w3c.dom.Node;

import java.util.Collection;
import java.util.List;

public interface NodeToBlock {
    Collection<? extends Block> getBlocks(Node node, BpmnDocument bpmnDocument, Pair<String, List<Integer>> path);
}
