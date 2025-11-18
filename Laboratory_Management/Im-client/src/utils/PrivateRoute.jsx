import { Navigate } from "react-router-dom";
import { jwtDecode } from "jwt-decode";

export default function PrivateRoute({ children }) {
  const token = sessionStorage.getItem("token");

  if (!token) {
    return <Navigate to="/login" replace />;
  }

  let mustChange = sessionStorage.getItem("mustChangePassword") === "true";
  if (!mustChange) {
    try {
      const payload = jwtDecode(token);
      mustChange = payload?.mustChangePassword === true;
    } catch (error) {
      console.warn("Failed to decode JWT when checking password status", error);
    }
  }

  if (mustChange) {
    return <Navigate to="/changePassword" replace />;
  }

  return children;
}