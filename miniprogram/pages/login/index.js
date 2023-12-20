import {fetchHome} from '../../services/home/home';
import {fetchGoodsList} from '../../services/good/fetchGoods';
import Toast from 'tdesign-miniprogram/toast/index';


Page({
    data: {
        phoneNumber: '',        // 手机号
        inputSmsCode: '',         // 验证码
        smsCode: '',         // 验证码
        isCounting: false,  // 是否正在倒计时
        countdown: 60,     // 倒计时时间（秒）
        submitActive: false,
        smsCodeActive: true
    },


    onShow() {
    },

    onLoad() {
    },
    init() {
    },
    handlePhoneInput: function (event) {
        let value = event.detail.value;
        // 只允许输入11位数字
        let filteredValue = value.replace(/\D/g, '').slice(0, 11);
        this.setData({
            phoneNumber: filteredValue,
            smsCodeActive: filteredValue.length !== 11
        });
        this.checkLoginActive();
    },

    handleCodeInput: function (event) {
        let value = event.detail.value;
        // 只允许输入4位数字
        let filteredValue = value.replace(/\D/g, '').slice(0, 4);
        this.setData({
            inputSmsCode: filteredValue
        });

        this.checkLoginActive();
    },

    sendCode: function () {
        let that = this;

        // 模拟发送验证码请求
        Toast({
            context: this,
            selector: '#t-toast',
            message: '短信已发送(1111)',
            duration: 2000,
            icon: '',
        });
        // 在实际使用中，需要调用后端接口发送验证码
        // 这里使用定时器模拟倒计时60秒
        that.setData({
            isCounting: true,
            smsCode: "1111"
        });

        let timer = setInterval(function () {
            let currentCountdown = that.data.countdown;

            if (currentCountdown > 0) {
                that.setData({
                    countdown: currentCountdown - 1
                });
            } else {
                that.setData({
                    isCounting: false,
                    countdown: 60
                });
                clearInterval(timer);
            }
        }, 1000);
    },
    checkLoginActive() {
        this.setData({submitActive: this.data.inputSmsCode.length === 4 && this.data.phoneNumber.length === 11});
    },
    login: function () {
        let phoneNumber = this.data.phoneNumber;
        let inputSmsCode = this.data.inputSmsCode;

        // 在这里处理登录逻辑，可以调用后端接口进行验证
        if (this.data.smsCode !== this.data.inputSmsCode) {
            Toast({
                context: this,
                selector: '#t-toast',
                message: '请输入正确的验证码',
                duration: 2000,
                icon: '',
            });
            return
        }

        // 假设登录成功，保存用户信息到本地缓存
        let userInfo = {
            phoneNumber: phoneNumber
        };
        wx.setStorageSync('userInfo', userInfo);

        // 跳转到其他页面
        wx.switchTab({
            url: '/pages/home/home' // 替换成实际的页面路径
        });
    },

});
