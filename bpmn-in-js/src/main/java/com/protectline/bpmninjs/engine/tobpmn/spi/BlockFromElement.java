package com.protectline.bpmninjs.engine.tobpmn.spi;


import com.protectline.bpmninjs.engine.tobpmn.jstoblocks.BlockFromElementResult;

import java.util.Map;

public interface BlockFromElement {
    BlockFromElementResult parse(String content, Map<String, String> attributes);
}
