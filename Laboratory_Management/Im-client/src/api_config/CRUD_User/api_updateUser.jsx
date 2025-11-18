import http from "../../axios/axios.jsx";

export const api_updateUser = async (userId, email,
                                     firstName,
                                     lastName,
                                     gender,
                                     phoneNumber,
                                     address,
                                     dateOfBirth) => {

    const userData = {
        email,
        firstName,
        lastName,
        gender,
        phoneNumber,
        address,
        dateOfBirth
    };
    return http.put(`/users/${userId}`, userData);
};