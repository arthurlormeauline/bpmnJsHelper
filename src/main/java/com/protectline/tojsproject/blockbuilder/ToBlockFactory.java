package com.protectline.tojsproject.blockbuilder;

import com.protectline.bpmndocument.model.element.Element;

public class ToBlockFactory {
    public static ToBlock getToBlock(Element element){
        switch (element.getType()){
            case SCRIPT:
                return new ScriptToBlock();
            default:
                throw new IllegalStateException("Unexpected value: " + element.getType());
        }
    }
}
