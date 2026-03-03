const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
  // 关闭esline
  lintOnSave: false,
  transpileDependencies: true,
  publicPath: './',
  devServer: {
    client: {
      overlay: false
    }
  }
})
