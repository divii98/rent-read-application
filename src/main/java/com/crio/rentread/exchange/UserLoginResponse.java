package com.crio.rentread.exchange;

import com.crio.rentread.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
public class UserLoginResponse {
    String message;
    User userDetails;
}
