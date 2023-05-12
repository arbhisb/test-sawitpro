package com.asb.playgroundJava11.model.request;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class LoginRequest {
    @NotBlank
	private String phoneNumber;

    @NotBlank
	private String password;
}
