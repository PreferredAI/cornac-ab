import { createApp } from 'vue'
import { createRouter, createWebHistory } from 'vue-router'
// import './style.css'
import './index.css'
import App from './App.vue'
import Welcome from './views/Welcome.vue'
import Setup from './views/Setup.vue'
import HelloWorld from './views/HelloWorld.vue'
import Dashboard from './views/Dashboard.vue'

const router = createRouter({
    history: createWebHistory(),
    routes: [
        { path: '/', component: Welcome },
        { path: '/setup', component: Setup },
        { path: '/hello', component: HelloWorld },
        { path: '/dashboard', component: Dashboard },
    ]
})

const app = createApp(App)

app.use(router)
app.mount('#app')