package com.protectline.jsproject.parser;

class Token {
    private final TOKEN_TYPE type;
    private final Object value;
    
    Token(TOKEN_TYPE type, String value) {
        this.type = type;
        this.value = value;
    }
    
    Token(TOKEN_TYPE type, Object value) {
        this.type = type;
        this.value = value;
    }
    
    TOKEN_TYPE getType() {
        return type;
    }
    
    String getValue() {
        return value.toString();
    }
    
    Object getObjectValue() {
        return value;
    }
    
    OpenMark getOpenMark() {
        if (type == TOKEN_TYPE.OPEN_MARK && value instanceof OpenMark) {
            return (OpenMark) value;
        }
        throw new IllegalStateException("Token is not an OPEN_MARK");
    }
    
    CloseMark getCloseMark() {
        if (type == TOKEN_TYPE.CLOSE_MARK && value instanceof CloseMark) {
            return (CloseMark) value;
        }
        throw new IllegalStateException("Token is not a CLOSE_MARK");
    }
    
    String getStringValue() {
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