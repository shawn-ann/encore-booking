import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

/* Layout */
import Layout from '@/layout'

export const constantRoutes = [
  {
    path: '/redirect',
    component: Layout,
    hidden: true,
    children: [
      {
        path: '/redirect/:path(.*)',
        component: () => import('@/views/redirect/index')
      }
    ]
  },
  {
    path: '/login',
    component: () => import('@/views/login/index'),
    hidden: true
  },
  {
    path: '/auth-redirect',
    component: () => import('@/views/login/auth-redirect'),
    hidden: true
  },
  {
    path: '/404',
    component: () => import('@/views/error-page/404'),
    hidden: true
  },
  {
    path: '/401',
    component: () => import('@/views/error-page/401'),
    hidden: true
  },
  {
    path: '/',
    component: Layout,
    redirect: '/dashboard',
    children: [
      {
        path: 'dashboard',
        component: () => import('@/views/dashboard/index'),
        name: '首页',
        meta: { title: '首页', icon: 'dashboard', affix: true }
      }
    ]
  },
  {
    path: '/concert',
    component: Layout,
    redirect: '/concert/list',
    name: '演唱会',
    meta: {
      title: '演唱会',
      icon: 'el-icon-s-help'
    },
    children: [
      {
        path: 'list',
        component: () => import('@/views/concert/list'),
        name: '演唱会管理',
        meta: { title: '演唱会管理', icon: 'list', affix: true }
      },
      // {
      //   path: 'session/list',
      //   component: () => import('@/views/concert/session/list'),
      //   name: '场次管理',
      //   meta: { title: '场次管理', icon: 'list' }
      // },
      // {
      //   path: 'ticket-category/list',
      //   component: () => import('@/views/concert/ticket-category/list'),
      //   name: '票档管理',
      //   meta: { title: '票档管理', icon: 'list' }
      // },
      {
        path: 'inventory/:concertId(\\d+)',
        component: () => import('@/views/concert/inventory/list'),
        name: '库存管理',
        meta: { title: '库存管理', icon: 'list' },
        hidden: true
      }
    ]
  },
  {
    path: '/agent',
    component: Layout,
    redirect: '/agent/list',
    name: '代理商',
    meta: {
      title: '代理商',
      icon: 'el-icon-s-help'
    },
    children: [
      {
        path: 'list',
        component: () => import('@/views/agent/list'),
        name: '代理商管理',
        meta: { title: '代理商管理', icon: 'list' }
      },
      {
        path: 'quota/list',
        component: () => import('@/views/agent/quota/list'),
        name: '配额管理',
        meta: { title: '配额管理', icon: 'list' }
      }
    ]
  },
  {
    path: '/order',
    component: Layout,
    redirect: '/order/list',
    name: '订单',
    meta: {
      title: '订单',
      icon: 'el-icon-s-help'
    },
    children: [
      {
        path: 'list',
        component: () => import('@/views/order/list'),
        name: '订单管理',
        meta: { title: '订单管理', icon: 'list' }
      }
    ]
  }
]

/**
 * asyncRoutes
 * the routes that need to be dynamically loaded based on user roles
 */
export const asyncRoutes = [
  // 404 page must be placed at the end !!!
  { path: '*', redirect: '/404', hidden: true }
]

const createRouter = () => new Router({
  // mode: 'history', // require service support
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRoutes
})

const router = createRouter()

// Detail see: https://github.com/vuejs/vue-router/issues/1234#issuecomment-357941465
export function resetRouter() {
  const newRouter = createRouter()
  router.matcher = newRouter.matcher // reset router
}

export default router
