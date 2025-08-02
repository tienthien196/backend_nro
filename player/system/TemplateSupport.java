package player.system;


import java.util.ArrayList;
import java.util.List;


//integer  ko truyen vao thi = 0
// string ko truyen vao thi = null


// byte  -128 -> 127
// short -32768 -> 32767
// int -2147483648 -> 2147483647
// long -9223372036854775808 -> 9223372036854775807

public class TemplateSupport {

    public static class ItemoptionTemplate {
        public int Id; 
        public String name;
        public int type; 
        
        // neu ko co constructor nay thi se loi empty constructors
        ItemoptionTemplate(){};
        
        ItemoptionTemplate(int _id, String _name, int _type){
            Id = _id;
            name = _name;
            type = _type;
        }
    }
    public static class ItemTemplate {
    
        public short id;
        public byte type;
        public byte gender;


        public String name;
        public String description;

        public int level;
        public int smRequire;
        public int gollPrice;

        public int iconID;
        public int part;
        public int head;
        public int body;
        public int leg;

        public ItemTemplate(){

        }

        public ItemTemplate(short id, byte type, byte gender, String name, String description, short iconID, short part, boolean isUpToUp, int strRequire) {
            this.id = id;
            this.type = type;
            this.gender = gender;
            this.name = name;
            this.description = description;
            this.iconID = iconID;
            this.part = part;

            this.smRequire = strRequire;
        }

    }

    public static void main(String[] args) {
        ItemoptionTemplate itemOption = new ItemoptionTemplate();
        ItemoptionTemplate itemOption2 = new ItemoptionTemplate();
        itemOption2.Id = 2;


        System.out.println("Item :" + itemOption.type);
    }
    
    public static class MobTempplate{
        public int id;
        public short type;
        public int hp;
        public String name;

        public byte percentExp;


    }

    public static class NpcTemplate{
        public int id;
        public String name;

        public int avt;
        public int head;
        public int body;
        public int leg;
    }

    public static class MapTemplate{
        public int id;
        public byte type;
        public int planetId;

        public String name;
    }
}
