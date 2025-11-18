import {api_changePassword} from "../../../api_config/api_changePassword.jsx";
import "./ChangePassword.css";
import {useState} from "react";
import {jwtDecode} from "jwt-decode";
import {useNavigate} from "react-router-dom";
import {logoutUser} from "../../../utils/authLogout.jsx"

export const ChangePassword = () => {
    const [oldPassword, setOldPassword] = useState("");
    const [newPassword, setNewPassword] = useState("");
    const [confirmNewPassword, setConfirmNewPassword] = useState("");
    const navigate = useNavigate();
    const [message, setMessage] = useState("");
    const token = sessionStorage.getItem("token");
    let userId = null;
    const [loading, setLoading] = useState(false);

    if (token) {
        try {
            const decodedToken = jwtDecode(token);
            userId = decodedToken.userId;
        } catch (error) {
            setMessage("User token is invalid. Please log in again.");
        }
    }
    const handleSubmit = async (e) => {
        e.preventDefault();

        setMessage("");
        if (!userId) {
            setMessage("User not logged in or token is missing!");
            return;
        }

        if (newPassword !== confirmNewPassword) {
            setMessage("New password and confirm password do not match!");
            return;
        }

        setLoading(true);
        try {
            const result = await api_changePassword(
                userId,
                oldPassword,
                newPassword,
                confirmNewPassword
            );

            if (result?.data?.result?.changed) {
                setMessage("Password changed successfully!");
                try {
                    localStorage.removeItem("myapp-password");
                    localStorage.setItem("myapp-remember", "false");
                } catch (err) {
                    console.warn("Failed to update localStorage after password change", err);
                }
                sessionStorage.removeItem("mustChangePassword");
                logoutUser(navigate);
            } else {
                setMessage(result?.data?.message || "Password change failed!");
            }

        } catch (error) {
            const errMsg = error?.response?.data?.message || "An error occurred!";
            setMessage(errMsg);
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="change-password-page-wrapper">
            <div className="change-password-container">
                <h2>Change Password</h2>

                <form onSubmit={handleSubmit} noValidate>
                    <div className="form-group">
                        <label>Old Password:</label>
                        <input
                            type="password"
                            value={oldPassword}
                            onChange={(e) => setOldPassword(e.target.value)}
                            placeholder="Enter your current password"
                            required
                        />
                    </div>

                    <div className="form-group">
                        <label>New Password:</label>
                        <input
                            type="password"
                            value={newPassword}
                            onChange={(e) => setNewPassword(e.target.value)}
                            placeholder="Enter new password"
                            required
                        />
                    </div>

                    <div className="form-group">
                        <label>Confirm New Password:</label>
                        <input
                            type="password"
                            value={confirmNewPassword}
                            onChange={(e) => setConfirmNewPassword(e.target.value)}
                            placeholder="Confirm new password"
                            required
                        />
                    </div>

                    <button
                        type="submit"
                        className={`btn-submit ${loading ? "loading" : ""}`}
                        disabled={loading}
                    >
                        {loading ? "Changing..." : "Change Password"}
                    </button>
                </form>

                {message && (
                    <p
                        className={`message ${
                            message.toLowerCase().includes("success") ? "success" : "error"
                        }`}
                        role="status"
                    >
                        {message}
                    </p>
                )}
            </div>
        </div>
    );

};

