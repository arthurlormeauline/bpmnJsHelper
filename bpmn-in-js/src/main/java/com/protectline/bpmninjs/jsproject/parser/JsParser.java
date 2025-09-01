package com.protectline.bpmninjs.jsproject.parser;

import com.protectline.bpmninjs.common.block.Block;

import java.util.List;
import java.util.Map;

public interface JsParser {
    
    /**
     * Parse le contenu avec les attributs fournis
     * @param content le contenu à parser
     * @param attributes les attributs extraits de la balise (ex: {"id": "230"})
     * @return le résultat du parsing
     */
    ParseResult parse(String content, Map<String, String> attributes);
    
    /**
     * Résultat du parsing contenant les blocks créés et le contenu nettoyé
     */
    class ParseResult {
        private final List<Block> blocks;
        private final String cleanedContent;
        
        public ParseResult(List<Block> blocks, String cleanedContent) {
            this.blocks = blocks;
            this.cleanedContent = cleanedContent;
        }
        
        public List<Block> getBlocks() {
            return blocks;
        }
        
        public String getCleanedContent() {
            return cleanedContent;
        }
    }
}
