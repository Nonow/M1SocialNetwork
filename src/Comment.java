import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Comment {
    private Date date; //date of the comment
    private int idCommentaire; // is the (unique) identifier of the comment (integer)
    private int idUser; // is the (unique) identifier of the user (integer)
    private String comment; // is the content of the comment (string)
    private String user; // is the name of the user (string)
    private int pidCommentaire; // is the identifier of the comment this comment comments (-1 if this comment comments a message)
    private int pidMessage; // is the identifier of the message this comment comments (-1 if this comment comments a comment)
    private int score;

    public Comment (Date p_date, int p_idC, int p_idU, String p_comment, String p_user, int p_pidC, int p_pidM){
        date = p_date;
        idCommentaire = p_idC;
        idUser = p_idU;
        comment = p_comment;
        user = p_user;
        pidCommentaire = p_pidC;
        pidMessage = p_pidM;
        score = 20;
    }

    /* ********** Setters *********** */
    public void setDate(Date date) {
        this.date = date;
    }

    public void setIdCommentaire(int idCommentaire) {
        this.idCommentaire = idCommentaire;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setPidCommentaire(int pidCommentaire) {
        this.pidCommentaire = pidCommentaire;
    }

    public void setPidMessage(int pidMessage) {
        this.pidMessage = pidMessage;
    }

    public void setScore(int score) {
        this.score = score;
    }

    /* ********** Getters *********** */

    public Date getDate() {
        return date;
    }

    public int getIdCommentaire() {
        return idCommentaire;
    }

    public int getIdUser() {
        return idUser;
    }

    public String getComment() {
        return comment;
    }

    public String getUser() {
        return user;
    }

    public int getPidCommentaire() {
        return pidCommentaire;
    }

    public int getPidMessage() {
        return pidMessage;
    }

    public int getScore() {
        return score;
    }
}
