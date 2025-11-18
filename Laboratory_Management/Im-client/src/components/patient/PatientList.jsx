import React from 'react';
import { Edit, Trash2, CheckCircle, XCircle } from 'lucide-react';
import { Link } from 'react-router-dom';
import "../../css/style.css"

const PatientList = ({ patients, onEdit, onDelete }) => {
    // 2. Định nghĩa quyền (roles) theo CÁCH 1 (SecurityConfig)

    // Luật BE: PUT ... -> hasAnyRole("ADMIN", "USER")

    if (patients.length === 0) {
        return <p className="list-empty">Không tìm thấy bệnh nhân nào.</p>;
    }

    return (
        <div className="table-container">
            <table className="data-table">
                {/* SỬA LỖI: Thẻ <tr> bắt đầu ngay sau <thead>
                  để loại bỏ "whitespace text node"
                */}
                <thead><tr>
                    <th>Patient ID</th>
                    <th>Full Name</th>
                    <th>Email</th>
                    <th>Telephone</th>
                    <th>Status</th>
                    <th>Action</th>
                </tr></thead>

                {/* SỬA LỖI: Vòng lặp {patients.map...}
                  bắt đầu ngay sau <tbody>
                */}
                <tbody>{patients.map((patient) => (
                    <tr key={patient.patientId}>
                        <td className="table-td-code">{patient.patientCode}</td>
                        <td>
                            <Link
                                to={`/patient/${patient.patientId}`}
                                className="table-action-link"
                            >
                                {patient.fullName}
                            </Link>
                        </td>
                        <td className="table-td-muted">{patient.email}</td>
                        <td className="table-td-muted">{patient.phoneNumber}</td>
                        <td>
                            {patient.isActive ? (
                                <span className="status-badge status-active">
                                    <CheckCircle size={16} /> Under Treatment
                                </span>
                            ) : (
                                <span className="status-badge status-inactive">
                                    <XCircle size={16} /> Discharged from Hospital
                                </span>
                            )}
                        </td>
                        <td className="table-actions">

                            <button
                                onClick={() => onEdit(patient)}
                                className="btn-icon-text btn-icon-edit"
                                title="Chỉnh sửa"
                            >
                                <Edit size={18} />
                            </button>

                            <button
                                onClick={() => onDelete(patient)}
                                className="btn-icon-text btn-icon-delete"
                                title="Xóa"
                            >
                                <Trash2 size={18} />
                            </button>)
                        </td>
                    </tr>
                ))}</tbody>
            </table>
        </div>
    );
};

export default PatientList;