package com.protectline.bpmninjs.translateunitfactory.function.fromjsprojecttoblock;

import com.protectline.bpmninjs.bpmndocument.model.BpmnPath;
import com.protectline.bpmninjs.bpmndocument.model.NodeType;
import com.protectline.bpmninjs.common.block.Block;
import com.protectline.bpmninjs.jsproject.blocksfromelement.BlockFromElement;
import com.protectline.bpmninjs.jsproject.blocksfromelement.BlockFromElementResult;
import com.protectline.bpmninjs.translateunitfactory.template.Template;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FunctionBlockFromElementBuilder implements BlockFromElement {
    
    private final Template template;
    private static final Pattern TEMPLATE_PLACEHOLDER_PATTERN = Pattern.compile("\\*\\*([^*]+)\\*\\*");
    
    public FunctionBlockFromElementBuilder(Template template) {
        this.template = template;
    }
    
    @Override
    public BlockFromElementResult parse(String content, Map<String, String> attributes) {
        List<Block> blocks = new ArrayList<>();
        
        // Template FUNCTION: "//<function id=**id**>\n**name**() {\n**content**\n}\n//</function>\n\n"
        // On extrait les placeholders **name** et **content**
        
        String functionName = extractFunctionName(content);
        String functionContent = extractFunctionContent(content);
        String functionId = attributes.get("id");
        
        if (functionName != null && functionContent != null && functionId != null) {
            // Créer le Block
            BpmnPath path = new BpmnPath(extractIdFromFunctionName(functionName));
            NodeType nodeType = determineNodeType(functionName);
            
            Block Block = new Block(path, functionName, functionContent, nodeType, functionId);
            blocks.add(Block);
        }
        
        // Retourner une chaîne vide car tout le contenu a été parsé en bloc
        return new BlockFromElementResult(blocks, "");
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
