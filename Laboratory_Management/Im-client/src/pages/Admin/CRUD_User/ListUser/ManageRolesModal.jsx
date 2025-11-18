import React, { useState } from 'react';
import './ManageRolesModal.css';

export default function ManageRolesModal({
    user,
    availableRoles = [],
    onSave,
    onClose,
    isLoading
}) {
    const [selectedRoles, setSelectedRoles] = useState(
        Array.isArray(user?.roleCodes) ? user.roleCodes : []
    );

    const toggleRole = (roleCode) => {
        setSelectedRoles(prev => {
            if (prev.includes(roleCode)) {
                return prev.filter(code => code !== roleCode);
            }
            return [...prev, roleCode];
        });
    };

    const handleSubmit = (event) => {
        event.preventDefault();
        onSave(selectedRoles);
    };

    return (
        <div
            className="modal-overlay"
            onMouseDown={(event) => {
                if (event.target === event.currentTarget && !isLoading) {
                    onClose();
                }
            }}
        >
            <div className="modal-content roles-modal">
                <h2>Manage Roles</h2>
                <p className="roles-subtitle">
                    Assign roles for <strong>{user?.lastName} {user?.firstName}</strong>
                </p>
                <form onSubmit={handleSubmit}>
                    <div className="roles-list">
                        {availableRoles.length === 0 && (
                            <div className="roles-empty">No roles available.</div>
                        )}
                        {availableRoles.map((role) => (
                            <label key={role.roleCode} className="role-option">
                                <input
                                    type="checkbox"
                                    checked={selectedRoles.includes(role.roleCode)}
                                    onChange={() => toggleRole(role.roleCode)}
                                    disabled={isLoading}
                                />
                                <span>
                                    <strong>{role.name || role.roleCode}</strong>
                                    {role.description && (
                                        <small>{role.description}</small>
                                    )}
                                </span>
                            </label>
                        ))}
                    </div>

                    <div className="modal-actions">
                        <button
                            type="button"
                            className="btn-cancel"
                            onClick={onClose}
                            disabled={isLoading}
                        >
                            Cancel
                        </button>
                        <button
                            type="submit"
                            className="btn-save"
                            disabled={isLoading}
                        >
                            {isLoading ? 'Saving...' : 'Save'}
                        </button>
                    </div>
                </form>
            </div>
        </div>
    );
}

