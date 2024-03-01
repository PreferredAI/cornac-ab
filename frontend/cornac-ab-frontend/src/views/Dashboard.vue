<script setup>
import { getCornacInstances, getActiveExperiment } from '../services';
import SimpleCard from '../components/SimpleCard.vue';
import TightCard from '../components/TightCard.vue';
import { ArrowRightCircleIcon } from '@heroicons/vue/24/solid'
import { Calendar, DatePicker } from 'v-calendar';
import 'v-calendar/style.css';

</script>

<template>
    <div class="container mx-auto p-4">
        <header class="bg-white shadow">
            <div class="mx-auto px-4 py-6 sm:px-6 lg:px-8">
            <h1 class="text-3xl font-bold tracking-tight text-gray-900">Experiment Dashboard</h1>
            </div>
        </header>
        <div class="lg:grid lg:gap-4">
            <div class="mt-4 col-span-12">
                <h2 class="text-2xl font-bold tracking-tight text-gray-900">Experiment Details</h2>
            </div>        
            <TightCard>
                <template #header>
                    Experiment ID
                </template>
                <template #content>
                    {{ activeExperiment.id }}
                </template>
            </TightCard>
            <TightCard>
                <template #header>
                    Status
                </template>
                <template #content>
                    {{ activeExperiment.status }}
                </template>
            </TightCard>
            <TightCard>
                <template #header>
                    Start Time
                </template>
                <template #content>
                    {{ activeExperiment.startDateTime }}
                </template>
            </TightCard>
            <TightCard>
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
                <TightCard>
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
            <div class="mt-4 max-h-96 w-full overflow-auto overscroll-contain">
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

            <iframe src="http://0.0.0.0:5601/app/dashboards#/view/0d1cdc40-ba96-11ee-8517-e5d0135698f5?embed=true&_g=(filters%3A!()%2CrefreshInterval%3A(pause%3A!t%2Cvalue%3A0)%2Ctime%3A(from%3A'2023-12-23T07%3A22%3A52.702Z'%2Cto%3A'2023-12-28T09%3A06%3A38.135Z'))&hide-filter-bar=true&show-query-input=true&_a=(query:(language:kuery,query:'experiment_id:1'))" height="2048" width="100%"></iframe>

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
                <div class="col-span-6 my-2 mx-2 bg-white rounded-lg border border-dashed border-gray-900/25 px-6 py-4">
                    <p class="text-base font-semibold leading-6 text-gray-900 col-span-3">Filter by Model</p>
                    <div class="grid grid-cols-3 mt-2">
                        <div v-for="modelInstance in modelInstances">
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
                <div class="mt-2 mb-2 ml-4 col-span-12">
                    <button v-on:click="runEvaluation" class="rounded-md bg-indigo-600 px-6 py-2 text-md font-semibold text-white shadow-sm hover:bg-indigo-500 focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-indigo-600">Run Cornac Evaluation</button>
                </div>
            </div>

            <iframe id="feedback_iframe" src="http://0.0.0.0:5601/app/dashboards#/view/7ae59870-b90b-11ee-8517-e5d0135698f5?embed=true&_g=(filters%3A!()%2CrefreshInterval%3A(pause%3A!t%2Cvalue%3A0)%2Ctime%3A(from%3A'2023-12-23T07%3A22%3A52.702Z'%2Cto%3A'2024-02-21T16:00:00.000Z'))&hide-filter-bar=true&_a=(query:(language:kuery,query:'experiment_id:1'))" height="2048" width="100%"></iframe>
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
        this.loadActiveExperiment();
        this.loadModelInstances();
    },
    methods: {
        loadActiveExperiment() {
            // this.isLoading = true;
            // Get active experiment
            getActiveExperiment().then((experiment) => {
                let data = experiment.data;
                this.activeExperiment = data
            });
        },
        loadModelInstances() {
            // this.isLoading = true;
            // Get model instances
            getCornacInstances().then((instances) => {
                let data = instances.data;
                this.modelInstances = data;
            });
        },
        runEvaluation() {
            alert(document.querySelector('div[data-test-subj="dataSharedTimefilterDuration"]')['data-shared-timefilter-duration']);
        },
        updateDashboard(fromDate, toDate) {
            fromDate = fromDate.toISOString();
            toDate = toDate.toISOString();
            document.querySelector('#feedback_iframe').src = `http://0.0.0.0:5601/app/dashboards#/view/7ae59870-b90b-11ee-8517-e5d0135698f5?embed=true&_g=(filters%3A!()%2CrefreshInterval%3A(pause%3A!t%2Cvalue%3A0)%2Ctime%3A(from%3A'${fromDate}'%2Cto%3A'${toDate}'))&hide-filter-bar=true&_a=(query:(language:kuery,query:'experiment_id:1'))`;
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
