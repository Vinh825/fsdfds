import http from "../axios/axios.jsx";

export const api_changePassword = async (
    userId,
    oldPassword,
    newPassword,
    confirmNewPassword
) => {
    return http.post(`/users/change-password/${userId}`, {
        oldPassword,
        newPassword,
        confirmNewPassword,
    });
};

