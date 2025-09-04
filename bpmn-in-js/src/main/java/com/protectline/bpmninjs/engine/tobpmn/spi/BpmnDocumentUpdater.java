package com.protectline.bpmninjs.engine.tobpmn.spi;

import com.protectline.bpmninjs.model.bpmndocument.api.BpmnDocument;

public interface BpmnDocumentUpdater {
    void updateDocument(BpmnDocument document);
}
