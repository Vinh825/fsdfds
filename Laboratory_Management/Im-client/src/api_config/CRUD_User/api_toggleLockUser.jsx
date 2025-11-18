import http from "../../axios/axios.jsx";

export const api_toggleLockUser = async (userId, locked) => {
    return http.patch(`/users/${userId}/lock`, { locked });
};

