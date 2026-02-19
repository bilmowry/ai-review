import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.sql.Connection;
import java.sql.DriverManager;

public class VulnerableApp {

    // ISSUE 1: Hardcoded AWS Credentials
    // Testing tools should flag these strings as high-risk secrets.
    private static final String AWS_ACCESS_KEY = "AKIAEXAMPLE123456789";
    private static final String AWS_SECRET_KEY = "wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY";

    // ISSUE 2: Hardcoded Database Credentials
    private static final String DB_URL = "jdbc:mysql://localhost:3306/users_db";
    private static final String DB_USER = "admin";
    private static final String DB_PASS = "P@ssw0rd123!"; 

    public void connectToDatabase() {
        try {
            // Passing hardcoded credentials directly into a connection string
            Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
            System.out.println("Connected successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void encryptData(String data) {
        try {
            // ISSUE 3: Hardcoded Encryption Key
            // Keys should be pulled from a Key Management Service (KMS) or Environment Variable.
            String secret = "my_super_secret_key"; 
            SecretKeySpec keySpec = new SecretKeySpec(secret.getBytes(), "AES");

            // ISSUE 4: Insecure/Weak Algorithm
            // Using AES with "ECB" mode is insecure as it doesn't use an Initialization Vector (IV).
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, keySpec);
            
            byte[] encrypted = cipher.doFinal(data.getBytes());
            System.out.println("Encrypted data: " + new String(encrypted));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        VulnerableApp app = new VulnerableApp();
        app.encryptData("Sensitive User Info");
    }
}
