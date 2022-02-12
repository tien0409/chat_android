const jwt = require("jsonwebtoken");
const asyncHandler = require("express-async-handler");

const { JWT_SECRET } = require("../configs/env");
const User = require("../models/user.model");

const auth = asyncHandler(async (req, res, next) => {
  if (req.headers && req.headers.authorization) {
    const token = req.headers.authorization.split(" ")[1];

    try {
      const payload = jwt.verify(token, JWT_SECRET);
      const user = await User.findById(payload.userId);

      if (!user) {
        res.status(404);
        throw new Error("User not found");
      }
      req.user = user;
      next();
    } catch (err) {
      console.error(err);
      res.status(401);
      throw new Error("Not authorized, token failed");
    }
  } else {
    // no token
    res.status(401);
    throw new Error("Not authorized, no token");
  }
});

module.exports = { auth };
