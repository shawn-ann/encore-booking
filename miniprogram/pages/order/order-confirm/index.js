import Toast from 'tdesign-miniprogram/toast/index';
import { fetchSettleDetail } from '../../../services/order/orderConfirm';
import { commitPay, wechatPayOrder } from './pay';
import { getAddressPromise } from '../../usercenter/address/list/util';

const stripeImg = `https://cdn-we-retail.ym.tencent.com/miniapp/order/stripe.png`;

Page({
  data: {
    placeholder: '备注信息',
    stripeImg,
    loading: false,
    settleDetailData: {
      storeGoodsList: [], //正常下单商品列表
      outOfStockGoodsList: [], //库存不足商品
      abnormalDeliveryGoodsList: [], // 不能正常配送商品
      inValidGoodsList: [], // 失效或者库存不足
      limitGoodsList: [], //限购商品
    }, // 获取结算页详情 data
    orderCardList: [], // 仅用于商品卡片展示
    goodsRequestList: [],
    popupShow: false, // 不在配送范围 失效 库存不足 商品展示弹框
    notesPosition: 'center',
    storeInfoList: [],
    storeNoteIndex: 0, //当前填写备注门店index
    promotionGoodsList: [], //当前门店商品列表(优惠券)
    currentStoreId: null, //当前优惠券storeId
  },

  payLock: false,
  onLoad(options) {
    this.setData({
      loading: true,
    });
    this.handleOptionsParams(options);
  },
  onShow() {
  },

  init() {
    this.setData({
      loading: true,
    });
    debugger;
    const { goodsRequestList } = this;
    this.handleOptionsParams({ goodsRequestList });
  },
  // 处理不同情况下跳转到结算页时需要的参数
  handleOptionsParams(options) {
    let { goodsRequestList } = this; // 商品列表
    
    if (typeof options.goodsRequestList === 'string') {
      goodsRequestList = JSON.parse(options.goodsRequestList);
    }
    //获取结算页请求数据列表
    this.goodsRequestList = goodsRequestList;
    const params = {
      goodsRequestList,
    };
    fetchSettleDetail(params).then(
      (res) => {
        this.setData({
          loading: false,
        });
        this.initData(res.data);
      },
      () => {
        //接口异常处理
        this.handleError();
      },
    );
  },
  initData(resData) {
    // 转换商品卡片显示数据
    const data = this.handleResToGoodsCard(resData);

    this.setData({ settleDetailData: data });
    this.isInvalidOrder(data);
  },

  isInvalidOrder(data) {
    // 失效 不在配送范围 限购的商品 提示弹窗
    if (
      (data.limitGoodsList && data.limitGoodsList.length > 0) ||
      (data.abnormalDeliveryGoodsList &&
        data.abnormalDeliveryGoodsList.length > 0) ||
      (data.inValidGoodsList && data.inValidGoodsList.length > 0)
    ) {
      this.setData({ popupShow: true });
      return true;
    }
    this.setData({ popupShow: false });
    if (data.settleType === 0) {
      return true;
    }
    return false;
  },

  handleError() {
    Toast({
      context: this,
      selector: '#t-toast',
      message: '结算异常, 请稍后重试',
      duration: 2000,
      icon: '',
    });

    setTimeout(() => {
      wx.navigateBack();
    }, 1500);
    this.setData({
      loading: false,
    });
  },
  getRequestGoodsList(storeGoodsList) {
    debugger;
    const filterStoreGoodsList = [];
    storeGoodsList &&
      storeGoodsList.forEach((store) => {
        store.skuDetailVos &&
          store.skuDetailVos.forEach((goods) => {
            const data = goods;
            filterStoreGoodsList.push(data);
          });
      });
    return filterStoreGoodsList;
  },
  handleGoodsRequest(goods, isOutStock = false) {
    const {
      reminderStock,
      quantity,
      storeId,
      uid,
      saasId,
      spuId,
      goodsName,
      skuId,
      roomId,
    } = goods;
    const resQuantity = isOutStock ? reminderStock : quantity;
    return {
      quantity: resQuantity,
      storeId,
      uid,
      saasId,
      spuId,
      goodsName,
      skuId,
      roomId,
    };
  },
  handleResToGoodsCard(data) {
    // 转换数据 符合 goods-card展示
    const orderCardList = []; // 订单卡片列表

    data.storeGoodsList &&
      data.storeGoodsList.forEach((ele) => {
        const orderCard = {
          id: ele.storeId,
          status: 0,
          statusDesc: '',
          amount: ele.storeTotalPayAmount,
          goodsList: [],
        }; // 订单卡片
        ele.skuDetailVos.forEach((item, index) => {
          orderCard.goodsList.push({
            id: index,
            thumb: item.image,
            title: item.goodsName,
            price: item.tagPrice || item.settlePrice || '0', // 优先取限时活动价
            settlePrice: item.settlePrice,
            titlePrefixTags: item.tagText ? [{ text: item.tagText }] : [],
            num: item.quantity,
            skuId: item.skuId,
            spuId: item.spuId,
            storeId: item.storeId,
          });
        });

        orderCardList.push(orderCard);
      });

    this.setData({ orderCardList });
    return data;
  },
  onInput(e) {
  },
  onBlur() {
    this.setData({
      notesPosition: 'center',
    });
  },
  onFocus() {
    this.setData({
      notesPosition: 'self',
    });
  },
  onTap() {
    this.setData({
      placeholder: '',
    });
  },
  onNoteConfirm() {
    // 备注信息 确认按钮
    const { storeInfoList, storeNoteIndex } = this.data;
    this.tempNoteInfo[storeNoteIndex] = this.noteInfo[storeNoteIndex];
    storeInfoList[storeNoteIndex].remark = this.noteInfo[storeNoteIndex];

    this.setData({
      dialogShow: false,
      storeInfoList,
    });
  },
  onNoteCancel() {
    // 备注信息 取消按钮
    const { storeNoteIndex } = this.data;
    this.noteInfo[storeNoteIndex] = this.tempNoteInfo[storeNoteIndex];
    this.setData({
      dialogShow: false,
    });
  },

  onSureCommit() {
    // 商品库存不足继续结算
    const { settleDetailData } = this.data;
    const { outOfStockGoodsList, storeGoodsList, inValidGoodsList } =
      settleDetailData;
    if (
      (outOfStockGoodsList && outOfStockGoodsList.length > 0) ||
      (inValidGoodsList && storeGoodsList)
    ) {
      // 合并正常商品 和 库存 不足商品继续支付
      // 过滤不必要的参数
      const filterOutGoodsList = [];
      outOfStockGoodsList &&
        outOfStockGoodsList.forEach((outOfStockGoods) => {
          outOfStockGoods.unSettlementGoods.forEach((ele) => {
            const data = ele;
            data.quantity = ele.reminderStock;
            filterOutGoodsList.push(data);
          });
        });
      const filterStoreGoodsList = this.getRequestGoodsList(storeGoodsList);
      const goodsRequestList = filterOutGoodsList.concat(filterStoreGoodsList);
      this.handleOptionsParams({ goodsRequestList });
    }
  },
  // 提交订单
  submitOrder() {
    const {
      settleDetailData,
    } = this.data;
    const { goodsRequestList } = this;

    if (
      this.payLock ||
      !settleDetailData.settleType ||
      !settleDetailData.totalAmount
    ) {
      return;
    }
    this.payLock = true;
    const resSubmitCouponList = this.handleCouponList(submitCouponList);
    const params = {
      goodsRequestList: goodsRequestList,
      userName: "",
      totalAmount: settleDetailData.totalPayAmount, //取优惠后的结算金额
    };
    commitPay(params).then(
      (res) => {
        this.payLock = false;
        const { data } = res;
        // 提交出现 失效 不在配送范围 限购的商品 提示弹窗
        if (this.isInvalidOrder(data)) {
          return;
        }
        if (res.code === 'Success') {
          this.handlePay(data, settleDetailData);
        } else {
          Toast({
            context: this,
            selector: '#t-toast',
            message: res.msg || '提交订单超时，请稍后重试',
            duration: 2000,
            icon: '',
          });
          setTimeout(() => {
            // 提交支付失败   返回购物车
            wx.navigateBack();
          }, 2000);
        }
      },
      (err) => {
        this.payLock = false;
        if (
          err.code === 'CONTAINS_INSUFFICIENT_GOODS' ||
          err.code === 'TOTAL_AMOUNT_DIFFERENT'
        ) {
          Toast({
            context: this,
            selector: '#t-toast',
            message: err.msg || '支付异常',
            duration: 2000,
            icon: '',
          });
          this.init();
        } else if (err.code === 'ORDER_PAY_FAIL') {
          Toast({
            context: this,
            selector: '#t-toast',
            message: '支付失败',
            duration: 2000,
            icon: 'close-circle',
          });
          setTimeout(() => {
            wx.redirectTo({ url: '/order/list' });
          });
        } else if (err.code === 'ILLEGAL_CONFIG_PARAM') {
          Toast({
            context: this,
            selector: '#t-toast',
            message:
              '支付失败，微信支付商户号设置有误，请商家重新检查支付设置。',
            duration: 2000,
            icon: 'close-circle',
          });
          setTimeout(() => {
            wx.redirectTo({ url: '/order/list' });
          });
        } else {
          Toast({
            context: this,
            selector: '#t-toast',
            message: err.msg || '提交支付超时，请稍后重试',
            duration: 2000,
            icon: '',
          });
          setTimeout(() => {
            // 提交支付失败  返回购物车
            wx.navigateBack();
          }, 2000);
        }
      },
    );
  },

  // 处理支付
  handlePay(data, settleDetailData) {
    const { channel, payInfo, tradeNo, interactId, transactionId } = data;
    const { totalAmount, totalPayAmount } = settleDetailData;
    const payOrderInfo = {
      payInfo: payInfo,
      orderId: tradeNo,
      orderAmt: totalAmount,
      payAmt: totalPayAmount,
      interactId: interactId,
      tradeNo: tradeNo,
      transactionId: transactionId,
    };

    if (channel === 'wechat') {
      wechatPayOrder(payOrderInfo);
    }
  },

  hide() {
    // 隐藏 popup
    this.setData({
      'settleDetailData.abnormalDeliveryGoodsList': [],
    });
  },

  onGoodsNumChange(e) {
    const {
      detail: { value },
      currentTarget: {
        dataset: { goods },
      },
    } = e;
    const index = this.goodsRequestList.findIndex(
      ({ storeId, spuId, skuId }) =>
        goods.storeId === storeId &&
        goods.spuId === spuId &&
        goods.skuId === skuId,
    );
    if (index >= 0) {
      // eslint-disable-next-line no-confusing-arrow
      const goodsRequestList = this.goodsRequestList.map((item, i) =>
        i === index ? { ...item, quantity: value } : item,
      );
      this.handleOptionsParams({ goodsRequestList });
    }
  },

  onPopupChange() {
    this.setData({
      popupShow: !this.data.popupShow,
    });
  },
});
