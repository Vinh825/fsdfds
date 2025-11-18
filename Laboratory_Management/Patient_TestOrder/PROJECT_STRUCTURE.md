# Project Structure - Patient Service

## Overview
Spring Boot Patient Service với cấu trúc giống IAM service và database PostgreSQL.

## Cấu trúc thư mục
```
patient/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/healthcare/patient/
│   │   │       ├── PatientApplication.java           # Main application class
│   │   │       ├── controller/                       # REST Controllers
│   │   │       │   ├── PatientController.java
│   │   │       │   └── PatientMedicalRecordController.java
│   │   │       ├── service/                          # Business Logic
│   │   │       │   ├── PatientService.java
│   │   │       │   └── PatientMedicalRecordService.java
│   │   │       ├── repository/                       # Data Access Layer
│   │   │       │   ├── PatientRepository.java
│   │   │       │   ├── PatientMedicalRecordRepository.java
│   │   │       │   └── PatientMedicalRecordVersionRepository.java
│   │   │       ├── entity/                           # JPA Entities
│   │   │       │   ├── Patient.java
│   │   │       │   ├── PatientMedicalRecord.java
│   │   │       │   └── PatientMedicalRecordVersion.java
│   │   │       ├── dto/                              # Data Transfer Objects
│   │   │       │   ├── PatientRequestDTO.java
│   │   │       │   ├── PatientResponseDTO.java
│   │   │       │   ├── PatientMedicalRecordRequestDTO.java
│   │   │       │   └── PatientMedicalRecordResponseDTO.java
│   │   │       ├── utils/                            # Utility Classes
│   │   │       │   └── ResponseUtil.java
│   │   │       └── common/                           # Common Classes
│   │   │           ├── ApiResponse.java
│   │   │           └── GlobalExceptionHandler.java
│   │   └── resources/
│   │       ├── application.properties                 # Application configuration
│   │       └── db-schema.sql                         # Database schema
│   └── test/
│       └── java/                                      # Test files (created by Spring Boot)
├── pom.xml                                            # Maven configuration
├── .gitental                                            # Git ignore
├── README.md                                          # Project documentation
└── PROJECT_STRUCTURE.md                              # This file
```

## Entities

### 1. Patient
- **Bảng**: `patients`
- **Fields**: patientId, patientCode, fullName, dateOfBirth, gender, address, phoneNumber, email, identityNumber, emergencyContactName, emergencyContactPhone, isActive, isDeleted, timestamps
- **Relationships**: One-to-Many với PatientMedicalRecord

### 2. PatientMedicalRecord
- **Bảng**: `patient_medical_records`
- **Fields**: recordId, patient (FK), recordType, medicalHistory, currentMedications, allergies, clinicalNotes, lastTestDate, patientAgeAtRecord, patientSnapshotData, recordDate, versionNumber, isCurrent, isDeleted, timestamps
- **Relationships**: Many-to-One với Patient, One-to-Many với PatientMedicalRecordVersion

### 3. PatientMedicalRecordVersion
- **Bảng**: `patient_medical_record_versions`
- **Fields**: versionId, patientMedicalRecord (MENT&tan medicalRecordId, patient (FK), all record fields, versionNumber, changeReason, timestamps
- **Relationships**: Many-to-One với PatientMedicalRecord, Many-to-One với Patient

## API Endpoints

### Patient APIs
- POST `/api/patients` - Tạo patient mới
- GET `/api/patients` - Lấy tất cả patients
- GET `/api/patients/{id}` - Lấy patient theo ID
- GET `/api/patients/code/{code}` - Lấy patient theo code
- GET `/api/patients/active` - Lấy active patients
- GET `/api/patients/search?keyword={keyword}` - Tìm kiếm patients
- PUT `/api/patients/{id}` - Cập nhật patient
- DELETE `/api/patients/{id}` - Xóa patient (soft delete)

### Patient Medical Record APIs
- POST `/api/patient-medical-records` - Tạo medical record mới
- GET `/api/patient-medical-records/{id}` - Lấy record theo ID
- GET `/api/patient-medical-records/patient/{patientId}` - Lấy tất cả records của patient
- GET `/api/patient-medical-records/patient/{patientId}/current` - Lấy current records
- PUT `/api/patient-medical-records/{id}` - Cập nhật record
- DELETE `/api/patient-medical-records/{id}` - Xóa record (soft delete)

## Dependencies (pom.xml)
- Spring Boot 3.2.0
- Spring Web
- Spring Data JPA
- PostgreSQL Driver
- Spring Validation
- Lombok
- Spring Actuator
- Spring DevTools

## Đặc điểm chính
1. **Version Control**: Hệ thống versioning tự động cho medical records
2. **Soft Delete**: Tất cả delete operations đều là soft delete
3. **Audit Trail**: Tracking createdBy, updatedBy cho audit // chưa xong
4. **Validation**: Request validation với Java Bean Validation
5. **Error Handling**: Global exception handler
6. **Search**: Full-text search cho patients
7. **Pagination**: Ready for pagination implementation

## Cách chạy
1. Cài đặt PostgreSQL
2. Tạo database `Laboratory`
3. Chạy script `db-schema.sql`
4. Update database credentials trong `application.properties`
5. Chạy lệnh: `mvn spring-boot:run`

## Notes
- Tất cả entity classes dùng Lombok để giảm boilerplate code
- JSON data types được hỗ trợ cho patient_snapshot_data
- Timestamps tự động update với @CreationTimestamp và @UpdateTimestamp
- Service layer dùng @Transactional cho data consistency


