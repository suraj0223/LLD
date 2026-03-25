package com.text_editor;

import java.util.HashMap;
import java.util.Map;

public class TextEditorService {
    private final Map<String, Document> documents;

    public TextEditorService() {
        this.documents = new HashMap<>();
    }

    public Document createDocument(String documentId) {
        Document doc = new Document(documentId);
        documents.put(documentId, doc);
        return doc;
    }

    public Document getDocument(String documentId) {
        Document doc = documents.get(documentId);
        if (doc == null) {
            throw new IllegalArgumentException("Document not found: " + documentId);
        }
        return doc;
    }

    public boolean insertCharacter(Document document, char character, int row, int col, CharacterStyle style) {
        return document.insert(character, row, col, style);
    }

    public char removeCharacter(Document document, int row, int col) {
        return document.remove(row, col);
    }

    public void undoOperation(Document document) {
        document.undo();
    }

    public void redoOperation(Document document) {
        document.redo();
    }
}
