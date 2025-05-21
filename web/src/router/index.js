import Vue from 'vue'
import VueRouter from 'vue-router'

import store from '@/store'



Vue.use(VueRouter)

const routesAll = [
  {
    path: '/', redirect: '/auth/login'
    // path: '/', redirect: '/coverPage'
  },
  {
  path: '/auth',
  name: 'auth',
  component: () => import('@/views/Login/index.vue'),
  children: [

    {
      path: 'login',
      name: 'login',
      component: () => import('@/views/Login/components/loginForm.vue'),
      meta: {
        roles: 3,
      }
    },
    {
      path: 'register',
      name: 'register',
      component: () => import('@/views/Login/components/registerForm.vue'),
      meta: {
        roles: 3,
      }
    }
  ]},
{
  path: '/Shopping',
  name: 'Shopping',
  component: () => import('@/views/ShoppingPage/index.vue'),
  meta: {
    roles: 2,
  }
},
{
  path : '/HomePage',
  name: 'HomePage',
  component: () => import('@/views/HomePage/index.vue'),
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
      meta: {
        roles: 1,
      }
    },
    {
      path: 'ProductsMgmt',
      name: 'ProductsMgmt',
      component: () => import('@/views/MgmtPage/views/ProductsMgmt.vue'),
      meta: {
        roles: 1,
      }
    },
    {
      path: 'InformationMgmt',
      name: 'InformationMgmt',
      component: () => import('@/views/MgmtPage/views/InformationMgmt.vue'),
      meta: {
        roles: 1,
      }
    },
    {
      path: 'SeriesMgmt',
      name: 'SeriesMgmt',
      component: () => import('@/views/MgmtPage/views/SeriesMgmt.vue'),
      meta: {
        roles: 1,
      }
    },
    {
      path: 'HistoryOrder',
      name: 'HistoryOrder',
      component: () => import('@/views/MgmtPage/views/HistoryOrder.vue'),
      meta: {
        roles: 1,
      }
    },


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
          path: '/auth/login',
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
          path: '/auth/login',
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
