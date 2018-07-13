var app = getApp();
Page({

  /**
   * 页面的初始数据
   */
  data: {
    httphost: app.data.httphost,
    httpserver: app.data.httpserver,
    actionSheetHidden: true,
    menu: '',
    last_data:'',
    load_time : 0,
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    var that = this
    var goods_id = options.goods_id;
    var goods_count = options.goods_count;
    var cart_id = options.cart_id;
    var attribute_detail_id = options.attribute_detail_id;
    var address_id = options.address_id;
    if (typeof (address_id) == 'undefined') {
      address_id =null;
    }
    wx.request({
      url: that.data.httpserver + 'toorder',
      data: {
        goods_id: goods_id, goods_count: goods_count, cart_id: cart_id, attribute_detail_id: attribute_detail_id, address_id: address_id
      },
      header: wx.getStorageSync('header'),
      success: function (request) {
        console.log('获取预支付订单信息'+request);
        if (request.data.result == 1005) {
          wx.showModal({
            title: '提示',
            content: request.data.message,
            showCancel: false,
            success: function (res) {
              if (res.confirm) {
              }
            }
          })
        } else {
          var list = request.data.list;
          for(var i = 0;i<list.length;i++){
            var goods_price = list[i].goods_price;
            list[i].goods_price = goods_price.toFixed(2)
          }
          var couponlist = request.data.couponlist;
          for (var i = 0; i < couponlist.length; i++) {
            var coupon_price = couponlist[i].coupon_price;
            couponlist[i].coupon_price = coupon_price.toFixed(2)
          }
          var coupon = request.data.coupon;
          if (Object.keys(coupon).length != 0){
            if (coupon.coupon_id != 0) {
              coupon.coupon_price = coupon.coupon_price.toFixed(2);
            }
          }
         
          that.setData({
            pd: request.data.pd,
            list: list,
            address: request.data.address,
            freight_price: request.data.freight_price,
            couponlist: couponlist,
            coupon: request.data.coupon,
            coupon_count: request.data.coupon_count,
            order_total: request.data.order_total.toFixed(2),
          })
        }
      },
      fail: function () {
        console.log(1);
      }
    })
    var load_time = that.data.load_time ;
    ++load_time
    that.setData({
      load_time: load_time
    })
    console.log(".load_time= " + load_time);
  },
  actionSheetTap: function () {
    this.setData({
      actionSheetHidden: !this.data.actionSheetHidden
    })
  },
  actionSheetbindchange: function () {
    this.setData({
      actionSheetHidden: !this.data.actionSheetHidden
    })
  },
  /**
   * 更改优惠券
   */
  chose_coupon(e) {
    var that = this
    var goods_id = e.currentTarget.dataset.goods_id;
    var goods_count = e.currentTarget.dataset.goods_count;
    var attribute_detail_id = e.currentTarget.dataset.attribute_detail_id;
    var coupon_id = e.currentTarget.dataset.coupon_id;
    var coupon_name = e.currentTarget.dataset.coupon_name;
    wx.showLoading();
    wx.request({
      url: that.data.httpserver + 'order_total',
      type: 'post',
      header: wx.getStorageSync('header'),
      data: { coupon_id: coupon_id, goods_id: goods_id, goods_count: goods_count, attribute_detail_id: attribute_detail_id },
      success: function (request) {
        console.log(request.data);
        wx.hideLoading();
        var coupon = request.data.coupon
        if (coupon.coupon_id != 0) {
        coupon.coupon_price = coupon.coupon_price.toFixed(2)
        }
        that.setData({
          coupon: coupon,
          order_total: request.data.order_total.toFixed(2),
          actionSheetHidden: true,
        })
      }
    })

  },
  /**
   * 提交订单
   */
  add_order(e) {
    var that = this;
    var attribute_detail_id = that.data.pd.attribute_detail_id;
    var address_id = that.data.address.address_id;
    if (address_id == '' || address_id == null) {
      console.log('请添加收货地址！');
      return;
    }
    var cart_id = that.data.pd.cart_id;
    var goods_id = that.data.pd.goods_id;
    var goods_count = that.data.pd.goods_count;
    var coupon_id = that.data.coupon.coupon_id;
    if (coupon_id == '' || coupon_id == null) {
      coupon_id = 0;
    }
    var pay_way = 2;
    wx.showLoading();
    wx.request({
      url: that.data.httpserver + 'addorder',
      type: 'post',
      data: { coupon_id: coupon_id, goods_id: goods_id, goods_count: goods_count, cart_id: cart_id, address_id: address_id, pay_way: pay_way, attribute_detail_id: attribute_detail_id },
      header: wx.getStorageSync('header'),
      success: function (request) {
        wx.hideLoading();
        if (request.data.result == 1) {
          if (pay_way == 3) {
            wx.navigateTo({
              url: order / result,
            })
          }
          else if (pay_way == 2) {
            wx.requestPayment(
              {
                'timeStamp': request.data.return_pay.timeStamp,
                'nonceStr': request.data.return_pay.nonceStr,
                'package': request.data.return_pay.package,
                'signType': 'MD5',
                'paySign': request.data.return_pay.paySign,
                'success': function (res) { 
                  wx.navigateTo({
                    url: '../list/list',
                  })
                },
                'fail': function (res) {
                  wx.navigateTo({
                    url: '../list/list',
                  }) },
                'complete': function (res) { }
              })
          }
        } else {
          console.log(data.message);
        }
      }
    })
  },
  /**
   * 地址列表
   */
  address_list(){
    // var goods_id = this.data.pd.goods_id;
    // var goods_count = this.data.pd.goods_count;
    // var cart_id = this.data.pd.cart_id;
    // var attribute_detail_id = this.data.pd.attribute_detail_id
    wx.navigateTo({
      url: '../address/list/list',
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
    var load_time = this.data.load_time;
    if (this.data.load_time!=1){
      var pages = getCurrentPages();
      var currPage = pages[pages.length - 1]; //当前页面
      var options = {
        goods_id: this.data.pd.goods_id,
        goods_count: this.data.pd.goods_count,
        attribute_detail_id: this.data.pd.attribute_detail_id,
        cart_id: this.data.pd.cart_id,
        address_id: this.data.pd.address_id,
      }
      this.onLoad(options);
    }
    ++load_time;
    this.setData({
      load_time: load_time
    })
    
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