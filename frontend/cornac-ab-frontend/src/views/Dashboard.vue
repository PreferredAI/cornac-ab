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
                End Time
            </template>
            <template #content>
                {{ activeExperiment.endDateTime }}
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
        <!-- <div>
            comments: possible sections in this dashboard:
            - ability to control time frame of recommendations and feedback given by users
            - ability to show popular items recommended to users
            - items with positive feedback given by users
        </div> -->
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
        <!-- <div class="mt-4 mb-4">
            <button class="mt-2 bg-blue-500 text-white px-4 py-2 rounded-md">Run Evaluation</button>
        </div> -->
        <div class="mt-8 mb-4 col-span-12">
            <h2 class="text-2xl font-bold tracking-tight text-gray-900">Overall Statistics</h2>
        </div>
        <SimpleCard>
            <template #header>
                Recommendations Made
            </template>
            <template #content>
                {{ overallStatistics.recommendationsMade }}
            </template>
        </SimpleCard>
        <SimpleCard>
            <template #header>
                Popular Item ID
            </template>
            <template #content>
                {{ overallStatistics.popularItemID }}
            </template>
        </SimpleCard>
        <SimpleCard>
            <template #header>
                Feedback Made
            </template>
            <template #content>
                {{ overallStatistics.feedbackMade }}
            </template>
        </SimpleCard>
        <SimpleCard>
            <template #header>
                Feedback to Recommendations Ratio
            </template>
            <template #content>
                {{ overallStatistics.feedbackToRecommendationsRatio }}
            </template>
        </SimpleCard>
        <SimpleCard>
            <template #header>
                Average Feedback Ranking
            </template>
            <template #content>
                {{ overallStatistics.averageFeedbackRanking }}
            </template>
        </SimpleCard>
        <!-- <div>
            comments: possible sections in this dashboard:
            - ability to control time frame of recommendations and feedback given by users
            - ability to show popular items recommended to users
            - items with positive feedback given by users
        </div> -->

    </div>
    <div class="mt-8">
        <iframe src="http://0.0.0.0:5601/app/dashboards#/view/722b74f0-b882-11e8-a6d9-e546fe2bba5f?embed=true&_a=(description:'Analyze%20mock%20eCommerce%20orders%20and%20revenue',filters:!(),fullScreenMode:!f,options:(hidePanelTitles:!f,useMargins:!t),panels:!((embeddableConfig:(),gridData:(h:10,i:'1',w:36,x:12,y:18),id:'37cc8650-b882-11e8-a6d9-e546fe2bba5f',panelIndex:'1',type:visualization,version:'2.11.1'),(embeddableConfig:(vis:(legendOpen:!f)),gridData:(h:11,i:'2',w:12,x:12,y:7),id:ed8436b0-b88b-11e8-a6d9-e546fe2bba5f,panelIndex:'2',type:visualization,version:'2.11.1'),(embeddableConfig:(),gridData:(h:7,i:'3',w:18,x:0,y:0),id:'09ffee60-b88c-11e8-a6d9-e546fe2bba5f',panelIndex:'3',type:visualization,version:'2.11.1'),(embeddableConfig:(),gridData:(h:7,i:'4',w:30,x:18,y:0),id:'1c389590-b88d-11e8-a6d9-e546fe2bba5f',panelIndex:'4',type:visualization,version:'2.11.1'),(embeddableConfig:(),gridData:(h:11,i:'5',w:48,x:0,y:28),id:'45e07720-b890-11e8-a6d9-e546fe2bba5f',panelIndex:'5',type:visualization,version:'2.11.1'),(embeddableConfig:(),gridData:(h:10,i:'6',w:12,x:0,y:18),id:'10f1a240-b891-11e8-a6d9-e546fe2bba5f',panelIndex:'6',type:visualization,version:'2.11.1'),(embeddableConfig:(),gridData:(h:11,i:'7',w:12,x:0,y:7),id:b80e6540-b891-11e8-a6d9-e546fe2bba5f,panelIndex:'7',type:visualization,version:'2.11.1'),(embeddableConfig:(vis:(legendOpen:!f)),gridData:(h:11,i:'8',w:12,x:24,y:7),id:'4b3ec120-b892-11e8-a6d9-e546fe2bba5f',panelIndex:'8',type:visualization,version:'2.11.1'),(embeddableConfig:(vis:(legendOpen:!f)),gridData:(h:11,i:'9',w:12,x:36,y:7),id:'9ca7aa90-b892-11e8-a6d9-e546fe2bba5f',panelIndex:'9',type:visualization,version:'2.11.1'),(embeddableConfig:(),gridData:(h:18,i:'10',w:48,x:0,y:54),id:'3ba638e0-b894-11e8-a6d9-e546fe2bba5f',panelIndex:'10',type:search,version:'2.11.1'),(embeddableConfig:(mapCenter:!(28.304380682962783,-22.148437500000004),mapZoom:2),gridData:(h:15,i:'11',w:24,x:0,y:39),id:'9c6f83f0-bb4d-11e8-9c84-77068524bcab',panelIndex:'11',type:visualization,version:'2.11.1'),(embeddableConfig:(),gridData:(h:15,i:'12',w:24,x:24,y:39),id:b72dd430-bb4d-11e8-9c84-77068524bcab,panelIndex:'12',type:visualization,version:'2.11.1')),query:(language:kuery,query:''),timeRestore:!t,title:'%5BeCommerce%5D%20Revenue%20Dashboard',viewMode:edit)&_g=(filters:!(),refreshInterval:(pause:!f,value:900000),time:(from:now-7d,to:now))" height="1024" width="100%"></iframe>
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
                id: '',
                status: '',
                startDateTime: '',
                endDateTime: '',
                userSeed: '',
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
                    serviceName: '',
                    port: '',
                    status: '',
                },
                {
                    serviceName: '',
                    port: '',
                    status: '',
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
