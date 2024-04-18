<template>
    <div class="rounded border border-stroke py-1 px-1 shadow dark:border-strokedark dark:bg-boxdark dark:bg-slate-600">
        <div class="flex items-center justify-center">
            <div v-if="isLoading" class="animate-pulse w-full">
                <div class="h-2 bg-slate-200 rounded"></div>
                <div class="h-2 bg-slate-200 rounded mt-2"></div>
                <div class="h-2 bg-slate-200 rounded mt-2"></div>
            </div>
            <div v-else>
                <h4 class="text-title-md font-semibold text-black dark:text-white">
                    <slot name="header" />
                </h4>
                <span class="text-md dark:text-white">
                    <slot name="content" />
                </span>
            </div>
        </div>
    </div>
</template>

<script>
export default {
    name: 'TightCard',
    props: ['isLoading'],
};
</script>
