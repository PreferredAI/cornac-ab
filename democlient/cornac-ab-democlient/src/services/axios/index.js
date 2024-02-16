import Axios from 'axios';

const axios = Axios.create({
    baseURL: 'http://127.0.0.1:8080/api',
    timeout: 0,
    // headers: {
    //     'Content-Type': 'application/json',
    //     'Accept': 'application/json'
    // }
});

export { axios };