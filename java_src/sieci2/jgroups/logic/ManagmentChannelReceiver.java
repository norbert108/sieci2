package sieci2.jgroups.logic;

import com.google.protobuf.InvalidProtocolBufferException;
import org.jgroups.Address;
import org.jgroups.Message;
import org.jgroups.ReceiverAdapter;
import org.jgroups.View;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ManagmentChannelReceiver extends ReceiverAdapter {

    ChatClient chatClient = null;

    public ManagmentChannelReceiver(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @Override
    public void viewAccepted(View view) {
        // tylko jak spierdala z chata
//        super.viewAccepted(view);
        System.out.println("joined/left:");
        List<Address> members = view.getMembers();
        for (Address member : members) {
            System.out.println(member.toString());
        }
//        System.out.println(view.toString());
    }

    @Override
    public void receive(Message message) {
        System.out.println("received msg");
        try {
            ChatOperationProtos.ChatAction chatAction = ChatOperationProtos.ChatAction.parseFrom(message.getRawBuffer());
            String channelName = chatAction.getChannel();

            // get current chat state, update it
            Map<String, List<ChatOperationProtos.ChatAction>> chatState = chatClient.getState();
            if (chatAction.getAction() == ChatOperationProtos.ChatAction.ActionType.JOIN) {
                if (chatState.containsKey(channelName)) {
                    chatState.get(channelName).add(chatAction);
                } else {
                    List<ChatOperationProtos.ChatAction> membersList = new ArrayList<>();
                    membersList.add(chatAction);

                    chatState.put(channelName, membersList);
                }
            }
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getState(OutputStream output) {
        Map<String, List<ChatOperationProtos.ChatAction>> stateMap = chatClient.getState();
        synchronized (stateMap) {
            ChatOperationProtos.ChatState.Builder builder = ChatOperationProtos.ChatState.newBuilder();
            for (List<ChatOperationProtos.ChatAction> actions : stateMap.values()) {
                builder.addAllState(actions);
            }

            try {
                output.write(builder.build().toByteArray());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void setState(InputStream input) {
        try {
            super.setState(input);

            List<ChatOperationProtos.ChatAction> state = ChatOperationProtos.ChatState.parseFrom(input).getStateList();
            Map<String, List<ChatOperationProtos.ChatAction>> stateMap = new HashMap<>();
            for (ChatOperationProtos.ChatAction action : state) {
                String channelName = action.getChannel();
                if (stateMap.containsKey(channelName)) {
                    List<ChatOperationProtos.ChatAction> actions = stateMap.get(channelName);
                    actions.add(action);
                } else {
                    List<ChatOperationProtos.ChatAction> actions = new ArrayList<>();
                    actions.add(action);
                    stateMap.put(channelName, actions);
                }
            }
            chatClient.setState(stateMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
