import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

const store = new Vuex.Store({
    state: {
        token: uni.getStorageSync('token') || '',
        userInfo: uni.getStorageSync('userInfo') || {},
        cart: uni.getStorageSync('cart') || [], // [{productId, count, spec, ...}]
        tableId: uni.getStorageSync('tableId') || ''
    },
    mutations: {
        SET_TOKEN(state, token) {
            state.token = token
            uni.setStorageSync('token', token)
        },
        SET_USER_INFO(state, userInfo) {
            state.userInfo = userInfo
            uni.setStorageSync('userInfo', userInfo)
        },
        SET_TABLE_ID(state, tableId) {
            state.tableId = tableId
            uni.setStorageSync('tableId', tableId)
        },
        ADD_TO_CART(state, product) {
            // Check if exists
            const existing = state.cart.find(item => item.id === product.id && item.spec === product.spec)
            if (existing) {
                existing.count += product.count
            } else {
                state.cart.push(product)
            }
            uni.setStorageSync('cart', state.cart)
        },
        REMOVE_FROM_CART(state, { id, spec }) {
            const index = state.cart.findIndex(item => item.id === id && item.spec === spec)
            if (index !== -1) {
                state.cart.splice(index, 1)
            }
            uni.setStorageSync('cart', state.cart)
        },
        UPDATE_CART_COUNT(state, { id, spec, count }) {
            const item = state.cart.find(item => item.id === id && item.spec === spec)
            if (item) {
                item.count = count
                if (item.count <= 0) {
                    const index = state.cart.indexOf(item)
                    state.cart.splice(index, 1)
                }
            }
            uni.setStorageSync('cart', state.cart)
        },
        CLEAR_CART(state) {
            state.cart = []
            uni.setStorageSync('cart', state.cart)
        }
    },
    getters: {
        cartCount: state => state.cart.reduce((acc, item) => acc + item.count, 0),
        cartTotal: state => state.cart.reduce((acc, item) => acc + item.price * item.count, 0)
    }
})

export default store
