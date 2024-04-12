<script setup>
import BookDetailsModal from '../components/BookDetailsModal.vue';
import BookHistory from '../components/BookHistory.vue';
import { getRecommendations, postFeedback } from '../services';
const modal = ref(null);
</script>

<template>
      <!-- <div class="absolute inset-x-0 -top-40 -z-10 transform-gpu overflow-hidden blur-3xl sm:-top-80" aria-hidden="true">
        <div class="relative left-[calc(50%-11rem)] aspect-[1155/678] w-[36.125rem] -translate-x-1/2 rotate-[30deg] bg-gradient-to-tr from-[#ff80b5] to-[#9089fc] opacity-30 sm:left-[calc(50%-30rem)] sm:w-[72.1875rem]" style="clip-path: polygon(74.1% 44.1%, 100% 61.6%, 97.5% 26.9%, 85.5% 0.1%, 80.7% 2%, 72.5% 32.5%, 60.2% 62.4%, 52.4% 68.1%, 47.5% 58.3%, 45.2% 34.5%, 27.5% 76.7%, 0.1% 64.9%, 17.9% 100%, 27.6% 76.8%, 76.1% 97.7%, 74.1% 44.1%)" />
      </div> -->
      <div class="mx-auto py-8 sm:py-8 lg:py-8">
        <div class="bg-white">
          <div class="mx-auto max-w-2xl px-4 py-4 sm:px-6 lg:max-w-7xl lg:px-8">
            <h2 class="text-3xl font-bold tracking-tight text-gray-900 sm:text-4xl">Books previously rated</h2>
            <div v-if="loading" class="mt-8">
              <div class="flex justify-center">
                Loading...
              </div>
            </div>
            <div v-else>
              <BookHistory :books="data.pastRatings"/>
            </div>
          </div>
          <div class="mx-auto max-w-2xl px-4 py-4 sm:px-6 lg:max-w-7xl lg:px-8">
            <BookDetailsModal v-if="data" ref="modal" :book="selectedBook" :recommendationId="data.recommendationId"/>
            <h2 class="text-3xl font-bold tracking-tight text-gray-900 sm:text-4xl">Books Recommended for you</h2>
            <p class="mt-4 text-gray-500">These books are specially curated and recommended. Click on any of these books to view more about them.</p>
            <div v-if="loading" class="mt-8">
              <div class="flex justify-center">
                Loading...
              </div>
            </div>
            <div v-else-if="error" class="mt-8">
              <div class="bg-red-100 text-center border-t-4 border-red-500 rounded-b-lg text-red-900 px-4 py-3 shadow-md" role="danger">
                <div class="flex justify-center my-2">
                    <svg class="fill-current h-20 w-20 text-red-500" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 27.963 27.963">	
                        <path d="M13.983,0C6.261,0,0.001,6.259,0.001,13.979c0,7.724,6.26,13.984,13.982,13.984s13.98-6.261,13.98-13.984
                            C27.963,6.259,21.705,0,13.983,0z M13.983,26.531c-6.933,0-12.55-5.62-12.55-12.553c0-6.93,5.617-12.548,12.55-12.548
                            c6.931,0,12.549,5.618,12.549,12.548C26.531,20.911,20.913,26.531,13.983,26.531z"/>
                        <polygon points="15.579,17.158 16.191,4.579 11.804,4.579 12.414,17.158 		"/>
                        <path d="M13.998,18.546c-1.471,0-2.5,1.029-2.5,2.526c0,1.443,0.999,2.528,2.444,2.528h0.056c1.499,0,2.469-1.085,2.469-2.528 C16.441,19.575,15.468,18.546,13.998,18.546z"/>
                    </svg>
                </div>
                <div>
                    <p class="font-bold text-xl">Unexpected Error Occurred</p>
                    <p class="mt-2 text-md">An unexpected error has occurred. Please try again.</p>
                    <p class="mt-2 text-sm">Error Details: {{ error }}</p>
                    <button class="mt-4 bg-white hover:bg-orange-500 text-orange-700 font-semibold hover:text-white py-2 px-4 border border-orange-500 hover:border-transparent rounded" @click="loadRecommendations()" >
                    Try again
                    </button>
                </div>
              </div>
            </div>
            <div v-else class="grid grid-cols-1 gap-x-24 gap-y-12 mt-10 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 xl:gap-x-16">
              <button v-for="item in data.items" :key="item.itemId" v-on:click="" @click="openModal(item)" class="group">
                <div class="aspect-h-1 aspect-w-1 w-full overflow-hidden rounded-lg bg-gray-200 xl:aspect-h-8 xl:aspect-w-7">
                  <img :src="item.attributes.image" :alt="item.attributes.title" class="h-full w-full object-cover object-center group-hover:opacity-75" />
                </div>
                <h3 class="mt-4 text-sm text-gray-700">{{ item.attributes.authors }}</h3>
                <p class="mt-1 text-lg font-medium text-gray-900">{{ item.attributes.original_title }}</p>
              </button>
              
            </div>
          </div>
        </div>
      </div>
      <!-- <div class="absolute inset-x-0 top-[calc(100%-13rem)] -z-10 transform-gpu overflow-hidden blur-3xl sm:top-[calc(100%-30rem)]" aria-hidden="true">
        <div class="relative left-[calc(50%+3rem)] aspect-[1155/678] w-[36.125rem] -translate-x-1/2 bg-gradient-to-tr from-[#ff80b5] to-[#9089fc] opacity-30 sm:left-[calc(50%+36rem)] sm:w-[72.1875rem]" style="clip-path: polygon(74.1% 44.1%, 100% 61.6%, 97.5% 26.9%, 85.5% 0.1%, 80.7% 2%, 72.5% 32.5%, 60.2% 62.4%, 52.4% 68.1%, 47.5% 58.3%, 45.2% 34.5%, 27.5% 76.7%, 0.1% 64.9%, 17.9% 100%, 27.6% 76.8%, 76.1% 97.7%, 74.1% 44.1%)" />
      </div> -->
</template>

<script>
import csv from '../assets/popular_book_details.csv';
import { ref } from 'vue';
export default {
    name: 'Books',
    data() {
        return {
          loading: true,
          error: false,
          modalLoading: false,
          modalError: false,
          userid: '',
          data: null,
          selectedBook: {
            "book_id" : "1",
            "goodreads_book_id" : "2767052",
            "best_book_id":"2767052",
            "work_id":"2792775",
            "books_count":"272",
            "isbn":"439023483",
            "isbn13":"9.78043902348e+12",
            "authors":"Suzanne Collins",
            "original_publication_year":"2008.0",
            "original_title":"The Hunger Games",
            "title":"The Hunger Games (The Hunger Games, #1)",
            "language_code":"eng",
            "average_rating":"4.34",
            "ratings_count":"4780653",
            "work_ratings_count":"4942365",
            "work_text_reviews_count":"155254",
            "ratings_1":"66715",
            "ratings_2":"127936",
            "ratings_3":"560092",
            "ratings_4":"1481305",
            "ratings_5":"2706317",
            "image_url":"https://images.gr-assets.com/books/1447303603m/2767052.jpg",
            "small_image_url":"https://images.gr-assets.com/books/1447303603s/2767052.jpg"
          }
        }
    },
    mounted() {
      if (localStorage.userid) {
        this.userid = localStorage.userid;
        this.loadRecommendations();
      } else {
        this.$router.push('/login'); // redirect to welcome page
      }
    },
    methods: {
      loadRecommendations(){
        this.loading = true;
        this.error = false;
        getRecommendations(this.userid).then((response) => {
          this.data = response.data;
          this.loading = false;
        }).catch((error) => {
          this.error = error;
          this.loading = false;
        });
      },
      openModal(itemSelected) {
        // do API call to track book click (feedback)
        this.modalLoading = true;
        postFeedback(this.data.recommendationId, itemSelected.itemId, 1, "click").then((response) => {
          this.modalLoading = false;
          this.selectedBook = itemSelected;
          this.$refs.modal.open();
        }).catch((error) => {
          alert("Unexpected error occurred. Please try again.");
          this.modalError = error;
          this.modalLoading = false;
        });
      }
    }
};
</script>
