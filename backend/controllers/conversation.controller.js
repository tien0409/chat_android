const asyncHandler = require("express-async-handler");

const Conversation = require("../models/conversation.model");
const Message = require("../models/message.model");

/*
 * @desc create conversation, if conversation exist then response conversation exist
 * @route POST /api/conversation/:userId
 * @access Private
 */
const createConversation = asyncHandler(async (req, res) => {
  const { userReq, user } = req;

  const conversationExist = await Conversation.findOne({
    members: { $all: [userReq._id, user._id] },
  });

  if (conversationExist) {
    return res.status(200).json({ _id: conversationExist._id });
  }

  const conversation = new Conversation({ members: [userReq._id, user._id] });
  await conversation.save();

  res.status(201).json({ _id: conversation._id });
});

/*
 * @desc get all conversation and info user in conversation
 * @route GET /api/conversation
 * @access Private
 */
const getAllConversation = asyncHandler(async (req, res) => {
  const conversations = await Conversation.find().populate(
    "members",
    "avatar username"
  );
  const lastMessageConvervsation = await Promise.all(
    conversations.map(async (c) => {
      const getLastMessage = async () => {
        const data = await Message.findOne({
          conversation: c._id,
        }).sort({ createdAt: -1 });
        return data;
      };
      return { ...c._doc, lastMessage: await getLastMessage() };
    })
  );

  res.status(201).json({
    message: "",
    data: lastMessageConvervsation,
  });
});

module.exports = { createConversation, getAllConversation };
