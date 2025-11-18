import React from 'react';
import { AlertTriangle } from 'lucide-react';
import "../../css/style.css"
const ConfirmationModal = ({ isOpen, onClose, onConfirm, title, message }) => {
    if (!isOpen) return null;

    return (
        <div className="modal-overlay">
            <div className="modal-content modal-content-confirm">
                <div className="modal-body">
                    <div className="confirm-body">
                        <div className="confirm-icon-wrapper">
                            <AlertTriangle aria-hidden="true" />
                        </div>
                        <div className="confirm-text-wrapper">
                            <h3 className="modal-title" id="modal-title">
                                {title}
                            </h3>
                            <div className="confirm-message">
                                <p>
                                    {message}
                                </p>
                            </div>
                        </div>
                    </div>
                </div>
                <div className="modal-footer confirm-footer">
                    <button
                        type="button"
                        className="btn btn-danger"
                        onClick={onConfirm}
                    >
                        Đồng ý
                    </button>
                    <button
                        type="button"
                        className="btn btn-secondary"
                        onClick={onClose}
                    >
                        Hủy
                    </button>
                </div>
            </div>
        </div>
    );
};

export default ConfirmationModal;