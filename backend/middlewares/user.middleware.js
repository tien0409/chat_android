const asyncHandler = require("express-async-handler");

const User = require("../models/user.model");

const getUserId = asyncHandler(async (req, res, next, userId) => {
  if (userId.match(/^[0-9a-fA-F]{24}$/)) {
    const userReq = await User.findById(userId);
    if (userReq) {
      req.userReq = userReq;
      return next();
    }
  }
  res.status(404);
  next(new Error("User not found"));
});

const getUsername = asyncHandler(async (req, res, next, username) => {
  const userReq = await User.findOne({ username });
  if (userReq) {
    req.userReq = userReq;
    return next();
  }
  res.status(404);
  next(new Error("User not found"));
});

module.exports = { getUserId, getUsername };
