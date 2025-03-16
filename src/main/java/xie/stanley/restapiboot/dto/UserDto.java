package xie.stanley.restapiboot.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import xie.stanley.restapiboot.model.UserType;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Getter
@Setter
public class UserDto {
	@NotNull(message = "nama tidak boleh kosong")
	@Size(min = 3, message = "nama tidak boleh kurang dari 3 huruf")
	private String name;

	@JsonFormat(pattern = "yyyy-MM-dd")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@NotNull(message = "tanggal lahir tidak boleh kosong")
	private LocalDate birthDate;

	@NotNull(message = "alamat tidak boleh kosong")
	private String address;

	@NotNull(message = "tipe user tidak boleh kosong dan hanya boleh value: [LENDER, BORROWER]")
	private UserType userType;
}
