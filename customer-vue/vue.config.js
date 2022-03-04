const fs = require("fs");
module.exports = {
  transpileDependencies: true,
  devServer:{
    allowedHosts: 'all',
    https: {
      key: fs.readFileSync('mydomain.key'),
      cert: fs.readFileSync('trustService.cer'),
    }

  }
}
