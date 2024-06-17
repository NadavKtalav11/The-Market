package DomainLayer.Store.LogicalConditions;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import DomainLayer.Store.PoliciesRulesLogicalConditions.Rule;
import DomainLayer.Store.PoliciesRulesLogicalConditions.XorRule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class XorRuleTest {

    private Rule<Object, Object> rule1;
    private Rule<Object, Object> rule2;
    private XorRule<Object, Object> xorRule;

    @BeforeEach
    void setUp() {
        rule1 = mock(Rule.class);
        rule2 = mock(Rule.class);
        xorRule = new XorRule<>(rule1, rule2);
    }

    @Test
    void checkRule_BothRulesTrue_ReturnsFalse() {
        // Arrange
        when(rule1.checkRule(any(), any())).thenReturn(true);
        when(rule2.checkRule(any(), any())).thenReturn(true);

        // Act
        boolean result = xorRule.checkRule(new Object(), new Object());

        // Assert
        assertFalse(result);
    }

    @Test
    void checkRule_FirstRuleTrueSecondRuleFalse_ReturnsTrue() {
        // Arrange
        when(rule1.checkRule(any(), any())).thenReturn(true);
        when(rule2.checkRule(any(), any())).thenReturn(false);

        // Act
        boolean result = xorRule.checkRule(new Object(), new Object());

        // Assert
        assertTrue(result);
    }

    @Test
    void checkRule_FirstRuleFalseSecondRuleTrue_ReturnsTrue() {
        // Arrange
        when(rule1.checkRule(any(), any())).thenReturn(false);
        when(rule2.checkRule(any(), any())).thenReturn(true);

        // Act
        boolean result = xorRule.checkRule(new Object(), new Object());

        // Assert
        assertTrue(result);
    }

    @Test
    void checkRule_BothRulesFalse_ReturnsFalse() {
        // Arrange
        when(rule1.checkRule(any(), any())).thenReturn(false);
        when(rule2.checkRule(any(), any())).thenReturn(false);

        // Act
        boolean result = xorRule.checkRule(new Object(), new Object());

        // Assert
        assertFalse(result);
    }

    @Test
    void getDescription_ReturnsCombinedDescription() {
        // Arrange
        when(rule1.getDescription()).thenReturn("Rule 1");
        when(rule2.getDescription()).thenReturn("Rule 2");

        // Act
        String description = xorRule.getDescription();

        // Assert
        assertEquals(" (Rule 1 xor Rule 2) ", description);
    }
}

