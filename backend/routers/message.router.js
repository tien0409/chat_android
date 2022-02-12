const express = require("express");

const router = express.Router();
const { auth } = require("../middlewares/auth.middleware");
const { getConversationId } = require("../middlewares/conversation.middleware");
const {
  getAllMessage,
  createMessage,
} = require("../controllers/message.controller");
const { createMessageValidator } = require("../validators/message.validator");

router.param("conversationId", getConversationId);

router
  .route("/:conversationId")
  .get(auth, getAllMessage)
  .post(auth, createMessageValidator(), createMessage);

module.exports = router;
