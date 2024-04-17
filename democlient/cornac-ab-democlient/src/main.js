import { createApp } from 'vue'
import { createRouter, createWebHistory } from 'vue-router'
import './style.css'
import App from './App.vue'
import Welcome from './views/Welcome.vue'
import Books from './views/Books.vue'

const router = createRouter({
    history: createWebHistory(),
    routes: [
        { path: '/login', component: Welcome },
        { path: '/', name: 'Books', component: Books },
    ]
})

const app = createApp(App)

app.use(router)
app.mount('#app')