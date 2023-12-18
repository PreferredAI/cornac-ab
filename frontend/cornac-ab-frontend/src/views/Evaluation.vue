<template>
    <div>
        <label for="days">Select Evaluation Period:</label>
        <select v-model="selectedDays" id="days">
            <option value="7">Past 7 days</option>
            <option value="30">Past 30 days</option>
            <option value="90">Past 90 days</option>
            <option value="365">Past 365 days</option>
        </select>
        <button @click="evaluateData">Evaluate</button>
    </div>
</template>

<script>
export default {
    data() {
        return {
            selectedDays: 7
        };
    },
    methods: {
        evaluateData() {
            // Perform data evaluation based on selectedDays value
            console.log(`Evaluating data for the past ${this.selectedDays} days`);
        }
    }
};
</script>
