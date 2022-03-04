import Vue from 'vue';
import VueRouter from 'vue-router';

import HomeLayout from '../views/Layout/HomeLayout.vue';
const ACCESS_TOKEN_NAME = "accessToken";
const EXPIRED_TIME_NAME = "expiredTime";

Vue.use(VueRouter);

const auth = async function (to, from, next) {
  localStorage.setItem(ACCESS_TOKEN_NAME, to.query.accessToken);
  localStorage.setItem(EXPIRED_TIME_NAME, to.query.expiredTime)
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
        name: 'home',
        component: () => import('../views/HomeView')
      },
      {
        path: "/search",
        name: 'search-store',
        component: () => import('../views/SearchStore')
      }
    ]
  },
  {
    path: '/login',
    redirect: 'login',
    component: HomeLayout,
    children: [
      {
        path: "/login",
        name: 'login',
        component: () => import('../views/LoginPage')
      }
    ]
  },
  {
    path: '/auth',
    name: 'auth',
    beforeEnter: auth,
    component: () => import('../views/Layout/AuthSuccess.vue')

  },
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
});

export default router;