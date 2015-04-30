package cse403.homesafe.Data;

/**
 * Created by Vivek on 4/28/15, modified by Ethan
 *
 *  A securityData object stores passwords for both regular and emergency
 *  situation. It also provides functionality to check both passwords
 *  and increase the panic model.
 */
public class SecurityData {
    private String regularPwd;
    private String emergPwd;


    // Representation invariant:
    // SecurityData must not be null;

    /**
     * constructor
     * @param regularPwd password for regular situation
     * @param emergPwd  password for emergency situation
     * @throws IllegalArgumentException if regularPwd or emergPwd is null
     */
    public SecurityData(String regularPwd, String emergPwd) {
        this.regularPwd = regularPwd;
        this.emergPwd = emergPwd;
    }

    /**
     * check if the password of regular situation is correct or not
     * @param regularPwd     password for regular situation
     * @return  true if password is correct, negative if incorrect
     * @throws IllegalArgumentException if regularPwd is null
     */
    protected boolean checkPwdRegular(String regularPwd) {
        return false;
    }

    /**
     * check if the password of emergency situation is correct or not
     * @param emergPwd     password for emergency situation
     * @return  true if password is correct, negative if incorrect
     * @throws IllegalArgumentException if emergPwd is null
     */
    protected boolean checkPwdEmergency(String emergPwd) {
        return false;
    }

    /**
     * increase the panic model
     */
    protected void incrPanic() {

    }
}
