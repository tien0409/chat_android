const mongoose = require("mongoose");
const { MONGO_URI } = require("../configs/env");

const connectDb = async () => {
  try {
    await mongoose.connect(MONGO_URI);
    console.log("Connect database success");
  } catch (err) {
    console.error(err);
    console.error("Error connect database");
    process.exit(1);
  }
};

module.exports = connectDb;
