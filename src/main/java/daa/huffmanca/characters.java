package daa.huffmanca;

public class characters {
    private char character;
    private int frequency;
    private String code;

    public characters(){
        this.character=0;
        this.frequency=0;
        this.code="";
    }
    public characters(char character, int frequency, String code){
        this.character=character;
        this.frequency=frequency;
        this.code=code;
    }

    public char getCharacter() {
        return character;
    }
    public void setCharacter(char character) {
        this.character = character;
    }
    public int getFrequency() {
        return frequency;
    }
    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
}
