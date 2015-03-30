package zestaw2.logic;

import org.jgroups.Message;
import org.jgroups.ReceiverAdapter;
import org.jgroups.View;

public class ChatChannelReceiver extends ReceiverAdapter {

    private ChatClient chatClient;
    private String channelName;

    public ChatChannelReceiver(ChatClient chatClient, String channelName){
        this.chatClient = chatClient;
        this.channelName = channelName;
    }

    @Override
    public void viewAccepted(View view){

    }

    @Override
    public void receive(Message message){
        try {
            ChatOperationProtos.ChatMessage chatMessage = ChatOperationProtos.ChatMessage.parseFrom(message.getRawBuffer());

            // TODO: sender nick support
            chatClient.receiveMessage(channelName, chatMessage.getMessage());
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
