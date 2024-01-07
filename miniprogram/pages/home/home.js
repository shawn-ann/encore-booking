import {fetchHome} from '../../services/home/home';
import {fetchConcerts} from '../../services/concert/fetchConcerts';
import Toast from 'tdesign-miniprogram/toast/index';

const obj2Params = (obj = {}, encode = false) => {
    const result = [];
    Object.keys(obj).forEach((key) =>
        result.push(`${key}=${encode ? encodeURIComponent(obj[key]) : obj[key]}`),
    );

    return result.join('&');
};

Page({
    data: {
        imgSrcs: [],
        tabList: [],
        concertList: [],
        concertListLoadStatus: 0,
        pageLoading: false,
        current: 1,
        autoplay: true,
        duration: '500',
        interval: 5000,
        navigation: {type: 'dots'},
        swiperImageProps: {mode: 'scaleToFill'},
    },

    goodListPagination: {
        index: 0,
        num: 20,
    },

    privateData: {
        tabIndex: 0,
    },

    onShow() {
        this.getTabBar().init();
    },

    onLoad() {
        this.init();
    },

    onReachBottom() {
        if (this.data.concertListLoadStatus === 0) {
            this.loadConcertList();
        }
    },

    onPullDownRefresh() {
        this.init();
    },

    init() {
        this.loadHomePage();
    },

    loadHomePage() {
        // wx.stopPullDownRefresh();

        this.setData({
            pageLoading: true,
        });
        this.loadConcertList();
    },

    tabChangeHandle(e) {
        this.privateData.tabIndex = e.detail;
        this.loadConcertList(true);
    },

    onReTry() {
        this.loadConcertList();
    },

    loadConcertList() {
        try {
            fetchConcerts().then(res => {
                this.setData({
                    concertList: res,
                    pageLoading: false,
                });
            });
        } catch (err) {
            this.setData({concertListLoadStatus: 3});
        }
    },

    goodListClickHandle(e) {
        // const { index } = e.detail;
        // const { spuId } = this.data.concertList[index];
        // wx.navigateTo({
        //   url: `/pages/concert/details/index?spuId=${spuId}`,
        // });
    },

    buy(e) {
        const concertId = e.currentTarget.dataset.id;
        const path = `/pages/order/order-confirm/index?concertId=` + concertId;
        wx.navigateTo({
            url: path,
        });
    },

    navToSearchPage() {
        wx.navigateTo({url: '/pages/concert/search/index'});
    },

    navToActivityDetail({detail}) {
        const {index: promotionID = 0} = detail || {};
        wx.navigateTo({
            url: `/pages/promotion-detail/index?promotion_id=${promotionID}`,
        });
    },
});
