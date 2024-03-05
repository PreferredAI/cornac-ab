<script setup>
import { getCornacInstances, getActiveExperiment } from '../services';
import SimpleCard from '../components/SimpleCard.vue';
import TightCard from '../components/TightCard.vue';
import { ArrowRightCircleIcon } from '@heroicons/vue/24/solid'
import { Calendar, DatePicker } from 'v-calendar';
import 'v-calendar/style.css';

</script>

<template>
    <div v-if="error">
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
                <button class="mt-4 bg-white hover:bg-orange-500 text-orange-700 font-semibold hover:text-white py-2 px-4 border border-orange-500 hover:border-transparent rounded" @click="loadDashboard()" >
                Try again
                </button>
            </div>
        </div>
    </div>
    <div v-else class="container mx-auto p-4">
        <header class="bg-white shadow">
            <div class="mx-auto px-4 py-6 sm:px-6 lg:px-8">
            <h1 class="text-3xl font-bold tracking-tight text-gray-900">Experiment Dashboard</h1>
            </div>
        </header>
        <div class="lg:grid lg:gap-4">
            <div class="mt-4 col-span-12">
                <h2 class="text-2xl font-bold tracking-tight text-gray-900">Experiment Details</h2>
            </div>        
            <TightCard :isLoading=isLoading>
                <template #header>
                    Experiment ID
                </template>
                <template #content>
                    {{ activeExperiment.id }}
                </template>
            </TightCard>
            <TightCard :isLoading=isLoading>
                <template #header>
                    Status
                </template>
                <template #content>
                    {{ activeExperiment.status }}
                </template>
            </TightCard>
            <TightCard :isLoading=isLoading>
                <template #header>
                    Start Time
                </template>
                <template #content>
                    {{ activeExperiment.startDateTime }}
                </template>
            </TightCard>
            <TightCard :isLoading=isLoading>
                <template #header>
                    User Random Seed
                </template>
                <template #content>
                    {{ activeExperiment.userSeed }}
                </template>
            </TightCard>

        </div>
        <div class="lg:grid lg:gap-4">
            <div class="mt-4 col-span-12">
                <h2 class="text-2xl font-bold tracking-tight text-gray-900">Model Instances</h2>
            </div>
            <div v-for="instance in modelInstances">
                <TightCard :isLoading=isLoading>
                    <template #header>
                        {{ instance.serviceName }}
                    </template>
                    <template #content>
                        Port {{ instance.port }} - {{ instance.status }}
                    </template>
                </TightCard>
            </div>
        </div>
        <div>
            <div class="mt-4">
                <h2 class="text-2xl font-bold tracking-tight text-gray-900">Users Assigned Models</h2>
            </div>
            <div v-if="isLoading" class="animate-pulse w-full">
                <div class="h-10 bg-slate-200 rounded mt-2"></div>
                <div v-for="index in 12" class="h-4 bg-slate-200 rounded mt-2"></div>
            </div>
            <div v-else class="mt-4 max-h-96 w-full overflow-auto overscroll-contain">
                <table v-if="usersAssignedModels.length" class="w-full text-left rtl:text-right text-gray-500 dark:text-gray-400">
                    <thead class="text-sm text-gray-700 uppercase bg-gray-50 dark:bg-gray-700 dark:text-gray-400">
                        <tr>
                            <th scope="col" class="px-6 py-3">User ID</th>
                            <th scope="col" class="px-6 py-3">Model Assigned</th>
                            <th scope="col" class="px-6 py-3">Recommendations Served</th>
                            <th scope="col" class="px-6 py-3">Feedbacks by User</th>
                            <th scope="col" class="px-6 py-3">Last Feedback Interaction</th>
                        </tr>
                    </thead>
                    <tbody class="text-sm">
                        <tr v-for="(userModel, index) in usersAssignedModels" :key="index" class="bg-white border dark:bg-gray-800 dark:border-gray-700">
                            <td class="px-6 py-2">{{ userModel.userID }}</td>
                            <td class="px-6 py-2">{{ userModel.modelAssigned }}</td>
                            <td class="px-6 py-2">{{ userModel.recommendationsMade }}</td>
                            <td class="px-6 py-2">{{ userModel.feedbackMade }}</td>
                            <td class="px-6 py-2">{{ userModel.lastFeedback }}</td>
                        </tr>
                    </tbody>
                </table>
                <div v-else>
                    <p class="text-xl tracking-tight font-semibold text-gray-900">No assigned users yet.</p>
                    <p class="text-l tracking-tight text-gray-900">This table will populate as more recommendations are made by users.</p>
                </div>
            </div>
        </div>
        <div class="mt-8">
            <div class="mt-8 mb-4 col-span-12">
                <h2 class="text-2xl font-bold tracking-tight text-gray-900">Recommendations Dashboard</h2>
                <p class="text-lg text-gray-900">This dashboard shows information regarding to the loaded recommendations shown to users.</p>
            </div>

            <div v-if="isLoading" class="animate-pulse w-full">
                <div class="h-10 bg-slate-200 rounded mt-2"></div>
                <div v-for="index in 12" class="h-4 bg-slate-200 rounded mt-2"></div>
            </div>
            <div v-else>
                <iframe :src="getRecommendationIframeUrl()" height="2048" width="100%"></iframe>
            </div>
            <!-- <iframe id="opensearch_iframe" src="http://0.0.0.0:5602/app/dashboards#/view/7ae59870-b90b-11ee-8517-e5d0135698f5?embed=true&_g=(filters%3A!()%2CrefreshInterval%3A(pause%3A!t%2Cvalue%3A0)%2Ctime%3A(from%3A'2023-12-23T07%3A22%3A52.702Z'%2Cto%3A'2024-01-11T07%3A22%3A52.702Z'))&show-query-input=true&show-time-filter=true&hide-filter-bar=true" height="2048" width="100%"></iframe> -->
            <!-- <iframe src="http://0.0.0.0:5601/app/dashboards#/view/33b096d0-aea2-11ee-b947-3122bf2f92d4?embed=true&_g=(filters%3A!()%2CrefreshInterval%3A(pause%3A!t%2Cvalue%3A0)%2Ctime%3A(from%3A'2023-12-13T09%3A01%3A28.484Z'%2Cto%3A'2024-01-12T09%3A01%3A58.901Z'))&show-top-menu=true&show-time-filter=true" height="1024" width="100%"></iframe> -->
        </div>
        <div class="mt-8">
            <div class="mt-8 mb-4 col-span-12">
                <h2 class="text-xl font-bold tracking-tight text-gray-900">Feedback Dashboard</h2>
                <p class="text-lg text-gray-900">This dashboard shows information regarding to user interactions.</p> 
                <p class="mt-2">Use the controls to update the dashboard and select "Run Evaluation" to calculate metrics of individual models based on the user feedback data.</p>
            </div>

            
            <div class="grid grid-cols-12 bg-gray-100">
                
                <div class="col-span-6 my-2 mx-2 bg-white rounded-lg border border-dashed border-gray-900/25 px-6 py-4">
                    <p class="text-base font-semibold leading-6 text-gray-900">Filter by Timestamp</p>

                    <div v-if="isLoading" class="animate-pulse mt-4">
                        <div class="h-10 inline-block w-1/3 bg-slate-200 rounded"></div>
                        <ArrowRightCircleIcon class="inline-block mx-2 mb-6 h-10 w-10 text-gray-300" />
                        <div class="h-10 inline-block w-1/3 bg-slate-200 rounded"></div>
                    </div>

                    <div v-else>
                        <DatePicker v-model.range="dashboardFilters.timestamp" color="indigo" is-required>
                            <template #default="{ inputValue, inputEvents }">
                            <div class="mt-2">
                                <input class="w-1/3 inline bg-gray-50 border border-gray-300 text-gray-900 text-md text-center rounded-lg focus:ring-blue-500 focus:border-blue-500 block px-2 p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500" :value="inputValue.start" v-on="inputEvents.start" />
                                <ArrowRightCircleIcon class="inline mx-2 h-10 w-10 text-indigo-600" />
                                <input class="w-1/3 inline bg-gray-50 border border-gray-300 text-gray-900 text-md text-center rounded-lg focus:ring-blue-500 focus:border-blue-500 block px-2 p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500" :value="inputValue.end" v-on="inputEvents.end" />
                            </div>
                            </template>
                        </DatePicker>
                    </div>
                </div>
                <div class="col-span-6 my-2 mx-2 bg-white rounded-lg border border-dashed border-gray-900/25 px-6 py-4">
                    <p class="text-base font-semibold leading-6 text-gray-900 col-span-3">Filter by Model</p>
                    <div class="grid grid-cols-3 mt-2">
                        <div v-if="isLoading" v-for="index in 3" class="animate-pulse mt-2 mx-2">
                            <div class="h-10 inline-block w-full bg-slate-200 rounded-lg"></div>
                        </div>
                        <div v-else v-for="modelInstance in modelInstances">
                            <div :class="dashboardFilters.model.indexOf(modelInstance.serviceName) > -1 ? 'ring-2 ring-indigo-500 bg-indigo-100': ''" class="relative mt-4 mx-2 grid place-items-center rounded-lg border hover:bg-indigo-200 border-indigo-900/25 py-1">
                                <button type="button" class="absolute right-1 text-gray-400 hover:text-gray-500" @click="filterSelected(modelInstance.serviceName)">
                                    <!-- <span class="sr-only">Remove</span> -->
                                    <!-- <XMarkIcon class="h-6 w-6" aria-hidden="true" /> -->
                                </button>
                                <p class="text-md font-semibold text-center text-black">{{ modelInstance.serviceName }}</p>
                            </div>
                            <!-- Active: "ring-2 ring-indigo-500" -->
                        </div>  
                    </div>
                </div>
                <div v-if="isLoading" class="animate-pulse mt-2 mb-2 ml-4">
                    <div class="h-10 w-full bg-slate-200 rounded-lg"></div>
                </div>
                <div v-else class="mt-2 mb-2 ml-4 col-span-12">
                    <button v-on:click="runEvaluation" class="rounded-md bg-indigo-600 px-6 py-2 text-md font-semibold text-white shadow-sm hover:bg-indigo-500 focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-indigo-600">Run Cornac Evaluation</button>
                </div>
            </div>
            <div v-if="isLoading" class="animate-pulse w-full">
                <div class="h-10 bg-slate-200 rounded mt-2"></div>
                <div v-for="index in 12" class="h-4 bg-slate-200 rounded mt-2"></div>
            </div>
            <div v-else>
                <iframe id="feedback_iframe" :src="getFeedbackIframeUrl()" height="2048" width="100%"></iframe>
            </div>
            <!-- <iframe id="feedback_iframe" src="http://0.0.0.0:5602/app/dashboards#/view/7ae59870-b90b-11ee-8517-e5d0135698f5?embed=true&_g=(filters%3A!()%2CrefreshInterval%3A(pause%3A!t%2Cvalue%3A0)%2Ctime%3A(from%3A'2023-12-23T07%3A22%3A52.702Z'%2Cto%3A'2024-02-21T16:00:00.000Z'))&_a=(query:(language:kuery,query:'model=BPR'))&show-time-filter=true&show-top-menu=true&hide-filter-bar=true&show-query-input=true" height="2048" width="100%"></iframe> -->
            <!-- <iframe src="http://0.0.0.0:5601/app/dashboards#/view/33b096d0-aea2-11ee-b947-3122bf2f92d4?embed=true&_g=(filters%3A!()%2CrefreshInterval%3A(pause%3A!t%2Cvalue%3A0)%2Ctime%3A(from%3A'2023-12-13T09%3A01%3A28.484Z'%2Cto%3A'2024-01-12T09%3A01%3A58.901Z'))&show-top-menu=true&show-time-filter=true" height="1024" width="100%"></iframe> -->
        </div>
    </div>
</template>



<script>
import { getActiveExperiment } from '../services';

export default {
    name: 'Dashboard',
    data() {
        return {
            isLoading: false,
            error: null,
            activeExperiment: {
                id: '1',
                status: 'RUNNING',
                startDateTime: '2024-01-01 00:00:00',
                endDateTime: '',
                userSeed: '123',
            },
            overallStatistics: {
                recommendationsMade: 0,
                popularItemID: 0,
                feedbackMade: 0,
                feedbackToRecommendationsRatio: 0,
                averageFeedbackRanking: 0,
            },
            modelInstances: [
                {
                    serviceName: 'BPR',
                    port: '61278',
                    status: 'RUNNING',
                },
                {
                    serviceName: 'BiVAECF',
                    port: '61279',
                    status: 'RUNNING',
                },
                {
                    serviceName: 'LightGCN',
                    port: '61280',
                    status: 'RUNNING',
                },
            ],
            usersAssignedModels: [
                {
                    userID: '1',
                    modelAssigned: 'BPR',
                    recommendationsMade: 25,
                    feedbackMade: 10,
                    lastFeedback: '2024-01-06 16:23:42'
                },
                {
                    userID: '4',
                    modelAssigned: 'BPR',
                    recommendationsMade: 43,
                    feedbackMade: 12,
                    lastFeedback: '2024-01-12 20:46:11'
                },
                {
                    userID: '6',
                    modelAssigned: 'LightGCN',
                    recommendationsMade: 12,
                    feedbackMade: 1,
                    lastFeedback: '2024-01-01 09:02:02'
                },
                {
                    userID: '13',
                    modelAssigned: 'BPR',
                    recommendationsMade: 59,
                    feedbackMade: 35,
                    lastFeedback: '2024-01-11 23:20:49'
                },
                {
                    userID: '15',
                    modelAssigned: 'LightGCN',
                    recommendationsMade: 49,
                    feedbackMade: 32,
                    lastFeedback: '2024-01-12 21:55:56'
                },
                {
                    userID: '23',
                    modelAssigned: 'BiVAECF',
                    recommendationsMade: 65,
                    feedbackMade: 36,
                    lastFeedback: '2024-01-11 23:45:32'
                },
            ],
            dashboardFilters:{
                timestamp: {
                    start: new Date(2023, 12, 23),
                    end: new Date(2024, 1, 11),
                },
                model: ["BPR", "BiVAECF", "LightGCN"],
            },
        }
    },
    mounted() {
        this.loadDashboard();
    },
    methods: {
        loadDashboard() {
            this.isLoading = true;
            this.error = null;
            // Get active experiment
            getActiveExperiment().then((experiment) => {
                let data = experiment.data;
                this.activeExperiment = data

                // Get model instances
                getCornacInstances().then((instances) => {
                    let data = instances.data;
                    this.modelInstances = data;

                    this.isLoading = false;
                }).catch((error) => {
                    this.error = error;
                    this.isLoading = false;
                });

            }).catch((error) => {
                this.error = error;
                this.isLoading = false;
            });
        },
        loadActiveExperiment() {
            // Get active experiment
            getActiveExperiment().then((experiment) => {
                this.isLoading = false;
                let data = experiment.data;
                this.activeExperiment = data
            }).catch((error) => {
                this.error = error;
            });
        },
        loadModelInstances() {
            // Get model instances
            getCornacInstances().then((instances) => {
                let data = instances.data;
                this.modelInstances = data;
            }).catch((error) => {
                this.error = error;
            });
        },
        getRecommendationIframeUrl() {
            var toDate = (new Date()).toISOString();
            var fromDate = (new Date()).toISOString();
            return `http://0.0.0.0:5601/app/dashboards#/view/0d1cdc40-ba96-11ee-8517-e5d0135698f5?embed=true&_g=(filters%3A!()%2CrefreshInterval%3A(pause%3A!t%2Cvalue%3A0)%2Ctime%3A(from%3A'${fromDate}'%2Cto%3A'${toDate}'))&hide-filter-bar=true&show-query-input=true&_a=(query:(language:kuery,query:'experiment_id:${this.activeExperiment.id}'))`;
        },
        getFeedbackIframeUrl() {
            var toDate = (new Date()).toISOString();
            var fromDate = (new Date()).toISOString();
            return `http://0.0.0.0:5601/app/dashboards#/view/7ae59870-b90b-11ee-8517-e5d0135698f5?embed=true&_g=(filters%3A!()%2CrefreshInterval%3A(pause%3A!t%2Cvalue%3A0)%2Ctime%3A(from%3A'${fromDate}'%2Cto%3A'${toDate}'))&hide-filter-bar=true&_a=(query:(language:kuery,query:'experiment_id:${this.activeExperiment.id}'))`;
        },
        runEvaluation() {
            alert(document.querySelector('div[data-test-subj="dataSharedTimefilterDuration"]')['data-shared-timefilter-duration']);
        },
        updateDashboard(fromDate, toDate) {
            fromDate = fromDate.toISOString();
            toDate = toDate.toISOString();
            document.querySelector('#feedback_iframe').src = `http://0.0.0.0:5601/app/dashboards#/view/7ae59870-b90b-11ee-8517-e5d0135698f5?embed=true&_g=(filters%3A!()%2CrefreshInterval%3A(pause%3A!t%2Cvalue%3A0)%2Ctime%3A(from%3A'${fromDate}'%2Cto%3A'${toDate}'))&hide-filter-bar=true&_a=(query:(language:kuery,query:'experiment_id:${this.activeExperiment.id}'))`;
        },
        filterSelected(filter) {
            alert(filter);
            if (this.dashboardFilters.model.indexOf(filter) > -1) {
                this.dashboardFilters.model.splice(this.dashboardFilters.model.indexOf(filter), 1);
            } else {
                this.dashboardFilters.model.push(filter);
            }
            alert(this.dashboardFilters.model);
        }

    },
    watch: {
        dashboardFilters: {
            handler: function (newVal, oldVal) {
                const fromDate = newVal.timestamp.start;
                const toDate = newVal.timestamp.end;
                this.updateDashboard(fromDate, toDate);
            },
            deep: true
        },
    }
}
</script>

<style>
/* Add any custom styles here */
</style>
