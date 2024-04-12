<script setup>
import { XMarkIcon } from '@heroicons/vue/24/outline';
import AddMetricModal from '../components/AddMetricModal.vue';
import { ArrowRightCircleIcon, ArrowLeftCircleIcon } from '@heroicons/vue/24/solid'
import { Calendar, DatePicker } from 'v-calendar';
import 'v-calendar/style.css';

import { ref } from 'vue';
import { postEvaluationResults, getFeedbackSummary } from '../services';


const modal = ref(null);

const openModal = () => {
  modal.value.open();
};
</script>

<template>
    <div class="container mx-auto p-4">
        <header class="bg-white shadow">
            <div class="mx-auto px-4 py-6 sm:px-6 lg:px-8">
            <h1 class="text-3xl font-bold tracking-tight text-gray-900">Evaluation</h1>
            </div>
        </header>
        <div class="mt-6 flex items-center justify-start gap-x-6 col-span-12">
            <button v-on:click="returnToDashboard" type="button" class="inline-flex items-center rounded-md bg-indigo-600 px-3 py-2 text-sm font-semibold text-white shadow-sm hover:bg-indigo-500 focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-indigo-600">
                <ArrowLeftCircleIcon class="-ml-0.5 mr-1.5 h-5 w-5 text-white-400" aria-hidden="true" />
                Return to Dashboard
            </button>
        </div>
        <div v-if="isLoading" class="mt-10 justify-center rounded-lg border border-dashed border-gray-900/25 bg-blue-100 px-6 py-10">
            <h2 class="mb-2 text-lg font-semibold text-gray-900 dark:text-white">Calculating Metrics</h2>
            <p class="text-lg tracking-tight text-gray-900 dark:text-gray-400">This will take longer on evaluations with larger feedback data.</p>

            <ul class="mt-4 max-w-md space-y-2 text-gray-500 list-inside dark:text-gray-400">
                
                <li class="flex items-center">
                    <svg aria-hidden="true" class="w-4 h-4 me-2 text-gray-200 animate-spin dark:text-gray-600 fill-blue-600" viewBox="0 0 100 101" fill="none" xmlns="http://www.w3.org/2000/svg"><path d="M100 50.5908C100 78.2051 77.6142 100.591 50 100.591C22.3858 100.591 0 78.2051 0 50.5908C0 22.9766 22.3858 0.59082 50 0.59082C77.6142 0.59082 100 22.9766 100 50.5908ZM9.08144 50.5908C9.08144 73.1895 27.4013 91.5094 50 91.5094C72.5987 91.5094 90.9186 73.1895 90.9186 50.5908C90.9186 27.9921 72.5987 9.67226 50 9.67226C27.4013 9.67226 9.08144 27.9921 9.08144 50.5908Z" fill="currentColor"/><path d="M93.9676 39.0409C96.393 38.4038 97.8624 35.9116 97.0079 33.5539C95.2932 28.8227 92.871 24.3692 89.8167 20.348C85.8452 15.1192 80.8826 10.7238 75.2124 7.41289C69.5422 4.10194 63.2754 1.94025 56.7698 1.05124C51.7666 0.367541 46.6976 0.446843 41.7345 1.27873C39.2613 1.69328 37.813 4.19778 38.4501 6.62326C39.0873 9.04874 41.5694 10.4717 44.0505 10.1071C47.8511 9.54855 51.7191 9.52689 55.5402 10.0491C60.8642 10.7766 65.9928 12.5457 70.6331 15.2552C75.2735 17.9648 79.3347 21.5619 82.5849 25.841C84.9175 28.9121 86.7997 32.2913 88.1811 35.8758C89.083 38.2158 91.5421 39.6781 93.9676 39.0409Z" fill="currentFill"/></svg>
                    Retrieving Feedback data
                </li>
                <div v-for="(model, index) in models" :key="index">
                    <li class="flex items-center">
                        <svg aria-hidden="true" class="w-4 h-4 me-2 text-gray-200 animate-spin dark:text-gray-600 fill-blue-600" viewBox="0 0 100 101" fill="none" xmlns="http://www.w3.org/2000/svg"><path d="M100 50.5908C100 78.2051 77.6142 100.591 50 100.591C22.3858 100.591 0 78.2051 0 50.5908C0 22.9766 22.3858 0.59082 50 0.59082C77.6142 0.59082 100 22.9766 100 50.5908ZM9.08144 50.5908C9.08144 73.1895 27.4013 91.5094 50 91.5094C72.5987 91.5094 90.9186 73.1895 90.9186 50.5908C90.9186 27.9921 72.5987 9.67226 50 9.67226C27.4013 9.67226 9.08144 27.9921 9.08144 50.5908Z" fill="currentColor"/><path d="M93.9676 39.0409C96.393 38.4038 97.8624 35.9116 97.0079 33.5539C95.2932 28.8227 92.871 24.3692 89.8167 20.348C85.8452 15.1192 80.8826 10.7238 75.2124 7.41289C69.5422 4.10194 63.2754 1.94025 56.7698 1.05124C51.7666 0.367541 46.6976 0.446843 41.7345 1.27873C39.2613 1.69328 37.813 4.19778 38.4501 6.62326C39.0873 9.04874 41.5694 10.4717 44.0505 10.1071C47.8511 9.54855 51.7191 9.52689 55.5402 10.0491C60.8642 10.7766 65.9928 12.5457 70.6331 15.2552C75.2735 17.9648 79.3347 21.5619 82.5849 25.841C84.9175 28.9121 86.7997 32.2913 88.1811 35.8758C89.083 38.2158 91.5421 39.6781 93.9676 39.0409Z" fill="currentFill"/></svg>
                        Sending Feedback data to Cornac: {{model}}
                    </li>
                </div>
            </ul>
        </div>
        <div v-else-if="!isEvaluationDone">
            <div>
                <h2 class="mt-8 text-2xl tracking-tight font-semibold text-gray-900">Filters Selected</h2>
            </div>
            <div class="mt-2 grid grid-cols-12 bg-white">
                <div class="col-span-6 mx-2 bg-gray-50 rounded-lg border border-dashed border-gray-900/25 px-6 py-4">
                    <p class="text-base text-xl font-semibold leading-6 text-gray-900">Timestamp</p>

                    <div class="mt-4">
                        <p class="w-1/3 inline bg-gray-100 border ring-1 ring-gray-200 border-gray-300 text-gray-500 text-md font-semibold rounded-lg block w-full px-10 p-2 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white">{{dateFrom.toLocaleDateString()}}</p>
                        <ArrowRightCircleIcon class="inline mx-2 h-10 w-10 text-gray-400" />
                        <p class="w-1/3 inline bg-gray-100 border ring-1 ring-gray-200 border-gray-300 text-gray-500 text-md font-semibold rounded-lg block w-full px-10 p-2 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white">{{dateTo.toLocaleDateString()}}</p>
                    </div>
                </div>
                <div class="col-span-6 mx-2 bg-gray-50 rounded-lg border border-dashed border-gray-900/25 px-6 py-4">
                    <p class="text-base text-xl font-semibold leading-6 text-gray-900 col-span-3">Models</p>
                    <div class="grid grid-cols-3 mt-2">
                        <div v-for="model in models" class="relative mt-2 mx-2 grid place-items-center rounded-lg border border-gray-900/25 py-1 ring-1 ring-gray-200 bg-gray-100">
                            <button type="button" class="absolute right-1 text-gray-400 hover:text-gray-500" />
                            <p class="text-md font-semibold text-center text-gray-500">{{ model }}</p>
                        </div>
                    </div>
                </div>
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
                <div class="my-6 col-span-12">
                    <h2 class="mt-2 text-2xl tracking-tight font-semibold text-gray-900">Feedback data</h2>
                </div>
                <div v-if="isSummaryLoading" class="animate-pulse w-full">
                    <div class="h-10 bg-slate-200 rounded mt-2"></div>
                    <div v-for="index in 5" class="h-6 bg-slate-200 rounded mt-2"></div>
                </div>
                <div v-else-if="feedbackData.length" class="mt-2 max-h-96 overflow-auto overscroll-contain">
                    <p class="text-lg tracking-tight text-gray-900">
                        Summary of data that will be put through individual models.
                    </p>

                    <table class="mt-2 w-full text-md text-left rtl:text-right text-gray-500 dark:text-gray-400">
                        <thead class="text-md text-gray-700 uppercase bg-gray-50 dark:bg-gray-700 dark:text-gray-400">
                            <tr>
                            <th scope="col" class="px-4 py-2">Model</th>
                            <th scope="col" class="px-4 py-2">Number of Feedbacks</th>
                            <th scope="col" class="px-4 py-2">Unique Users</th>
                            <th scope="col" class="px-4 py-2">Unique Items</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr v-for="(feedback, index) in feedbackData" :key="index" class="bg-white border-b dark:bg-gray-800 dark:border-gray-700">
                                <td class="px-4 py-2">{{ feedback.model }}</td>
                                <td class="px-4 py-2">{{ feedback.feedbackCount }}</td>
                                <td class="px-4 py-2">{{ feedback.uniqueUsers }}</td>
                                <td class="px-4 py-2">{{ feedback.uniqueItems }}</td>
                            </tr>
                        </tbody>
                    </table>
                </div>
                <div v-else class="mt-8">
                    <p class="text-xl tracking-tight font-semibold text-gray-900">No feedback data received yet.</p>
                    <p class="text-lg tracking-tight text-gray-900">Please run evaluation again when more feedback data is provided by users.</p>
                </div>
            </div>
            
            <div class="my-6 lg:grid lg:gap-4">
                <h2 class="mt-2 text-2xl tracking-tight font-semibold text-gray-900 col-span-12">Select metrics to compute</h2>
                <p v-if="addedMetrics.length" class="text-lg tracking-tight text-gray-900">
                    These {{ addedMetrics.length }} metrics will be added to the evaluation.
                </p>
                <p v-else class="text-lg tracking-tight text-gray-900">
                    No metrics added yet. Click the button below to add metrics.
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
                            <p v-if="addedMetric.k" class="mt-1 text-md uppercase font-semibold text-center text-white">k={{ addedMetric.k }}</p>
                        </div>
                    </div>
                    <div class="my-2 grid grid-cols-1">
                        <button @click="openModal" class="mt-2 justify-center bg-indigo-50 hover:bg-indigo-100 rounded-lg border-2 border-dashed fill border-indigo-500/50 px-6 py-2">
                            <div class="text-center">
                                <svg class="mx-auto h-7 w-7 text-indigo-600" viewBox="0 0 24 24" fill="currentColor" aria-hidden="true">
                                    <path d="M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm5 11h-4v4h-2v-4H7v-2h4V7h2v4h4v2z"/>
                                </svg>
                                <p class="text-lg leading-5 font-semibold text-indigo-600">Add Metric</p>
                            </div>
                        </button>
                    </div>
                </div>
                <div class="mt-6 flex items-center justify-end gap-x-6 col-span-12">
                    <button type="button" v-on:click="evaluateData" :disabled="!addedMetrics.length" :class="addedMetrics.length? 'bg-indigo-600 hover:bg-indigo-500':'bg-indigo-200'"  class="rounded-md  px-10 py-2 text-md font-semibold text-white shadow-sm focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-indigo-600">Start Evaluation</button>
                </div>
            </div>
        </div>
        <div v-else>
            <div class="lg:grid lg:grid-cols-2 lg:gap-4">
                <div class="mt-8 mb-4 col-span-2">
                    <h2 class="text-2xl font-bold tracking-tight text-gray-900">Evaluation Results</h2>
                </div>
                <div class="col-span-2 my-4">
                    <p class="text-xl font-semibold tracking-tight text-gray-900">
                        Metric Results
                    </p>
                    <table class="w-full text-md text-left rtl:text-right dark:text-gray-400">
                        <thead class="text-md uppercase bg-gray-50 dark:bg-gray-700 dark:text-gray-400">
                            <tr>
                                <th></th>
                                <th v-for="(metric, index) in evaluationResults.metrics" :key="index" scope="col" class="text-md px-4 py-2">{{ metric }}</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr v-for="(model, index) in evaluationResults.modelMetricResults" :key="index" class="bg-white border-b dark:bg-gray-800 dark:border-gray-700">
                                <td class="text-md font-bold px-4 py-2 bg-gray-50 dark:bg-gray-700 dark:text-gray-400">{{ model.model }}</td>
                                <td v-for="(metric, metIndex) in evaluationResults.metrics" :key="valIndex" class="text-lg px-4 py-2">{{ model.metricValues[metric].toFixed(6) }} </td>
                            </tr>
                        </tbody>
                    </table>
                </div>
                <div v-for="(tResult, index) in evaluationResults.tresults" :key="index" class="col-span-2 my-4">
                    <p class="text-xl font-semibold tracking-tight text-gray-900">
                        T-Test p-values - {{ tResult.metric }}
                    </p>
                    <table class="w-full text-md text-left rtl:text-right dark:text-gray-400">
                        <thead class="text-md uppercase bg-gray-50 dark:bg-gray-700 dark:text-gray-400">
                            <tr>
                                <th></th>
                                <th v-for="(tModel, tKey) in tResult.modelComparison" :key="tKey" scope="col" class="text-md px-4 py-2">{{ tModel.model }}</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr v-for="(fromModel, tKey) in tResult.modelComparison" :key="tKey" class="bg-white border-b dark:bg-gray-800 dark:border-gray-700">
                                <td class="text-md font-bold px-4 py-2 bg-gray-50 dark:bg-gray-700 dark:text-gray-400">{{ fromModel.model }}</td>
                                <td v-for="(toModel, valIndex) in tResult.modelComparison" :key="valIndex" class="px-4 py-2 text-lg">{{ fromModel.toModelPValues[toModel.model].pvalue.toFixed(6) }} </td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </div>
            <div class="mt-6 flex items-center justify-start gap-x-6 col-span-12">
                <!-- <button type="button" class="text-sm font-semibold leading-6 text-gray-900">Cancel</button> -->
                <button v-on:click="downloadEvaluation" type="button" class="rounded-md bg-indigo-600 px-3 py-2 text-lg font-semibold text-white shadow-sm hover:bg-indigo-500 focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-indigo-600">Download Evaluation to CSV</button>
            </div>
        </div>
    </div>
</template>

<script>
export default {
    mounted() {
        if (!this.$route.query.dateFrom || !this.$route.query.dateTo || !this.$route.query.experimentId || !this.$route.query.models) {
            this.returnToDashboard();
        } else {
            this.loadFeedbackSummaryData();
        }
        this.dateFrom = new Date(this.$route.query.dateFrom);
        this.dateTo = new Date(this.$route.query.dateTo);
        this.experimentId = this.$route.query.experimentId;
        this.models = this.$route.query.models;
    },
    data() {
        return {
            isSummaryLoading: true,
            isLoading: false,
            error: null,
            experimentId: 1,
            dateFrom: new Date(2024, 0, 12),
            dateTo: new Date(2024, 0, 13),
            models: ["BPR", "BiVAECF", "LightGCN"],
            addedMetrics: [
                {
                    metric: "RMSE",
                    type: "rating",
                },
                {
                    metric: "NDCG",
                    type: "ranking",
                    k: 50
                },
            ],
            isEvaluationDone: false,
            feedbackData:[],
            evaluationResults: {
                metrics: ["AUC", "NDCG@50", "Recall@50"],
                modelMetricResults: [
                    {
                        "model": "BPR",
                        "metricValues": {"AUC": 0.8477, "NDCG@50": 0.0567, "Recall@50": 0.0821}
                    },
                    {
                        "model": "BiVAECF",
                        "metricValues": {"AUC": 0.8991, "NDCG@50": 0.0720, "Recall@50": 0.1117}
                    },
                    {
                        "model": "LightGCN",
                        "metricValues": {"AUC": 0.8982, "NDCG@50": 0.0704, "Recall@50": 0.1097}
                    },
                ],
                tresults: [
                    {
                        "metric": "AUC",
                        "modelComparison": [
                            {
                                model: "BPR",
                                toModelPValues: {
                                    "BPR": {
                                        "tValue": 0.0000,
                                        "pValue": 1.0000,
                                    },
                                    "BiVAECF": {
                                        "tValue": -52.8353,
                                        "pValue": 0.0000,
                                    },
                                    "LightGCN": {
                                        "tValue": -50.7681,
                                        "pValue": 0.0000,
                                    }
                                },
                            },
                            {
                                model: "BiVAECF",
                                toModelPValues: {
                                    "BPR": {
                                        "tValue": 52.8353,
                                        "pValue": 0.0000,
                                    },
                                    "BiVAECF": {
                                        "tValue": 0.0000,
                                        "pValue": 1.0000,
                                    },
                                    "LightGCN": {
                                        "tValue": 1.0878,
                                        "pValue": 0.2767,
                                    },
                                },
                            },
                            {
                                model: "LightGCN",
                                toModelPValues: {
                                    "BPR": {
                                        "tValue": 50.7681,
                                        "pValue": 0.0000,
                                    },
                                    "BiVAECF": {
                                        "tValue": -1.0878,
                                        "pValue": 0.2767,
                                    },
                                    "LightGCN": {
                                        "tValue": 0.0000,
                                        "pValue": 1.0000,
                                    },
                                },
                            },
                        ],
                    },
                    {
                        "metric": "NDCG@50",
                        "modelComparison": [
                            {
                                model: "BPR",
                                toModelPValues: {
                                    "BPR": {
                                        "tValue": 0.0000,
                                        "pValue": 1.0000,
                                    },
                                    "BiVAECF": {
                                        "tValue": -16.2271,
                                        "pValue": 0.0000,
                                    },
                                    "LightGCN": {
                                        "tValue": -14.8709,
                                        "pValue": 0.0000,
                                    }
                                },
                            },
                            {
                                model: "BiVAECF",
                                toModelPValues: {
                                    "BPR": {
                                        "tValue": 16.2271,
                                        "pValue": 0.0000,
                                    },
                                    "BiVAECF": {
                                        "tValue": 0.0000,
                                        "pValue": 1.0000,
                                    },
                                    "LightGCN": {
                                        "tValue": 1.6746,
                                        "pValue": 0.0940,
                                    },
                                },
                            },
                            {
                                model: "LightGCN",
                                toModelPValues: {
                                    "BPR": {
                                        "tValue": 14.8709,
                                        "pValue": 0.0000,
                                    },
                                    "BiVAECF": {
                                        "tValue": -1.6746,
                                        "pValue": 0.0940,
                                    },
                                    "LightGCN": {
                                        "tValue": 0.0000,
                                        "pValue": 1.0000,
                                    },
                                },
                            },
                        ],
                    },
                    {
                        "metric": "Recall@50",
                        "modelComparison": [
                            {
                                model: "BPR",
                                toModelPValues: {
                                    "BPR": {
                                        "tValue": 0.0000,
                                        "pValue": 1.0000,
                                    },
                                    "BiVAECF": {
                                        "tValue": -18.9934,
                                        "pValue": 0.0000,
                                    },
                                    "LightGCN": {
                                        "tValue": -18.0030,
                                        "pValue": 0.0000,
                                    }
                                },
                            },
                            {
                                model: "BiVAECF",
                                toModelPValues: {
                                    "BPR": {
                                        "tValue": 18.9934,
                                        "pValue": 0.0000,
                                    },
                                    "BiVAECF": {
                                        "tValue": 0.0000,
                                        "pValue": 1.0000,
                                    },
                                    "LightGCN": {
                                        "tValue": 1.1628,
                                        "pValue": 0.2449,
                                    },
                                },
                            },
                            {
                                model: "LightGCN",
                                toModelPValues: {
                                    "BPR": {
                                        "tValue": 18.0030,
                                        "pValue": 0.0000,
                                    },
                                    "BiVAECF": {
                                        "tValue": -1.1628,
                                        "pValue": 0.2449,
                                    },
                                    "LightGCN": {
                                        "tValue": 0.0000,
                                        "pValue": 1.0000,
                                    },
                                },
                            },
                        ],
                    },
                ],
            },
        };
    },
    methods: {
        returnToDashboard() {
            this.$router.push({ path: '/dashboard' });
        },
        loadFeedbackSummaryData() {
            getFeedbackSummary(this.models, this.experimentId, this.dateFrom, this.dateTo).then((response) => {
                console.log('Feedback summary data:', response);
                this.feedbackData = response.data;
                this.isSummaryLoading = false;
            }).catch((error) => {
                this.error = error;
                this.isSummaryLoading = false;
            });
        },
        evaluateData() {
            console.log(`Evaluating data...`);
            this.isLoading = true;
            var metrics = this.addedMetrics.map(metric => {
                return metric.type === "ranking" ? `${metric.metric}(k=${metric.k})` : `${metric.metric}()`
            });

            postEvaluationResults(
                metrics, this.models, this.experimentId, this.dateFrom, this.dateTo
            ).then((response) => {
                console.log('Evaluation results:', response);
                this.evaluationResults = response.data;
                this.isEvaluationDone = true;
                this.isLoading = false;
            }).catch((error) => {
                this.error = error;
                this.isLoading = false;
            });
        },
        downloadEvaluation() {
            // Download evaluation results to CSV
            console.log('Downloading evaluation results to CSV');
            var tables = document.getElementsByTagName("table");
            console.log('Tables:', tables);

            var csv = [];

            tables.forEach(table => {
                var rows = table.rows;
                for (var i = 0; i < rows.length; i++) {
                    var row = [],
                        cols = rows[i].cells;
                    for (var j = 0; j < cols.length; j++) {
                        row.push(cols[j].innerText);
                    }
                    csv.push(row.join(","));
                }
                csv.push("\n\n");
            });

            downloadCSV(csv.join("\n"), "evaluation_results.csv");
        },
        downloadCSV(csv, filename) {
            var csvFile;
            var downloadLink;

            // CSV file
            csvFile = new Blob([csv], { type: "text/csv" });

            // Download link
            downloadLink = document.createElement("a");

            // File name
            downloadLink.download = filename;

            // Create a link to the file
            downloadLink.href = window.URL.createObjectURL(csvFile);

            // Hide download link
            downloadLink.style.display = "none";

            // Add the link to DOM
            document.body.appendChild(downloadLink);

            // Click download link
            downloadLink.click();
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
