<template>
    <div class="container mx-auto p-4">
        <header class="bg-white shadow">
            <div class="mx-auto max-w-7xl px-4 py-6 sm:px-6 lg:px-8">
            <h1 class="text-3xl font-bold tracking-tight text-gray-900">Environment Setup</h1>
            </div>
        </header>
        <form>
            <div class="space-y-12">
            <div class="border-b border-gray-900/10 pb-12 mt-12">
                <h2 class="text-base font-semibold leading-7 text-gray-900">Model setup</h2>
                <p class="mt-1 text-sm leading-6 text-gray-600">Add your trained models by zipping them up and uploading them here.</p>

                <div class="lg:grid lg:grid-cols-2 lg:gap-x-6">
                    <div v-for="index in numModels" :key="index" class="mt-10 grid grid-cols-1">
                        <div class="mt-2 justify-center rounded-lg border border-dashed border-gray-900/25 px-6 py-10">
                            <div class="col-span-4 mt-4">
                                <label for="model-name" class="block text-sm font-medium leading-6 text-gray-900">Model name</label>
                                <div class="mt-2">
                                    <input type="text" name="model-name" id="model-name" class="block w-full px-4 py-2 rounded-md border-0 py-1.5 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-inset focus:ring-indigo-600 sm:text-sm sm:leading-6" placeholder="My model" />
                                </div>
                            </div>
                            <div class="col-span-4 mt-4">
                                <label for="cover-photo" class="block text-sm font-medium leading-6 text-gray-900">Model file</label>
                                <div class="mt-2 flex justify-center rounded-lg border border-dashed border-gray-900/25 px-6 py-10">
                                <div class="text-center">
                                    <PhotoIcon class="mx-auto h-12 w-12 text-gray-300" aria-hidden="true" />
                                    <div class="mt-4 flex text-sm leading-6 text-gray-600">
                                    <label for="file-upload" class="relative cursor-pointer rounded-md bg-white font-semibold text-indigo-600 focus-within:outline-none focus-within:ring-2 focus-within:ring-indigo-600 focus-within:ring-offset-2 hover:text-indigo-500">
                                        <span>Upload a file</span>
                                        <input id="file-upload" name="file-upload" type="file" class="sr-only" />
                                    </label>
                                    <p class="pl-1">or drag and drop</p>
                                    </div>
                                    <p class="text-xs leading-5 text-gray-600">ZIP, up to 10MB</p>
                                </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <button @click="addMoreModel" class="mt-2 bg-blue-500 text-white px-4 py-2 rounded-md">Add more</button>

                </div>

            </div>

            <div class="border-b border-gray-900/10 pb-12 mt-4">
                <h2 class="text-base font-semibold leading-7 text-gray-900">Experiment mode setup</h2>
                <p class="mt-1 text-sm leading-6 text-gray-600">Set up how you would like to split users to receive models.</p>
                Time, user, manual modes.
            </div>

            <div class="mt-6 flex items-center justify-end gap-x-6">
            <!-- <button type="button" class="text-sm font-semibold leading-6 text-gray-900">Cancel</button> -->
            <button type="submit" class="rounded-md bg-indigo-600 px-3 py-2 text-sm font-semibold text-white shadow-sm hover:bg-indigo-500 focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-indigo-600">Continue</button>
            </div>
        </form>

        <div class="mb-4">
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
        </div>
    </div>
</template>

<script>
export default {
    data() {
        return {
            name: '',
            files: [],
            numModels: 2,
        };
    },
    methods: {
        addFile() {
            this.files.push({ name: '' });
        },
        removeFile(index) {
            this.files.splice(index, 1);
        },
        addMoreModel(e) {
            this.numModels += 1;
            e.preventDefault();
        },
    }
};
</script>

<style>
/* Add Tailwind CSS classes here */
</style>
