<script setup>
import { getActiveExperiment } from '../services';
import TightCard from '../components/TightCard.vue';
import { ArrowRightCircleIcon, Cog6ToothIcon } from '@heroicons/vue/24/solid'
import { DatePicker } from 'v-calendar';
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
        <header class="bg-white shadow dark:bg-slate-800 dark:shadow-slate-500">
            <div class="mx-auto px-4 py-6 sm:px-6 lg:px-8">
            <h1 class="text-3xl font-bold tracking-tight text-gray-900 dark:text-white">Experiment Dashboard</h1>
            </div>
        </header>
        <div class="mt-6 flex items-center justify-start gap-x-6 col-span-12">
            <button v-on:click="redirectToSetup" type="button" class="inline-flex items-center rounded-md bg-indigo-600 px-3 py-2 text-sm font-semibold text-white shadow-sm hover:bg-indigo-500 focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-indigo-600">
                <Cog6ToothIcon class="-ml-0.5 mr-1.5 h-5 w-5 text-white-400" aria-hidden="true" />
                Start a New Experiment
            </button>
        </div>
        <div v-if="!activeExperiment" class="mt-4 bg-white p-4 rounded dark:bg-slate-500">
            <h2 class="text-2xl font-bold tracking-tight text-gray-900 dark:text-white">Start a New Experiment</h2>
            <p class="text-lg text-gray-900 dark:text-white">No running experiments found. Upload your models to begin a new one!</p>
        </div>
        <div class="lg:grid lg:gap-4">
            <div class="mt-4 col-span-12">
                <h2 class="text-2xl font-bold tracking-tight text-gray-900 dark:text-white">Experiment Details</h2>
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
                <h2 class="text-2xl font-bold tracking-tight text-gray-900 dark:text-white">Model Instances</h2>
            </div>
            <div v-for="instance in activeExperiment.cornacInstances">
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
                <h2 class="text-2xl font-bold tracking-tight text-gray-900 dark:text-white">Users Dashboard</h2>
            </div>
            <div v-if="isLoading" class="animate-pulse w-full">
                <div class="h-10 bg-slate-200 rounded mt-2"></div>
                <div v-for="index in 12" class="h-4 bg-slate-200 rounded mt-2"></div>
            </div>
            <div v-else class="mt-4 max-h-full w-full overflow-auto overscroll-contain">
                <iframe id="overview_iframe" src="http://localhost:5601/app/dashboards#/view/4c3da600-f0cb-11ee-ad50-a7e44edec98b?embed=true&_g=(filters%3A!()%2Cquery%3A(language%3Akuery%2Cquery%3A'')%2CrefreshInterval%3A(pause%3A!t%2Cvalue%3A0)%2Ctime%3A(from%3A'2023-12-08T07%3A39%3A57.280Z'%2Cto%3Anow))&hide-filter-bar=true" height="900" width="100%"></iframe>
            </div>
        </div>
        <div class="mt-8">
            <div class="mt-8 mb-4 col-span-12">
                <h2 class="text-2xl font-bold tracking-tight text-gray-900 dark:text-white">Recommendations Dashboard</h2>
                <p class="text-lg text-gray-900 dark:text-white">This dashboard shows information regarding to the loaded recommendations shown to users.</p>
            </div>

            <div v-if="isLoading" class="animate-pulse w-full">
                <div class="h-10 bg-slate-200 rounded mt-2"></div>
                <div v-for="index in 12" class="h-4 bg-slate-200 rounded mt-2"></div>
            </div>
            <div v-else>
                <iframe :src="getRecommendationIframeUrl()" height="2048" width="100%"></iframe>
            </div>
        </div>
        <div class="mt-8">
            <div class="mt-8 mb-4 col-span-12">
                <h2 class="text-xl font-bold tracking-tight text-gray-900 dark:text-white">Feedback Dashboard</h2>
                <p class="text-lg text-gray-900 dark:text-white">This dashboard shows information regarding to user interactions.</p> 
            </div>

            
            <div class="grid grid-cols-12 bg-gray-100">

                <div class="col-span-12 my-2 px-4">
                    <p class="mt-2 font-semibold">
                        Filter Dashboard by Timestamp & Model
                    </p>
                    <p class="mt-2 font-light">
                        Data filtered can be further used to calculate a variety of rating and ranking metrics available on the Cornac package.
                    </p>
                </div>
                
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
                                <input class="w-1/3 inline bg-gray-50 border border-gray-300 text-gray-900 text-md text-center rounded-lg focus:ring-blue-500 focus:border-blue-500 block px-2 p-2.5" :value="inputValue.start" v-on="inputEvents.start" />
                                <ArrowRightCircleIcon class="inline mx-2 h-10 w-10 text-indigo-600" />
                                <input class="w-1/3 inline bg-gray-50 border border-gray-300 text-gray-900 text-md text-center rounded-lg focus:ring-blue-500 focus:border-blue-500 block px-2 p-2.5" :value="inputValue.end" v-on="inputEvents.end" />
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
                        <div v-else v-for="modelInstance in activeExperiment.cornacInstances">
                            <div class="mt-2 mx-2">
                                <button v-on:click="filterSelected(modelInstance.serviceName)" :class="dashboardFilters.model.indexOf(modelInstance.serviceName) > -1 ? 
                                    'ring-2 ring-indigo-500 bg-indigo-100': ''" class="w-full px-2 text-md font-semibold rounded-lg border text-gray-900 hover:text-gray-500 hover:bg-indigo-200 border-indigo-900/25 py-1">
                                    {{ modelInstance.serviceName }}
                                </button>
                            </div>
                        </div>  
                    </div>
                </div>
                <div v-if="isLoading" class="animate-pulse mt-2 mb-2 ml-4">
                    <div class="h-10 w-full bg-slate-200 rounded-lg"></div>
                </div>
                <div v-else class="mt-2 mb-2 ml-4 col-span-12">
                    <button v-on:click="runEvaluation" class="rounded-md bg-indigo-600 px-6 py-2 text-md font-semibold text-white shadow-sm hover:bg-indigo-500 focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-indigo-600">Run Cornac Evaluation</button>
                    <a class="mx-2 font-medium">
                        <a class="font-bold text-lg text-indigo-600">&lt;&lt;</a> Calculate metrics such as  <a class="font-bold text-indigo-600">RMSE, NDCG, Precision, Recall</a>.
                    </a>
                </div>
            </div>
            <div v-if="isLoading" class="animate-pulse w-full">
                <div class="h-10 bg-slate-200 rounded mt-2"></div>
                <div v-for="index in 12" class="h-4 bg-slate-200 rounded mt-2"></div>
            </div>
            <div v-else>
                <iframe id="feedback_iframe" :src="getFeedbackIframeUrl()" height="2048" width="100%"></iframe>
            </div>
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
                cornacInstances: [
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
            },
            overallStatistics: {
                recommendationsMade: 0,
                popularItemID: 0,
                feedbackMade: 0,
                feedbackToRecommendationsRatio: 0,
                averageFeedbackRanking: 0,
            },
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
                    start: new Date(2023, 11, 23),
                    end: new Date(2024, 0, 11),
                    // end: new Date(),
                },
                model: ["BPR", "BiVAECF", "LightGCN"],
            },
        }
    },
    mounted() {
        this.loadDashboard();
    },
    methods: {
        redirectToSetup() {
            this.$router.push({ path: '/setup' });
        },
        loadDashboard() {
            this.isLoading = true;
            this.error = null;
            // Get active experiment
            getActiveExperiment().then((experiment) => {
                let data = experiment.data;
                this.activeExperiment = data

                this.isLoading = false;

            }).catch((error) => {
                if (error.indexOf("No running experiment found") !== -1) {
                    this.activeExperiment = null;
                } else {
                    this.error = error;
                }
                this.isLoading = false;
            });
        },
        getRecommendationIframeUrl() {
            var toDate = this.dashboardFilters.timestamp.end.toISOString();
            var fromDate = this.dashboardFilters.timestamp.start.toISOString();
            return `http://localhost:5601/app/dashboards#/view/0d1cdc40-ba96-11ee-8517-e5d0135698f5?embed=true&_g=(filters%3A!()%2CrefreshInterval%3A(pause%3A!t%2Cvalue%3A0)%2Ctime%3A(from%3A'${fromDate}'%2Cto%3A'${toDate}'))&hide-filter-bar=true&_a=(query:(language:kuery,query:'experiment_id:${this.activeExperiment.id}'))`;
        },
        getFeedbackIframeUrl() {
            var toDate = this.dashboardFilters.timestamp.end.toISOString();
            var fromDate = this.dashboardFilters.timestamp.start.toISOString();
            var models = this.dashboardFilters.model.join(',');
            return `http://localhost:5601/app/dashboards#/view/7ae59870-b90b-11ee-8517-e5d0135698f5?embed=true&_g=(filters%3A!()%2CrefreshInterval%3A(pause%3A!t%2Cvalue%3A0)%2Ctime%3A(from%3A'${fromDate}'%2Cto%3A'${toDate}'))&hide-filter-bar=true&_a=(query:(language:kuery,query:'model:${models}%20and%20experiment_id:${this.activeExperiment.id}'))`;
        },
        runEvaluation() {
            this.$router.push({ 
                path: '/evaluation', 
                query: {
                    experimentId: this.activeExperiment.id,
                    models: this.dashboardFilters.model,
                    dateFrom: this.dashboardFilters.timestamp.start.toISOString(),
                    dateTo: this.dashboardFilters.timestamp.end.toISOString(),
                }
            });
        },
        updateDashboard(fromDate, toDate) {
            fromDate = fromDate.toISOString();
            toDate = toDate.toISOString();
            document.querySelector('#feedback_iframe').src = `http://localhost:5601/app/dashboards#/view/7ae59870-b90b-11ee-8517-e5d0135698f5?embed=true&_g=(filters%3A!()%2CrefreshInterval%3A(pause%3A!t%2Cvalue%3A0)%2Ctime%3A(from%3A'${fromDate}'%2Cto%3A'${toDate}'))&hide-filter-bar=true&_a=(query:(language:kuery,query:'experiment_id:${this.activeExperiment.id}'))`;
        },
        filterSelected(filter) {
            if (this.dashboardFilters.model.indexOf(filter) > -1) {
                this.dashboardFilters.model.splice(this.dashboardFilters.model.indexOf(filter), 1);
            } else {
                this.dashboardFilters.model.push(filter);
            }
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
