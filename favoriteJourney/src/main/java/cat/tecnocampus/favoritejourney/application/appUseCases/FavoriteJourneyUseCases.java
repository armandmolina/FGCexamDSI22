package cat.tecnocampus.favoritejourney.application.appUseCases;


import cat.tecnocampus.favoritejourney.application.appUseCases.exceptions.UserDoesNotExistException;
import cat.tecnocampus.favoritejourney.application.portsOut.FavoriteJourneyPort;
import cat.tecnocampus.favoritejourney.application.portsOut.JourneyPort;
import cat.tecnocampus.favoritejourney.application.portsOut.StationPort;
import cat.tecnocampus.favoritejourney.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.UUID;

@Service
public class FavoriteJourneyUseCases implements cat.tecnocampus.favoritejourney.application.portsIn.FavoriteJourneyPortIn {
    private StationPort stationPort;
    private FavoriteJourneyPort favoriteJourneyPort;
    private JourneyPort journeyPort;
    private RestTemplate restTemplate;

    public FavoriteJourneyUseCases(StationPort stationPort, FavoriteJourneyPort favoriteJourneyPort, JourneyPort journeyPort,
                                   RestTemplate restTemplate) {
        this.stationPort = stationPort;
        this.favoriteJourneyPort = favoriteJourneyPort;
        this.journeyPort = journeyPort;
        this.restTemplate = restTemplate;
    }

    @Override
    public List<Station> getStations() {
        return stationPort.findAll();
    }

    @Override
    public Station getStation(String nom) {
        return stationPort.findByName(nom);
    }

    @Override
    public void addUserFavoriteJourney(String username, FavoriteJourney favoriteJourney, int delay, int faultRatio) {
        saveFavoriteJourney(favoriteJourney, username, delay, faultRatio);
    }

    private void saveFavoriteJourney(FavoriteJourney favoriteJourney, String username, int delay, int faultRatio) {
        String journeyId = saveJourneyIfDoesNotExist(favoriteJourney.getJourney());
        favoriteJourney.getJourney().setId(journeyId);

        //TODO 2: heu d'embolcallar la crida REST amb un circuit breaker de manera que si el circuit és obert s'escrigui a
        // la bbdd el favorite journey amb el checked a false.
        // Si heu implementat el primer exercici el circuit breaker el posareu a l'adaptador que heu creat
        // Us pot convenir que el mètode userExists retorni un string amb un d'aquest tres valors: "true", "false" o "unchecked"
        // En aquest punt (i no a l'adaptador de sotida (si heu fet el primer exercici) haureu de:
        //    Gravar el journey amb checked = true si el mètode userExists retorna "true"
        //    No gravar el journey si el mètode userExists retorna "false"
        //    Gravar el journey amb checked = false si el mètode userExists retorna "uncheked"
        boolean checked = true;
        if (!userExists(username, delay, faultRatio)) {
            throw new UserDoesNotExistException(username);
        }

        favoriteJourneyPort.saveFavoriteJourney(favoriteJourney,username, checked);
    }

    private String saveJourneyIfDoesNotExist(Journey journey) {
        String journeyId = journeyPort.getJourneyId(journey);
        if (journeyId.equals("-1")) {
            journeyId = UUID.randomUUID().toString();
            journey.setId(journeyId);
            journeyPort.saveJourney(journey);
        }
        return journeyId;
    }

    @Override
    public List<FavoriteJourney> getFavoriteJourneys(String username) {
        return favoriteJourneyPort.findFavoriteJourneys(username);
    }

    //TODO 3: quan arribi el missatge de que un usuari s'ha esborrat, s'ha de cridar aquest mètode per esborrar els seus
    //  favorite journeys
    @Override
    public void deleteFavoriteJourneys(String username) {
        favoriteJourneyPort.deleteFavoriteJourney(username);
    }

    //TODO 4.1: si implementeu el discovery service feu servir l'adreça que us dóna aquest en comptes d'una fixa
    //TODO 4.2: si implementeu el discovery service voldreu que el restTemplate estigui balancejat, és a dir, si hi ha més d'una instància
    // del microservei d'usuaris que les crides es vagin repartint entre ells


    //TODO 1: heu de refactoritzar aquesta crida REST (o tot el mètode) de manera que segueixi l'arquitecutra hexagonal.
    // Aquesta crida s'hauria de fer en un adaptador i la seva interfície hauria d'estar en un port de sortida.
    private boolean userExists(String username, int delay, int faultRatio) {
        String url = "http://localhost:8080/users/exists/" + username + "?delay=" + delay + "&faultRatio=" + faultRatio;
        return restTemplate.getForObject(url, Boolean.class);
    }
}
