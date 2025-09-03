package com.protectline.bpmninjs.jsproject.blocksfactory;


import java.util.Map;

public interface BlockFromElement {
    BlockFromElementResult parse(String content, Map<String, String> attributes);
}
