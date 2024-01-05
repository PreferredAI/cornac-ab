import {axios} from '../axios'

export const createCornacInstance = (formData) => {
    return axios.post('/recommend/instance', formData, {
        headers: {
            'Content-Type': 'multipart/form-data'
        }
    });
}

export const getCornacInstances = () => {
    return axios.get(`/recommend/instance`);
}
