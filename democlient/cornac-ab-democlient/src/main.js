import { createApp } from 'vue'
import { createRouter, createWebHistory } from 'vue-router'
import './style.css'
import App from './App.vue'
import Welcome from './views/Welcome.vue'
import Books from './views/Books.vue'
import BookDetails from './views/BookDetails.vue'

const router = createRouter({
    history: createWebHistory(),
    routes: [
        { path: '/login', component: Welcome },
        { path: '/', name: 'Books', component: Books },
        { path: '/bookdetails/:bookid', name: 'BookDetails', component: BookDetails },
    ]
})

const app = createApp(App)

app.use(router)
app.mount('#app')