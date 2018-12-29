import java.util.Scanner;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.PreparedStatement;

public class Main {

    public static void main(String[] args) {

        String scelta = "", message = "", username = "", prodotto = "", passwordDB = "";
        int sceltaInt = 0, utenti = 0, idUtente = 0, idProdotto = 0, prodPresenti = 0, daAcquistare = 0;
        boolean online = false;

        try {
            String connURL = "jdbc:sqlserver://213.140.22.237\\SQLEXPRESS:1433;databaseName=gottardo.joshua;user=gottardo.joshua;password=xxx123#";
            Connection connection = DriverManager.getConnection(connURL);

            do {
                Scanner scan = new Scanner(System.in);
                System.out.println("");
                System.out.println("1: REGISTRAZIONE");
                System.out.println("2: LOGIN");
                System.out.println("3: CHIUDI");
                scelta = scan.nextLine();
                sceltaInt = Integer.parseInt(scelta);

                switch (sceltaInt) {
                    case 1:
                        System.out.print("Nome: ");
                        String nome = scan.nextLine();
                        System.out.print("Cognome: ");
                        String cognome = scan.nextLine();
                        System.out.print("Mail: ");
                        String mail = scan.nextLine();
                        do {
                            System.out.print("Username: ");
                            username = scan.nextLine();
                            String sql1a = "SELECT COUNT(idUtente) as utenti FROM MWUtente WHERE username = ?";
                            PreparedStatement stmt1a = connection.prepareStatement(sql1a);
                            stmt1a.setString(1, username);
                            ResultSet rs1a = stmt1a.executeQuery();
                            while (rs1a.next()) {
                                utenti = rs1a.getInt("utenti");
                            }
                            if (utenti == 1) {
                                System.out.println("L'username è già in utilizzo");
                            }
                            rs1a.close();
                        } while (utenti == 1);
                        System.out.print("Password: ");
                        String password = scan.nextLine();
                        String sql1b = "INSERT INTO MWUtente(nome, cognome, mail, username, password) VALUES (?, ?, ?, ?, ?)";
                        PreparedStatement stmt1b = connection.prepareStatement(sql1b);
                        stmt1b.setString(1, nome);
                        stmt1b.setString(2, cognome);
                        stmt1b.setString(3, mail);
                        stmt1b.setString(4, username);
                        stmt1b.setString(5, password);
                        stmt1b.executeUpdate();
                        System.out.println("---UTENTE CREATO---");

                        break;

                    case 2:
                        System.out.print("Username: ");
                        username = scan.nextLine();
                        System.out.print("Password: ");
                        password = scan.nextLine();
                        String sql2a = "SELECT COUNT(idUtente) as utenti FROM MWUtente WHERE username = ? AND password = ?";
                        PreparedStatement stmt2a = connection.prepareStatement(sql2a);
                        stmt2a.setString(1, username);
                        stmt2a.setString(2, password);
                        ResultSet rs2a = stmt2a.executeQuery();
                        while (rs2a.next()) {
                            utenti = rs2a.getInt("utenti");
                        }
                        rs2a.close();
                        if (utenti == 0) {
                            System.out.println("---LE CREDENZIALI NON CORRISPONDONO AD ALCUN UTENTE, REGISTRATI O RIPROVA IL LOGIN---");
                        } else {
                            String sql2b = "SELECT idUtente FROM MWUtente WHERE username = ? AND password = ?";
                            PreparedStatement stmt2b = connection.prepareStatement(sql2b);
                            stmt2b.setString(1, username);
                            stmt2b.setString(2, password);
                            ResultSet rs2b = stmt2b.executeQuery();
                            while (rs2b.next()) {
                                idUtente = rs2b.getInt("idUtente");
                            }
                            rs2b.close();

                            String sql2c = "INSERT INTO MWAccesso(oraLogin, dataLogin, idUtente) VALUES (?, ?, ?)";
                            PreparedStatement stmt2c = connection.prepareStatement(sql2c);
                            stmt2c.setTime(1, java.sql.Time.valueOf(java.time.LocalTime.now()));
                            stmt2c.setDate(2, java.sql.Date.valueOf(java.time.LocalDate.now()));
                            stmt2c.setInt(3, idUtente);
                            stmt2c.executeUpdate();

                            System.out.println("--LOGIN ESEGUITO CON SUCCESSO---");
                            online = true;
                        }
                        while (online == true) {
                            System.out.println("");
                            System.out.println("1: VISUALIZZA PRODOTTI");
                            System.out.println("2: ACQUISTA UN PRODOTTO");
                            System.out.println("3: VISUALIZZA I PRODOTTI NEL CARRELLO");
                            System.out.println("4: LOGOUT");
                            System.out.println("5: IMPOSTAZIONI AVANZATE");
                            scelta = scan.nextLine();
                            sceltaInt = Integer.parseInt(scelta);
                            switch (sceltaInt) {
                                case 1:
                                    Statement stmt = connection.createStatement();
                                    String sql1c = "SELECT * FROM MWProdotto";
                                    ResultSet rs1c = stmt.executeQuery(sql1c);
                                    while (rs1c.next()) {
                                        String nomeProd = rs1c.getString("nome");
                                        String categoria = rs1c.getString("categoria");
                                        int prezzo = rs1c.getInt("prezzo");
                                        int quantita = rs1c.getInt("quantita");
                                        message +=
                                                   nomeProd + " - categoria: " + categoria + " - prezzo: " + prezzo
                                                       + " euro - quantità disponibile " + quantita + "\n";
                                    }
                                    rs1c.close();
                                    System.out.println(message);
                                    break;

                                case 2:
                                    System.out.println("Quale prodotto vuoi acquistare?");
                                    prodotto = scan.nextLine();
                                    String sql2b = "SELECT COUNT(idProdotto) as prodotti FROM MWProdotto WHERE nome = ?";
                                    PreparedStatement stmt2b = connection.prepareStatement(sql2b);
                                    stmt2b.setString(1, prodotto);
                                    ResultSet rs2b = stmt2b.executeQuery();
                                    while (rs2b.next()) {
                                        prodPresenti = rs2b.getInt("prodotti");
                                    }
                                    rs2b.close();
                                    if (prodPresenti == 0) {
                                        System.out.println("---IL PRODOTTO CERCATO NON E' PRESENTE---");
                                    } else {
                                        String sql2c = "SELECT idProdotto FROM MWProdotto WHERE nome = ?";
                                        PreparedStatement stmt2c = connection.prepareStatement(sql2c);
                                        stmt2c.setString(1, prodotto);
                                        ResultSet rs2c = stmt2c.executeQuery();
                                        while (rs2c.next()) {
                                            idProdotto = rs2c.getInt("idProdotto");
                                        }
                                        rs2c.close();

                                        do {
                                            System.out.println("Quanti nei vuoi acquistare?");
                                            String quantitaTemp = scan.nextLine();
                                            daAcquistare = Integer.parseInt(quantitaTemp);
                                            String sql2d = "SELECT quantita FROM MWProdotto WHERE idProdotto = ?";
                                            PreparedStatement stmt2d = connection.prepareStatement(sql2d);
                                            stmt2d.setInt(1, idProdotto);
                                            ResultSet rs2d = stmt2d.executeQuery();
                                            while (rs2d.next()) {
                                                prodPresenti = rs2d.getInt("quantita");
                                            }
                                            rs2d.close();
                                            if (daAcquistare > prodPresenti) {
                                                System.out.println("---NON CI SONO ABBASTANZA " + prodotto
                                                                   + ", SELEZIONA UNA QUANTITA' MINORE");
                                            } else {
                                                System.out.println("Vuoi aggiungerlo al carrello(1) o acquistarlo(2)?");
                                                scelta = scan.nextLine();
                                                sceltaInt = Integer.parseInt(scelta);
                                                switch (sceltaInt) {
                                                    case 1:
                                                        String sql1d =
                                                                       "INSERT INTO MWCarrello(idUtente, idProdotto, quantita) VALUES (?, ?, ?);";
                                                        sql1d += "UPDATE MWProdotto SET quantita = quantita - ? WHERE idProdotto = ?;";
                                                        PreparedStatement stmt1d = connection.prepareStatement(sql1d);
                                                        stmt1d.setInt(1, idUtente);
                                                        stmt1d.setInt(2, idProdotto);
                                                        stmt1d.setInt(3, daAcquistare);
                                                        stmt1d.setInt(4, daAcquistare);
                                                        stmt1d.setInt(5, idProdotto);
                                                        stmt1d.executeUpdate();

                                                        System.out.println("---PRODOTTO AGGIUNTO AL CARRELLO---");
                                                        break;

                                                    case 2:
                                                        String sql2e = "INSERT INTO MWAcquisto(idUtente, idProdotto, quantita, data) VALUES (?, ?, ?, ?)";
                                                        sql2e += "UPDATE MWProdotto SET quantita = quantita - ? WHERE idProdotto = ?";
                                                        PreparedStatement stmt2e = connection.prepareStatement(sql2e);
                                                        stmt2e.setInt(1, idUtente);
                                                        stmt2e.setInt(2, idProdotto);
                                                        stmt2e.setInt(3, daAcquistare);
                                                        stmt2e.setDate(4, java.sql.Date.valueOf(java.time.LocalDate.now()));
                                                        stmt2e.setInt(5, daAcquistare);
                                                        stmt2e.setInt(6, idProdotto);
                                                        stmt2e.executeUpdate();

                                                        System.out.println("---PRODOTTO ACQUISTATO---");
                                                        break;

                                                    default:
                                                        System.out.println("---SCELTA INCORRETTA---");
                                                        break;
                                                }
                                            }
                                        } while (daAcquistare > prodPresenti);
                                    }
                                    break;

                                case 3:
                                    String sql3a = "SELECT MWCarrello.quantita, MWProdotto.nome FROM (MWCarrello INNER JOIN MWProdotto ON MWCarrello.idProdotto = MWProdotto.idProdotto) WHERE idUtente = ?";
                                    PreparedStatement stmt3a = connection.prepareStatement(sql3a);
                                    stmt3a.setInt(1, idUtente);
                                    stmt3a.executeQuery();
                                    ResultSet rs3a = stmt3a.executeQuery();

                                    if (rs3a.next() == false) {
                                        System.out.println("Nessun prodotto nel carrello");
                                    } else {
                                        do {
                                            String prodottoCarrello = rs3a.getString("nome");
                                            int quantitaCarrello = rs3a.getInt("quantita");
                                            if (quantitaCarrello == 1) {
                                                System.out.println(prodottoCarrello + " (1 pezzo)");
                                            } else {
                                                System.out.println(prodottoCarrello + " (" + quantitaCarrello + " pezzi)");
                                            }
                                        } while (rs3a.next());
                                    }

                                    rs3a.close();
                                    break;

                                case 4:
                                    String sql4 = "UPDATE MWAccesso SET oraLogout=?, dataLogout=? WHERE idUtente = ? AND oraLogout is null AND dataLogout is null";
                                    PreparedStatement stmt4 = connection.prepareStatement(sql4);
                                    stmt4.setTime(1, java.sql.Time.valueOf(java.time.LocalTime.now()));
                                    stmt4.setDate(2, java.sql.Date.valueOf(java.time.LocalDate.now()));
                                    stmt4.setInt(3, idUtente);
                                    stmt4.executeUpdate();
                                    idUtente = 0;
                                    System.out.println("--LOGOUT EFFETTUATO CON SUCCESSO---");
                                    online = false;
                                    break;

                                case 5:
                                    System.out.println("");
                                    System.out.println("1: MODIFICA PASSWORD");
                                    System.out.println("2: ELIMINA ACCOUNT");
                                    scelta = scan.nextLine();
                                    sceltaInt = Integer.parseInt(scelta);
                                    switch (sceltaInt) {
                                        case 1:
                                            System.out.println("Inserisci la password attuale");
                                            String passwordVecchia = scan.nextLine();
                                            String sql1f = "SELECT password FROM MWUtente WHERE idUtente = ?";
                                            PreparedStatement stmt1f = connection.prepareStatement(sql1f);
                                            stmt1f.setInt(1, idUtente);
                                            ResultSet rs1f = stmt1f.executeQuery();
                                            while (rs1f.next()) {
                                                passwordDB = rs1f.getString("password");
                                            }
                                            rs1f.close();
                                            if (passwordVecchia.equals(passwordDB)) {
                                                System.out.println("Inserisci nuova password");
                                                String nuovaPassword = scan.nextLine();
                                                String sql1g = "UPDATE MWUtente SET password = ? WHERE idUtente = ?";
                                                PreparedStatement stmt1g = connection.prepareStatement(sql1g);
                                                stmt1g.setString(1, nuovaPassword);
                                                stmt1g.setInt(2, idUtente);
                                                stmt1g.executeUpdate();
                                                System.out.println("---PASSWORD AGGIORNATA CORRETTAMENTE---");
                                            } else {
                                                System.out.println("---LA PASSWORD E' ERRATA---");
                                            }
                                            break;

                                        case 2:
                                            System.out.println("Inserisci la tua password");
                                            String passwordConferma = scan.nextLine();
                                            String sql2c = "SELECT password FROM MWUtente WHERE idUtente = ?";
                                            PreparedStatement stmt2c = connection.prepareStatement(sql2c);
                                            stmt2c.setInt(1, idUtente);
                                            ResultSet rs2c = stmt2c.executeQuery();
                                            while (rs2c.next()) {
                                                passwordDB = rs2c.getString("password");
                                            }
                                            rs2c.close();
                                            if (passwordDB.equals(passwordConferma)) {
                                                String sql2d = "DELETE FROM MWAccesso WHERE idUtente = ?;";
                                                sql2d += "DELETE FROM MWAcquisto WHERE idUtente = ?;";
                                                sql2d += "DELETE FROM MWCarrello WHERE idUtente = ?;";
                                                sql2d += "DELETE FROM MWUtente WHERE idUtente = ?;";
                                                PreparedStatement stmt2d = connection.prepareStatement(sql2d);
                                                stmt2d.setInt(1, idUtente);
                                                stmt2d.setInt(2, idUtente);
                                                stmt2d.setInt(3, idUtente);
                                                stmt2d.setInt(4, idUtente);
                                                stmt2d.executeUpdate();
                                                System.out.println("---ACCOUNT ELIMINATO---");
                                                online = false;
                                            } else {
                                                System.out.println("---LA PASSWORD E' ERRATA---");
                                            }
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
                            String sql4 = "UPDATE MWAccesso SET oraLogout=?, dataLogout=? WHERE idUtente = ? AND oraLogout is null AND dataLogout is null";
                            PreparedStatement stmt4 = connection.prepareStatement(sql4);
                            stmt4.setTime(1, java.sql.Time.valueOf(java.time.LocalTime.now()));
                            stmt4.setDate(2, java.sql.Date.valueOf(java.time.LocalDate.now()));
                            stmt4.setInt(3, idUtente);
                            stmt4.executeUpdate();
                            idUtente = 0;
                            online = false;
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