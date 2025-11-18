import React, {useState} from 'react';
import {useNavigate} from 'react-router-dom';
import {api_createUser} from '../../../../api_config/CRUD_User/api_createUser.jsx';
import {
    UserPlus,
    Mail,
    User,
    Phone,
    Calendar,
    Users,
    MapPin,
    Loader2
} from 'lucide-react';
import './CreateUser.css';

export default function CreateUser() {
    const navigate = useNavigate();

    const [formData, setFormData] = useState({
        email: '',
        firstName: '',
        lastName: '',
        gender: 'MALE',
        phoneNumber: '',
        address: '',
        dateOfBirth: ''
    });

    const [loading, setLoading] = useState(false);
    const [message, setMessage] = useState(null);

    const handleChange = (e) => {
        const {name, value} = e.target;
        setFormData(prev => ({
            ...prev,
            [name]: value
        }));
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setLoading(true);
        setMessage(null);

        try {

            const response = await api_createUser(
                formData.email,
                formData.firstName,
                formData.lastName,
                formData.gender,
                formData.phoneNumber,
                formData.address,
                formData.dateOfBirth
            );

            const createdUser = response.data?.result || response.data;

            setMessage({
                type: 'success',
                text: `User created successfully!`
            });

            setTimeout(() => {
                navigate('/users');
            }, 1500);

        } catch (error) {
            const errorText = error.response?.data?.message || error.message || "An unknown error has occurred.";
            setMessage({type: 'error', text: errorText});
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="create-user-page-wrapper">
            <div className="create-user-container">
                <div className="create-user-header">
                    <div className="create-user-icon-wrapper">
                        <UserPlus className="create-user-icon" size={32}/>
                    </div>
                    <h2>Create New User</h2>
                </div>
                {message && (
                    <p
                        className={`message ${message.type === 'success' ? 'success' : 'error'
                        }`}
                        role="status"
                    >
                        {message.text}
                    </p>
                )}

                <form onSubmit={handleSubmit} noValidate>
                    <div className="form-row">
                        <div className="form-group">
                            <label>
                                <User size={16}/>
                                Last Name
                            </label>
                            <input
                                type="text"
                                name="firstName"
                                value={formData.firstName}
                                onChange={handleChange}
                                placeholder="Nguyễn"
                                required
                            />
                        </div>
                        <div className="form-group">
                            <label>
                                <User size={16}/>
                                First Name
                            </label>
                            <input
                                type="text"
                                name="lastName"
                                value={formData.lastName}
                                onChange={handleChange}
                                placeholder="Văn A"
                                required
                            />
                        </div>
                    </div>

                    <div className="form-group">
                        <label>
                            <Mail size={16}/>
                            Email
                        </label>
                        <input
                            type="email"
                            name="email"
                            value={formData.email}
                            onChange={handleChange}
                            placeholder="nguyenvana@example.com"
                            required
                        />
                    </div>

                    <div className="form-row">
                        <div className="form-group">
                            <label>
                                <Phone size={16}/>
                                Phone Number
                            </label>
                            <input
                                type="tel"
                                name="phoneNumber"
                                value={formData.phoneNumber}
                                onChange={handleChange}
                                placeholder="0123456789"
                                required
                            />
                        </div>
                        <div className="form-group">
                            <label>
                                <Calendar size={16}/>
                                Date of Birth
                            </label>
                            <input
                                type="date"
                                name="dateOfBirth"
                                value={formData.dateOfBirth}
                                onChange={handleChange}
                                required
                            />
                        </div>
                    </div>

                    <div className="form-group">
                        <label>
                            <Users size={16}/>
                            Gender
                        </label>
                        <select
                            name="gender"
                            value={formData.gender}
                            onChange={handleChange}
                            required
                        >
                            <option value="MALE">Male</option>
                            <option value="FEMALE">Female</option>
                        </select>
                    </div>

                    <div className="form-group">
                        <label>
                            <MapPin size={16}/>
                            Address
                        </label>
                        <input
                            type="text"
                            name="address"
                            value={formData.address}
                            onChange={handleChange}
                            placeholder="123 ABC Street, District 1, HCMC"
                        />
                    </div>

                    <button
                        type="submit"
                        className={`btn-submit ${loading ? "loading" : ""}`}
                        disabled={loading}
                    >
                        {loading ? (
                            <>
                                <Loader2 className="btn-loader" size={18}/>
                                <span>Creating...</span>
                            </>
                        ) : (
                            <>
                                <UserPlus size={18}/>
                                <span>Create User</span>
                            </>
                        )}
                    </button>
                </form>
            </div>
        </div>
    );
}