package com.asb.playgroundJava11.model.request;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class RegisterRequest {
   
    @NotBlank
    @Size(min = 10, max = 13)
    @Pattern(regexp = "^08\\d+", message = "phone number must start with 08")
	private String phoneNumber;

    @NotBlank
    @Size(max = 60)
	private String name;

    @NotBlank
    @Size(min = 6, max = 16)
    @Pattern(regexp = "(?=.*\\d)(?=.*[A-Z])\\w+", message = "password must atleast 1 number and 1 uppercase")
	private String password;
}
