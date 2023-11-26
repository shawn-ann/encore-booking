Component({
    externalClasses: ['wr-class', 'header-class', 'title-class'],

    options: {
        multipleSlots: true,
    },

    relations: {

    },

    created() {
        this.children = [];
    },

    properties: {
        buyerList: {
            type: Array,
            value: [],
        },
    },

    data: {
        buyerList: [
            {name: "王XXX", phoneNumber: "123456789", idNumber: "110133199901017890"},
            {name: "李XXX", phoneNumber: "", idNumber: ""},
        ]
    },

    methods: {

        addBuyer() {
            let buyerList = this.data.buyerList.slice();
            buyerList.push({name: "", phoneNumber: "", idNumber: ""});
            this.setData({buyerList: buyerList});
        },
        handlePhoneNumberInput(event) {
            let index = event.currentTarget.dataset.index; // 获取当前表单的索引
            let value = event.detail.value; // 获取输入框的值

            // 只允许输入11位数字
            let filteredValue = value.replace(/\D/g, '').slice(0, 11);

            let buyerList = this.data.buyerList.slice(); // 获取购买人信息列表的副本
            buyerList[index].phoneNumber = filteredValue; // 更新手机号字段

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
                filteredValue = first +"X";
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
            buyerList.splice(index, 1); // 从数组中移除相应记录

            this.setData({
                buyerList: buyerList // 更新购买人信息列表
            });
        }
    },
});
