package sieci2.jgroups;

import org.jgroups.JChannel;
import org.jgroups.Message;
import org.jgroups.ReceiverAdapter;
import org.jgroups.View;
import org.jgroups.protocols.*;
import org.jgroups.protocols.pbcast.FLUSH;
import org.jgroups.protocols.pbcast.GMS;
import org.jgroups.protocols.pbcast.NAKACK;
import org.jgroups.protocols.pbcast.STABLE;
import org.jgroups.stack.ProtocolStack;

import pl.edu.agh.dsrg.sr.chat.protos.ChatOperationProtos;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatClient {

    private final String CONTROL_CHANNEL_NAME = "ChatManagement768624";

    private ProtocolStack protocolStack = new ProtocolStack();

    private JChannel controlChannel = new JChannel(false);
    private Map<JChannel, List<String>> channelsInfo = new HashMap<>();

    public void start() throws Exception{

        controlChannel.setProtocolStack(protocolStack);
        protocolStack.addProtocol(new UDP())
                .addProtocol(new PING())
                .addProtocol(new MERGE2())
                .addProtocol(new FD_SOCK())
                .addProtocol(new FD_ALL().setValue("timeout", 12000).setValue("interval", 3000))
                .addProtocol(new VERIFY_SUSPECT())
                .addProtocol(new BARRIER())
                .addProtocol(new NAKACK())
                .addProtocol(new UNICAST2())
                .addProtocol(new STABLE())
                .addProtocol(new GMS())
                .addProtocol(new UFC())
                .addProtocol(new MFC())
                .addProtocol(new FRAG2())
                .addProtocol(new SEQUENCER())
                .addProtocol(new FLUSH());
        protocolStack.init();

        controlChannel.setReceiver(new ReceiverAdapter(){
            @Override
            public void viewAccepted(View view) {
                super.viewAccepted(view);
                System.out.println(view.toString());
            }

            public void receive(Message msg) {
                System.out.println("received msg from "
                        + msg.getSrc() + ": "
                        + msg.getObject());
            }
        });

        controlChannel.connect(CONTROL_CHANNEL_NAME);
    }

    public void synchronize(){

    }

    public static void main(String[] args){
        try{
            new ChatClient().start();

            while(true);

//            ChatOperationProtos.ChatAction chatAction = ChatOperationProtos.ChatAction.newBuilder()
//                    .setNickname("xD")
//                    .setAction(ChatOperationProtos.ChatAction.ActionType.JOIN)
//                    .setChannel("xDDDDDDDD")
//                    .build();
//            byte[] byteStream = chatAction.toByteArray();
//            this.controlChannel.send(new Message(null, null, byteStream));

        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
