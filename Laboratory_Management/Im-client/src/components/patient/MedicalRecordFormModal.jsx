import React, { useState, useEffect } from 'react';
import { X } from 'lucide-react';
import "../../css/style.css"
// 1. Thêm `patient` vào props (ở dòng đầu tiên)
const MedicalRecordFormModal = ({ isOpen, onClose, onSave, recordToEdit, patient }) => {
    const [formData, setFormData] = useState({
        recordType: '',
        medicalHistory: '',
        currentMedications: '',
        allergies: '',
        clinicalNotes: '',
        lastTestDate: '',
        changeReason: '', // Dùng cho việc update
    });

    // Style (giữ nguyên)
    const inputStyle = "w-full px-3 py-2 rounded-md bg-bg border border-line focus:outline-none focus:border-primary-teal text-text";
    const labelStyle = "block text-sm font-medium text-muted mb-1";
    const textAreaStyle = `${inputStyle} min-h-[80px]`;

    // useEffect (giữ nguyên)
    useEffect(() => {
        if (recordToEdit) {
            setFormData({
                recordType: recordToEdit.recordType || '',
                medicalHistory: recordToEdit.medicalHistory || '',
                currentMedications: recordToEdit.currentMedications || '',
                allergies: recordToEdit.allergies || '',
                clinicalNotes: recordToEdit.clinicalNotes || '',
                lastTestDate: recordToEdit.lastTestDate || '',
                changeReason: '',
            });
        } else {
            setFormData({
                recordType: '', medicalHistory: '', currentMedications: '',
                allergies: '', clinicalNotes: '', lastTestDate: '', changeReason: '',
            });
        }
    }, [recordToEdit, isOpen]);

    // handleChange (giữ nguyên)
    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData((prev) => ({ ...prev, [name]: value }));
    };

    // 2. Cập nhật handleSubmit để TÍNH TUỔI
    const handleSubmit = (e) => {
        e.preventDefault();

        let finalFormData = { ...formData };

        // --- LOGIC MỚI: Tự động tính tuổi ---
        // Chỉ tính tuổi khi TẠO MỚI (không có recordToEdit) và CÓ thông tin patient
        if (!recordToEdit && patient && patient.dateOfBirth) {
            try {
                const birthDate = new Date(patient.dateOfBirth);
                const today = new Date();
                let age = today.getFullYear() - birthDate.getFullYear();
                const m = today.getMonth() - birthDate.getMonth();
                if (m < 0 || (m === 0 && today.getDate() < birthDate.getDate())) {
                    age--;
                }
                // Thêm tuổi vào data gửi đi
                finalFormData.patientAgeAtRecord = age;
            } catch (e) {
                console.error("Không thể tính tuổi:", e);
            }
        }
        // --- KẾT THÚC LOGIC MỚI ---

        // 3. Gửi
        onSave(finalFormData); // Gửi data đã có tuổi
    };

    if (!isOpen) return null;

    return (
        <div className="modal-overlay">
            <div className="modal-content modal-content-record-form">
                <form onSubmit={handleSubmit}>
                    {/* Header */}
                    <div className="modal-header">
                        <h2 className="modal-title">
                            {recordToEdit ? 'Chỉnh sửa Hồ sơ' : 'Thêm Hồ sơ Bệnh án'}
                        </h2>
                        <button type="button" onClick={onClose} className="modal-close-btn">
                            <X size={24} />
                        </button>
                    </div>

                    {/* Body Form */}
                    <div className="modal-body form-grid">
                        <div>
                            <label className="form-label">Type of record (e.g., General check-up)</label>
                            <input type="text" name="recordType" value={formData.recordType} onChange={handleChange} className="form-input" />
                        </div>
                        <div>
                            <label className="form-label">Date of last examination/test</label>
                            <input type="date" name="lastTestDate" value={formData.lastTestDate} onChange={handleChange} className="form-input" />
                        </div>

                        <div className="form-grid-span-2">
                            <label className="form-label">Medical history</label>
                            <textarea name="medicalHistory" value={formData.medicalHistory} onChange={handleChange} className="form-textarea"></textarea>
                        </div>
                        <div className="form-grid-span-2">
                            <label className="form-label">Current medication</label>
                            <textarea name="currentMedications" value={formData.currentMedications} onChange={handleChange} className="form-textarea"></textarea>
                        </div>
                        <div className="form-grid-span-2">
                            <label className="form-label">Allergy</label>
                            <textarea name="allergies" value={formData.allergies} onChange={handleChange} className="form-textarea"></textarea>
                        </div>
                        <div className="form-grid-span-2">
                            <label className="form-label">Clinical notes</label>
                            <textarea name="clinicalNotes" value={formData.clinicalNotes} onChange={handleChange} className="form-textarea"></textarea>
                        </div>
                        {recordToEdit && (
                            <div className="form-grid-span-2">
                                <label className="form-label">Reason for update</label>
                                <input type="text" name="changeReason" value={formData.changeReason} onChange={handleChange} className="form-input" placeholder="Nhập lý do thay đổi..." />
                            </div>
                        )}
                    </div>

                    {/* Footer */}
                    <div className="modal-footer">
                        <button type="button" onClick={onClose} className="btn btn-secondary">Hủy</button>
                        <button type="submit" className="btn btn-primary">Lưu Hồ sơ</button>
                    </div>
                </form>
            </div>
        </div>
    );
};

export default MedicalRecordFormModal;