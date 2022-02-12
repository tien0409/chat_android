const mongoose = require("mongoose");

const Conversation = require("../../models/conversation.model");
const User = require("../../models/user.model");
const Message = require("../../models/message.model");

module.exports = function (socket, io) {
  const createConversation = async (data) => {
    const {
      members: [receiver, user],
    } = JSON.parse(data);

    const receiverId = receiver._id.replaceAll('"', "");
    const userId = user._id.replaceAll('"', "");

    socket.join(`${receiverId}-${userId}`);
    socket.join(`${userId}-${receiverId}`);

    const conversationExist = await Conversation.findOne({
      members: { $all: [receiverId, userId] },
    });

    if (!conversationExist) {
      const conversation = new Conversation({
        members: [receiverId, userId],
      });
      await conversation.save();

      socket.emit("server-send-conversation", conversation._id.toString());
    } else {
      socket.emit("server-send-conversation", conversationExist._id.toString());
    }
  };

  const getAllConversation = async (userId) => {
    const conversations = await Conversation.find({
      members: userId.replaceAll('"', ""),
    }).populate("members", "avatar username");

    const lastMessageConvervsation = await Promise.all(
      conversations.map(async (c) => {
        socket.join(
          `${c.members[0]._id.toString()}-${c.members[1]._id.toString()}`
        );
        socket.join(
          `${c.members[1]._id.toString()}-${c.members[0]._id.toString()}`
        );
        socket.join(c.members[0]._id.toString());
        socket.join(c.members[1]._id.toString());

        const getLastMessage = async () => {
          const data = await Message.findOne({
            conversation: c._id,
          }).sort({ createdAt: -1 });
          return data;
        };
        return { ...c._doc, lastMessage: await getLastMessage() };
      })
    );

    socket.emit("server-send-conversations", lastMessageConvervsation);
  };

  socket.on("client-get-conversation", createConversation);

  socket.on("client-get-conversations", getAllConversation);
};
