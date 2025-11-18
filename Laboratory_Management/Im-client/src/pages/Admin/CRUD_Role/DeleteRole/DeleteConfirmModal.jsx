import React, { useEffect, useRef } from "react";
import "./DeleteConfirmModal.css";

export default function DeleteConfirmModal({
    role = {},
    onClose,
    onConfirm,
    isLoading = false,
}) {
    const cancelBtnRef = useRef(null);
    const roleName = role.name || role.roleCode || "this role";

    useEffect(() => {
        cancelBtnRef.current?.focus();
        const handleKey = (e) => {
            if (e.key === "Escape") onClose?.();
            if (e.key === "Enter" && document.activeElement?.dataset?.confirmBtn)
                onConfirm?.();
        };
        document.addEventListener("keydown", handleKey);
        const originalOverflow = document.body.style.overflow;
        document.body.style.overflow = "hidden";
        return () => {
            document.removeEventListener("keydown", handleKey);
            document.body.style.overflow = originalOverflow;
        };
    }, [onClose, onConfirm]);

    return (
        <div
            className="modal-overlay"
            onMouseDown={(e) =>
                e.target === e.currentTarget && onClose && onClose()
            }
        >
            <div
                className="delete-modal-content"
                role="dialog"
                aria-modal="true"
                aria-labelledby="delete-title"
                aria-describedby="delete-desc"
            >
                <div className="delete-icon-wrap">
                    <div className="delete-icon">
                        <svg viewBox="0 0 24 24" aria-hidden="true">
                            <path
                                fill="currentColor"
                                d="M11 15h2v2h-2v-2zm0-8h2v6h-2V7zm1-5C6.48 2 2 6.48 2
                12s4.48 10 10 10 10-4.48 10-10S17.52 2 12
                2zm0 18c-4.41 0-8-3.59-8-8s3.59-8
                8-8 8 3.59 8 8-3.59 8-8 8z"
                            />
                        </svg>
                    </div>
                </div>

                <h2 id="delete-title" className="delete-title">
                    Delete role <strong className="delete-username">
                        {"  "}
                        {roleName}
                    </strong> ?
                </h2>

                <p id="delete-desc" className="delete-message">
                    Are you sure you want to permanently delete
                    <strong className="delete-username">
                        {" "}
                        {roleName}
                    </strong>
                    ?
                </p>

                <p className="delete-warning">This action cannot be undone.</p>

                <div className="modal-actions">
                    <button
                        type="button"
                        ref={cancelBtnRef}
                        className="btn-cancel"
                        onClick={onClose}
                        disabled={isLoading}
                    >
                        Cancel
                    </button>
                    <button
                        type="button"
                        className="btn-confirm-delete"
                        onClick={onConfirm}
                        disabled={isLoading}
                        data-confirm-btn="true"
                    >
                        {isLoading ? <span className="spinner" /> : "Delete"}
                    </button>
                </div>
            </div>
        </div>
    );
}

