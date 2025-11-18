import React, { useState, useEffect, useCallback } from 'react';
import { Plus, Search, RefreshCw } from 'lucide-react';
import * as api from '../../api_config/patient_api/api.js';
import PatientList from '../../components/patient/PatientList.jsx';
import PatientFormModal from '../../components/patient/PatientFormModal';
import ConfirmationModal from '../../components/patient/ConfirmationModal.jsx';
import Loader from '../../components/patient/Loader.jsx';
import toast from 'react-hot-toast';
import '../../css/style.css';

const PatientManagementPage = () => {
    // ... (Toàn bộ logic state và các hàm của bạn giữ nguyên) ...
    const [patients, setPatients] = useState([]);
    const [isLoading, setIsLoading] = useState(false);
    const [isFormOpen, setIsFormOpen] = useState(false);
    const [isConfirmOpen, setIsConfirmOpen] = useState(false);
    const [selectedPatient, setSelectedPatient] = useState(null);
    const [patientToDelete, setPatientToDelete] = useState(null);
    const [searchTerm, setSearchTerm] = useState('');
// 1. Lấy thông tin user
    // { role: 'ROLE_ADMIN', ... }

    // 2. Định nghĩa quyền dựa trên CÁCH 1 (SecurityConfig)
    // Luật 1: POST /api/patients -> hasRole("ADMIN")

    const fetchPatients = useCallback(async () => {
        setIsLoading(true);
        try {
            let response;
            if (searchTerm) {
                response = await api.searchPatients(searchTerm);
            } else {
                response = await api.getAllPatients();
            }
            setPatients(response.data);
        } catch (error) {
            toast.error('Unable to load the patient list.');
            console.error(error);
        } finally {
            setIsLoading(false);
        }

    }, [searchTerm]);

    useEffect(() => {
        fetchPatients();
    }, [fetchPatients]);

    const handleSave = async (patientData) => {
        setIsLoading(true);
        try {
            if (selectedPatient) {
                // Cập nhật
                await api.updatePatient(selectedPatient.patientId, patientData);
                toast.success('Patient updated successfully!');
            } else {
                // Tạo mới
                await api.createPatient(patientData);
                toast.success('New patient added successfully!');
            }
            setIsFormOpen(false);
            setSelectedPatient(null);
            fetchPatients(); // Tải lại danh sách
        } catch (error) {
            toast.error('An error occurred while saving.');
            console.error(error);
        } finally {
            setIsLoading(false);
        }

    };
    const handleCreate = () => {
        setSelectedPatient(null);
        setIsFormOpen(true);
    };
    const handleEdit = (patient) => {
        setSelectedPatient(patient);
        setIsFormOpen(true);
    };
    const handleDeleteRequest = (patient) => {
        setPatientToDelete(patient);
        setIsConfirmOpen(true);
    };
    const handleConfirmDelete = async () => {
        if (!patientToDelete) return;


        setIsLoading(true);
        try {
            await api.deletePatient(patientToDelete.patientId);
            toast.success(`Patient has been deleted ${patientToDelete.fullName}`);
            setIsConfirmOpen(false);
            setPatientToDelete(null);
            fetchPatients(); // Tải lại danh sách
        } catch (error) {
            toast.error('Error when deleting the patient.');
            console.error(error);
        } finally {
            setIsLoading(false);
        }

    };

    // BƯỚC 2: Toàn bộ phần return bên dưới đã được cập nhật className
    return (
        <div className="page-container space-y-6">
            {isLoading && <Loader />}

            {/* Header của trang */}
            <div className="page-header">
                <h1 className="page-title">Patient List</h1>
                <div className="header-actions">
                    {/* Tìm kiếm */}
                    <div className="search-container">
                        <input
                            type="text"
                            placeholder="Search by name, code"
                            value={searchTerm}
                            onChange={(e) => setSearchTerm(e.target.value)}
                            className="search-input"
                        />
                        <Search className="search-icon" size={18} />
                    </div>
                    <button
                        onClick={fetchPatients}
                        className="btn-icon"
                        title="Reload"
                    >
                        <RefreshCw size={20} />
                    </button>

                    <button
                        onClick={handleCreate}
                        className="btn btn-primary"
                    >
                        <Plus size={18} />
                        Add new
                    </button>
                </div>
            </div>

            {/* Danh sách bệnh nhân */}
            <PatientList
                patients={patients}
                onEdit={handleEdit}
                onDelete={handleDeleteRequest}

            />

            {/* Modal Form (Thêm/Sửa) */}
            <PatientFormModal
                isOpen={isFormOpen}
                onClose={() => setIsFormOpen(false)}
                onSave={handleSave}
                patientToEdit={selectedPatient}
            />

            {/* Modal Xác nhận Xóa */}
            <ConfirmationModal
                isOpen={isConfirmOpen}
                onClose={() => setIsConfirmOpen(false)}
                onConfirm={handleConfirmDelete}
                title="Confirm Deletion"
                message={`Are you sure you want to delete the patient? ${patientToDelete?.fullName}?`}
            />
        </div>
    );
};

export default PatientManagementPage;

