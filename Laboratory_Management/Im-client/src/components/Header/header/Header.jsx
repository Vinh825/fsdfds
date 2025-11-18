import "./Header.css";
import { useNavigate, useLocation } from "react-router-dom";
import { useState, useEffect, useRef } from "react";
import { logout as apiLogout } from "../../../api_config/api_authentication.jsx";
import { api_getUserById } from "../../../api_config/CRUD_User/api_getUserById.jsx";
import { getUserInfo } from "../../../utils/Jwt.jsx";
import {
    Stethoscope,
    Home,
    Users,
    Shield,
    UserPlus,
    Lock,
    LogOut,
    Mail,
    Loader2,
    Sun,
    Moon,
    ChevronDown
} from "lucide-react";
import { useTheme } from "../../../context/ThemeContext.jsx";

export default function Header() {
    const navigate = useNavigate();
    const location = useLocation();
    const menuRef = useRef(null);
    const [loggingOut, setLoggingOut] = useState(false);
    const [user, setUser] = useState(null);
    const [loadingUser, setLoadingUser] = useState(true);
    const [isMenuOpen, setIsMenuOpen] = useState(false);
    const { theme, toggleTheme } = useTheme();

    // === Fetch user info ===
    useEffect(() => {
        const fetchUserInfo = async () => {
            try {
                const userInfo = getUserInfo();
                if (userInfo && userInfo.userId) {
                    const response = await api_getUserById(userInfo.userId);
                    if (response.data && response.data.result) {
                        setUser(response.data.result);
                    }
                }
            } catch (error) {
                console.error("Failed to fetch user info:", error);
            } finally {
                setLoadingUser(false);
            }
        };

        fetchUserInfo();
    }, []);

    // === handleLogout ===
    const handleLogout = async () => {
        if (loggingOut) return;
        setLoggingOut(true);
        try {
            const token = sessionStorage.getItem("token");

            if (token) await apiLogout({ token });

            localStorage.removeItem("token");
            sessionStorage.removeItem("token");
            navigate("/login", { replace: true });
        } catch {
            localStorage.removeItem("token");
            sessionStorage.removeItem("token");
            navigate("/login", { replace: true });
        } finally {
            setLoggingOut(false);
        }
    };

    // === handleChangePassword ===
    const handleChangePassword = () => {
        navigate("/changePassword");
    };
    const handleNavigate = (path) => {
        navigate(path);
    };

    const getUserDisplayName = () => {
        if (!user) return "User";
        if (user.firstName && user.lastName) {
            return `${user.lastName} ${user.firstName}`;
        }
        return user.email || "User";
    };

    const getUserInitials = () => {
        if (!user) return "U";
        if (user.firstName && user.lastName) {
            return `${user.lastName.charAt(0)}${user.firstName.charAt(0)}`.toUpperCase();
        }
        if (user.email) {
            return user.email.charAt(0).toUpperCase();
        }
        return "U";
    };

    const navLinks = [
        { label: "Dashboard", icon: Home, path: "/dashboard" },
        { label: "Users", icon: Users, path: "/users" },
        { label: "Roles", icon: Shield, path: "/roles/list" },
        { label: "Create User", icon: UserPlus, path: "/create" }
    ];

    const isActive = (path) => location.pathname.startsWith(path);

    useEffect(() => {
        const handleClickOutside = (event) => {
            if (menuRef.current && !menuRef.current.contains(event.target)) {
                setIsMenuOpen(false);
            }
        };
        document.addEventListener("mousedown", handleClickOutside);
        return () => document.removeEventListener("mousedown", handleClickOutside);
    }, []);

    useEffect(() => {
        setIsMenuOpen(false);
    }, [location.pathname]);

    const ThemeIcon = theme === "dark" ? Sun : Moon;
    const themeLabel = theme === "dark" ? "Light Mode" : "Dark Mode";

    const handleChangePasswordAndClose = () => {
        setIsMenuOpen(false);
        handleChangePassword();
    };

    const handleLogoutAndClose = () => {
        setIsMenuOpen(false);
        handleLogout();
    };

    const handleToggleTheme = () => {
        toggleTheme();
    };

    const renderUserInfo = () => (
        <div className="user-info" ref={menuRef}>
            <button type="button" className="user-summary" onClick={() => setIsMenuOpen(prev => !prev)}>
                <div className="user-avatar">
                    {getUserInitials()}
                </div>
                <div className="user-details">
                    <div className="user-name">{getUserDisplayName()}</div>
                    <div className="user-email">
                        <Mail size={12} />
                        <span>{user?.email || "No email"}</span>
                    </div>
                </div>
                <ChevronDown size={16} className={`user-summary-chevron ${isMenuOpen ? "open" : ""}`} />
            </button>
            {isMenuOpen && (
                <div className="user-menu">
                    <button type="button" onClick={handleChangePasswordAndClose} disabled={loggingOut}>
                        <Lock size={14} />
                        <span>Change Password</span>
                    </button>
                    <button type="button" onClick={handleLogoutAndClose} disabled={loggingOut}>
                        <LogOut size={14} />
                        <span>{loggingOut ? "Exiting..." : "Logout"}</span>
                    </button>
                    <button type="button" onClick={handleToggleTheme}>
                        <ThemeIcon size={14} />
                        <span>{themeLabel}</span>
                    </button>
                </div>
            )}
        </div>
    );

    return (
        <header className="app-header">
            <div className="logo">
                <Stethoscope className="logo-icon" size={28} />
                <a>Healthcare Laboratory</a>
            </div>

            <nav className="navigation">
                {navLinks.map((link) => (
                    <button
                        key={link.path}
                        type="button"
                        className={`nav-link ${isActive(link.path) ? "active" : ""}`}
                        onClick={() => handleNavigate(link.path)}
                    >
                        <link.icon size={18} />
                        <span>{link.label}</span>
                    </button>
                ))}
            </nav>

            <div className="header-actions">
                {loadingUser ? (
                    <div className="user-info-loading">
                        <Loader2 size={18} className="spinner" />
                    </div>
                ) : (
                    renderUserInfo()
                )}
            </div>
        </header>
    );
}
