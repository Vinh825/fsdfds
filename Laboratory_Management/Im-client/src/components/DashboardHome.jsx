import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import {
    Stethoscope,
    Users,
    Shield,
    UserPlus,
    List,
    UserCheck,
    Activity,
    Sparkles,
    ArrowRight
} from 'lucide-react';
import './DashboardHome.css';
import { api_getDashboardStats } from '../api_config/api_dashboardStats.jsx';

export default function DashboardHome() {
    const [stats, setStats] = useState({
        totalUsers: null,
        activeUsers: null,
        totalRoles: null
    });
    const [loadingStats, setLoadingStats] = useState(false);
    const [statsError, setStatsError] = useState(null);

    useEffect(() => {
        const fetchStats = async () => {
            setLoadingStats(true);
            setStatsError(null);
            try {
                const response = await api_getDashboardStats();
                const data = response.data?.result;
                if (data) {
                    setStats({
                        totalUsers: data.totalUsers,
                        activeUsers: data.activeUsers,
                        totalRoles: data.totalRoles
                    });
                } else {
                    setStatsError('Cannot load dashboard totals.');
                }
            } catch (err) {
                setStatsError(err.message || 'Cannot load dashboard totals.');
            } finally {
                setLoadingStats(false);
            }
        };

        fetchStats();
    }, []);

    const renderStatValue = (value) => {
        if (loadingStats) return '...';
        if (value === null || value === undefined) return '--';
        return value.toLocaleString();
    };

    return (
        <div className="dashboard-wrapper">
            {/* Header */}
            <div className="dashboard-header">
                <div className="header-content">
                    <div className="header-icon-wrapper">
                        <Stethoscope className="header-icon" size={48} />
                    </div>
                    <div>
                        <h1>Healthcare Laboratory</h1>
                        <p>System Management Dashboard</p>
                    </div>
                </div>
                <div className="header-decoration">
                    <Sparkles className="sparkle-icon" size={24} />
                </div>
            </div>

            {/* Main Content */}
            <div className="dashboard-content">
                {/* Welcome Card */}
                <div className="welcome-card">
                    <div className="welcome-icon-wrapper">
                        <Sparkles className="welcome-icon" size={40} />
                    </div>
                    <div className="welcome-text">
                        <h2>Welcome back!</h2>
                        <p>Select a function below to start managing the system</p>
                    </div>
                </div>

                {/* Management Sections */}
                <div className="management-sections">
                    {/* User Management */}
                    <div className="management-section">
                        <div className="section-header">
                            <div className="section-icon-wrapper">
                                <Users className="section-icon" size={28} />
                            </div>
                            <h3>User Management</h3>
                        </div>
                        <p className="section-description">
                            Create, view, and manage user information in the system.
                        </p>
                        <div className="action-buttons">
                            <Link to="/create" className="btn btn-primary">
                                <UserPlus className="btn-icon" size={18} />
                                <span className="btn-text">Create New User</span>
                                <ArrowRight className="btn-arrow" size={16} />
                            </Link>
                            <Link to="/users" className="btn btn-secondary">
                                <List className="btn-icon" size={18} />
                                <span className="btn-text">User List</span>
                                <ArrowRight className="btn-arrow" size={16} />
                            </Link>
                        </div>
                    </div>

                    {/* Role Management */}
                    <div className="management-section">
                        <div className="section-header">
                            <div className="section-icon-wrapper">
                                <Shield className="section-icon" size={28} />
                            </div>
                            <h3>Role Management</h3>
                        </div>
                        <p className="section-description">
                            Set up and manage roles and permissions in the system.
                        </p>
                        <div className="action-buttons">
                            <Link to="/roles/create" className="btn btn-primary">
                                <UserPlus className="btn-icon" size={18} />
                                <span className="btn-text">Create New Role</span>
                                <ArrowRight className="btn-arrow" size={16} />
                            </Link>
                            <Link to="/roles/list" className="btn btn-secondary">
                                <List className="btn-icon" size={18} />
                                <span className="btn-text">Role List</span>
                                <ArrowRight className="btn-arrow" size={16} />
                            </Link>
                        </div>
                    </div>
                </div>

                {/* Quick Stats */}
                <div className="quick-stats">
                    <div className="stat-card">
                        <div className="stat-icon-wrapper">
                            <Users className="stat-icon" size={32} />
                        </div>
                        <div className="stat-content">
                            <div className="stat-label">Total Users</div>
                            <div className="stat-value">{renderStatValue(stats.totalUsers)}</div>
                        </div>
                    </div>
                    <div className="stat-card">
                        <div className="stat-icon-wrapper">
                            <Shield className="stat-icon" size={32} />
                        </div>
                        <div className="stat-content">
                            <div className="stat-label">Total Roles</div>
                            <div className="stat-value">{renderStatValue(stats.totalRoles)}</div>
                        </div>
                    </div>
                    <div className="stat-card">
                        <div className="stat-icon-wrapper">
                            <Activity className="stat-icon" size={32} />
                        </div>
                        <div className="stat-content">
                            <div className="stat-label">Active</div>
                            <div className="stat-value">{renderStatValue(stats.activeUsers)}</div>
                        </div>
                    </div>
                </div>

                {statsError && (
                    <div className="stats-error">
                        {statsError}
                    </div>
                )}
            </div>
        </div>
    );
}
