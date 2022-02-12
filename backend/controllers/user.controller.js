const { validationResult } = require("express-validator");
const asyncHandler = require("express-async-handler");

const generateToken = require("../utils/generate-token");
const User = require("../models/user.model");

const signUp = asyncHandler(async (req, res) => {
  console.log("req.body", req.body);
  const errors = validationResult(req);
  if (!errors.isEmpty()) {
    res.status(400);
    throw new Error(errors.array()[0].msg);
  }
  const { email, username, fullName, password, avatar } = req.body;

  const newUser = new User({ email, username, fullName, password, avatar });
  await newUser.save();

  res.status(201).json({
    message: "",
    data: {
      _id: newUser._id,
      email: newUser.email,
      username: newUser.username,
      fullName: newUser.fullName,
      avatar: newUser.avatar,
      phoneNumber: newUser.phoneNumber,
      token: generateToken(newUser._id),
    },
  });
});

const signIn = asyncHandler(async (req, res) => {
  const errors = validationResult(req);
  if (!errors.isEmpty()) {
    res.status(400);
    throw new Error(errors.array()[0].msg);
  }

  const { email, password } = req.body;

  const user = await User.findOne({ email });

  if (user && (await user.isCorrectPassword(password))) {
    res.status(200).json({
      message: "",
      data: {
        _id: user._id,
        email: user.email,
        username: user.username,
        avatar: user.avatar,
        fullName: user.fullName,
        phoneNumber: user.phoneNumber,
        token: generateToken(user._id),
      },
    });
  } else {
    res.status(401);
    throw new Error("Email or Password incorrect. Please try again");
  }
});

const getUsers = asyncHandler(async (req, res) => {
  const { _id: idLoggedIn } = req.user;

  const users = await User.find({
    _id: { $ne: idLoggedIn },
  }).select("-password");

  res.status(200).json({
    message: '',
    data: users
  });
});

module.exports = {
  signUp,
  signIn,
  getUsers,
};
