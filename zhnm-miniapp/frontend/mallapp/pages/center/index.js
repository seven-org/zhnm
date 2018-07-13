var util = require('../../utils/util.js');
var app = getApp();
Page({

  /**
   * 页面的初始数据
   */
  data: {
    httphost: app.data.httphost,
    httpserver: app.data.httpserver,
  },

  order_list: function (event) {
    var status = event.currentTarget.dataset.status;
    wx.navigateTo({
      url: '../order/list/list?status=' + status,
    })
  },
   collect_list: function () {
    wx.navigateTo({
      url: '../collect/list',
    })
  },
   address_list: function () {
     wx.navigateTo({
       url: '../address/list/list',
     })
   },
   coupon_list: function () {
     wx.navigateTo({
       url: '../coupon/user/list/list',
     })
   },
   user_index: function () {
     wx.navigateTo({
       url: '../user/index/index',
     })
   },
    entitycard_list: function () {
     wx.navigateTo({
       url: '../entitycard/list/list',
     })
   },
    giftcard_list: function () {
      wx.navigateTo({
        url: '../giftcard/list/list',
      })
    },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    var that = this ;
    util.request(app.data.httpserver + 'center/index').then(function (request) {
        that.setData({
          count: request.count,
          shopUser: request.shopUser,
          cart_count: request.cart_count,
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