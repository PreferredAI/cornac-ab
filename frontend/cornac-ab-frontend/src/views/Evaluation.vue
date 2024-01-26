<script setup>
import { XMarkIcon } from '@heroicons/vue/24/outline';
import AddMetricModal from '../components/AddMetricModal.vue';

import { ref } from 'vue';


const modal = ref(null);

const openModal = () => {
  modal.value.open();
};
</script>

<template>
    <header class="bg-white shadow">
        <div class="mx-auto max-w-7xl px-4 py-6 sm:px-6 lg:px-8">
        <h1 class="text-3xl font-bold tracking-tight text-gray-900">Evaluation</h1>
        </div>
    </header>
    <div v-if="!isEvaluationDone">
        <div>
            <h2 class="mt-8 text-2xl tracking-tight font-semibold text-gray-900">Filters Selected</h2>
            Date Range, Models selected
        </div>
        <!-- <div>
            <h2 class="mt-8 text-2xl tracking-tight font-semibold text-gray-900">Select Evaluation Period</h2>

            <div class="grid grid-cols-4 gap-4 mt-4 sm:grid-cols-8 lg:grid-cols-4">
                <div v-for="dayNum in possibleDays">
                    <label v-if="dayNum == selectedDays" class="group relative flex items-center justify-center rounded-md border ring-2 ring-indigo-500 py-3 px-4 text-sm font-medium uppercase hover:bg-gray-50 focus:outline-none sm:flex-1 sm:py-6 cursor-pointer bg-white text-gray-900 shadow-sm">
                        <input type="radio" name="size-choice" v-model="selectedDays" :value="dayNum" class="sr-only" aria-labelledby="size-choice-1-label">
                        <span id="size-choice-1-label">Past {{ dayNum }} Days</span>
                        <span class="pointer-events-none absolute -inset-px rounded-md" aria-hidden="true"></span>
                    </label>
                    <label v-else class="group relative flex items-center justify-center rounded-md border py-3 px-4 text-sm font-medium uppercase hover:bg-gray-50 focus:outline-none sm:flex-1 sm:py-6 cursor-pointer bg-white text-gray-900 shadow-sm">
                        <input type="radio" name="size-choice" v-model="selectedDays" :value="dayNum" class="sr-only" aria-labelledby="size-choice-1-label">
                        <span id="size-choice-1-label">Past {{ dayNum }} Days</span>
                        <span class="pointer-events-none absolute -inset-px rounded-md" aria-hidden="true"></span>
                    </label>
                </div>
            </div>
        </div> -->
        <div>
            <div class="mt-8 mb-4 col-span-12">
                <h2 class="mt-8 text-2xl tracking-tight font-semibold text-gray-900">Feedback data from past {{ selectedDays }} days</h2>
            </div>
            <div v-if="feedbackData.length" class="mt-8 max-h-96 overflow-auto overscroll-contain">
                <p class="text-l tracking-tight text-gray-900">
                    These {{ feedbackData.length }} feedback records provided by users will be added to the evaluation.
                </p>

                <table class="w-full text-sm text-left rtl:text-right text-gray-500 dark:text-gray-400">
                    <thead class="text-xs text-gray-700 uppercase bg-gray-50 dark:bg-gray-700 dark:text-gray-400">
                        <tr>
                        <th scope="col" class="px-6 py-3">User ID</th>
                        <th scope="col" class="px-6 py-3">Item ID</th>
                        <th scope="col" class="px-6 py-3">Rating</th>
                        <th scope="col" class="px-6 py-3">Timestamp</th>
                        <th scope="col" class="px-6 py-3">Model Allocated</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr v-for="(feedback, index) in feedbackData" :key="index" class="bg-white border-b dark:bg-gray-800 dark:border-gray-700">
                            <td class="px-6 py-4">{{ feedback.userId }}</td>
                            <td class="px-6 py-4">{{ feedback.itemId }}</td>
                            <td class="px-6 py-4">{{ feedback.rating }}</td>
                            <td class="px-6 py-4">{{ feedback.timestamp }}</td>
                            <td class="px-6 py-4">{{ feedback.modelAllocated }}</td>
                        </tr>
                    </tbody>
                </table>
            </div>
            <div v-else class="mt-8">
                <p class="text-xl tracking-tight font-semibold text-gray-900">No feedback data received yet.</p>
                <p class="text-l tracking-tight text-gray-900">Please run evaluation again when more feedback data is provided by users.</p>
            </div>
        </div>
        
        <div class="lg:grid lg:gap-4">
            <h2 class="mt-12 text-2xl tracking-tight font-semibold text-gray-900 col-span-12">Select metrics to compute</h2>
            <p class="text-l tracking-tight text-gray-900">
                These {{ addedMetrics.length }} metrics will be added to the evaluation.
            </p>

            <div class="lg:grid lg:col-span-12 lg:grid-cols-4 lg:gap-x-6">
                <AddMetricModal ref="modal" :metrics-already-added="addedMetrics" @add-metric="addMetric" />
                <div v-for="(addedMetric, index) in addedMetrics" :key="index" class="my-2 grid grid-cols-1">
                    <div class="transition relative mt-2 grid place-items-center rounded-lg border bg-indigo-500 ring-2 ring-indigo-500 border-grey-300/25 py-1">
                        <!-- <p class="mb-2 text-sm uppercase font-semibold text-center text-black">{{ addedMetric.type }} metric</p> -->
                        <button type="button" class="absolute right-1 text-white hover:text-gray-500" @click="removeMetric(index)">
                            <span class="sr-only">Remove</span>
                            <XMarkIcon class="h-6 w-6" aria-hidden="true" />
                        </button>
                        <p class="text-xl font-semibold text-center text-white">{{ addedMetric.metric }}</p>
                        <p v-if="addedMetric.k" class="mt-1 text-sm uppercase font-semibold text-center text-white">k={{ addedMetric.k }}</p>
                    </div>
                </div>
                <div class="my-2 grid grid-cols-1">
                    <button @click="openModal" class="mt-2 justify-center bg-indigo-50 hover:bg-indigo-100 rounded-lg border-2 border-dashed fill border-indigo-500/50 px-6 py-2">
                        <div class="text-center">
                            <svg class="mx-auto h-7 w-7 text-indigo-600" viewBox="0 0 24 24" fill="currentColor" aria-hidden="true">
                                <path d="M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm5 11h-4v4h-2v-4H7v-2h4V7h2v4h4v2z"/>
                            </svg>
                            <p class="text-sm leading-5 font-semibold text-indigo-600">Add Metric</p>
                        </div>
                    </button>
                </div>
            </div>
            <div class="mt-6 flex items-center justify-end gap-x-6 col-span-12">
                <button v-on:click="evaluateData" type="button" class="rounded-md bg-indigo-600 px-3 py-2 text-sm font-semibold text-white shadow-sm hover:bg-indigo-500 focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-indigo-600">Continue</button>
            </div>
        </div>
    </div>
    <div v-else>
        <div class="lg:grid lg:gap-4">
            <div class="mt-8 mb-4 col-span-12">
                <h2 class="text-2xl font-bold tracking-tight text-gray-900">Evaluation Results</h2>
            </div>
            <div class="col-span-12">
                <table class="w-full text-sm text-left rtl:text-right text-gray-500 dark:text-gray-400">
                    <thead class="text-s text-gray-700 uppercase bg-gray-50 dark:bg-gray-700 dark:text-gray-400">
                        <tr>
                            <th></th>
                            <th v-for="(model, index) in evaluationResults.models" :key="index" scope="col" class="px-6 py-3">{{ model }}</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr v-for="(metric, index) in evaluationResults.metrics" :key="index" class="bg-white border-b dark:bg-gray-800 dark:border-gray-700">
                            <td class="text-s font-bold text-gray-700 px-6 py-4 bg-gray-50 dark:bg-gray-700 dark:text-gray-400">{{ metric.metric }}</td>
                            <td v-for="(value, valIndex) in metric.values" :key="valIndex" class="px-6 py-4">{{ value }} </td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </div>
        <div class="mt-6 flex items-center justify-start gap-x-6 col-span-12">
            <!-- <button type="button" class="text-sm font-semibold leading-6 text-gray-900">Cancel</button> -->
            <button v-on:click="evaluateData" type="button" class="rounded-md bg-indigo-600 px-3 py-2 text-sm font-semibold text-white shadow-sm hover:bg-indigo-500 focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-indigo-600">Download Evaluation to CSV</button>
        </div>
    </div>
</template>

<script>
export default {
    data() {
        return {
            selectedDays: 7,
            possibleDays: [7, 14, 30, 90],
            addedMetrics: [
                {
                    metric: "MAE",
                    type: "rating"
                },
                {
                    metric: "RMSE",
                    type: "rating"
                },
                {
                    metric: "MSE",
                    type: "rating"
                },
                {
                    metric: "NDCG",
                    type: "ranking",
                    k: 10
                },
                {
                    metric: "NCRR",
                    type: "ranking",
                    k: 10
                },
                {
                    metric: "MRR",
                    type: "ranking", //no k
                    k: 10
                },
                {
                    metric: "HitRatio",
                    type: "ranking",
                    k: 10
                },
                {
                    metric: "Precision",
                    type: "ranking",
                    k: 10
                },
                {
                    metric: "Recall",
                    type: "ranking",
                    k: 10
                },
                {
                    metric: "FMeasure",
                    type: "ranking",
                    k: 10
                },
                {
                    metric: "AUC",
                    type: "ranking", //no k
                    k: 10
                },
                {
                    metric: "MAP",
                    type: "ranking", //no k
                    k: 10
                }
            ],
            isEvaluationDone: false,
            feedbackData:[
                {
                    userId: 9,
                    itemId: 8,
                    rating: 1,
                    timestamp: '2023-12-13 15:46:10',
                    modelAllocated: 'LightGCN'
                },
                {
                    userId: 15,
                    itemId: 398,
                    rating: 1,
                    timestamp: '2023-12-13 15:46:16',
                    modelAllocated: 'LightGCN'
                },
                {
                    userId: 15,
                    itemId: 275,
                    rating: 1,
                    timestamp: '2023-12-13 15:46:21',
                    modelAllocated: 'LightGCN'
                },
                {
                    userId: 37,
                    itemId: 7173,
                    rating: 1,
                    timestamp: '2023-12-13 15:46:26',
                    modelAllocated: 'BPR'
                },
                {
                    userId: 34,
                    itemId: 380,
                    rating: 1,
                    timestamp: '2023-12-13 15:46:30',
                    modelAllocated: 'BPR'
                },
                {
                    userId: 34,
                    itemId: 483,
                    rating: 1,
                    timestamp: '2023-12-13 15:46:37',
                    modelAllocated: 'BPR'
                },
                {
                    userId: 35,
                    itemId: 8598,
                    rating: 1,
                    timestamp: '2023-12-13 15:46:37',
                    modelAllocated: 'BiVAECF'
                },
                {
                    userId: 35,
                    itemId: 3581,
                    rating: 1,
                    timestamp: '2023-12-13 15:46:38',
                    modelAllocated: 'BPR'
                }
            ],
            evaluationResults: {
                "models": ["BPR", "BiVAECF", "LightGCN"],
                "metrics": [
                    {
                        "metric": "AUC",
                        "values": [0.8477, 0.8991, 0.8982]
                    },
                    {
                        "metric": "NDCG@50",
                        "values": [0.0567, 0.0720, 0.0704]
                    },
                    {
                        "metric": "Recall@50",
                        "values": [0.0821, 0.1117, 0.1097]
                    },
                ]
            },
        };
    },
    methods: {
        evaluateData() {
            // Perform data evaluation based on selectedDays value
            console.log(`Evaluating data for the past ${this.selectedDays} days`);
            this.isEvaluationDone = true;
        },
        downloadEvaluation() {
            // Download evaluation results to CSV
            console.log('Downloading evaluation results to CSV');
            this.isEvaluationDone = false;
        },
        removeMetric(index) {
            this.addedMetrics.splice(index, 1);
        },
        addMetric(metricSelected) {
            this.addedMetrics.push(metricSelected);
        }
    }
};
</script>
