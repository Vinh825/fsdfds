package com.example.IAM.repository;

import com.example.IAM.entity.ForgetPassword;
import org.springframework.data.repository.CrudRepository;

public interface ForgotOtpRepository extends CrudRepository<ForgetPassword, String> {
}
