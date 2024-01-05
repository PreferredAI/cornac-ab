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
