package DomainLayer.Store.LogicalConditions;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import DomainLayer.Store.PoliciesRulesLogicalConditions.CondRule;
import DomainLayer.Store.PoliciesRulesLogicalConditions.Rule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CondRuleTest {

    private Rule<Object, Object> rule1;
    private Rule<Object, Object> rule2;
    private CondRule<Object, Object> condRule;

    @BeforeEach
    void setUp() {
        rule1 = mock(Rule.class);
        rule2 = mock(Rule.class);
        condRule = new CondRule<>(rule1, rule2);
    }

    @Test
    void checkRule_BothRulesTrue_ReturnsTrue() {
        // Arrange
        when(rule1.checkRule(any(), any())).thenReturn(true);
        when(rule2.checkRule(any(), any())).thenReturn(true);

        // Act
        boolean result = condRule.checkRule(new Object(), new Object());

        // Assert
        assertTrue(result);
    }

    @Test
    void checkRule_FirstRuleFalse_ReturnsFalse() {
        // Arrange
        when(rule1.checkRule(any(), any())).thenReturn(false);

        // Act
        boolean result = condRule.checkRule(new Object(), new Object());

        // Assert
        assertFalse(result);
        // Verify that rule2 is not checked if rule1 is false
        verify(rule2, never()).checkRule(any(), any());
    }

    @Test
    void checkRule_FirstRuleTrueSecondRuleFalse_ReturnsFalse() {
        // Arrange
        when(rule1.checkRule(any(), any())).thenReturn(true);
        when(rule2.checkRule(any(), any())).thenReturn(false);

        // Act
        boolean result = condRule.checkRule(new Object(), new Object());

        // Assert
        assertFalse(result);
    }

    @Test
    void getDescription_ReturnsCombinedDescription() {
        // Arrange
        when(rule1.getDescription()).thenReturn("Rule 1");
        when(rule2.getDescription()).thenReturn("Rule 2");

        // Act
        String description = condRule.getDescription();

        // Assert
        assertEquals("Rule 2 only if Rule 1", description);
    }
}
