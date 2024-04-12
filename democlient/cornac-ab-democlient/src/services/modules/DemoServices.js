import {axios} from "../axios";

export const postFeedback = (recommendId, itemId, rating) => {
    return axios.post("/app/feedback/", {}, {
        params: {
            recommendId: recommendId,
            itemId: itemId,
            rating: rating
        }
    });
}

export const getRecommendations = (userId) => {
    return axios.get("/app/recommendations/", {
        params: {
            userId: userId
        }
    });
}
