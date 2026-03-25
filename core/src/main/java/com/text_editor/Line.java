package com.text_editor;

import java.util.ArrayList;
import java.util.List;

public class Line {
    private final List<StyledCharacter> characters;

    public Line() {
        this.characters = new ArrayList<>();
    }

    public void insertAt(int col, StyledCharacter styledChar) {
        if (col > characters.size()) {
            // Pad with spaces if col is beyond current length
            while (characters.size() < col) {
                characters.add(new StyledCharacter(' ', CharacterStyle.defaultStyle()));
            }
        }
        characters.add(col, styledChar);
    }

    public StyledCharacter removeAt(int col) {
        if (col < 0 || col >= characters.size()) {
            throw new IndexOutOfBoundsException("Column " + col + " is out of bounds for line of length " + characters.size());
        }
        return characters.remove(col);
    }

    public StyledCharacter getCharAt(int col) {
        if (col < 0 || col >= characters.size()) {
            throw new IndexOutOfBoundsException("Column " + col + " is out of bounds for line of length " + characters.size());
        }
        return characters.get(col);
    }

    public List<StyledCharacter> getCharacters() {
        return characters;
    }

    public int length() {
        return characters.size();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (StyledCharacter sc : characters) {
            sb.append(sc.getValue());
        }
        return sb.toString();
    }
}
