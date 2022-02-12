const userSocket = require("./user");
const conversationSocket = require("./conversation");
const messageSocket = require("./message");

const userOnline = [];

const socketio = (httpServer) => {
  const io = require("socket.io")(httpServer, {
    cors: {
      origin: "*",
    },
  });

  io.on("connection", (socket) => {
    console.log("user connected");

    socket.on("join", function (userNickname) {
      socket.broadcast.emit(
        "user jointed",
        userNickname + ": has joined the chat"
      );
    });

    socket.on("client-online", (userId) => {
      const _userId = userId.replaceAll('"', '')

      if (!userOnline.includes(userId)) {
        userOnline.push(userId);
      }

      socket._id = _userId;
      socket.join(_userId);
    });

    socket.on("messagedetection", (senderNickname, messageContent) => {
      console.log(senderNickname + ": " + messageContent);

      let message =
        ("message: ", messageContent, "senderNickname: ", senderNickname);

      socket.emit("message", message);
    });

    socket.on("disconnect", () => {
      console.log("user has left");
      socket.broadcast.emit("userdisconnect", "user has left");
    });

    userSocket(socket, io);
    conversationSocket(socket, io);
    messageSocket(socket, io);
  });
};

module.exports = socketio;
