import Vue from 'vue';
import VueRouter from 'vue-router';
import jwt from "@/common/jwt";
import auth from "@/api/auth";

import HomeLayout from '../views/Layout/HomeLayout.vue';
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
    await router.replace("/login");
  }
  next();
};

const routes = [
  {
    path: '/',
    redirect: 'home',
    beforeEnter: authCheck,
    component: HomeLayout,
    children: [
      {
        path: "/home",
        name: 'home',
        component: () => import('../views/HomeView')
      },
      {
        path: "/search",
        name: 'search-store',
        component: () => import('../views/SearchStore')
      },
      {
        path: "/history",
        name: 'order-history',
        component: () => import('../views/OrderHistory')
      },
      {
        path: "/favorite",
        name: 'favorite-store',
        component: () => import('../views/FavoriteStore')
      },
      {
        path: '/login',
        name: 'login',
        component: () => import('../views/LoginPage')
      },
      {
        path: "/item/:storeId/:itemId",
        name: 'itemDetail',
        component: () => import('../views/ItemDetail')
      },
      {
        path: "/order",
        name: 'orderPage',
        component: () => import('../views/OrderPage')
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