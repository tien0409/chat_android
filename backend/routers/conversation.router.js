const express = require("express");

const router = express.Router();
const {
  createConversation,
  getAllConversation,
} = require("../controllers/conversation.controller");
const { auth } = require("../middlewares/auth.middleware");
const { getUserId } = require("../middlewares/user.middleware");

router.param("userId", getUserId);

router.route("/").get(auth, getAllConversation);
router.route("/:userId").post(auth, createConversation);

module.exports = router;
