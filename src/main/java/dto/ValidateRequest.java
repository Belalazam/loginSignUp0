package dto;

import lombok.Data;

@Data
public class ValidateRequest {
    private String email;
    private String otp;
}
