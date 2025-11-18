import http from "../../axios/axios.jsx";

export const api_updateUserRoles = async (userId, roleCodes = []) => {
    return http.put(`/users/${userId}/roles`, { roleCodes });
};

