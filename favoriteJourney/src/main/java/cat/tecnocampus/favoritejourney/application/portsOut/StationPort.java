package cat.tecnocampus.favoritejourney.application.portsOut;

import cat.tecnocampus.favoritejourney.domain.Station;

import java.util.List;

public interface StationPort {
    List<Station> findAll();

    Station findByName(String nom);
}
