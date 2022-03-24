# Examen pràctic DSI 

Tenim un sistema que conté **usuaris** i els seus **amics**, unes estacions de tren **estacions**, uns viatges (**journeys**) i 
els viatges favorits dels usuaris (**favoriteJourneys**).

A l'examen ens centrarem en els **usuaris** i els seus **favoriteJourneys**

Dels usuaris guardem:
* username
* name
* secondName
* email

Dels *favorite journeys* guardem:
* username *// és l'usuari propietari del journey*
* origin
* destination
* dayTimes (els dies i hores de l'inici del viatge)
  * dayOfWeek
  * time
* checked   *// per saber segur que el propietari existeix (ja veurem més endavant l'utilitat)*

Aquest sistema ja el tenim implementat amb dos microserveis que funcionen i que segueixen (quasi) l'arquitectura hexagonal.
Com us podeu imaginar són dos microserveis molt senzills que només fan operacions CRUD amb els usuaris i els journeys sense tenir gaire
de lògica de negoci.

El microservei d'**usuaris** conté els usuaris i els seus amics. Aquest servei no agrega usuaris i els seus favorite journeys, és a dir, quan 
demanem una llista d'usuaris ens arriba la informació d'aquests **sense** els seus favorite journeys (i de fet sense els seus amics)

El microservei de **favorite journeys** conté les estacions, els *journeys* (estacions d'origen i destí) i els favorite journeys (un journey
amb una llista de dies i hores d'inici del viatge).

Vagi per endavant que no ens caldrà saber massa les interioritats dels microserveis per poder resoldre les preguntes de l'examen.

#### Arquitectura: (que ja teniu feta)

* Estan implementats seguint l’arquitectura hexagonal amb poc mapping. Al ser solament CRUD no cal complicar-nos la vida. Només
té mapping el cas d'ús de creació dels favorite journeys (però això no té cap importància de cara a resoldre l'examen)
* Que siguin sense mapping vol dir que tots els components usen directament els objectes del domini
* Tenen els ports d’entrada i de sortida, i adaptadors web i de persistència
* Estan estructurats en paquets sense usar mòduls (per simplificar l’examen)

#### Comunicació: (que ja teniu feta)

* Ara mateix només hi ha implementada una comunicació via REST d’un cap a l’altre i està fet sense tenir en compte l’arquitectura hexagonal,
és a dir, sense usar ports i adaptadors.
* En aquesta comunicació ja implementada el microservei de *favorite journeys*, en el moment de creació d'un journey nou, comprova si l'userName realment 
existeix entre els usuaris. Si és així, la nota es crea amb l'atribut *checked* a cert. Si l'usuari no existeix aleshores favorite journey no es crea.

#### Exemples de crides
**GET** per llistar usuaris, amics, estacions i favorite journeys
* http://localhost:8080/users
* http://localhost:8080/users/tina
* http://localhost:8080/users/exists/{username} (returna true o false depenent de si el usuari username existeix)
* http://localhost:8081/stations
* http://localhost:8081/stations/{name}
* http://localhost:8081/users/{username}/favoriteJourneys

**POST** per crear favorite journeys de l'usuari *username*
* http://localhost:8081/users/{username}/favoriteJourneys
  
On al body posarem el favorite journey que volem crear
```
{
	"origin": "Lleida-Pirineus",
	"destination": "Alcoletge",
	"dayTimes": [
        {"dayOfWeek" : "Monday",
         "time" : "12:50"},
         {"dayOfWeek" : "Tuesday",
         "time" : "13:50"}
         ]
}
```
* Hi ha altres post però no ens interessen per l'examen

**DELETE** per esborrar usuaris
* http://localhost:8080/users/{username}

On s'esborra l'usuari *username*. **Atenció** perquè ara mateix quan s'esborra un usuari NO s'esborren els seus favorite journeys!!!

## Què heu de fer? 
Heu d'implementar els següents exercicis. Són obligatoris els TODO 1, TODO 2 i TODO 3. I heu d'escollir si voleu fer 
el TODO 4 o el TODO 5. Per tant si feu 4 exercicis podeu obtenir 10 punts. Si algú té temps pot implementar els dos darrers exercicis 
i obtenir un 12, una nota superior a 10, que pot anar bé de cara a fer la mitja!

* **TODO 1:** (Val 2 punts) Fer que la comunicació via REST esmentada anteriorment, i que ja està implementada, segueixi l’arquitectura hexagonal. És a dir que hi hagi el port i l'adaptador pertinents.
  Recorda que en aquesta comunicació el microservei de favorite journey pregunta al microservei d'usuaris si l'usuari del journey existeix.
* **TODO 2:** (Val 3 punts) Embolcallar la comunicació del punt anterior amb un *circuit breaker* de manera que quan el circuit estigui obert  
  el journey es crei igualment però amb l'atribut **checked** a false. D'aquesta forma aconseguim crear notes noves encara que el 
  microservei d'usuaris estigui caigut. En un procediment de tipus batch posterior es podria comprovar l'usuari del journey i posar el checked a true 
  (però això és una història per un altre dia)
* **TODO 3:** (Val 3 punts) Volem que quan s'esborra un usuari també s'esborrin tots els seus favorite journeys. Per fer-ho el microservei d'usuaris enviarà 
  un missatge asíncron al de favorite journeys tot indicant el *userName* del que s'han d'esborrar els journeys. Heu de tenir en compte que si hi ha més d'una 
  instància del microservei de jouneys només una rebi el missatge.
  Si voleu treure puntuació màxima en aquest exrcici heu de seguir l'arquitectura hexagonal, és a dir, implementar els ports i els adaptadors pertinents.
  El missatge pot ser in simple String amb el nom d'usuari o podeu crear una classe "contenidora" del missatge dins dels ports. Qualsevol
  de les dues opcions és bona i tindrà la mateixa puntuació.
* **TODO 4:** (Val 2 punts) Afegir un discovery service. Us heu d'assegurar que el restTemplate del primer i segon exercici està balancejat (laodbalanced) 
* **TODO 5:** (Val 2 punts) Afegir un edge (gateway) service que redireccioni les crides al microservei pertinent. 
 
Dins del mateix codi hi ha comentaris del tipus TODO on hi podreu trobar alguna pista o aclariment

