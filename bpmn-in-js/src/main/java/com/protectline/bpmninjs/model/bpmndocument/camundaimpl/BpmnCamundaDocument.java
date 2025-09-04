package com.protectline.bpmninjs.model.bpmndocument.camundaimpl;

import com.protectline.bpmninjs.model.bpmndocument.api.BpmnDocument;
import com.protectline.bpmninjs.model.bpmndocument.api.model.Node;
import com.protectline.bpmninjs.model.bpmndocument.api.model.ScriptNode;
import com.protectline.bpmninjs.model.bpmndocument.api.model.ServiceTaskNode;
import com.protectline.bpmninjs.model.bpmndocument.api.model.StartNode;
import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.instance.ScriptTask;
import org.camunda.bpm.model.bpmn.instance.ServiceTask;
import org.camunda.bpm.model.bpmn.instance.StartEvent;
import org.camunda.bpm.model.xml.instance.ModelElementInstance;
import org.camunda.bpm.model.xml.type.ModelElementType;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import static com.protectline.bpmninjs.model.bpmndocument.camundaimpl.DomElementUtil.getScriptsAsDomElement;

public class BpmnCamundaDocument implements BpmnDocument {
    private BpmnModelInstance modelInstance = null;

    public BpmnCamundaDocument(File processFile) throws IOException {
        formatBpmnFile(processFile);
        this.modelInstance = Bpmn.readModelFromFile(processFile);
    }


    @Override
    public List<Node> getFirstLevelElements() {
        List<Node> nodes = new ArrayList<>();

        nodes.addAll(getScriptNodes());
        nodes.addAll(getServiceTaskNode());
        nodes.addAll(getStartNode());

        return nodes;
    }

    private List<? extends StartNode> getStartNode() {
        return getModelElementInstance(StartEvent.class).stream()
                .map(modelElementInstance -> new CamundaStartNode(modelElementInstance))
                .toList();
    }

    private List<? extends ServiceTaskNode> getServiceTaskNode() {
        return getModelElementInstance(ServiceTask.class).stream()
                .map(modelElementInstance -> new CamundaServiceTaskNode(modelElementInstance))
                .toList();
    }

    private List<? extends ScriptNode> getScriptNodes() {
        return getModelElementInstance(ScriptTask.class).stream()
                .map(modelElementInstance -> new CamundaScriptNode(modelElementInstance))
                .toList();
    }

    private List<ModelElementInstance> getModelElementInstance(Class<? extends ModelElementInstance> aClass) {
        ModelElementType taskType = modelInstance.getModel().getType(aClass);
        return modelInstance.getModelElementsByType(taskType).stream().toList();
    }

    @Override
    public void setScript(String elementId, Integer scriptIndex, String content) {
        getScriptsAsDomElement(modelInstance.getModelElementById(elementId).getDomElement())
                .get(scriptIndex)
                .setTextContent(content);
    }

    @Override
    public void writeToFile(File file) throws IOException {
       Bpmn.writeModelToFile(file, modelInstance);
       formatBpmnFile(file);
    }

    private static void formatBpmnFile(File file) throws IOException {
        String newContentWithoutEmptyLines = Files.readString(file.toPath());
        Files.writeString(file.toPath(), newContentWithoutEmptyLines);
    }
}
