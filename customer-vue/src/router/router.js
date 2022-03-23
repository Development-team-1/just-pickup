import Vue from 'vue';
import VueRouter from 'vue-router';
import jwt from "@/common/jwt";
import auth from "@/api/auth";

import HomeLayout from '../views/Layout/HomeLayout.vue';
import StoreLayout from "@/views/Layout/StoreLayout";
const ACCESS_TOKEN_NAME = "accessToken";
const EXPIRED_TIME_NAME = "expiredTime";

Vue.use(VueRouter);

const authCheck = async function (to, from, next) {
  if(to.path==="/login"){
    next();
    return;
  }
  try {
    if (jwt.isExpired()) {
      // refresh 호출
      await auth.requestReissue();
    } else {
      await auth.requestCheckAccessToken();
    }
  } catch (error) {
    await router.push("/login");
  }
  next();
};

const routes = [
  {
    path: '/',
    redirect: 'home',
    component: HomeLayout,
    children: [
      {
        path: "/home",
        beforeEnter: authCheck,
        name: 'home',
        component: () => import('../views/HomeView')
      },
      {
        path: "/search",
        beforeEnter: authCheck,
        name: 'search-store',
        component: () => import('../views/SearchStore')
      },
      {
        path: "/history",
        beforeEnter: authCheck,
        name: 'order-history',
        component: () => import('../views/OrderHistory')
      },
      {
        path: "/favorite",
        beforeEnter: authCheck,
        name: 'favorite-store',
        component: () => import('../views/FavoriteStore')
      },
      {
        path: "/notification",
        beforeEnter: authCheck,
        name: 'notification',
        component: () => import('../views/NotificationView')
      },
      // {
      //   path: "/item/:itemId",
      //   beforeEnter: authCheck,
      //   name: 'itemDetail',
      //   component: () => import('../views/ItemDetail')
      // },
      {
        path: "/order",
        beforeEnter: authCheck,
        name: 'orderPage',
        component: () => import('../views/OrderPage')
      },
      {
        path: "/mypage",
        beforeEnter: authCheck,
        name: 'mypage',
        component: () => import('../views/MyPage')
      },
      {
        path: "/order-detail/:orderId",
        beforeEnter: authCheck,
        name: 'order-detail',
        props: true,
        component: () => import('../views/OrderDetail')
      },
    ]
  },
  {
    path: '/login',
    component: () => import('../views/LoginPage'),
  },

  {
    path: '/store',
    redirect: 'store',
    component: StoreLayout,
    children: [
      {
        path: "/store/:storeId",
        name: "store",
        beforeEnter: authCheck,
        component: () => import('../views/StoreView'),
        props: true
      },
    ]
  },
  {
    path: '/item',
    redirect: 'item',
    component: StoreLayout,
    children: [
      {
        path: "/item/:itemId",
        beforeEnter: authCheck,
        name: 'itemDetail',
        component: () => import('../views/ItemDetail')
      },
    ]
  },
  {
    path: '/auth',
    name: 'auth',
    beforeEnter: async function (to, from, next) {
      localStorage.setItem(ACCESS_TOKEN_NAME, to.query.accessToken);
      localStorage.setItem(EXPIRED_TIME_NAME, to.query.expiredTime)
      next();
    },
    component: () => import('../views/Layout/AuthSuccess.vue')

  },
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
});

export default router;