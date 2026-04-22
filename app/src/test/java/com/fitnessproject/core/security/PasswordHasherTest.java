package com.fitnessproject.core.security;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

public class PasswordHasherTest {

    @Test
    public void testSaltGeneration_ProducesUniqueSalts() {
        String salt1 = PasswordHasher.generateSalt();
        String salt2 = PasswordHasher.generateSalt();

        assertNotNull(salt1);
        assertNotNull(salt2);
        assertNotEquals("Generated salts should be unique randomly", salt1, salt2);
    }

    @Test
    public void testHashPassword_GivenSamePasswordAndSalt_ProducesSameHash() {
        String password = "securePassword123";
        String salt = PasswordHasher.generateSalt();

        String hash1 = PasswordHasher.hashPassword(password, salt);
        String hash2 = PasswordHasher.hashPassword(password, salt);

        assertEquals("Same password and salt must yield identical hash for verification to work", hash1, hash2);
    }

    @Test
    public void testHashPassword_GivenDifferentPasswords_ProducesDifferentHash() {
        String salt = PasswordHasher.generateSalt();

        String hash1 = PasswordHasher.hashPassword("securePassword123", salt);
        String hash2 = PasswordHasher.hashPassword("differentPassword", salt);

        assertNotEquals("Different passwords must yield different hashes", hash1, hash2);
    }

    @Test
    public void testHashPassword_GivenDifferentSalts_ProducesDifferentHash() {
        String password = "securePassword123";
        String salt1 = PasswordHasher.generateSalt();
        String salt2 = PasswordHasher.generateSalt();

        String hash1 = PasswordHasher.hashPassword(password, salt1);
        String hash2 = PasswordHasher.hashPassword(password, salt2);

        assertNotEquals("Different salts must yield different hashes even if password is identical", hash1, hash2);
    }
}

