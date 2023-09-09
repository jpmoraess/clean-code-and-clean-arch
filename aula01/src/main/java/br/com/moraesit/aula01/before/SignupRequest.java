package br.com.moraesit.aula01.before;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignupRequest {
	@NotEmpty
	private String name;
	@NotEmpty
	private String cpf;
	@NotEmpty
	private String email;
	@NotEmpty
	private String carPlate;
	private boolean isPassenger;
	private boolean isDriver;
}
