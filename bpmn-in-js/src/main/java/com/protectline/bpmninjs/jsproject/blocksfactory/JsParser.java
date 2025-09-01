package com.protectline.bpmninjs.jsproject.blocksfactory;


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
}
