package com.text_editor;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

public class Document {
    private final String documentId;
    private final List<Line> rows;
    private final LinkedList<Operation> operations;
    private int currentIndex; // points to the last executed operation, -1 if none

    public Document(String documentId) {
        this.documentId = documentId;
        this.rows = new ArrayList<>();
        this.operations = new LinkedList<>();
        this.currentIndex = -1;
    }

    public boolean insert(char character, int row, int col, CharacterStyle style) {
        ensureRowExists(row);
        Line line = rows.get(row);
        StyledCharacter styledChar = new StyledCharacter(character, style);
        InsertOperation op = new InsertOperation(line, row, col, styledChar);

        truncateRedoHistory();
        op.execute();
        operations.add(op);
        currentIndex++;

        return true;
    }

    public char remove(int row, int col) {
        if (row < 0 || row >= rows.size()) {
            throw new IndexOutOfBoundsException("Row " + row + " does not exist");
        }
        Line line = rows.get(row);
        DeleteOperation op = new DeleteOperation(line, row, col);

        truncateRedoHistory();
        op.execute();
        operations.add(op);
        currentIndex++;

        return op.getDeletedCharacter().getValue();
    }

    public void undo() {
        if (currentIndex < 0) {
            throw new IllegalStateException("Nothing to undo");
        }
        Operation op = operations.get(currentIndex);
        op.reverse();
        currentIndex--;
    }

    public void redo() {
        if (currentIndex >= operations.size() - 1) {
            throw new IllegalStateException("Nothing to redo");
        }
        currentIndex++;
        Operation op = operations.get(currentIndex);
        op.execute();
    }

    private void ensureRowExists(int row) {
        while (rows.size() <= row) {
            rows.add(new Line());
        }
    }

    private void truncateRedoHistory() {
        // Remove all operations after currentIndex
        while (operations.size() > currentIndex + 1) {
            operations.removeLast();
        }
    }

    public String getDocumentId() {
        return documentId;
    }

    public List<Line> getRows() {
        return rows;
    }

    public String getContent() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < rows.size(); i++) {
            sb.append(rows.get(i).toString());
            if (i < rows.size() - 1) {
                sb.append("\n");
            }
        }
        return sb.toString();
    }
}
