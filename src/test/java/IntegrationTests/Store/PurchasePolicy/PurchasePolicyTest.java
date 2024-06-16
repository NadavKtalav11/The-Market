package IntegrationTests.Store.PurchasePolicy;

import DomainLayer.Store.PoliciesRulesLogicalConditions.*;
import DomainLayer.Store.StorePurchasePolicy.PurchasePolicy;
import DomainLayer.Store.StorePurchasePolicy.PurchaseRulesRepository;
import Util.ProductDTO;
import Util.UserDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class PurchasePolicyTest {
    private PurchasePolicy purchasePolicy;

    @BeforeEach
    public void setUp() {
        purchasePolicy = new PurchasePolicy();
    }

    @Test
    public void testAddAndCheckRule() {
        // Creating a simple rule that always returns true
        Rule<UserDTO, List<ProductDTO>> trueRule = new SimpleRule<>(PurchaseRulesRepository.getByRuleNumber(3)); //basket contains less than 5 tomatoes
        List<Rule<UserDTO, List<ProductDTO>>> rules = Collections.singletonList(trueRule);
        purchasePolicy.addRule(rules, Collections.emptyList());

        UserDTO user = new UserDTO("User1", "user1@gmail.com", "12/3/45", "Israel", "Ashqelon", "rabin", "moshe");
        ProductDTO product = new ProductDTO("Product1", 10, 5, "A product", "TOYS");
        List<ProductDTO> products = Collections.singletonList(product);

        assertTrue(purchasePolicy.checkPurchasePolicy(user, products));
    }

    @Test
    public void testAddMultipleRulesWithAndOperator() {
        // Creating simple rules
        Rule<UserDTO, List<ProductDTO>> trueRule = new SimpleRule<>(PurchaseRulesRepository.getByRuleNumber(3)); //basket contains less than 5 tomatoes
        Rule<UserDTO, List<ProductDTO>> falseRule = new SimpleRule<>(PurchaseRulesRepository.getByRuleNumber(5)); //basket contains at least 2 corns

        List<Rule<UserDTO, List<ProductDTO>>> rules = Arrays.asList(trueRule, falseRule);
        List<String> operators = Collections.singletonList("AND");
        purchasePolicy.addRule(rules, operators);

        UserDTO user = new UserDTO("User1", "user1@gmail.com", "12/3/45", "Israel", "Ashqelon", "rabin", "moshe");
        ProductDTO product = new ProductDTO("Product1", 10, 5, "A product", "TOYS");
        List<ProductDTO> products = Collections.singletonList(product);

        assertFalse(purchasePolicy.checkPurchasePolicy(user, products));
    }

    @Test
    public void testAddMultipleRulesWithOrOperator() {
        // Creating simple rules
        Rule<UserDTO, List<ProductDTO>> trueRule = new SimpleRule<>(PurchaseRulesRepository.getByRuleNumber(3)); //basket contains less than 5 tomatoes
        Rule<UserDTO, List<ProductDTO>> falseRule = new SimpleRule<>(PurchaseRulesRepository.getByRuleNumber(5)); //basket contains at least 2 corns


        List<Rule<UserDTO, List<ProductDTO>>> rules = Arrays.asList(falseRule, trueRule);
        List<String> operators = Collections.singletonList("OR");
        purchasePolicy.addRule(rules, operators);

        UserDTO user = new UserDTO("User1", "user1@gmail.com", "12/3/45", "Israel", "Ashqelon", "rabin", "moshe");
        ProductDTO product = new ProductDTO("Product1", 10, 5, "A product", "TOYS");
        List<ProductDTO> products = Collections.singletonList(product);

        assertTrue(purchasePolicy.checkPurchasePolicy(user, products));
    }

    @Test
    public void testAddCondRule() {
        // Creating simple rules
        Rule<UserDTO, List<ProductDTO>> trueRule = new SimpleRule<>(PurchaseRulesRepository.getByRuleNumber(3)); //basket contains less than 5 tomatoes
        Rule<UserDTO, List<ProductDTO>> falseRule = new SimpleRule<>(PurchaseRulesRepository.getByRuleNumber(5)); //basket contains at least 2 corns


        List<Rule<UserDTO, List<ProductDTO>>> rules = Arrays.asList(trueRule, falseRule);
        List<String> operators = Collections.singletonList("COND");
        purchasePolicy.addRule(rules, operators);

        UserDTO user = new UserDTO("User1", "user1@gmail.com", "12/3/45", "Israel", "Ashqelon", "rabin", "moshe");
        ProductDTO product = new ProductDTO("Product1", 10, 5, "A product", "TOYS");
        List<ProductDTO> products = Collections.singletonList(product);

        assertFalse(purchasePolicy.checkPurchasePolicy(user, products));
    }

    @Test
    public void testGetRulesDescriptions() {
        // Creating a simple rule
        Rule<UserDTO, List<ProductDTO>> trueRule = new SimpleRule<>(PurchaseRulesRepository.getByRuleNumber(3)); //basket contains less than 5 tomatoes

        List<Rule<UserDTO, List<ProductDTO>>> rules = Collections.singletonList(trueRule);
        purchasePolicy.addRule(rules, Collections.emptyList());

        List<String> descriptions = purchasePolicy.getRulesDescriptions();

        assertEquals(1, descriptions.size());
        assertEquals(PurchaseRulesRepository.getByRuleNumber(3).getDescription(), descriptions.get(0));
    }

    @Test
    public void testRemoveRule() {
        // Creating a simple rule
        Rule<UserDTO, List<ProductDTO>> trueRule = new SimpleRule<>(PurchaseRulesRepository.getByRuleNumber(3)); //basket contains less than 5 tomatoes
        List<Rule<UserDTO, List<ProductDTO>>> rules = Collections.singletonList(trueRule);
        purchasePolicy.addRule(rules, Collections.emptyList());

        purchasePolicy.removeRule(0);
        List<String> descriptions = purchasePolicy.getRulesDescriptions();

        assertEquals(0, descriptions.size());
    }
}
