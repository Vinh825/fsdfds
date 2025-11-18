import "./Forget.css";
import { useState } from "react";
import {
  sendEmail,
  verifyOtp,
  resetPassword,
} from "../../api_config/api_forget.jsx";
import { useNavigate } from "react-router-dom";

export default function Forget() {
  const [step, setStep] = useState(1);
  const [loading, setLoading] = useState(false);
  const [email, setEmail] = useState("");
  const [otp, setOtp] = useState("");
  const [newPassword, setNewPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");
  const [msg, setMsg] = useState("");
  const [err, setErr] = useState("");
  const [username, setUsername] = useState("");
  const navigate = useNavigate();

  const handleSendEmail = async (e) => {
    e.preventDefault();
    setErr("");
    setMsg("");

    if (!email.trim()) {
      setErr("Please enter email.");
      return;
    }
    if (!username.trim()) return setErr("Please enter username.");
    setLoading(true);
    try {
      const res = await sendEmail(email.trim(), username.trim());
      setMsg(res?.data?.message || "OTP has been sent to your email!");
      setStep(2);
    } catch (error) {
      setErr(error?.response?.data?.message || "Failed to send email.");
    } finally {
      setLoading(false);
    }
  };

  const handleVerifyOtp = async (e) => {
    e.preventDefault();
    setErr("");
    setMsg("");
    if (!otp.trim()) {
      setErr("Please enter OTP.");
      return;
    }
    setLoading(true);
    try {
      const res = await verifyOtp({ email: email.trim(), code: otp.trim() });
      setMsg(res?.data?.message || "OTP is valid.");
      setStep(3);
    } catch (error) {
      setErr(
        error?.response?.data?.message || "OTP is invalid or has expired."
      );
    } finally {
      setLoading(false);
    }
  };

  const handleResetPassword = async (e) => {
    e.preventDefault();
    setErr("");
    setMsg("");

    if (newPassword.length < 8) {
      setErr("Password must be at least 8 characters.");
      return;
    }
    if (newPassword !== confirmPassword) {
      setErr("Confirm password does not match.");
      return;
    }

    setLoading(true);
    try {
      const res = await resetPassword({
        email: email.trim(),
        code: otp.trim(),
        newPassword,
        confirmNewPassword: confirmPassword,
      });
      setMsg(res?.data?.message || "Password updated successfully.");
      setTimeout(() => navigate("/login"), 1200);
    } catch (error) {
      setErr(error?.response?.data?.message || "Password reset failed.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="forget-wrap">
      <div className="forget-bg-pattern"></div>

      <div className="forget-card">
        <div className="forget-header">
          <div className="forget-icon">
            <svg width="40" height="40" viewBox="0 0 24 24" fill="none">
              <path
                d="M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm1 15h-2v-2h2v2zm0-4h-2V7h2v6z"
                fill="currentColor"
              />
            </svg>
          </div>
          <h2 className="forget-title">Forgot password</h2>
          <p className="forget-subtitle">Recover your account</p>
        </div>

        <div className="forget-progress">
          <div className={`forget-progress-step ${step >= 1 ? "active" : ""}`}>
            <div className="forget-progress-circle">1</div>
            <span>Email & Username</span>
          </div>
          <div
            className={`forget-progress-line ${step >= 2 ? "active" : ""}`}
          ></div>
          <div className={`forget-progress-step ${step >= 2 ? "active" : ""}`}>
            <div className="forget-progress-circle">2</div>
            <span>OTP</span>
          </div>
          <div
            className={`forget-progress-line ${step >= 3 ? "active" : ""}`}
          ></div>
          <div className={`forget-progress-step ${step >= 3 ? "active" : ""}`}>
            <div className="forget-progress-circle">3</div>
            <span>Password</span>
          </div>
        </div>

        {msg && <div className="forget-alert forget-alert-success">{msg}</div>}
        {err && <div className="forget-alert forget-alert-danger">{err}</div>}

        {step === 1 && (
          <form onSubmit={handleSendEmail} className="forget-form">
            <div className="forget-input-group">
              <label className="forget-label">Email Address</label>
              <input
                className="forget-input"
                type="email"
                placeholder="you@example.com"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                required
              />
            </div>

            <div className="forget-input-group">
              <label className="forget-label">Username</label>
              <input
                className="forget-input"
                type="text"
                placeholder="Enter your username"
                value={username}
                onChange={(e) => setUsername(e.target.value)}
                required
              />
            </div>

            <button
              type="submit"
              className="forget-btn forget-btn-primary"
              disabled={loading || !email.trim() || !username.trim()}
              aria-disabled={loading || !email.trim() || !username.trim()}
            >
              {loading ? (
                <>
                  <span className="forget-spinner"></span>
                  Sending...
                </>
              ) : (
                "Send OTP"
              )}
            </button>
          </form>
        )}

        {step === 2 && (
          <form onSubmit={handleVerifyOtp} className="forget-form">
            <div className="forget-note">
              <svg width="16" height="16" viewBox="0 0 24 24" fill="none">
                <path
                  d="M20 4H4c-1.1 0-2 .9-2 2v12c0 1.1.9 2 2 2h16c1.1 0 2-.9 2-2V6c0-1.1-.9-2-2-2zm0 4l-8 5-8-5V6l8 5 8-5v2z"
                  fill="currentColor"
                />
              </svg>
              OTP has been sent to: <strong>{email}</strong>
            </div>

            <div className="forget-input-group">
              <label className="forget-label">OTP</label>
              <input
                className="forget-input forget-input-otp"
                type="text"
                inputMode="numeric"
                pattern="\d{6}"
                placeholder="Enter 6 digits"
                value={otp}
                onChange={(e) =>
                  setOtp(e.target.value.replace(/\D/g, "").slice(0, 6))
                }
                maxLength={6}
                required
              />
              {otp && !/^\d{6}$/.test(otp) && (
                <small className="forget-hint">OTP must be 6 digits.</small>
              )}
            </div>

            <div className="forget-btn-group">
              <button
                type="button"
                className="forget-btn forget-btn-secondary"
                onClick={() => setStep(1)}
              >
                ← Change email
              </button>

              <button
                type="submit"
                className="forget-btn forget-btn-primary"
                disabled={loading || !/^\d{6}$/.test(otp)}
                aria-disabled={loading || !/^\d{6}$/.test(otp)}
              >
                {loading ? (
                  <>
                    <span className="forget-spinner"></span>
                    Validating...
                  </>
                ) : (
                  "Verify OTP"
                )}
              </button>
            </div>
          </form>
        )}

        {step === 3 && (
          <form onSubmit={handleResetPassword} className="forget-form">
            <div className="forget-note">
              <svg width="16" height="16" viewBox="0 0 24 24" fill="none">
                <path
                  d="M18 8h-1V6c0-2.76-2.24-5-5-5S7 3.24 7 6v2H6c-1.1 0-2 .9-2 2v10c0 1.1.9 2 2 2h12c1.1 0 2-.9 2-2V10c0-1.1-.9-2-2-2zm-6 9c-1.1 0-2-.9-2-2s.9-2 2-2 2 .9 2 2-.9 2-2 2zM9 8V6c0-1.66 1.34-3 3-3s3 1.34 3 3v2H9z"
                  fill="currentColor"
                />
              </svg>
              Set new password for: <strong>{email}</strong>
            </div>

            <div className="forget-input-group">
              <label className="forget-label">New Password</label>
              <input
                className="forget-input"
                type="password"
                placeholder="At least 8 characters"
                value={newPassword}
                onChange={(e) => setNewPassword(e.target.value)}
                minLength={8}
                required
              />
            </div>

            <div className="forget-input-group">
              <label className="forget-label">Confirm Password</label>
              <input
                className="forget-input"
                type="password"
                placeholder="Re-enter new password"
                value={confirmPassword}
                onChange={(e) => setConfirmPassword(e.target.value)}
                minLength={8}
                required
              />
              {confirmPassword && newPassword !== confirmPassword && (
                <small className="forget-hint">
                  Confirm password does not match.
                </small>
              )}
            </div>

            <div className="forget-btn-group">
              <button
                type="button"
                className="forget-btn forget-btn-secondary"
                onClick={() => setStep(2)}
              >
                ← Enter OTP
              </button>

              <button
                type="submit"
                className="forget-btn forget-btn-primary"
                disabled={
                  loading ||
                  newPassword.length < 8 ||
                  confirmPassword.length < 8 ||
                  newPassword !== confirmPassword
                }
                aria-disabled={
                  loading ||
                  newPassword.length < 8 ||
                  confirmPassword.length < 8 ||
                  newPassword !== confirmPassword
                }
              >
                {loading ? (
                  <>
                    <span className="forget-spinner"></span>
                    Validating...
                  </>
                ) : (
                  "Update Password"
                )}
              </button>
            </div>
          </form>
        )}
      </div>
    </div>
  );
}
