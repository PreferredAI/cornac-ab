<template>
    <div class="relative isolate px-6 pt-14 lg:px-8">
      <div class="mx-auto max-w-2xl py-32 sm:py-48 lg:py-56">
        <div class="hidden sm:mb-8 sm:flex sm:justify-center">
          <div class="relative rounded-full px-3 py-1 text-sm leading-6 text-gray-600 ring-1 ring-gray-900/10 hover:ring-gray-900/20">
            The best books, only found here! <a href="https://cornac.preferred.ai/" class="font-semibold text-indigo-600"><span class="absolute inset-0" aria-hidden="true" />View Here <span aria-hidden="true">&rarr;</span></a>
          </div>
        </div>
        <div class="text-center">
          <h1 class="text-4xl font-bold tracking-tight text-gray-900 sm:text-6xl">Welcome to Books A-B</h1>
          <p class="mt-6 text-lg leading-8 text-gray-600">Find the best books, rate them and get going.</p>
          
          <div v-if="!loggedInUserid" class="mt-10 flex items-center justify-center gap-x-6">
            <form class="space" action="#" method="POST">
              <div>
                <label for="userid" class="block text-sm font-medium leading-6 text-gray-900">Enter Username</label>
                <div class="mt-2">
                  <input id="userid" name="userid" v-model="userid" required class="block w-full rounded-md border-0 py-1.5 px-4 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:text-sm sm:leading-6">
                </div>
              </div>
              <div class="mt-4">
                <button type="submit" @click.stop.prevent="submit()" class="rounded-md bg-indigo-600 px-3.5 py-2.5 text-sm font-semibold text-white shadow-sm hover:bg-indigo-500 focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-indigo-600">Get Started</button>
              </div>
            </form>
          </div>
          <div v-else class="mt-4">
            <h3 class="font-semibold">Currently logged in as: {{ loggedInUserid }}</h3>
            <div class="mt-4">
              <a href="/"  type="submit" class="mx-2 rounded-md bg-indigo-600 px-8 py-2.5 text-sm font-semibold text-white shadow-sm hover:bg-indigo-500 focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-indigo-600">View Books</a>
              <button type="submit" @click.stop.prevent="logout()" class="mx-2 rounded-md bg-indigo-600 px-8 py-2.5 text-sm font-semibold text-white shadow-sm hover:bg-indigo-500 focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-indigo-600">Log Out</button>
            </div>
          </div>
        </div>
      </div>
    </div>
</template>

<script>
export default {
    name: 'Welcome',
    data() {
        return {
            userid: '',
            loggedInUserid: '',
        };
    },
    mounted() {
      if (localStorage.userid) {
        this.loggedInUserid = localStorage.userid;
      }
    },
    methods: {
        submit() {
            localStorage.setItem('userid', this.userid); // add to localstorage
            this.loggedInUserid = this.userid;
            // this.$router.push('/'); // redirect to books page
            this.$router.go();
        },
        logout() {
            localStorage.removeItem('userid'); // remove from localstorage
            this.$router.go(); // refresh welcome page to update state
        }
    }
}
</script>
