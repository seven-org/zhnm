var app = getApp();
var util = require('../../../utils/util.js');

Page({
  /**
   * 页面的初始数据
   */
  data: {
    httphost: app.data.httphost,
    httpserver: app.data.httpserver
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    var that = this;
    // wx.request({
    //   url: that.data.httpserver + 'user/info',
    //   header: wx.getStorageSync('header'),
    //   success: function (request) {
    util.request(app.data.httpserver + 'user/info').then(function (request) { 
        that.setData({
          phone: request.phone,
        })
    })
  },
  change(e) {
    var that = this;
    var phone = e.detail.value
    that.setData({
      phone: phone,
    })
  },
  update() {
    var that = this;
    var phone = that.data.phone;
    wx.showToast({
      icon: 'loading',
    })
    // wx.request({
    //   url: that.data.httpserver + 'user/update_phone',
    //   header: wx.getStorageSync('header'),
    //   data: { phone: phone },
    //   success: function (request) {
    var data = { phone: phone }
    util.request(app.data.httpserver + 'user/update_phone', data = data).then(function (request) { 
        wx.showToast({
          title: request.message,
          icon: 'success',
        })
        that.setData({
          phone: request.shopUser.phone,
        })
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