/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package invoicingapp_monolithic;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 *
 * @author frede
 */
public class Password {
    
    private static final int ITERATIONS = 65536;
    private static final int KEY_LENGTH = 256;
    private static final String ALGORITHM = "PBKDF2WithHmacSHA512";
    
    private String password;
    private String hashed;
    
    public Password(){}
    
    public Password(String password){
        this.password=password;
        try {
            this.hashed=hashPassword(password);
        } catch (Exception ex) {
            Logger.getLogger(Password.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // Genera una sal segura
    private static byte[] getSalt() throws Exception {
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[16];
        sr.nextBytes(salt);
        return salt;
    }

    // Genera un hash de la contraseña
    public static String hashPassword(String password) throws Exception {
        byte[] salt = getSalt();
        PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, ITERATIONS, KEY_LENGTH);
        SecretKeyFactory skf = SecretKeyFactory.getInstance(ALGORITHM);
        byte[] hash = skf.generateSecret(spec).getEncoded();
        return Base64.getEncoder().encodeToString(salt) + "$" + Base64.getEncoder().encodeToString(hash);
    }

    // Verifica si la contraseña coincide con el hash
    public static boolean checkPassword(String password, String stored) throws Exception {
        String[] parts = stored.split("\\$");
        byte[] salt = Base64.getDecoder().decode(parts[0]);
        byte[] hash = Base64.getDecoder().decode(parts[1]);
        PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, ITERATIONS, KEY_LENGTH);
        SecretKeyFactory skf = SecretKeyFactory.getInstance(ALGORITHM);
        byte[] testHash = skf.generateSecret(spec).getEncoded();
        return java.util.Arrays.equals(hash, testHash);
    }

    /**
     * @return the hashed
     */
    public String getHashed() {
        return hashed;
    }
    
    public void setHashed(String hashed){
        this.hashed=hashed;
    }
    
}
