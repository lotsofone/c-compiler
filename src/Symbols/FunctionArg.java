package Symbols;

public class FunctionArg{
    public String name;
    public int type;
    public int offset;
    public int size;
    public FunctionArg(String name, int type, int offset, int size){
        this.name = name;
        this.type = type;
        this.offset = offset;
        this.size = size;
    }
}