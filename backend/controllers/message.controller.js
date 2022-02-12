const asyncHandler = require("express-async-handler");

const Message = require("../models/message.model");

/*
 * @desc  get all message in conversation
 * @route Get /api/message/:conversationId
 * @access Private
 */
const getAllMessage = asyncHandler(async (req, res) => {
  const { conversationReq } = req;

  const messages = await Message.find({
    conversation: conversationReq._id,
  });

  res.status(200).json({
    message: "",
    data: messages,
  });
});

/*
 * @desc  create message
 * @route Post /api/message/:conversationId
 * @access Private
 */
const createMessage = asyncHandler(async (req, res) => {
  const { conversationReq, user } = req;
  const { content } = req.body;

  const message = new Message({
    conversation: conversationReq._id,
    sender: user._id,
    avatar: user.avatar,
    content,
  });
  await message.save();

  const messages = await Message.find({
    conversation: conversationReq._id,
  });

  res.status(201).json({
    message: "",
    data: messages,
  });
});

module.exports = {
  getAllMessage,
  createMessage,
};
