import React, { useEffect } from 'react';
import {
    Users,
    Mail,
    Phone,
    Calendar,
    MapPin,
    CreditCard
} from 'lucide-react';
import './UserDetailModal.css';

export default function UserDetailModal({ user, onClose }) {
    useEffect(() => {
        const handleKeyDown = (event) => {
            if (event.key === 'Escape') {
                onClose?.();
            }
        };

        document.addEventListener('keydown', handleKeyDown);

        const originalOverflow = document.body.style.overflow;
        document.body.style.overflow = 'hidden';

        return () => {
            document.removeEventListener('keydown', handleKeyDown);
            document.body.style.overflow = originalOverflow;
        };
    }, [onClose]);

    if (!user) return null;

    const details = [
        {
            icon: Users,
            label: 'Full Name',
            value: `${user.lastName || ''} ${user.firstName || ''}`.trim() || 'N/A'
        },
        {
            icon: Mail,
            label: 'Email',
            value: user.email
        },
        {
            icon: Phone,
            label: 'Phone Number',
            value: user.phoneNumber
        },
        {
            icon: Calendar,
            label: 'Date of Birth',
            value: user.dateOfBirth
                ? `${user.dateOfBirth}${user.age ? ` (Age: ${user.age})` : ''}`
                : null
        },
        {
            icon: Users,
            label: 'Gender',
            value: user.gender
        },
        {
            icon: MapPin,
            label: 'Address',
            value: user.address
        },
        {
            icon: CreditCard,
            label: 'Identity Number',
            value: user.identityNumber
        },
        {
            icon: CreditCard,
            label: 'ID',
            value: user.id
        },
        {
            icon: Users,
            label: 'Roles',
            value: Array.isArray(user.roleCodes) && user.roleCodes.length
                ? user.roleCodes.join(', ')
                : 'No roles assigned'
        },
        {
            icon: Users,
            label: 'Status',
            value: user.locked
                ? 'Locked'
                : (user.active === false ? 'Inactive' : 'Active')
        }
    ].filter(detail => Boolean(detail.value));

    return (
        <div
            className="detail-modal-overlay"
            onMouseDown={(event) => {
                if (event.target === event.currentTarget) {
                    onClose?.();
                }
            }}
        >
            <div className="detail-modal-content" role="dialog" aria-modal="true">
                <div className="detail-modal-header">
                    <div className="detail-avatar">
                        <Users size={36} />
                    </div>
                    <div>
                        <h3>{`${user.lastName || ''} ${user.firstName || ''}`.trim() || 'User Detail'}</h3>
                        <p>{user.email || 'No email provided'}</p>
                    </div>
                    <button className="detail-close-btn" onClick={onClose} aria-label="Close details">
                        &times;
                    </button>
                </div>

                <div className="detail-grid">
                    {details.map(detail => (
                        <div key={detail.label} className="detail-item">
                            <detail.icon size={18} className="detail-icon" />
                            <div>
                                <span className="detail-label">{detail.label}</span>
                                <span className="detail-value">{detail.value}</span>
                            </div>
                        </div>
                    ))}
                    {details.length === 0 && (
                        <div className="detail-empty">No detail data available.</div>
                    )}
                </div>
            </div>
        </div>
    );
}

