package javalette;

public class Reg {
    
    private static int count = 0;
    
    public String Ident_;
    public String Type_;

    public Reg(String type){
        Type_ = type;
        Ident_ = "%t" + String.valueOf(count);
        count++;
    }

    public String TypeAndIdent(){
        return Type_ + " " + Ident_;
    }
}
