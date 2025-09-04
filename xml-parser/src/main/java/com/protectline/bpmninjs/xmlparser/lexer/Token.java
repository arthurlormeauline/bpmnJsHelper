package com.protectline.bpmninjs.xmlparser.lexer;

import com.protectline.bpmninjs.xmlparser.parser.CloseMark;
import com.protectline.bpmninjs.xmlparser.parser.OpenMark;
import com.protectline.bpmninjs.xmlparser.parser.SelfCloseMark;

public class Token {
    private final TOKEN_TYPE type;
    private final Object value;
    
    public Token(TOKEN_TYPE type, String value) {
        this.type = type;
        this.value = value;
    }
    
    public Token(TOKEN_TYPE type, Object value) {
        this.type = type;
        this.value = value;
    }
    
    public TOKEN_TYPE getType() {
        return type;
    }
    
    public String getValue() {
        return value.toString();
    }
    
    Object getObjectValue() {
        return value;
    }
    
    public OpenMark getOpenMark() {
        if (type == TOKEN_TYPE.OPEN_MARK && value instanceof OpenMark) {
            return (OpenMark) value;
        }
        throw new IllegalStateException("Token is not an OPEN_MARK");
    }
    
    public CloseMark getCloseMark() {
        if (type == TOKEN_TYPE.CLOSE_MARK && value instanceof CloseMark) {
            return (CloseMark) value;
        }
        throw new IllegalStateException("Token is not a CLOSE_MARK");
    }
    
    public SelfCloseMark getSelfCloseMark() {
        if (type == TOKEN_TYPE.SELF_CLOSE_MARK && value instanceof SelfCloseMark) {
            return (SelfCloseMark) value;
        }
        throw new IllegalStateException("Token is not a SELF_CLOSE_MARK");
    }
    
    public String getStringValue() {
        if (value instanceof String) {
            return (String) value;
        }
        return value.toString();
    }
    
    @Override
    public String toString() {
        return "Token{" +
                "type=" + type +
                ", value=" + value +
                '}';
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Token token = (Token) o;
        return type == token.type && value.equals(token.value);
    }
    
    @Override
    public int hashCode() {
        return type.hashCode() * 31 + value.hashCode();
    }
}
