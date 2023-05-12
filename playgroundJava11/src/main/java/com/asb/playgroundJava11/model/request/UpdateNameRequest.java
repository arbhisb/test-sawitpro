package com.asb.playgroundJava11.model.request;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class UpdateNameRequest {
    @NotBlank
	private String name;
}
