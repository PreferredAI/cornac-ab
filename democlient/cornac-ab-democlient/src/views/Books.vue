<script setup>
import BookDetailsModal from '../components/BookDetailsModal.vue';
const modal = ref(null);

const openModal = () => {
  modal.value.open();
};

</script>

<template>
      <!-- <div class="absolute inset-x-0 -top-40 -z-10 transform-gpu overflow-hidden blur-3xl sm:-top-80" aria-hidden="true">
        <div class="relative left-[calc(50%-11rem)] aspect-[1155/678] w-[36.125rem] -translate-x-1/2 rotate-[30deg] bg-gradient-to-tr from-[#ff80b5] to-[#9089fc] opacity-30 sm:left-[calc(50%-30rem)] sm:w-[72.1875rem]" style="clip-path: polygon(74.1% 44.1%, 100% 61.6%, 97.5% 26.9%, 85.5% 0.1%, 80.7% 2%, 72.5% 32.5%, 60.2% 62.4%, 52.4% 68.1%, 47.5% 58.3%, 45.2% 34.5%, 27.5% 76.7%, 0.1% 64.9%, 17.9% 100%, 27.6% 76.8%, 76.1% 97.7%, 74.1% 44.1%)" />
      </div> -->
      <div class="mx-auto py-8 sm:py-8 lg:py-8">
        <div class="bg-white">
          <div class="mx-auto max-w-2xl px-4 py-4 sm:px-6 lg:max-w-7xl lg:px-8">
            <BookDetailsModal ref="modal" :book.title="test" />
            <h2 class="text-3xl font-bold tracking-tight text-gray-900 sm:text-4xl">Books Recommended for you</h2>
            <p class="mt-4 text-gray-500">These books are specially curated and recommended. Click on any of these books to view and rate them.</p>

            <div class="grid grid-cols-1 gap-x-24 gap-y-12 mt-10 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 xl:gap-x-16">
              <button v-for="book in books" :key="book.book_id" @click="openModal" class="group">
                <div class="aspect-h-1 aspect-w-1 w-full overflow-hidden rounded-lg bg-gray-200 xl:aspect-h-8 xl:aspect-w-7">
                  <img :src="book.image_url" :alt="book.small_image_url" class="h-full w-full object-cover object-center group-hover:opacity-75" />
                </div>
                <h3 class="mt-4 text-sm text-gray-700">{{ book.authors }}</h3>
                <p class="mt-1 text-lg font-medium text-gray-900">{{ book.original_title }}</p>
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
            userid: '',
            books: [],
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
        this.books = csv;
      } else {
        this.$router.push('/login'); // redirect to welcome page
      }
    },
};
</script>
