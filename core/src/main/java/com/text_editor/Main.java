package com.text_editor;

public class Main {
    public static void main(String[] args) {
        TextEditorService service = new TextEditorService();
        Document doc = service.createDocument("doc-1");
        CharacterStyle style = CharacterStyle.defaultStyle();
        CharacterStyle boldStyle = new CharacterStyle(true, false, "Arial", "sans-serif", 14);

        // Insert "Hello" on row 0
        service.insertCharacter(doc, 'H', 0, 0, boldStyle);
        service.insertCharacter(doc, 'e', 0, 1, style);
        service.insertCharacter(doc, 'l', 0, 2, style);
        service.insertCharacter(doc, 'l', 0, 3, style);
        service.insertCharacter(doc, 'o', 0, 4, style);

        // Insert "Hi" on row 2 (row 1 auto-created as empty)
        service.insertCharacter(doc, 'H', 2, 0, style);
        service.insertCharacter(doc, 'i', 2, 1, style);

        System.out.println("--- After inserts ---");
        System.out.println(doc.getContent());

        // Remove 'o' from row 0, col 4
        char removed = service.removeCharacter(doc, 0, 4);
        System.out.println("\nRemoved: " + removed);
        System.out.println("--- After remove ---");
        System.out.println(doc.getContent());

        // Undo the remove (brings back 'o')
        service.undoOperation(doc);
        System.out.println("\n--- After undo ---");
        System.out.println(doc.getContent());

        // Undo the 'i' insert
        service.undoOperation(doc);
        System.out.println("\n--- After second undo ---");
        System.out.println(doc.getContent());

        // Redo the 'i' insert
        service.redoOperation(doc);
        System.out.println("\n--- After redo ---");
        System.out.println(doc.getContent());
    }
}
