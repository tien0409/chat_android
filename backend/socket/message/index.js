const Message = require("../../models/message.model");
const Conversation = require("../../models/conversation.model");
const User = require("../../models/user.model");

module.exports = function (socket, io, auth) {
  const getAllMessageInConversation = async (data) => {
    const {_id} = JSON.parse(data);

    const messages = await Message.find({ conversation: _id });
    socket.emit("server-send-messages", messages);
  };

  socket.on("client-get-messages", getAllMessageInConversation);
};
