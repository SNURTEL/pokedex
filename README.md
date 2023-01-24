# Aplikacja na androida: Pokedex

# Tematyka
* Przedstawianie informacji o Pokemonach pobieranych z PokeApi

# Wymagania
* [Czysta Architektura](https://developer.android.com/topic/architecture?gclid=Cj0KCQiAsdKbBhDHARIsANJ6-jdNi51WZKVm6YwBBnQIHYv1kqkaBEsu0h6uJWhtUH23XbrN5SQON54aAqVyEALw_wcB&gclsrc=aw.ds) -> średni priorytet
* [Offline first aplikacja](https://developer.android.com/topic/architecture/data-layer/offline-first) -> średni priorytet
* Wyświetlanie listy pobieżnych informacji o pokemonach -> wysoki priorytet
* Możliwość wyświetlenia dokładnych informacji o pokemonie -> średni priorytet
* Implementacja scrollowalnej listy za pomocą pagingu -> mały priorytet

# Planowane technologie
* Android Jetpack   -> Recycler view w przypadku 
* XML / Compose UI -> UI
* JUnit -> Testowanie
* Navigation Component  -> Nawigacja między ekranami, Safe args plugin w celu zapewniania type safety przekazywanych argumentów)
* Retrofit  -> Fetchowanie danych z PokeApi (w połączeniu z Kotlin Coroutines, żeby zapewnić prostą asynchroniczność) 
* Room -> lokalna baza danych do cache-owania fetchowanych danych
* Kotlin Coroutines -> Asynchroniczność
* Kotlin
* Hilt -> DI w celu uproszczenia implementacji Czystej Architektury
* Kotlinx Serialization

# Architektura potoku zapytań implementująca Unidiractional Data Flow
* Wysłanie zapytania do PokeApi
* Odebranie wyniku zapytania
* Zapis wyniku do lokalnej bazy danych
* Otrzymanie nowych danych z bazy danych
* Wyświetlenie danych w aplikacji

# Wstępne ekrany:
* Scrollowalna lista streszczonych informacji pokemonów
* Możliwość przejścia do ekranu ze szczegółami danego pokemona

# Encje:
* PokemonSnapshot: id, iconUrl, name
* PokemonDetails: id, iconUrl, name, abilities, types, ...

# Testowanie
* Wykorzystywanie fałszywego repozytorium zapytań o pokemony w celu testowania poprawności działania ViewModel-u
* Wykorzystywanie fałszywych źródeł danych i lokalnej bazy danych w celu testowania poprawności działania repozytorium
