package com.protectline.bpmninjs.jsproject.blocksfromelement;


import com.protectline.bpmninjs.common.block.Block;

import java.util.List;
import java.util.Map;

public interface BlockFromElement {
    BlockFromElementResult parse(String content, Map<String, String> attributes);
}
