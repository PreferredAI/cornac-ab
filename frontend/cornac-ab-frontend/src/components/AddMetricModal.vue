<script setup>
import { ref } from 'vue';
import { XMarkIcon } from '@heroicons/vue/24/outline'
const isContentShown = ref(false);
const open = () => (isContentShown.value = true);
const modalClose = () => (isContentShown.value = false);

defineExpose({ open, modalClose });
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

                <div class="grid grid-cols-12 w-full gap-4">
                  <h2 class="col-span-12 text-2xl font-bold text-gray-900">Add New Metric</h2>

                  <p class="col-span-12 text-xl mt-8 text-gray-900">Rating Metrics</p>                  

                  <button v-for="(metric, index) in metricsAvailable.rating" :key="index" @click="selectMetric(metric, 'rating')" :class="metric == metricSelected.metric ? 'bg-indigo-100 ring-2 ring-indigo-500':''" class="col-span-3 mt-2 mx-2 place-items-center rounded-lg border border-indigo-900/25 hover:bg-gray-50 py-1">
                    <p class="text-l font-semibold text-center text-black">{{ metric }}</p>
                  </button>

                  <p class="col-span-12 text-xl mt-8 text-gray-900">Ranking Metrics</p>
                  <div v-for="(metric, index) in metricsAvailable.ranking" :key="index" class="mx-2 col-span-3">
                    <button @click="selectMetric(metric, 'ranking')" :class="metric == metricSelected.metric ? 'bg-indigo-100 ring-2 ring-indigo-500':''" class="w-full mt-2 grid place-items-center rounded-lg border border-indigo-900/25 hover:bg-gray-50 py-1">
                      <p class="text-l font-semibold text-center text-black">{{ metric }}</p>
                    </button>
                    <div :class="metric == metricSelected.metric ? 'ring-2 ring-indigo-500':'invisible'" class="relative mt-2 rounded-md shadow-sm">
                      <div class="pointer-events-none absolute inset-y-0 left-0 flex items-center pl-3">
                        <span class="text-gray-500 sm:text-sm">K=</span>
                      </div>
                      <input type="number" v-model="metricSelected.k" class="block w-full rounded-md border-0 py-1.5 pl-8 pr-2 text-gray-900 ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:text-sm sm:leading-6" placeholder="10" />
                    </div>
                  </div>


                  
                  <div class="mt-6 flex items-center justify-start gap-x-6 col-span-12">
                    <button v-on:click="addMetric(modalClose)" type="button" class="rounded-md bg-indigo-600 px-3 py-2 text-sm font-semibold text-white shadow-sm hover:bg-indigo-500 focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-indigo-600">Add Selected Metric</button>
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
    name: 'AddMetricModal',
    emits: ['addMetric'],
    props: ['metricsAlreadyAdded'],
    data() {
      return {
          metricSelected: {
            "metric": "",
            "type": "",
            "k": ""
          },
          metricsAvailable: {
            "rating": [
              "MAE",
              "RMSE",
              "MSE",
            ],
            "ranking": [
              "NDCG",
              "NCRR",
              "MRR",
              "HitRatio",
              "Precision",
              "Recall",
              "FMeasure",
              "AUC",
              "MAP"
            ],
          }
      }
    },
    methods: {
      selectMetric(metric, type) {
        this.metricSelected = {
          "metric": metric,
          "type": type
        };

        if (type == "ranking") {
          this.metricSelected.k = "";
        }
      },
      addMetric(modalClose) {
        if (this.metricSelected.metric == "") {
          alert("Please select a metric.");
          return;
        } else if (this.metricSelected.type == "ranking" && this.metricSelected.k == "") {
          alert("Please input a value for k.");
          return;
        } else if (this.metricSelected.type == "ranking" && parseInt(this.metricSelected.k) <= 0) {
          alert("Please input a value for k greater than 0.");
          return;
        } else if (this.metricsAlreadyAdded.includes(this.metricSelected)) {
          alert("This metric has already been added.");
          return;
        }

        this.$emit('addMetric', this.metricSelected);

        modalClose();

        this.metricSelected = {
          "metric": "",
          "type": "",
          "k": ""
        };
      },
    }
};
</script>
