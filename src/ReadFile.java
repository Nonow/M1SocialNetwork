import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;
import java.util.Date;
import java.util.ArrayList;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.TimeUnit;

import static java.lang.Thread.sleep;

public class ReadFile extends UnicastRemoteObject implements interfaceRMI {
    protected ArrayList<Message> listMessage;
    protected ArrayList<Comment> listComment;
    protected int Best[];

    public ReadFile() throws RemoteException {
        listMessage = new ArrayList<>();
        listComment = new ArrayList<>();
        Best = new int [5];
    }

    public ArrayList<Message> getlistMessage() {
        return listMessage;
    }

    public void setlistMessage(ArrayList<Message> listMessage) {
        this.listMessage = listMessage;
    }

    public ArrayList<Comment> getlistComment() {
        return listComment;
    }

    public void setlistComment(ArrayList<Comment> listComment) {
        this.listComment = listComment;
    }


    public void launch() throws RemoteException {
        Random random = new Random();

        try (FileReader reader = new FileReader("reseauSocial.txt");
             BufferedReader br = new BufferedReader(reader)) {

            // read line by line
            String line;
            while ((line = br.readLine()) != null) {
                Date date = new Date();
                //on attend 1 à 3 seconde
                sleep(3000/(random.nextInt(3)+1));
                System.out.println( date + " Nouvelle ligne lu :" + line);
                // on analyse la ligne lu
                String[] chaine = line.split("\\|");
                //System.out.println(chaine.length);
                //on verifie selon la longeur s'il s'agit d'un ajout de message ou commentaires
                switch (chaine.length){
                    case 4:
                        //System.out.println("case 4");
                        listMessage.add(new Message(date,Integer.parseInt(chaine[0]), Integer.parseInt(chaine[1]), chaine[2], chaine[3] ));
                        break;
                    case 5:
                        //System.out.println("case 5");
                        listComment.add(new Comment(date,Integer.parseInt(chaine[0]), Integer.parseInt(chaine[1]), chaine[2], chaine[3], Integer.parseInt(chaine[4]), -1 ));
                        break;
                    case 6:
                        //System.out.println("case 6");
                        listComment.add(new Comment(date,Integer.parseInt(chaine[0]), Integer.parseInt(chaine[1]), chaine[2], chaine[3], -1,  Integer.parseInt(chaine[5]) ));
                        break;
                    default:
                        System.out.println("erreur lecture ligne du fichier");
                }
                Maj_score();
            }
            br.close();
        } catch (IOException | InterruptedException e) {
            System.err.format("IOException: %s%n", e);
        }
    }

    // Actualise les scores par rapport au temps
    public void Maj_score() throws RemoteException{
        Date date = new Date();
        for(int i=0; i < getlistMessage().size(); i++){
            Message msg = Score_message(date,i);
            listMessage.set(i,msg);
            System.out.println(msg.getScore());
        }
    }

    // Actualise le score d'un message
    public Message Score_message(Date date,int x){
        Message msg = getlistMessage().get(x);      // on prend un message de la liste
        msg.Actuscore(date);
        for (int i = 0; i < getlistComment().size(); i++) {     // parcours liste Commentaire
            if (msg.getIdMessage() == getlistComment().get(i).getPidMessage()) {        // Si l'id du msg = PidMessage dans le commmentaire
                msg.setNbcmt(msg.getNbcmt() + 1);
                Comment cmt = Score_comment(date, i);
                listComment.set(i, cmt);
                msg.addScore(listComment.get(i).getScore());        // on ajoute les scores des commentaires au message
            }
        }
        if (msg.getScore() < 0) {       // Si le score atteint 0
            msg.setScore(0);
        }
        return msg;
    }

    // Actualise le score d'un commentaire comme la fonction Score_message
    public Comment Score_comment(Date date,int x){
        Comment cmt = getlistComment().get(x);
        cmt.Actuscore(date);
        for (int i = 0; i < getlistComment().size(); i++) {
            if (cmt.getIdCommentaire() == getlistComment().get(i).getPidCommentaire()) {
                cmt.setNbcmt(cmt.getNbcmt() + 1);
                Comment tmp = Score_comment(date, i);
                listComment.set(i, tmp);
                cmt.addScore(listComment.get(i).getScore());
            }
        }
        if (cmt.getScore() < 0) {
            cmt.setScore(0);
        }
        return cmt;
    }

    public String bestOf(){
        String var = "ok tu as reussit bg";
        return var;
    }
/*
    //function d'incrementation val Importance pour ajout comment sans idMessage
    public void updateScore (int pidComment) throws RemoteException{
        for (Comment com:listComment){
            if (com.getIdCommentaire() == pidComment){
                //on verifie s'il s'agit d'un commentaire directement lier a un message
                if (com.getPidMessage() > 0){
                    for (Message o:listMessage){
                        if (o.getIdMessage() == com.getPidMessage()){
                            o.setImportance(o.getImportance()+20);
                            System.out.println(" recherche  " + o.getImportance());
                        }
                    }
                }
                // si c'est un commentaire de commentaire on relancer la function avec le pere
                else{
                    updateScore(com.getPidCommentaire());
                }
            }
        }


    }

    //function d'incrementation val Importance pour ajout comment avec idMessage
    public void updateScoreID (int pidMessage) throws RemoteException{
        for (Message o:listMessage){
            if (o.getIdMessage() == pidMessage){
                o.setImportance(o.getImportance()+20);
                System.out.println(o.getImportance());
            }
        }
    }
    */

}
