package com.protectline.bpmninjs.xmlparser;

import java.util.List;

/**
 * Interface pour définir les patterns de tokens selon le type de contenu à parser
 * Supporte maintenant plusieurs symboles possibles pour un même type de token
 */
public interface TokenDefinition {
    
    /**
     * Retourne la liste des symboles possibles pour un type de token donné
     * Par exemple, OPEN pourrait retourner ["<", "<?"] pour BPMN
     */
    List<String> getTypeValues(TOKEN_TYPE type);
    
    /**
     * Détermine le type de token à partir d'un symbole
     */
    TOKEN_TYPE getType(String str);

    /**
     * Méthode de compatibilité - retourne le premier symbole pour un type
     * @deprecated Utiliser getTypeValues() à la place
     */
    @Deprecated
    default String getTypeValue(TOKEN_TYPE type) {
        List<String> values = getTypeValues(type);
        return values.isEmpty() ? null : values.get(0);
    }

    default String extractTokenString(String content, int position, int length) {
        return content.substring(position, position + length);
    }
}
