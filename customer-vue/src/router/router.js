import Vue from 'vue';
import VueRouter from "vue-router";

import HomeLayout from "@/views/Layout/HomeLayout";

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    redirect: 'home',
    component: HomeLayout,
    children: [
      {
        path: "/home",
        name: 'TestView',
        component: () => import('./../views/TestView')
      }
    ]
  }
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

export default router;