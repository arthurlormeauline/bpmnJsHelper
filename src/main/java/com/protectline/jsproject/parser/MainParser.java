package com.protectline.jsproject.parser;

import com.protectline.common.block.Block;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainParser implements JsParser {
    
    private final Template template;
    
    public MainParser(Template template) {
        this.template = template;
    }
    
    @Override
    public ParseResult parse(String content, Map<String, String> attributes) {
        // Le MainParser ne créé pas de blocs, il retourne juste le contenu tel quel
        // pour qu'il soit repris par la boucle principale et parsé pour trouver d'autres balises
        
        List<Block> blocks = new ArrayList<>();
        
        // Pour le template MAIN: "//<main>\n\n//<FUNCTIONS>\n//<main/>"
        // On ignore tout ce qui n'est pas entre ** dans le template
        // Ici il n'y a rien entre **, donc on retourne le contenu tel quel
        
        return new ParseResult(blocks, content);
    }
}
