package com.protectline.bpmninjs.jsproject.blocksfactory;

import com.protectline.bpmninjs.common.block.Block;

import java.util.List;

/**
 * Interface publique pour le parser JS Project
 */
public interface JsProjectParserInterface {
    
    /**
     * Parse le contenu JS en blocs selon l'architecture lexer/parser
     * @param jsContent le contenu JavaScript à parser
     * @return la liste des blocs parsés
     */
    List<Block> parseJsToBlocks(String jsContent);
}
