const { check } = require("express-validator");

const createMessageValidator = () => [
  check("content", "Content is required").not().isEmpty(),
];

module.exports = { createMessageValidator };
