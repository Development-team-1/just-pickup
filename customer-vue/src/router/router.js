import Vue from 'vue';
import VueRouter from 'vue-router';

import HomeLayout from '../views/Layout/HomeLayout.vue';

Vue.use(VueRouter);

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
  }
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
});

export default router;