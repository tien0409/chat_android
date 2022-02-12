const express = require("express");
const passport = require("passport");

const router = express.Router();

const { signUp, signIn, getUsers } = require("../controllers/user.controller");
const {
  signUpValidator,
  signInValidator,
} = require("../validators/user.validator");
const { auth } = require("../middlewares/auth.middleware");

router.post("/signup", signUpValidator(), signUp);
router.post("/signin", signInValidator(), signIn);
router.get("/", auth, getUsers);

module.exports = router;
