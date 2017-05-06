package com.twosigma.testing.reporter;

/**
 * @author mykola
 */
public class MessageToken {
    private String type;
    private Object value;

    public MessageToken(String type, Object value) {
        this.type = type;
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public Object getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "MessageToken{" +
                "type='" + type + '\'' +
                ", value=" + value +
                '}';
    }
}
