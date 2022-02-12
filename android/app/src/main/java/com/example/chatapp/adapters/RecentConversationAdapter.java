package com.example.chatapp.adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.chatapp.databinding.ItemContainerRecentConversationBinding;
import com.example.chatapp.listeners.ConversationListener;
import com.example.chatapp.responses.conversation.ConversationItem;
import com.example.chatapp.responses.user.DataItem;

import java.util.List;

public class RecentConversationAdapter extends RecyclerView.Adapter<RecentConversationAdapter.ConversationViewHolder> {
    private final List<ConversationItem> conversationItems;
    private final ConversationListener conversationListener;
    private final String senderId;

    public RecentConversationAdapter(List<ConversationItem> conversationItems, String senderId, ConversationListener conversationListener) {
        this.conversationItems = conversationItems;
        this.conversationListener = conversationListener;
        this.senderId = senderId;
    }

    private Bitmap getConversationImage(String encodedImage) {
        byte[] bytes = Base64.decode(encodedImage, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    @NonNull
    @Override
    public ConversationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ConversationViewHolder(
                ItemContainerRecentConversationBinding.inflate(
                        LayoutInflater.from(parent.getContext()),
                        parent,
                        false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull ConversationViewHolder holder, int position) {
        holder.setData(conversationItems.get(position));
    }

    @Override
    public int getItemCount() {
        return conversationItems.size();
    }

    class ConversationViewHolder extends RecyclerView.ViewHolder {
        ItemContainerRecentConversationBinding binding;

        ConversationViewHolder(ItemContainerRecentConversationBinding itemContainerRecentConversationBinding) {
            super(itemContainerRecentConversationBinding.getRoot());
            binding = itemContainerRecentConversationBinding;
        }

        void setData(ConversationItem conversationItem) {
            DataItem receiver = new DataItem();
            for (int i = 0; i < conversationItem.getMembers().size(); i++) {
                if (!conversationItem.getMembers().get(i).getId().equals(senderId)) {
                    receiver.setAvatar(conversationItem.getMembers().get(i).getAvatar());
                    receiver.setUsername(conversationItem.getMembers().get(i).getUsername());
                    receiver.setId(conversationItem.getMembers().get(i).getId());
                }
            }
            binding.imageProfile.setImageBitmap(getConversationImage(receiver.getAvatar()));
            binding.txtUsername.setText(receiver.getUsername());

            if (conversationItem.getLastMessage() != null) {
                String _message;
                if (conversationItem.getLastMessage().getSender().equals(senderId)) {
                    _message = "Bạn: " + conversationItem.getLastMessage().getContent();
                } else {
                    _message = conversationItem.getLastMessage().getContent();
                }
                binding.txtRecentMessage.setText(_message);
            }
            binding.getRoot().setOnClickListener(v -> {
                conversationListener.onConversationClicked(receiver);
            });
        }
    }
}
