package com.crio.rentread.exchange;

import com.crio.rentread.entity.Role;
import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class UserRegisterRequest {
    @NotBlank(message = "first_name cannot be blank")
    String first_name;
    @NotBlank(message = "last_name cannot be blank")
    String last_name;
    @NotBlank(message = "email cannot be blank")
    String email;
    @NotBlank(message = "password cannot be blank")
    String password;
    @JsonEnumDefaultValue
    Role role = Role.USER;
}
