<template>
    <div class="grid grid-cols-subgrid col-span-3">
        <div class="block p-6 bg-white border border-gray-200 rounded-lg shadow hover:bg-gray-100 dark:bg-gray-800 dark:border-gray-700 dark:hover:bg-gray-700">
            <h5 class="mb-2 text-xl font-semibold tracking-tight text-gray-900 dark:text-white">
                <slot name="header" />
            </h5>
            <p class="font-normal text-lg text-gray-700 dark:text-gray-400">
                <slot name="content" />
            </p>
        </div>
    </div>
    <!-- <div class="bg-white rounded-lg shadow-md p-4">
        <slot></slot>
    </div> -->
</template>

<script>
export default {
    name: 'SimpleCard',
};
</script>
