# Tjedan 3, Dan 6 – Detaljni plan: Odjava i čija je lokalna baza

Room trenutno pamti treninge ali ne pamti čiji su.

## Rješenja

1. **Prazni pri odjavi** – treba se paziti da se ne izgubi nespremljeno
2. **Oznaka vlasnika preko `ownerId`**
3. **Provjera vlasnika pri prijavi** – pamćenje vlasnika lokalnih podataka

Potrebno je spremanje obaviti u ispravnom redoslijedu operacija tako da se prvo sve pošalje u oblak, a tek onda obrišu lokalni treninzi i token.

## Problem ako token istekne

Rješavamo na dva načina:

1. **Siguran zadani put** – pokušaj backup, a ako ne uspije ništa ne briši i prikaži poruku
2. **Izričit izraz** – korisnik potvrdi odjavu iako će treninzi biti nespremljeni

Sve auth promjene idu kroz `AuthRepository` tako da `isLoggedIn` bude Flow koji promatra trenutno stanje i promjene.  
`AuthRepository` istovremeno piše u `TokenStorage` i u `_isLoggedIn` kako bi ekran ispravno prikazao ikone.

## Vježbe

### 1. (Predvidi, pa testiraj) Odjavi se s jednim nespremljenim treningom. Što će biti u tablici `workouts` na backendu nakon odjave, i zašto?

- Bez prisilne odjave: nespremljeni trening ostaje lokalno, backend ga nema.
- S prisilnom odjavom: nespremljeni trening nestaje i lokalno, a backend ga i dalje nema.

### 2. (Bez koda) Zašto `logout()` ne smije prvo pozvati `tokenStorage.clearToken()`, a tek onda slanje? Što bi se dogodilo s nespremljenim treninzima?

Ako se prvo pozove brisanje tokena, zahtjev za slanje treninga backendu neće biti valjan jer neće se znati kome dodijeliti trening.  
Nespremljeni treninzi ostaju na samom uređaju dok se korisnik opet ne prijavi i obavi ispravno spremanje treninga.

### 3. (Bez koda) Odjava briše lokalne treninge, a brisanje se ne prenosi u oblak. Prati: odjaviš se, ponovno prijaviš, pritisneš Restore. Što si prije odjave lokalno obrisao, a bilo je u oblaku — gdje je sad?

Nakon što se pritisne Restore, podaci se opet dohvate s backenda na uređaj.  
Lokalno brisanje nije bilo sinkronizirano kao brisanje u oblaku, pa backend i dalje smatra da trening postoji.