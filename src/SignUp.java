import java.awt.*;
import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SignUp {
    private String password, userName,userEmail,verificationReply;
    //testing commit
    public boolean emailExist(){
        return false;
    }
    public void setUserEmail(String userEmail){
        this.userEmail=userEmail;
    }
    public void setUserName(){}
    public void setPassword(String password){
        this.password=password;
    }
    public String getVerificationReply() {
        return verificationReply;
    }
    public void saveAccount(){
        try{
            File savingFolder= new File(userEmail);
            savingFolder.mkdir();
            File userInfo = new File(userEmail+"/UserInfo.txt");
            userInfo.createNewFile();
            PrintWriter saveToFile = new PrintWriter(new BufferedWriter(new FileWriter(userInfo)));
            saveToFile.printf("%s\n%s\n%s\n%s",userName,userName,hashPasswords(password),hashPasswords(verificationReply));
            saveToFile.close();
        }catch (IOException e){
            Label error = new Label("Error Saving Account : "+ e.getMessage());
        }catch (Exception e){
            Label error= new Label("Error : "+e.getMessage());
        }

    }
    public String hashPasswords (String entity){
        StringBuilder hexEntity = new StringBuilder();
        try{
            MessageDigest MD = MessageDigest.getInstance("SHA-256");
            MD.update(entity.getBytes());
            byte[] hashedEntity = MD.digest();

            //StringBuilder hexEntity = new StringBuilder();
            for(byte b : hashedEntity){
                hexEntity.append(String.format("%02x", b));
            }
        }catch(NoSuchAlgorithmException e){
            System.out.print("Algorithm not supported " + e.getMessage());
        }

        return hexEntity.toString();
    }




}
