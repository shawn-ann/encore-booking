import Toast from 'tdesign-miniprogram/toast/index';
import {fetchTickets} from '../../../services/ticket/fetchTickets';
import {wechatPayOrder} from './pay';
import { createOrder } from '../../../services/order/orderConfirm';
import {sendVerifyCode, verifyVerifyCode} from '../../../services/verify_code/sendVerifyCode';

const stripeImg = `https://cdn-we-retail.ym.tencent.com/miniapp/order/stripe.png`;

Page({
    data: {
        placeholder: '备注信息',
        stripeImg,
        loading: false,
        selectedSessionIndex: 0,
        selectedTicketIndex: 0,
        goods: {
            name: '',
            sessions: [],
        },
        settleDetailData: {
            storeGoodsList: [], //正常下单商品列表
            outOfStockGoodsList: [], //库存不足商品
            abnormalDeliveryGoodsList: [], // 不能正常配送商品
            inValidGoodsList: [], // 失效或者库存不足
            limitGoodsList: [], //限购商品
        }, // 获取结算页详情 data
        orderCardList: [], // 仅用于商品卡片展示
        goodsRequest: {},
        popupShow: false, // 不在配送范围 失效 库存不足 商品展示弹框
        notesPosition: 'center',
        storeInfoList: [],
        storeNoteIndex: 0, //当前填写备注门店index
        promotionGoodsList: [], //当前门店商品列表(优惠券)
        currentStoreId: null, //当前优惠券storeId
        buyerList: [
            {name: "王XXX", mobile: "13664227822", idNumber: "110133199901017890"},
        ],
        canPay: false,
        dialogVisible: false,
        smsCountDown: 0,
        smsCode: "",
        inputSmsCode: ""
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
        const {goodsRequest} = this;
        this.handleOptionsParams({goodsRequest});
    },
    onChangeSession(event) {
        const {value} = event.detail;
        this.setData({selectedSessionIndex: value, selectedTicketIndex: 0});
        this.updateTotalAmount();
    },
    onChangeTicketCategory(event) {
        const {value} = event.detail;
        this.setData({selectedTicketIndex: value});
        this.updateTotalAmount();
    },
    // 处理不同情况下跳转到结算页时需要的参数
    handleOptionsParams(options) {
        let {concertId} = options; // 商品列表
        let that = this;
        fetchTickets(concertId).then(res => {
            this.setData({
                loading: false,
                goods: res
            });
            that.updateTotalAmount();
        });
    },
    initData(resData) {
        // 转换商品卡片显示数据
        const data = this.handleResToGoodsCard(resData);

        this.setData({settleDetailData: data});
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
            this.setData({popupShow: true});
            return true;
        }
        this.setData({popupShow: false});
        if (data.settleType === 0) {
            return true;
        }
        return false;
    },
    sendSms() {
        if (this.data.smsCountDown > 0) {
            return;
        }
        sendVerifyCode('', false).then(res => {
            this.setData({smsCountDown: 60, smsCode: res});
            Toast({
                context: this,
                selector: '#t-toast',
                message: '短信已发送',
                duration: 2000,
                icon: '',
            });
            let that = this;
            // 开始倒计时
            let timer = setInterval(function () {
                let currentCountdown = that.data.smsCountDown;

                if (currentCountdown > 0) {
                    // 倒计时减1秒
                    that.setData({
                        smsCountDown: currentCountdown - 1
                    });
                } else {
                    // 倒计时为0，清除定时器
                    clearInterval(timer);
                }
            }, 1000);
        });
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
                    titlePrefixTags: item.tagText ? [{text: item.tagText}] : [],
                    num: item.quantity,
                    skuId: item.skuId,
                    spuId: item.spuId,
                    storeId: item.storeId,
                });
            });

            orderCardList.push(orderCard);
        });

        this.setData({orderCardList});
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
        const {storeInfoList, storeNoteIndex} = this.data;
        this.tempNoteInfo[storeNoteIndex] = this.noteInfo[storeNoteIndex];
        storeInfoList[storeNoteIndex].remark = this.noteInfo[storeNoteIndex];

        this.setData({
            dialogShow: false,
            storeInfoList,
        });
    },
    onNoteCancel() {
        // 备注信息 取消按钮
        const {storeNoteIndex} = this.data;
        this.noteInfo[storeNoteIndex] = this.tempNoteInfo[storeNoteIndex];
        this.setData({
            dialogShow: false,
        });
    },

    onSureCommit() {
        // 商品库存不足继续结算
        const {settleDetailData} = this.data;
        const {outOfStockGoodsList, storeGoodsList, inValidGoodsList} =
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
            const goodsRequest = filterOutGoodsList.concat(filterStoreGoodsList);
            this.handleOptionsParams({goodsRequest});
        }
    },

    confirmOrder() {
        if (!this.buyerValidation()) {
            return;
        }
        this.setData({
            dialogVisible: true,
            inputSmsCode: ""
        });
    },

    confirmHandle() {
        if (this.data.smsCode === "" || this.data.smsCode !== this.data.inputSmsCode) {
            Toast({
                context: this,
                selector: '#t-toast',
                message: '请输入正确的验证码',
                duration: 2000,
                icon: '',
            });
            return
        }
        let that = this;
        verifyVerifyCode('', this.data.inputSmsCode).then(res => {
            that.setData({
                dialogVisible: false
            });
            that.submitOrder();
        })

    },
    cancelHandle() {
        this.setData({
            dialogVisible: false,
            labelValue: '',
        });
    },
    // 提交订单
    submitOrder() {
        this.payLock = true;
        let quotaId = this.data.goods.sessions[this.data.selectedSessionIndex].tickets[this.data.selectedTicketIndex].quotaId;
        const params = {
            quotaId: quotaId,
            buyerList: this.data.buyerList
        };
        createOrder(params).then(
            (res) => {
                this.payLock = false;
                this.handlePay(res);
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
                        wx.redirectTo({url: '/order/list'});
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
                        wx.redirectTo({url: '/order/list'});
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
    handlePay(data) {
        const {payInfo, orderNumber, totalFee} = data;
        const payOrderInfo = {
            payInfo: payInfo,
            orderId: orderNumber,
            orderAmt: totalFee,
            payAmt: totalFee,
            interactId: orderNumber,
            tradeNo: orderNumber,
            transactionId: orderNumber,
        };

        wechatPayOrder(payOrderInfo).then(res =>{
            // 支付成功
            wx.redirectTo({url: `/pages/order/pay-result/index`});
        });
    },

    hide() {
        // 隐藏 popup
        this.setData({
            'settleDetailData.abnormalDeliveryGoodsList': [],
        });
    },

    onGoodsNumChange(e) {
        const {
            detail: {value},
            currentTarget: {
                dataset: {goods},
            },
        } = e;
        const index = this.goodsRequest.findIndex(
            ({storeId, spuId, skuId}) =>
                goods.storeId === storeId &&
                goods.spuId === spuId &&
                goods.skuId === skuId,
        );
        if (index >= 0) {
            // eslint-disable-next-line no-confusing-arrow
            const goodsRequest = this.goodsRequest.map((item, i) =>
                i === index ? {...item, quantity: value} : item,
            );
            this.handleOptionsParams({goodsRequest});
        }
    },

    onPopupChange() {
        this.setData({
            popupShow: !this.data.popupShow,
        });
    },

    addBuyer() {
        let canAdd = this.buyerValidation();
        if (!canAdd) {
            return;
        }
        if (this.data.buyerList.length > this.data.goods.remainingQuantity) {
            Toast({
                context: this,
                selector: '#t-toast',
                message: "库存不足",
                duration: 3000,
                icon: '',
            });
            return;
        }
        let buyerList = this.data.buyerList.slice();
        buyerList.push({name: "", mobile: "", idNumber: ""});

        this.setData({buyerList: buyerList});
        this.updateTotalAmount();
    },
    updateTotalAmount() {
        let price = parseInt(this.data.goods.sessions[this.data.selectedSessionIndex].tickets[this.data.selectedTicketIndex].price);
        let amount = price * this.data.buyerList.length;
        this.setData({totalPayAmount: amount});
    },
    handleNameChange(event) {
        let index = event.currentTarget.dataset.index; // 获取当前表单的索引
        let value = event.detail.value; // 获取输入框的值

        let buyerList = this.data.buyerList.slice(); // 获取购买人信息列表的副本
        buyerList[index].name = value; // 更新手机号字段

        this.setData({
            buyerList: buyerList // 更新购买人信息列表
        });
    },
    handleSmsCodeInput(event) {
        let value = event.detail.value; // 获取输入框的值

        this.setData({
            inputSmsCode: value // 更新购买人信息列表
        });
    },
    handleMobileInput(event) {
        let index = event.currentTarget.dataset.index; // 获取当前表单的索引
        let value = event.detail.value; // 获取输入框的值

        // 只允许输入11位数字
        let filteredValue = value.replace(/\D/g, '').slice(0, 11);

        let buyerList = this.data.buyerList.slice(); // 获取购买人信息列表的副本
        buyerList[index].mobile = filteredValue; // 更新手机号字段

        this.setData({
            buyerList: buyerList // 更新购买人信息列表
        });
    },
    // 身份证号输入框的变化事件处理函数
    handleIDNumberChange: function (event) {
        let index = event.currentTarget.dataset.index; // 获取当前表单的索引
        let value = event.detail.value; // 获取输入框的值

        let first = value.slice(0, 17).replace(/[^0-9]/g, '');
        let second = value.slice(17, 18).replace(/[^0-9xX]/g, '');
        let filteredValue = first + second;
        if (second === "x" || second === "X") {
            filteredValue = first + "X";
        }
        if (filteredValue.length < 18) {
            filteredValue = value.replace(/[^0-9]/g, '');
        }
        let buyerList = this.data.buyerList.slice(); // 获取购买人信息列表的副本
        buyerList[index].idNumber = filteredValue; // 更新身份证号字段

        this.setData({
            buyerList: buyerList // 更新购买人信息列表
        });
    },
    deleteBuyer: function (event) {
        let index = event.currentTarget.dataset.index; // 获取当前表单的索引
        let buyerList = this.data.buyerList.slice(); // 获取购买人信息列表的副本
        if (buyerList.length == 1) {
            Toast({
                context: this,
                selector: '#t-toast',
                message: '至少需要一个购票人',
                duration: 2000,
                icon: '',
            });
            return;
        }
        buyerList.splice(index, 1); // 从数组中移除相应记录

        this.setData({
            buyerList: buyerList // 更新购买人信息列表
        });

        this.updateTotalAmount();
    },
    buyerValidation() {
        let buyerList = this.data.buyerList.slice(); // 获取购买人信息列表的副本
        for (let index = 0; index < buyerList.length; index++) {
            let buyer = buyerList[index];
            if (buyer.name === "") {
                this.buyerValidateFailed((index + 1), "姓名");
                return false;
            }
            if (buyer.idNumber === "" || buyer.idNumber.length < 18) {
                this.buyerValidateFailed((index + 1), "有效身份证号");
                return false;
            }
            if (buyer.mobile === "" || buyer.mobile.length < 11) {
                this.buyerValidateFailed((index + 1), "有效手机号");
                return false;
            }
        }
        this.setData({canPay: true});
        return true;
    },
    buyerValidateFailed(num, field) {
        Toast({
            context: this,
            selector: '#t-toast',
            message: "请输入第" + num + "个购票人的" + field,
            duration: 3000,
            icon: '',
        });
        this.setData({canPay: false});
    }
});
