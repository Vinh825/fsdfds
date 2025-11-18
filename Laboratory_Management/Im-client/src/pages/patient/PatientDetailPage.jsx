import React, { useState, useEffect, useCallback } from 'react';
import { useParams, Link } from 'react-router-dom';
import * as api from '../../api_config/patient_api/api.js';
import toast from 'react-hot-toast';
import { ArrowLeft, Plus, User } from 'lucide-react';
import Loader from '../../components/patient/Loader.jsx';
import MedicalRecordList from '../../components/patient/MedicalRecordList.jsx';
import MedicalRecordFormModal from '../../components/patient/MedicalRecordFormModal.jsx';
import ConfirmationModal from '../../components/patient/ConfirmationModal.jsx';

// BƯỚC 1: Sửa lại đường dẫn import cho ĐÚNG
import '../../css/style.css';
// (Và đảm bảo bạn đã import global.css trong file main.jsx nhé)
const formatDate = (dateString) => {
    if (!dateString) return 'Chưa có';
    try {
        return new Date(dateString).toLocaleDateString('vi-VN', {
            day: '2-digit',
            month: '2-digit',
            year: 'numeric',
        });
    } catch  {
        return dateString;
    }
};

const formatGender = (gender) => {
    if (gender === 'Male') return 'Nam';
    if (gender === 'Female') return 'Nữ';
    if (gender === 'Other') return 'Khác';
    return 'Chưa có';
};
//----------------------
const PatientDetailPage = () => {
    // ... (Toàn bộ logic state và hàm của bạn giữ nguyên) ...
    const { patientId } = useParams();
    const [patient, setPatient] = useState(null);
    const [records, setRecords] = useState([]);
    const [isLoading, setIsLoading] = useState(false);
    const [isFormOpen, setIsFormOpen] = useState(false);
    const [isConfirmOpen, setIsConfirmOpen] = useState(false);
    const [recordToEdit, setRecordToEdit] = useState(null);
    const [recordToDelete, setRecordToDelete] = useState(null);

    const fetchData = useCallback(async () => {
        setIsLoading(true);
        try {
            const patientRes = await api.getPatientById(patientId);
            setPatient(patientRes.data);

            const recordsRes = await api.getRecordsByPatientId(patientId);
            setRecords(recordsRes.data);
        } catch (error) {
            toast.error('Unable to load detailed data.');
            console.error(error);
        } finally {
            setIsLoading(false);
        }
    }, [patientId]);

    useEffect(() => {
        fetchData();
    }, [fetchData]);

    // === Xử lý CRUD cho Hồ sơ bệnh án ===

    const handleSaveRecord = async (recordData) => {
        setIsLoading(true);
        const dataWithPatientId = { ...recordData, patientId: patient.patientId };

        try {
            if (recordToEdit) {
                await api.updateRecord(recordToEdit.patientMedicalRecordId, dataWithPatientId);
                toast.success('Profile updated successfully!');
            } else {
                await api.createRecord(dataWithPatientId);
                toast.success('New profile added successfully!');
            }
            setIsFormOpen(false);
            setRecordToEdit(null);
            fetchData(); // Tải lại toàn bộ dữ liệu
        } catch  {
            toast.error('Error saving the profile.');
        } finally {
            setIsLoading(false);
        }
    };

    const handleEditRecord = (record) => {
        setRecordToEdit(record);
        setIsFormOpen(true);
    };

    const handleDeleteRecordRequest = (record) => {
        setRecordToDelete(record);
        setIsConfirmOpen(true);
    };

    const handleConfirmDeleteRecord = async () => {
        if (!recordToDelete) return;
        setIsLoading(true);
        try {
            await api.deleteRecord(recordToDelete.patientMedicalRecordId);
            toast.success('Profile deleted.');
            setIsConfirmOpen(false);
            setRecordToDelete(null);
            fetchData(); // Tải lại dữ liệu
        } catch  {
            toast.error('Error deleting profile.');
        } finally {
            setIsLoading(false);
        }
    };


    if (isLoading && !patient) return <Loader />;
    if (!patient) return <p className="text-center text-muted">Patient not found.</p>;

    // BƯỚC 2: Toàn bộ phần return bên dưới đã được cập nhật className
    return (
        <div className="page-container space-y-6">
            {isLoading && <Loader />}

            <Link to="/patient-home" className="back-link">
                <ArrowLeft size={18} />
                Back
            </Link>

            {/* Thông tin bệnh nhân - ĐÃ THIẾT KẾ LẠI */}
            <div className="patient-info-card">
                {/* Card Header */}
                <div className="patient-profile-header">
                    <div className="profile-avatar">
                        <User size={48} />
                    </div>
                    <div className="profile-header-info">
                        <h1>{patient.fullName}</h1>
                        <span className="patient-code">{patient.patientCode}</span>
                    </div>
                </div>

                {/* Card Body */}
                <div className="patient-profile-body">
                    {/* Cột 1: Thông tin cá nhân */}
                    <div className="info-section">
                        <h3>Personal information</h3>
                        <dl className="info-list">
                            <div className="info-item">
                                <dt className="info-label">Date of birth</dt>
                                <dd className="info-value">{formatDate(patient.dateOfBirth)}</dd>
                            </div>
                            <div className="info-item">
                                <dt className="info-label">Gender</dt>
                                <dd className="info-value">{formatGender(patient.gender)}</dd>
                            </div>
                            <div className="info-item">
                                <dt className="info-label">Address</dt>
                                <dd className="info-value">{patient.address || 'Chưa có'}</dd>
                            </div>
                        </dl>
                    </div>

                    {/* Cột 2: Thông tin liên hệ */}
                    <div className="info-section">
                        <h3>Contact information</h3>
                        <dl className="info-list">
                            <div className="info-item">
                                <dt className="info-label">Telephone</dt>
                                <dd className="info-value">{patient.phoneNumber || 'Chưa có'}</dd>
                            </div>
                            <div className="info-item">
                                <dt className="info-label">Email</dt>
                                <dd className="info-value">{patient.email || 'Chưa có'}</dd>
                            </div>
                        </dl>
                    </div>
                </div>
            </div>
            {/* --- Hết phần thiết kế lại --- */}


            {/* Phần hồ sơ bệnh án (Giữ nguyên) */}
            <div className="space-y-4">
                <div className="records-header">
                    <h2 className="text-2xl font-bold text-text">Medical Records</h2>
                    <button
                        onClick={() => { setRecordToEdit(null); setIsFormOpen(true); }}
                        className="btn btn-primary"
                    >
                        <Plus size={18} />
                        Add New Profile
                    </button>
                </div>
                <MedicalRecordList
                    records={records}
                    onEdit={handleEditRecord}
                    onDelete={handleDeleteRecordRequest}
                />
            </div>

            {/* Modals (Giữ nguyên) */}
            <MedicalRecordFormModal
                isOpen={isFormOpen}
                onClose={() => setIsFormOpen(false)}
                onSave={handleSaveRecord}
                recordToEdit={recordToEdit}
                patient={patient}
            />
            <ConfirmationModal
                isOpen={isConfirmOpen}
                onClose={() => setIsConfirmOpen(false)}
                onConfirm={handleConfirmDeleteRecord}
                title="Xác nhận Xóa Hồ sơ"
                message="Bạn có chắc chắn muốn xóa hồ sơ bệnh án này?"
            />
        </div>
    );
};

export default PatientDetailPage;

