var app = getApp();
var util = require('../../utils/util.js');
Page({
  /**
   * 页面的初始数据
   */
  data: {
    httphost: app.data.httphost,
    httpserver: app.data.httpserver,
    cartlist: [],
    total_price: '0.00',
    userInfo: {}
  },
 
  onLoad: function () {
    var that = this
    // wx.request({
    //   url: that.data.httpserver + 'cart/list',
    //   header: wx.getStorageSync('header'),
    //   success: function (request) {
    util.request(app.data.httpserver + 'cart/list').then(function (request) {
        console.log(request);
        that.setData({
          cartlist: request.cartlist,
          page: request.page,
          cart_count: request.cart_count,
        })
    })
  },
  to_order(e){
    let cartlist = this.data.cartlist;                  // 获取购物车列表
    var cart_id ='';
    var goods_id = '';
    var goods_count = '';
    var attribute_detail_id = '';
    var is_st = 0;
    for (let i = 0; i < cartlist.length; i++) {         // 循环列表得到每个数据
      if (cartlist[i].selected) {               // 判断选中才会计算价格
        ++is_st;
        if (is_st == 1){
          cart_id = cartlist[i].cart_id;
          goods_id = cartlist[i].goods_id;
          goods_count = cartlist[i].goods_count;
          attribute_detail_id = cartlist[i].attribute_detail_id;
        }else{
          cart_id = cart_id +','+cartlist[i].cart_id;
          goods_id = goods_id + ',' + cartlist[i].goods_id;
          goods_count = goods_count + ',' + cartlist[i].goods_count;
          attribute_detail_id = attribute_detail_id + ',' + cartlist[i].attribute_detail_id;
        }
      }
    }
    if (is_st!=0){
      wx.navigateTo({
        url: '../order/payment/info?goods_id=' + goods_id + '&goods_count=' + goods_count + '&cart_id=' + cart_id + '&attribute_detail_id=' + attribute_detail_id + '&address_id=',
      })
    }else{
      wx.showModal({
        title: '提示',
        showCancel: false,
        content: '您还未选中任何商品',
      })
    }
  },
  selectList(e) {
    const index = e.currentTarget.dataset.index;    // 获取data- 传进来的index
    let cartlist = this.data.cartlist;                    // 获取购物车列表
    const selected = cartlist[index].selected;         // 获取当前商品的选中状态
    cartlist[index].selected = !selected;              // 改变状态
    this.setData({
      cartlist: cartlist
    });
    this.getTotalPrice();                           // 重新获取总价
  },
  selectAll(e) {
    let selectAllStatus = this.data.selectAllStatus;    // 是否全选状态
    selectAllStatus = !selectAllStatus;
    let cartlist = this.data.cartlist;

    for (let i = 0; i < cartlist.length; i++) {
      cartlist[i].selected = selectAllStatus;            // 改变所有商品状态
    }
    this.setData({
      selectAllStatus: selectAllStatus,
      cartlist: cartlist
    });
    this.getTotalPrice();                               // 重新获取总价
  },
  // 增加数量
  addCount(e) {
    const index = e.currentTarget.dataset.index;
    let cartlist = this.data.cartlist;
    let goods_count = cartlist[index].goods_count;
    goods_count = parseInt(goods_count) + 1;
    cartlist[index].goods_count = goods_count;
    var cart_id = cartlist[index].cart_id;
    var that = this
    // wx.request({
    //   url: that.data.httpserver + 'cart/update',
    //   data: { goods_count: goods_count, cart_id: cart_id},
    //   header: wx.getStorageSync('header'),
    //   success: function (request) {
    var data = { goods_count: goods_count, cart_id: cart_id }
    util.request(app.data.httpserver + 'cart/update', data = data).then(function (request) {
        console.log(request)
        that.setData({
          cartlist: cartlist,
          cart_count: request.cart_count,
        })
    })
    // this.setData({
    //   cartlist: cartlist,
    // });
    that.getTotalPrice();
  },
  // 减少数量
  minusCount(e) {
    const index = e.currentTarget.dataset.index;
    let cartlist = this.data.cartlist;
    let goods_count = cartlist[index].goods_count;
    if (goods_count <= 1) {
      return false;
    }
    goods_count = goods_count - 1;
    cartlist[index].goods_count = goods_count;
    var cart_id = cartlist[index].cart_id;
    var that = this
    // wx.request({
    //   url: that.data.httpserver + 'cart/update',
    //   data: { goods_count: goods_count, cart_id: cart_id },
    //   header: wx.getStorageSync('header'),
    //   success: function (request) {
    var data = { goods_count: goods_count, cart_id: cart_id } 
    util.request(app.data.httpserver + 'cart/update', data=data ).then(function (request) {
        that.setData({
          cartlist: cartlist,
          cart_count: request.cart_count,
        })
    })
    that.getTotalPrice();
  },
  deleteList(e) {
    const index = e.currentTarget.dataset.index;
    let cartlist = this.data.cartlist;
    var cart_id = cartlist[index].cart_id;
    var that = this
      wx.showModal({
        title: '确认删除？',
        content: '您确定要把此商品从购物车删除吗?',
        showCancel: true,
        success: function (res) {
          if (res.confirm) {
            // wx.request({
            //   url: that.data.httpserver + 'cart/delete',
            //   data: { cart_id: cart_id },
            //   header: wx.getStorageSync('header'),
            //   success: function (request) {
            var data = { cart_id: cart_id }
            util.request(app.data.httpserver + 'cart/delete', data=data).then(function (request) {          
                  cartlist.splice(index, 1);              // 删除购物车列表里这个商品
                  wx.showToast({
                    title: '商品已删除!',
                    icon: 'succes',
                    duration: 1000,
                  })
                  that.setData({
                    cartlist: cartlist,
                    cart_count: request.cart_count,
                  })
            })
          }
        }
      })
    
   
    
    if (!cartlist.length) {                  // 如果购物车为空
      that.setData({
        hasList: false              // 修改标识为false，显示购物车为空页面
      });
    } else {                              // 如果不为空
      that.getTotalPrice();           // 重新计算总价格
    }
  },
  getTotalPrice() {
    let cartlist = this.data.cartlist;                  // 获取购物车列表
    let total = 0;
    for (let i = 0; i < cartlist.length; i++) {         // 循环列表得到每个数据
      if (cartlist[i].selected) {                   // 判断选中才会计算价格
        total += cartlist[i].goods_count * cartlist[i].goods_price;     // 所有价格加起来
      }
    }
    this.setData({                                // 最后赋值到data中渲染到页面
      cartlist: cartlist,
      total_price: total.toFixed(2)
    });
  },
  /**
   * 商品详情
   */
  goods_info(e){
    var goods_id = e.currentTarget.dataset.goods_id;
    wx.navigateTo({
      url: '../goods/info/info?goods_id=' + goods_id,
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