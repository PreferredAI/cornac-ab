<script setup>
import { ref } from 'vue';
import { XMarkIcon } from '@heroicons/vue/24/outline'
const isContentShown = ref(true);
const open = () => (isContentShown.value = true);

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

                <div class="grid w-full grid-cols-6">
                  <h2 class="text-2xl col-span-12 font-bold text-gray-900 sm:pr-12">Add New Metric</h2>

                  <p class="text-xl col-span-12 mt-2 text-gray-900">Rating Metrics</p>
                  <div class="transition mt-2 mx-2 grid place-items-center rounded-lg border ring-2 ring-indigo-500 border-indigo-900/25 py-1">
                      <p class="text-xl font-semibold text-center text-black">MAE</p>
                  </div>
                  <div class="transition mt-2 mx-2 grid place-items-center rounded-lg border ring-2 ring-indigo-500 border-indigo-900/25 py-1">
                      <p class="text-xl font-semibold text-center text-black">RMSE</p>
                  </div>


                  <p class="text-xl col-span-12 mt-2 text-gray-900">Ranking Metrics</p>


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
    name: 'AddMetricModal',
    data () {
        return {
            ratingMetrics: [
              
            ],
        }
    },
};
</script>
