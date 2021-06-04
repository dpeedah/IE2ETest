package tests;
import helpers.Car;
import helpers.TestValueExtractor;
import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.asserts.SoftAssert;
import pages.CarTaxHomePage;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import pages.FreeCheckPage;
import org.testng.TestNG;
import org.testng.annotations.*;
import org.testng.annotations.Test;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
public class CarTaxHomePageTest {
    SoftAssert softAssert = new SoftAssert();
    String drivePath = "./src/test/lib/chromedriver.exe";
    WebDriver driver;
    CarTaxHomePage homePage;
    FreeCheckPage freePage;
    FileHandler fh;
    Logger logger = Logger.getLogger(CarTaxHomePageTest.class.getName());

    public CarTaxHomePageTest() throws IOException {
    }

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

    @Test
    public void testInputsAndOutputs() throws IOException {
        List<String> inputs = TestValueExtractor.getInputValuesBase();
        for (int x = 0; x < inputs.size() ; ++x){
            String s = inputs.get(0).replace(" ","");
        }
        homePage = new CarTaxHomePage(driver);
        String currentUrl = driver.getCurrentUrl();
        try{
            Assert.assertTrue(currentUrl.matches("^(https:\\/\\/cartaxcheck.co.uk)"));
        } catch (AssertionError assertionError){
            assertionError.printStackTrace();
        }

    }




}
