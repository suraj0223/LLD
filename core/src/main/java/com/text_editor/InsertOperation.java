package com.text_editor;

public class InsertOperation implements Operation {
    private final Line line;
    private final int row;
    private final int col;
    private final StyledCharacter styledCharacter;

    public InsertOperation(Line line, int row, int col, StyledCharacter styledCharacter) {
        this.line = line;
        this.row = row;
        this.col = col;
        this.styledCharacter = styledCharacter;
    }

    @Override
    public void execute() {
        line.insertAt(col, styledCharacter);
    }

    @Override
    public void reverse() {
        line.removeAt(col);
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }
}
