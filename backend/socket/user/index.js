const User = require("../../models/user.model");
const Message = require("../../models/message.model");
const Conversation = require("../../models/conversation.model");

module.exports = function (socket, io) {
  const getUserLoggedIn = async (id) => {
    const user = await User.findById(id)
      .populate("followers", "avatar username fullName")
      .populate("following", "avatar username fullName");

    socket.emit("server-send-user-logged-in", {
      _id: user._id,
      email: user.email,
      username: user.username,
      avatar: user.avatar,
      fullName: user.fullName,
      phoneNumber: user.phoneNumber,
    });
  };

  const sendMessage = async (data) => {
    const { room, sender, conversation, content, avatar } = JSON.parse(data);

    const isFirstMessage = (await Message.countDocuments({ conversation })) < 1;
    const _rooms = room.split("-");

    if (isFirstMessage) {
      _rooms.forEach((_room) => {
        if (_room !== socket._id) {
          console.log("_room", _room);
          console.log("socket._id", socket._id)
          socket.to(_room).emit("server-send-first-message", true);
        }
      });

      // io.to(room).emit("server-send-first-message");
      // socket.to(room1).emit("server-send-first-message");
      // socket.to(room2).emit("server-send-first-message");
    }

    const message = new Message({ conversation, content, avatar, sender });

    await message.save();

    io.to(room).emit("server-send-message", message);
  };

  socket.on("client-send-message", sendMessage);

  socket.on("client-user-typing", (data) => {
    io.to(data.room).emit("server-user-typing");
  });
};
