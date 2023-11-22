Component({
  externalClasses: ['wr-class'],
  useStore: [],
  properties: {
    stock: {
      type: null,
      value: '',
      observer(stock) {
        this.setData({ stock })
      },
    },
  },

  data: {
    stock: 0,
  },
});
