package com.protectline.bpmndocument.model.camundaadapter;

import com.protectline.bpmndocument.BpmnDocument;
import com.protectline.bpmndocument.model.Node;
import com.protectline.bpmndocument.model.ScriptNode;
import com.protectline.bpmndocument.model.ServiceTaskNode;
import com.protectline.bpmndocument.model.StartNode;
import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.instance.ScriptTask;
import org.camunda.bpm.model.bpmn.instance.ServiceTask;
import org.camunda.bpm.model.bpmn.instance.StartEvent;
import org.camunda.bpm.model.xml.instance.ModelElementInstance;
import org.camunda.bpm.model.xml.type.ModelElementType;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class BpmnCamundaDocument implements BpmnDocument {
    private BpmnModelInstance modelInstance = null;

    public BpmnCamundaDocument(Path workingDirectory, String processName) {
        File processFile = workingDirectory.resolve("input").resolve(processName + ".bpmn").toFile();
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
}
