<script setup>
import { getCornacInstances, getActiveExperiment } from '../services';
import SimpleCard from '../components/SimpleCard.vue';

</script>

<template>
    <header class="bg-white shadow">
        <div class="mx-auto max-w-7xl px-4 py-6 sm:px-6 lg:px-8">
        <h1 class="text-3xl font-bold tracking-tight text-gray-900">Experiment Dashboard</h1>
        </div>
    </header>
    <div class="lg:grid lg:gap-4">
        <!-- <div class="mt-4 mb-4">
            <button class="mt-2 bg-blue-500 text-white px-4 py-2 rounded-md">Run Evaluation</button>
        </div> -->
        <div class="mt-8 mb-4 col-span-12">
            <h2 class="text-2xl font-bold tracking-tight text-gray-900">Experiment Details</h2>
        </div>
        <SimpleCard>
            <template #header>
                Experiment ID
            </template>
            <template #content>
                {{ activeExperiment.id }}
            </template>
        </SimpleCard>
        <SimpleCard>
            <template #header>
                Status
            </template>
            <template #content>
                {{ activeExperiment.status }}
            </template>
        </SimpleCard>
        <SimpleCard>
            <template #header>
                Start Time
            </template>
            <template #content>
                {{ activeExperiment.startDateTime }}
            </template>
        </SimpleCard>
        <SimpleCard>
            <template #header>
                User Random Seed
            </template>
            <template #content>
                {{ activeExperiment.userSeed }}
            </template>
        </SimpleCard>

    </div>
    <div class="lg:grid lg:gap-4">
        <!-- <div class="mt-4 mb-4">
            <button class="mt-2 bg-blue-500 text-white px-4 py-2 rounded-md">Run Evaluation</button>
        </div> -->
        <div class="mt-8 mb-4 col-span-12">
            <h2 class="text-2xl font-bold tracking-tight text-gray-900">Model Instances Running</h2>
        </div>
        <div v-for="instance in modelInstances">
            <SimpleCard>
                <template #header>
                    Name: {{ instance.serviceName }}
                </template>
                <template #content>
                    Port {{ instance.port }} - {{ instance.status }}
                </template>
            </SimpleCard>
        </div>
    </div>
    <div class="lg:grid lg:gap-4">
        <div class="mt-8 mb-4 col-span-12">
            <h2 class="text-2xl font-bold tracking-tight text-gray-900">Users Assigned Models</h2>
        </div>
        <div class="max-h-96 overflow-auto overscroll-contain">
            <table v-if="usersAssignedModels.length" class="w-full text-sm text-left rtl:text-right text-gray-500 dark:text-gray-400">
                <thead class="text-xs text-gray-700 uppercase bg-gray-50 dark:bg-gray-700 dark:text-gray-400">
                    <tr>
                    <th scope="col" class="px-6 py-3">User ID</th>
                    <th scope="col" class="px-6 py-3">Model Assigned</th>
                    </tr>
                </thead>
                <tbody>
                    <tr v-for="(userModel, index) in usersAssignedModels" :key="index" class="bg-white border-b dark:bg-gray-800 dark:border-gray-700">
                        <td class="px-6 py-4">{{ userModel.userID }}</td>
                        <td class="px-6 py-4">{{ userModel.modelAssigned }}</td>
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
            <h2 class="text-2xl font-bold tracking-tight text-gray-900">Opensearch Dashboard</h2>
        </div>
        <iframe src="http://0.0.0.0:5601/app/dashboards#/view/33b096d0-aea2-11ee-b947-3122bf2f92d4?embed=true&_g=(filters%3A!()%2CrefreshInterval%3A(pause%3A!t%2Cvalue%3A0)%2Ctime%3A(from%3A'2023-12-13T09%3A01%3A28.484Z'%2Cto%3A'2024-01-12T09%3A01%3A58.901Z'))&show-top-menu=true&show-time-filter=true" height="1024" width="100%"></iframe>
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
                    serviceName: 'MF - Variant A',
                    port: '61278',
                    status: 'RUNNING',
                },
                {
                    serviceName: 'MF - Variant B',
                    port: '61279',
                    status: 'RUNNING',
                },
                {
                    serviceName: 'MF - Variant C',
                    port: '61280',
                    status: 'RUNNING',
                },
            ],
            usersAssignedModels: [
                {
                    userID: '1',
                    modelAssigned: 'MF - Variant A',
                },
                {
                    userID: '4',
                    modelAssigned: 'MF - Variant B',
                },
                {
                    userID: '6',
                    modelAssigned: 'MF - Variant B',
                },
                {
                    userID: '13',
                    modelAssigned: 'MF - Variant A',
                },
                {
                    userID: '15',
                    modelAssigned: 'MF - Variant A',
                },
                {
                    userID: '22',
                    modelAssigned: 'MF - Variant B',
                },
            ],
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

    },
}
</script>

<style>
/* Add any custom styles here */
</style>
