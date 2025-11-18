import React, { useState, useEffect, useRef } from 'react';
import { useNavigate } from 'react-router-dom';
import { api_listRole } from '../../../../api_config/CRUD_Role/api_listRole.jsx';
import { api_updateRole } from '../../../../api_config/CRUD_Role/api_updateRole.jsx';
import { api_deleteRole } from '../../../../api_config/CRUD_Role/api_deleteRole.jsx';
import { api_getPrivileges } from '../../../../api_config/CRUD_Role/api_getPrivileges.jsx';
import {
    Shield,
    Key,
    FileText,
    CheckSquare,
    Edit,
    Trash2,
    Loader2,
    ShieldPlus
} from 'lucide-react';
import './ListRole.css';
import EditRoleModal from '../UpdateRole/EditRoleModal.jsx';
import DeleteConfirmModal from '../DeleteRole/DeleteConfirmModal.jsx';
import Pagination from '../../../../components/common/Pagination.jsx';
import useDebounce from '../../../../hooks/useDebounce.js';

export default function RoleListPage() {
    const navigate = useNavigate();
    const itemsPerPage = 3;

    const [roles, setRoles] = useState([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);
    const [currentPage, setCurrentPage] = useState(1);
    const [pagination, setPagination] = useState({ page: 0, size: itemsPerPage, total: 0 });

    const [isModalOpen, setIsModalOpen] = useState(false);
    const [currentRoleToEdit, setCurrentRoleToEdit] = useState(null);
    const [isDeleteModalOpen, setIsDeleteModalOpen] = useState(false);
    const [roleToDelete, setRoleToDelete] = useState(null);
    const [loadingAction, setLoadingAction] = useState(false);
    const [searchTerm, setSearchTerm] = useState('');
    const [privilegeFilter, setPrivilegeFilter] = useState('');
    const [sortOrder, setSortOrder] = useState('name,asc');
    const [privilegeOptions, setPrivilegeOptions] = useState([]);
    const debouncedSearchTerm = useDebounce(searchTerm, 400);
    const debouncedPrivilegeFilter = useDebounce(privilegeFilter, 200);
    const debouncedSortOrder = useDebounce(sortOrder, 200);
    const filtersEffectHasRun = useRef(false);

    useEffect(() => {
        fetchRoles(1);
        fetchPrivileges();
    }, []);

    useEffect(() => {
        if (!filtersEffectHasRun.current) {
            filtersEffectHasRun.current = true;
            return;
        }
        setCurrentPage(1);
        fetchRoles(1, pagination.size || itemsPerPage);
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [debouncedSearchTerm, debouncedPrivilegeFilter, debouncedSortOrder]);

    const pageSize = pagination.size || itemsPerPage;

    const buildQueryParams = (page, size) => {
        const params = {
            page: page - 1,
            size,
            sort: sortOrder
        };
        const trimmedSearch = searchTerm.trim();
        if (trimmedSearch) {
            params.q = trimmedSearch;
        }
        if (privilegeFilter) {
            params.privilege = privilegeFilter;
        }
        return params;
    };

    const fetchRoles = async (page = 1, size = pageSize) => {
        setLoading(true);
        setError(null);
        try {
            const response = await api_listRole(buildQueryParams(page, size));
            const data = response.data?.result;
            if (!data) {
                setError("Invalid data structure returned from API.");
                return;
            }
            setRoles(Array.isArray(data.items) ? data.items : []);
            setPagination({
                page: data.page ?? 0,
                size: data.size ?? size,
                total: data.total ?? 0
            });
            setCurrentPage((data.page ?? 0) + 1);
        } catch (err) {
            setError(err.message || "An error occurred while fetching the list.");
        } finally {
            setLoading(false);
        }
    };

    const fetchPrivileges = async () => {
        try {
            const response = await api_getPrivileges();
            const data = response.data?.result;
            setPrivilegeOptions(Array.isArray(data) ? data : []);
        } catch (err) {
            // silent
        }
    };

    const confirmDelete = async () => {
        if (!roleToDelete) return;

        setLoadingAction(true);
        try {
            await api_deleteRole(roleToDelete.roleCode);
            const totalAfterDelete = Math.max((pagination.total ?? roles.length) - 1, 0);
            const totalPagesAfterDelete = pageSize ? Math.ceil(totalAfterDelete / pageSize) : 0;
            const targetPage = totalPagesAfterDelete === 0 ? 1 : Math.min(currentPage, totalPagesAfterDelete);
            await fetchRoles(Math.max(targetPage, 1), pageSize);
            closeDeleteModal();
        } catch (err) {
            setError(err.response?.data?.message || err.message || "Error deleting role.");
        } finally {
            setLoadingAction(false);
        }
    };

    const openEditModal = (role) => {
        setCurrentRoleToEdit(role);
        setIsModalOpen(true);
    };

    const closeEditModal = () => {
        setIsModalOpen(false);
        setCurrentRoleToEdit(null);
    };

    const openDeleteModal = (role) => {
        setRoleToDelete(role);
        setIsDeleteModalOpen(true);
    };

    const closeDeleteModal = () => {
        setRoleToDelete(null);
        setIsDeleteModalOpen(false);
    };

    const handleSaveUpdate = async (updatedRoleData) => {
        setLoadingAction(true);
        try {
            const response = await api_updateRole(
                updatedRoleData.roleCode,
                updatedRoleData.name,
                updatedRoleData.description,
                updatedRoleData.privileges
            );

            if (response.data && response.data.result) {
                await fetchRoles(currentPage, pageSize);
                closeEditModal();
            } else {
                setError("Error updating: Invalid data returned.");
            }
        } catch (err) {
            setError(err.response?.data?.message || err.message || "Error updating role.");
        } finally {
            setLoadingAction(false);
        }
    };

    if (loading && roles.length === 0) {
        return (
            <div style={{ padding: '20px', textAlign: 'center', color: 'var(--green)' }}>
                <Loader2 size={32} className="btn-icon-spin" style={{ margin: '20px auto' }} />
                <p>Loading roles...</p>
            </div>
        );
    }

    if (error && roles.length === 0) {
        return (
            <div style={{ padding: '20px', color: 'red' }}>
                <p>Error: {error}</p>
                <button onClick={() => fetchRoles(currentPage)}>Retry</button>
            </div>
        );
    }

    const totalPages = pageSize ? Math.ceil((pagination.total ?? 0) / pageSize) : 0;

    const handlePageChange = (page) => {
        if (page < 1 || page > totalPages) return;
        setCurrentPage(page);
        fetchRoles(page, pageSize);
        window.scrollTo({ top: 0, behavior: 'smooth' });
    };

    const handleResetFilters = () => {
        setSearchTerm('');
        setPrivilegeFilter('');
        setSortOrder('name,asc');
        fetchRoles(1, itemsPerPage);
    };

    return (
        <div style={{ padding: '20px' }}>
            <div className="list-role-header">
                <div className="list-role-header-content">
                    <div className="list-role-header-icon">
                        <Shield size={32} />
                    </div>
                    <h2>Role List</h2>
                </div>
                <button
                    className="btn-create-header"
                    onClick={() => navigate('/roles/create')}
                >
                    <ShieldPlus size={18} />
                    <span>Create Role</span>
                </button>
            </div>
            <div className="list-filters">
                <div className="filter-control">
                    <label htmlFor="role-search">Search</label>
                    <input
                        id="role-search"
                        type="text"
                        value={searchTerm}
                        onChange={(e) => setSearchTerm(e.target.value)}
                        placeholder="Role name, code..."
                    />
                </div>
                <div className="filter-control">
                    <label htmlFor="privilege-filter">Privilege</label>
                    <select
                        id="privilege-filter"
                        value={privilegeFilter}
                        onChange={(e) => setPrivilegeFilter(e.target.value)}
                    >
                        <option value="">All privileges</option>
                        {privilegeOptions.map((priv) => (
                            <option key={priv} value={priv}>
                                {priv}
                            </option>
                        ))}
                    </select>
                </div>
                <div className="filter-control">
                    <label htmlFor="sort-order">Sort</label>
                    <select
                        id="sort-order"
                        value={sortOrder}
                        onChange={(e) => setSortOrder(e.target.value)}
                    >
                        <option value="name,asc">Name A → Z</option>
                        <option value="name,desc">Name Z → A</option>
                        <option value="createdAt,desc">Newest</option>
                        <option value="createdAt,asc">Oldest</option>
                    </select>
                </div>
                <div className="filter-actions">
                    <button className="filter-btn ghost" onClick={handleResetFilters} disabled={loading}>
                        Reset
                    </button>
                </div>
            </div>
            <div className="role-list-container">
                {roles.map(role => (
                    <div key={role.roleCode} className="card-container">
                        <div className="role-card-header">
                            <div className="role-avatar">
                                <Shield size={24} />
                            </div>
                            <h3>{role.name}</h3>
                        </div>

                        <div className="role-info-grid">
                            <div className="info-item">
                                <Key size={18} className="info-icon" />
                                <div>
                                    <span className="info-label">Role Code</span>
                                    <span className="info-value">{role.roleCode}</span>
                                </div>
                            </div>
                            {role.description && (
                                <div className="info-item">
                                    <FileText size={18} className="info-icon" />
                                    <div>
                                        <span className="info-label">Description</span>
                                        <span className="info-value">{role.description}</span>
                                    </div>
                                </div>
                            )}
                            <div className="info-item info-item-full">
                                <CheckSquare size={18} className="info-icon" />
                                <div>
                                    <span className="info-label">Privileges</span>
                                    <div className="privileges-list">
                                        {role.privileges && role.privileges.length > 0 ? (
                                            role.privileges.map((priv, idx) => (
                                                <span
                                                    key={idx}
                                                    className={`privilege-badge ${priv.permitted ? 'permitted' : 'denied'}`}
                                                >
                                                    {priv.privilege} {priv.permitted ? '✓' : '✗'}
                                                </span>
                                            ))
                                        ) : (
                                            <span className="info-value">No privileges assigned</span>
                                        )}
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div className="card-actions">
                            <button
                                className="btn-update"
                                onClick={() => openEditModal(role)}
                                disabled={loadingAction}
                            >
                                {loadingAction ? (
                                    <Loader2 size={16} className="btn-icon-spin" />
                                ) : (
                                    <Edit size={16} />
                                )}
                                <span>Update</span>
                            </button>
                            <button
                                className="btn-delete"
                                onClick={() => openDeleteModal(role)}
                                disabled={loadingAction}
                            >
                                {loadingAction ? (
                                    <Loader2 size={16} className="btn-icon-spin" />
                                ) : (
                                    <Trash2 size={16} />
                                )}
                                <span>Delete</span>
                            </button>
                        </div>
                    </div>
                ))}
                {roles.length === 0 && !loading && (
                    <div className="empty-state">
                        <p>No roles found.</p>
                    </div>
                )}
            </div>

            {isModalOpen && currentRoleToEdit && (
                <EditRoleModal
                    role={currentRoleToEdit}
                    onClose={closeEditModal}
                    onSave={handleSaveUpdate}
                    isLoading={loadingAction}
                />
            )}

            {isDeleteModalOpen && roleToDelete && (
                <DeleteConfirmModal
                    role={roleToDelete}
                    onClose={closeDeleteModal}
                    onConfirm={confirmDelete}
                    isLoading={loadingAction}
                />
            )}

            {pagination.total > 0 && (
                <Pagination
                    currentPage={currentPage}
                    totalPages={totalPages}
                    onPageChange={handlePageChange}
                    itemsPerPage={pageSize}
                    totalItems={pagination.total}
                />
            )}
        </div>
    );
}

