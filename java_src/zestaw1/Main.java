package zestaw1;

import zestaw1.chat.MulticastChat;

public class Main {

    public static void main(String args[]){
        (new MulticastChat("224.0.0.27", 9999, "janusz")).run();
    }
}
