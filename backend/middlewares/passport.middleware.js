const asyncHandler = require("express-async-handler");
const passport = require("passport");
const JwtStrategy = require("passport-jwt").Strategy;
const ExtractJwt = require("passport-jwt").ExtractJwt;

const { JWT_SECRET } = require("../configs/env");
const User = require("../models/user.model");

const opts = {
  jwtFromRequest: ExtractJwt.fromAuthHeaderAsBearerToken(),
  secretOrKey: JWT_SECRET,
};

passport.use(
  new JwtStrategy(
    opts,
    asyncHandler(async (payload, done) => {
      console.log("payload", payload)
      const user = await User.findById(payload.userId);
      if (!user) {
        const error = new Error("User not found");
        return done(error, false);
      }
      return done(null, user);
    })
  )
);
