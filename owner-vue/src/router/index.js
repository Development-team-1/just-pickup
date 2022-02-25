import Vue from 'vue'
import VueRouter from 'vue-router'

import DashboardLayout from "@/views/Layout/DashboardLayout";
import AuthLayout from "@/views/Layout/AuthLayout";

Vue.use(VueRouter)

const routes = [
  {
    path: '/dashboard',
    redirect: 'dashboard',
    component: DashboardLayout,
    children: [
      {
        path: "/dashboard",
        name: 'dashboard',
        component: () => import('./../views/Dashboard')
      },
      {
        path: '/category',
        name: 'category',
        component: () => import('./../views/Category')
      },
      {
        path: '/menu',
        name: 'menu',
        component: () => import('./../views/Menu')
      },
      {
        path: '/prev-order',
        name: 'prev-order',
        component: () => import('./../views/PrevOrder')
      },
      {
        path: '/order',
        name: 'order',
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
