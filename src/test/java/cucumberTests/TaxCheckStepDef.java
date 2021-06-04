package cucumberTests;
import helperpackage.Car;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.InvalidArgumentException;
import org.openqa.selenium.WebDriver;
import pomPages.CarTaxHomePage;
import pomPages.FreeCheckPage;
import org.openqa.selenium.WebDriver;
import org.testng.asserts.SoftAssert;
import pomPages.CarTaxHomePage;
import org.openqa.selenium.chrome.ChromeDriver;
import pomPages.FreeCheckPage;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class TaxCheckStepDef {
    String drivePath = "./src/test/drivers/chromedriver.exe";
    String currentUrl;
    WebDriver driver;
    CarTaxHomePage homePage;
    FreeCheckPage freePage;

    @When("the website {string} is live")
    public void openSite(String url) throws IOException {
        System.setProperty("webdriver.chrome.driver", drivePath);
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        driver.get("https://"+url+"/");
    }

    @When("user enters value of {string}")
    public void enterText(String reg) throws InterruptedException {
        homePage = new CarTaxHomePage(driver);
        Thread.sleep(500);
        homePage.editText(reg);
    }

    @When("user clicks on the free option")
    public void clickSubmit(){
        homePage.clickFreeCheck();
    }

    @Then("user should be taken to the {string} page")

    @Given("Valid Registration value of {string}")
    public void checkReg(String reg){
        if (!Car.isValid(reg)){
            throw new InvalidArgumentException("Invalid reg");
        };
    }





}
