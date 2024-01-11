<template>
<div class="bg-white">
    <div class="pt-6">

      <!-- Image gallery -->
      <div class="mx-auto mt-6 max-w-2xl sm:px-6 lg:grid lg:max-w-7xl lg:grid-cols-3 lg:gap-x-8 lg:px-8">
        <div class="aspect-h-4 aspect-w-3 hidden overflow-hidden rounded-lg lg:block">
          <img :src="book.image_url" :alt="book.small_image_url" class="h-full w-full object-cover object-center" />
        </div>
      </div>

      <!-- Product info -->
      <div class="mx-auto max-w-2xl px-4 pb-16 pt-10 sm:px-6 lg:grid lg:max-w-7xl lg:grid-cols-3 lg:grid-rows-[auto,auto,1fr] lg:gap-x-8 lg:px-8 lg:pb-24 lg:pt-16">
        <div class="lg:col-span-2 lg:border-r lg:border-gray-200 lg:pr-8">
          <h1 class="text-2xl font-bold tracking-tight text-gray-900 sm:text-3xl">{{ book.original_title }}</h1>
        </div>

        <div class="py-10 lg:col-span-2 lg:col-start-1 lg:border-r lg:border-gray-200 lg:pb-16 lg:pr-8 lg:pt-6">
          <!-- Description and details -->
          <div>
            <h3 class="sr-only">Description</h3>

            <div class="space-y-6">
              <p class="text-base text-gray-900">{{ book.title }}</p>
            </div>
          </div>

          <!-- <div class="mt-10">
            <h3 class="text-sm font-medium text-gray-900">Highlights</h3>

            <div class="mt-4">
              <ul role="list" class="list-disc space-y-2 pl-4 text-sm">
                <li v-for="highlight in product.highlights" :key="highlight" class="text-gray-400">
                  <span class="text-gray-600">{{ highlight }}</span>
                </li>
              </ul>
            </div>
          </div> -->

          <div class="mt-10">
            <h2 class="text-sm font-medium text-gray-900">Details</h2>

            <div class="mt-4 space-y-6">
              <p class="text-sm text-gray-600">Authors: {{ book.authors }}</p>
              <p class="text-sm text-gray-600">Year {{ book.original_publication_year }}</p>
              <p class="text-sm text-gray-600">Average Rating: {{ book.average_rating }} stars</p>

            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { RadioGroup, RadioGroupLabel, RadioGroupOption } from '@headlessui/vue'

export default {
    name: 'BookDetails',
    data() {
      return {
        "book" : {
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
      };
    },
    computed: {
        bookid() {
            return this.$route.params.bookid
        }
    }
}
</script>
