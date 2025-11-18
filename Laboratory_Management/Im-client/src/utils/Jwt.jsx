import {jwtDecode} from "jwt-decode";

export function getToken() {
    return sessionStorage.getItem("token");
    console.log("token", sessionStorage.getItem("token"));
}

export function getUserInfo() {
    const token = getToken();
    if (!token) return null;

    try {
        const decodedToken = jwtDecode(token);
        return decodedToken;
    } catch (error) {
        console.error("Invalid token:", error);
        return null;
    }
}
