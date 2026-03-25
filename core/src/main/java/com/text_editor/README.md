## Design a Text Editor

### Functionality support
  - Undo
  - Redo
  - Add a character at any position
  - Remove a character at any position
  - Each character can have different font, family & size, bold, italic
  - If the row being inserted into does not exist, add a new row and place the character at the given location

### Core entities

- TextEditorService
- Document
- Line
- StyledCharacter
- CharacterStyle
- Operation (interface) — InsertOperation, DeleteOperation

### Relationships

- Document (1) -> Line (n)
- Line (1) -> StyledCharacter (n)
- StyledCharacter (1) -> CharacterStyle (1)
- Document (1) -> Operation (n) — via doubly linked list for undo/redo

### Class diagram

```txt
TextEditorService
- documents: List<Document>

+ getDocument(documentId): Document
+ insertCharacter(document: Document, character: char, row: int, col: int, style: CharacterStyle): boolean
+ removeCharacter(document: Document, row: int, col: int): char
+ undoOperation(document: Document): void
+ redoOperation(document: Document): void

Document
- documentId: String
- rows: List<Line>
- operations: DoublyLinkedList<Operation>
- currentOperation: Node<Operation>   // pointer into the linked list

+ undo(): void                        // move currentOperation backward, call operation.reverse()
+ redo(): void                        // move currentOperation forward, call operation.execute()
+ insert(character: char, row: int, col: int, style: CharacterStyle): boolean
    // if row >= rows.size(), add new rows until row exists
    // create InsertOperation, truncate any redo history after currentOperation, execute & append
+ remove(row: int, col: int): char
    // create DeleteOperation, truncate any redo history after currentOperation, execute & append

Line
- characters: List<StyledCharacter>

+ insertAt(col: int, styledChar: StyledCharacter): void
+ removeAt(col: int): StyledCharacter
+ getCharacters(): List<StyledCharacter>

StyledCharacter
- value: char
- style: CharacterStyle

CharacterStyle
- bold: boolean
- italic: boolean
- font: String
- fontFamily: String
- fontSize: int

Operation (interface)
+ execute(): void
+ reverse(): void

InsertOperation implements Operation
- document: Document
- row: int
- col: int
- styledCharacter: StyledCharacter

+ execute(): void   // inserts the character at (row, col)
+ reverse(): void   // removes the character at (row, col)

DeleteOperation implements Operation
- document: Document
- row: int
- col: int
- styledCharacter: StyledCharacter   // stored on execute so reverse() can re-insert

+ execute(): void   // removes the character at (row, col), stores it
+ reverse(): void   // re-inserts the stored character at (row, col)
```