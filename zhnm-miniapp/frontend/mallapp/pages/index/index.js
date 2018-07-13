var util = require('../../utils/util.js');
var app = getApp();

 /*var swiper = require("../../static/js/swiper.js");*/

Page({
  /**
   * 页面的初始数据
   */
  data: {
    inputShowed: false,
    inputVal: "",
    httphost: app.data.httphost,
    httpserver: app.data.httpserver,
  },

  
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function () {
    var that = this; 
    util.request(app.data.httpserver + 'index').then(function (request) {
      console.log(request);
      console.log(request.result);
      if (request.result === 1) {
        var banner = request.bannerlist
        var navigation = request.navigationlist
        //组合轮播图片
        for (var i = 0; i < banner.length; i++) {
          var app_url = banner[i].app_url
          var attr_value = '';
          if (typeof (app_url) != 'undefined' && app_url.indexOf('/') != -1) {
            banner[i].app_url = app_url.split('/')[0];
            // var attr_name_value = app_url.split('?')[1].split('=');
            var attr_value = app_url.split('/')[1];
          }
          banner[i].attr_value = attr_value;
        }
        //组合导航分类
        for (var i = 0; i < navigation.length; i++) {
          var navigation_app_url = navigation[i].navigation_app_url
          var navigation_attr_value = '';
          if (typeof (navigation_app_url) != 'undefined' && navigation_app_url.indexOf('/') != -1) {
            navigation[i].navigation_app_url = navigation_app_url.split('/')[0];
            // var attr_name_value = navigation_app_url.split('?')[1].split('=');
            navigation_attr_value = navigation_app_url.split('/')[1];
          }
          navigation[i].navigation_attr_value = navigation_attr_value;
        }
        that.setData({
          banner: banner,
          navigation: navigation,
          news: request.newslist,
          tuijian: request.tuijianlist,
          goods: request.goodslist,
          cart_count: request.cart_count,
        })
      } else if (request.result === 1005){
        wx.redirectTo({
              url: '/pages/welcome/index'
            })
      }
    })
    },
    // wx.request({
    //   url: app.data.httpserver + 'index',
    //   header: wx.getStorageSync('header'),
    //   success:function(request){
      
    //   }
    // })
  // },
  bannerLoad: function (e) {
    var imageSize = util.imageUtil(e)
    this.setData({
      bannerwidth: imageSize.imageWidth,
      bannerheight: imageSize.imageHeight
    })
  }, 
  imageLoad: function (e) {
    var imageSize = util.util(e)
    this.setData({
      imagewidth: imageSize.imageWidth,
      imageheight: imageSize.imageHeight
    })
  }, 
  showInput: function () {
    console.log('showInput');
    this.setData({
      inputShowed: true
    });
  },
  hideInput: function () {
    this.setData({
      inputVal: "",
      inputShowed: false
    });
  },
  clearInput: function () {
    this.setData({
      inputVal: ""
    });
  },
  inputTyping: function (e) {
    this.setData({
      inputVal: e.detail.value
    });
  },

  news_list: function () {
    wx.navigateTo({
      url: '../news/list/list'
    })
  },
  index: function () {
    wx.navigateTo({
      url: 'index'
    })
  },
  goods_list: function (e) {
    var category_id = e.currentTarget.dataset.category_id
    wx.navigateTo({
      url: '../goods/list/list?category_id=' + category_id,
    })
  },
  search_list: function () {
    var goods_name = this.data.inputVal;
    wx.navigateTo({
      url: '../goods/list/list?goods_name=' + goods_name,
    })
  },
  goods_info: function (e) {
    var goods_id = e.currentTarget.dataset.goods_id;
    console.log('goods_id->'+goods_id);
    wx.navigateTo({
      url: '../goods/info/info?goods_id=' + goods_id,
    })
  },
  coupon_list() {
    wx.navigateTo({
      url: '../coupon/list/list',
    })
  },
  entitycard_index(){
    wx.navigateTo({
      url: '../entitycard/index',
    })
  },
  giftcard_index() {
    wx.navigateTo({
      url: '../giftcard/index/index',
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
  
  },
  scanCode: function () {
    wx.navigateTo({
      url: '../coupon/list/list',
    })
  }
  /**
   * 调起扫码
   */
  // scanCode:function(){
  //   wx.scanCode({
  //     success: (res) => {
  //       console.log(res)
  //       wx.navigateTo({
  //         url: '../'+res.result,
  //       })
  //     }
  //   })
  // }
})