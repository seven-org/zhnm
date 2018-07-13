var app = getApp()
var util = require('../../../utils/util.js');
Page({
  /**
   * 页面的初始数据
   */
  data: {
    httphost: app.data.httphost,
    httpserver: app.data.httpserver,
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    var that = this;
    wx.showNavigationBarLoading();
    // wx.request({
    //   url: that.data.httpserver + 'address/list',
    //   header: wx.getStorageSync('header'),
    //   success: function (request) {
    util.request(app.data.httpserver + 'address/list').then(function (request) {
        if (request.result==1){
          that.setData({
            list: request.addrlist,
          })
      }
    })
  },
  address_info: function (e) {
    var msg = e.currentTarget.dataset.msg;
    var address_id = e.currentTarget.dataset.address_id;
    wx.navigateTo({
      url: '../info/info?msg=' + msg + '&address_id=' + address_id,
    })
  },
  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {
  
  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {
    this.onLoad();
  },

  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide: function () {
  
  },

  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload: function () {
  
  },

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh: function () {
  
  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom: function () {
  
  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage: function () {
  
  }
})