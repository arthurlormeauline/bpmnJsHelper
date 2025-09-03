package com.protectline.bpmninjs.application.tobpmn.jstoblocks;


import java.util.Map;

public interface BlockFromElement {
    BlockFromElementResult parse(String content, Map<String, String> attributes);
}
