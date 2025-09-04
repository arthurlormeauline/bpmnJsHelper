package com.protectline.bpmninjs.model.bpmndocument.homemadeimpl;



import com.protectline.bpmninjs.xmlparser.lexer.TOKEN_TYPE;
import com.protectline.bpmninjs.xmlparser.lexer.TokenDefinition;

import java.util.List;

import static com.protectline.bpmninjs.xmlparser.lexer.TOKEN_TYPE.*;


/**
 * Définition des tokens pour les fichiers BPMN XML (<element> et <?xml ...?>)
 */
public class BpmnTokenDefinition implements TokenDefinition {

    @Override
    public List<String> getTypeValues(TOKEN_TYPE type) {
        return switch (type) {
            case OPEN -> List.of("<", "<?");
            case CLOSE -> List.of(">", "?>");
            case EQUALS -> List.of("=");
            case END_SYMBOL -> List.of("/");
            case QUOTE -> List.of("\"");
            default -> throw new IllegalArgumentException("No strings for this type : " + type);
        };
    }

    @Override
    public TOKEN_TYPE getType(String str) {
        return switch (str) {
            case "<", "<?" -> OPEN;
            case ">", "?>" -> CLOSE;
            case "=" -> EQUALS;
            case "/" -> END_SYMBOL;
            case "\"" -> QUOTE;
            default -> STRING;
        };
    }
}
