import {useEffect, useState} from "react";
import {useNavigate} from "react-router-dom";
import {User, Lock, Eye, EyeOff} from "lucide-react";
import {login} from "../../api_config/api_authentication.jsx";
import CryptoJS from "crypto-js";
import "./login.css";

export default function Login() {
    const [showPassword, setShowPassword] = useState(false);
    const [error, setError] = useState("");
    const [loading, setLoading] = useState(false);
    const navigate = useNavigate();
    const SECRET_KEY = "lm-frontend-remember-key";

    const initialRemember = localStorage.getItem("myapp-remember") === "true";

    const [remember, setRemember] = useState(initialRemember);

    const getDecryptedPassword = () => {
        const encrypted = localStorage.getItem("myapp-password");
        if (!encrypted) return "";
        try {
            const bytes = CryptoJS.AES.decrypt(encrypted, SECRET_KEY);
            return bytes.toString(CryptoJS.enc.Utf8);
        } catch (err) {
            console.error("Failed to decrypt password:", err);
            return "";
        }
    };

    const [username, setUsername] = useState(localStorage.getItem("myapp-username") || "");
    const [password, setPassword] = useState(getDecryptedPassword());

    useEffect(() => {
        const token = sessionStorage.getItem("token");
        if (token) navigate("/dashboard", {replace: true});
    }, [navigate]);

    const handleSubmit = async (e) => {
        e.preventDefault();
        setLoading(true);
        setError("");

        try {
            const response = await login({username, password});
            const token = response?.token;

            if (token) {
                sessionStorage.setItem("token", token);
                const mustChangePassword = response?.mustChangePassword === true;

                if (mustChangePassword) {
                    sessionStorage.setItem("mustChangePassword", "true");
                } else {
                    sessionStorage.removeItem("mustChangePassword");
                }

                if (remember) {
                    localStorage.setItem("myapp-username", username);
                    localStorage.setItem("myapp-remember", "true");
                    const encrypted = CryptoJS.AES.encrypt(
                        password,
                        SECRET_KEY
                    ).toString();
                    localStorage.setItem("myapp-password", encrypted);
                } else {
                    localStorage.removeItem("myapp-username");
                    localStorage.removeItem("myapp-password");
                    localStorage.removeItem("myapp-remember");
                }

                navigate(mustChangePassword ? "/changePassword" : "/dashboard", {replace: true});
            } else {
                setError("Login failed: No token received.");
            }
        } catch (err) {
            setError(
                err?.response?.data?.message || "Login failed. Please try again."
            );
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="lp-page">
            <div className="lp-blob lp-blob-a"/>
            <div className="lp-blob lp-blob-b"/>

            <div className="lp-wrap">
                <div className="lp-card">
                    {/* LEFT */}
                    <div className="lp-left">
                        <div className="lp-head">
                            <div className="lp-badge">LM</div>
                            <div>
                                <div className="lp-kicker">Welcome back</div>
                                <h2 className="lp-title">
                                    Sign in to <span>Healthcare Laboratory</span>
                                </h2>
                            </div>
                        </div>

                        <form className="lp-form" onSubmit={handleSubmit} noValidate>
                            {/* Email */}
                            <div className="lp-field">
                                <label className="lp-label">Email</label>
                                <div className="lp-inputwrap">
                                    <User className="lp-icon"/>
                                    <input
                                        className="lp-input"
                                        type="text"
                                        value={username}
                                        onChange={(e) => setUsername(e.target.value)}
                                        placeholder="Enter your username..."
                                        required
                                    />
                                </div>
                            </div>

                            {/* Password */}
                            <div className="lp-field">
                                <label className="lp-label">Password</label>
                                <div className="lp-inputwrap">
                                    <Lock className="lp-icon"/>
                                    <input
                                        className="lp-input"
                                        type={showPassword ? "text" : "password"}
                                        value={password}
                                        onChange={(e) => setPassword(e.target.value)}
                                        placeholder="Enter your password..."
                                        required
                                    />
                                    <button
                                        type="button"
                                        className="lp-eye"
                                        onClick={() => setShowPassword((s) => !s)}
                                        aria-label={
                                            showPassword ? "Hide password" : "Show password"
                                        }
                                    >
                                        {showPassword ? <EyeOff size={18}/> : <Eye size={18}/>}
                                    </button>
                                </div>
                            </div>

                            {/* Remember & Forgot */}
                            <div className="lp-row">
                                <label className="lp-remember">
                                    <input
                                        type="checkbox"
                                        checked={remember}
                                        onChange={(e) => setRemember(e.target.checked)}
                                    />
                                    <span>Remember me</span>
                                </label>
                                <button
                                    type="button"
                                    className="lp-link"
                                    onClick={() => navigate("/forget")}
                                >
                                    Forgot password?
                                </button>
                            </div>

                            {error && <p className="lp-error">{error}</p>}

                            <button type="submit" disabled={loading} className="lp-btn">
                                {loading ? "Signing in..." : "LOGIN"}
                            </button>
                        </form>
                    </div>

                    {/* RIGHT */}
                    <div className="lp-right">
                        <div className="lp-right-overlay"/>
                        <div className="lp-right-blur lp-right-blur-a"/>
                        <div className="lp-right-blur lp-right-blur-b"/>
                        <div className="lp-right-content">
                            <h1>Healthcare Laboratory</h1>
                            <p>
                                Is a centralized software solution supporting the complete blood
                                testing workflow, designed to support business goals such as
                                reducing patient wait times, improving lab throughput, and
                                maximizing the utilization of high-value hematology analyzers.
                            </p>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
}

