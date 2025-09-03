package com.protectline.bpmninjs.application.tobpmn.spi;

import com.protectline.bpmninjs.bpmndocument.BpmnDocument;

public interface DocumentUpdater {
    void updateDocument(BpmnDocument document);
}
