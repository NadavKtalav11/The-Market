package DomainLayer.Role;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class RoleFacadeTest {

    private RoleFacade roleFacade;

    @BeforeEach
    public void setUp() {
        roleFacade = RoleFacade.getInstance();
    }

    @Test
    public void testCreateStoreOwner() {
        int memberId = 123;
        int storeId = 1;
        roleFacade.createStoreOwner(memberId, storeId, true, -1);
        assertTrue(roleFacade.verifyStoreOwner(storeId, memberId));
    }

    @Test
    public void testCreateStoreManager() {
        int memberId = 456;
        int storeId = 1;
        roleFacade.createStoreManager(memberId, storeId, true, true, -1);
        assertTrue(roleFacade.verifyStoreManager(storeId, memberId));
    }

    @Test
    public void testVerifyStoreOwnerIsFounder() {
        int memberId = 789;
        int storeId = 1;
        roleFacade.createStoreOwner(memberId, storeId, true, -1);
        assertTrue(roleFacade.verifyStoreOwnerIsFounder(storeId, memberId));
    }

    @Test
    public void testGetInformationAboutStoreRoles() {
        int storeId = 1;
        int ownerId = 123;
        int managerId = 456;
        roleFacade.createStoreOwner(ownerId, storeId, true, -1);
        roleFacade.createStoreManager(managerId, storeId, true, true, -1);
        Map<Integer, String> storeRoles = roleFacade.getInformationAboutStoreRoles(storeId);
        assertTrue(storeRoles.containsKey(ownerId));
        assertTrue(storeRoles.containsKey(managerId));
        assertEquals("owner", storeRoles.get(ownerId));
        assertEquals("manager", storeRoles.get(managerId));
    }

    @Test
    public void testGetStoreManagersAuthorizations() {
        int memberId = 789;
        int storeId = 1;
        roleFacade.createStoreManager(memberId, storeId, true, true, -1);
        Map<Integer, List<Integer>> managersAuthorizations = roleFacade.getStoreManagersAuthorizations(storeId);
        assertTrue(managersAuthorizations.containsKey(memberId));
        assertFalse(managersAuthorizations.get(memberId).isEmpty());
    }

    @Test
    public void testGetAllStoreManagers() {
        int memberId = 789;
        int storeId = 1;
        roleFacade.createStoreManager(memberId, storeId, true, true, -1);
        List<Integer> allStoreManagers = roleFacade.getAllStoreManagers(storeId);
        assertTrue(allStoreManagers.contains(memberId));
    }

    @Test
    public void testGetAllStoreOwners() {
        int memberId = 789;
        int storeId = 1;
        roleFacade.createStoreOwner(memberId, storeId, true, -1);
        List<Integer> allStoreOwners = roleFacade.getAllStoreOwners(storeId);
        assertTrue(allStoreOwners.contains(memberId));
    }
}
