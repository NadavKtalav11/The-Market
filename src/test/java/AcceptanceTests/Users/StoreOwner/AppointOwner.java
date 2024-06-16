package AcceptanceTests.Users.StoreOwner;

import AcceptanceTests.BridgeToTests;
import AcceptanceTests.ProxyToTest;
import ServiceLayer.Response;
import Util.ExceptionsEnum;
import Util.UserDTO;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;

public class AppointOwner {
    private static BridgeToTests impl;
    static String saarUserID;
    static String tomUserID;
    static String jalalUserID;
    static String samiUserID;
    static String storeID;
    static String secondOwnerID;
    private static final String JALAL_USERNAME = "jalal";
    private static final String JALAL_PASSWORD = "Kasoomm3";
    private static final String SECOND_OWNER_USERNAME = "secondOwner";
    private static final String SECOND_OWNER_PASSWORD = "SecondOwner1";


    @BeforeAll
    public static void setUp() {
        impl = new ProxyToTest("Real");
        saarUserID = impl.enterMarketSystem().getData();
        impl.register(saarUserID,"saar", "10/04/84", "Israel", "Jerusalem", "Yehuda halevi 18", "saar", "Fadidaa1");
        tomUserID = impl.enterMarketSystem().getData();
        impl.register(tomUserID,"tom", "27/11/85", "Israel", "Jerusalem", "Yehuda halevi 17", "tom", "Shlaifer2");
        jalalUserID = impl.enterMarketSystem().getData();
        impl.register(jalalUserID,"jalal", "08/02/82", "Israel", "Jerusalem", "Yehuda halevi 13", "jalal", "Kasoomm3");
        impl.login(saarUserID, "saar", "Fadidaa1");
        storeID = impl.openStore(saarUserID, "alona", "shopping").getData();
        impl.appointStoreOwner(saarUserID, "tom", storeID);
        samiUserID = impl.enterMarketSystem().getData();
        impl.register(samiUserID,"sami", "08/02/82", "Israel", "Jerusalem", "Yehuda halevi 13", "sami", "Ka939kkmm3");
        secondOwnerID = impl.enterMarketSystem().getData();
        impl.register(secondOwnerID, SECOND_OWNER_USERNAME, "15/05/83", "Israel", "Jerusalem", "Yehuda halevi 12", SECOND_OWNER_USERNAME, SECOND_OWNER_PASSWORD);
        jalalUserID = impl.enterMarketSystem().getData();
        impl.register(jalalUserID, JALAL_USERNAME, "08/02/82", "Israel", "Jerusalem", "Yehuda halevi 13", JALAL_USERNAME, JALAL_PASSWORD);
    }

    @Test
    public void successfulAppointedTest() {
        assertTrue(impl.appointStoreOwner(saarUserID, "sami",storeID).isSuccess());
    }

    @Test
    public void alreadyStoreOwnerTest() {
        Response<String> response = impl.appointStoreOwner(saarUserID, "tom",storeID);
        assertFalse(response.isSuccess());
        assertEquals(ExceptionsEnum.memberIsAlreadyStoreOwner.toString(), response.getDescription());
    }

    @Test
    public void concurrentAppointOwnerTest() throws InterruptedException {
        // Create a latch to synchronize the start of both threads
        CountDownLatch latch = new CountDownLatch(1);

        // Atomic booleans to track appointment results
        AtomicBoolean appointmentSucceeded = new AtomicBoolean(false);
        AtomicBoolean appointmentFailed = new AtomicBoolean(false);

        // Define the first thread (Saar appointing Jalal)
        Thread thread1 = new Thread(() -> {
            try {
                latch.await(); // Wait for the latch to be released
                Response<String> response = impl.appointStoreOwner(saarUserID, JALAL_USERNAME, storeID);
                if (response.isSuccess()) {
                    appointmentSucceeded.set(true);
                } else {
                    appointmentFailed.set(true);
                    assertEquals(ExceptionsEnum.memberIsAlreadyStoreOwner.toString(), response.getDescription());
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        // Define the second thread (SecondOwner appointing Jalal)
        Thread thread2 = new Thread(() -> {
            try {
                latch.await(); // Wait for the latch to be released
                Response<String> response = impl.appointStoreOwner(secondOwnerID, JALAL_USERNAME, storeID);
                if (response.isSuccess()) {
                    appointmentSucceeded.set(true);
                } else {
                    appointmentFailed.set(true);
                    assertEquals(ExceptionsEnum.memberIsAlreadyStoreOwner.toString(), response.getDescription());
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        // Start both threads
        thread1.start();
        thread2.start();

        // Release the latch, allowing both threads to proceed
        latch.countDown();

        // Wait for both threads to finish
        thread1.join();
        thread2.join();

        // Verify that exactly one appointment succeeded and one failed
        assertTrue(appointmentSucceeded.get(), "One of the appointments should have succeeded");
        assertTrue(appointmentFailed.get(), "One of the appointments should have failed");
    }
}

