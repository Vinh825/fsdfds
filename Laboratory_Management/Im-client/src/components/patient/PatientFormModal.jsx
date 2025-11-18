import React, { useState, useEffect } from 'react';
import { X } from 'lucide-react';
import "../../css/style.css"
const PatientFormModal = ({ isOpen, onClose, onSave, patientToEdit }) => {
    const [formData, setFormData] = useState({
        fullName: '',
        dateOfBirth: '',
        gender: 'Male',
        address: '',
        phoneNumber: '',
        email: '',
        identityNumber: '',
        emergencyContactName: '',
        emergencyContactPhone: '',
        isActive: true,
    });

    // Khi `patientToEdit` thay đổi, cập nhật form
    useEffect(() => {
        if (patientToEdit) {
            setFormData({
                patientCode: patientToEdit.patientCode || '',
                fullName: patientToEdit.fullName || '',
                dateOfBirth: patientToEdit.dateOfBirth || '',
                gender: patientToEdit.gender || 'Male',
                address: patientToEdit.address || '',
                phoneNumber: patientToEdit.phoneNumber || '',
                email: patientToEdit.email || '',
                identityNumber: patientToEdit.identityNumber || '',
                emergencyContactName: patientToEdit.emergencyContactName || '',
                emergencyContactPhone: patientToEdit.emergencyContactPhone || '',
                isActive: patientToEdit.isActive !== null ? patientToEdit.isActive : true,
            });
        } else {
            // Reset form khi tạo mới
            setFormData({
                fullName: '', dateOfBirth: '', gender: 'Male', address: '',
                phoneNumber: '', email: '', identityNumber: '',
                emergencyContactName: '', emergencyContactPhone: '', isActive: true,
            });
        }
    }, [patientToEdit, isOpen]);

    const handleChange = (e) => {
        const { name, value, type, checked } = e.target;
        setFormData((prev) => ({
            ...prev,
            [name]: type === 'checkbox' ? checked : value,
        }));
    };

    const handleSubmit = (e) => {
        e.preventDefault();
        onSave(formData);
    };

    if (!isOpen) return null;

    // Style cho input chung
    const inputStyle = "w-full px-3 py-2 rounded-md bg-bg border border-line focus:outline-none focus:border-primary-teal text-text";
    const labelStyle = "block text-sm font-medium text-muted mb-1";

    return (
        <div className="modal-overlay">
            <div className="modal-content modal-content-form">
                <form onSubmit={handleSubmit}>
                    {/* Header Modal */}
                    <div className="modal-header">
                        <h2 className="modal-title">
                            {patientToEdit ? 'Edit Patient' : 'Add New Patient'}
                        </h2>

                        <button type="button" onClick={onClose} className="modal-close-btn">
                            <X size={24} />
                        </button>
                    </div>

                    {/* Body Modal (Form) */}
                    <div className="modal-body form-grid">
                        {/* Full Name (Required) */}
                        <div className="form-grid-span-2">
                            <label className="form-label form-label-required">Full Name <span>*</span></label>
                            <input type="text" name="fullName" value={formData.fullName} onChange={handleChange} className="form-input" required />
                        </div>

                        {/* Date of Birth */}
                        <div>
                            <label className="form-label">Date of birth</label>
                            <input type="date" name="dateOfBirth" value={formData.dateOfBirth} onChange={handleChange} className="form-input" />
                        </div>

                        {/* Gender */}
                        <div>
                            <label className="form-label">Gender</label>
                            <select name="gender" value={formData.gender} onChange={handleChange} className="form-select">
                                <option value="Male">Male</option>
                                <option value="Female">Female</option>
                                <option value="Other">Other</option>
                            </select>
                        </div>

                        {/* Address */}
                        <div className="form-grid-span-2">
                            <label className="form-label">Address</label>
                            <input type="text" name="address" value={formData.address} onChange={handleChange} className="form-input" />
                        </div>

                        {/* Phone Number */}
                        <div>
                            <label className="form-label">Phone number</label>
                            <input type="tel" name="phoneNumber" value={formData.phoneNumber} onChange={handleChange} className="form-input" placeholder="09xxxxxxxx" />
                        </div>

                        {/* Email */}
                        <div>
                            <label className="form-label">Email</label>
                            <input type="email" name="email" value={formData.email} onChange={handleChange} className="form-input" placeholder="user@example.com" />
                        </div>

                        {/* Identity Number */}
                        <div>
                            <label className="form-label">CCCD/CMND</label>
                            <input type="text" name="identityNumber" value={formData.identityNumber} onChange={handleChange} className="form-input" />
                        </div>

                        {/* Is Active */}
                        <div className="form-checkbox-container">
                            <input type="checkbox" id="isActive" name="isActive" checked={formData.isActive} onChange={handleChange} className="form-checkbox" />
                            <label htmlFor="isActive" className="form-checkbox-label">Under Treatment</label>
                        </div>

                        {/* Emergency Contact */}
                        <h3 className="form-section-title">Emergency contact</h3>
                        <div>
                            <label className="form-label">Contact Name</label>
                            <input type="text" name="emergencyContactName" value={formData.emergencyContactName} onChange={handleChange} className="form-input" />
                        </div>
                        <div>
                            <label className="form-label">Contact person's phone number</label>
                            <input type="tel" name="emergencyContactPhone" value={formData.emergencyContactPhone} onChange={handleChange} className="form-input" />
                        </div>
                    </div>

                    {/* Footer Modal */}
                    <div className="modal-footer">
                        <button
                            type="button"
                            onClick={onClose}
                            className="btn btn-secondary"
                        >
                            Hủy
                        </button>
                        <button
                            type="submit"
                            className="btn btn-primary"
                        >
                            Lưu lại
                        </button>
                    </div>
                </form>
            </div>
        </div>
    );
};

export default PatientFormModal;