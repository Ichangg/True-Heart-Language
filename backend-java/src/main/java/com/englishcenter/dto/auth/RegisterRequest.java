package com.englishcenter.dto.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {
    @NotBlank(message = "Tên đăng nhập không được để trống.")
    @Size(min = 3, message = "Tên đăng nhập ít nhất 3 ký tự.")
    private String username;

    @NotBlank(message = "Mật khẩu không được để trống.")
    @Size(min = 6, message = "Mật khẩu ít nhất 6 ký tự.")
    private String password;

    @NotBlank(message = "Họ tên không được để trống.")
    private String fullName;

    private String email;
    private String phone;
    private String role;
    private String address;
    private String dateOfBirth;
}
