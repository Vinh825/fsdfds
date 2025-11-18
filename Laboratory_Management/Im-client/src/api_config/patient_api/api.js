import axios from 'axios';

// Cấu hình baseURL cho API Spring Boot của bạn
const API_URL = 'http://localhost:8080/api/patients';

const apiClient = axios.create({
    baseURL: API_URL,
    headers: {
        'Content-Type': 'application/json',
    }
});

// Hàm trợ giúp để thêm header X-User-Id (Bạn có thể thay đổi "admin-fe" sau này)
const getAuthHeaders = () => ({
    headers: {
        'X-User-Id': 'admin-fe'
    }
});

// --- Patient API ---

export const getAllPatients = () => apiClient.get('');

export const getActivePatients = () => apiClient.get('active');

export const getPatientById = (id) => apiClient.get(`${id}`);

export const searchPatients = (keyword) => apiClient.get('search', { params: { keyword } });

export const createPatient = (patientData) => {
    return apiClient.post('', patientData, getAuthHeaders());
};

export const updatePatient = (id, patientData) => {
    return apiClient.put(`/${id}`, patientData, getAuthHeaders());
};

export const deletePatient = (id) => {
    return apiClient.delete(`/${id}`, getAuthHeaders());
};
const recordApi = axios.create({
    baseURL: 'http://localhost:8080/api/patient-medical-records',
    headers: {
        'Content-Type': 'application/json',
    }
});

export const getRecordsByPatientId = (patientId) => {
    return recordApi.get(`/patient/${patientId}`);
};

export const createRecord = (recordData) => {
    return recordApi.post('', recordData, getAuthHeaders());
};

export const updateRecord = (id, recordData) => {
    return recordApi.put(`/${id}`, recordData, getAuthHeaders());
};

export const deleteRecord = (id) => {
    return recordApi.delete(`/${id}`, getAuthHeaders());
};