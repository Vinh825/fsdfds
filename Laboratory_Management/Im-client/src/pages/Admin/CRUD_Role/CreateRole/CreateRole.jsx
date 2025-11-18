import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { api_createRole } from '../../../../api_config/CRUD_Role/api_createRole.jsx';
import { api_getPrivileges } from '../../../../api_config/CRUD_Role/api_getPrivileges.jsx';
import {
    ShieldPlus,
    Shield,
    FileText,
    Key,
    Loader2,
    CheckSquare
} from 'lucide-react';
import './CreateRole.css';

export default function CreateRole() {
    const navigate = useNavigate();

    const [formData, setFormData] = useState({
        name: '',
        roleCode: '',
        description: ''
    });

    const [privilegesList, setPrivilegesList] = useState([]);
    const [selectedPrivileges, setSelectedPrivileges] = useState([]);
    const [loading, setLoading] = useState(false);
    const [loadingPrivileges, setLoadingPrivileges] = useState(true);
    const [message, setMessage] = useState(null);

    useEffect(() => {
        const fetchPrivileges = async () => {
            try {
                const response = await api_getPrivileges();
                if (response.data && response.data.result) {
                    const fetched = response.data.result;
                    setPrivilegesList(fetched);
                    if (fetched.length > 0) {
                        setSelectedPrivileges([fetched[0]]);
                    }
                }
            } catch (error) {
                setMessage({ type: 'error', text: 'Failed to load privileges' });
            } finally {
                setLoadingPrivileges(false);
            }
        };
        fetchPrivileges();
    }, []);

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

    const handleSubmit = async (e) => {
        e.preventDefault();
        setLoading(true);
        setMessage(null);

        if (!formData.name || !formData.roleCode) {
            setMessage({ type: 'error', text: 'Name and Role Code are required' });
            setLoading(false);
            return;
        }

        if (selectedPrivileges.length === 0) {
            setMessage({ type: 'error', text: 'Please select at least one privilege' });
            setLoading(false);
            return;
        }

        try {
            const response = await api_createRole(
                formData.name,
                formData.roleCode,
                formData.description,
                selectedPrivileges.map(privilege => ({ privilege, permitted: true }))
            );

            setMessage({
                type: 'success',
                text: `Role "${formData.roleCode}" created successfully!`
            });

            setTimeout(() => {
                navigate('/roles/list');
            }, 1500);

        } catch (error) {
            const errorText = error.response?.data?.message || error.message || "An unknown error has occurred.";
            setMessage({ type: 'error', text: errorText });
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="create-role-page-wrapper">
            <div className="create-role-container">
                <div className="create-role-header">
                    <div className="create-role-icon-wrapper">
                        <ShieldPlus className="create-role-icon" size={32} />
                    </div>
                    <h2>Create New Role</h2>
                </div>
                {message && (
                    <p
                        className={`message ${message.type === 'success' ? 'success' : 'error'}`}
                        role="status"
                    >
                        {message.text}
                    </p>
                )}

                <form onSubmit={handleSubmit} noValidate>
                    <div className="form-group">
                        <label>
                            <Key size={16} />
                            Role Code
                        </label>
                        <input
                            type="text"
                            name="roleCode"
                            value={formData.roleCode}
                            onChange={handleChange}
                            placeholder="ROLE_EXAMPLE"
                            required
                            style={{ textTransform: 'uppercase' }}
                        />
                        <small className="form-hint">3-50 characters, uppercase letters, numbers, underscores, and hyphens only</small>
                    </div>

                    <div className="form-group">
                        <label>
                            <Shield size={16} />
                            Name
                        </label>
                        <input
                            type="text"
                            name="name"
                            value={formData.name}
                            onChange={handleChange}
                            placeholder="Example Role"
                            required
                        />
                    </div>

                    <div className="form-group">
                        <label>
                            <FileText size={16} />
                            Description
                        </label>
                        <textarea
                            name="description"
                            value={formData.description}
                            onChange={handleChange}
                            placeholder="Role description..."
                            rows="3"
                        />
                    </div>

                    <div className="form-group">
                        <label>
                            <CheckSquare size={16} />
                            Privileges
                        </label>
                        {loadingPrivileges ? (
                            <div className="privileges-loading">
                                <Loader2 size={16} className="btn-icon-spin" />
                                <span>Loading privileges...</span>
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
                                <div className="privileges-container">
                                    {privilegesList.map(privilege => (
                                        <label key={privilege} className={`privilege-option ${selectedPrivileges.includes(privilege) ? 'selected' : ''}`}>
                                            <input
                                                type="checkbox"
                                                value={privilege}
                                                checked={selectedPrivileges.includes(privilege)}
                                                onChange={() => handlePrivilegeToggle(privilege)}
                                            />
                                            <span className="privilege-label">{privilege}</span>
                                        </label>
                                    ))}
                                </div>
                            </>
                        )}
                    </div>

                    <button
                        type="submit"
                        className={`btn-submit ${loading ? "loading" : ""}`}
                        disabled={loading || loadingPrivileges}
                    >
                        {loading ? (
                            <>
                                <Loader2 className="btn-loader" size={18} />
                                <span>Creating...</span>
                            </>
                        ) : (
                            <>
                                <ShieldPlus size={18} />
                                <span>Create Role</span>
                            </>
                        )}
                    </button>
                </form>
            </div>
        </div>
    );
}

