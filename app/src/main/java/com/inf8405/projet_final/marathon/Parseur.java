package com.inf8405.projet_final.marathon;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Parseur {

    // Obtenir la liste des participants
    public static Map<String,Participant> ParseToParticipantOfMarathonMap(String message) throws JSONException {
        JSONArray json=new JSONArray(message);
        Map<String,Participant> participantMap= new HashMap<String,Participant>();
        for(int i=0;i<json.length();i++)
        {
            Participant participant= new Participant();
            participant.setId(json.getJSONObject(i).getString("idparticipant"));
            participant.setCourriel(json.getJSONObject(i).getString("courriel"));
            participant.setPhotoEn64(json.getJSONObject(i).getString("photo"));
            participant.setIdPosition(json.getJSONObject(i).getString("position_idposition"));
            participant.setPassword(json.getJSONObject(i).getString("password"));
            participantMap.put(participant.getId(), participant);
        }
        return participantMap;
    }

    // Obtenir informations sur un participant
    public static Participant ParseJsonToParticipant(String userStr) throws JSONException {
        JSONArray temp = new JSONArray(userStr);
        // recuperation de la premiere case de l'array
        JSONObject json = temp.getJSONObject(0);
        Participant participant= new Participant();
        participant.setId(json.getString("idparticipant"));
        participant.setCourriel(json.getString("courriel"));
        participant.setPhotoEn64(json.getString("photo"));
        participant.setIdPosition(json.getString("position_idposition"));
        participant.setPassword(json.getString("password"));

        return participant;
    }

    // Ajouter un nouveau participant

    public static String ParseParticipantToJsonFormat(Participant participant) throws JSONException {
        JSONObject userJson = new JSONObject();
        JSONObject positionJson=new JSONObject();
        JSONObject json=new JSONObject();
        // create user
        userJson.put("idparticipant",participant.getId());
        userJson.put("courriel",participant.getCourriel());
        userJson.put("photo",participant.getPhotoEn64());
        userJson.put("position_idposition",participant.getIdPosition());
        userJson.put("password",participant.getPassword());


        // creation de la partie position
        positionJson.put("idposition",participant.getPosition().getId());
        positionJson.put("latitude",String.valueOf(participant.getPosition().getLatitude()));
        positionJson.put("longitude",String.valueOf(participant.getPosition().getLongitude()));
        positionJson.put("radius",String.valueOf(participant.getPosition().getRadius()));
        positionJson.put("position_time",participant.getPosition().getDateString());
        positionJson.put("temperature",participant.getPosition().getTemperature());
        positionJson.put("humidity",participant.getPosition().getHumidity());
        positionJson.put("speed",participant.getPosition().getSpeed());
        positionJson.put("date_position",participant.getPosition().getDay());

        // JSON FINAL
        json.put("position",positionJson);
        json.put("participant",userJson);
        return json.toString();

    }

    //Obtenir la liste des marathons

    public static Map<String,Marathon> ParseToMarathonMap(String message) throws JSONException {
        JSONArray json=new JSONArray(message);
        Map<String,Marathon> marathonMap= new HashMap<String,Marathon>();
        for(int i=0;i<json.length();i++)
        {
            Marathon marathon= new Marathon();
            marathon.setId(json.getJSONObject(i).getString("idmarathon"));
            marathon.setNom(json.getJSONObject(i).getString("nom"));
            marathon.setDate(json.getJSONObject(i).getString("marathon_date"));
            marathon.setDistance(json.getJSONObject(i).getDouble("distance"));
            marathon.setPositionDepart(json.getJSONObject(i).getString("position_depart"));
            marathon.setPositionArrivee(json.getJSONObject(i).getString("position_arrivee"));
            marathon.setPositionArriveeLatitude(json.getJSONObject(i).getDouble("position_arrivee_latitude"));
            marathon.setPositionArriveeLongitude(json.getJSONObject(i).getDouble("position_arrivee_longitude"));
            marathon.setPositionDepartLatitude(json.getJSONObject(i).getDouble("position_depart_latitude"));
            marathon.setPositionDepartLongitude(json.getJSONObject(i).getDouble("position_depart_longitude"));
            marathon.setTemperature(json.getJSONObject(i).getDouble("temperature"));
            marathon.setHumidity(json.getJSONObject(i).getDouble("humidity"));
            marathon.setActual(json.getJSONObject(i).getBoolean("actual"));
            marathonMap.put(marathon.getId(), marathon);
        }
        return marathonMap;
    }

    // Mettre à jour la position, la température et l'humidité d'un participant


    public static String ParsePositionToJsonFormat(Position pos) throws JSONException {
        JSONObject json = new JSONObject();
        JSONObject positionJson = new JSONObject();

        json.put("idposition",pos.getId());
        json.put("latitude",String.valueOf(pos.getLatitude()));
        json.put("longitude",String.valueOf(pos.getLongitude()));
        json.put("radius",String.valueOf(pos.getRadius()));
        json.put("position_time",pos.getDateString());
        json.put("temperature",pos.getTemperature());
        json.put("humidity",pos.getHumidity());
        json.put("speed",pos.getSpeed());
        json.put("date_position",pos.getDay());

        positionJson.put("position",json);
        return positionJson.toString();
    }

    // Récuppérer un participantavec son courriel et son mot de passe

    public static String ParseAuthentificationInfoToJsonFormat(String courriel, String password) throws JSONException {
        JSONObject json = new JSONObject();

        json.put("courriel", courriel);
        json.put("password", password);

        return  json.toString();
    }


    // Récuppérer le classement d'un participant par idmarathon
    public static Participant ParseJsonToRankParticipant(String userStr) throws JSONException {
        JSONArray temp = new JSONArray(userStr);
        // recuperation de la premiere case de l'array
        JSONObject json = temp.getJSONObject(0);
        Participant participant= new Participant();
        participant.setId(json.getString("idparticipant"));
        participant.setCourriel(json.getString("courriel"));
        participant.setPhotoEn64(json.getString("photo"));
        participant.setIdPosition(json.getString("position_idposition"));
        participant.setRank(json.getInt("rank"));
        participant.setAverageSpeed(json.getDouble("average_speed"));
        participant.setDistanceParcourue(json.getDouble("distance_run"));


        return participant;
    }


    // Récuppérer les 3 premiers participants par idmarathon

    public static Map<String,Participant> ParseToParticipantWinersOfMarathonMap(String message) throws JSONException {
        JSONArray json=new JSONArray(message);
        Map<String,Participant> participantMap= new HashMap<String,Participant>();
        for(int i=0;i<json.length();i++)
        {
            Participant participant= new Participant();
            participant.setId(json.getJSONObject(i).getString("idparticipant"));
            participant.setCourriel(json.getJSONObject(i).getString("courriel"));
            participant.setPhotoEn64(json.getJSONObject(i).getString("photo"));
            participant.setIdPosition(json.getJSONObject(i).getString("position_idposition"));
            participant.setPassword(json.getJSONObject(i).getString("password"));
            participantMap.put(participant.getId(), participant);
        }
        return participantMap;
    }

    // Obtenir la position, la température et l'humidité d'un participant

    public static Map<String,Position> ParseToPositionsMap(String message) throws JSONException {
        JSONArray json=new JSONArray(message);
        Map<String,Position> positionsMap= new HashMap<String,Position>();
        for(int i=0;i<json.length();i++)
        {
            Position position= new Position();
            position.setId(json.getJSONObject(i).getString("idposition"));
            position.setLatitude(Double.parseDouble(json.getJSONObject(i).getString("latitude")));
            position.setLongitude(Double.parseDouble(json.getJSONObject(i).getString("longitude")));
            position.setRadius(Double.parseDouble(json.getJSONObject(i).getString("radius")));
            position.setDate(json.getJSONObject(i).getString("position_time"));
            position.setTemperature(json.getJSONObject(i).getDouble("temperature"));
            position.setHumidity(json.getJSONObject(i).getDouble("humidity"));
            position.setSpeed(json.getJSONObject(i).getDouble("speed"));
            position.setDay(json.getJSONObject(i).getString("date_position"));
            positionsMap.put(position.getId(), position);
        }
        return positionsMap;
    }

    public static Position ParseToPosition(String message) throws JSONException {
        JSONArray json=new JSONArray(message);
        Position position= new Position();

        position.setId(json.getJSONObject(0).getString("idposition"));
        position.setLatitude(Double.parseDouble(json.getJSONObject(0).getString("latitude")));
        position.setLongitude(Double.parseDouble(json.getJSONObject(0).getString("longitude")));
        position.setRadius(Double.parseDouble(json.getJSONObject(0).getString("radius")));
        position.setDate(json.getJSONObject(0).getString("position_time"));
        position.setTemperature(json.getJSONObject(0).getDouble("temperature"));
        position.setHumidity(json.getJSONObject(0).getDouble("humidity"));
        position.setSpeed(json.getJSONObject(0).getDouble("speed"));
        position.setDay(json.getJSONObject(0).getString("date_position"));

        return position;
    }

    //Obtenir la liste des marathons actuels

    public static Map<String,Marathon> ParseToActualMarathonMap(String message) throws JSONException {
        JSONArray json=new JSONArray(message);
        Map<String,Marathon> marathonMap= new HashMap<String,Marathon>();
        for(int i=0;i<json.length();i++)
        {
            Marathon marathon= new Marathon();
            marathon.setId(json.getJSONObject(i).getString("idmarathon"));
            marathon.setNom(json.getJSONObject(i).getString("nom"));
            marathon.setDate(json.getJSONObject(i).getString("marathon_date"));
            marathon.setDistance(json.getJSONObject(i).getDouble("distance"));
            marathon.setPositionDepart(json.getJSONObject(i).getString("position_depart"));
            marathon.setPositionArrivee(json.getJSONObject(i).getString("position_arrivee"));
            marathon.setPositionArriveeLatitude(json.getJSONObject(i).getDouble("position_arrivee_latitude"));
            marathon.setPositionArriveeLongitude(json.getJSONObject(i).getDouble("position_arrivee_longitude"));
            marathon.setPositionDepartLatitude(json.getJSONObject(i).getDouble("position_depart_latitude"));
            marathon.setPositionDepartLongitude(json.getJSONObject(i).getDouble("position_depart_longitude"));
            marathon.setTemperature(json.getJSONObject(i).getDouble("temperature"));
            marathon.setHumidity(json.getJSONObject(i).getDouble("humidity"));
            marathon.setActual(json.getJSONObject(i).getBoolean("actual"));
            marathonMap.put(marathon.getId(), marathon);
        }
        return marathonMap;
    }



    //Obtenir la liste des marathons historiques

    public static Map<String,Marathon> ParseToHistoricMarathonMap(String message) throws JSONException {
        JSONArray json=new JSONArray(message);
        Map<String,Marathon> marathonMap= new HashMap<String,Marathon>();
        for(int i=0;i<json.length();i++)
        {
            Marathon marathon= new Marathon();
            marathon.setId(json.getJSONObject(i).getString("idmarathon"));
            marathon.setNom(json.getJSONObject(i).getString("nom"));
            marathon.setDate(json.getJSONObject(i).getString("marathon_date"));
            marathon.setDistance(json.getJSONObject(i).getDouble("distance"));
            marathon.setPositionDepart(json.getJSONObject(i).getString("position_depart"));
            marathon.setPositionArrivee(json.getJSONObject(i).getString("position_arrivee"));
            marathon.setPositionArriveeLatitude(json.getJSONObject(i).getDouble("position_arrivee_latitude"));
            marathon.setPositionArriveeLongitude(json.getJSONObject(i).getDouble("position_arrivee_longitude"));
            marathon.setPositionDepartLatitude(json.getJSONObject(i).getDouble("position_depart_latitude"));
            marathon.setPositionDepartLongitude(json.getJSONObject(i).getDouble("position_depart_longitude"));
            marathon.setTemperature(json.getJSONObject(i).getDouble("temperature"));
            marathon.setHumidity(json.getJSONObject(i).getDouble("humidity"));
            marathon.setActual(json.getJSONObject(i).getBoolean("actual"));

            marathonMap.put(marathon.getId(), marathon);
        }
        return marathonMap;
    }
}
