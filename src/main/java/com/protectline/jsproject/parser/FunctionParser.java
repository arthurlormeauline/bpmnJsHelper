package com.protectline.jsproject.parser;

import com.protectline.bpmndocument.model.BpmnPath;
import com.protectline.bpmndocument.model.NodeType;
import com.protectline.common.block.Block;
import com.protectline.common.block.FunctionBlock;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FunctionParser implements JsParser {
    
    private final Template template;
    private static final Pattern TEMPLATE_PLACEHOLDER_PATTERN = Pattern.compile("\\*\\*([^*]+)\\*\\*");
    
    public FunctionParser(Template template) {
        this.template = template;
    }
    
    @Override
    public ParseResult parse(String content, Map<String, String> attributes) {
        List<Block> blocks = new ArrayList<>();
        
        // Template FUNCTION: "//<function id=**id**>\n**name**() {\n**content**\n}\n//<function/>\n\n"
        // On extrait les placeholders **name** et **content**
        
        String functionName = extractFunctionName(content);
        String functionContent = extractFunctionContent(content);
        String functionId = attributes.get("id");
        
        if (functionName != null && functionContent != null && functionId != null) {
            // Créer le FunctionBlock
            BpmnPath path = new BpmnPath(extractIdFromFunctionName(functionName));
            NodeType nodeType = determineNodeType(functionName);
            
            FunctionBlock functionBlock = new FunctionBlock(path, functionName, functionContent, nodeType, functionId);
            blocks.add(functionBlock);
        }
        
        // Retourner une chaîne vide car tout le contenu a été parsé en bloc
        return new ParseResult(blocks, "");
    }
    
    private String extractFunctionName(String content) {
        // Cherche le pattern: functionName() {
        Pattern namePattern = Pattern.compile("([\\w_]+)\\(\\)\\s*\\{");
        Matcher matcher = namePattern.matcher(content);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }
    
    private String extractFunctionContent(String content) {
        // Extrait le contenu entre les accolades de la fonction
        int firstBrace = content.indexOf('{');
        int lastBrace = content.lastIndexOf('}');
        
        if (firstBrace != -1 && lastBrace != -1 && firstBrace < lastBrace) {
            return content.substring(firstBrace + 1, lastBrace).trim();
        }
        return null;
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
