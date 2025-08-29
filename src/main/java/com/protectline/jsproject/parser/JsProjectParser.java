package com.protectline.jsproject.parser;

import com.protectline.bpmndocument.model.BpmnPath;
import com.protectline.bpmndocument.model.NodeType;
import com.protectline.common.block.Block;
import com.protectline.common.block.FunctionBlock;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JsProjectParser {

    private static final Pattern FUNCTION_PATTERN = Pattern.compile(
        "//<([^>]+)>\\s*\\n" +  // UUID comment
        "([\\w_]+)\\(\\)\\s*\\{\\s*\\n" +  // Function name
        "(.*?)\\n" +  // Function content
        "\\}\\s*\\n" +  // End of function
        "//<\\1/>",  // Closing UUID comment
        Pattern.DOTALL
    );

    public List<Block> parseJsToBlocks(String jsContent) {
        List<Block> blocks = new ArrayList<>();
        
        Matcher matcher = FUNCTION_PATTERN.matcher(jsContent);
        
        while (matcher.find()) {
            String uuid = matcher.group(1);
            String functionName = matcher.group(2);
            String content = matcher.group(3).trim();
            
            // Extract ID from function name (remove suffix like _0, _1)
            String id = extractIdFromFunctionName(functionName);
            
            // Determine node type based on function name patterns
            NodeType nodeType = determineNodeType(functionName);
            
            BpmnPath path = new BpmnPath(id);
            FunctionBlock block = new FunctionBlock(path, functionName, content, nodeType, uuid);
            blocks.add(block);
        }
        
        return blocks;
    }
    
    private String extractIdFromFunctionName(String functionName) {
        // Remove trailing _0, _1, etc. to get the base ID
        return functionName.replaceAll("_\\d+$", "");
    }
    
    private NodeType determineNodeType(String functionName) {
        // Simple heuristic based on function name patterns
        if (functionName.toLowerCase().contains("event")) {
            return NodeType.START;
        } else if (functionName.toLowerCase().contains("script") || 
                   functionName.toLowerCase().contains("delay")) {
            return NodeType.SCRIPT;
        } else {
            return NodeType.SERVICE_TASK;
        }
    }
}
