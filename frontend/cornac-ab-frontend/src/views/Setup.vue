<template>
    <div class="container mx-auto p-4">
        <header class="bg-white shadow">
            <div class="mx-auto max-w-7xl px-4 py-6 sm:px-6 lg:px-8">
            <h1 class="text-3xl font-bold tracking-tight text-gray-900">Environment Setup</h1>
            </div>
        </header>
        <div v-if="isLoading" class="mt-10 justify-center rounded-lg border border-dashed border-gray-900/25 bg-blue-100 px-6 py-10">
            <h2 class="mb-2 text-lg font-semibold text-gray-900 dark:text-white">Setting up your Experiment</h2>
            <ul class="max-w-md space-y-2 text-gray-500 list-inside dark:text-gray-400">
                <li class="flex items-center">
                    <svg v-if="!isExperimentCreated" aria-hidden="true" class="w-4 h-4 me-2 text-gray-200 animate-spin dark:text-gray-600 fill-blue-600" viewBox="0 0 100 101" fill="none" xmlns="http://www.w3.org/2000/svg"><path d="M100 50.5908C100 78.2051 77.6142 100.591 50 100.591C22.3858 100.591 0 78.2051 0 50.5908C0 22.9766 22.3858 0.59082 50 0.59082C77.6142 0.59082 100 22.9766 100 50.5908ZM9.08144 50.5908C9.08144 73.1895 27.4013 91.5094 50 91.5094C72.5987 91.5094 90.9186 73.1895 90.9186 50.5908C90.9186 27.9921 72.5987 9.67226 50 9.67226C27.4013 9.67226 9.08144 27.9921 9.08144 50.5908Z" fill="currentColor"/><path d="M93.9676 39.0409C96.393 38.4038 97.8624 35.9116 97.0079 33.5539C95.2932 28.8227 92.871 24.3692 89.8167 20.348C85.8452 15.1192 80.8826 10.7238 75.2124 7.41289C69.5422 4.10194 63.2754 1.94025 56.7698 1.05124C51.7666 0.367541 46.6976 0.446843 41.7345 1.27873C39.2613 1.69328 37.813 4.19778 38.4501 6.62326C39.0873 9.04874 41.5694 10.4717 44.0505 10.1071C47.8511 9.54855 51.7191 9.52689 55.5402 10.0491C60.8642 10.7766 65.9928 12.5457 70.6331 15.2552C75.2735 17.9648 79.3347 21.5619 82.5849 25.841C84.9175 28.9121 86.7997 32.2913 88.1811 35.8758C89.083 38.2158 91.5421 39.6781 93.9676 39.0409Z" fill="currentFill"/></svg>
                    <svg v-else class="w-4 h-4 me-2 text-green-500 dark:text-green-400 flex-shrink-0" aria-hidden="true" xmlns="http://www.w3.org/2000/svg" fill="currentColor" viewBox="0 0 20 20">
                        <path d="M10 .5a9.5 9.5 0 1 0 9.5 9.5A9.51 9.51 0 0 0 10 .5Zm3.707 8.207-4 4a1 1 0 0 1-1.414 0l-2-2a1 1 0 0 1 1.414-1.414L9 10.586l3.293-3.293a1 1 0 0 1 1.414 1.414Z"/>
                    </svg>
                    Creating your experiment environment
                </li>
                <div v-for="(model, index) in models" :key="index">
                    <li class="flex items-center">
                        <svg v-if="!isExperimentCreated" aria-hidden="true" class="w-4 h-4 me-2 text-gray-200 dark:text-gray-600 fill-blue-600" viewBox="0 0 350 350" fill="none" xmlns="http://www.w3.org/2000/svg">
                            <path d="M165,0C74.019,0,0,74.019,0,165s74.019,165,165,165s165-74.019,165-165S255.981,0,165,0z M225.606,175.605
                            l-80,80.002C142.678,258.535,138.839,260,135,260s-7.678-1.464-10.606-4.394c-5.858-5.857-5.858-15.355,0-21.213l69.393-69.396
                            l-69.393-69.392c-5.858-5.857-5.858-15.355,0-21.213c5.857-5.858,15.355-5.858,21.213,0l80,79.998
                            c2.814,2.813,4.394,6.628,4.394,10.606C230,168.976,228.42,172.792,225.606,175.605z"/>
                        </svg>
                        <svg v-else-if="!model.isModelReady" aria-hidden="true" class="w-4 h-4 me-2 text-gray-200 animate-spin dark:text-gray-600 fill-blue-600" viewBox="0 0 100 101" fill="none" xmlns="http://www.w3.org/2000/svg"><path d="M100 50.5908C100 78.2051 77.6142 100.591 50 100.591C22.3858 100.591 0 78.2051 0 50.5908C0 22.9766 22.3858 0.59082 50 0.59082C77.6142 0.59082 100 22.9766 100 50.5908ZM9.08144 50.5908C9.08144 73.1895 27.4013 91.5094 50 91.5094C72.5987 91.5094 90.9186 73.1895 90.9186 50.5908C90.9186 27.9921 72.5987 9.67226 50 9.67226C27.4013 9.67226 9.08144 27.9921 9.08144 50.5908Z" fill="currentColor"/><path d="M93.9676 39.0409C96.393 38.4038 97.8624 35.9116 97.0079 33.5539C95.2932 28.8227 92.871 24.3692 89.8167 20.348C85.8452 15.1192 80.8826 10.7238 75.2124 7.41289C69.5422 4.10194 63.2754 1.94025 56.7698 1.05124C51.7666 0.367541 46.6976 0.446843 41.7345 1.27873C39.2613 1.69328 37.813 4.19778 38.4501 6.62326C39.0873 9.04874 41.5694 10.4717 44.0505 10.1071C47.8511 9.54855 51.7191 9.52689 55.5402 10.0491C60.8642 10.7766 65.9928 12.5457 70.6331 15.2552C75.2735 17.9648 79.3347 21.5619 82.5849 25.841C84.9175 28.9121 86.7997 32.2913 88.1811 35.8758C89.083 38.2158 91.5421 39.6781 93.9676 39.0409Z" fill="currentFill"/></svg>
                        <svg v-else class="w-4 h-4 me-2 text-green-500 dark:text-green-400 flex-shrink-0" aria-hidden="true" xmlns="http://www.w3.org/2000/svg" fill="currentColor" viewBox="0 0 20 20">
                            <path d="M10 .5a9.5 9.5 0 1 0 9.5 9.5A9.51 9.51 0 0 0 10 .5Zm3.707 8.207-4 4a1 1 0 0 1-1.414 0l-2-2a1 1 0 0 1 1.414-1.414L9 10.586l3.293-3.293a1 1 0 0 1 1.414 1.414Z"/>
                        </svg>
                        Spinning up your model: {{model.name}} - cornac.models.{{ model.class }}
                    </li>
                </div>
            </ul>
        </div>
        <form v-else>
            <div class="space-y-12">
                <div class="border-b border-gray-900/10 pb-12 mt-6">
                    <h2 class="mt-6 text-2xl tracking-tight font-semibold text-gray-900">Model setup</h2>
                    <p class="mt-1 text-md text-gray-900">Add your trained models by zipping them up and uploading them here.</p>

                    <div class="lg:grid lg:grid-cols-2 lg:gap-x-6">
                        <div v-for="(model, index) in models" :key="index" class="mt-6 grid grid-cols-1">
                            <div class="transition mt-2 justify-center rounded-lg border border-dashed border-gray-900/25 px-4 py-4">
                                <div class="col-span-4 mt-2">
                                    <label v-bind:for="'model-name-' + index" class="block text-lg font-semibold leading-6 text-gray-900">Model name</label>
                                    <div class="mt-2">
                                        <input type="text" name="model-name" v-bind:id="'model-name-' + index"  v-model="model.name" class="block w-full px-4 py-2 rounded-md border-0 py-1.5 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-inset focus:ring-indigo-600 sm:text-sm sm:leading-6" placeholder="My model" />
                                    </div>
                                </div>
                                <!-- <div class="col-span-4 mt-4">
                                    <label v-bind:for="'model-class-' + index" class="block text-sm font-medium leading-6 text-gray-900">Model class</label>
                                    <div class="mt-2">
                                        <div class="flex w-full rounded-md shadow-sm ring-1 ring-inset ring-gray-300 focus-within:ring-2 focus-within:ring-inset focus-within:ring-indigo-600">
                                            <span class="flex select-none items-center pl-3 text-gray-500 sm:text-sm">cornac.models.</span>
                                            <input type="text" name="model-class" v-bind:id="'model-class-' + index" v-model="model.class" class="block flex-1 border-0 bg-transparent py-2 pl-1 text-gray-900 placeholder:text-gray-400 focus:ring-0 sm:text-sm sm:leading-6" placeholder="MF" />
                                        </div>
                                    </div>
                                </div> -->
                                <div class="col-span-4 mt-4">
                                    <label for="model-file" class="block text-lg font-semibold leading-6 text-gray-900">Model file</label>
                                    <label :class="model.file ? 'bg-green-50': 'bg-grey-50'" v-bind:for="'dropzone-file-' + index" class="flex mt-2 flex-col items-center justify-center w-full h-36 border-2 border-gray-300 border-dashed rounded-lg cursor-pointer dark:hover:bg-bray-800 dark:bg-gray-700 hover:bg-gray-100 dark:border-gray-600 dark:hover:border-gray-500 dark:hover:bg-gray-600">
                                        <div v-if="!model.file" class="flex flex-col items-center justify-center pt-5 pb-6">
                                            <svg class="w-8 h-8 mb-2 text-gray-500 dark:text-gray-400" aria-hidden="true" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 20 16">
                                                <path stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 13h3a3 3 0 0 0 0-6h-.025A5.56 5.56 0 0 0 16 6.5 5.5 5.5 0 0 0 5.207 5.021C5.137 5.017 5.071 5 5 5a4 4 0 0 0 0 8h2.167M10 15V6m0 0L8 8m2-2 2 2"/>
                                            </svg>
                                            <p class="mb-2 text-sm text-gray-500 dark:text-gray-400"><span class="font-semibold">Click to upload</span> or drag and drop</p>
                                            <p class="text-xs text-gray-500 dark:text-gray-400">ZIP, up to 100 MB</p>
                                        </div>
                                        <div v-else class="flex flex-col items-center justify-center pt-5 pb-6">
                                            <svg class="w-8 h-8 mb-2 text-gray-500 dark:text-gray-400" xmlns="http://www.w3.org/2000/svg" height="32" fill="none" viewBox="0 -960 960 960" width="32">
                                                <path fill="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="m424-296 282-282-56-56-226 226-114-114-56 56 170 170Zm56 216q-83 0-156-31.5T197-197q-54-54-85.5-127T80-480q0-83 31.5-156T197-763q54-54 127-85.5T480-880q83 0 156 31.5T763-763q54 54 85.5 127T880-480q0 83-31.5 156T763-197q-54 54-127 85.5T480-80Zm0-80q134 0 227-93t93-227q0-134-93-227t-227-93q-134 0-227 93t-93 227q0 134 93 227t227 93Zm0-320Z"/>
                                            </svg>
                                            <p class="mb-2 text-md text-gray-500 dark:text-gray-400"><span class="font-semibold">File Added</span></p>
                                            <p class="text-md text-gray-500 dark:text-gray-400"><span class="font-semibold">{{model.file.name}}</span>, {{ model.fileSize }}</p>
                                        </div>
                                        <input v-bind:id="'dropzone-file-' + index" type="file" v-on:change="fileAdded($event, index)" accept=".zip" class="hidden" />
                                    </label>
                                </div> 
                                <div v-if="index > 1" class="col-span-4 mt-8">
                                    <div class="text-right">
                                        <button @click="removeModel(index)" type="button" class="inline-flex w-full justify-center rounded-md bg-red-600 px-3 py-2 text-sm font-semibold text-white shadow-sm hover:bg-red-500 sm:ml-3 sm:w-auto">Remove</button>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="mt-6 grid grid-cols-1">
                            <button @click="addMoreModel" class="mt-2 justify-center bg-indigo-50 hover:bg-indigo-100 rounded-lg border border-dashed fill border-gray-900/25 px-6 py-10">

                                    <div class="text-center">
                                        <svg class="mx-auto h-12 w-12 text-indigo-600" viewBox="0 0 24 24" fill="currentColor" aria-hidden="true">
                                            <path d="M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm5 11h-4v4h-2v-4H7v-2h4V7h2v4h4v2z"/>
                                        </svg>
                                        <p class="mt-2 text-lg leading-5 font-semibold text-indigo-600">Add More Models</p>
                                    </div>

                                </button>
                        </div>
                    </div>

                </div>

            </div>
<!-- 
            <div class="border-b border-gray-900/10 pb-12 mt-4">
                <h2 class="text-base font-semibold leading-7 text-gray-900">Experiment mode setup</h2>
                <p class="mt-1 text-sm leading-6 text-gray-600">Set up how you would like to split users to receive models.</p>
                Time, user, manual modes.
            </div> -->

            <div class="mt-6 flex items-center justify-end gap-x-6">
            <!-- <button type="button" class="text-sm font-semibold leading-6 text-gray-900">Cancel</button> -->
            <button v-on:click="submitForm" type="button" class="rounded-md bg-indigo-600 px-3 py-2 text-sm font-semibold text-white shadow-sm hover:bg-indigo-500 focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-indigo-600">Continue</button>
            </div>
        </form>

        <!-- <div class="mb-4">
            <label for="name" class="block font-bold mb-2">Name:</label>
            <input type="text" id="name" v-model="name" class="border border-gray-300 px-4 py-2 rounded-md w-full" />
        </div>

        <div class="mb-4">
            <label for="files" class="block font-bold mb-2">Please upload the Cornac trained model</label>
            <label for="files" class="font-bold mb-2">The model should include a:
                - .pkl file - .pkl.trainset file</label>
            <h2 class="text-lg font-bold mb-2">Files:</h2>
            <ul>
                <li v-for="(file, index) in files" :key="index" class="mb-2">
                    <input type="text" v-model="file.name" class="border border-gray-300 px-4 py-2 rounded-md w-full" />
                    <button @click="removeFile(index)" class="ml-2 bg-red-500 text-white px-4 py-2 rounded-md">Remove</button>
                </li>
            </ul>
            <button @click="addFile" class="mt-2 bg-blue-500 text-white px-4 py-2 rounded-md">Add File</button>
        </div> -->
    </div>
</template>

<script>
import { createCornacInstance, createNewExperiment } from '../services';


export default {
    data() {
        return {
            isLoading: false,
            isExperimentCreated: false,
            loadedModelCount: 0,
            models: [
                {
                    name: 'BPR',
                    class: '',
                    file: { name: 'BPR.zip' },
                    fileSize: '34.85 MB',
                    isModelReady: false,
                },
                {
                    name: 'BiVAECF',
                    class: '',
                    file: { name: 'BiVAECF.zip' },
                    fileSize: '68.56 MB',
                    isModelReady: false,
                },
                {
                    name: 'LightGCN',
                    class: '',
                    file: { name: 'LightGCN.zip' },
                    fileSize: '54.05 MB',
                    isModelReady: false,
                },
            ],
        };
    },
    methods: {
        addMoreModel(e) {
            this.models.push({
                name: '',
                class: '',
                file: '',
                fileSize: '',
                isModelReady: false,
            });
            e.preventDefault();
        },
        removeModel(index) {
            this.models.splice(index, 1);
            e.preventDefault();
        },
        formatFileSize(bytes) {
            var sufixes = ['B', 'kB', 'MB', 'GB', 'TB'];
            var i = Math.floor(Math.log(bytes) / Math.log(1024));
            return `${(bytes / Math.pow(1024, i)).toFixed(2)} ${sufixes[i]}`;
        },
        fileAdded(event, index) {
            this.models[index].file = event.target.files[0];
            this.models[index].fileSize = this.formatFileSize(event.target.files[0].size);
        },
        submitForm(e) {
            this.isLoading = true;

            createNewExperiment({userSeed: 123}).then((response) => {
                this.isExperimentCreated = true;
                console.log("Experiment created");

                this.models.forEach((model) => {
                const formData = new FormData();
                formData.append('name', model.name);
                // formData.append('modelClass', "cornac.models." + model.class);

                formData.append('modelClass', "cornac.models.MF");
                formData.append('file', model.file);
                createCornacInstance(formData).then((response) => {
                    model.isModelReady = true;
                    console.log("Model" + model.name + "created");

                    this.loadedModelCount++;

                    if (this.loadedModelCount == this.models.length) {
                        // redirect to dashboard
                        console.log("All models loaded");
                        this.$router.push('/dashboard');
                    }
                }).catch(function(error){
                    console.log(error);
                    this.isLoading = false;
                });
            });
            }).catch(function(error){
                console.log(error);
                this.isLoading = false;
            });

            e.preventDefault();
        }
    }
};
</script>

<style>
/* Add Tailwind CSS classes here */
</style>
