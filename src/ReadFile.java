import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;
import java.util.Date;
import java.util.ArrayList;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import static java.lang.Thread.sleep;

public class ReadFile extends UnicastRemoteObject implements interfaceRMI {
    protected ArrayList<Message> listMessage;
    protected ArrayList<Comment> listComment;

    public ReadFile() throws RemoteException {
        listMessage = new ArrayList<>();
        listComment = new ArrayList<>();
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
                        //On met a jour le score du message lie
                        updateScore(Integer.parseInt(chaine[4]));
                        break;
                    case 6:
                        //System.out.println("case 6");
                        listComment.add(new Comment(date,Integer.parseInt(chaine[0]), Integer.parseInt(chaine[1]), chaine[2], chaine[3], -1,  Integer.parseInt(chaine[5]) ));
                        //On met a jour le score du message lie
                        updateScoreID(Integer.parseInt(chaine[5]));
                        break;
                    default:
                        System.out.println("erreur lecture ligne du fichier");
                }
            }
            br.close();
        } catch (IOException | InterruptedException e) {
            System.err.format("IOException: %s%n", e);
        }
    }

    public void findBestOf () throws RemoteException{

    }

    /*
    public void updateScore () throws RemoteException{
        int sum = 0;
        for (Message o:listMessage){
            if (o.getIdMessage() == id){
                for (Comment com:listComment){
                    if (com.getPidMessage() == id){
                        sum = sum + com.getScore();
                    }
                }
                sum = sum + o.getScore();
                o.setImportance(sum);
                System.out.println(o.getImportance());
            }
        }
    }
     */

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

}
