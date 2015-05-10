package cse403.homesafe.Data;

/**
 * Created by Vivek on 4/28/15, modified by Ethan, Jacob
 *
 *  A securityData object stores passwords for both regular and emergency
 *  situation. It also provides functionality to check both passwords
 *  and increase the panic model. Since SecuritData is a singleton, use
 *  getInstance() to use its methods.
 */
public class SecurityData {
    private String regularPwd;
    private String emergPwd;

    private static SecurityData instance;

    static {
        //TODO: Read passwords from db
        instance = new SecurityData("hunter2", "1234");
    }

    // Representation invariant:
    // All fields must not be null;

    /**
     * constructor
     * @param regularPwd password for regular situation
     * @param emergPwd  password for emergency situation
     * @throws IllegalArgumentException if regularPwd or emergPwd is null
     */
    private SecurityData(String regularPwd, String emergPwd) {
        this.regularPwd = regularPwd;
        this.emergPwd = emergPwd;
    }

    /*
     * @return An instance of the SecurityData singleton.
     */
    public static SecurityData getInstance() {
        return instance;
    }

    /**
     * check if the password of regular situation is correct or not
     * @param regularPwd     password for regular situation
     * @return  true if password is correct, negative if incorrect
     */
    public boolean checkPwdRegular(String regularPwd) {
        if (regularPwd == null)
            throw new IllegalArgumentException("Null regular password.");
        return regularPwd.equals(this.regularPwd);
    }

    /**
     * check if the password of emergency situation is correct or not
     * @param emergPwd     password for emergency situation
     * @return  true if password is correct, negative if incorrect
     */
    public boolean checkPwdEmergency(String emergPwd) {
        return emergPwd.equals(this.emergPwd);
    }

    /**
     * increase the panic model
     */
    protected void incrPanic() {

    }
}
