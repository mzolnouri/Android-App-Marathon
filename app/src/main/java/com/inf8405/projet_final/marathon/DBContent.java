package com.inf8405.projet_final.marathon;

import android.util.Log;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DBContent {

    private Map<String,Participant> participantMap_ = new HashMap<String,Participant>();
    private Map<String,Marathon> MarathonMap_ = new HashMap<String,Marathon>();
    private Map<String,Position> positionMap_ = new HashMap<String,Position>();

    private String actualParticipantId_=new String();



    private String actualMarathonId_=new String();
    private String responseStr = new String();

    // instance du singleton
    private static DBContent Instance = null;

    public static DBContent getInstance() {
        if (Instance==null)
        {
            Instance=new DBContent();
        }
        return Instance;
    }

    public static void DestroyInstance()
    {
        Instance=null;
    }

    private DBContent() {
    }

    public Participant getActualParticipant() {
       if (participantMap_.containsKey(actualParticipantId_))
            return participantMap_.get(actualParticipantId_);
        else
            return null;

    }

    public Marathon getActualMarathon() {
        if (MarathonMap_.containsKey(actualMarathonId_))
            return MarathonMap_.get(actualMarathonId_);
        else
            return null;
    }

    public void setActualMarathonId(String actualMarathonId_) {
        this.actualMarathonId_ = actualMarathonId_;
    }

    /**
     * Cette fonction permet d'ajouter un nouvel utilisateur a la base de donnee
     * @param NParticipant
     * @return retourne l'information si l'utilisateur est ajoute ou pas
     */
    public String CreerNouvelUtilisateur(final Participant NParticipant)
    {
        responseStr=Constants.UserNotAdded;
        Thread thread = new Thread(new Runnable() {
            public void run() {
                // todo password enregistre localement est dangereux, voir solution alternative
                try {
                    Log.d("CreerNouvelUtilisateur", "cest mon test a moi");
                    // reponse true ou false du cote serveur
                    String reponsePost = DBConnexion.postRequest("http://najibarbaoui.com/api/insert_participant.php", Parseur.ParseParticipantToJsonFormat(NParticipant));
                    if(reponsePost.contentEquals("1"))
                    {
                        responseStr=Constants.UserAdded;
                        participantMap_.put(NParticipant.getId(),NParticipant);
                        actualParticipantId_=NParticipant.getId();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }});
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return responseStr;

    }

    public void updateParticipantInformationInRemoteContent(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String temp = DBConnexion.postRequest("http://najibarbaoui.com/api/update_participant.php", Parseur.ParseParticipantWithoutPositionToJsonFormat(participantMap_.get(actualParticipantId_)));
                    Log.d("okkkkkkaa", temp);
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Cette fonction permet de mettre a jour la position, temperature, humidity de l'utilisateur actuel dans la base de donnee
     */
    public void UpdateRemotePosition()
    {
        Thread thread = new Thread(new Runnable() {
            public void run() {
                Log.d("UpdatePosition", "c mon test a moi");
                try {
                    DBConnexion.postRequest("http://najibarbaoui.com/api/update_position.php",
                            Parseur.ParsePositionToJsonFormat(participantMap_.get(actualParticipantId_).getPosition()));

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public Map<String,Marathon> GetMarathonMap(String participantId){
        // To Do
        return null;
    }

    public Map<String,Marathon> getListHistoricMarathon(){
        // le clear au cas ou la map contient deja klke chose
        final List<Map<String,Marathon>> maps = new ArrayList<Map<String,Marathon>>();
        Thread UsersThread = new Thread(new Runnable() {
            public void run() {
                Log.d("marathon historic test", "c mon test a moi");
                try{
                    maps.add(Parseur.ParseToHistoricMarathonMap(DBConnexion.getRequest("http://najibarbaoui.com/api/historic_marathon.php"))) ;
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        UsersThread.start();
        try {
            UsersThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return maps.get(0);
    }

    public Map<String,Marathon> getListActualMarathon(){
        // le clear au cas ou la map contient deja klke chose
        final List<Map<String,Marathon>> maps = new ArrayList<Map<String,Marathon>>();
        Thread UsersThread = new Thread(new Runnable() {
            public void run() {
                Log.d("marathon actual test", "c mon test a moi");
                try{
                    maps.add(Parseur.ParseToHistoricMarathonMap(DBConnexion.getRequest("http://najibarbaoui.com/api/actual_marathon.php"))) ;
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        UsersThread.start();
        try {
            UsersThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return maps.get(0);
    }



    /**
     * Cette fonction permet l'authentification d'un utilisateur en utilisant son courriel et mot de passe
     * @param courriel
     * @param password
     * @return si l'email est errone, le password est errone ou si l'acces est valide
     */
    // authentification envoit une requette au serveur le serveur renvoit une reponse positive ou negative
    // si positive, renvoit les infos de l<utilisateur sinon renvoit quelle est le probleme
    public String authentification(final String courriel, final String password)
    {
        responseStr=Constants.WrongEmail;
        Thread thread = new Thread(new Runnable() {
            public void run() {
                try {
                    Log.d("authentification", "c<est mon test a moi");
                    String reponsePost= DBConnexion.postRequest("http://najibarbaoui.com/api/ouvrirsession.php",Parseur.ParseAuthentificationInfoToJsonFormat(courriel, password));

                    if(reponsePost.contentEquals("0"))
                    {
                        responseStr=MConstants.WRONG_PASSWORD;
                    }
                    else if(!reponsePost.contentEquals("1"))
                    {
                        responseStr=MConstants.ACCESS_GRANTED;
                        Participant participantActuel;
                        participantActuel = Parseur.ParseJsonToParticipant(reponsePost);
                        actualParticipantId_=participantActuel.getId();

                        participantMap_.put(actualParticipantId_, participantActuel);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return responseStr;
    }

    /**
     * cette fonction permet de recuperer les informations de tout les utilisateurs du groupe dont l'id est idGroupe
     * @param idMarathon
     * @return map contenant comme cle l id del'utilisateur et valeur l objet utilisateur
     */
    //
    public  Map<String,Participant> GetUsersFromMarathon(final String idMarathon)
    {
        // le clear au cas ou
        final List<Map<String,Participant>> maps = new ArrayList<Map<String,Participant>>();
        Thread UsersThread = new Thread(new Runnable() {
            public void run() {
                Log.d("Users test", "c mon test a moi");
                try{
                    maps.add(Parseur.ParseToParticipantOfMarathonMap(DBConnexion.getRequest("http://najibarbaoui.com/api/participantbymarathon.php?id_marathon="
                            + idMarathon)));
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        });
        UsersThread.start();
        try {
            UsersThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return maps.get(0);
    }

    /**
     * cette fonction recupere la liste de 3 premiers participants
     * @return
     */
    public  Map<String,Participant> getWinnersParticipant(final String idMarathon)
    {
        // le clear au cas ou la map contient deja klke chose
        final List<Map<String,Participant>> maps = new ArrayList<Map<String,Participant>>();
        Thread UsersThread = new Thread(new Runnable() {
            public void run() {
                Log.d("Participant winner test", "c mon test a moi");
                try{
                    maps.add(Parseur.ParseToParticipantWinersOfMarathonMap(DBConnexion.getRequest("http://najibarbaoui.com/api/winnersparticipantbymarathon.php?id_marathon="+idMarathon))) ;
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        UsersThread.start();
        try {
            UsersThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return maps.get(0);
    }


    /**
     * Fonction de recuperation de la position d'un utilisateur
     * @param userid
     * @return position de l'utilisateur associe au userid
     */
    public  Position GetUserPosition(final String userid, final String idMarathon)
    {
        final Position[] position = new Position[1];
        Thread PositionsThread= new Thread(new Runnable() {
            public void run() {
                Log.d("Positions test", "c mon test a moi");
                try{
                    if (!participantMap_.containsKey(userid))// 0661359033// 661903420
                    {
                        participantMap_=GetUsersFromMarathon(idMarathon);
                    }
                    // TODO set the right url
                    position[0] = Parseur.ParseToPosition(DBConnexion.getRequest("http://najibarbaoui.com/api/position.php?id_position=" +
                            participantMap_.get(userid).getIdPosition()));
                    if (participantMap_.containsKey(userid))
                    {
                        participantMap_.get(userid).setPosition(position[0]);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        });
        PositionsThread.start();
        try {
            PositionsThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return position[0];
    }

}
