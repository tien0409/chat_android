package com.example.chatapp.adapters;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.chatapp.databinding.ItemContainerReceivedMessageBinding;
import com.example.chatapp.databinding.ItemContainerSentMessageBinding;
import com.example.chatapp.responses.message.MessageItem;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int VIEW_TYPE_SENT = 1;
    public static final int VIEW_TYPE_RECEIVED = 2;
    private final List<MessageItem> messageItemList;
    private final Bitmap receiverProfileImage;
    private final String sender;

    public ChatAdapter(List<MessageItem> messageItemList, Bitmap receiverProfileImage, String senderId) {
        this.messageItemList = messageItemList;
        this.receiverProfileImage = receiverProfileImage;
        this.sender = senderId;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_SENT) {
            return new SentMessageViewHolder(
                    ItemContainerSentMessageBinding.inflate(
                            LayoutInflater.from(parent.getContext()),
                            parent,
                            false
                    )
            );
        } else {
            return new ReceivedMessageViewHolder(
                    ItemContainerReceivedMessageBinding.inflate(
                            LayoutInflater.from(parent.getContext()),
                            parent,
                            false
                    )
            );
        }
    }

    @Override
    public void onBindViewHolder(@NotNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == VIEW_TYPE_SENT) {
            ((SentMessageViewHolder) holder).setData(messageItemList.get(position));
        } else {
            ((ReceivedMessageViewHolder) holder).setData(messageItemList.get(position), receiverProfileImage);
        }
    }

    @Override
    public int getItemCount() {
        return messageItemList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (messageItemList.get(position).getSender().equals(sender)) {
            return VIEW_TYPE_SENT;
        }
        return VIEW_TYPE_RECEIVED;
    }

    static class SentMessageViewHolder extends RecyclerView.ViewHolder {
        private final ItemContainerSentMessageBinding binding;

        SentMessageViewHolder(ItemContainerSentMessageBinding itemContainerSentMessageBinding) {
            super(itemContainerSentMessageBinding.getRoot());
            binding = itemContainerSentMessageBinding;
        }

        void setData(MessageItem messageItem) {
            binding.txtMessage.setText(messageItem.getContent());
            binding.txtSendAt.setText(messageItem.getCreatedAt());
        }
    }

    static class ReceivedMessageViewHolder extends RecyclerView.ViewHolder {
        private final ItemContainerReceivedMessageBinding binding;

        ReceivedMessageViewHolder(ItemContainerReceivedMessageBinding itemContainerReceivedMessageBinding) {
            super(itemContainerReceivedMessageBinding.getRoot());
            binding = itemContainerReceivedMessageBinding;
        }

        void setData(MessageItem messageItem, Bitmap receiverProfileImage) {
            binding.txtMessage.setText(messageItem.getContent());
            binding.txtSendAt.setText(messageItem.getCreatedAt());
            binding.imageProfile.setImageBitmap(receiverProfileImage);
        }
    }
}
