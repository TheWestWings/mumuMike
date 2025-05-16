import axios from 'axios'
import store from '@/store'

const service = axios.create({
    baseURL: 'http://localhost:8080', 
    timeout: 15000,
    
})

service.interceptors.request.use(
    config => {
      let token = store.state.token
      if (token) {
        config.headers['Authorization'] = `Bearer ${token}`
      }
      return config
    },
    error => {
        console.error(error)

    }
  )

export default service



