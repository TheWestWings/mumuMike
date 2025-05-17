import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    role: 3,
    token: null,
    username: null,
    email: null,
    phone: null,
    password: null,
  },
  getters: {
  },
  mutations: {
    setRole(state, role) {
      state.role = role
    },
    clearRole(state) {
      state.role = null
    },
    setToken(state, token) {
      state.token = token
    },
    clearToken(state) {
      state.token = null
    },
    setUsername(state, username) {
      state.username = username
    },
    clearUsername(state) {
      state.username = null
    },
    setEmail(state, email) {
      state.email = email
    },
    clearEmail(state) {
      state.email = null
    },
    setPhone(state, phone) {
      state.phone = phone
    },
    clearPhone(state) {
      state.phone = null
    },
    setPassword(state, password) {
      state.password = password
    },
    clearPassword(state) {
      state.password = null
    },

    clearAll(state) {
      state.role = null
      state.token = null
      state.username = null
      state.email = null
      state.phone = null
      state.password = null
    }

  },
  actions: {
  },
  modules: {

  }
})
