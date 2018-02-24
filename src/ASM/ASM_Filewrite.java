package ASM;

import java.io.*;

public class ASM_Filewrite {
    public String path;
    public ASM_Filewrite(String path){
        this.path = path;
    }
    public void output(String content) throws IOException {
        File file = new File(this.path);
        FileWriter buffer = new FileWriter(file);
        buffer.write(content);
        buffer.close();

    }
}
