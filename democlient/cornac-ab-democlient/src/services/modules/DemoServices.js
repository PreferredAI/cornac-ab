import {axios} from "../axios";

export const postFeedback = (recommendId, itemId, rating, action) => {
    return axios.post("/app/feedback/", {}, {
        params: {
            recommendId: recommendId,
            itemId: itemId,
            rating: rating,
            action: action,
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
