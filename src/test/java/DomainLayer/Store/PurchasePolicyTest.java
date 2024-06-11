package DomainLayer.Store;

import DomainLayer.Store.PurchasePolicy;
import Util.ProductDTO;
import Util.UserDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PurchasePolicyTest {

    private PurchasePolicy purchasePolicy;
    private Rule<UserDTO, List<ProductDTO>> mockRule1;
    private Rule<UserDTO, List<ProductDTO>> mockRule2;
    private UserDTO userDTO;

    @BeforeEach
    void setUp() {
        purchasePolicy = new PurchasePolicy();
        mockRule1 = mock(Rule.class);
        mockRule2 = mock(Rule.class);
        userDTO = new UserDTO("userId12345", "BestUser", "12/12/12", "Israel", "Bash", "Mesada", "Toy");
    }

    @Test
    void checkPurchasePolicy_withNoRules_shouldReturnTrue() {
        List<ProductDTO> products = new ArrayList<>();

        assertTrue(purchasePolicy.checkPurchasePolicy(userDTO, products));
    }

    @Test
    void checkPurchasePolicy_withFailingRule_shouldReturnFalse() {
        List<ProductDTO> products = new ArrayList<>();

        when(mockRule1.checkRule(userDTO, products)).thenReturn(false);

        List<Rule<UserDTO, List<ProductDTO>>> rules = List.of(mockRule1);
        purchasePolicy.addRule(rules, new ArrayList<>());

        assertFalse(purchasePolicy.checkPurchasePolicy(userDTO, products));
    }

    @Test
    void checkPurchasePolicy_withPassingRule_shouldReturnTrue() {
        List<ProductDTO> products = new ArrayList<>();

        when(mockRule1.checkRule(userDTO, products)).thenReturn(true);

        List<Rule<UserDTO, List<ProductDTO>>> rules = List.of(mockRule1);
        purchasePolicy.addRule(rules, new ArrayList<>());

        assertTrue(purchasePolicy.checkPurchasePolicy(userDTO, products));
    }

    @Test
    void addRule_withAndOperator_shouldCombineRulesCorrectly() {
        List<ProductDTO> products = new ArrayList<>();

        when(mockRule1.checkRule(userDTO, products)).thenReturn(true);
        when(mockRule2.checkRule(userDTO, products)).thenReturn(false);

        List<Rule<UserDTO, List<ProductDTO>>> rules = List.of(mockRule1, mockRule2);
        List<String> operators = List.of("AND");
        purchasePolicy.addRule(rules, operators);

        assertFalse(purchasePolicy.checkPurchasePolicy(userDTO, products));
    }

    @Test
    void addRule_withOrOperator_shouldCombineRulesCorrectly() {
        List<ProductDTO> products = new ArrayList<>();

        when(mockRule1.checkRule(userDTO, products)).thenReturn(false);
        when(mockRule2.checkRule(userDTO, products)).thenReturn(true);

        List<Rule<UserDTO, List<ProductDTO>>> rules = List.of(mockRule1, mockRule2);
        List<String> operators = List.of("OR");
        purchasePolicy.addRule(rules, operators);

        assertTrue(purchasePolicy.checkPurchasePolicy(userDTO, products));
    }

    @Test
    void addRule_withCondOperator_shouldCombineRulesCorrectly() {
        List<ProductDTO> products = new ArrayList<>();

        when(mockRule1.checkRule(userDTO, products)).thenReturn(true);
        when(mockRule2.checkRule(userDTO, products)).thenReturn(true);

        List<Rule<UserDTO, List<ProductDTO>>> rules = List.of(mockRule1, mockRule2);
        List<String> operators = List.of("COND");
        purchasePolicy.addRule(rules, operators);

        assertTrue(purchasePolicy.checkPurchasePolicy(userDTO, products));
    }

    @Test
    void getRulesDescriptions_shouldReturnCorrectDescriptions() {
        when(mockRule1.getDescription()).thenReturn("Rule 1");
        when(mockRule2.getDescription()).thenReturn("Rule 2");

        List<Rule<UserDTO, List<ProductDTO>>> rules = List.of(mockRule1, mockRule2);
        List<String> operators = new ArrayList<>();
        operators.add("AND"); // This is not used in the test, but it is required by the method signature
        purchasePolicy.addRule(rules, operators);

        List<String> descriptions = purchasePolicy.getRulesDescriptions();
        assertEquals(1, descriptions.size());
        assertEquals("Rule 1 and Rule 2", descriptions.get(0));

    }

    @Test
    void removeRule_shouldRemoveCorrectRule() {
        when(mockRule1.getDescription()).thenReturn("Rule 1");
        when(mockRule2.getDescription()).thenReturn("Rule 2");

        List<Rule<UserDTO, List<ProductDTO>>> rules = List.of(mockRule1, mockRule2);
        List<String> operators = new ArrayList<>();
        operators.add("AND"); // This is not used in the test, but it is required by the method signature
        purchasePolicy.addRule(rules, operators);

        purchasePolicy.removeRule(0);
        List<String> descriptions = purchasePolicy.getRulesDescriptions();
        assertEquals(0, descriptions.size());
    }
}
