<script setup>
import { ref } from 'vue';
import { XMarkIcon } from '@heroicons/vue/24/outline'
const isContentShown = ref(false);
const open = () => (isContentShown.value = true);

const book = {
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
  "image_url":"https://images.gr-assets.com/books/1447303603l/2767052.jpg",
  "small_image_url":"https://images.gr-assets.com/books/1447303603s/2767052.jpg"
};

defineExpose({ open });
</script>

<template>
  <TransitionRoot as="template" :show="isContentShown">
    <Dialog as="div" class="relative z-10" @close="isContentShown = false">
      <TransitionChild as="template" enter="ease-out duration-300" enter-from="opacity-0" enter-to="opacity-100" leave="ease-in duration-200" leave-from="opacity-100" leave-to="opacity-0">
        <div class="fixed inset-0 hidden bg-gray-500 bg-opacity-75 transition-opacity md:block" />
      </TransitionChild>

      <div class="fixed inset-0 z-10 w-screen overflow-y-auto">
        <div class="flex min-h-full items-stretch justify-center text-center md:items-center md:px-2 lg:px-4">
          <TransitionChild as="template" enter="ease-out duration-300" enter-from="opacity-0 translate-y-4 md:translate-y-0 md:scale-95" enter-to="opacity-100 translate-y-0 md:scale-100" leave="ease-in duration-200" leave-from="opacity-100 translate-y-0 md:scale-100" leave-to="opacity-0 translate-y-4 md:translate-y-0 md:scale-95">
            <DialogPanel class="flex w-full transform text-left text-base transition md:my-8 md:max-w-2xl md:px-4 lg:max-w-4xl">
              <div class="relative flex w-full items-center overflow-hidden bg-white px-4 pb-8 pt-14 shadow-2xl sm:px-6 sm:pt-8 md:p-6 lg:p-8">
                <button type="button" class="absolute right-4 top-4 text-gray-400 hover:text-gray-500 sm:right-6 sm:top-8 md:right-6 md:top-6 lg:right-8 lg:top-8" @click="isContentShown = false">
                  <span class="sr-only">Close</span>
                  <XMarkIcon class="h-6 w-6" aria-hidden="true" />
                </button>

                <div class="grid w-full grid-cols-1 items-start gap-x-6 gap-y-8 sm:grid-cols-12 lg:gap-x-8">
                  <div class="aspect-h-3 aspect-w-2 overflow-hidden rounded-lg bg-gray-100 sm:col-span-4 lg:col-span-5">
                    <img :src="book.image_url" :alt="Test" class="object-cover object-center" />
                  </div>
                  <div class="sm:col-span-8 lg:col-span-7">
                    <h2 class="text-2xl font-bold text-gray-900 sm:pr-12">{{ book.original_title }}</h2>

                    <section aria-labelledby="information-heading" class="mt-2">
                      <h3 id="information-heading" class="sr-only">Product information</h3>

                      <p class="text-2xl text-gray-900">{{ book.title }}</p>

                      <!-- Reviews -->
                      <div class="mt-6">
                        <h4 class="sr-only">Reviews</h4>
                        <div class="flex items-center">
                          <div class="flex items-center">
                            <!-- <StarIcon v-for="rating in [0, 1, 2, 3, 4]" :key="rating" :class="[product.rating > rating ? 'text-gray-900' : 'text-gray-200', 'h-5 w-5 flex-shrink-0']" aria-hidden="true" /> -->
                          </div>
                          <!-- <p class="sr-only">{{ product.rating }} out of 5 stars</p> -->
                          <!-- <a href="#" class="ml-3 text-sm font-medium text-indigo-600 hover:text-indigo-500">{{ product.reviewCount }} reviews</a> -->
                        </div>
                      </div>
                    </section>

                    <section aria-labelledby="options-heading">
                      <h3 id="options-heading" class="sr-only">Product options</h3>

                      <form>
                        <div class="mt-2">
                          <h2 class="text-lg font-medium text-gray-900">Details</h2>

                          <div class="mt-2 space-y-6">
                            <p class="text-md text-gray-600">Authors</p>{{ book.authors }}
                            <p class="text-md text-gray-600">Year Released</p>{{ book.original_publication_year }}
                            <p class="text-md text-gray-600">Average Rating</p>{{ book.average_rating }} stars
                            <p class="text-md text-gray-600">ISBN</p>{{ book.isbn }}

                          </div>
                        </div>

                        <!-- <button type="submit" class="mt-6 flex w-full items-center justify-center rounded-md border border-transparent bg-indigo-600 px-8 py-3 text-base font-medium text-white hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:ring-offset-2">Rate Book</button> -->
                      </form>
                    </section>
                  </div>
                </div>
              </div>
            </DialogPanel>
          </TransitionChild>
        </div>
      </div>
    </Dialog>
  </TransitionRoot>
</template>

<script>
import {
  Dialog,
  DialogPanel,
  RadioGroup,
  RadioGroupLabel,
  RadioGroupOption,
  TransitionChild,
  TransitionRoot,
} from '@headlessui/vue'

export default {
    name: 'BookDetailsModal',
};
</script>
