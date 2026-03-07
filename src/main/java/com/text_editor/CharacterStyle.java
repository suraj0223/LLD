package com.text_editor;

public class CharacterStyle {
    private boolean bold;
    private boolean italic;
    private String font;
    private String fontFamily;
    private int fontSize;

    public CharacterStyle(boolean bold, boolean italic, String font, String fontFamily, int fontSize) {
        this.bold = bold;
        this.italic = italic;
        this.font = font;
        this.fontFamily = fontFamily;
        this.fontSize = fontSize;
    }

    public static CharacterStyle defaultStyle() {
        return new CharacterStyle(false, false, "Arial", "sans-serif", 12);
    }

    public boolean isBold() {
        return bold;
    }

    public void setBold(boolean bold) {
        this.bold = bold;
    }

    public boolean isItalic() {
        return italic;
    }

    public void setItalic(boolean italic) {
        this.italic = italic;
    }

    public String getFont() {
        return font;
    }

    public void setFont(String font) {
        this.font = font;
    }

    public String getFontFamily() {
        return fontFamily;
    }

    public void setFontFamily(String fontFamily) {
        this.fontFamily = fontFamily;
    }

    public int getFontSize() {
        return fontSize;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    @Override
    public String toString() {
        return "CharacterStyle{bold=" + bold + ", italic=" + italic +
                ", font='" + font + "', fontFamily='" + fontFamily + "', fontSize=" + fontSize + "}";
    }
}
