package zestaw2.logic;

import com.google.protobuf.InvalidProtocolBufferException;
import org.jgroups.Address;
import org.jgroups.Message;
import org.jgroups.ReceiverAdapter;
import org.jgroups.View;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;

public class ManagmentChannelReceiver extends ReceiverAdapter {

    ChatClient chatClient = null;

    public ManagmentChannelReceiver(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @Override
    public void viewAccepted(View view){
        //TODO: remove disconnected clients and empty channels
        List<Address> membersList = view.getMembers();
        for (Address member : membersList) {
            System.out.println(member.toString());
        }

        System.out.println("Client joined/left!");
    }

    @Override
    public void receive(Message message) {
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
            } else if (chatAction.getAction() == ChatOperationProtos.ChatAction.ActionType.LEAVE){
                List<ChatOperationProtos.ChatAction> membersList = chatState.get(chatAction.getChannel());
                membersList.remove(chatAction);
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
