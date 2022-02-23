module.exports = {
  transpileDependencies: [
    'vuetify'
  ],
  devServer:{
    proxy:{
      'store-service/' :{
        target: 'http://localhost:8001',
        ws:true,
      },
      'order-service/' :{
        target: 'http://localhost:8001',
        ws:true,
      }
    }
  }
}
