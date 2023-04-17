package javalette;

import javalette.Absyn.*;

public class Reg {
    
    private static int count = 0;
    
    public String Ident_;
    public String Type_;

    public Reg(String type, String ident){
        Type_ = type;
        Ident_ = ident;
    }

    public Reg(String type){
        Type_ = type;
        Ident_ = "%t" + String.valueOf(count);
        count++;
    }

    public String TypeAndIdent(){
        return Type_ + " " + Ident_;
    }

    public Type GetType(){
        if (Type_.equals("i1")) return new Bool();
        else if (Type_.equals("i32")) return new Int();
        else if (Type_.equals("double")) return new Doub();
        else return null; 
    }
}
