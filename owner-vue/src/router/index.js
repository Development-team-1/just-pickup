import Vue from 'vue'
import VueRouter from 'vue-router'

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
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

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

export default router
