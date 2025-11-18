import React, {useState, useEffect, useRef} from 'react';
import {useNavigate} from 'react-router-dom';
import {api_listUser} from '../../../../api_config/CRUD_User/api_ListUser.jsx';
import {api_updateUser} from '../../../../api_config/CRUD_User/api_updateUser.jsx';
import {api_deleteUser} from '../../../../api_config/CRUD_User/api_deleteUser.jsx';
import {api_getAllRoles} from '../../../../api_config/CRUD_Role/api_getAllRoles.jsx';
import {api_updateUserRoles} from '../../../../api_config/CRUD_User/api_updateUserRoles.jsx';
import {api_toggleLockUser} from '../../../../api_config/CRUD_User/api_toggleLockUser.jsx';
import {
    Users,
    Mail,
    Phone,
    CreditCard,
    Edit,
    Trash2,
    Loader2,
    UserPlus,
    Shield,
    Lock,
    Unlock
} from 'lucide-react';
import './ListUser.css';
import EditUserModal from '../UpdateUser/EditUserModal.jsx';
import DeleteConfirmModal from '../DeleteUser/DeleteConfirmModal.jsx';
import Pagination from '../../../../components/common/Pagination.jsx';
import UserDetailModal from './UserDetailModal.jsx';
import ManageRolesModal from './ManageRolesModal.jsx';
import useDebounce from '../../../../hooks/useDebounce.js';

export default function UserListPage() {
    const navigate = useNavigate();
    const itemsPerPage = 3;

    const [users, setUsers] = useState([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);
    const [pagination, setPagination] = useState({page: 0, size: itemsPerPage, total: 0});
    const [currentPage, setCurrentPage] = useState(1);

    const [isModalOpen, setIsModalOpen] = useState(false);
    const [currentUserToEdit, setCurrentUserToEdit] = useState(null);
    const [isDeleteModalOpen, setIsDeleteModalOpen] = useState(false);
    const [userToDelete, setUserToDelete] = useState(null);
    const [loadingAction, setLoadingAction] = useState(false);
    const [isDetailModalOpen, setIsDetailModalOpen] = useState(false);
    const [currentUserToView, setCurrentUserToView] = useState(null);
    const [isRolesModalOpen, setIsRolesModalOpen] = useState(false);
    const [currentUserRoles, setCurrentUserRoles] = useState(null);
    const [searchTerm, setSearchTerm] = useState('');
    const [genderFilter, setGenderFilter] = useState('ALL');
    const [roleFilter, setRoleFilter] = useState('');
    const [roleOptions, setRoleOptions] = useState([]);
    const debouncedSearchTerm = useDebounce(searchTerm, 400);
    const debouncedRoleFilter = useDebounce(roleFilter, 200);
    const filtersEffectHasRun = useRef(false);

    const pageSize = pagination.size || itemsPerPage;

    const buildQueryParams = (page, size) => {
        const params = {page: page - 1, size};
        const trimmedSearch = searchTerm.trim();
        if (trimmedSearch) {
            params.q = trimmedSearch;
        }
        if (genderFilter !== 'ALL') {
            params.gender = genderFilter;
        }
        if (roleFilter) {
            params.roleCode = roleFilter;
        }
        return params;
    };

    const fetchUsers = async (page = 1, size = pageSize) => {
        setLoading(true);
        setError(null);
        try {
            const response = await api_listUser(buildQueryParams(page, size));
            const data = response.data?.result;
            if (data) {
                setUsers(Array.isArray(data.items) ? data.items : []);
                setPagination({
                    page: data.page ?? 0,
                    size: data.size ?? size,
                    total: data.total ?? 0
                });
                setCurrentPage((data.page ?? 0) + 1);
            } else {
                setError("Invalid data structure returned from API.");
            }
        } catch (err) {
            setError(err.message || "An error occurred while fetching the list.");
        } finally {
            setLoading(false);
        }
    };

    const loadRoleOptions = async () => {
        try {
            const response = await api_getAllRoles();
            const data = response.data?.result;
            setRoleOptions(Array.isArray(data) ? data : []);
        } catch (err) {
            // silent fail
        }
    };

    useEffect(() => {
        fetchUsers(1);
        loadRoleOptions();
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, []);

    useEffect(() => {
        if (!filtersEffectHasRun.current) {
            filtersEffectHasRun.current = true;
            return;
        }
        setCurrentPage(1);
        fetchUsers(1, pagination.size || itemsPerPage);
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [debouncedSearchTerm, genderFilter, debouncedRoleFilter]);

    const openEditModal = (user) => {
        setCurrentUserToEdit(user);
        setIsModalOpen(true);
    };

    const closeEditModal = () => {
        setIsModalOpen(false);
        setCurrentUserToEdit(null);
    };

    const openDeleteModal = (user) => {
        setUserToDelete(user);
        setIsDeleteModalOpen(true);
    };

    const closeDeleteModal = () => {
        setUserToDelete(null);
        setIsDeleteModalOpen(false);
    };

    const confirmDelete = async () => {
        if (!userToDelete) return;

        setLoadingAction(true);
        try {
            await api_deleteUser(userToDelete.id);
            const totalAfterDelete = Math.max((pagination.total ?? users.length) - 1, 0);
            const pageSize = pagination.size || itemsPerPage;
            const totalPagesAfterDelete = pageSize ? Math.ceil(totalAfterDelete / pageSize) : 0;
            const targetPage = totalPagesAfterDelete === 0
                ? 1
                : Math.min(currentPage, totalPagesAfterDelete);
            await fetchUsers(Math.max(targetPage, 1), pageSize);
            closeDeleteModal();
        } catch (err) {
            setError(err.message || "Error deleting user.");
        } finally {
            setLoadingAction(false);
        }
    };

    const handleSaveUpdate = async (updatedUserData) => {
        setLoadingAction(true);
        try {
            const response = await api_updateUser(
                updatedUserData.id,
                updatedUserData.email,
                updatedUserData.firstName,
                updatedUserData.lastName,
                updatedUserData.gender,
                updatedUserData.phoneNumber,
                updatedUserData.address,
                updatedUserData.dateOfBirth
            );

            if (response.data && response.data.result) {
                const updatedUser = response.data.result;
                setUsers(prevUsers => prevUsers.map(user =>
                    user.id === updatedUser.id ? updatedUser : user
                ));
                closeEditModal();
            } else {
                setError("Error updating: Invalid data returned.");
            }
        } catch (err) {
            setError(err.message || "Error updating user.");
        } finally {
            setLoadingAction(false);
        }
    };

    const openDetailModal = (user) => {
        setCurrentUserToView(user);
        setIsDetailModalOpen(true);
    };

    const closeDetailModal = () => {
        setCurrentUserToView(null);
        setIsDetailModalOpen(false);
    };

    const handleSaveRoles = async (updatedRoles) => {
        if (!currentUserRoles) return;
        setLoadingAction(true);
        try {
            const response = await api_updateUserRoles(currentUserRoles.id, updatedRoles);
            const updatedUser = response.data?.result;
            if (updatedUser) {
                setUsers(prevUsers => prevUsers.map(user =>
                    user.id === updatedUser.id ? updatedUser : user
                ));
                closeRolesModal();
            } else {
                setError("Error updating roles: Invalid response.");
            }
        } catch (err) {
            setError(err.message || "Error updating roles.");
        } finally {
            setLoadingAction(false);
        }
    };

    const handleToggleLock = async (user) => {
        if (!user) return;
        setLoadingAction(true);
        try {
            const response = await api_toggleLockUser(user.id, !user.locked);
            const updatedUser = response.data?.result;
            if (updatedUser) {
                setUsers(prevUsers => prevUsers.map(item =>
                    item.id === updatedUser.id ? updatedUser : item
                ));
            } else {
                setError("Error updating status: Invalid response.");
            }
        } catch (err) {
            setError(err.message || "Error updating user status.");
        } finally {
            setLoadingAction(false);
        }
    };

    const openRolesModal = (user) => {
        setCurrentUserRoles(user);
        setIsRolesModalOpen(true);
    };

    const closeRolesModal = () => {
        setCurrentUserRoles(null);
        setIsRolesModalOpen(false);
    };

    if (loading) {
        return <div className="loading-container">
            <div>Loading...</div>
        </div>;
    }

    if (error) {
        return <div className="error-container">
            Error: {error}
            <button onClick={() => {
                setError(null);
                fetchUsers(currentPage);
            }}>Retry
            </button>
        </div>;
    }

    const totalPages = pageSize ? Math.ceil((pagination.total ?? 0) / pageSize) : 0;

    const handlePageChange = (page) => {
        if (page < 1 || page > totalPages) return;
        setCurrentPage(page);
        fetchUsers(page, pageSize);
        window.scrollTo({top: 0, behavior: 'smooth'});
    };

    const handleResetFilters = () => {
        setSearchTerm('');
        setGenderFilter('ALL');
        setRoleFilter('');
        fetchUsers(1, itemsPerPage);
    };

    return (
        <div style={{padding: '20px'}}>
            <div className="list-user-header">
                <div className="list-user-header-content">
                    <div className="list-user-header-icon">
                        <Users size={32}/>
                    </div>
                    <h2>User List</h2>
                </div>
                <button
                    className="btn-create-header"
                    onClick={() => navigate('/create')}
                >
                    <UserPlus size={18}/>
                    <span>Create User</span>
                </button>
            </div>
            <div className="list-filters">
                <div className="filter-control">
                    <label htmlFor="user-search">Search</label>
                    <input
                        id="user-search"
                        type="text"
                        value={searchTerm}
                        onChange={(e) => setSearchTerm(e.target.value)}
                        placeholder="Name, email, phone..."
                    />
                </div>
                <div className="filter-control">
                    <label htmlFor="gender-filter">Gender</label>
                    <select
                        id="gender-filter"
                        value={genderFilter}
                        onChange={(e) => setGenderFilter(e.target.value)}
                    >
                        <option value="ALL">All</option>
                        <option value="MALE">Male</option>
                        <option value="FEMALE">Female</option>
                    </select>
                </div>
                <div className="filter-control">
                    <label htmlFor="role-filter">Role</label>
                    <select
                        id="role-filter"
                        value={roleFilter}
                        onChange={(e) => setRoleFilter(e.target.value)}
                    >
                        <option value="">All roles</option>
                        {roleOptions.map(role => (
                            <option key={role.roleCode} value={role.roleCode}>
                                {role.name || role.roleCode}
                            </option>
                        ))}
                    </select>
                </div>
                <div className="filter-actions">
                    <button className="filter-btn ghost" onClick={handleResetFilters} disabled={loading}>
                        Reset
                    </button>
                </div>
            </div>
            <div className="user-list-container">
                {users.map(user => {
                    const userRoles = Array.isArray(user.roleCodes) ? user.roleCodes : [];
                    const isLocked = Boolean(user.locked);
                    const isActive = user.active !== false && !isLocked;
                    const summaryFields = [
                        {
                            icon: Mail,
                            label: 'Email',
                            value: user.email
                        },
                        {
                            icon: Phone,
                            label: 'Phone',
                            value: user.phoneNumber
                        },
                        {
                            icon: CreditCard,
                            label: 'Identity Number',
                            value: user.identityNumber
                        }
                    ].filter(field => Boolean(field.value));

                    return (
                        <div key={user.id} className="card-container">
                            <div className="user-card-header">
                                <div className="user-avatar">
                                    <Users size={24}/>
                                </div>
                                <button
                                    type="button"
                                    className="user-name-button"
                                    onClick={() => openDetailModal(user)}
                                >
                                    {user.lastName} {user.firstName}
                                </button>
                            </div>

                            <div className="user-info-grid">
                                {summaryFields.map(field => (
                                    <div key={field.label} className="info-item">
                                        <field.icon size={18} className="info-icon"/>
                                        <div>
                                            <span className="info-label">{field.label}</span>
                                            <span className="info-value">{field.value}</span>
                                        </div>
                                    </div>
                                ))}
                                {summaryFields.length === 0 && (
                                    <div className="info-item">
                                        <div>
                                            <span className="info-label">No quick info</span>
                                            <span className="info-value">Click the name to view full details.</span>
                                        </div>
                                    </div>
                                )}
                            </div>

                            <div className="role-chip-wrapper">
                                <div className="role-chip-title">
                                    <Shield size={16}/>
                                    <span>Roles</span>
                                </div>
                                <div className="role-chip-list">
                                    {userRoles.length === 0 && (
                                        <span className="role-chip empty">No roles</span>
                                    )}
                                    {userRoles.map((role) => (
                                        <span key={role} className="role-chip">{role}</span>
                                    ))}
                                </div>
                            </div>

                            <div className="status-pill-row">
                                <span className={`status-pill ${isLocked ? 'status-locked' : 'status-active'}`}>
                                    {isLocked ? 'Locked' : 'Active'}
                                </span>
                                {!isActive && !isLocked && (
                                    <span className="status-pill status-inactive">Inactive</span>
                                )}
                            </div>

                            <div className="card-actions">
                                <button
                                    className="btn-update"
                                    onClick={() => openEditModal(user)}
                                    disabled={loadingAction}
                                >
                                    {loadingAction ? (
                                        <Loader2 size={16} className="btn-icon-spin"/>
                                    ) : (
                                        <Edit size={16}/>
                                    )}
                                    <span>Update</span>
                                </button>
                                <button
                                    className="btn-delete"
                                    onClick={() => openDeleteModal(user)}
                                    disabled={loadingAction}
                                >
                                    {loadingAction ? (
                                        <Loader2 size={16} className="btn-icon-spin"/>
                                    ) : (
                                        <Trash2 size={16}/>
                                    )}
                                    <span>Delete</span>
                                </button>

                                <button
                                    className="btn-secondary"
                                    onClick={() => openRolesModal(user)}
                                    disabled={loadingAction}
                                >
                                    <Shield size={16}/>
                                    <span>Manage Roles</span>
                                </button>

                                <button
                                    className={isLocked ? 'btn-unlock' : 'btn-lock'}
                                    onClick={() => handleToggleLock(user)}
                                    disabled={loadingAction}
                                >
                                    {isLocked ? <Unlock size={16}/> : <Lock size={16}/>}
                                    <span>{isLocked ? 'Unlock' : 'Lock'}</span>
                                </button>

                            </div>
                </div>
                )
                    ;
                })}
                {users.length === 0 && (
                    <div className="empty-state">
                        <p>No users found.</p>
                    </div>
                )}
            </div>

            {
                isModalOpen && currentUserToEdit && (
                    <EditUserModal
                        user={currentUserToEdit}
                        onClose={closeEditModal}
                        onSave={handleSaveUpdate}
                        isLoading={loadingAction}
                    />
                )
            }

            {
                isDeleteModalOpen && userToDelete && (
                    <DeleteConfirmModal
                        user={userToDelete}
                        onClose={closeDeleteModal}
                        onConfirm={confirmDelete}
                        isLoading={loadingAction}
                    />
                )
            }

            {
                isRolesModalOpen && currentUserRoles && (
                    <ManageRolesModal
                        user={currentUserRoles}
                        availableRoles={roleOptions}
                        onClose={closeRolesModal}
                        onSave={handleSaveRoles}
                        isLoading={loadingAction}
                    />
                )
            }

            {
                isDetailModalOpen && currentUserToView && (
                    <UserDetailModal
                        user={currentUserToView}
                        onClose={closeDetailModal}
                    />
                )
            }

            {
                pagination.total > 0 && (
                    <Pagination
                        currentPage={currentPage}
                        totalPages={totalPages}
                        onPageChange={handlePageChange}
                        itemsPerPage={pageSize}
                        totalItems={pagination.total}
                    />
                )
            }
        </div>
    );
}