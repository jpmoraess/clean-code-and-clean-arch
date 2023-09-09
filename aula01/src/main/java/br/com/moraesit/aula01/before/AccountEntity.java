package br.com.moraesit.aula01.before;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "account")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountEntity {
	@Id
	private UUID accountId;
	private String name;
	private String email;
	private String cpf;
	private String carPlate;
	private boolean isPassenger;
	private boolean isDriver;
	private LocalDateTime date;
	private boolean isVerified;
	private UUID verificationCode;
}
