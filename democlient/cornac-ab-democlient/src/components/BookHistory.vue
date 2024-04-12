<script setup>
  import { ref } from 'vue';
  import { register } from 'swiper/element/bundle';

  register();
</script>

<template>
  <div class="my-4">
    <swiper-container
      :slides-per-view="5"
      :space-between="10"
      :breakpoints="{
        640: {
          slidesPerView: 2,
          spaceBetween: 40,
        },
        768: {
          slidesPerView: 4,
          spaceBetween: 40,
        },
        1024: {
          slidesPerView: 5,
          spaceBetween: 20,
        },
      }"
      :navigation="true"
      @swiperprogress="onProgress"
      @swiperslidechange="onSlideChange"
    >
      <swiper-slide v-for="book in books">
        <div class="aspect-h-2 aspect-w-1 w-36 mx-2 overflow-hidden rounded-lg bg-gray-200 xl:aspect-h-8 xl:aspect-w-7">
          <img :src="book.attributes.image" :alt="book.attributes.title" class="w-full object-cover object-center group-hover:opacity-75" />
        </div>
        <h3 class="mt-4 text-sm text-gray-700">{{ book.attributes.authors }}</h3>
        <p class="mt-1 truncate text-lg font-medium text-gray-900">{{ book.attributes.original_title }}</p>
        <div class="flex">
          <svg v-for="_ in book.attributes.userRating" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20" fill="yellow" class="w-5 h-5" stroke="currentColor">
            <path fill-rule="evenodd" d="M10.868 2.884c-.321-.772-1.415-.772-1.736 0l-1.83 4.401-4.753.381c-.833.067-1.171 1.107-.536 1.651l3.62 3.102-1.106 4.637c-.194.813.691 1.456 1.405 1.02L10 15.591l4.069 2.485c.713.436 1.598-.207 1.404-1.02l-1.106-4.637 3.62-3.102c.635-.544.297-1.584-.536-1.65l-4.752-.382-1.831-4.401Z" clip-rule="evenodd" />
          </svg>
        </div>

      </swiper-slide>
    </swiper-container>
  </div>
</template>

<script>

export default {
  name: 'BookHistory',
  props: ['books'],
}
</script>