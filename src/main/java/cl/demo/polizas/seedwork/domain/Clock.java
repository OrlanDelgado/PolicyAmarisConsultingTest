package cl.demo.polizas.seedwork.domain;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * Port interface for accessing current date and time. This abstraction allows for easier testing by providing a way to
 * mock time.
 */
public interface Clock {

    /**
     * Returns the current instant.
     * 
     * @return The current instant
     */
    Instant now();

    /**
     * Returns the current local date in the system default time zone.
     * 
     * @return The current local date
     */
    default LocalDate today() {
        return now().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    /**
     * Returns the current local date time in the system default time zone.
     * 
     * @return The current local date time
     */
    default LocalDateTime nowLocal() {
        return now().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    /**
     * Returns the current local date in the specified time zone.
     * 
     * @param zoneId The time zone ID
     * @return The current local date in the specified time zone
     */
    default LocalDate today(ZoneId zoneId) {
        return now().atZone(zoneId).toLocalDate();
    }

    /**
     * Returns the current local date time in the specified time zone.
     * 
     * @param zoneId The time zone ID
     * @return The current local date time in the specified time zone
     */
    default LocalDateTime nowLocal(ZoneId zoneId) {
        return now().atZone(zoneId).toLocalDateTime();
    }

    /**
     * Default implementation that uses the system clock.
     */
    Clock SYSTEM = new Clock() {
        @Override
        public Instant now() {
            return Instant.now();
        }
    };
}
