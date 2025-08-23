import java.util.Scanner;

//testing recommit
public class LogIn{
        private String userEmail;
        private final String[] userInfo = new String[4];

        public LogIn(){
        }

        public void setEmail(String email){
            this.userEmail = email;
        }
        public String getEmail(){
            return userEmail;
        }
        private String getPassword(){
            String password = "";
            Scanner reader = new Scanner(userEmail + "//UserInfo.txt");
            for(int i = 0; reader.hasNextLine(); i++){
                userInfo[i] = reader.nextLine();
            }
            password = userInfo[2];
            return password;
        }
        public boolean verifiedPassword(String password){
            return getPassword().equals(new SignUp().hashPasswords(password));
        }
        public boolean checkVerificationAnswer(String reply){
            String verificationAnswers = userInfo[3];
            return verificationAnswers.equals(new SignUp().hashPasswords(reply));
        }
}
