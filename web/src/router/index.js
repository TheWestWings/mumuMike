import Vue from 'vue'
import VueRouter from 'vue-router'

import LoginPage from '@/views/Login/index.vue'






Vue.use(VueRouter)

const routes = [
  {
    path: '/', redirect: '/home'
    // path: '/', redirect: '/coverPage'
  },
  {
  path: '/auth',
  name: 'auth',
  component: LoginPage,
  children: [

    {
      path: '/login',
      name: 'login',
      component: () => import('@/views/Login/components/loginForm.vue'),
    },
    {
      path: '/register',
      name: 'register',
      component: () => import('@/views/Login/components/registerForm.vue'),
    }
  ]
},
{
  path: '/home',
  name: 'home',
  component: () => import('@/views/HomePage/index.vue'),
},
{
  path : '/coverPage',
  name: 'coverPage',
  component: () => import('@/views/CoverPage/index.vue'),
},
{
  path: '/MgmtPage',
  name: 'MgmtPage',
  component: () => import('@/views/MgmtPage/index.vue'),
}

]

const router = new VueRouter({
  mode: 'history',
  routes
})

export default router
