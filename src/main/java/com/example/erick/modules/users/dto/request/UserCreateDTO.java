package com.example.erick.modules.users.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserCreateDTO {
    @NotBlank(message = "Username không được để trống")
    @Size(min = 5, message = "Username phải có ít nhất 5 ký tự")
    @Pattern(regexp = "^[a-zA-Z]+$", message = "Username chỉ được chứa chữ cái")
    private String username;

    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không đúng định dạng")
    private String email;

    @NotBlank(message = "Full name không được để trống")
    private String fullName;
}
