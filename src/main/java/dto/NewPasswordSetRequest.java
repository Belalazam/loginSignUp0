package dto;


import lombok.Data;

@Data
public class NewPasswordSetRequest {
    private String email;
    private String newPassword;
}
