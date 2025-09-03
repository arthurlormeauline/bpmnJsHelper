package com.protectline.bpmninjs.application.tobpmn.spi;


import com.protectline.bpmninjs.application.tobpmn.jstoblocks.BlockFromElementResult;

import java.util.Map;

public interface BlockFromElement {
    BlockFromElementResult parse(String content, Map<String, String> attributes);
}
