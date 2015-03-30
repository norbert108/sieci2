package zestaw2.logic;

import org.jgroups.*;
import org.jgroups.protocols.*;
import org.jgroups.protocols.pbcast.*;
import org.jgroups.stack.ProtocolStack;
import zestaw2.gui.ChatClientUI;

import java.net.InetAddress;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class ChatClient {
    private String nickname;
    private DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    private final String CONTROL_CHANNEL_NAME = "ChatManagement768624";

    private JChannel controlChannel = new JChannel(false);

    private Map<String, List<ChatOperationProtos.ChatAction>> channelsInfo = new HashMap<>();
    private Map<String, JChannel> openedChannels = new HashMap<>(); //map of channels current client is connected to
    private Map<String, ChatClientUI> chatInstances = new HashMap<>();

    public ChatClient(String nickname) throws Exception {
        this.nickname = nickname;

        try {
            controlChannel = openChannel(CONTROL_CHANNEL_NAME, null, new ManagmentChannelReceiver(this));
            controlChannel.getState(null, 10000);

            // check if supplied nickname is unique in chat
            View controlChannelView = controlChannel.getView();
            List<Address> membersList = controlChannelView.getMembers();
            List<String> memberStringList = new ArrayList<>();
            for (Address member : membersList) {
                if (!memberStringList.contains(member.toString())) {
                    memberStringList.add(member.toString());
                } else {
                    controlChannel.disconnect();
                    throw new Exception("Nickname in use");
                }
            }
        } catch (Exception e) {
            if (e.getMessage().equals("Nickname in use")) throw e;
            else {
                e.printStackTrace();
            }
        }
    }

    public String getNickname() {
        return nickname;
    }

    public Map<String, List<ChatOperationProtos.ChatAction>> getState() {
        return channelsInfo;
    }

    public void setState(Map<String, List<ChatOperationProtos.ChatAction>> state) {
        synchronized (this.channelsInfo) {
            for (Map.Entry<String, List<ChatOperationProtos.ChatAction>> entry : state.entrySet()) {
                if (this.channelsInfo.containsKey(entry.getKey())) {
                    this.channelsInfo.get(entry.getKey()).addAll(entry.getValue());
                } else {
                    this.channelsInfo.put(entry.getKey(), entry.getValue());
                }
            }
        }
    }

    private JChannel openChannel(String channelName, String hostName, ReceiverAdapter receiver) throws Exception {

        JChannel channel = new JChannel(false);
        ProtocolStack protocolStack = new ProtocolStack();

        channel.setProtocolStack(protocolStack);
        UDP udp = new UDP();
        if (hostName != null) {
            udp.setValue("mcast_group_addr", InetAddress.getByName(hostName));
        }

        protocolStack.addProtocol(udp)
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
                .addProtocol(new STATE_TRANSFER())
                .addProtocol(new FLUSH());
        protocolStack.init();

        channel.setReceiver(receiver);
        channel.setName(nickname);
        channel.connect(channelName);

        return channel;
    }

    public void joinChannel(String channelName) throws Exception {
        JChannel channel = openChannel(channelName, channelName, new ChatChannelReceiver(this, channelName));
        openedChannels.put(channelName, channel);

        // TODO: refactor
        ChatClientUI chatClientUI = new ChatClientUI(this);
        chatClientUI.setChannelName(channelName);
        chatInstances.put(channelName, chatClientUI);

        sendClientStatus(channelName, nickname, ChatOperationProtos.ChatAction.ActionType.JOIN);
    }

    public void sendClientStatus(String channelName, String nickname, ChatOperationProtos.ChatAction.ActionType action) {
        ChatOperationProtos.ChatAction chatAction = ChatOperationProtos.ChatAction.newBuilder()
                .setChannel(channelName)
                .setNickname(nickname)
                .setAction(action)
                .build();

        Message message = new Message(null, null, chatAction.toByteArray());
        try {
            controlChannel.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Receive message from channel
     */
    public void receiveMessage(String channelName, String nickname, String message) {
        ChatClientUI chatClientUI = this.chatInstances.get(channelName);

        String time = this.dateFormat.format(new Date());
        chatClientUI.displayClientMessage(nickname, time, message);
    }

    /**
     * Send message to all channels clinet is connected to
     */
    public void sendMessage(String channelName, String messageText) {
        JChannel channel = this.openedChannels.get(channelName);

        if (channel != null) {
            ChatOperationProtos.ChatMessage chatMessage = ChatOperationProtos.ChatMessage.newBuilder().setMessage(messageText).build();
            Message message = new Message(null, null, chatMessage.toByteArray());
            try {
                channel.send(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
