package com.protectline.bpmninjs.engine.tobpmn.spi;


import com.protectline.bpmninjs.engine.tobpmn.jstoblocks.BlockFromJsElementResult;

import java.util.Map;

public interface BlockFromJsNode {
    BlockFromJsElementResult parse(String content, Map<String, String> attributes);
}
