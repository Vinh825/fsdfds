import http from "../../axios/axios.jsx";

export const api_getUserById = async (userId) => {
    return http.get(`/users/${userId}`);
};

