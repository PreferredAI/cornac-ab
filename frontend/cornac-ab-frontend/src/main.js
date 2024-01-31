import { createApp } from 'vue'
import { createRouter, createWebHistory } from 'vue-router'
import { setupCalendar } from 'v-calendar';
// import './style.css'
import './index.css'
import App from './App.vue'
import Welcome from './views/Welcome.vue'
import Setup from './views/Setup.vue'
import Dashboard from './views/Dashboard.vue'
import Evaluation from './views/Evaluation.vue'

const router = createRouter({
    history: createWebHistory(),
    routes: [
        { path: '/', component: Welcome },
        { path: '/setup', component: Setup },
        { path: '/dashboard', component: Dashboard },
        { path: '/evaluation', component: Evaluation },
    ]
})

const app = createApp(App)

app.use(router)
app.mount('#app')

app.use(setupCalendar, {})