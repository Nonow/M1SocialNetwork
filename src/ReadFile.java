import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;
import java.util.Date;
import java.util.ArrayList;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.TimeUnit;
import java.lang.*;

import static java.lang.Thread.sleep;

import java.util.*;

public class ReadFile extends UnicastRemoteObject implements interfaceRMI {
    protected ArrayList<Message> listMessage;
    protected ArrayList<Comment> listComment;
    protected int Best[];

    public ReadFile() throws RemoteException {
        listMessage = new ArrayList<>();
        listComment = new ArrayList<>();
        Best = new int[5];
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
                sleep(800);
                //sleep(3000/(random.nextInt(3)+1));
                System.out.println(date + " Nouvelle ligne lu :" + line);
                // on analyse la ligne lu
                String[] chaine = line.split("\\|");
                //System.out.println(chaine.length);
                //on verifie selon la longeur s'il s'agit d'un ajout de message ou commentaires
                switch (chaine.length) {
                    case 4:
                        //System.out.println("case 4");
                        listMessage.add(new Message(date, Integer.parseInt(chaine[0]), Integer.parseInt(chaine[1]), chaine[2], chaine[3]));
                        break;
                    case 5:
                        //System.out.println("case 5");
                        listComment.add(new Comment(date, Integer.parseInt(chaine[0]), Integer.parseInt(chaine[1]), chaine[2], chaine[3], Integer.parseInt(chaine[4]), -1));
                        break;
                    case 6:
                        //System.out.println("case 6");
                        listComment.add(new Comment(date, Integer.parseInt(chaine[0]), Integer.parseInt(chaine[1]), chaine[2], chaine[3], -1, Integer.parseInt(chaine[5])));
                        break;
                    default:
                        System.out.println("erreur lecture ligne du fichier");
                }
                Score_total();
            }
            br.close();
        } catch (IOException | InterruptedException e) {
            System.err.format("IOException: %s%n", e);
        }
    }

    // Actualise le score d'un message
    public void Score_message(Message msg) {
        int score_tmp = 0;
        if (msg.getImportance() > 0) {       // Si le score atteint 0
            msg.setImportance(msg.getScore());
            for (int i = 0; i < listComment.size(); i++) {     // parcours liste Commentaire
                if (msg.getIdMessage() == listComment.get(i).getPidMessage()) {        // Si l'id du msg = PidMessage dans le commmentaire
                    int cmt = Score_comment(i);
                    score_tmp = score_tmp + cmt;
                    //System.out.println("Score du commantaire "+cmt.getScore());
                    //listComment.set(i, cmt);
                    //System.out.println("Score du message " + msg.getScore());
                    msg.setImportance(msg.getScore() + score_tmp);
                    //msg.addScore(cmt.getScore());        // on ajoute les scores des commentaires au message
                    //System.out.println("Score importance " + msg.getImportance() + " i = "+ i);
                    //}
                }
            }
        }
    }

    // Actualise le score d'un commentaire comme la fonction Score_message
    public int Score_comment(int x) {
        Comment cmt = getlistComment().get(x);
        int score_tmp = cmt.getScore();
        for (int i = 0; i < listComment.size(); i++) {
            if (cmt.getIdCommentaire() == listComment.get(i).getPidCommentaire()) {
                int tmp = Score_comment(i);
                score_tmp = score_tmp + tmp;
                //listComment.set(i, tmp);
                //cmt.addScore(tmp.getScore());
            }
        }
        if (cmt.getScore() < 0) {
            cmt.setScore(0);
        }
        return score_tmp;
    }

    public String bestOf() {
        String var = "ok tu as reussit bg";
        return var;
    }

    public void Maj_score() throws RemoteException {
        Date date = new Date();
        try {
            sleep(1000);
            for (Message msg : listMessage) {
                long d = Math.abs(date.getTime() - msg.getDate().getTime());
                int time = (int) TimeUnit.SECONDS.convert(d, TimeUnit.MILLISECONDS);
                if (time != 0 && (time % 30) == 0) {
                    msg.minusScore(1);
                }
                System.out.println("Id message =  " + msg.getIdMessage() + " importance : " + msg.getImportance());
            }
            for (Comment cmt : listComment) {
                long d = Math.abs(date.getTime() - cmt.getDate().getTime());
                int time = (int) TimeUnit.SECONDS.convert(d, TimeUnit.MILLISECONDS);
                if (time != 0 && (time % 30) == 0) {
                    cmt.minusScore(1);
                }

            }
            // updateScore();
        } catch (InterruptedException e) {
            System.err.format("IOException: %s%n", e);
        }
    }

    public void Score_total() throws RemoteException {
        for (Message msg : listMessage) {
            Score_message(msg);
        }
    }


    //function d'incrementation val Importance pour ajout comment sans idMessage
    public void updateScore() throws RemoteException {
        for (Comment com : listComment) {
            //on verifie s'il s'agit d'un commentaire directement lier a un message
            if (com.getPidMessage() > 0) {
                for (Message o : listMessage) {
                    if (o.getIdMessage() == com.getPidMessage()) {
                        o.setScore(o.getScore() + com.getScore());
                        System.out.println(" pour " + o.getIdMessage() + " on a " + o.getScore());
                    }
                }
            }
            // si c'est un commentaire de commentaire on relancer la function avec le pere
            else {
                updateScore(com.getPidCommentaire(), com.getScore());
            }
        }
    }

    public void updateScore(int pidCom, int score) throws RemoteException {
        for (Comment com : listComment) {
            if (com.getIdCommentaire() == pidCom) {
                //on verifie s'il s'agit d'un commentaire directement lier a un message
                if (com.getPidMessage() > 0) {
                    for (Message o : listMessage) {
                        if (o.getIdMessage() == com.getPidMessage()) {
                            o.setScore(o.getScore() + (score + com.getScore()));
                            System.out.println(" pour " + o.getIdMessage() + " on a " + o.getScore());
                        }
                    }
                }
                // si c'est un commentaire de commentaire on relancer la function avec le pere
                else {
                    updateScore(com.getPidCommentaire(), (score + com.getScore()));
                }
            }
        }
    }


}
