# Documentazione delle classi — Yankton Bank

Yankton Bank è un'avventura testuale sviluppata in Java con interfaccia grafica Swing. Il giocatore assume il ruolo di un ladro che deve infiltrarsi in una banca abbandonata, disattivare i sistemi di sicurezza e recuperare una riserva d'oro nascosta. L'architettura del progetto è suddivisa in cinque package principali, ciascuno con responsabilità ben definite.

---

## Package `code.yankton_bank.database`

Il package gestisce tutta la persistenza dei dati su file system: salvataggi di gioco e classifica dei punteggi.

### `DatabaseManager`

Classe di utilità statica (`final`) che centralizza la gestione dei percorsi dei file usati dal gioco. Fornisce tre metodi statici per ottenere la cartella dati dell'applicazione (`~/.yanktonbank`), il file della classifica e il file di salvataggio per uno specifico slot. Il suo scopo è evitare path hard-coded sparsi nel codice, garantendo un unico punto di configurazione.

### `GameSaveDAO`

Data Access Object responsabile del salvataggio e caricamento dello stato di una partita. Il metodo `save` serializza in un file `.properties` le informazioni essenziali: nome del giocatore, punteggio, stanza corrente e contenuto dell'inventario. Il metodo `loadInto` ricostruisce lo stato nel `GameDesc` attivo, inclusa la ricerca della stanza tramite visita BFS del grafo delle stanze. È presente anche la gestione di versioni del file di salvataggio, per garantire compatibilità futura.

### `ScoreboardDAO`

Data Access Object per la classifica dei punteggi. Permette di aggiungere una nuova voce (`append`) con nome del giocatore e punteggio, e di caricare l'elenco completo delle voci già registrate (`loadAll`). I dati sono memorizzati in un file di testo semplice con formato `nome;punteggio`.

---

## Package `code.yankton_bank.impl`

Il package contiene la descrizione del gioco e tutti gli observer che implementano la logica dei comandi del giocatore. Applica il **pattern Observer**: ogni comando è gestito da un observer dedicato, disaccoppiando la logica dal controller.

### `GameDesc`

Classe principale dello stato di gioco, implementa l'interfaccia `GameDescription`. Inizializza e mantiene l'intera struttura della mappa (sette stanze collegate), gli oggetti di scena, i comandi disponibili e il giocatore. Espone metodi per modificare lo stato del gioco — livello di pericolo, punteggio, stato delle telecamere, della sicurezza e dell'armadietto — e getter per accedere a ogni singola stanza. È il modello centrale a cui tutti gli observer si riferiscono.

### `EndObserver`

Gestisce la conclusione della partita, sia per comando esplicito del giocatore (`fine`) sia in modo automatico (chiamato da `MoveObserver` alla vittoria). Il metodo statico `finish` mostra il riepilogo del punteggio, chiede il nome del giocatore tramite una finestra di dialogo Swing e salva il risultato nella classifica tramite `ScoreboardDAO`. Infine riporta il gioco al menu principale.

### `HelpObserver`

Observer semplice che risponde al comando `aiuto` stampando l'elenco di tutti i comandi disponibili nel gioco.

### `InventoryObserver`

Risponde al comando `inventario` e mostra a schermo il contenuto dell'inventario del giocatore, formattato tramite `PrettyPrint`.

### `LookObserver`

Gestisce il comando `guarda`. Descrive la stanza corrente — nome, descrizione e oggetti visibili — costruendo dinamicamente il testo da mostrare al giocatore.

### `MoveObserver`

Gestisce i comandi di movimento direzionale (nord, sud, est, ovest). Verifica la disponibilità dell'uscita richiesta e lo stato di blocco della stanza di destinazione. Aggiorna la posizione del giocatore e, se il giocatore raggiunge l'ingresso con l'oro nell'inventario, attiva automaticamente la sequenza di vittoria tramite `EndObserver.finish`.

### `OpenObserver`

Gestisce il comando `apri` applicato a oggetti specifici della scena: la porta d'ingresso, la porta dello stanzino, l'armadietto e la cassetta di sicurezza nel caveau. Ogni caso produce effetti diversi sullo stato del gioco — come rendere visibili le chiavi o l'oro — a seconda del contesto in cui viene eseguito.

### `PickUpObserver`

Gestisce il comando `prendi`. Cerca l'oggetto nella stanza corrente, verifica che sia visibile e raccoglibile, lo rimuove dalla stanza e lo aggiunge all'inventario del giocatore. Gestisce anche effetti collaterali specifici, come l'incremento del punteggio al momento della raccolta delle chiavi o il messaggio aggiuntivo quando si raccoglie l'oro.

### `SaveObserver`

Gestisce i comandi `salva` e `carica`. Delega le operazioni rispettivamente a `GameSaveDAO.save` e `GameSaveDAO.loadInto`, usando il primo slot disponibile, e mostra conferma o errore tramite finestra di dialogo Swing.

### `UseObserver`

Gestisce il comando `usa` applicato agli oggetti dell'inventario o presenti nella scena. Copre tutti gli oggetti interattivi del gioco: le pinze per tagliare il cavo della telecamera, il grimaldello per forzare porte, le chiavi per sbloccare passaggi, i tre terminali della sala sorveglianza (dove la scelta sbagliata attiva l'allarme e termina la partita), il tastierino del caveau e l'oro. Ogni azione modifica lo stato del gioco in modo coerente con la narrativa.

---

## Package `code.yankton_bank.type`

Il package contiene i modelli di dominio del gioco: le entità fondamentali che rappresentano stanze, oggetti, giocatore, comandi e inventario.

### `AdvObject`

Rappresenta un oggetto interattivo dell'avventura. Ogni oggetto ha un identificatore numerico, nome, descrizione e due flag booleani: `visible` (se il giocatore può vederlo nella stanza) e `pickupable` (se può essere raccolto). Include un metodo statico di utilità `findInRoom` per cercare un oggetto per nome all'interno di una stanza.

### `AdvObjectContainer`

Estende `AdvObject` e aggiunge la capacità di contenere altri oggetti tramite una lista interna. Implementa `Serializable`. Rappresenta contenitori fisici nella scena che possono ospitare oggetti annidati.

### `Command`

Modello di un comando riconoscibile dal parser. Associa una parola chiave testuale (es. `"nord"`) a un valore dell'enumerazione `CommandType`, che identifica l'azione da eseguire.

### `CommandType`

Enumerazione di tutti i tipi di comando supportati dal gioco: movimenti (NORTH, SOUTH, EAST, WEST), azioni (LOOK, PICK_UP, USE, OPEN), comandi di sistema (INVENTORY, HELP, END, SAVE, LOAD) e il valore speciale UNKNOWN per input non riconosciuti.

### `GameObserver`

Interfaccia del pattern Observer. Dichiara il solo metodo `update(ParserOutput, Object)`, che ogni observer concreto implementa per reagire all'input del giocatore. Disaccoppia la logica dei comandi dalla gestione centrale in `GameController`.

### `Inventory`

Contenitore degli oggetti raccolti dal giocatore. Gestisce una lista interna di `AdvObject` e offre metodi per aggiungere, rimuovere, cercare e verificare la presenza di oggetti per nome. Fornisce anche metodi di utilità come `size`, `isEmpty` e una rappresentazione testuale formattata dell'inventario.

### `Player`

Modello del giocatore. Mantiene il nome, la stanza corrente e un riferimento all'inventario (istanziato alla creazione e non sostituibile). È la classe centrale dello stato del giocatore acceduta da quasi tutti gli observer.

### `Room`

Modello di una stanza del gioco. Ogni stanza ha un identificatore numerico, un flag di blocco (`locked`), nome, descrizione e un percorso opzionale per un'immagine associata. Gestisce internamente una mappa delle uscite verso altre stanze (indicizzata per direzione) e una lista degli oggetti presenti. Espone metodi di convenienza per impostare le uscite nelle quattro direzioni cardinali.

---

## Package `code.yankton_bank.ui`

Il package gestisce l'intera interfaccia grafica del gioco, costruita con Java Swing. Comprende il controller principale, le finestre e i componenti visivi.

### `GameController`

Controller principale del ciclo di gioco. Alla creazione inizializza `GameDesc`, il parser e registra tutti gli observer. Gestisce il redirect di `System.out` verso la console grafica tramite la inner class `SwingConsoleOutputStream`. Il metodo `submit` riceve ogni comando dell'utente, lo fa elaborare dal parser e lo distribuisce agli observer registrati. Gestisce anche il salvataggio automatico dopo ogni comando e l'aggiornamento della vista (immagine della stanza, titolo della finestra). Il metodo statico `returnToMenu` chiude la finestra di gioco e riapre il menu principale.

### `GameFrame`

Finestra principale del gioco. Divide lo spazio verticalmente tra un pannello immagine (che mostra la stanza corrente a schermo intero con ritaglio centrato) e una console testuale con campo input. Contiene pulsanti per salvare la partita e tornare al menu. Ogni volta che la finestra viene ridimensionata, l'immagine viene ridisegnata in modo adattivo.

### `MainMenuFrame`

Finestra del menu principale, visualizzata all'avvio e al ritorno dal gioco. Presenta cinque pulsanti: avvia una nuova partita, carica l'ultimo salvataggio, mostra i comandi disponibili, apre la classifica ed esce dall'applicazione. Avvia anche il loop musicale di sottofondo tramite `MusicHandler`.

### `ScoreboardFrame`

Finestra che mostra la classifica dei punteggi in una tabella Swing. Legge i dati tramite `ScoreboardDAO` e li presenta con colonne Giocatore e Punteggio.

### `SimpleGameFrame`

Versione semplificata di `GameFrame`, senza immagini o controlli aggiuntivi. Espone le stesse API essenziali (`println`, `setOnSubmit`) ed è pensata per test o varianti leggere del gioco.

### `ConsolePanel`

Pannello Swing autonomo che combina un'area di testo non modificabile (output) e un campo di input con pulsante di invio. Permette di registrare una callback `onSubmit` che viene invocata a ogni pressione del tasto Invio o del pulsante. Componente riutilizzabile indipendente dal controller.

### `ImagePanel`

Pannello Swing che mostra un'immagine con un overlay semi-trasparente scuro sovrapposto. L'overlay è attivabile/disattivabile tramite il metodo `setDark`, utile per simulare stanze buie o non ancora esplorate.

### `LoadingScreen`

Finestra di dialogo modale che mostra una barra di avanzamento indeterminata durante il caricamento delle risorse. Il metodo `showAndRun` esegue un `Runnable` in background tramite `SwingWorker` e garantisce una durata minima visualizzata prima di chiudersi.

---

## Package `code.yankton_bank.util`

Il package raccoglie classi di utilità trasversali al progetto, tutte stateless e con costruttore privato (pattern utility class).

### `Concurrent`

Gestisce l'esecuzione di task in background tramite un `ExecutorService` con thread daemon. Il metodo statico `runAsync` sottomette un `Runnable` al pool, mentre `shutdown` garantisce una terminazione ordinata. È usato da `UseObserver` per ritardare la chiamata a `EndObserver.finish` dopo il messaggio di allarme.

### `HttpRest`

Client HTTP minimale per richieste GET. Accetta un URL e un timeout in millisecondi, e restituisce il corpo della risposta come stringa, oppure `null` in caso di errore o codice HTTP diverso da 200. È il livello di trasporto usato da `RestHintService`.

### `ImageLoader`

Carica immagini dal classpath (cartella `/images`) e le scala alla dimensione richiesta. Implementa una cache interna tramite `ConcurrentHashMap` per evitare letture ripetute dello stesso file. Se l'immagine non viene trovata, genera un'immagine segnaposto grigia con testo descrittivo.

### `MusicHandler`

Gestisce la riproduzione audio tramite le Java Sound API. Permette di avviare un loop musicale da una risorsa del classpath (`playLoop`) e di interromperlo (`stopLoop`). Mantiene un riferimento statico all'unico `Clip` attivo, garantendo che non si sovrappongano più tracce in loop.

### `PrettyPrint`

Classe di formattazione testuale con un unico metodo statico `print`, che aggiunge una riga di separatori prima e dopo il testo passato. Usata da tutti gli observer per uniformare la presentazione dell'output nella console di gioco.

### `RestHintService`

Servizio che recupera un suggerimento testuale da un endpoint JSON pubblico (jsonplaceholder). Usa `HttpRest` per la chiamata HTTP e analizza manualmente la risposta per estrarre il campo `title`. In caso di errore di rete o risposta non valida, restituisce `null` senza interrompere il gioco.

### `ScoreboardService`

Servizio alternativo per la gestione della classifica, indipendente da `ScoreboardDAO`. Scrive i punteggi in append su un file `scores.txt` nella directory di lavoro e offre il metodo `loadTopScores` che carica, ordina per punteggio decrescente e restituisce i migliori N risultati come lista di stringhe formattate.
