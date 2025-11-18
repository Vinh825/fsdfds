import React from 'react';
import { Edit, Trash2 } from 'lucide-react';
import "../../css/style.css"
const MedicalRecordList = ({ records, onEdit, onDelete }) => {
    if (records.length === 0) {
        return (
            <div className="bg-card rounded-lg border border-line shadow-md p-12">
                <p className="text-center text-muted">This patient does not have any medical records yet.</p>
            </div>
        );
    }

    return (
        <div className="list-container">
            <ul>
                {records.map((record) => (
                    <li key={record.patientMedicalRecordId} className="list-item">
                        <div className="list-item-header">
                            <h3 className="list-item-title">{record.recordType || 'General profile'}</h3>
                            <div className="list-item-actions">
                                <button
                                    onClick={() => onEdit(record)}
                                    className="btn-icon-text btn-icon-edit"
                                    title="Edit"
                                >
                                    <Edit size={18} />
                                </button>
                                <button
                                    onClick={() => onDelete(record)}
                                    className="btn-icon-text btn-icon-delete"
                                    title="Delete"
                                >
                                    <Trash2 size={18} />
                                </button>
                            </div>
                        </div>
                        <div className="list-item-body">
                            <p><strong>Medical history:</strong> {record.medicalHistory}</p>
                            <p><strong>Current medications:</strong> {record.currentMedications}</p>
                            <p><strong>Allergy:</strong> {record.allergies}</p>
                            <p>
                                <strong>Treatment date: </strong>
                                {record.lastTestDate ? new Date(record.lastTestDate).toLocaleDateString('vi-VN') : 'Chưa có'}
                            </p>
                        </div>
                    </li>
                ))}
            </ul>
        </div>
    );
};

export default MedicalRecordList;