package com.protectline.bpmninjs.application.tojsproject.stub;

import com.protectline.bpmninjs.bpmndocument.model.BpmnPath;
import com.protectline.bpmninjs.bpmndocument.model.NodeType;
import com.protectline.bpmninjs.common.block.Block;

import java.util.List;

public class StubBlock {
    public static Block getExpectedBlock(String id, String name, String script, NodeType nodeType) {
        return getExpectedBlock(id, name, script, nodeType, null);
    }

    public static Block getExpectedBlock(String id, String name, String script, NodeType nodeType, String uuid) {
        BpmnPath expectedPath = new BpmnPath(id);
        String expectedName = name;
        String expectedContent = script;
        return uuid == null ? new Block(expectedPath, expectedName, expectedContent, nodeType) :
                new Block(expectedPath, expectedName, expectedContent, nodeType, uuid);
    }

    public static List<Block> getExpectedNewBlocks() {
        var expectedDelayDefinition = getExpectedBlock("Activity_0q6wfo8", "Delay_definition_0", "NEW : delay definition script", NodeType.SCRIPT);
        var expectedGetDeviceByName_0 = getExpectedBlock("Activity_0rwauzg", "Get_device_by_name_0", "NEW : get device by name url script", NodeType.SERVICE_TASK);
        var expectedGetDeviceByName_1 = getExpectedBlock("Activity_0rwauzg", "Get_device_by_name_1", "NEW : get device by name output script", NodeType.SERVICE_TASK);
        var expectedStartEvent = getExpectedBlock("Event_1u1d1qi", "Event_1u1d1qi_0", "NEW : start event script", NodeType.START);

        return List.of(expectedDelayDefinition, expectedGetDeviceByName_0, expectedGetDeviceByName_1, expectedStartEvent);
    }


    public static List<Block> getExpectedBlocks() {
        var expectedDelayDefinition = getExpectedBlock("Activity_0q6wfo8", "Delay_definition_0", "delay definition script", NodeType.SCRIPT);
        var expectedGetDeviceByName_0 = getExpectedBlock("Activity_0rwauzg", "Get_device_by_name_0", "get device by name url script", NodeType.SERVICE_TASK);
        var expectedGetDeviceByName_1 = getExpectedBlock("Activity_0rwauzg", "Get_device_by_name_1", "get device by name output script", NodeType.SERVICE_TASK);
        var expectedStartEvent = getExpectedBlock("Event_1u1d1qi", "Event_1u1d1qi_0", "start event script", NodeType.START);

        return List.of(expectedDelayDefinition, expectedGetDeviceByName_0, expectedGetDeviceByName_1, expectedStartEvent);
    }

    public static List<Block> getExpectedBlocksWithUUID() {
        var expectedDelayDefinition = getExpectedBlock("Activity_0q6wfo8",
                "Delay_definition_0",
                "delay definition script",
                NodeType.SCRIPT,
                "880ed997-78b2-4ff6-bb02-9eeadfa6bcb7");
        var expectedGetDeviceByName_0 = getExpectedBlock("Activity_0rwauzg",
                "Get_device_by_name_0",
                "get device by name url script",
                NodeType.SERVICE_TASK,
                "74b70353-a19e-4ad7-9838-5251ab67d8a6");
        var expectedGetDeviceByName_1 = getExpectedBlock("Activity_0rwauzg",
                "Get_device_by_name_1",
                "get device by name output script",
                NodeType.SERVICE_TASK,
                "a96073f8-befb-430d-b8a0-466100ca5bc9");
        var expectedStartEvent = getExpectedBlock("Event_1u1d1qi",
                "Event_1u1d1qi_0",
                "start event script",
                NodeType.START,
                "da508040-ebc4-4267-82fb-d7d4576e136e");

        return List.of(expectedDelayDefinition, expectedGetDeviceByName_0, expectedGetDeviceByName_1, expectedStartEvent);
    }
}
