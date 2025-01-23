package bg.sofia.uni.fmi.mjt.access;

import bg.sofia.uni.fmi.mjt.exceptions.FailedAccountOperation;
import bg.sofia.uni.fmi.mjt.exceptions.FailedRegistrationException;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Optional;
import java.util.stream.Stream;

public abstract class AccountAccess {

    private static final String HASH_ALGORITHM = "MD5";
    private static final int SALT_SIZE = 8;
    private static final String ACCOUNTS_FILE = "./accounts.txt";
    private static final int PARSE_INT_RADIX = 16;

    protected InputStream openAccountsFileForReading() throws FailedAccountOperation {
        try {
            return new FileInputStream(ACCOUNTS_FILE);
        } catch (IOException e) {
            throw new FailedAccountOperation("Internal problem while creating a registration", e);
        }
    }

    protected OutputStream openAccountsFileForWriting() throws FailedAccountOperation {
        try {
            return new FileOutputStream(ACCOUNTS_FILE, true);
        } catch (IOException e) {
            throw new FailedAccountOperation("Internal problem while creating a registration", e);
        }
    }

    protected Optional<StoredUser> doesUserExist(InputStream inputStream, User user)
        throws FailedRegistrationException {
        try (InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
             BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {
            Stream<String> content = bufferedReader.lines();
            return doesUserExist(content, user);

        } catch (IOException e) {
            throw new FailedRegistrationException("Internal problem while creating reservation", e);
        }
    }

    protected SaltHashContainer hashPassword(String password) throws NoSuchAlgorithmException {
        byte[] salt = prepareSalt();
        String convertedSalt = convertByteHashToString(salt);
        String hash = hashPassword(password, convertedSalt);
        return new SaltHashContainer(hash, convertedSalt);
    }

    protected String hashPassword(String password, String salt) throws NoSuchAlgorithmException {
        byte[] bytes = convertHexStringToByteArray(salt);
        MessageDigest messageDigest = MessageDigest.getInstance(HASH_ALGORITHM);
        messageDigest.update(bytes);
        return convertByteHashToString(messageDigest.digest(password.getBytes(StandardCharsets.UTF_8)));
    }

    private byte[] prepareSalt() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] salt = new byte[SALT_SIZE];
        secureRandom.nextBytes(salt);
        return salt;
    }

    private String convertByteHashToString(byte[] hash) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            hexString.append(String.format("%02X", b));
        }
        return hexString.toString();
    }

    private byte[] convertHexStringToByteArray(String hexString) {
        if (hexString.length() % 2 != 0) {
            throw new IllegalArgumentException("Hexadecimal string must have an even length.");
        }

        byte[] byteArray = new byte[hexString.length() / 2];
        for (int i = 0; i < hexString.length(); i += 2) {
            String hexPair = hexString.substring(i, i + 2);
            byteArray[i / 2] = (byte) Integer.parseInt(hexPair, PARSE_INT_RADIX);
        }
        return byteArray;
    }

    private Optional<StoredUser> doesUserExist(Stream<String> fileContent, User toCheck) {

        return fileContent
            .map(StoredUser::of)
            .filter(user -> user.equals(toCheck))
            .findFirst();
    }

}
