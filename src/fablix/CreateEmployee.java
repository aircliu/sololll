package fablix;

import org.jasypt.util.password.StrongPasswordEncryptor;

public class CreateEmployee {
    public static void main(String[] args) {
        // Create a new password encryptor
        StrongPasswordEncryptor encryptor = new StrongPasswordEncryptor();
        
        // Encrypt the password "classta"
        String encryptedPassword = encryptor.encryptPassword("classta");
        
        // Generate the SQL INSERT statement
        System.out.println("Execute this SQL command:");
        System.out.println("USE moviedb;");
        System.out.println("INSERT INTO employees (email, password, fullname) VALUES ('classta@email.edu', '" + encryptedPassword + "', 'TA CS122B');");
    }
}