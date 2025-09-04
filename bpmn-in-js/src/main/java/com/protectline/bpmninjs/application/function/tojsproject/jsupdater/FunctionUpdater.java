package com.protectline.bpmninjs.application.function.tojsproject.jsupdater;

import com.protectline.bpmninjs.model.common.block.Block;
import com.protectline.bpmninjs.model.common.block.BlockType;
import com.protectline.bpmninjs.engine.tojsproject.spi.JsUpdater;
import com.protectline.bpmninjs.model.template.Template;

import java.text.Normalizer;
import java.util.List;
import java.util.regex.Pattern;

public class FunctionUpdater implements JsUpdater {

    private final Template template;

    public FunctionUpdater(Template template) {
        this.template = template;
    }

    @Override
    public String update(String input, List<Block> blocks) {
        var functions = buildFunctions(blocks);
        return input.replace(template.getFlag(), functions);
    }

    private String buildFunctions(List<Block> blocks) {
        StringBuilder allFunction = new StringBuilder();
        blocks.forEach(block ->
        {
            if (block.getType().equals(BlockType.FUNCTION)) {
                var function = buildFunction((Block) block);
                allFunction.append(function);
            }
        });

        return allFunction.toString();
    }

    private String buildFunction(Block block) {
        var function = template.getTemplate();
        function = function.replace("**id**", block.getId().toString());
        function = function.replace("**name**", normalizeJavaScriptFunctionName(block.getName().toString()));
        function = function.replace("**content**", block.getContent().toString());
        return function;
    }
    
    /**
     * Normalise un nom pour qu'il soit valide en tant que nom de fonction JavaScript
     * - Supprime les accents
     * - Supprime les caractères spéciaux
     * - Remplace les espaces par des underscores
     * - S'assure que le nom commence par une lettre ou underscore
     */
    private String normalizeJavaScriptFunctionName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return "unnamed_function";
        }
        
        // Supprimer les accents en décomposant les caractères Unicode puis en supprimant les diacritiques
        String normalized = Normalizer.normalize(name.trim(), Normalizer.Form.NFD);
        normalized = Pattern.compile("\\p{InCombiningDiacriticalMarks}+").matcher(normalized).replaceAll("");
        
        // Remplacer les caractères spéciaux et les espaces par des underscores
        normalized = normalized.replaceAll("[^a-zA-Z0-9_]", "_");
        
        // Supprimer les underscores multiples consécutifs
        normalized = normalized.replaceAll("_+", "_");
        
        // Supprimer les underscores en début et fin
        normalized = normalized.replaceAll("^_+|_+$", "");
        
        // S'assurer que le nom commence par une lettre ou un underscore et n'est pas vide
        if (normalized.isEmpty() || Character.isDigit(normalized.charAt(0))) {
            normalized = "func_" + normalized;
        }
        
        return normalized;
    }
}
