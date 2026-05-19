package ebusiness.controller;

import ebusiness.ejb.AuthenticationEJB;
import ebusiness.entity.Wuser;
import ebusiness.util.EmailUtil;
import ebusiness.util.PasswordUtil;
import ebusiness.util.ValidationUtil;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import jakarta.mail.MessagingException;
import java.io.Serializable;
import java.security.SecureRandom;
import java.util.logging.Level;
import java.util.logging.Logger;

@Named("authBean")
@SessionScoped
public class AuthenticationController implements Serializable {

    @EJB
    private AuthenticationEJB authenticationEJB;

    private String username;
    private String password;
    private String passwordv;
    private String fname;
    private String lname;
    private String email;
    private String verificationcode;
    private String verificationcode1;
    private boolean logged;
    private Wuser recoveryUser;
    private static final SecureRandom RANDOM = new SecureRandom();

    public String validateUser() {
        FacesContext context = FacesContext.getCurrentInstance();
        String loginUsername = ValidationUtil.trimToEmpty(username);
        Wuser user = authenticationEJB.findByUsername(loginUsername);
        if (user == null) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Login Failed!", "Username '" + loginUsername + "' does not exist."));
            username = null;
            password = null;
            return null;
        }
        if (!user.getPassword().equals(PasswordUtil.hashPassword(password))) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Login Failed!", "The password specified is not correct."));
            return null;
        }
        logged = true;
        return "default.xhtml?faces-redirect=true";
    }

    public String logout() {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "login.xhtml?faces-redirect=true";
    }

    public String createVerificationCode() {
        FacesContext context = FacesContext.getCurrentInstance();
        email = ValidationUtil.trimToEmpty(email);
        if (ValidationUtil.isBlank(email)) {
            context.addMessage(null, new FacesMessage("Email address is required."));
            return null;
        }
        if (authenticationEJB.findByEmail(email) != null) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Email already registered!", "Please choose a different email."));
            return null;
        }
        // Store the code in the session bean until the user completes registration.
        verificationcode1 = generateCode();
        try {
            EmailUtil.sendCode(email, "The Verification Code", "The Verification Code", verificationcode1);
            return "register.xhtml";
        } catch (MessagingException ex) {
            Logger.getLogger(AuthenticationController.class.getName()).log(Level.SEVERE, null, ex);
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Email Error", EmailUtil.describeSendFailure(ex)));
            return null;
        }
    }

    public String createUser() {
        FacesContext context = FacesContext.getCurrentInstance();
        if (ValidationUtil.isBlank(fname) || ValidationUtil.isBlank(lname) || ValidationUtil.isBlank(username)
                || ValidationUtil.isBlank(password) || ValidationUtil.isBlank(passwordv)) {
            context.addMessage(null, new FacesMessage("All fields are required."));
            return null;
        }
        if (!ValidationUtil.isStrongPassword(password)) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Password requirements not met",
                    "Use at least 8 characters with uppercase, lowercase, number, and special character."));
            return null;
        }
        if (!password.equals(passwordv)) {
            context.addMessage(null, new FacesMessage("The specified passwords do not match, please try again!"));
            return null;
        }
        if (verificationcode == null || !verificationcode.equals(verificationcode1)) {
            context.addMessage(null, new FacesMessage("Wrong verification code, please try again!"));
            return null;
        }
        if (authenticationEJB.findByUsername(username) != null) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Username already registered!", "Please choose a different username."));
            return null;
        }

        Wuser user = new Wuser();
        user.setFirstname(fname);
        user.setLastname(lname);
        user.setUsername(username);
        user.setEmail(email);
        // Only the hashed password is persisted; the plain text value is cleared below.
        user.setPassword(PasswordUtil.hashPassword(password));
        authenticationEJB.createUser(user);
        clearFields();
        context.getExternalContext().getFlash().setKeepMessages(true);
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                "Account created successfully", "You can now log in with your new account."));
        return "login.xhtml?faces-redirect=true";
    }

    public String createRecoveryCode() {
        FacesContext context = FacesContext.getCurrentInstance();
        recoveryUser = authenticationEJB.findByEmail(email);
        if (recoveryUser == null) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid Email!", "Email '" + email + "' does not exist."));
            return null;
        }
        // Keep the matched user in session so resetUser can update the same account.
        verificationcode1 = generateCode();
        try {
            EmailUtil.sendCode(email, "The Recovery Code", "The Recovery Code", verificationcode1);
            return "userRecovery.xhtml";
        } catch (MessagingException ex) {
            Logger.getLogger(AuthenticationController.class.getName()).log(Level.SEVERE, null, ex);
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Email Error", EmailUtil.describeSendFailure(ex)));
            return null;
        }
    }

    public String resetUser() {
        FacesContext context = FacesContext.getCurrentInstance();
        if (recoveryUser == null) {
            context.addMessage(null, new FacesMessage("Recovery session expired. Please start again."));
            return "emailRecovery.xhtml";
        }
        if (!password.equals(passwordv)) {
            context.addMessage(null, new FacesMessage("The specified passwords do not match, please try again!"));
            return null;
        }
        if (verificationcode == null || !verificationcode.equals(verificationcode1)) {
            context.addMessage(null, new FacesMessage("Wrong recovery code, please try again!"));
            return null;
        }
        recoveryUser.setPassword(PasswordUtil.hashPassword(password));
        authenticationEJB.updateUser(recoveryUser);
        clearFields();
        return "index.xhtml?faces-redirect=true";
    }

    private String generateCode() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890abcdefghijklmnopqrstuvwxyz!@#$%&*-+=?";
        StringBuilder sb = new StringBuilder();
        // SecureRandom avoids predictable verification and recovery codes.
        while (sb.length() < 20) {
            sb.append(chars.charAt(RANDOM.nextInt(chars.length())));
        }
        return sb.toString();
    }

    private void clearFields() {
        username = null; password = null; passwordv = null; fname = null; lname = null;
        email = null; verificationcode = null; verificationcode1 = null; recoveryUser = null;
    }

    public boolean isLogged() { return logged; }
    public void setLogged(boolean logged) { this.logged = logged; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getPasswordv() { return passwordv; }
    public void setPasswordv(String passwordv) { this.passwordv = passwordv; }
    public String getFname() { return fname; }
    public void setFname(String fname) { this.fname = fname; }
    public String getLname() { return lname; }
    public void setLname(String lname) { this.lname = lname; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getVerificationcode() { return verificationcode; }
    public void setVerificationcode(String verificationcode) { this.verificationcode = verificationcode; }
    public Wuser getRecoveryUser() { return recoveryUser; }
}
