package com.protectline.bpmninjs.application.tojsproject.stub;

import com.protectline.bpmninjs.bpmndocument.model.BpmnPath;
import com.protectline.bpmninjs.bpmndocument.model.NodeType;
import com.protectline.bpmninjs.common.block.Block;

import java.util.List;
import java.util.Objects;

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

    /**
     * Compare deux blocs en excluant l'UUID (utile pour les tests)
     */
    public static boolean equalsIgnoringId(Block block1, Block block2) {
        if (block1 == block2) return true;
        if (block1 == null || block2 == null) return false;
        
        return Objects.equals(block1.getPath(), block2.getPath()) &&
               Objects.equals(block1.getName(), block2.getName()) &&
               Objects.equals(block1.getContent(), block2.getContent()) &&
               Objects.equals(block1.getType(), block2.getType()) &&
               Objects.equals(block1.getNodeType(), block2.getNodeType()) &&
               Objects.equals(block1.getScriptIndex(), block2.getScriptIndex());
    }

    /**
     * Compare deux listes de blocs en excluant les UUIDs
     */
    public static boolean equalsIgnoringId(List<Block> list1, List<Block> list2) {
        if (list1 == list2) return true;
        if (list1 == null || list2 == null) return false;
        if (list1.size() != list2.size()) return false;
        
        for (int i = 0; i < list1.size(); i++) {
            if (!equalsIgnoringId(list1.get(i), list2.get(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Debug - compare et affiche les différences entre deux blocs
     */
    public static void debugBlockComparison(Block block1, Block block2) {
        System.out.println("=== DEBUG BLOCK COMPARISON ===");
        System.out.println("Block1 ID: " + block1.getId());
        System.out.println("Block2 ID: " + block2.getId());
        
        System.out.println("Path equals: " + Objects.equals(block1.getPath(), block2.getPath()));
        System.out.println("  Block1 path: '" + block1.getPath() + "'");
        System.out.println("  Block2 path: '" + block2.getPath() + "'");
        
        System.out.println("Name equals: " + Objects.equals(block1.getName(), block2.getName()));
        System.out.println("  Block1 name: '" + block1.getName() + "' (length: " + (block1.getName() != null ? block1.getName().length() : "null") + ")");
        System.out.println("  Block2 name: '" + block2.getName() + "' (length: " + (block2.getName() != null ? block2.getName().length() : "null") + ")");
        
        System.out.println("Content equals: " + Objects.equals(block1.getContent(), block2.getContent()));
        System.out.println("Type equals: " + Objects.equals(block1.getType(), block2.getType()));
        System.out.println("NodeType equals: " + Objects.equals(block1.getNodeType(), block2.getNodeType()));
        System.out.println("ScriptIndex equals: " + Objects.equals(block1.getScriptIndex(), block2.getScriptIndex()));
        System.out.println("===============================");
    }
}
