package tests;
import helperpackage.Car;
import helperpackage.TestValueExtractor;
//
// import jdk.jfr.internal.LogLevel;
import org.junit.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.junit.runners.Parameterized;
import org.openqa.selenium.WebDriver;
import pomPages.CarTaxHomePage;
import org.openqa.selenium.chrome.ChromeDriver;
import pomPages.FreeCheckPage;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

@FixMethodOrder(MethodSorters.DEFAULT)
public class CarTaxPageTest {
    String drivePath = "./src/test/drivers/chromedriver.exe";
    String currentUrl;
    WebDriver driver;
    CarTaxHomePage homePage;
    FreeCheckPage freePage;
    FileHandler fh;
    Logger logger = Logger.getLogger(CarTaxPageTest.class.getName());
    List<String> input_values = TestValueExtractor.getInputValuesBase();
    String inputsCsv = String.join(",", input_values);
    List<Car> output_values = TestValueExtractor.getOutputValues();
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
        if ( (realCar.getReg().equals(expected.getReg()))
            && (realCar.getColor().equals(expected.getColor()))
        && (realCar.getMake().equals(expected.getMake()))
        && (realCar.getModel().equals(expected.getModel()))
        && (realCar.getYearMake().equals(expected.getYearMake())) ){
            logInfo("MATCHING OUTPUT - EXPECTED : " + expected.toString() + "\n REAL OUTPUT : " + realCar.toString());
        }else{
            logWarning("FAILURE - Expected : " + expected.toString() +  "\n" +  "Returned : " + realCar.toString());
        }
    }

    @BeforeClass
    public void setLogger() throws IOException {
        String date = java.time.LocalDate.now().toString();
        String name = "log " + date + ".txt";
        fh = new FileHandler("./src/test/logs/" + name);
        logger.addHandler(fh);
    }

    //set the environment before each test
    @BeforeEach
    public void setEnv() throws IOException {
        System.setProperty("webdriver.chrome.driver", drivePath);
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        driver.get("https://cartaxcheck.co.uk/");
    }

    @AfterEach
    public void closeEnd() throws IOException{
        driver.close();
    }

    @AfterClass
    public void closeEnv(){
        for(Handler h:logger.getHandlers())
        {
            h.close();   //must call h.close or a .LCK file will remain.
        }
        driver.close();
    }


    /*@Test
    public void a_testPageUp() throws InterruptedException {
        homePage = new CarTaxHomePage(driver);
        Thread.sleep(500);
        String currentUrl = driver.getCurrentUrl();
        Assert.assertTrue(currentUrl.matches("^(https:\\/\\/cartaxcheck.co.uk\\/)"));
        Thread.sleep(500);
        driver.close();
    }*/

    @ParameterizedTest
    @MethodSource("getInputs")
    public void testInputOutput(String register) throws InterruptedException, IOException {
        String regNoSpace = register.replace(" ","");
        String capitalisedReg = regNoSpace.toUpperCase(Locale.ROOT);
        homePage = new CarTaxHomePage(driver);
        Thread.sleep(1000);
        homePage.editText(capitalisedReg);
        Thread.sleep(1000);
        homePage.clickFreeCheck();
        Thread.sleep(1000);
        freePage = new FreeCheckPage(driver);

        // badReq() returns true if the "Vehicle not found" error appears on the site This causes the test to fail.
        if (freePage.badReq()){
            String error = "Bad request given, register of : " + capitalisedReg;
            logWarning(error);
            reset();
            Assert.assertTrue(!currentUrl.matches("^(https:\\/\\/cartaxcheck.co.uk\\/)"));
        }
        currentUrl = driver.getCurrentUrl();
        Assert.assertTrue(currentUrl.matches("^(https:\\/\\/cartaxcheck.co.uk\\/)\\S+"));
        Thread.sleep(1000);
        String reg = freePage.getRegReturned();
        String make = freePage.getMakeReturned();
        String model = freePage.getModelReturned();
        String color = freePage.getColourReturned();
        int year = freePage.getYearReturned();
        Car realCar = new Car(reg,make,model,color,year);
        Car outputExpected = getOutputCar(capitalisedReg);
        compareOutputCars(realCar,outputExpected);
        Assert.assertTrue(outputExpected.getReg().equals(reg));
        Assert.assertTrue(outputExpected.getMake().equals(make));
        Assert.assertTrue(outputExpected.getModel().equals(model));
        Assert.assertTrue(outputExpected.getColor().equals(color));
        Assert.assertTrue(outputExpected.getYearMake().equals(year));
        driver.navigate().to("https://cartaxcheck.co.uk/");
        reset();
        currentUrl = driver.getCurrentUrl();
        Assert.assertTrue(currentUrl.matches("^(https:\\/\\/cartaxcheck.co.uk\\/)"));
    }


    private static Stream<String> getInputs() throws IOException {
        List<String> input_values = TestValueExtractor.getInputValuesBase();
        return input_values.stream();
    }

    //tests all inputs with outputs
/*    @Test
    public void testInputsAndOutputs() throws IOException, InterruptedException {
        List<String> input_values = TestValueExtractor.getInputValuesBase();
        List<Car> output_values = TestValueExtractor.getOutputValues();
        for (int x = 0; x < input_values.size() ; ++x){
            String regNoSpace = input_values.get(x).replace(" ","");
            String capitalisedReg = regNoSpace.toUpperCase(Locale.ROOT);
            homePage = new CarTaxHomePage(driver);
            Thread.sleep(1000);
            homePage.editText(capitalisedReg);
            Thread.sleep(1000);
            homePage.clickFreeCheck();
            Thread.sleep(1000);
            freePage = new FreeCheckPage(driver);

            // badReq() returns true if the "Vehicle not found" error appears on the site This causes the test to fail.
            if (freePage.badReq()){
                String error = "Bad request given, register of : " + capitalisedReg;
                logWarning(error);
                reset();
                softAssert.assertTrue(!currentUrl.matches("^(https:\\/\\/cartaxcheck.co.uk\\/)"));
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
    }*/




}
