module.exports = {
  webpack: function (config, env) {
    config.output.filename = 'static/js/[name].[contenthash].js';
    config.output.chunkFilename = 'static/js/[name].[contenthash].chunk.js';
    return config;
  },
};