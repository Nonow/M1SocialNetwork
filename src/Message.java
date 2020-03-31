import java.util.Date;

public class Message {
    private Date date; //date of the message
    private int idMessage; //is the (unique) identiﬁer of the message (integer)
    private int idUser; //is the (unique) identiﬁer of the user (integer)
    private String message; //is the content of the message (string)
    private String user; //is the name of the user (string)
    private int score;

    public Message(Date pdate, int pidM, int pidU, String pmessage, String puser){
        date = pdate;
        idMessage = pidM;
        idUser = pidU;
        message = pmessage;
        user = puser;
        score = 20;
    }

    /* ********** Setters *********** */
    public void setDate(Date date) {
        this.date = date;
    }

    public void setIdMessage(int idMessage) {
        this.idMessage = idMessage;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setScore(int score) {
        this.score = score;
    }

    /* ********** Getters *********** */
    public Date getDate() {
        return date;
    }

    public int getIdMessage() {
        return idMessage;
    }

    public int getIdUser() {
        return idUser;
    }

    public String getMessage() {
        return message;
    }

    public String getUser() {
        return user;
    }

    public int getScore() {
        return score;
    }
}
