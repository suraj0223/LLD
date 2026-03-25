package com.text_editor;

public class StyledCharacter {
    private final char value;
    private CharacterStyle style;

    public StyledCharacter(char value, CharacterStyle style) {
        this.value = value;
        this.style = style;
    }

    public char getValue() {
        return value;
    }

    public CharacterStyle getStyle() {
        return style;
    }

    public void setStyle(CharacterStyle style) {
        this.style = style;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
