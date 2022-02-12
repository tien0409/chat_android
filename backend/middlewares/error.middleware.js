const notFoundRoute = (req, res, next) => {
  const err = new Error(`Not found - ${req.originalUrl}`);
  res.status(404);
  next(err);
};

const errHandler = (err, req, res, next) => {
  const statusCode = req.statusCode === 200 ? 500 : res.statusCode;
  res.status(statusCode).json({
    code: statusCode,
    message: err.message,
  });
};

module.exports = { notFoundRoute, errHandler };
