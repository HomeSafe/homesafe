//import org.junit.Assert;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.junit.runners.JUnit4;
//import org.powermock.api.mockito.PowerMockito;
//import org.powermock.core.classloader.annotations.PrepareForTest;
//import org.powermock.modules.junit4.PowerMockRunner;
//
//import cse403.homesafe.HSTimerActivity;
//
//import static org.junit.Assert.assertEquals;
//
///**
// * Test for PasswordPrompter -- a yet unimplemented test.
// */
//@RunWith(PowerMockRunner.class)
//@PrepareForTest(HSTimerActivity.class)
//public class PasswordPrompterTest {
//
//    PasswordPrompter prompter;
//
//    @Before
//    public void setUp() {
//        prompter = null;
//    }
//
//    @Test
//    public void testCreation() {
//        prompter = new PasswordPrompter();
//        Assert.assertNotNull(prompter);
//    }
//
//    // ======= Testing regular password entry =========
//
//    @Test
//    public void testSetAndCheckPasswordCorrect() {
//        prompter = new PasswordPrompter();
//        prompter.setPassword("hunter2");
//        Assert.assertEquals(true, prompter.checkPassword("hunter2"));
//    }
//
//    @Test
//    public void testSetAndCheckPasswordIncorrect() {
//        prompter = new PasswordPrompter();
//        prompter.setPassword("hunter2");
//        Assert.assertNotEquals(true, prompter.checkPassword("2hunter"));
//    }
//
//    // ======= Testing emergency password entry ======
//
//    @Test
//    public void testSetAndCheckEmergencyPasswordCorrect() {
//        prompter = new PasswordPrompter();
//        prompter.setEmergencyPassword("hunter2EM");
//        // Should behave the same, but in background automatically sends emails
//        Assert.assertEquals(true, prompter.checkPassword("hunter2EM"));
//    }
//
//    @Test
//    public void testSetAndCheckEmergencyPasswordIncorrect() {
//        prompter = new PasswordPrompter();
//        prompter.setEmergencyPassword("hunter2");
//        Assert.assertNotEquals(true, prompter.checkPassword("2hunter"));
//    }
//
//    // ======== flow test correct ========
//
//    @Test
//    public void testChangePasswordSuccess() {
//        prompter = new PasswordPrompter();
//        prompter.setPassword("hunter2");
//        prompter.changePassword(); // tells prompter to display a popup
//        prompter.onPasswordEntry("hunter2"); // simulate button click
//        prompter.onNewPasswordEntry("hunter3");
//
//        //password should be changed
//        Assert.assertEquals(true, prompter.checkPassword("hunter3"));
//
//    }
//
//    // ======== flow test incorrect ========
//
//    @Test
//    public void testChangePasswordFailure() {
//        prompter = new PasswordPrompter();
//        prompter.setPassword("hunter2");
//        prompter.changePassword(); // tells prompter to display a popup
//        prompter.onPasswordEntry("hunterWRONG"); // simulate button click
//        prompter.onNewPasswordEntry("hunter3");
//
//        //password should not be changed
//        Assert.assertEquals(false, prompter.checkPassword("hunter3"));
//    }
//
//    // ======== flow test emergency password ========
//
//    @Test
//    public void testChangePasswordFailure() {
//        prompter = new PasswordPrompter();
//        prompter.setPassword("hunter2");
//        prompter.setEmergencyPassword("hunterEM");
//        prompter.changePassword(); // tells prompter to display a popup
//        prompter.onPasswordEntry("hunterEM"); // simulate button click
//        prompter.onNewPasswordEntry("hunter3");
//
//        //password should be changed as expected, but contacts notified
//        Assert.assertEquals(true, prompter.checkPassword("hunter3"));
//    }
//}
