import Vue from 'vue'
import VueRouter from 'vue-router'

import DashboardLayout from "@/views/Layout/DashboardLayout";
import AuthLayout from "@/views/Layout/AuthLayout";

import jwt from "../common/jwt.js";
import auth from "../api/auth.js";

Vue.use(VueRouter)

const authCheck = async function (to, from, next) {
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
    path: '/dashboard',
    redirect: 'dashboard',
    component: DashboardLayout,
    children: [
      {
        path: '/category',
        name: 'category',
        beforeEnter: authCheck,
        component: () => import('./../views/Category')
      },
      {
        path: '/menu',
        name: 'menu',
        beforeEnter: authCheck,
        component: () => import('./../views/Menu')
      },
      {
        path: '/prev-order',
        name: 'prev-order',
        beforeEnter: authCheck,
        component: () => import('./../views/PrevOrder')
      },
      {
        path: '/order',
        name: 'order',
        beforeEnter: authCheck,
        component: () => import('./../views/Order.vue')
      }
    ]
  },
  {
    path: '/',
    redirect: 'login',
    component: AuthLayout,
    children: [
      {
        path: '/login',
        name: 'login',
        component: () => import('./../views/LoginUser.vue')
      },
      {
        path: '/register',
        name: 'register',
        component: () => import('./../views/RegisterUser.vue')
      }
    ]
  }
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

export default router