package cucumberTests.steps;

import helperpackage.Car;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import pomPages.CarTaxHomePage;
import pomPages.FreeCheckPage;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
public class StepDefinitions {
        String drivePath = "./src/test/drivers/chromedriver.exe";
        String currentUrl;
        WebDriver driver;
        CarTaxHomePage homePage;
        FreeCheckPage freePage;

        @Given("Valid Registration value of {string}")
        public void checkReg(String reg){
            assertTrue(Car.isValid(reg));
        }

        @When("the website {string} is live")
        public void openSite(String url) throws IOException {
            System.setProperty("webdriver.chrome.driver", drivePath);
            driver = new ChromeDriver();
            driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
            driver.get("https://"+url+"/");
        }

        @And("user enters value of {string}")
        public void enterText(String reg) throws InterruptedException {
            homePage = new CarTaxHomePage(driver);
            Thread.sleep(500);
            homePage.editText(reg);
        }

        @And("user clicks on the free option")
        public void clickSubmit(){
            homePage.clickFreeCheck();
        }

        @Then("user should be taken to the {string} page")
        public void checkPage(String url){
            freePage = new FreeCheckPage(driver);
            String currentUrl = driver.getCurrentUrl();
            String regex = "^(https:\\/\\/"+url +"\\/)";
            String x  = "^(https:\\/\\/" + url +  "?\\S+)";
            if (!currentUrl.matches(regex)){
                throw new IllegalArgumentException("Bad link");
            };
        }

        @And("Number plate {string} Should appear")
        public void checkResults(String plate, String make){
            String plateReturned = freePage.getRegReturned();
            String makeReturned = freePage.getMakeReturned();
            if (plateReturned.equals(plateReturned) && makeReturned.equals(make)){
                //
            }else{
                throw new IllegalArgumentException("Wrongfully returned");
            }
        }






    }


