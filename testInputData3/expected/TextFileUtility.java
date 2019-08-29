
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.apache.derby.impl.sql.execute.SumAggregator;
import org.foi.nwtis.student3.messages.strategy.Context;
import org.foi.nwtis.student3.messages.strategy.FormatedExecutor;
import org.foi.nwtis.student3.messages.strategy.OtherExecutor;
import org.foi.nwtis.student3.utils.MessagesUtils;
import org.foi.nwtis.student3.konfiguracije.Konfiguracija;
import org.foi.nwtis.student3.models.Summary;

/**
 *
 * @author Sname1
 */
public class MessageController {
    
    private List<MimeMessage> items;
    private Konfiguracija config;
    
    public MessageController(Konfiguracija config){
        this.config=config;
        
    }
    
    public void processMessages(Message[] messages, Folder inbox, Folder other, File webPage,Summary summary) throws MessagingException{
        List<MimeMessage> items=parseMessages(messages);
        summary.setMsgNumber(items.size());
        Context context;
        inbox.open(Folder.READ_WRITE);
        other.open(Folder.READ_WRITE);
        for(MimeMessage msg:items){
            if(isItFormated(msg)){
                context=new Context(new FormatedExecutor(webPage,config,summary),msg, inbox);
            }else{
                context=new Context(new OtherExecutor(),msg, other);
            }
            context.executeStrategy();
        }
        inbox.close(true);
        other.close(true);
        
    }
    
    private boolean isItFormated(MimeMessage msg){
        String command;
        try {
            command = MessagesUtils.parseContent(msg);
        } catch (IOException ex) {
            return false;
        } catch (MessagingException ex) {
            return false;
        }
        return isItFormated(command);
    }
    private boolean isItFormated(String command){
        return (command !=null && (command.contains("ADD") || command.contains("UPDATE")))?true:false;
    }
    
    private List<MimeMessage> parseMessages(Message[] messages){
        List<MimeMessage> items = new ArrayList<>();
        for (Message message : messages) {
            MimeMessage mmsg = (MimeMessage) message;
            items.add(mmsg);
        }
        return items;
    }
    
}/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.student3.messages.strategy;

import javax.mail.Folder;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author Sname1
 */
public class Context {
     private Strategy strategy;
     private MimeMessage msg;
     private Folder folder;

    public Context(Strategy strategy, MimeMessage msg, Folder folder) {
        this.strategy = strategy;
        this.folder=folder;
        this.msg=msg;
    }

    public void executeStrategy(){
        strategy.execute(msg,folder);
    }
}/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.student3.messages.strategy;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.apache.derby.client.am.DateTime;
import org.foi.nwtis.student3.models.Command;
import org.foi.nwtis.student3.models.CommandAction;
import org.foi.nwtis.student3.utils.Constants;
import org.foi.nwtis.student3.utils.DBManager;
import org.foi.nwtis.student3.utils.MessagesUtils;
import org.foi.nwtis.student3.konfiguracije.Konfiguracija;
import org.foi.nwtis.student3.models.CommandType;
import org.foi.nwtis.student3.models.Summary;

/**
 *
 * @author Sname1
 */
public class FormatedExecutor implements Strategy {
    
    private MimeMessage msg;
    private Folder folder;
    private File webPage;
    private Konfiguracija config;
    private Summary summary;
    
    public FormatedExecutor(File file, Konfiguracija config,Summary summary){
        this.webPage=file;
        this.config=config;
        this.summary=summary;
    }

    @Override
    public void execute(MimeMessage msg, Folder folder) {
        this.msg=msg;
        this.folder=folder;
        try {
            saveMsg();
            processCommand();
            msg.setFlag(Flags.Flag.DELETED, true);
        } catch (MessagingException ex) {
            Logger.getLogger(FormatedExecutor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(FormatedExecutor.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    private void processCommand() throws MessagingException, IOException{
            Command command=new Command(msg);
            if(command.getAction()== CommandAction.ADD && !isExist(command.getContent())){
                if(command.getType().equals(CommandType.GRAD)){
                    summary.setAddGrad();
                }else{
                    summary.setAddTvrtka();
                }
                DBManager db=DBManager.getInstance(config);
                db.putElem(command.getContent());
                downloadPage(command);
            }else if(command.getAction().equals(CommandAction.UPDATE) && isExist(command.getContent())){
                if(command.getType().equals(CommandType.GRAD)){
                    summary.setUpdateGrad();
                }else{
                    summary.setUpdateTvrtka();
                }
                downloadPage(command);
            }else{
                // greska
            }
        
    }
    
    private boolean isExist(String content){
        DBManager db=DBManager.getInstance(config);
        return db.checkIfElemExist(content);
    }
    
    private void downloadPage(Command command) throws IOException{
        String time=getTime();
        if(!webPage.exists()){
            webPage.mkdir();
        }
        File file=new File(webPage.getAbsolutePath()+"/"+command.getContent()+time);
        if(!file.exists()){
            file.mkdir();
        }
        for(String prefix:Constants.prefiks){
            String url="www."+command.getContent()+prefix;
            File web=new File(file.getAbsoluteFile()+"/"+url);
            web.mkdir();
            saveUrl(web.getAbsolutePath()+"/index.html","http://"+url);
        }
            
    }
    
    public void saveUrl(String filename, String urlString)
            throws MalformedURLException, IOException {
        BufferedInputStream in = null;
        FileOutputStream fout = null;
        try {
            in = new BufferedInputStream(new URL(urlString).openStream());
            fout = new FileOutputStream(filename);
            

            final byte data[] = new byte[1024];
            int count;
            while ((count = in.read(data, 0, 1024)) != -1) {
                fout.write(data, 0, count);
            }
        } finally {
            if (in != null) {
                in.close();
            }
            if (fout != null) {
                fout.close();
            }
        }
    }
    
    private String getTime(){
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = new Date();
        return dateFormat.format(date);
    }
    
    
    private void saveMsg() throws MessagingException{
        folder.appendMessages(convertToArray());
}
}


public class OtherExecutor implements Strategy{
    
    private void saveMsg() throws MessagingException{
        folder.appendMessages(convertToArray());
    }
    
    private Message[] convertToArray(){
        Message[] msgs=new Message[1];
        msgs[0]=msg;
        return msgs;
    }    
}/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.student3.messages.strategy;

import javax.mail.Folder;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author Sname1
 */
public interface Strategy {
    public void execute(MimeMessage msg, Folder folder);
    
}/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.student3.models;

import java.io.IOException;
import javax.mail.MessagingException;
public class Command {
    private CommandAction action;
    private CommandType type;
    private String content;

    public Command(MimeMessage msg) throws IOException, MessagingException {
        this.action=MessagesUtils.getAction(msg);
        this.content=MessagesUtils.getContent(msg);
        this.type=MessagesUtils.getType(msg);
}

}
/**
 *
 * @author Sname1
 */
public enum CommandAction {
        ADD, UPDATE
}/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.student3.models;

/**
 *
 * @author Sname1
 */
public enum CommandType {
    GRAD, TVRTKA
}/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.student3.models;

import org.apache.derby.client.am.DateTime;

/**
 *
 * @author Sname1
 */
public class Datoteka {
    
    private String path;
    private String name;
    private long size;
    private DateTime dateTime;

    public String getPath() {
        return path;
}
}
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Sname1
 */
public class Summary {
    
    private Date dateStart;
    private Date dateEnd;
    private long milisec;
    private int addGrad=0;
    private int addTvrtka=0;
    private int updateGrad=0;
    private int updateTvrtka=0;
    private int msgNumber=0;
    private DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy hh.mm.ss.zzz");
    public Summary() {
        this.dateStart=new Date();

}
    public void setDateEnd() {
        Date date = new Date();
        this.dateEnd=date;
        setMilisec(dateEnd.getTime()-dateStart.getTime());
    }

    public long getMilisec() {
        return milisec;
}
}
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import org.foi.nwtis.student3.konfiguracije.Konfiguracija;
import org.foi.nwtis.student3.konfiguracije.KonfiguracijaApstraktna;
import org.foi.nwtis.student3.konfiguracije.NemaKonfiguracije;

/**
 *
 * @author Sname1
 */
public class ConfigUtils {
    
     public static Konfiguracija loadConfiguration(ServletContext context){
         return loadConfiguration(getFilePath(context));
     }
     private static Konfiguracija loadConfiguration(String filePath) {
         Konfiguracija config=null;
         try {
             config = KonfiguracijaApstraktna.preuzmiKonfiguraciju(filePath);
         } catch (NemaKonfiguracije ex) {
             Logger.getLogger(ConfigUtils.class.getName()).log(Level.SEVERE, null, ex);
         }
        return config;
    }
     
      public static String getFilePath(ServletContext context) {
        StringBuilder path = new StringBuilder();
        path.append(context.getRealPath(Constants.PATH_WEB_INF));
        path.append(File.separator); 
        path.append(context.getInitParameter(Constants.CONFIGURATION_NAME));   
        return path.toString();
    }
    
}/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.student3.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Sname1
 */
public class Constants {
    
        public static List<String> prefiks=Arrays.asList(".hr",".info", ".com", ".eu");
        
        public static final String LANG_CRO = "hr";
        public static final String LANG_ENG = "en";
        public static final String LANG_DE = "de";
        public static final String PATH_WEB_INF = "/WEB-INF";
        public static final String CONFIGURATION_NAME = "konfiguracija";
        public static final String DB_CONFIG="BP_Konfig";
        
        public static final String SMTP_HOST = "mail.smtp.host";
        public static final String STORE = "imap";
       
        
        public static final String SERVER_DATABASE = "server.database";
        public static final String ADMIN_USERNAME = "admin.username";
        public static final String ADMIN_PASSWORD = "admin.password";
        public static final String ADMIN_DATABASE = "admin.database";
        public static final String USER_USERNAME = "user.username";
        public static final String USER_PASSWORD = "user.password";
        public static final String USER_DATABASE = "user.database";
        public static final String DRIVER_ODBC = "driver.database.odbc";
        public static final String DRIVER_MYSQL = "driver.database.mysql";
        public static final String DRIVER_DERBY = "driver.database.derby";
        public static final String DRIVER_POSTGRES = "driver.database.postgresql";
       
        public static final String FOLDER_WEB="content.directory";
        public static final String FOLDER_INBOX = "INBOX";
        public static final String MAIL_INTERVAL = "mail.interval";
        public static final String MAIL_SERVER_ADDRESS = "mail.server.address";
        public static final String MAIL_SERVER_PORT = "mail.server.port";
        public static final String MAIL_USER_ADDRESS = "mail.user.address";
        public static final String MAIL_USER_PASS = "mail.user.password";
        public static final String MAIL_SUBJECT = "mail.subject";
        public static final String MAIL_INBOX_FORMATTED = "mail.inbox.formatted";
        public static final String MAIL_INBOX_OTHER = "mail.inbox.other";
  
    
}/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.student3.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.student3.konfiguracije.Konfiguracija;

/**
 *
 * @author Sname1
 */
public class DBManager {
    
    private static DBManager INSTANCA;
    private Konfiguracija config;


    public DBManager(Konfiguracija config) {
        this.config = config;
    }

    public static DBManager getInstance(Konfiguracija config) {
        if (INSTANCA == null) {
            INSTANCA = new DBManager(config);
        }
        return INSTANCA;
    }
    public Connection getSQLConnection() {
        String user = config.dajPostavku(Constants.ADMIN_USERNAME);
        String password = config.dajPostavku(Constants.ADMIN_PASSWORD);
        
        String server = config.dajPostavku(Constants.SERVER_DATABASE);
        String database = config.dajPostavku(Constants.ADMIN_DATABASE);
        String driver = config.dajPostavku(Constants.DRIVER_DERBY);
            
        try {
            return DriverManager.getConnection(server + database, user, password);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }
    
    
    public boolean checkIfElemExist(String elem) {
        String upit = "SELECT name from Elementi where name='"+elem+"'";
        Connection sqlConnection = this.getSQLConnection();
        try (
            ResultSet result = sqlConnection.createStatement().executeQuery(upit);) {
            while (result.next()) {
                return true;
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public void putElem(String elem){
        String upit = "insert into Elementi VALUES ('"+elem+"')";
        Connection sqlConnection = this.getSQLConnection();
        try { 
            sqlConnection.createStatement().executeUpdate(upit);
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
            
        }
    

}/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.student3.utils;

import org.foi.nwtis.student3.konfiguracije.Konfiguracija;

/**
 *
 * @author Sname1
 */
public class Konekcija {
    
    private String server;
    private String user;
    private String password;

    public Konekcija() {
}
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.student3.utils;

import javax.mail.Folder;
import javax.mail.MessagingException;
import javax.mail.Store;

/**
 *
 * @author Sname1
 */
public class KonekcijaUtils {
    
     public synchronized static Folder createFolder(Store store, String folder) throws MessagingException {
        Folder defaultFolder = store.getDefaultFolder();
        Folder messageFolder = defaultFolder.getFolder(folder);
        if (!messageFolder.exists()) {
            messageFolder.create(Folder.HOLDS_MESSAGES);
        }
        return messageFolder;
    }
     
      public static Folder openFolder(Store store, String folder) throws MessagingException {
        Folder inboxFolder = store.getFolder(folder);
        inboxFolder.open(Folder.READ_ONLY);
        return inboxFolder;
    }
    
}/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.student3.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.foi.nwtis.student3.models.CommandAction;
import org.foi.nwtis.student3.models.CommandType;

/**
 *
 * @author Sname1
 */
public class MessagesUtils {
    
 public static String parseContent(MimeMessage msg) throws IOException, MessagingException {
        if (msg.getContent() == null) {
            return null;
        }
        int limit = msg.getContent().toString().lastIndexOf(";");
        limit = limit > 0 ? limit + 1 : 0;
        return msg.getContent().toString().substring(0, limit);
    }      
    public static CommandAction getAction(MimeMessage msg) throws IOException, MessagingException{
         if (msg.getContent() == null) {
            return null;
        }
        int end = msg.getContent().toString().lastIndexOf(";");
        int begin=msg.getContent().toString().indexOf(";");
        end = end > 0 ? end + 1 : 0;
        begin = begin > 0 ? begin + 1 : 0;
        return msg.getContent().toString().substring(begin, end).contains("ADD")?CommandAction.ADD:CommandAction.UPDATE;
    }
    
    public static String getContent(MimeMessage msg) throws IOException, MessagingException{
         if (msg.getContent() == null) {
            return null;
        }
        int end = msg.getContent().toString().indexOf(";");
        int begin=msg.getContent().toString().indexOf(" ");
        end = end > 0 ? end : 0;
        begin = begin > 0 ? begin + 1 : 0;
        return msg.getContent().toString().substring(begin, end);
    }
    
    public static CommandType getType(MimeMessage msg) throws IOException, MessagingException{
         if (msg.getContent() == null) {
            return null;
        }
        return msg.getContent().toString().contains("GRAD")?CommandType.GRAD:CommandType.TVRTKA;
    }
}/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.student3.web.kontrole;

import java.io.File;
import org.foi.nwtis.student3.messages.MessageController;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import javax.servlet.ServletContext;
import org.foi.nwtis.student3.konfiguracije.Konfiguracija;
import org.foi.nwtis.student3.konfiguracije.bp.BP_Konfiguracija;
import org.foi.nwtis.student3.models.Summary;
import org.foi.nwtis.student3.utils.ConfigUtils;
import org.foi.nwtis.student3.utils.Constants;
import org.foi.nwtis.student3.utils.Konekcija;
import org.foi.nwtis.student3.utils.KonekcijaUtils;

/**
 *
 * @author Sname1
 */
public class Worker implements Runnable{

    private BP_Konfiguracija configDB;
    private Konfiguracija config;
    private ServletContext context;
    private static Worker instance;
    private Konekcija connnection;
    private MessageController controller;
    
    public static Worker getInstance(ServletContext context) {
        if (instance == null) {
            instance = new Worker(context);
        }
        return instance;
    }
    
    private Worker(ServletContext context){
        this.context = context;
        this.config=ConfigUtils.loadConfiguration(context);
        this.connnection=new Konekcija(this.config);
        this.controller=new MessageController(this.config);
    }

    private Store napraviKonekciju(Session session, String store) throws MessagingException{
        Store sto = session.getStore(store);
        sto.connect(this.connnection.getServer(), this.connnection.getUser(), this.connnection.getPassword());
        return sto;
    }
    @Override
    public void run() {
        Summary summary =new Summary();
        Properties properties = System.getProperties();
        Session session = Session.getInstance(properties, null);
        Store store;
        try {
            store = napraviKonekciju(session, Constants.STORE);
            Folder formatedInbox=KonekcijaUtils.createFolder(store, this.config.dajPostavku(Constants.MAIL_INBOX_FORMATTED));
            Folder otherInbox=KonekcijaUtils.createFolder(store, this.config.dajPostavku(Constants.MAIL_INBOX_OTHER));
            File webPage = new File(this.config.dajPostavku(Constants.FOLDER_WEB));
            
            Folder inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_WRITE);
 
            this.controller.processMessages(inbox.getMessages(), formatedInbox, otherInbox,webPage,summary);
            inbox.close(true);
            store.close();
            summary.setDateEnd();
            
        } catch (MessagingException ex) {
            Logger.getLogger(Worker.class.getName()).log(Level.SEVERE, null, ex);
        }
           
    }
    
}/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.student3.web.slusaci;

import java.io.File;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import org.foi.nwtis.student3.utils.ConfigUtils;
import org.foi.nwtis.student3.utils.Constants;
import org.foi.nwtis.student3.web.kontrole.Worker;
import org.foi.nwtis.student3.konfiguracije.Konfiguracija;
import org.foi.nwtis.student3.konfiguracije.NemaKonfiguracije;
import org.foi.nwtis.student3.konfiguracije.bp.BP_Konfiguracija;

/**
 *
 * @author Sname1
 */
@WebListener
public class SlusacAplikacije implements ServletContextListener {

    private ScheduledExecutorService executor;
    private Worker processor;
    private Konfiguracija config;
    
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext context = sce.getServletContext();
        this.config=ConfigUtils.loadConfiguration(context);
        this.executor = Executors.newSingleThreadScheduledExecutor();
        int interval = Integer.valueOf(this.config.dajPostavku(Constants.MAIL_INTERVAL));
        if (this.processor == null) {
            this.processor=Worker.getInstance(context);
            this.executor.scheduleAtFixedRate(this.processor, 0, interval, TimeUnit.SECONDS);
        }
        /*String dir = context.getRealPath("/WEB-INF");
try {} catch (NemaKonfiguracije ex) {}
        context.setAttribute(Constants.DB_CONFIG, bp_konfig);
    */
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
     executor.shutdown();
}
}
public class Lokalizacija implements Serializable {
    @PostConstruct
    public void init() {
        vazeciJezik=new Locale("hr");
        odabraniJezik=Constants.LANG_CRO;
}
public Object odaberiJezik() {
        if (odabraniJezik != null && jezici.get(odabraniJezik) != null) {

return "";
        } 
    }
}
/**
 *
 * @author Sname1
 */
@Named(value = "pregledPreuzetihPodataka")
@RequestScoped
public class PregledPreuzetihPodataka {

    /**
     * Creates a new instance of PregledPreuzetihPodataka
     */
    public PregledPreuzetihPodataka() {
    }
    
}/*

*/
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletContext;
import org.foi.nwtis.student3.utils.ConfigUtils;
import org.foi.nwtis.student3.utils.Constants;

public class SlanjePoruke {

public SlanjePoruke() {
}

}
