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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatClient {
    private String nickname;

    private final String CONTROL_CHANNEL_NAME = "ChatManagement768624";

    private ProtocolStack protocolStack = new ProtocolStack();

    private JChannel controlChannel = new JChannel(false);
    private Map<JChannel, List<String>> channelsInfo = new HashMap<>();

    public ChatClient(String nickname){
        this.nickname = nickname;
    }

    /** Initializes protocol stack and connects to management channel */
    public void init() {
        try {
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

            controlChannel.setReceiver(new ReceiverAdapter() {
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void synchronize() {

    }
}
