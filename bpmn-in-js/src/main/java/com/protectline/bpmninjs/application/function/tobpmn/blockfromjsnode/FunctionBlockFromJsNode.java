package com.protectline.bpmninjs.application.function.tobpmn.blockfromjsnode;

import com.protectline.bpmninjs.model.bpmndocument.api.model.BpmnPath;
import com.protectline.bpmninjs.model.bpmndocument.api.model.NodeType;
import com.protectline.bpmninjs.model.block.Block;
import com.protectline.bpmninjs.engine.tobpmn.spi.BlockFromJsNode;
import com.protectline.bpmninjs.engine.tobpmn.jstoblocks.BlockFromJsElementResult;
import com.protectline.bpmninjs.model.template.Template;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FunctionBlockFromJsNode implements BlockFromJsNode {
    
    private final Template template;
    private static final Pattern TEMPLATE_PLACEHOLDER_PATTERN = Pattern.compile("\\*\\*([^*]+)\\*\\*");
    
    public FunctionBlockFromJsNode(Template template) {
        this.template = template;
    }
    
    @Override
    public BlockFromJsElementResult parse(String jsElement, Map<String, String> attributes) {
        List<Block> blocks = new ArrayList<>();
        
        // Template FUNCTION: "//<function id=**id**>\n**name**() {\n**content**\n}\n//</function>\n\n"
        // On extrait les placeholders **name** et **content**
        
        String functionName = extractFunctionName(jsElement);
        String functionContent = extractFunctionContent(jsElement);
        String functionId = attributes.get("id");
        
        if (functionName != null && functionContent != null && functionId != null) {
            BpmnPath path = new BpmnPath(extractIdFromFunctionName(functionName));
            NodeType nodeType = determineNodeType(functionName);
            
            Block Block = new Block(path, functionName, functionContent, nodeType, functionId);
            blocks.add(Block);
        }
        
        return new BlockFromJsElementResult(blocks, "");
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
            String rawContent = content.substring(firstBrace + 1, lastBrace);
            // Le template ajoute un retour à la ligne après '{' et avant '}' 
            // On doit enlever le premier et le dernier retour à la ligne (quelque soit l'encodage)
            rawContent = rawContent.replaceFirst("^\\R", ""); // Enlève le premier retour à la ligne
            rawContent = rawContent.replaceFirst("\\R$", ""); // Enlève le dernier retour à la ligne
            return rawContent;
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
