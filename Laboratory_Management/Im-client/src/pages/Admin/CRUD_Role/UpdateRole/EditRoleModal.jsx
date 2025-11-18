import React, { useState, useEffect } from 'react';
import { api_getPrivileges } from '../../../../api_config/CRUD_Role/api_getPrivileges.jsx';
import './EditRoleModal.css';

export default function EditRoleModal({ role, onClose, onSave, isLoading }) {
    const [formData, setFormData] = useState({
        name: role?.name || '',
        description: role?.description || ''
    });
    const [privilegesList, setPrivilegesList] = useState([]);
    const [selectedPrivileges, setSelectedPrivileges] = useState([]);
    const [loadingPrivileges, setLoadingPrivileges] = useState(true);

    useEffect(() => {
        const fetchPrivileges = async () => {
            try {
                const response = await api_getPrivileges();
                if (response.data && response.data.result) {
                    setPrivilegesList(response.data.result);
                }
            } catch (error) {
                console.error('Failed to load privileges', error);
            } finally {
                setLoadingPrivileges(false);
            }
        };
        fetchPrivileges();
    }, []);

    useEffect(() => {
        if (role) {
            const permitted = (role.privileges || [])
                .filter(p => p.permitted)
                .map(p => p.privilege);
            setFormData({
                name: role.name || '',
                description: role.description || '',
            });
            if (permitted.length > 0) {
                setSelectedPrivileges(permitted);
            } else {
                setSelectedPrivileges(['READ_ONLY']);
            }
        }
    }, [role]);

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData(prev => ({
            ...prev,
            [name]: value
        }));
    };

    const handlePrivilegeToggle = (privilege) => {
        setSelectedPrivileges(prev => {
            if (prev.includes(privilege)) {
                if (prev.length === 1) return prev;
                return prev.filter(item => item !== privilege);
            }
            return [...prev, privilege];
        });
    };

    const handleSelectAllPrivileges = () => {
        setSelectedPrivileges(privilegesList);
    };

    const handleClearPrivileges = () => {
        setSelectedPrivileges([]);
    };

    const handleSubmit = (e) => {
        e.preventDefault();
        if (selectedPrivileges.length === 0) {
            return;
        }
        onSave({
            ...formData,
            roleCode: role.roleCode,
            privileges: selectedPrivileges.map(privilege => ({ privilege, permitted: true }))
        });
    };

    return (
        <div
            className="modal-overlay"
            onMouseDown={(e) => {
                if (e.target === e.currentTarget) {
                    onClose();
                }
            }}
        >
            <div className="modal-content">
                <form onSubmit={handleSubmit}>
                    <h2>Edit Role</h2>

                    <div className="form-group">
                        <label>Role Code</label>
                        <input
                            type="text"
                            value={role?.roleCode || ''}
                            disabled
                            style={{ opacity: 0.6, cursor: 'not-allowed' }}
                        />
                    </div>

                    <div className="form-group">
                        <label>Name</label>
                        <input
                            type="text"
                            name="name"
                            value={formData.name}
                            onChange={handleChange}
                            required
                        />
                    </div>

                    <div className="form-group">
                        <label>Description</label>
                        <textarea
                            name="description"
                            value={formData.description}
                            onChange={handleChange}
                            rows="3"
                            style={{
                                width: '100%',
                                padding: '10px 14px',
                                border: '1px solid var(--line)',
                                borderRadius: '6px',
                                fontSize: '14px',
                                background: 'rgba(255, 255, 255, 0.06)',
                                color: 'var(--text)',
                                boxSizing: 'border-box',
                                fontFamily: 'inherit',
                                resize: 'vertical'
                            }}
                        />
                    </div>

                    <div className="form-group">
                        <label>Privileges</label>
                        {loadingPrivileges ? (
                            <div style={{ padding: '12px', color: 'var(--muted)' }}>
                                Loading privileges...
                            </div>
                        ) : (
                            <>
                                <div className="privileges-actions">
                                    <button
                                        type="button"
                                        className="privilege-action-btn"
                                        onClick={handleSelectAllPrivileges}
                                        disabled={privilegesList.length === 0}
                                    >
                                        Select All
                                    </button>
                                    <button
                                        type="button"
                                        className="privilege-action-btn ghost"
                                        onClick={handleClearPrivileges}
                                        disabled={selectedPrivileges.length === 0}
                                    >
                                        Clear
                                    </button>
                                </div>
                                <div className="privilege-summary">
                                    {selectedPrivileges.length} privilege{selectedPrivileges.length === 1 ? '' : 's'} selected
                                </div>
                                <div className="privileges-container-modal">
                                    {privilegesList.map(privilege => (
                                        <label key={privilege} className={`privilege-option-modal ${selectedPrivileges.includes(privilege) ? 'selected' : ''}`}>
                                            <input
                                                type="checkbox"
                                                value={privilege}
                                                checked={selectedPrivileges.includes(privilege)}
                                                onChange={() => handlePrivilegeToggle(privilege)}
                                            />
                                            <span className="privilege-label-modal">{privilege}</span>
                                        </label>
                                    ))}
                                </div>
                            </>
                        )}
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
                            disabled={isLoading || loadingPrivileges}
                        >
                            {isLoading ? "Saving..." : "Save Changes"}
                        </button>
                    </div>
                </form>
            </div>
        </div>
    );
}

