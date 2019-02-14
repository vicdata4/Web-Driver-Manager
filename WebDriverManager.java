/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tests;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.openqa.selenium.Platform;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.tests.Home;
import org.tests.About;
import org.tests.Media;
import org.tests.News;
import org.tests.Support;
import org.tests.Contact;

/**
 *
 * @author vicdata4
 */

public class WebDriverManager {
	
	public static String route = "";
	public static String enviroment = "";
	public static WebDriver driver;
	
	public static String localRoute = "http://localhost:4200/";
	public static String productionRoute = "https://example.com/";
	
    protected Properties ic = new Properties();

   protected WebDriverManager() {
        try (InputStream is = getClass().getResourceAsStream("/infocenter.properties")){
            ic.load(is);
        } catch (IOException ioe){}
    }
		
	public static void main(String[] args) throws IOException, InterruptedException {

		String browserName = checkBrowserName(args);
		String className = args[0].toString();
		browserName = getBrowserType(browserName);

		
		if(args.length == 2){
			route = localRoute;
		}else if(args.length == 3 && args[2].toLowerCase().equalsIgnoreCase("pro")){
			route = productionRoute;
			enviroment = "_PRODUCTION";
		}else{
			browserName = "";
		}
		
		if(!browserName.isEmpty() && args.length < 4){
			switch (className.toLowerCase()) {
				case "Home": runSingleTest(Home.class, browserName, className); break;
				case "About": runSingleTest(About.class, browserName, className); break;
				case "Media": runSingleTest(Media.class, browserName, className); break;
				case "News": runSingleTest(News.class, browserName, className); break;
				case "Support": runSingleTest(Support.class, browserName, className); break;
				case "Contact": runSingleTest(Contact.class, browserName, className); break;
				case "all": { runAllTests(browserName); break; }
				default: showNameAlert();
			}
		}else{
			showNameAlert();
		}
	}
	
	public static void runSingleTest(Class page, String browserName, String className) throws FileNotFoundException, IOException{
		PrintStream out = new PrintStream(new FileOutputStream(getFileReference(browserName, className.toUpperCase())));
		setBrowser(browserName);
		Result result = JUnitCore.runClasses(page);
		printResults(result, out, page.toString());
		
	}
	
	public static void runAllTests(String browserName) throws FileNotFoundException{
		PrintStream out = new PrintStream(new FileOutputStream(getFileReference(browserName, "ALL")));
		Class[] vb = getAllClasses();
		Result result;
		for(Class cl: vb){
			setBrowser(browserName);
			result = JUnitCore.runClasses(cl);
			printResults(result, out, cl.toString());
		}
	}
	
	public static String getFileReference(String browser, String className){
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmm_");
		Date date = new Date();
		String fileName = dateFormat.format(date) + browser + "_" + className;		

		
		String fileRef = System.getProperty("user.dir") +  "\\ConsoleOutputs\\" + fileName + enviroment + ".txt";
				
		return fileRef;
	}
	
	public static String checkBrowserName(String[] args){
		if(args.length > 1){
			return args[1].toString();
		}else{
			showArgsAlert();
			return "";
		}
	}
	
	public static String getBrowserType(String browserName){
		switch (browserName.toLowerCase()) {
			case "f": browserName = "Firefox"; break;
			case "c": browserName = "Chrome"; break;
			case "e": browserName = "Edge"; break;
            default: browserName = ""; break;
        }	
		return browserName;
	}
	
	public static Class[] getAllClasses(){
		Class[] vb = new Class[6];
		vb[0] = Home.class;
		vb[1] = About.class;
		vb[2] = Media.class;
		vb[3] = News.class;
		vb[4] = Support.class;
		vb[5] = Contact.class;
		return vb;
	}
	
	public static void setBrowser(String browsername){
		
		if(browsername.equalsIgnoreCase("Firefox")){
			System.setProperty("webdriver.gecko.driver", System.getProperty("user.dir") +  "\\Drivers\\geckodriver.exe");
			//hidden marionnete
			System.setProperty(FirefoxDriver.SystemProperty.DRIVER_USE_MARIONETTE,"true");
			System.setProperty(FirefoxDriver.SystemProperty.BROWSER_LOGFILE,"/dev/null");
			//hidden marionnete
			FirefoxProfile options = new FirefoxProfile();
			options.setPreference("browser.download.folderList", 2);
			options.setPreference("browser.download.dir", System.getProperty("user.dir") + "\\Downloads");
			options.setPreference("browser.download.manager.alertOnEXEOpen", false);
			options.setPreference("browser.helperApps.neverAsk.saveToDisk", "application/msword, application/csv, application/ris, text/csv, image/png, application/pdf, text/html, text/plain, application/zip, application/x-zip, application/x-zip-compressed, application/download, application/octet-stream");
			options.setPreference("browser.download.manager.showWhenStarting", false);
			options.setPreference("browser.download.manager.focusWhenStarting", false);  
			options.setPreference("browser.download.useDownloadDir", true);
			options.setPreference("browser.helperApps.alwaysAsk.force", false);
			options.setPreference("browser.download.manager.alertOnEXEOpen", false);
			options.setPreference("browser.download.manager.closeWhenDone", true);
			options.setPreference("browser.download.manager.showAlertOnComplete", false);
			options.setPreference("browser.download.manager.useWindow", false);
			options.setPreference("services.sync.prefs.sync.browser.download.manager.showWhenStarting", false);
			options.setPreference("security.insecure_field_warning.contextual.enabled", false);
			FirefoxOptions firefoxOptions = new FirefoxOptions();
			firefoxOptions.setProfile(options);
			driver = new FirefoxDriver(firefoxOptions);   
			String url = route;
			driver.manage().window().maximize();
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			driver.get(url);
		}
		
		if(browsername.equalsIgnoreCase("Chrome")){
			System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") +  "\\Drivers\\chromedriver.exe");
			String downloadFilepath = System.getProperty("user.dir") +  "\\Downloads";
			DesiredCapabilities caps = DesiredCapabilities.chrome();
			HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
			chromePrefs.put("profile.default_content_settings.popups", 0);
			chromePrefs.put("download.default_directory", downloadFilepath);
			ChromeOptions options = new ChromeOptions();
			options.setExperimentalOption("prefs", chromePrefs);
			options.addArguments("--test-type");
			options.addArguments("--disable-extensions");
			options.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
			driver = new ChromeDriver(options);   
			String url = route;       
			driver.manage().window().maximize();
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			driver.get(url);
		}
		
		if(browsername.equalsIgnoreCase("Edge")){
			System.setProperty("webdriver.edge.driver", System.getProperty("user.dir") +  "\\Drivers\\edgedriver.exe");
			EdgeOptions e = new EdgeOptions();
			e.setCapability("browser.download.folderList", 2);
			e.setCapability("download.default_directory", System.getProperty("user.dir") + "\\Downloads");
			driver = new EdgeDriver(e);   
			String url = route;    
			driver.manage().window().maximize();
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			driver.get(url);
		}	
	}

	public static void printResults(Result result, PrintStream out, String page){
		if(result.getFailures().size() == 0){
			out.println(separator);
			out.println(page.toUpperCase() + " // All tests passed successfuly.");
			out.println(separator);
		}else{
			out.println(separator);
			out.println(page.toUpperCase() + " | " + result.getFailureCount() + " tests failed.");
			out.println(separator);
		}
		
		
		out.print(" ");
		
		for (Failure failure : result.getFailures()) {
			System.out.println(separator);
			System.out.print(testHeader);
			System.out.println(failure.getTestHeader());
			System.out.println(testMessage);
			System.out.println(failure.getMessage());
			System.out.println(testException);
			System.out.println(failure.getException());
			System.out.println(separator);
			addFailedTestToTXT(failure, out);
		}
	}
		
	public static void addFailedTestToTXT(Failure failure, PrintStream out){
		out.println(separator);
		out.println("CLASS-NAME: " + failure.getTestHeader().matches("\\(([^\\)]+)\\)"));
		out.println(" ");
		out.println(testHeader + " " + failure.getTestHeader());
		out.println(" ");
		out.println(testMessage);
		out.println(failure.getMessage());
		out.println(" ");
		out.println(testException);
		out.println(failure.getException());
		out.println(" ");
		out.println(" ");
	}

	public static void showArgsAlert(){
		System.err.println("=========================");
		System.err.println("PLEASE INSERT BROWSERTYPE");
		System.err.println("=========================");
	}
	
	public static void showNameAlert(){
		System.err.println("=================================");
		System.err.println("PLEASE CHECK YOUR INPUT ARGUMENTS");
		System.err.println("=================================");
	}
	
	public static String separator = "=================================================================";
	public static String testHeader = "#TEST-HEADER: ";
	public static String testMessage = "#MESSAGE: ";
	public static String testException = "#INFO-EXCEPTION: ";
	
}


