package com.protectline.bpmninjs.engine.tobpmn.spi;

import com.protectline.bpmninjs.model.bpmndocument.api.BpmnDocument;

public interface DocumentUpdater {
    void updateDocument(BpmnDocument document);
}
