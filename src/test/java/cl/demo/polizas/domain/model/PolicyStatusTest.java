// package cl.demo.polizas.domain.model;

// import org.junit.jupiter.api.Test;
// import static org.junit.jupiter.api.Assertions.*;

// class PolicyStatusTest {
// @Test
// void validTransitions() {
// assertTrue(PolicyStatus.EMITIDA.canTransitionTo(PolicyStatus.ACTIVA));
// assertTrue(PolicyStatus.ACTIVA.canTransitionTo(PolicyStatus.ANULADA));
// }

// @Test
// void invalidTransitions() {
// assertFalse(PolicyStatus.EMITIDA.canTransitionTo(PolicyStatus.ANULADA));
// assertFalse(PolicyStatus.ACTIVA.canTransitionTo(PolicyStatus.EMITIDA));
// assertFalse(PolicyStatus.ANULADA.canTransitionTo(PolicyStatus.EMITIDA));
// assertFalse(PolicyStatus.ANULADA.canTransitionTo(PolicyStatus.ACTIVA));
// }
// }
