import Vue from 'vue'
import VueRouter from 'vue-router'

import LoginPage from '@/views/Login/index.vue'



Vue.use(VueRouter)

const routesAll = [
  {
    path: '/', redirect: '/login'
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
      meta: {
        roles: [0, 1, 2],
      }
    },
    {
      path: '/register',
      name: 'register',
      component: () => import('@/views/Login/components/registerForm.vue'),
      meta: {
        roles: [0, 1, 2],
      }
      
    }
  ]
},
{
  path: '/home',
  name: 'home',
  component: () => import('@/views/HomePage/index.vue'),
  meta: {
    roles: [0, 1, 2],
  }
  
  
},
{
  path : '/coverPage',
  name: 'coverPage',
  component: () => import('@/views/CoverPage/index.vue'),
  meta: {
    roles: [0, 1, 2],
  }
},
{
  path: '/MgmtPage',
  name: 'MgmtPage',
  component: () => import('@/views/MgmtPage/index.vue'),
  meta: {
    roles: [0, 1],
  }
}

]

// function setRoutes(routes) {
//   const newRoutes = []
//   routes.forEach((route) => {
//     if (route.meta.roles) {
//       route.children = setRoutes(route.children)
//     }
//     newRoutes.push(route)
//   })
//   return newRoutes
// }

//  setRoutes(routesAll)


const router = new VueRouter({
  mode: 'history',
  routes: routesAll
})

export default router
