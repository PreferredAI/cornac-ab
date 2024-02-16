import {axios} from "../axios";

export const postFeedback = (experimentId, userId, itemId) => {
    return axios.post("/demo/feedback", {
        experimentId: experimentId,
        userId: userId,
        itemId: itemId
    });
}

export const getRecommendations = (experimentId, userId) => {
    return axios.get("/demo/recommendations", {
        params: {
            experimentId: experimentId,
            userId: userId
        }
    });
}
