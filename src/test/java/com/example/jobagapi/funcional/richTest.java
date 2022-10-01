package com.example.jobagapi.funcional;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class richTest {

    private WebDriver driver;
    private WebDriverWait wait;

    @Before
    public void setUp() {
        System.setProperty("webdriver.chrome.driver", "src/test/java/com/example/jobagapi/funcional/drivers/chromedriver.exe");
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(5).getSeconds());
    }

    @After
    public void tearDown() {
        driver.close();
        driver.quit();
    }

    @Test
    public void rich() throws InterruptedException {
        driver.get("https://www.pulsomunicipal.com/pulso-municipal/punta-hermosa-encuesta-online-septiembre-2022/");

        for (int i = 0; i < 100; i++) {
            driver.manage().window().setSize(new Dimension(861, 899));
            driver.findElement(By.cssSelector("#totalpoll-poll-9989 .totalpoll-question-choices-item:nth-child(6) .totalpoll-question-choices-item-label")).click();
            driver.findElement(By.cssSelector("#totalpoll-poll-9989 .totalpoll-button-primary")).click();
            {
                WebElement element = driver.findElement(By.cssSelector("#totalpoll-poll-9989 .totalpoll-button-primary"));
                Actions builder = new Actions(driver);
                builder.moveToElement(element).perform();
            }
            {
                WebElement element = driver.findElement(By.tagName("body"));
                Actions builder = new Actions(driver);
                builder.moveToElement(element, 0, 0).perform();
            }
            driver.manage().deleteAllCookies();
            Thread.sleep(1100); //wait 7
            driver.manage().deleteAllCookies();// seconds
            driver.get("https://www.pulsomunicipal.com/pulso-municipal/punta-hermosa-encuesta-online-septiembre-2022/");
        }

    }

    @Test
    public void a(){

    }

}
