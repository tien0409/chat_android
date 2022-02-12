const jwt = require("jsonwebtoken");
const { JWT_SECRET } = require("../configs/env");

const generateToken = (userId) => {
  return jwt.sign(
    {
      userId,
    },
    JWT_SECRET,
  );
};

module.exports = generateToken;
