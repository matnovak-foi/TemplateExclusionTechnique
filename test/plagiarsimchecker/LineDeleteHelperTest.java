package plagiarsimchecker;

import org.foi.common.JavaCodePartsRemover;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;

public class LineDeleteHelperTest {
    LineDeleteHelper deleteHelper = new LineDeleteHelper();

    @Test
    public void canRemoveImportAndPackageStatements(){
        String block = "package org.foi.common.filesystem.file;\n" +
                "\n" +
                "import java.io.*;\n" +
                "import java.nio.charset.Charset;\n" +
                "import java.nio.charset.StandardCharsets;\n" +
                "import java.nio.file.Files;\n" +
                "import java.nio.file.OpenOption;\n" +
                "import java.nio.file.StandardOpenOption;\n" +
                "import java.nio.file.attribute.FileAttribute;\n" +
                "import static org.foi.mpc.MPCexcpetions;\n" +
                "import javax.swing.plaf.synth.SynthOptionPaneUI;\n";
        block = deleteHelper.deleteOkLines(block);
        String expected = "";
        assertEquals(expected, block);
    }

    @Test
    public void canRemoveAnotations(){
        String block ="@Entity\n" +
                "@Table(name = \"STATIONS\")\n" +
                "@XmlRootElement\n" +
                "@NamedQueries({\n" +
                "    @NamedQuery(name = \"Stations.findAll\", query = \"SELECT s FROM Stations s\"),\n" +
                "    @NamedQuery(name = \"Stations.findByProviderid\", query = \"SELECT s FROM Stations s WHERE s.stationsPK.providerid = :providerid\"),\n" +
                "    @NamedQuery(name = \"Stations.findByStationid\", query = \"SELECT s FROM Stations s WHERE s.stationsPK.stationid = :stationid\"),\n" +
                "    @NamedQuery(name = \"Stations.findByStationname\", query = \"SELECT s FROM Stations s WHERE s.stationname = :stationname\"),\n" +
                "    @NamedQuery(name = \"Stations.findByLatitude\", query = \"SELECT s FROM Stations s WHERE s.latitude = :latitude\"),\n" +
                "    @NamedQuery(name = \"Stations.findByLongitude\", query = \"SELECT s FROM Stations s WHERE s.longitude = :longitude\"),\n" +
                "    @NamedQuery(name = \"Stations.findByElevationabovesealevel\", query = \"SELECT s FROM Stations s WHERE s.elevationabovesealevel = :elevationabovesealevel\"),\n" +
                "    @NamedQuery(name = \"Stations.findByDisplayflag\", query = \"SELECT s FROM Stations s WHERE s.displayflag = :displayflag\")})";
        block = deleteHelper.deleteOkLines(block);
        String expected = "";
        assertEquals(expected, block);
    }

    @Test
    public void canRemoveCompleteMainFunction(){
        String block = "public static void main(String [] args)\n{ " +
                " a=a; " +
                "\n}";
        block = deleteHelper.deleteOkLines(block);
        String expected = "";
        assertEquals(expected, block);
    }

    @Test
    public void canDeteleteCompleteClassWithFunctions(){
        String block = "public class TestClass { \n public static void main(String [])\n{ " +
                " a=a; " +
                "\n} \n }";
        block = deleteHelper.deleteOkLines(block);
        String expected = "";
        assertEquals(expected, block);
    }


    @Test
    public void canDeleteClassWithConstuctorFunctionInside(){
        String block = "    public static class FileCreateException extends RuntimeException {\n" +
                "        public FileCreateException(String message) {\n" +
                "            super(message);\n" +
                "        }\n" +
                "    }";
        block = deleteHelper.deleteOkLines(block);
        String expected = "";
        assertEquals(expected, block);
    }

    @Test
    public void canDeleteCompleteClassWithFunctionAndParams(){
        String block = "public class TestClass {" +
                "int bla;"+
                " \n public static void main(String [])\n{ " +
                " a=a; " +
                "\n} \n }";
        block = deleteHelper.deleteOkLines(block);
        String expected = "";
        assertEquals(expected, block);
    }

    @Test
    public void canDeleteCompleteFunctionWithAllSortsOfAccessModifiers(){
        String block = " \n abstract final public static void main(String [] args)\n{ " +
                "\n a=a; " +
                "\n aktivneAdrese = new ArrayList<>();"+
                "\n Lokacija l = dajLokaciju(adresa);"+
                "\n dajLokaciju(adresa);"+
                "\n} \n } ";
        block = deleteHelper.deleteOkLines(block);
        String expected = "}";
        assertEquals(expected, block);
    }

    @Test
    public void removeSyncronizedThis(){
        String block = "synchronized (this) {";
        block = deleteHelper.deleteOkLines(block);
        String expected = "if (syncTPLEXCLthis) {";
        assertEquals(expected, block);
    }

    @Test
    public void canDeleteAbstractFunctions(){
        String block = "\n private abstract EntityManager getEntityManager();";
        block = deleteHelper.deleteOkLines(block);
        String expected = "";
        assertEquals(expected, block);
    }

    @Test
    public void levesIncompleteClassAndDeletesOnlyFielsAndCompleteFunctions(){
        String block = "public class TestClass {" +
                "\nint bla;"+
                "\nprivate float temp;\n" +
                "    private float pressure;\n" +
                "    private float humidity;" +
                "    private String weather;\n" +
                "    private float clouds;\n" +
                "    private float windSpeed;\n" +
                "    private float windDeg;\n" +
                "    private String dt;\n"+
                " \n public static void main(String[] args)\n{ " +
                " a=a; " +
                "\n} \n ";
        block = deleteHelper.deleteOkLines(block);
        String expected = "public class TestClass {";
        assertEquals(expected, block);
    }

    @Test
    public void canDeleteFieldOrVaraibleInitializations(){
        String block = "" +
                "private Integer broj = new Integer(new Pero(){ public void setA(){} });" +
                "\n protected Integer broj = new Integer(new Pero(){});" +
                "\n public Integer broj = new Integer(new Pero());" +
                "\n static Integer broj = new Integer();" +
                "\n private \n int bla2 = \"pero\" ;" +
                "\n int bla=2;"+
                "\n int bla=2.2;"+
                "\n public String saljiPoruku() {\n EmailPovezivanje ep = (EmailPovezivanje) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(\"emailPovezivanje\");" +
                "\nzapisiPretrazivanje = false;" +
                "\n this.precipitationMode = precipitationMode;" +
                "\n java.util.Properties properties = System.getProperties();"+
                "\n aktivneAdrese = new ArrayList<>();" +
                "\nthis.sadržaj = sadržaj;" +
                "\nukupniBrojPoruka++;" +
                "\nServerSustava.brojRadnihDretvi--;" +
                "\nreturn \"\" + \"Ukupan broj zahtjeva=\" + ukupanBrojZahtjeva + \", Broj neispravnih zahtjeva=\" + brojNeispravnihZahtjeva + \", BrojNedozvoljenihZahtjeva=\" + brojNedozvoljenihZahtjeva + \", BrojUspjesnihZahtjeva=\" + brojUspjesnihZahtjeva + \", BrojPrekinutihZahtjeva=\" + brojPrekinutihZahtjeva + \", UkupnoVrijemeRadaRadnihDretvi=\" + ukupnoVrijemeRadaRadnihDretvi + \", BrojObavljenihSerijalizacija=\" + brojObavljenihSerijalizacija + '}';";
        block = deleteHelper.deleteOkLines(block);
        String expected = "public String saljiPoruku() {\n" +
                "\n" +
                "return \"\" + \"\" + ukupanBrojZahtjeva + \"\" + brojNeispravnihZahtjeva + \"\" + brojNedozvoljenihZahtjeva + \"\" + brojUspjesnihZahtjeva + \"\" + brojPrekinutihZahtjeva + \"\" + ukupnoVrijemeRadaRadnihDretvi + \"\" + brojObavljenihSerijalizacija + \"\";";
        assertEquals(expected, block);
    }

    @Test
    public void canDeleteInitalizationStatemetnsWithStrings(){
        String block = "" +
                "\n private static final String GM_BASE_URI = \"http://maps.google.com/\"; "+
                "\n private static final String GM_BASE_URI = 'http://maps.google.com/'; ";
        block = deleteHelper.deleteOkLines(block);
        String expected = "";
        assertEquals(expected, block);
    }

    @Test
    public void canDeleteInitializationStatementsWithFunctionCall(){
        String block = "" +
                "{ \n String odgovor = webResource.request(MediaType.APPLICATION_JSON).get(String.class);" +
                "{ \r\n java.util.Properties properties = System.getProperties();" +
                "{ \n zadaca.port = Integer.parseInt(m.group(1));" +
                "{ \n zadaca.nazivDatotekeKonfiguracije = m.group(2);" +
                "{ \n zadaca.trebaUcitati = m.group(3) == null ? false : true;" +
                "{ \n zadaca.nazivDatotekeSerijalizacije = m.group(4);" +
                "{ \nServerVremena sv = new ServerVremena(zadaca.port, zadaca.nazivDatotekeKonfiguracije, zadaca.trebaUcitati, zadaca.nazivDatotekeSerijalizacije);\n" +
                "{ \nServerVremena.kraj = kraj;" +
                "{ \nzadaca.port = Integer.parseInt(args[4]);\n" +
                "{ \n           zadaca.ipServera = args[2];\n" +
                "{ \n           zadaca.nazivDatotekeKonfiguracije = args[10];\n" +
                "{ \n           zadaca.korisnik = args[6];\n" +
                "{ \n           zadaca.lozinka = args[8];\n" +
                "{ \n           zadaca.admin_komanda = args[14];\n" +
                "{ \n           zadaca.vrijeme = \"\";\n" +
                "{ \nsender = ((InternetAddress) message.getFrom()[0]).getPersonal();\n" +
                "{ \nString odgovor = \"OK \" + new Date(new Date().getTime() + razlikaVremena);\n" +
                "{ \n String odgovor = \"OK \" + new Date(new Date().getTime() + razlikaVremena);\n" +
                "{ \n      os.write(odgovor.getBytes());" +
                "{ \nServletContext servletContext = (ServletContext) context.getMessageContext().get(MessageContext.SERVLET_CONTEXT);\n" +
                "{ \n this.konfig = (BP_Konfiguracija) servletContext.getAttribute(\"BP_Konfig\");\n" +
                "{ \n String APPID = konfig.getAPPID();\n" +
                "{ \n OWMKlijent owmk = new OWMKlijent(APPID);\n" +
                "{ \n MeteoPodaci mp = owmk.getRealTimeWeather(a.getGeoloc().getLatitude(), a.getGeoloc().getLongitude());\n" +
                "{ \n return mp;" +
                "{ \n multipart = (Multipart) message.getContent();\n" +
                "  \n" +
                "  // Loop over the parts of the email\n" +
                "  for ( int i = 0; i < multipart.getCount(); i++) {" +
                "" +
                "\n Poruka poruka = new Poruka(messId, sadrzajPoruke, datumMultipart, sender, subject, message.getContentType(),\n" +
                "                            message.getSize(), privitciPoruke.size(), message.getFlags(), privitciPoruke, true, true);\n" +
                "\n" +
                "                    poruke.add(poruka);" +
                "{\n "+
                "                .request(MediaType.APPLICATION_XML)\n" +
                "                .get(Authentication.class);";

        block = deleteHelper.deleteOkLines(block);
        String expected = "{ \n" +"{ \n" +"{ \n" +"{ \n" + "{ \n" +"{ \n" +"{ \n" +"\n" +"{ \n" +"{ \n" +"\n" +
                "{ \n" +"\n" +"{ \n" + "\n" +"{ \n" +"\n" +"{ \n" +"\n" +"{ \n" +"\n" +"{ \n" +
                "\n" +"{ \n" +"\n" +"{ \n" +"\n" +"{ \n" +"\n" +"{ { \n" +
                "\n" + "{ \n" + "\n" + "{ \n" +"\n" +  "{ \n" +"\n" + "{ \n" +"\n" + "{ \n" + " { \n" +
                "for ( int i = 0; i < multipart.getCount(); i++) {{\n" +
                "                 .request(MediaType.APPLICATION_XML)\n" +
                "                .get(Authentication.class);";
        assertEquals(expected, block);
    }

    @Test
    public void leavesInitializationsStatementsIfTheyAreNotCompleteDeletesOnlyOkPart(){
        String block = "" +
                "private Integer broj = new Integer(new Pero(){ public void setA(){ });";
        block = deleteHelper.deleteOkLines(block);
        String expected = "private Integer broj = new Integer(new Pero(){ );";
        assertEquals(expected, block);
    }

    @Test
    public void canDeleteFiledOrVariablesDeclaredButNotInitialized(){
        String block = "" +
                "\n final private int bla2 ;" +
                "\n int bla;";
        block = deleteHelper.deleteOkLines(block);
        String expected = "";
        assertEquals(expected, block);
    }


    @Test
    public void canDeleteFunctionCalls(){
        String block = "" +
                "\n Collections.sort(prognozaAdrese, Collator.getInstance(new Locale(\"hr\", \"HR\")));"+
                "\nzapisiUDnevnik(vrijemePozivanjaMetode, 200);" +
                "\ntest . zapisiUDnevnik(vrijemePozivanjaMetode, 200);" +
                "\n //TODO optimizirati broj čitanja prouka\r\nSystem.out.println(\"getPoruka\");\r\n";
        block = deleteHelper.deleteOkLines(block);
        String expected = "";
        assertEquals(expected, block);
    }


    @Test
    public void doNotDeleteLeftOverFunctionCall(){
        String block = "\n.path(OWMRESTHelper.getOWM_ForecastDaily_Path());";
        block = deleteHelper.deleteOkLines(block);
        String expected = block;
        assertEquals(expected, block);
    }

    @Test
    public void doNotDeleteLeftOverFunctionCallWithOtherStuff(){
        String block = "some code }" +
                "\n.path(OWMRESTHelper.getOWM_ForecastDaily_Path());";
        block = deleteHelper.deleteOkLines(block);
        String expected = "some code }\n" +
                ".path(OWMRESTHelper.getOWM_ForecastDaily_Path());";
        assertEquals(expected, block);
    }

    @Test
    public void canDeleteComments(){
        String block = "" +
                "\n public abstract class BaseBean { }/*\n" +
                " * To change this license header, choose License Headers in Project Properties.\n" +
                " * To change this template file, choose Tools | Templates\n" +
                " * and open the template in the editor.\n" +
                " */\n///*\n*/";
         block = deleteHelper.deleteOkLines(block);
        String expected = "";
        assertEquals(expected, block);
    }

    @Test
    public void canDeleteComments2() {
        String block = "mp.setWeatherNumber(jo.getJsonArray(\"weather\").getJsonObject(0).getInt(\"id\"));\n" +
                "            mp.setWeatherValue(jo.getJsonArray(\"weather\").getJsonObject(0).getString(\"description\"));\n" +
                "            mp.setWeatherIcon(jo.getJsonArray(\"weather\").getJsonObject(0).getString(\"icon\"));\n" +
                "            \n" +
                "            mp.setLastUpdate(new Date(jo.getJsonNumber(\"dt\").bigDecimalValue().longValue()*1000));\n" +
                "            //return mp;*/";
        block = deleteHelper.deleteOkLines(block);
        String expected = "*/";
        assertEquals(expected, block);
    }

    @Test
    public void canDeleteEmptyIfStataments1(){
        String block = "\n else {}";

        block = deleteHelper.deleteOkLines(block);
        String expected = block;
        assertEquals(expected, block);
        JavaCodePartsRemover javaCodePartsRemover = new JavaCodePartsRemover();
        block = javaCodePartsRemover.removeEmptyLoopBlockAndSimilar(block);
        block = javaCodePartsRemover.removeEmptyElseFinallyStatement(block);
        expected = "";
        assertEquals(expected, block);
    }

    @Test
    public void canDeleteEmptyIfStataments2(){
        String block = "\n else if(zapisiFiltriranje) {}";

        block = deleteHelper.deleteOkLines(block);
        String expected = "";
        assertEquals(expected, block);
    }

    @Test
    public void canDeleteEmptyIfStataments3(){
        String block = "\nif (zapisiFiltriranje) {}";

        block = deleteHelper.deleteOkLines(block);
        String expected = block;
        assertEquals(expected, block);
    }

    @Test
    public void canDeleteEmptyIfStataments4(){
        String block = "\nif (!(ipAdresa == null || ipAdresa.isEmpty())) {} ";

        block = deleteHelper.deleteOkLines(block);
        String expected = block;
        assertEquals(expected, block);
    }

    @Test
    public void canDeleteEmptyIfStataments5(){
        String block = "\n else if(!(ipAdresa == null || ipAdresa.isEmpty())) {} else {}";

        block = deleteHelper.deleteOkLines(block);
        String expected = "else {}";
        assertEquals(expected, block);
    }

    @Test
    public void canDeleteEmptyIfStataments6(){
        String block = "\nif (trajanje != 0) {} else {}";

        block = deleteHelper.deleteOkLines(block);
        String expected = "";
        assertEquals(expected, block);
    }

    @Test
    public void canDeleteEmptyIfStataments7() {
        String block = "\nif (posluziteljInput.isEmpty()) {\n" +
                "            errorMessage(posluziteljID, errorEmpty);\n" +
                "        } else if (posluziteljInput.length() < 9) {\n" +
                "            errorMessage(posluziteljID, \"Ime poslužitelja mora sadržavati minimalno 9 znakova!\");\n" +
                "        } else if (!posluziteljInput.equals(\"localhost\") && !posluziteljInput.equals(\"nwtis.nastava.foi.hr\")) {\n" +
                "            errorMessage(posluziteljID, \"Ime poslužitelja mora biti << localhost >> ili << nwtis.nastava.foi.hr >> \");\n" +
                "        } \n else if (!posluziteljInput.equals(\"localhost\") && !posluziteljInput.equals(\"nwtis.nastava.foi.hr\")) {\n" +
                "            errorMessage(posluziteljID, \"Ime poslužitelja mora biti << localhost >> ili << nwtis.nastava.foi.hr >> \");\n" +
                "        } else if (!posluziteljInput.equals(\"localhost\") && !posluziteljInput.equals(\"nwtis.nastava.foi.hr\")) {\n" +
                "            errorMessage(posluziteljID, \"Ime poslužitelja mora biti << localhost >> ili << nwtis.nastava.foi.hr >> \");\n" +
                "        } ";

        block = deleteHelper.deleteOkLines(block);
        String expected = "if (posluziteljInput.isEmpty()) {}";
        assertEquals(expected, block);

    }

    @Test
    public void canDeleteEmptyIfStataments12(){
        String block = "\nelse if (posluziteljInput.isEmpty()) {\n" +
                "            errorMessage(posluziteljID, errorEmpty);\n" +
                "        }\n else if (posluziteljInput.length() < 9) {\n" +
                "            errorMessage(posluziteljID, \"Ime poslužitelja mora sadržavati minimalno 9 znakova!\");\n" +
                "        }\n else if (!posluziteljInput.equals(\"localhost\") && !posluziteljInput.equals(\"nwtis.nastava.foi.hr\")) {\n" +
                "            errorMessage(posluziteljID, \"Ime poslužitelja mora biti << localhost >> ili << nwtis.nastava.foi.hr >> \");\n" +
                "        }";

        block = deleteHelper.deleteOkLines(block);
        String expected = "";
        assertEquals(expected, block);
    }

    @Test
    public void canDeleteEmptyIfStataments8(){
        String block = "\n if (usernameInput.isEmpty()) {\n" +
                "            errorMessage(usernameID, errorEmpty);\n" +
                "        } else if (usernameInput.length() <= 5) {\n" +
                "            errorMessage(usernameID, \"Korisničko ime mora sadržavati minimalno 6 znakova!\");\n" +
                "        }\n";

        block = deleteHelper.deleteOkLines(block);
        String expected = "if (usernameInput.isEmpty()) {}";
        assertEquals(expected, block);
    }

    @Test
    public void canDeleteEmptyIfStataments9(){
        String block = "        if (passwordInput.isEmpty()) {\n" +
                "            errorMessage(passwordID, errorEmpty);\n" +
                "        } else if (passwordInput.length() <= 5) {\n" +
                "            errorMessage(passwordID, \"Lozinka mora sadržavati minimalno 6 znakova!\");\n" +
                "        } else {}";

        block = deleteHelper.deleteOkLines(block);
        String expected = "";
        assertEquals(expected, block);
    }

    @Test
    public void canDeleteEmptyIfStataments10(){
        String block = "\n else if (komanda.indexOf(\"PAUSE\") != -1) {\n" +
                "                if (ServerVremena.isPauza()) {\n" +
                "                    odgovor = \"ERROR posluzitelj je vec pauziran\";\n" +
                "                } else {\n" +
                "                    odgovor = \"OK\";\n" +
                "                    ServerVremena.setPauza(true);\n" +
                "                }\n" +
                "            } else if (komanda.indexOf(\"START\") != -1) {\n" +
                "                if (ServerVremena.isPauza()) {\n" +
                "                    odgovor = \"OK\";\n" +
                "                    ServerVremena.setPauza(false);\n" +
                "                } else {\n" +
                "                    odgovor = \"ERROR posluzitelj nije pauziran\";\n" +
                "                }\n" +
                "            } else if (komanda.indexOf(\"CLEAN\") != -1) {\n" +
                "                odgovor = \"OK\";\n" +
                "            } else if (komanda.indexOf(\"SETTIME\") != -1) {\n" +
                "                odgovor = \"OK\";\n" +
                "            } else {\n" +
                "                odgovor = \"ERROR nepoznata komanda\";\n" +
                "            }";

        block = deleteHelper.deleteOkLines(block);
        String expected = "else {}";
        assertEquals(expected, block);
    }

    @Test
    public void canDeleteEmptyIfStataments(){
        String block = "" +
                "\n else {}" +
                "\n else if(zapisiFiltriranje) {}" +
                "\nif (zapisiFiltriranje) {}" +
                "\nif (!(ipAdresa == null || ipAdresa.isEmpty())) {} " +
                "\n else if(!(ipAdresa == null || ipAdresa.isEmpty())) {} else {}" +
                "\nif (trajanje != 0) {} else {}" +
                "\nif (posluziteljInput.isEmpty()) {\n" +
                "            errorMessage(posluziteljID, errorEmpty);\n" +
                "        } else if (posluziteljInput.length() < 9) {\n" +
                "            errorMessage(posluziteljID, \"Ime poslužitelja mora sadržavati minimalno 9 znakova!\");\n" +
                "        } else if (!posluziteljInput.equals(\"localhost\") && !posluziteljInput.equals(\"nwtis.nastava.foi.hr\")) {\n" +
                "            errorMessage(posluziteljID, \"Ime poslužitelja mora biti << localhost >> ili << nwtis.nastava.foi.hr >> \");\n" +
                "        }" +
                "\n if (usernameInput.isEmpty()) {\n" +
                "            errorMessage(usernameID, errorEmpty);\n" +
                "        } else if (usernameInput.length() <= 5) {\n" +
                "            errorMessage(usernameID, \"Korisničko ime mora sadržavati minimalno 6 znakova!\");\n" +
                "        }\n" +
                "\n" +
                "        if (passwordInput.isEmpty()) {\n" +
                "            errorMessage(passwordID, errorEmpty);\n" +
                "        } else if (passwordInput.length() <= 5) {\n" +
                "            errorMessage(passwordID, \"Lozinka mora sadržavati minimalno 6 znakova!\");\n" +
                "        } else {}"+
                "\n else {}" +
                "\n else if (komanda.indexOf(\"PAUSE\") != -1) {\n" +
                "                if (ServerVremena.isPauza()) {\n" +
                "                    odgovor = \"ERROR posluzitelj je vec pauziran\";\n" +
                "                } else {\n" +
                "                    odgovor = \"OK\";\n" +
                "                    ServerVremena.setPauza(true);\n" +
                "                }\n" +
                "            } else if (komanda.indexOf(\"START\") != -1) {\n" +
                "                if (ServerVremena.isPauza()) {\n" +
                "                    odgovor = \"OK\";\n" +
                "                    ServerVremena.setPauza(false);\n" +
                "                } else {\n" +
                "                    odgovor = \"ERROR posluzitelj nije pauziran\";\n" +
                "                }\n" +
                "            } else if (komanda.indexOf(\"CLEAN\") != -1) {\n" +
                "                odgovor = \"OK\";\n" +
                "            } else if (komanda.indexOf(\"SETTIME\") != -1) {\n" +
                "                odgovor = \"OK\";\n" +
                "            } else {\n" +
                "                odgovor = \"ERROR nepoznata komanda\";\n" +
                "            }";
        block = deleteHelper.deleteOkLines(block);
        String expected = "else {}\n" +
                "\n" +
                "\n" +
                " else {}";
        assertEquals(expected, block);
        JavaCodePartsRemover javaCodePartsRemover = new JavaCodePartsRemover();
        block = javaCodePartsRemover.removeEmptyLoopBlockAndSimilar(block);
        block = javaCodePartsRemover.removeEmptyElseFinallyStatement(block);
        block = javaCodePartsRemover.removeLeftoverWhiteSpaces(block);
        expected = "\n\n\n ";
        assertEquals(expected, block);
    }

    @Test
    public void nestedEmptyStatements(){
        String block = "finally {\n" +
                "            try {\n" +
                "              if (RCCdummy) {" +
                "                try{} catch (E ex){} finally {\n" +
                "                    try {\n" +
                "                     if (RCCdummy) {} else {}\n" +
                "                        \n" +
                "                     } catch (IOException ex) {} finally {}\n" +
                "                }" +
                "              }\n" +
                "\n" +
                "              else {} " +
                "            } catch (IOException ex) {} finally {}\n" +
                "        }";
        JavaCodePartsRemover codePartsRemover = new JavaCodePartsRemover();

        block = codePartsRemover.removeEmptyLoopBlockAndSimilar(block);
        block = codePartsRemover.removeEmptyElseFinallyStatement(block);
        String expected = "";
        assertEquals(expected, block);
    }

    @Test
    public void canDeleteEmptyTryCatchFinalyStataments(){
        String block = "" +
                "\n try (withResources) {  } catch (UnsupportedEncodingException ex) {} catch (JSONException ex) {} finally {}" +
                "\n try  {  } catch (UnsupportedEncodingException ex) {} catch (JSONException ex) {} finally {}" +
                "\n try  {  } catch (UnsupportedEncodingException | JSONException ex) {} finally {}" +
                "\n try  {  } catch (UnsupportedEncodingException ex) {} catch (JSONException ex) {} " +
                "\n try  {  } catch (UnsupportedEncodingException | JSONException | Exception ex) {} " +
                "\n try (FileOutputStream fos = new FileOutputStream(fullPath)) {} " +
                "\ntry (Connection veza = DriverManager.getConnection(url, korisnik, lozinka);\n" +
                "                Statement stmt = veza.createStatement();\n" +
                "                ResultSet rs = stmt.executeQuery(upit);) {}" +
                "\n {"+
                "\ncatch (UnsupportedEncodingException ex) {} catch (JSONException ex) {} catch (UnsupportedEncodingException | JSONException | Exception ex) {}"+
                "\n {"+
                "\ncatch (UnsupportedEncodingException ex) {} \n catch (JSONException ex) {} \n catch (UnsupportedEncodingException | JSONException | Exception ex) {}" +
                "\n {"+
                "} \n catch (AuthenticationFailedException e) {\n} finally {}";
        block = deleteHelper.deleteOkLines(block);
        String expected = "try  {} catch (Exception e) {}\n" +
                "try  {} catch (UnsupportedEncodingException | JSONException | Exception ex) {}\n" +
                "try(TPLEXCLdummy TPLEXCLdummy=TPLEXCLdummy){}\n" +
                "try(TPLEXCLdummy TPLEXCLdummy=TPLEXCLdummy){}\n" +
                " {\n" +
                "catch (Exception e) {}\n" +
                "{\n" +
                "catch (Exception e) {}\n" +
                "{} \n" +
                " catch (AuthenticationFailedException e) {} finally {}"
        ;
        assertEquals(expected, block);
        JavaCodePartsRemover javaCodePartsRemover = new JavaCodePartsRemover();
        block = javaCodePartsRemover.removeEmptyLoopBlockAndSimilar(block);
        block = javaCodePartsRemover.removeEmptyElseFinallyStatement(block);
        block = javaCodePartsRemover.removeLeftoverWhiteSpaces(block);
        expected = "try  {} catch (Exception e) {}\n" +
                "try  {} catch (UnsupportedEncodingException | JSONException | Exception ex) {}\n" +
                "try(TPLEXCLdummy TPLEXCLdummy=TPLEXCLdummy){}\n" +
                "try(TPLEXCLdummy TPLEXCLdummy=TPLEXCLdummy){}\n" +
                " {\n" +
                "catch (Exception e) {}\n" +
                "{\n" +
                "catch (Exception e) {}\n" +
                "{} \n" +
                " catch (AuthenticationFailedException e) {} "
                ;
        assertEquals(expected, block);
    }

    @Test
    public void canDeleteEmptyLoops(){
        String block = "" +
                "\n for ( i < args. i++) {}"+
                "\n for ( i > args. i--) {}"+
                "\n for ( i != args. i**) {}"+
                "\n for ( i < args. i//) {}"+
                "\n while (true) {}" +
                "\n for (int i = 0; i < messages.length; ++i) {}"+
                "\n for (int i = 0; i < messages.length; ++i) {" +
                "\n for (messageNumberFrom = ((currPage - 1) * pageSize); messageNumberFrom < messageNumberTo; messageNumberFrom++) {";

        block = deleteHelper.deleteOkLines(block);
        String expected = "for ( i < args. i\n" +
                "\n" +
                "\n" +
                " for (int i = 0; i < messages.length; ++i) {\n" +
                " for (messageNumberFrom = ((currPage - 1) * pageSize); messageNumberFrom < messageNumberTo; messageNumberFrom++) {";
        assertEquals(expected, block);
    }

    @Test
    public void canDeleteAnotations(){
        String block = "\n @javax.ws.rs.ApplicationPath(\"webresources\")";
        block = deleteHelper.deleteOkLines(block);
        String expected = "";
        assertEquals(expected, block);
    }

    @Test
    public void canDeleteEmptyFunctionsOfAnyKind(){
        String block = "\n public ObradaZahtjeva( ) {\n" +
                "        \n" +
                "    }\n..."  +
                "public static class FileDoesNotExistException extends RuntimeException {}\n" +
                "    public static class FileAlreadyExistException extends RuntimeException {}\n" +
                "\n public abstract class AbstractFacade<T> {}" +
                "\n public Set<Class<?>> getClasses() {}"+
                "\n private void addRestResourceClasses(Set<Class<?>> resources) {}"+
                "\n protected void doPost(HttpServletRequest request, HttpServletResponse response)\n" +
                "            throws ServletException, IOException {}"+
                "/**\n" +
                "     * Gets the value of the country property.\n" +
                "     * \n" +
                "     * @return\n" +
                "     *     possible object is\n" +
                "     *     {@link String }\n" +
                "     *     \n" +
                "     */\n" +
                "    public String getCountry() {\n" +
                "        return country;\n" +
                "    }"+
                "\npublic class LiveWeatherData {\n" +
                "public String getCountry {}\n" +
                "\n" +
                "    \n" +
                "    public void setCountry {}\n" +
                "}" +
                "\n/* a \n";
        block = deleteHelper.deleteOkLines(block);
        String expected = "...\n\n/*";
        assertEquals(expected, block);
    }

    @Test
    public void canDeleteAnotationsWithCurlyBracketsInsideOfQutationMarks(){
        String block = "@Path(\"{id}\")\n" +
                "    public MeteoRESTResource getMeteoRESTResource(@PathParam(\"id\") String id\n" +
                "    ) {\n" +
                "        return MeteoRESTResource.getInstance(id);\n" +
                "    }";
        block = deleteHelper.deleteOkLines(block);
        String expected = "";
        assertEquals(expected, block);
    }

    @Test
    public void javaUtilShouldBeDeleted(){
        String block = "java.util.Properties properties = System.getProperties();\n";
        block = deleteHelper.deleteOkLines(block);
        String expected = "";
        assertEquals(expected, block);
    }

    @Test
    public void removeStaticBlock(){
        String block = "static {\n" +
                "       jezici = new HashMap<>();\n" +
                "       jezici.put(\"Hrvatski\", new Locale (\"hr\"));\n" +
                "       jezici.put(\"English\", Locale.ENGLISH);\n" +
                "       jezici.put(\"Deutsch\", Locale.GERMAN);\n" +
                "   }";
        block = deleteHelper.deleteOkLines(block);
        String expected = "";
        assertEquals(expected, block);
    }

    @Test
    public void removeStringBlock() {
        String block = "String ret = \"<table class=\\\"restTablica\\\" >\";";
        block = deleteHelper.deleteOkLines(block);
        String expected = "";
        assertEquals(expected, block);
    }

    @Test
    public void removeFinallyBlock(){
        String block = "@Override\n" +
                "    public void run() {\n" +
                "        nazivDatotekeZaSerijalizaciju = konfig.dajPostavku(\"datoteka.evidencije.rada\");\n" +
                "        int intervalZaSerijalizaciju = Integer.parseInt(konfig.dajPostavku(\"interval.za.serijalizaciju\"));\n" +
                "        \n" +
                "        while(!krajRada) {\n" +
                "            long pocetak = System.currentTimeMillis();\n" +
                "            System.out.println(\"Dretva: \" + nazivDretve + \" Početak: \" + pocetak);\n" +
                "            ObjectOutputStream objectOutputStream = null;\n" +
                "            try {\n" +
                "                File f = new File(nazivDatotekeZaSerijalizaciju);\n" +
                "                objectOutputStream = new ObjectOutputStream(new FileOutputStream(f));\n" +
                "                objectOutputStream.writeObject(ServerSustava.evidencija);\n" +
                "                objectOutputStream.close();\n" +
                "            } catch (FileNotFoundException ex) {\n" +
                "                Logger.getLogger(SerijalizatorEvidencije.class.getName()).log(Level.SEVERE, null, ex);\n" +
                "            } catch (IOException ex) {\n" +
                "                    Logger.getLogger(SerijalizatorEvidencije.class.getName()).log(Level.SEVERE, null, ex);\n" +
                "            } finally {\n" +
                "                try {\n" +
                "                    objectOutputStream.close();\n" +
                "                } catch (IOException ex) {\n" +
                "                    Logger.getLogger(SerijalizatorEvidencije.class.getName()).log(Level.SEVERE, null, ex);\n" +
                "                }\n" +
                "            }\n" +
                "            long razlika = System.currentTimeMillis() - pocetak;\n" +
                "            try {\n" +
                "                Thread.sleep(intervalZaSerijalizaciju * 1000 - razlika);\n" +
                "            } catch (InterruptedException ex) {\n" +
                "                Logger.getLogger(SerijalizatorEvidencije.class.getName()).log(Level.SEVERE, null, ex);\n" +
                "            }\n" +
                "            ServerSustava.evidencija.setUkupnoVrijemeRadaRadnihDretvi(ServerSustava.evidencija.getUkupnoVrijemeRadaRadnihDretvi()+razlika);\n" +
                "            ServerSustava.azurirajEvidenciju(\"brojObavljenihSerijalizacija\");\n" +
                "\n {" +
                "finally {something}";
        block = deleteHelper.deleteOkLines(block);
        String expected = "public void run() {\n" +
                "while(!krajRada) {\n" +
                "\n" +
                "            try {} catch (InterruptedException ex) {}\n" +
                "\n" +
                " {finally {}";
        assertEquals(expected, block);
        JavaCodePartsRemover javaCodePartsRemover = new JavaCodePartsRemover();
        block = javaCodePartsRemover.removeEmptyLoopBlockAndSimilar(block);
        block = javaCodePartsRemover.removeEmptyElseFinallyStatement(block);
        block = javaCodePartsRemover.removeLeftoverWhiteSpaces(block);
        expected = "public void run() {\n" +
                "while(!krajRada) {\n" +
                "\n" +
                "            try {} catch (InterruptedException ex) {}\n" +
                "\n" +
                " {";
        assertEquals(expected, block);
    }

    @Test
    public void removeFunctionsWithGenerics(){
        String block = "public <T> T deleteJson(Class<T> responseType) throws ClientErrorException {\n" +
                "        return webTarget.request().delete(responseType);      \n" +
                "    }";
        block = deleteHelper.deleteOkLines(block);
        String expected = "";
        assertEquals(expected, block);
    }

    @Test
    public void systemShouldBeDeletedAndJavaUtilPropertiesLeveArrayInitialization(){
    String block = "public List<Poruka> getPoruke() throws IOException {\n" +
            "        //TODO optimizirati broj čitanja prouka\n" +
            "        System.out.println(\"getPoruka\");\n" +
            "        try {\n" +
            "            // Start the session\n" +
            "            java.util.Properties properties = System.getProperties();\n" +
            "            properties.put(\"mail.smtp.host\", server);\n" +
            "            Session session = Session.getInstance(properties, null);\n" +
            "\n" +
            "            // Connect to the store\n" +
            "            Store store = session.getStore(\"imap\");\n" +
            "            store.connect(server, korisnik, lozinka);\n" +
            "\n" +
            "            // Open the INBOX folder\n" +
            "            Folder folder = store.getFolder(odabranaMapa);\n" +
            "            folder.open(Folder.READ_ONLY);\n" +
            "\n" +
            "            Message[] messages = folder.getMessages();\n" +
            "            poruke = new ArrayList<>();\n" +
            "            // Print each message\n" +
            "            for (int i = 0; i < messages.length; ++i) {\n" +
            "                MimeMessage m = (MimeMessage) messages[i];\n" +
            "                String tip = m.getContentType().toLowerCase();";
        block = deleteHelper.deleteOkLines(block);
        String expected = "public List<Poruka> getPoruke() throws IOException {\n" +
                "        try {\n" +
                "\n" +
                "            for (int i = 0; i < messages.length; ++i) {";
        assertEquals(expected, block);
    }

    @Test
    public void deleteCommmentsBug() {
        String block = "//printData(\"Getting the Inbox folder.\");\n" +
                "            \n" +
                "            /*printData(\"----Lista foldera:\");\n" +
                "            for(Folder f : folder.list()) {\n" +
                "                printData(f.getName());\n" +
                "            }*/";
        String expected = "";
        block = deleteHelper.deleteOkLines(block);
        assertEquals(expected, block);
    }

    @Test
    public void commentBugWithLoop() {
        String block =
                "            //for (int i = stranicenjePoc; i < stranicenjeKraj; ++i) {\n" +
                "            "+
                "        }";
        String expected = block;
        JavaCodePartsRemover jcpr = new JavaCodePartsRemover();
        block = jcpr.removeEmptyLoopBlockAndSimilar(block);
        assertEquals(expected, block);
    }

    @Test
    public void canDeleteOkARealComplexExampleOfCode(){
        String block = "* To change this license header, choose License Headers in Project Properties.\n" +
                " * To change this template file, choose Tools | Templates\n" +
                " * and open the template in the editor.\n" +
                " */\n" +
                "package org.foi.nwtis.student2.ejb.eb;\n" +
                "\n" +
                "import java.io.Serializable;\n" +
                "import java.util.Date;\n" +
                "import javax.persistence.Basic;\n" +
                "import javax.persistence.Column;\n" +
                "import javax.persistence.Entity;\n" +
                "import javax.persistence.GeneratedValue;\n" +
                "import javax.persistence.GenerationType;\n" +
                "import javax.persistence.Id;\n" +
                "import javax.persistence.NamedQueries;\n" +
                "import javax.persistence.NamedQuery;\n" +
                "import javax.persistence.Table;\n" +
                "import javax.persistence.Temporal;\n" +
                "import javax.persistence.TemporalType;\n" +
                "import javax.validation.constraints.NotNull;\n" +
                "import javax.validation.constraints.Size;\n" +
                "import javax.xml.bind.annotation.XmlRootElement;\n" +
                "\n" +
                "/**\n" +
                " *\n" +
                " * @author NWTiS_1\n" +
                " */\n" +
                "@Entity\n" +
                "@Table(name = \"DNEVNIK\")\n" +
                "@XmlRootElement\n" +
                "@NamedQueries({\n" +
                "    @NamedQuery(name = \"Dnevnik.findAll\", query = \"SELECT d FROM Dnevnik d\"),\n" +
                "    @NamedQuery(name = \"Dnevnik.findById\", query = \"SELECT d FROM Dnevnik d WHERE d.id = :id\"),\n" +
                "    @NamedQuery(name = \"Dnevnik.findByKorisnik\", query = \"SELECT d FROM Dnevnik d WHERE d.korisnik = :korisnik\"),\n" +
                "    @NamedQuery(name = \"Dnevnik.findByUrl\", query = \"SELECT d FROM Dnevnik d WHERE d.url = :url\"),\n" +
                "    @NamedQuery(name = \"Dnevnik.findByIpadresa\", query = \"SELECT d FROM Dnevnik d WHERE d.ipadresa = :ipadresa\"),\n" +
                "    @NamedQuery(name = \"Dnevnik.findByVrijeme\", query = \"SELECT d FROM Dnevnik d WHERE d.vrijeme = :vrijeme\"),\n" +
                "    @NamedQuery(name = \"Dnevnik.findByTrajanje\", query = \"SELECT d FROM Dnevnik d WHERE d.trajanje = :trajanje\"),\n" +
                "    @NamedQuery(name = \"Dnevnik.findByStatus\", query = \"SELECT d FROM Dnevnik d WHERE d.status = :status\")})\n" +
                "public class Dnevnik implements Serializable {\n" +
                "    private static final long serialVersionUID = 1L;\n" +
                "    @Id\n" +
                "    @GeneratedValue(strategy = GenerationType.IDENTITY)\n" +
                "    @Basic(optional = false)\n" +
                "    @Column(name = \"ID\")\n" +
                "    private Integer id;\n" +
                "    @Basic(optional = false)\n" +
                "    @NotNull\n" +
                "    @Size(min = 1, max = 25)\n" +
                "    @Column(name = \"KORISNIK\")\n" +
                "    private String korisnik;\n" +
                "    @Basic(optional = false)\n" +
                "    @NotNull\n" +
                "    @Size(min = 1, max = 255)\n" +
                "    @Column(name = \"URL\")\n" +
                "    private String url;\n" +
                "    @Basic(optional = false)\n" +
                "    @NotNull\n" +
                "    @Size(min = 1, max = 25)\n" +
                "    @Column(name = \"IPADRESA\")\n" +
                "    private String ipadresa;\n" +
                "    @Column(name = \"VRIJEME\")\n" +
                "    @Temporal(TemporalType.TIMESTAMP)\n" +
                "    private Date vrijeme;\n" +
                "    @Basic(optional = false)\n" +
                "    @NotNull\n" +
                "    @Column(name = \"TRAJANJE\")\n" +
                "    private int trajanje;\n" +
                "    @Basic(optional = false)\n" +
                "    @NotNull\n" +
                "    @Column(name = \"STATUS\")\n" +
                "    private int status;\n" +
                "\n" +
                "    public Dnevnik() {\n" +
                "    }\n" +
                "\n" +
                "    public Dnevnik(Integer id) {\n" +
                "        this.id = id;\n" +
                "    }\n" +
                "\n" +
                "    public Dnevnik(Integer id, String korisnik, String url, String ipadresa, int trajanje, int status) {\n" +
                "        this.id = id;\n" +
                "        this.korisnik = korisnik;\n" +
                "        this.url = url;\n" +
                "        this.ipadresa = ipadresa;\n" +
                "        this.trajanje = trajanje;\n" +
                "        this.status = status;\n" +
                "    }\n" +
                "\n" +
                "    public Integer getId() {\n" +
                "        return id;\n" +
                "    }\n" +
                "\n" +
                "    public void setId(Integer id) {\n" +
                "        this.id = id;\n" +
                "    }\n" +
                "\n" +
                "    public String getKorisnik() {\n" +
                "        return korisnik;\n" +
                "    }\n" +
                "\n" +
                "    public void setKorisnik(String korisnik) {\n" +
                "        this.korisnik = korisnik;\n" +
                "    }\n" +
                "\n" +
                "    public String getUrl() {\n" +
                "        return url;\n" +
                "    }\n" +
                "\n" +
                "    public void setUrl(String url) {\n" +
                "        this.url = url;\n" +
                "    }\n" +
                "\n" +
                "    public String getIpadresa() {\n" +
                "        return ipadresa;\n" +
                "    }\n" +
                "\n" +
                "    public void setIpadresa(String ipadresa) {\n" +
                "        this.ipadresa = ipadresa;\n" +
                "    }\n" +
                "\n" +
                "    public Date getVrijeme() {\n" +
                "        return vrijeme;\n" +
                "    }\n" +
                "\n" +
                "    public void setVrijeme(Date vrijeme) {\n" +
                "        this.vrijeme = vrijeme;\n" +
                "    }\n" +
                "\n" +
                "    public int getTrajanje() {\n" +
                "        return trajanje;\n" +
                "    }\n" +
                "\n" +
                "    public void setTrajanje(int trajanje) {\n" +
                "        this.trajanje = trajanje;\n" +
                "    }\n" +
                "\n" +
                "    public int getStatus() {\n" +
                "        return status;\n" +
                "    }\n" +
                "\n" +
                "    public void setStatus(int status) {\n" +
                "        this.status = status;\n" +
                "    }\n" +
                "\n" +
                "    @Override\n" +
                "    public int hashCode() {\n" +
                "        int hash = 0;\n" +
                "        hash += (id != null ? id.hashCode() : 0);\n" +
                "        return hash;\n" +
                "    }\n" +
                "\n" +
                "    @Override\n" +
                "    public boolean equals(Object object) {\n" +
                "        // TODO: Warning - this method won't work in the case the id fields are not set\n" +
                "        if (!(object instanceof Dnevnik)) {\n" +
                "            return false;\n" +
                "        }\n" +
                "        Dnevnik other = (Dnevnik) object;\n" +
                "        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {\n" +
                "            return false;\n" +
                "        }\n" +
                "        return true;\n" +
                "    }\n" +
                "\n" +
                "    @Override\n" +
                "    public String toString() {\n" +
                "        return \"org.foi.nwtis.student2.ejb.eb.Dnevnik[ id=\" + id + \" ]\";\n" +
                "    }";
        block = deleteHelper.deleteOkLines(block);
        String expected = "*/\n\npublic class Dnevnik implements Serializable {";
        assertEquals(expected, block);
    }

    @Test
    public void canDeleteOkARealComplexExampleOfCode2(){
    String block = "    public LiveWeatherData dajMeteoPodatke(String zip) {\n"+
            "\n"+
            "        \n"+
            "        return getLiveWeatherByUSZipCode(zip, UnitType.METRIC, wb_code);\n"+
            "    }\n"+
            "\n"+
            "    private LiveWeatherData getLiveWeatherByUSZipCode(java.lang.String zipCode, net.wxbug.api.UnitType unittype, java.lang.String aCode) {\n"+
            "        net.wxbug.api.WeatherBugWebServicesSoap port = service.getWeatherBugWebServicesSoap();\n"+
            "        return port.getLiveWeatherByUSZipCode(zipCode, unittype, aCode);\n"+
            "    }\n"+
            "}\n"+
            "package net.wxbug.api;\n"+
            "\n"+
            "import javax.xml.bind.annotation.XmlAccessType;\n"+
            "import javax.xml.bind.annotation.XmlAccessorType;\n"+
            "import javax.xml.bind.annotation.XmlElement;\n"+
            "import javax.xml.bind.annotation.XmlSchemaType;\n"+
            "import javax.xml.bind.annotation.XmlType;\n"+
            "import javax.xml.datatype.XMLGregorianCalendar;\n"+
            "\n"+
            "\n"+
            "/**\n" +
            " * <p>Java class for LiveWeatherData complex type.\n" +
            " * \n" +
            " * <p>The following schema fragment specifies the expected content contained within this class.\n" +
            " * \n" +
            " * <pre>\n" +
            " * &lt;complexType name=\"LiveWeatherData\">\n" +
            " *   &lt;complexContent>\n" +
            " *     &lt;restriction base=\"{http://www.w3.org/2001/XMLSchema}anyType\">\n" +
            " *       &lt;sequence>\n" +
            " *         &lt;element name=\"AuxTemperature\" type=\"{http://www.w3.org/2001/XMLSchema}string\" minOccurs=\"0\"/>\n" +
            " *         &lt;element name=\"AuxTemperatureRate\" type=\"{http://www.w3.org/2001/XMLSchema}string\" minOccurs=\"0\"/>\n" +
            " *         &lt;element name=\"City\" type=\"{http://www.w3.org/2001/XMLSchema}string\" minOccurs=\"0\"/>\n" +
            " *         &lt;element name=\"CityCode\" type=\"{http://www.w3.org/2001/XMLSchema}string\" minOccurs=\"0\"/>\n" +
            " *         &lt;element name=\"Country\" type=\"{http://www.w3.org/2001/XMLSchema}string\" minOccurs=\"0\"/>\n" +
            " *         &lt;element name=\"CurrIcon\" type=\"{http://www.w3.org/2001/XMLSchema}string\" minOccurs=\"0\"/>\n" +
            " *         &lt;element name=\"CurrDesc\" type=\"{http://www.w3.org/2001/XMLSchema}string\" minOccurs=\"0\"/>\n" +
            " *         &lt;element name=\"DewPoint\" type=\"{http://www.w3.org/2001/XMLSchema}string\" minOccurs=\"0\"/>\n" +
            " *         &lt;element name=\"Elevation\" type=\"{http://www.w3.org/2001/XMLSchema}int\"/>\n" +
            " *         &lt;element name=\"ElevationUnit\" type=\"{http://www.w3.org/2001/XMLSchema}string\" minOccurs=\"0\"/>\n" +
            " *         &lt;element name=\"FeelsLike\" type=\"{http://www.w3.org/2001/XMLSchema}string\" minOccurs=\"0\"/>\n" +
            " *         &lt;element name=\"GustTime\" type=\"{http://www.w3.org/2001/XMLSchema}dateTime\"/>\n" +
            " *         &lt;element name=\"GustWindSpeed\" type=\"{http://www.w3.org/2001/XMLSchema}string\" minOccurs=\"0\"/>\n" +
            " *         &lt;element name=\"GustWindSpeedUnit\" type=\"{http://www.w3.org/2001/XMLSchema}string\" minOccurs=\"0\"/>\n" +
            " *         &lt;element name=\"GustWindDirectionString\" type=\"{http://www.w3.org/2001/XMLSchema}string\" minOccurs=\"0\"/>\n" +
            " *         &lt;element name=\"GustWindDirectionDegrees\" type=\"{http://www.w3.org/2001/XMLSchema}string\" minOccurs=\"0\"/>\n" +
            " *         &lt;element name=\"Humidity\" type=\"{http://www.w3.org/2001/XMLSchema}string\" minOccurs=\"0\"/>\n" +
            " *         &lt;element name=\"HumidityUnit\" type=\"{http://www.w3.org/2001/XMLSchema}string\" minOccurs=\"0\"/>\n" +
            " *         &lt;element name=\"HumidityHigh\" type=\"{http://www.w3.org/2001/XMLSchema}string\" minOccurs=\"0\"/>\n" +
            " *         &lt;element name=\"HumidityLow\" type=\"{http://www.w3.org/2001/XMLSchema}string\" minOccurs=\"0\"/>\n" +
            " *         &lt;element name=\"HumidityRate\" type=\"{http://www.w3.org/2001/XMLSchema}string\" minOccurs=\"0\"/>\n" +
            " *         &lt;element name=\"InputLocationUrl\" type=\"{http://www.w3.org/2001/XMLSchema}string\" minOccurs=\"0\"/>\n" +
            " *         &lt;element name=\"MoonPhase\" type=\"{http://www.w3.org/2001/XMLSchema}int\"/>\n" +
            " *         &lt;element name=\"MoonPhaseImage\" type=\"{http://www.w3.org/2001/XMLSchema}string\" minOccurs=\"0\"/>\n" +
            " *         &lt;element name=\"Pressure\" type=\"{http://www.w3.org/2001/XMLSchema}string\" minOccurs=\"0\"/>\n" +
            " *         &lt;element name=\"PressureUnit\" type=\"{http://www.w3.org/2001/XMLSchema}string\" minOccurs=\"0\"/>\n" +
            " *         &lt;element name=\"PressureHigh\" type=\"{http://www.w3.org/2001/XMLSchema}string\" minOccurs=\"0\"/>\n" +
            " *         &lt;element name=\"PressureLow\" type=\"{http://www.w3.org/2001/XMLSchema}string\" minOccurs=\"0\"/>\n" +
            " *         &lt;element name=\"PressureRate\" type=\"{http://www.w3.org/2001/XMLSchema}string\" minOccurs=\"0\"/>\n" +
            " *         &lt;element name=\"PressureRateUnit\" type=\"{http://www.w3.org/2001/XMLSchema}string\" minOccurs=\"0\"/>\n" +
            " *         &lt;element name=\"Light\" type=\"{http://www.w3.org/2001/XMLSchema}string\" minOccurs=\"0\"/>\n" +
            " *         &lt;element name=\"LightRate\" type=\"{http://www.w3.org/2001/XMLSchema}string\" minOccurs=\"0\"/>\n" +
            " *         &lt;element name=\"IndoorTemperature\" type=\"{http://www.w3.org/2001/XMLSchema}string\" minOccurs=\"0\"/>\n" +
            " *         &lt;element name=\"IndoorTemperatureRate\" type=\"{http://www.w3.org/2001/XMLSchema}string\" minOccurs=\"0\"/>\n" +
            " *         &lt;element name=\"Latitude\" type=\"{http://www.w3.org/2001/XMLSchema}double\"/>\n" +
            " *         &lt;element name=\"Longitude\" type=\"{http://www.w3.org/2001/XMLSchema}double\"/>\n" +
            " *         &lt;element name=\"ObDate\" type=\"{http://www.w3.org/2001/XMLSchema}string\" minOccurs=\"0\"/>\n" +
            " *         &lt;element name=\"ObDateTime\" type=\"{http://www.w3.org/2001/XMLSchema}dateTime\"/>\n" +
            " *         &lt;element name=\"RainMonth\" type=\"{http://www.w3.org/2001/XMLSchema}string\" minOccurs=\"0\"/>\n" +
            " *         &lt;element name=\"RainRate\" type=\"{http://www.w3.org/2001/XMLSchema}string\" minOccurs=\"0\"/>\n" +
            " *         &lt;element name=\"RainRateMax\" type=\"{http://www.w3.org/2001/XMLSchema}string\" minOccurs=\"0\"/>\n" +
            " *         &lt;element name=\"RainRateUnit\" type=\"{http://www.w3.org/2001/XMLSchema}string\" minOccurs=\"0\"/>\n" +
            " *         &lt;element name=\"RainToday\" type=\"{http://www.w3.org/2001/XMLSchema}string\" minOccurs=\"0\"/>\n" +
            " *         &lt;element name=\"RainUnit\" type=\"{http://www.w3.org/2001/XMLSchema}string\" minOccurs=\"0\"/>\n" +
            " *         &lt;element name=\"RainYear\" type=\"{http://www.w3.org/2001/XMLSchema}string\" minOccurs=\"0\"/>\n" +
            " *         &lt;element name=\"State\" type=\"{http://www.w3.org/2001/XMLSchema}string\" minOccurs=\"0\"/>\n" +
            " *         &lt;element name=\"StationIDRequested\" type=\"{http://www.w3.org/2001/XMLSchema}string\" minOccurs=\"0\"/>\n" +
            " *         &lt;element name=\"StationIDReturned\" type=\"{http://www.w3.org/2001/XMLSchema}string\" minOccurs=\"0\"/>\n" +
            " *         &lt;element name=\"StationName\" type=\"{http://www.w3.org/2001/XMLSchema}string\" minOccurs=\"0\"/>\n" +
            " *         &lt;element name=\"StationURL\" type=\"{http://www.w3.org/2001/XMLSchema}string\" minOccurs=\"0\"/>\n" +
            " *         &lt;element name=\"Sunrise\" type=\"{http://www.w3.org/2001/XMLSchema}dateTime\"/>\n" +
            " *         &lt;element name=\"Sunset\" type=\"{http://www.w3.org/2001/XMLSchema}dateTime\"/>\n" +
            " *         &lt;element name=\"Temperature\" type=\"{http://www.w3.org/2001/XMLSchema}string\" minOccurs=\"0\"/>\n" +
            " *         &lt;element name=\"TemperatureHigh\" type=\"{http://www.w3.org/2001/XMLSchema}string\" minOccurs=\"0\"/>\n" +
            " *         &lt;element name=\"TemperatureLow\" type=\"{http://www.w3.org/2001/XMLSchema}string\" minOccurs=\"0\"/>\n" +
            " *         &lt;element name=\"TemperatureRate\" type=\"{http://www.w3.org/2001/XMLSchema}string\" minOccurs=\"0\"/>\n" +
            " *         &lt;element name=\"TemperatureRateUnit\" type=\"{http://www.w3.org/2001/XMLSchema}string\" minOccurs=\"0\"/>\n" +
            " *         &lt;element name=\"TemperatureUnit\" type=\"{http://www.w3.org/2001/XMLSchema}string\" minOccurs=\"0\"/>\n" +
            " *         &lt;element name=\"TimeZone\" type=\"{http://www.w3.org/2001/XMLSchema}string\" minOccurs=\"0\"/>\n" +
            " *         &lt;element name=\"TimeZoneOffset\" type=\"{http://www.w3.org/2001/XMLSchema}double\"/>\n" +
            " *         &lt;element name=\"WebUrl\" type=\"{http://www.w3.org/2001/XMLSchema}string\" minOccurs=\"0\"/>\n" +
            " *         &lt;element name=\"WetBulb\" type=\"{http://www.w3.org/2001/XMLSchema}string\" minOccurs=\"0\"/>\n" +
            " *         &lt;element name=\"WindDirection\" type=\"{http://www.w3.org/2001/XMLSchema}string\" minOccurs=\"0\"/>\n" +
            " *         &lt;element name=\"WindDirectionAvg\" type=\"{http://www.w3.org/2001/XMLSchema}string\" minOccurs=\"0\"/>\n" +
            " *         &lt;element name=\"WindDirectionDegrees\" type=\"{http://www.w3.org/2001/XMLSchema}string\" minOccurs=\"0\"/>\n" +
            " *         &lt;element name=\"WindSpeed\" type=\"{http://www.w3.org/2001/XMLSchema}string\" minOccurs=\"0\"/>\n" +
            " *         &lt;element name=\"WindSpeedAvg\" type=\"{http://www.w3.org/2001/XMLSchema}string\" minOccurs=\"0\"/>\n" +
            " *         &lt;element name=\"WindSpeedUnit\" type=\"{http://www.w3.org/2001/XMLSchema}string\" minOccurs=\"0\"/>\n" +
            " *         &lt;element name=\"ZipCode\" type=\"{http://www.w3.org/2001/XMLSchema}string\" minOccurs=\"0\"/>\n" +
            " *       &lt;/sequence>\n" +
            " *     &lt;/restriction>\n" +
            " *   &lt;/complexContent>\n" +
            " * &lt;/complexType>\n" +
            " * </pre>\n" +
            " * \n" +
            " * \n" +
            " */"+
            "@XmlAccessorType(XmlAccessType.FIELD)\n"+
            "@XmlType(name = \"LiveWeatherData\", propOrder = {\n"+
            "    \"auxTemperature\",\n"+
            "    \"auxTemperatureRate\",\n"+
            "    \"city\",\n"+
            "    \"cityCode\",\n"+
            "    \"country\",\n"+
            "    \"currIcon\",\n"+
            "    \"currDesc\",\n"+
            "    \"dewPoint\",\n"+
            "    \"elevation\",\n"+
            "    \"elevationUnit\",\n"+
            "    \"feelsLike\",\n"+
            "    \"gustTime\",\n"+
            "    \"gustWindSpeed\",\n"+
            "    \"gustWindSpeedUnit\",\n"+
            "    \"gustWindDirectionString\",\n"+
            "    \"gustWindDirectionDegrees\",\n"+
            "    \"humidity\",\n"+
            "    \"humidityUnit\",\n"+
            "    \"humidityHigh\",\n"+
            "    \"humidityLow\",\n"+
            "    \"humidityRate\",\n"+
            "    \"inputLocationUrl\",\n"+
            "    \"moonPhase\",\n"+
            "    \"moonPhaseImage\",\n"+
            "    \"pressure\",\n"+
            "    \"pressureUnit\",\n"+
            "    \"pressureHigh\",\n"+
            "    \"pressureLow\",\n"+
            "    \"pressureRate\",\n"+
            "    \"pressureRateUnit\",\n"+
            "    \"light\",\n"+
            "    \"lightRate\",\n"+
            "    \"indoorTemperature\",\n"+
            "    \"indoorTemperatureRate\",\n"+
            "    \"latitude\",\n"+
            "    \"longitude\",\n"+
            "    \"obDate\",\n"+
            "    \"obDateTime\",\n"+
            "    \"rainMonth\",\n"+
            "    \"rainRate\",\n"+
            "    \"rainRateMax\",\n"+
            "    \"rainRateUnit\",\n"+
            "    \"rainToday\",\n"+
            "    \"rainUnit\",\n"+
            "    \"rainYear\",\n"+
            "    \"state\",\n"+
            "    \"stationIDRequested\",\n"+
            "    \"stationIDReturned\",\n"+
            "    \"stationName\",\n"+
            "    \"stationURL\",\n"+
            "    \"sunrise\",\n"+
            "    \"sunset\",\n"+
            "    \"temperature\",\n"+
            "    \"temperatureHigh\",\n"+
            "    \"temperatureLow\",\n"+
            "    \"temperatureRate\",\n"+
            "    \"temperatureRateUnit\",\n"+
            "    \"temperatureUnit\",\n"+
            "    \"timeZone\",\n"+
            "    \"timeZoneOffset\",\n"+
            "    \"webUrl\",\n"+
            "    \"wetBulb\",\n"+
            "    \"windDirection\",\n"+
            "    \"windDirectionAvg\",\n"+
            "    \"windDirectionDegrees\",\n"+
            "    \"windSpeed\",\n"+
            "    \"windSpeedAvg\",\n"+
            "    \"windSpeedUnit\",\n"+
            "    \"zipCode\"\n"+
            "})\n"+
            "public class LiveWeatherData {\n"+
            "\n"+
            "    @XmlElement(name = \"AuxTemperature\")\n"+
            "    protected String auxTemperature;\n"+
            "    @XmlElement(name = \"AuxTemperatureRate\")\n"+
            "    protected String auxTemperatureRate;\n"+
            "    @XmlElement(name = \"City\")\n"+
            "    protected String city;\n"+
            "    @XmlElement(name = \"CityCode\")\n"+
            "    protected String cityCode;\n"+
            "    @XmlElement(name = \"Country\")\n"+
            "    protected String country;\n"+
            "    @XmlElement(name = \"CurrIcon\")\n"+
            "    protected String currIcon;\n"+
            "    @XmlElement(name = \"CurrDesc\")\n"+
            "    protected String currDesc;\n"+
            "    @XmlElement(name = \"DewPoint\")\n"+
            "    protected String dewPoint;\n"+
            "    @XmlElement(name = \"Elevation\")\n"+
            "    protected int elevation;\n"+
            "    @XmlElement(name = \"ElevationUnit\")\n"+
            "    protected String elevationUnit;\n"+
            "    @XmlElement(name = \"FeelsLike\")\n"+
            "    protected String feelsLike;\n"+
            "    @XmlElement(name = \"GustTime\", required = true)\n"+
            "    @XmlSchemaType(name = \"dateTime\")\n"+
            "    protected XMLGregorianCalendar gustTime;\n"+
            "    @XmlElement(name = \"GustWindSpeed\")\n"+
            "    protected String gustWindSpeed;\n"+
            "    @XmlElement(name = \"GustWindSpeedUnit\")\n"+
            "    protected String gustWindSpeedUnit;\n"+
            "    @XmlElement(name = \"GustWindDirectionString\")\n"+
            "    protected String gustWindDirectionString;\n"+
            "    @XmlElement(name = \"GustWindDirectionDegrees\")\n"+
            "    protected String gustWindDirectionDegrees;\n"+
            "    @XmlElement(name = \"Humidity\")\n"+
            "    protected String humidity;\n"+
            "    @XmlElement(name = \"HumidityUnit\")\n"+
            "    protected String humidityUnit;\n"+
            "    @XmlElement(name = \"HumidityHigh\")\n"+
            "    protected String humidityHigh;\n"+
            "    @XmlElement(name = \"HumidityLow\")\n"+
            "    protected String humidityLow;\n"+
            "    @XmlElement(name = \"HumidityRate\")\n"+
            "    protected String humidityRate;\n"+
            "    @XmlElement(name = \"InputLocationUrl\")\n"+
            "    protected String inputLocationUrl;\n"+
            "    @XmlElement(name = \"MoonPhase\")\n"+
            "    protected int moonPhase;\n"+
            "    @XmlElement(name = \"MoonPhaseImage\")\n"+
            "    protected String moonPhaseImage;\n"+
            "    @XmlElement(name = \"Pressure\")\n"+
            "    protected String pressure;\n"+
            "    @XmlElement(name = \"PressureUnit\")\n"+
            "    protected String pressureUnit;\n"+
            "    @XmlElement(name = \"PressureHigh\")\n"+
            "    protected String pressureHigh;\n"+
            "    @XmlElement(name = \"PressureLow\")\n"+
            "    protected String pressureLow;\n"+
            "    @XmlElement(name = \"PressureRate\")\n"+
            "    protected String pressureRate;\n"+
            "    @XmlElement(name = \"PressureRateUnit\")\n"+
            "    protected String pressureRateUnit;\n"+
            "    @XmlElement(name = \"Light\")\n"+
            "    protected String light;\n"+
            "    @XmlElement(name = \"LightRate\")\n"+
            "    protected String lightRate;\n"+
            "    @XmlElement(name = \"IndoorTemperature\")\n"+
            "    protected String indoorTemperature;\n"+
            "    @XmlElement(name = \"IndoorTemperatureRate\")\n"+
            "    protected String indoorTemperatureRate;\n"+
            "    @XmlElement(name = \"Latitude\")\n"+
            "    protected double latitude;\n"+
            "    @XmlElement(name = \"Longitude\")\n"+
            "    protected double longitude;\n"+
            "    @XmlElement(name = \"ObDate\")\n"+
            "    protected String obDate;\n"+
            "    @XmlElement(name = \"ObDateTime\", required = true)\n"+
            "    @XmlSchemaType(name = \"dateTime\")\n"+
            "    protected XMLGregorianCalendar obDateTime;\n"+
            "    @XmlElement(name = \"RainMonth\")\n"+
            "    protected String rainMonth;\n"+
            "    @XmlElement(name = \"RainRate\")\n"+
            "    protected String rainRate;\n"+
            "    @XmlElement(name = \"RainRateMax\")\n"+
            "    protected String rainRateMax;\n"+
            "    @XmlElement(name = \"RainRateUnit\")\n"+
            "    protected String rainRateUnit;\n"+
            "    @XmlElement(name = \"RainToday\")\n"+
            "    protected String rainToday;\n"+
            "    @XmlElement(name = \"RainUnit\")\n"+
            "    protected String rainUnit;\n"+
            "    @XmlElement(name = \"RainYear\")\n"+
            "    protected String rainYear;\n"+
            "    @XmlElement(name = \"State\")\n"+
            "    protected String state;\n"+
            "    @XmlElement(name = \"StationIDRequested\")\n"+
            "    protected String stationIDRequested;\n"+
            "    @XmlElement(name = \"StationIDReturned\")\n"+
            "    protected String stationIDReturned;\n"+
            "    @XmlElement(name = \"StationName\")\n"+
            "    protected String stationName;\n"+
            "    @XmlElement(name = \"StationURL\")\n"+
            "    protected String stationURL;\n"+
            "    @XmlElement(name = \"Sunrise\", required = true)\n"+
            "    @XmlSchemaType(name = \"dateTime\")\n"+
            "    protected XMLGregorianCalendar sunrise;\n"+
            "    @XmlElement(name = \"Sunset\", required = true)\n"+
            "    @XmlSchemaType(name = \"dateTime\")\n"+
            "    protected XMLGregorianCalendar sunset;\n"+
            "    @XmlElement(name = \"Temperature\")\n"+
            "    protected String temperature;\n"+
            "    @XmlElement(name = \"TemperatureHigh\")\n"+
            "    protected String temperatureHigh;\n"+
            "    @XmlElement(name = \"TemperatureLow\")\n"+
            "    protected String temperatureLow;\n"+
            "    @XmlElement(name = \"TemperatureRate\")\n"+
            "    protected String temperatureRate;\n"+
            "    @XmlElement(name = \"TemperatureRateUnit\")\n"+
            "    protected String temperatureRateUnit;\n"+
            "    @XmlElement(name = \"TemperatureUnit\")\n"+
            "    protected String temperatureUnit;\n"+
            "    @XmlElement(name = \"TimeZone\")\n"+
            "    protected String timeZone;\n"+
            "    @XmlElement(name = \"TimeZoneOffset\")\n"+
            "    protected double timeZoneOffset;\n"+
            "    @XmlElement(name = \"WebUrl\")\n"+
            "    protected String webUrl;\n"+
            "    @XmlElement(name = \"WetBulb\")\n"+
            "    protected String wetBulb;\n"+
            "    @XmlElement(name = \"WindDirection\")\n"+
            "    protected String windDirection;\n"+
            "    @XmlElement(name = \"WindDirectionAvg\")\n"+
            "    protected String windDirectionAvg;\n"+
            "    @XmlElement(name = \"WindDirectionDegrees\")\n"+
            "    protected String windDirectionDegrees;\n"+
            "    @XmlElement(name = \"WindSpeed\")\n"+
            "    protected String windSpeed;\n"+
            "    @XmlElement(name = \"WindSpeedAvg\")\n"+
            "    protected String windSpeedAvg;\n"+
            "    @XmlElement(name = \"WindSpeedUnit\")\n"+
            "    protected String windSpeedUnit;\n"+
            "    @XmlElement(name = \"ZipCode\")\n"+
            "    protected String zipCode;\n"+
            "\n"+
            "\n"+
            "";
    block = deleteHelper.deleteOkLines(block);
    String expected = "}\n" +
            "\n" +
            "public class LiveWeatherData {";
    assertEquals(expected, block);
}
}