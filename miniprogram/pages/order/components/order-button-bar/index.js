import Toast from 'tdesign-miniprogram/toast/index';
import Dialog from 'tdesign-miniprogram/dialog/index';
import {OrderButtonTypes} from '../../config';
import {
    cancelOrder, applyRefund
} from '../../../../services/order/orderDetail';

Component({
    options: {
        addGlobalClass: true,
    },
    properties: {
        order: {
            type: Object,
            observer(order) {
                const buttonsRight = []
                switch (order.status) {
                    case "CREATED":
                        buttonsRight.push({type: OrderButtonTypes.CANCEL, name: "取消订单"})
                        break
                    case "PAID":
                        buttonsRight.push({type: OrderButtonTypes.APPLY_REFUND, name: "申请退款"})
                        break
                }
                // 订单的button bar 不显示申请售后按钮
                const deleteBtnIndex = buttonsRight.findIndex((b) => b.type === OrderButtonTypes.DELETE);
                let buttonsLeft = [];
                if (deleteBtnIndex > -1) {
                    buttonsLeft = buttonsRight.splice(deleteBtnIndex, 1);
                }
                this.setData({
                    buttons: {
                        left: buttonsLeft,
                        right: buttonsRight,
                    },
                });
            },
        },
        goodsIndex: {
            type: Number,
            value: null,
        },
        isBtnMax: {
            type: Boolean,
            value: false,
        },
    },

    data: {
        order: {},
        buttons: {
            left: [],
            right: [],
        },
    },

    methods: {
        // 点击【订单操作】按钮，根据按钮类型分发
        onOrderBtnTap(e) {
            const {type} = e.currentTarget.dataset;
            switch (type) {
                case OrderButtonTypes.CANCEL:
                    this.onCancel(this.data.order);
                    break;
                case OrderButtonTypes.PAY:
                    this.onPay(this.data.order);
                    break;
                case OrderButtonTypes.APPLY_REFUND:
                    this.onApplyRefund(this.data.order);
                    break;
            }
        },

        onCancel(order) {
            let orderId = order.id;
            const that = this;
            cancelOrder(orderId).then(res => {
                Toast({
                    context: this,
                    selector: '#t-toast',
                    message: '订单已取消',
                    icon: 'check-circle',
                });
                that.triggerEvent('refresh');
            })
        },

        onPay() {
            Toast({
                context: this,
                selector: '#t-toast',
                message: '你点击了去支付',
                icon: 'check-circle',
            });
        },

        onBuyAgain() {
            Toast({
                context: this,
                selector: '#t-toast',
                message: '你点击了再次购买',
                icon: 'check-circle',
            });
        },

        onApplyRefund(order) {
            let orderId = order.id;
            const that = this;
            applyRefund(orderId).then(res => {
                Toast({
                    context: this,
                    selector: '#t-toast',
                    message: '退款已申请',
                    icon: 'check-circle',
                });
                that.triggerEvent('refresh');
            })
        },

    },
});
