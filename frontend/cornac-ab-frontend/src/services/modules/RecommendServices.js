import {axios} from '../axios'

export const createCornacInstance = (data) => {
    return axios.post('/recommend/instance', data)
}

export const getCornacInstances = () => {
    return axios.get(`/recommend/instance`)
}
