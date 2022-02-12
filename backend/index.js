require("dotenv").config();
const express = require("express");
const cors = require("cors");
const morgan = require("morgan");

const { notFoundRoute, errHandler } = require("./middlewares/error.middleware");
const { PORT } = require("./configs/env");
const connectDb = require("./utils/db");

const app = express();
connectDb();

// third party middlewares
app.use(express.static("public"));
app.use(express.json());
app.use(express.urlencoded({ extended: true }));
app.use(morgan("dev"));
app.use(cors());

require("./middlewares/passport.middleware");

// routes
const userRouter = require("./routers/user.router");
const messageRouter = require("./routers/message.router");
const conversationRouter = require("./routers/conversation.router");

app.use("/api/user", userRouter);
app.use("/api/message", messageRouter);
app.use("/api/conversation", conversationRouter);

app.use(notFoundRoute);
app.use(errHandler);

const server = app.listen(PORT, () => {
  console.log(`Node app is running on port ${PORT}`);
});

require("./socket")(server);
