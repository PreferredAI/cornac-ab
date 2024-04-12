import {axios} from '../axios'

export const createNewExperiment = (userSeed) => {
    return axios.post('/experiment/', {
        userSeed: userSeed,
    });
}

export const getExperiments = () => {
    return axios.get(`/experiment/`);
}

export const getActiveExperiment = () => {
    return axios.get(`/experiment/active`);
}

export const getCornacInstances = () => {
    return axios.get(`/experiment/instance`);
}

export const postEvaluationResults = (metrics, models, experimentId, dateFrom, dateTo) => {
    return axios.post(`/experiment/evaluate`, {
        metrics: metrics,
        models: models,
        experimentId: experimentId,
        dateFrom: dateFrom,
        dateTo: dateTo
    });
}

export const getFeedbackSummary = (models, experimentId, dateFrom, dateTo) => {
    return axios.get(`/experiment/feedback/summary`, {
        params: {
            models: models,
            experimentId: experimentId,
            dateFrom: dateFrom,
            dateTo: dateTo
        },
        paramsSerializer: {
            indexes: null, // removes brackets in query params
        }
    });
}