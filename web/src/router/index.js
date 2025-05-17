import Vue from 'vue'
import VueRouter from 'vue-router'

import LoginPage from '@/views/Login/index.vue'
import store from '@/store'



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
        roles: 3,
      }
    },
    {
      path: '/register',
      name: 'register',
      component: () => import('@/views/Login/components/registerForm.vue'),
      meta: {
        roles: 3,
      }
      
    }
  ]
},
{
  path: '/home',
  name: 'home',
  component: () => import('@/views/HomePage/index.vue'),
  meta: {
    roles: 2,
  }
  
  
},
{
  path : '/coverPage',
  name: 'coverPage',
  component: () => import('@/views/CoverPage/index.vue'),
  meta: {
    roles: 3,
    
  }
},
{
  path: '/MgmtPage',
  name: 'MgmtPage',
  component: () => import('@/views/MgmtPage/index.vue'),
  meta: {
    roles: 1,
    requiresAuth: true
  },
  children: [
    {
      path: 'OrderMgmt',
      name: 'OrderMgmt',
      component: () => import('@/views/MgmtPage/views/OrderMgmt.vue'),
      meta: {
        roles: 1,
      }
    },
    {
      path: 'UsersMgmt',
      name: 'UsersMgmt',
      component: () => import('@/views/MgmtPage/views/UsersMgmt.vue'),
    }
  ]
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
//   return newRoutes77777777
// }

//  setRoutes(routesAll)




const router = new VueRouter({
  mode: 'history',
  routes: routesAll
})

  router.beforeEach((to, from, next) => {
    console.log('to', to)
    console.log('from', from)
    console.log(to.meta.roles)
    console.log('store.state.role', store.state.role)
    if (to.meta.roles === 3) {
      next()
    }
    else if (to.meta.roles === 2) {
      if(store.state.role <= 2) {
        next()
      }
      else {
        next({
          path: '/login',
          query: { redirect: to.path }
        })
      }
    }
    else if (to.meta.roles === 1) {
      if(store.state.role <= 1) {
        next()
      }
      else if(store.state.role === 2) {
        next({
          path: from.path,
        })
      }
      else {
        next({
          path: '/login',
          query: { redirect: to.path }
        })
      }
    }
    else {
      next({
        path: from.path,
      })
    }
})



export default router
