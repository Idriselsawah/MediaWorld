/*Questo file rappresenta la struttura del programma
  senza le istruzioni superflue o riguardanti SQL
  per permetterne facilmente la comprensione
  (per compilarlo non è necessario l'uso di driver)*/

public class Struttura {

    public static void main(String[] args) {

        boolean online = false;
		
            do {
                System.out.println("1: REGISTRAZIONE");
                System.out.println("2: LOGIN");
                System.out.println("3: CHIUDI");

                switch (sceltaInt) {
                    case 1:
                        //registrazione
                        break;

                    case 2:
                        //login
                            online = true;
                        }
                        while (online == true) {
                            System.out.println("1: VISUALIZZA PRODOTTI");
                            System.out.println("2: ACQUISTA UN PRODOTTO");
                            System.out.println("3: VISUALIZZA I PRODOTTI NEL CARRELLO");
                            System.out.println("4: LOGOUT");
                            System.out.println("5: IMPOSTAZIONI AVANZATE");
                            switch (scelta) {
                                case 1:
                                    //visualizza prodotti
                                    break;

                                case 2:
                                    //acquisto prodotto
                                    break;

                                case 3:
                                    //visualizza prodotti carrello
                                    break;

                                case 4:
                                    //logout
                                    online = false;
                                    break;

                                case 5:
                                    System.out.println("1: MODIFICA PASSWORD");
                                    System.out.println("2: ELIMINA ACCOUNT");
                                    switch (scelta) {
                                        case 1:
                                            //modifica password
                                            break;

                                        case 2:
                                            //eliminazione account
                                                online = false;
                                            break;

                                        default:
                                            System.out.println("---SCELTA INCORRETTA---");
                                            break;
                                    }
                                    break;

                                default:
                                    System.out.println("---SCELTA INCORRETTA---");
                                    break;
                            }
                        }
                        break;

                    case 3:
                        if (online == true) {
                            //faccio logout quando l'utente seleziona chiudi (solo se non è già stato fatto)
                        }
                        System.exit(0);
                        break;

                    default:
                        System.out.println("---SCELTA INCORRETTA---");
                        break;
                }
            } while (sceltaInt != 3);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}