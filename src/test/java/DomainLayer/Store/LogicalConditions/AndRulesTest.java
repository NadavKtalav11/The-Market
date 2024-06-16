package DomainLayer.Store.LogicalConditions;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import DomainLayer.Store.PoliciesRulesLogicalConditions.AndRule;
import DomainLayer.Store.PoliciesRulesLogicalConditions.Rule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AndRulesTest {

    private Rule<Object, Object> rule1;
    private Rule<Object, Object> rule2;
    private AndRule<Object, Object> andRule;

    @BeforeEach
    void setUp() {
        rule1 = mock(Rule.class);
        rule2 = mock(Rule.class);
        andRule = new AndRule<>(rule1, rule2);
    }

    @Test
    void checkRule_BothRulesTrue_ReturnsTrue() {
        // Arrange
        when(rule1.checkRule(any(), any())).thenReturn(true);
        when(rule2.checkRule(any(), any())).thenReturn(true);

        // Act
        boolean result = andRule.checkRule(new Object(), new Object());

        // Assert
        assertTrue(result);
    }

    @Test
    void checkRule_FirstRuleFalse_ReturnsFalse() {
        // Arrange
        when(rule1.checkRule(any(), any())).thenReturn(false);
        when(rule2.checkRule(any(), any())).thenReturn(true);

        // Act
        boolean result = andRule.checkRule(new Object(), new Object());

        // Assert
        assertFalse(result);
    }

    @Test
    void checkRule_SecondRuleFalse_ReturnsFalse() {
        // Arrange
        when(rule1.checkRule(any(), any())).thenReturn(true);
        when(rule2.checkRule(any(), any())).thenReturn(false);

        // Act
        boolean result = andRule.checkRule(new Object(), new Object());

        // Assert
        assertFalse(result);
    }

    @Test
    void checkRule_BothRulesFalse_ReturnsFalse() {
        // Arrange
        when(rule1.checkRule(any(), any())).thenReturn(false);
        when(rule2.checkRule(any(), any())).thenReturn(false);

        // Act
        boolean result = andRule.checkRule(new Object(), new Object());

        // Assert
        assertFalse(result);
    }

    @Test
    void getDescription_ReturnsCombinedDescription() {
        // Arrange
        when(rule1.getDescription()).thenReturn("Rule 1");
        when(rule2.getDescription()).thenReturn("Rule 2");

        // Act
        String description = andRule.getDescription();

        // Assert
        assertEquals("Rule 1 and Rule 2", description);
    }
}