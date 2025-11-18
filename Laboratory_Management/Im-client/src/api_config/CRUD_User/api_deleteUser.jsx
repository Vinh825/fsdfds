import http from "../../axios/axios.jsx";

export const api_deleteUser = async (userId) => {
    return http.delete(`/users/${userId}`);
};

