package DomainLayer.Role;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class RoleFacadeTest {

    private RoleFacade roleFacade;
    private StoreOwner mockStoreOwner;



    @BeforeEach
    public void setUp() {
        roleFacade.resetInstanceForTests();
        roleFacade = RoleFacade.getInstance();
        mockStoreOwner = mock(StoreOwner.class);
        when(mockStoreOwner.getStore_ID()).thenReturn(1);
    }

    @Test
    void testVerifyStoreOwner() throws Exception {
        roleFacade.createStoreOwner(1, 1, true, 2);
        assertTrue(roleFacade.verifyStoreOwner(1, 1));
        assertFalse(roleFacade.verifyStoreOwner(2, 1));
    }

    @Test
    void testVerifyStoreManager() throws Exception {
        roleFacade.createStoreManager(1, 1, true, true, 2);
        assertTrue(roleFacade.verifyStoreManager(1, 1));
        assertFalse(roleFacade.verifyStoreManager(2, 1));
    }

    @Test
    void testVerifyStoreOwnerIsFounder() throws Exception {
        roleFacade.createStoreOwner(1, 1, true, 2);
        assertTrue(roleFacade.verifyStoreOwnerIsFounder(1, 1));
        assertFalse(roleFacade.verifyStoreOwnerIsFounder(1, 2));
    }

    @Test
    void testManagerHasInventoryPermissions() throws Exception {
        roleFacade.createStoreManager(1, 1, true, false, 2);
        assertTrue(roleFacade.managerHasInventoryPermissions(1, 1));
        assertFalse(roleFacade.managerHasInventoryPermissions(2, 1));
    }

    @Test
    void testManagerHasPurchasePermissions() throws Exception {
        roleFacade.createStoreManager(1, 1, false, true, 2);
        assertTrue(roleFacade.managerHasPurchasePermissions(1, 1));
        assertFalse(roleFacade.managerHasPurchasePermissions(2, 1));
    }

    @Test
    void testUpdateStoreManagerPermissions() throws Exception {
        roleFacade.createStoreManager(1, 1, false, false, 2);
        roleFacade.updateStoreManagerPermissions(1, 1, true, true, 2);
        StoreManager storeManager = roleFacade.getStoreManager(1, 1);
        assertTrue(storeManager.hasInventoryPermissions());
        assertTrue(storeManager.hasPurchasePermissions());
    }

    @Test
    void testGetInformationAboutStoreRoles() throws Exception {
        roleFacade.createStoreOwner(1, 1, true, 2);
        roleFacade.createStoreManager(2, 1, true, true, 3);

        Map<Integer, String> roles = roleFacade.getInformationAboutStoreRoles(1);
        assertEquals(2, roles.size());
        assertEquals("owner", roles.get(1));
        assertEquals("manager", roles.get(2));
    }

    @Test
    void testGetStoreManagersAuthorizations() throws Exception {
        roleFacade.createStoreManager(1, 1, true, false, 2);
        roleFacade.createStoreManager(2, 1, false, true, 3);

        Map<Integer, List<Integer>> authorizations = roleFacade.getStoreManagersAuthorizations(1);
        assertEquals(2, authorizations.size());
        assertTrue(authorizations.containsKey(1));
        assertTrue(authorizations.containsKey(2));
    }

    @Test
    void testGetAllStoreManagers() throws Exception {
        roleFacade.createStoreManager(1, 1, true, false, 2);
        roleFacade.createStoreManager(2, 1, false, true, 3);

        List<Integer> managers = roleFacade.getAllStoreManagers(1);
        assertEquals(2, managers.size());
        assertTrue(managers.contains(1));
        assertTrue(managers.contains(2));
    }

    @Test
    void testGetAllStoreOwners() throws Exception {
        roleFacade.createStoreOwner(1, 1, true, 2);
        roleFacade.createStoreOwner(2, 1, false, 3);

        List<Integer> owners = roleFacade.getAllStoreOwners(1);
        assertEquals(2, owners.size());
        assertTrue(owners.contains(1));
        assertTrue(owners.contains(2));
    }

    @Test
    void testGetStoresByOwner() throws Exception {
        List<Integer> stores = new ArrayList<>();
        stores.add(1);
        stores.add(2);

        roleFacade.createStoreOwner(1, 1, true, 2);
        roleFacade.createStoreOwner(1, 2, false, 3);

        List<Integer> storesByOwner = roleFacade.getStoresByOwner(stores, 1);
        assertEquals(2, storesByOwner.size());
        assertTrue(storesByOwner.contains(1));
        assertTrue(storesByOwner.contains(2));
    }

}
