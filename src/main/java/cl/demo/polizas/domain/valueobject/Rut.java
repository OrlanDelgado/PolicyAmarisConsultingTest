package cl.demo.polizas.domain.valueobject;

import cl.demo.polizas.seedwork.domain.Guard;
import cl.demo.polizas.seedwork.domain.ValueObject;

import java.util.regex.Pattern;

/**
 * Value Object representing a Chilean RUT (Rol Único Tributario).
 * Immutable and validated RUT with verification digit calculation.
 */
public final class Rut extends ValueObject {

    private static final Pattern RUT_PATTERN = Pattern.compile("^\\d{1,8}-[\\dkK]$");
    private static final int[] VERIFICATION_WEIGHTS = {2, 3, 4, 5, 6, 7, 2, 3};

    private final String number;
    private final String verificationDigit;
    private final String fullRut;

    private Rut(String number, String verificationDigit) {
        this.number = number;
        this.verificationDigit = verificationDigit;
        this.fullRut = number + "-" + verificationDigit;
        validate();
    }

    /**
     * Creates a RUT from a full RUT string (e.g., "12345678-9").
     */
    public static Rut from(String rut) {
        Guard.notNullOrEmpty(rut, "RUT cannot be null or empty");
        
        String cleanRut = rut.replaceAll("[.-]", "").toUpperCase();
        if (cleanRut.length() < 2) {
            throw new IllegalArgumentException("RUT must have at least 2 characters");
        }
        
        String number = cleanRut.substring(0, cleanRut.length() - 1);
        String verificationDigit = cleanRut.substring(cleanRut.length() - 1);
        
        return new Rut(number, verificationDigit);
    }

    /**
     * Creates a RUT from number and verification digit.
     */
    public static Rut from(String number, String verificationDigit) {
        Guard.notNullOrEmpty(number, "RUT number cannot be null or empty");
        Guard.notNullOrEmpty(verificationDigit, "Verification digit cannot be null or empty");
        
        return new Rut(number, verificationDigit.toUpperCase());
    }

    /**
     * Gets the RUT number part.
     */
    public String getNumber() {
        return number;
    }

    /**
     * Gets the verification digit.
     */
    public String getVerificationDigit() {
        return verificationDigit;
    }

    /**
     * Gets the full RUT with format.
     */
    public String getFullRut() {
        return fullRut;
    }

    /**
     * Gets the RUT without formatting.
     */
    public String getCleanRut() {
        return number + verificationDigit;
    }

    /**
     * Validates the RUT format and verification digit.
     */
    @Override
    protected void validate() {
        // Validate format
        if (!RUT_PATTERN.matcher(fullRut).matches()) {
            throw new IllegalArgumentException("Invalid RUT format: " + fullRut);
        }

        // Validate number part
        if (number.length() < 1 || number.length() > 8) {
            throw new IllegalArgumentException("RUT number must be between 1 and 8 digits");
        }

        // Validate verification digit
        String calculatedDigit = calculateVerificationDigit(number);
        if (!calculatedDigit.equals(verificationDigit)) {
            throw new IllegalArgumentException("Invalid verification digit. Expected: " + 
                                           calculatedDigit + ", Got: " + verificationDigit);
        }
    }

    /**
     * Calculates the verification digit for a given RUT number.
     */
    private String calculateVerificationDigit(String rutNumber) {
        int sum = 0;
        int weightIndex = 0;
        
        // Reverse the number for calculation
        for (int i = rutNumber.length() - 1; i >= 0; i--) {
            int digit = Character.getNumericValue(rutNumber.charAt(i));
            sum += digit * VERIFICATION_WEIGHTS[weightIndex % VERIFICATION_WEIGHTS.length];
            weightIndex++;
        }
        
        int remainder = sum % 11;
        int verificationDigit = 11 - remainder;
        
        if (verificationDigit == 11) {
            return "0";
        } else if (verificationDigit == 10) {
            return "K";
        } else {
            return String.valueOf(verificationDigit);
        }
    }

    /**
     * Checks if the RUT is valid.
     */
    public boolean isValid() {
        try {
            validate();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Rut rut = (Rut) obj;
        return fullRut.equals(rut.fullRut);
    }

    @Override
    public int hashCode() {
        return fullRut.hashCode();
    }

    @Override
    public String toString() {
        return fullRut;
    }
}
