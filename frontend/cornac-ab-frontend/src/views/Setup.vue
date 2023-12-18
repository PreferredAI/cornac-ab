<template>
    <div class="container mx-auto p-4">
        <header class="bg-white shadow">
            <div class="mx-auto max-w-7xl px-4 py-6 sm:px-6 lg:px-8">
            <h1 class="text-3xl font-bold tracking-tight text-gray-900">Environment Setup</h1>
            </div>
        </header>

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
            files: []
        };
    },
    methods: {
        addFile() {
            this.files.push({ name: '' });
        },
        removeFile(index) {
            this.files.splice(index, 1);
        }
    }
};
</script>

<style>
/* Add Tailwind CSS classes here */
</style>
