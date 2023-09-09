package br.com.moraesit.aula01.before;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class AccountService {
	private final AccountJpaRepository accountRepository;

	public AccountService(AccountJpaRepository accountRepository) {
		this.accountRepository = accountRepository;
	}

	public void sendEmail(String email, String subject, String message) {
		log.info("Send e-mail: {}, subject: {} and message: {}", email, subject, message);
	}

	public boolean validateCpf(String str) {
		if (str != null) {
			if (!str.isEmpty()) {
				if (str.length() >= 11 && str.length() <= 14) {

					str = str.replace(".", "")
							.replace(".", "")
							.replace("-", "")
							.replace(" ", "");

					String finalStr = str;
					if (!str.chars().allMatch(c -> c == finalStr.charAt(0))) {
						try {
							int d1, d2;
							int dg1, dg2, rest;
							int digito;
							int nDigResult;
							d1 = d2 = 0;
							dg1 = dg2 = rest = 0;

							for (int nCount = 1; nCount < str.length() - 1; nCount++) {
								digito = Integer.parseInt(str.substring(nCount - 1, nCount));
								d1 = d1 + (11 - nCount) * digito;
								d2 = d2 + (12 - nCount) * digito;
							}

							rest = (d1 % 11);
							dg1 = (rest < 2) ? 0 : 11 - rest;
							d2 += 2 * dg1;
							rest = (d2 % 11);
							if (rest < 2)
								dg2 = 0;
							else
								dg2 = 11 - rest;

							String nDigVerific = str.substring(str.length() - 2);
							nDigResult = Integer.parseInt("" + dg1 + dg2);
							return nDigVerific.equals(String.valueOf(nDigResult));
						} catch (Exception e) {
							System.err.println("Erro !" + e);
							return false;
						}
					} else {
						return false;
					}
				} else {
					return false;
				}
			}
		}
		return false;
	}

	@Transactional
	public SignupResponse signup(SignupRequest request) {
		UUID accountId = UUID.randomUUID();
		UUID verificationCode = UUID.randomUUID();
		LocalDateTime date = LocalDateTime.now();

		Optional<AccountEntity> existingAccount = accountRepository.findByEmail(request.getEmail());

		if (existingAccount.isEmpty()) {
			if (request.getName().matches("[a-zA-Z]+ [a-zA-Z]+")) {
				if (request.getEmail().matches("^(.+)@(.+)$")) {
					if (validateCpf(request.getCpf())) {
						AccountEntity accountEntity = AccountEntity.builder()
								.accountId(accountId)
								.name(request.getName())
								.email(request.getEmail())
								.cpf(request.getCpf())
								.carPlate(request.getCarPlate())
								.isPassenger(request.isPassenger())
								.isDriver(request.isDriver())
								.date(date)
								.isVerified(Boolean.FALSE)
								.verificationCode(verificationCode)
								.build();

						accountRepository.save(accountEntity);

						sendEmail(request.getEmail(), "Verification", "Please verify your code at first login " + verificationCode);

						return new SignupResponse(accountId);
					} else {
						throw new IllegalArgumentException("Invalid cpf");
					}
				} else {
					throw new IllegalArgumentException("Invalid email");
				}
			} else {
				throw new IllegalArgumentException("Invalid name");
			}
		} else {
			throw new IllegalArgumentException("Account already exists");
		}
	}

	public AccountEntity getAccount(UUID accountId) {
		return accountRepository.findById(accountId)
				.orElseThrow(() -> new RuntimeException("Account not found."));
	}
}
