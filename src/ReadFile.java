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
import java.util.Scanner;

import static java.lang.Thread.sleep;

import java.util.*;

public class ReadFile extends UnicastRemoteObject implements interfaceRMI {
    protected ArrayList<Message> listMessage;
    protected ArrayList<Comment> listComment;
    protected int Best[];

    public ReadFile() throws RemoteException {
        listMessage = new ArrayList<>();
        listComment = new ArrayList<>();
        Best = new int[9];
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
                System.out.println(AfficherBest());
            }
            br.close();
        } catch (IOException | InterruptedException e) {
            System.err.format("IOException: %s%n", e);
        }
    }
    /* Calcul du score_total et actualiser best */
    public void Score_total() throws RemoteException {
        Best = new int[9];
        for (Message msg : listMessage) {
            Score_message(msg);
            bestOf(msg);
        }
    }

    // Actualise le score d'un message
    public void Score_message(Message msg) {
        int score_tmp = 0;
        if (msg.getImportance() > 0) {       // Si le score atteint 0 on ne regarde plus le message
            msg.setImportance(msg.getScore());
            for (int i = 0; i < listComment.size(); i++) {     // parcours liste Commentaire
                if (msg.getIdMessage() == listComment.get(i).getPidMessage()) {        // Si l'id du msg = PidMessage dans le commmentaire
                    int cmt = Score_comment(i);     // va chercher le score de tous les fils
                    score_tmp = score_tmp + cmt;
                    msg.setImportance(msg.getScore() + score_tmp);
                }
            }
        }
    }

    public void bestOf(Message msg) {
        //System.out.println("Id message =  " + msg.getIdMessage() + " importance : " + msg.getImportance());
        int challenger = msg.getImportance();   // Score total du message
        if (challenger > Best[8] || Best[6] == msg.getIdMessage()) {    // Si challenger est plus grand que le 3eme du best
            if (challenger > Best[5] || Best[3] == msg.getIdMessage()) {    // Si plus grand que le 2eme
                if (challenger > Best[2] || Best[0] == msg.getIdMessage()) {  // Si plus grand que le 1er
                    Best[3] = Best[0];  // on passe le score de l'ancien 1er au deuxième
                    Best[4] = Best[1];
                    Best[5] = Best[2];
                    Best[0] = msg.getIdMessage();
                    Best[1] = msg.getIdUser();
                    Best[2] = challenger;
                } else {
                    Best[6] = Best[3];  // on passe le score du 2eme au troisieme
                    Best[7] = Best[4];
                    Best[8] = Best[5];
                    Best[3] = msg.getIdMessage();
                    Best[4] = msg.getIdUser();
                    Best[5] = challenger;
                }
            } else {
                Best[6] = msg.getIdMessage();
                Best[7] = msg.getIdUser();
                Best[8] = challenger;
            }
        }
    }
    /* Affichage best3 */
    public String AfficherBest() {
        String var = "";
        for (int i = 0; i < Best.length; i++) {
            var += " " + Best[i] + " ";
        }
        return var;
    }

    /* Affichage XML du best3 au client */
    public String Best_XML() throws RemoteException {
        String var = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<Bests>\n";
        for (int i = 0; i < 3; i++) {
            var += "<Best" + (i+1) + ">\n";
            var += "<IdMessage>" + Best[i * 3] + "</IdMessage>\n";
            var += "<IdUser>" + Best[(i * 3) + 1] + "</IdUser>\n";
        }
        var += "</Bests>";



        return var;
    }


    // Actualise le score d'un commentaire comme la fonction Score_message
    public int Score_comment(int x) {
        Comment cmt = listComment.get(x);
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

    /* Maj du score en fonction du temps. Réduit le score tout les 30s */
    public void Maj_score() throws RemoteException {
        Date date = new Date();
        try {
            sleep(1000);
            for (Message msg : listMessage) {
                long d = Math.abs(date.getTime() - msg.getDate().getTime());
                int time = (int) TimeUnit.SECONDS.convert(d, TimeUnit.MILLISECONDS);
                if (time != 0 && (time % 30) == 0) {
                    msg.minusScore(1);
                    bestOf(msg);
                }
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

}
