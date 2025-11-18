import { logout as apiLogout } from "../api_config/api_authentication.jsx";
export const logoutUser = async (navigate) => {
    try {
        const token = sessionStorage.getItem("token");
        if (token) {
            await apiLogout({ token });
        }
    } catch (error) {
        console.error("API Logout failed:", error);
    } finally {
        localStorage.removeItem("token");
        sessionStorage.removeItem("token");
        sessionStorage.removeItem("mustChangePassword");
        if (navigate) {
            navigate("/login", { replace: true });
        } else {
            window.location.href = "/login";
        }
    }
};
