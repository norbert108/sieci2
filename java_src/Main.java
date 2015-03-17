import sieci2.chat.MulticastChat;

public class Main {

    public static void main(String args[]){
        (new MulticastChat("224.0.0.27", 9999, "janusz")).run();
    }
}
