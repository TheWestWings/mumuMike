import Vue from 'vue'
import Vuex from 'vuex'
import carModule from './car'

Vue.use(Vuex)

export default new Vuex.Store({
    state: {
        id: localStorage.getItem('id') || null,
        role: localStorage.getItem('role') || 3,
        token: localStorage.getItem('token') || null,
        username: localStorage.getItem('username') || null,
        email: localStorage.getItem('email') || null,
        phone: localStorage.getItem('phone') || null,
        pswd: localStorage.getItem('pswd') || null,
        avatar: localStorage.getItem('avatar') || null,
    },
    getters: {
    },
    mutations: {
        setId(state, id) {
            state.id = id
            localStorage.setItem('id', id)
        },
        clearId(state) {
            state.id = null
            localStorage.removeItem('id')
        },
        setRole(state, role) {
            state.role = role
            localStorage.setItem('role', role)
        },
        clearRole(state) {
            state.role = null
            localStorage.removeItem('role')
        },
        setToken(state, token) {
            state.token = token
            localStorage.setItem('token', token)
        },
        clearToken(state) {
            state.token = null
            localStorage.removeItem('token')
        },
        setUsername(state, username) {
            state.username = username
            localStorage.setItem('username', username)
        },
        clearUsername(state) {
            state.username = null
            localStorage.removeItem('username')
        },
        setEmail(state, email) {
            state.email = email
            localStorage.setItem('email', email)
        },
        clearEmail(state) {
            state.email = null
            localStorage.removeItem('email')
        },
        setPhone(state, phone) {
            state.phone = phone
            localStorage.setItem('phone', phone)
        },
        clearPhone(state) {
            state.phone = null
            localStorage.removeItem('phone')
        },
        setPswd(state, pswd) {
            state.pswd = pswd
            localStorage.setItem('pswd', pswd)
        },
        clearpswd(state) {
            state.pswd = null
            localStorage.removeItem('pswd')
        },
        setAvatar(state, avatar) {
            state.avatar = avatar
            localStorage.setItem('avatar', avatar)
        },
        clearAvatar(state) {
            state.avatar = null
            localStorage.removeItem('avatar')
        },

        clearAll(state) {
            state.role = null
            state.token = null
            state.username = null
            state.email = null
            state.phone = null
            state.pswd = null
            state.id = null

            localStorage.removeItem('id')
            localStorage.removeItem('role')
            localStorage.removeItem('token')
            localStorage.removeItem('username')
            localStorage.removeItem('email')
            localStorage.removeItem('phone')
            localStorage.removeItem('pswd')
        }

    },
    actions: {
    },
    modules: {
        car: carModule
    }
})
