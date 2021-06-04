package tests;
import helpers.Car;
import helpers.TestValueExtractor;
import jdk.jfr.internal.LogLevel;
import org.openqa.selenium.WebDriver;
import org.testng.asserts.SoftAssert;
import pages.CarTaxHomePage;
import org.openqa.selenium.chrome.ChromeDriver;
import pages.FreeCheckPage;
import org.testng.annotations.*;
import org.testng.annotations.Test;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
public class CarTaxPageTest {
    SoftAssert softAssert = new SoftAssert();
    String drivePath = "./src/test/lib/chromedriver.exe";
    String currentUrl;
    WebDriver driver;
    CarTaxHomePage homePage;
    FreeCheckPage freePage;
    FileHandler fh;
    Logger logger = Logger.getLogger(CarTaxPageTest.class.getName());

    public CarTaxPageTest() throws IOException {
    }

    //for info
    public void logInfo (String str){
        logger.log(Level.INFO,str);
    }

    //for info
    public void logFine (String str){
        logger.log(Level.FINE,str);
    }

    //for tests that fail
    public void logWarning (String str){
        logger.log(Level.WARNING,str);
    }


    public void reset(){
        driver.navigate().to("https://cartaxcheck.co.uk/");
    }
    public Car getOutputCar(String reg) throws IOException {
        List<Car> outputs = TestValueExtractor.getOutputValues();
        Car returnCar = new Car();
        for (int y = 0; y < outputs.size() ; ++y){
            Car car = outputs.get(y);
            if (car.getReg().toUpperCase(Locale.ROOT).equals(reg) ){
                returnCar = car;
            }
        }
        return returnCar;
    }

    public void compareOutputCars(Car realCar, Car expected){
        if ( realCar.equals(expected)){
            logFine("Car of reg " + realCar.getReg() + " Returned expected values when searched on cartaxcheck");
        }else{
            if (realCar.getColor() != expected.getColor()){
                logWarning("Expeced : " + expected.toString() +  "\n" +  "Returned : " + realCar.toString());
            }
        }
    }


    //set the environment before each test
    @BeforeClass
    public void setEnv() throws IOException {
        fh = new FileHandler("./src/test/logs/log.txt");
        System.setProperty("webdriver.chrome.driver", drivePath);
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        driver.get("https://cartaxcheck.co.uk/");
        logger.addHandler(fh);
    }

    @AfterClass
    public void closeEnv(){
        for(Handler h:logger.getHandlers())
        {
            h.close();   //must call h.close or a .LCK file will remain.
        }
        driver.close();
    }


    @Test
    public void testPageUp(){
        homePage = new CarTaxHomePage(driver);
        String currentUrl = driver.getCurrentUrl();
        softAssert.assertTrue(currentUrl.matches("^(https:\\/\\/cartaxcheck.co.uk\\/)"));
        softAssert.assertAll();
    }

    //tests all inputs with outputs
    @Test
    public void testInputsAndOutputs() throws IOException, InterruptedException {
        List<String> inputs = TestValueExtractor.getInputValuesBase();
        List<Car> outputs = TestValueExtractor.getOutputValues();
        for (int x = 0; x < inputs.size() ; ++x){
            String regNoSpace = inputs.get(x).replace(" ","");
            String capitalisedReg = regNoSpace.toUpperCase(Locale.ROOT);
            homePage = new CarTaxHomePage(driver);
            Thread.sleep(1000);
            homePage.editText(capitalisedReg);
            Thread.sleep(1000);
            homePage.clickFreeCheck();
            Thread.sleep(1000);
            freePage = new FreeCheckPage(driver);

            if (freePage.badReq()){
                String error = "Bad request given, register of : " + capitalisedReg;
                logInfo(error);
                reset();
                softAssert.assertTrue(currentUrl.matches("^(https:\\/\\/cartaxcheck.co.uk\\/)"));
                continue;
            }

            currentUrl = driver.getCurrentUrl();
            softAssert.assertTrue(currentUrl.matches("^(https:\\/\\/cartaxcheck.co.uk\\/)\\S+"));
            Thread.sleep(1000);
            String reg = freePage.getRegReturned();
            String make = freePage.getMakeReturned();
            String model = freePage.getModelReturned();
            String color = freePage.getColourReturned();
            int year = freePage.getYearReturned();
            Car realCar = new Car(reg,make,model,color,year);
            Car outputExpected = getOutputCar(capitalisedReg);
            compareOutputCars(realCar,outputExpected);
            softAssert.assertTrue(outputExpected.getReg().equals(reg));
            softAssert.assertTrue(outputExpected.getMake().equals(make));
            softAssert.assertTrue(outputExpected.getModel().equals(model));
            softAssert.assertTrue(outputExpected.getColor().equals(color));
            softAssert.assertTrue(outputExpected.getYearMake().equals(year));
            driver.navigate().to("https://cartaxcheck.co.uk/");
            reset();
            currentUrl = driver.getCurrentUrl();
            softAssert.assertTrue(currentUrl.matches("^(https:\\/\\/cartaxcheck.co.uk\\/)"));
        }
        softAssert.assertAll();
    }




}
