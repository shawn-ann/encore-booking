import Toast from 'tdesign-miniprogram/toast/index';
import {sendSms, loginSubmit} from '../../services/login/login';
import {fetchGoodsList} from "../../services/good/fetchGoods";


Page({
    data: {
        mobile: '',        // 手机号
        inputSmsCode: '',         // 验证码
        smsCode: '',         // 验证码
        isCounting: false,  // 是否正在倒计时
        countdown: 60,     // 倒计时时间（秒）
        submitActive: false, smsCodeActive: true
    },


    onShow() {
    },

    onLoad() {
    }, init() {
    }, handlePhoneInput: function (event) {
        let value = event.detail.value;
        // 只允许输入11位数字
        let filteredValue = value.replace(/\D/g, '').slice(0, 11);
        this.setData({
            mobile: filteredValue, smsCodeActive: filteredValue.length !== 11
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
    }, sendCode() {
        let that = this;
        sendSms(this.data.mobile).then(smsCode => {
            if (!smsCode) {
                return;
            }
            Toast({
                context: this, selector: '#t-toast', message: '短信验证码已发送', duration: 2000, icon: '',
            });
            // 在实际使用中，需要调用后端接口发送验证码
            // 这里使用定时器模拟倒计时60秒
            that.setData({
                isCounting: true, smsCode: smsCode
            });

            let timer = setInterval(function () {
                let currentCountdown = that.data.countdown;

                if (currentCountdown > 0) {
                    that.setData({
                        countdown: currentCountdown - 1
                    });
                } else {
                    that.setData({
                        isCounting: false, countdown: 60
                    });
                    clearInterval(timer);
                }
            }, 1000);
        });

    }, checkLoginActive() {
        this.setData({submitActive: this.data.inputSmsCode.length === 4 && this.data.mobile.length === 11});
    }, login: function () {
        let mobile = this.data.mobile;
        let inputSmsCode = this.data.inputSmsCode;

        // 在这里处理登录逻辑，可以调用后端接口进行验证
        if (this.data.smsCode !== this.data.inputSmsCode) {
            Toast({
                context: this, selector: '#t-toast', message: '请输入正确的验证码', duration: 2000, icon: '',
            });
            return
        }
        loginSubmit(mobile, inputSmsCode).then(response => {
            // 假设登录成功，保存用户信息到本地缓存
            let userInfo = {
                token: response.token,
                name: response.name,
                mobile: response.mobile
            };
            wx.setStorageSync('userInfo', userInfo);

            // 跳转到其他页面
            wx.switchTab({
                url: '/pages/home/home'
            });
        });

    },

});
