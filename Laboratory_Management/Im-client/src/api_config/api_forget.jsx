import http from "../axios/axios";


export const sendEmail = (email, username) => {
    return http.post("/forget", {
        to: { email, username }
    });
};


export const verifyOtp = ({ email, code }) => {
    return http.post("/forget/verify", { email, code });
};


export const resetPassword = ({ email, code, newPassword, confirmNewPassword }) => {
    return http.post("/forget/reset", { email, code, newPassword, confirmNewPassword });
};

