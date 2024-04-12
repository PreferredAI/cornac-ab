<script setup>
import { ref } from 'vue';
import { XMarkIcon } from '@heroicons/vue/24/outline'
import { postFeedback } from '../services';
const isContentShown = ref(false);
const open = () => {
  isContentShown.value = true;
};
const close = () => {
  isContentShown.value = false;
};

defineExpose({ open });
</script>

<template>
  <TransitionRoot as="template" :show="isContentShown">
    <Dialog as="div" class="relative z-10" @close="close">
      <TransitionChild as="template" enter="ease-out duration-300" enter-from="opacity-0" enter-to="opacity-100" leave="ease-in duration-200" leave-from="opacity-100" leave-to="opacity-0">
        <div class="fixed inset-0 hidden bg-gray-500 bg-opacity-75 transition-opacity md:block" />
      </TransitionChild>

      <div class="fixed inset-0 z-10 w-screen overflow-y-auto">
        <div class="flex min-h-full items-stretch justify-center text-center md:items-center md:px-2 lg:px-4">
          <TransitionChild as="template" enter="ease-out duration-300" enter-from="opacity-0 translate-y-4 md:translate-y-0 md:scale-95" enter-to="opacity-100 translate-y-0 md:scale-100" leave="ease-in duration-200" leave-from="opacity-100 translate-y-0 md:scale-100" leave-to="opacity-0 translate-y-4 md:translate-y-0 md:scale-95">
            <DialogPanel class="flex w-full transform text-left text-base transition md:my-8 md:max-w-2xl md:px-4 lg:max-w-4xl">
              <div class="relative flex w-full items-center overflow-hidden bg-white px-4 pb-8 pt-14 shadow-2xl sm:px-6 sm:pt-8 md:p-6 lg:p-8">
                <button type="button" class="absolute right-4 top-4 text-gray-400 hover:text-gray-500 sm:right-6 sm:top-8 md:right-6 md:top-6 lg:right-8 lg:top-8" @click="close">
                  <span class="sr-only">Close</span>
                  <XMarkIcon class="h-6 w-6" aria-hidden="true" />
                </button>

                <div class="grid w-full grid-cols-1 items-start gap-x-6 gap-y-8 sm:grid-cols-12 lg:gap-x-8">
                  <div class="h-full overflow-hidden rounded-lg sm:col-span-4 lg:col-span-5">
                    <img :src="book.attributes.image" class="h-full w-full" />
                  </div>
                  <div class="sm:col-span-8 lg:col-span-7">
                    <h2 class="text-2xl font-bold text-gray-900 sm:pr-12">{{ book.attributes.original_title }}</h2>

                    <section aria-labelledby="information-heading" class="mt-2">
                      <h3 id="information-heading" class="sr-only">Product information</h3>

                      <p class="text-2xl text-gray-900">{{ book.attributes.title }}</p>
                    </section>

                    <section aria-labelledby="options-heading">
                      <h3 id="options-heading" class="sr-only">Product options</h3>

                        <div class="mt-2">
                          <h2 class="text-lg font-medium text-gray-900">Details</h2>

                          <div class="mt-2 space-y-2">
                            <p class="text-md text-gray-600">Authors</p>{{ book.attributes.authors }}
                            <p class="text-md text-gray-600">Year Released</p>{{ book.attributes.year }}
                            <p class="text-md text-gray-600">Average Rating</p>{{ book.attributes.rating }} stars
                            <p class="text-md text-gray-600">ISBN</p>{{ book.attributes.isbn }}
                            
                            <p v-if="this.book.selectedRating" class="text-md text-gray-600"> Rated </p>
                            <p v-else class="text-md text-gray-600"> Rate this book </p>
                            <div v-if="this.book.selectedRating" class="flex">
                              <svg v-for="n in this.book.selectedRating" class="text-yellow-400" width="23" height="23" xmlns="http://www.w3.org/2000/svg" fill="currentColor" viewBox="0 0 24 24" stroke="currentColor">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M11.049 2.927c.3-.921 1.603-.921 1.902 0l1.519 4.674a1 1 0 00.95.69h4.915c.969 0 1.371 1.24.588 1.81l-3.976 2.888a1 1 0 00-.363 1.118l1.518 4.674c.3.922-.755 1.688-1.538 1.118l-3.976-2.888a1 1 0 00-1.176 0l-3.976 2.888c-.783.57-1.838-.197-1.538-1.118l1.518-4.674a1 1 0 00-.363-1.118l-3.976-2.888c-.784-.57-.38-1.81.588-1.81h4.914a1 1 0 00.951-.69l1.519-4.674z"></path>
                              </svg>
                              <svg v-for="n in (5 - this.book.selectedRating)" class="text-gray-600" width="23" height="23" xmlns="http://www.w3.org/2000/svg" fill="currentColor" viewBox="0 0 24 24" stroke="currentColor">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M11.049 2.927c.3-.921 1.603-.921 1.902 0l1.519 4.674a1 1 0 00.95.69h4.915c.969 0 1.371 1.24.588 1.81l-3.976 2.888a1 1 0 00-.363 1.118l1.518 4.674c.3.922-.755 1.688-1.538 1.118l-3.976-2.888a1 1 0 00-1.176 0l-3.976 2.888c-.783.57-1.838-.197-1.538-1.118l1.518-4.674a1 1 0 00-.363-1.118l-3.976-2.888c-.784-.57-.38-1.81.588-1.81h4.914a1 1 0 00.951-.69l1.519-4.674z"></path>
                              </svg>
                            </div>
                            <div v-else class="flex flex-row-reverse justify-end">
                              <button v-for="n in 5" @click.stop.prevent="rateBook(6-n)" class="text-gray-600 cursor-pointer peer peer-hover:text-yellow-400 hover:text-yellow-400 duration-100">
                                <svg width="23" height="23" xmlns="http://www.w3.org/2000/svg" fill="currentColor" viewBox="0 0 24 24" stroke="currentColor">
                                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M11.049 2.927c.3-.921 1.603-.921 1.902 0l1.519 4.674a1 1 0 00.95.69h4.915c.969 0 1.371 1.24.588 1.81l-3.976 2.888a1 1 0 00-.363 1.118l1.518 4.674c.3.922-.755 1.688-1.538 1.118l-3.976-2.888a1 1 0 00-1.176 0l-3.976 2.888c-.783.57-1.838-.197-1.538-1.118l1.518-4.674a1 1 0 00-.363-1.118l-3.976-2.888c-.784-.57-.38-1.81.588-1.81h4.914a1 1 0 00.951-.69l1.519-4.674z"></path>
                                </svg>
                              </button>
                            </div>
                          </div>
                        </div>
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
  TransitionChild,
  TransitionRoot,
} from '@headlessui/vue'

export default {
    name: 'BookDetailsModal',
    props: ['book', 'recommendationId'],
    data() {
        return {
          loading: false,
          // selectedRating: null,
        };
    },
    methods: {
        rateBook(rating) {
            // send rating to backend
            this.loading = true;

            postFeedback(this.recommendationId, this.book.itemId, rating, "rating").then((response) => {
              this.loading = false;
              this.book.selectedRating = rating;
              alert("Rating submitted successfully.");
            }).catch((error) => {
              alert("Unexpected error occurred. Please try again.");
            });
        }
    }
};
</script>
