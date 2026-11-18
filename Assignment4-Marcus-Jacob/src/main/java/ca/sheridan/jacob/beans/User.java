package ca.sheridan.jacob.beans;

import ca.sheridan.jacob.beans.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
	
	//STUDENT NOTE: code borrowed from week 10-1
	private Long    userid;
	@NonNull
	private String  email;
	@NonNull
	private String  encryptedpassword;
	@NonNull
	private Boolean enabled;
}
