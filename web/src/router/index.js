import Vue from 'vue'
import VueRouter from 'vue-router'

import LoginPage from '@/views/Login/index.vue'


Vue.use(VueRouter)

const routes = [{
  path: '/login',
  name: 'login',
  component: LoginPage,
},

]

const router = new VueRouter({
  routes
})

export default router
