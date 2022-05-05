package com.example.jobagapi.funcional;// Generated by Selenium IDE
import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;

public class CreateJobTest {
  private WebDriver driver;
  private WebDriverWait wait;

  @Before
  public void setUp() {
    System.setProperty("webdriver.chrome.driver", "src/test/java/com/example/jobagapi/funcional/drivers/chromedriver-v101.exe");
    driver = new ChromeDriver();
    wait = new WebDriverWait(driver, Duration.ofSeconds(5).getSeconds());
  }
  @After
  public void tearDown() {
    driver.quit();
  }
  @Test
  public void createJob() {
    driver.get("https://cute-lolly-d05dcb.netlify.app/");
    driver.findElement(By.id("mat-input-0")).click();
    driver.findElement(By.id("mat-input-0")).sendKeys("test@test.com");
    driver.findElement(By.id("mat-input-1")).sendKeys("test");
    driver.findElement(By.cssSelector(".btn-ingresar > .mat-button-wrapper")).click();
    wait.until(d -> d.findElement(By.cssSelector(".mat-toolbar > button:nth-child(1)")));
    driver.findElement(By.cssSelector(".mat-toolbar > button:nth-child(1)")).click();
    wait.until(d -> d.findElement(By.cssSelector(".mat-list-item:nth-child(5) > .mat-list-item-content")));
    driver.findElement(By.cssSelector(".mat-list-item:nth-child(5) > .mat-list-item-content")).click();
    driver.findElement(By.id("mat-input-2")).click();
    driver.findElement(By.id("mat-input-2")).sendKeys("test");
    driver.findElement(By.id("mat-input-3")).sendKeys("test");
    driver.findElement(By.id("mat-input-4")).sendKeys("test");
    driver.findElement(By.id("mat-input-5")).click();
    driver.findElement(By.id("mat-input-5")).sendKeys("01-01-2000");
    driver.findElement(By.id("mat-input-6")).click();
    driver.findElement(By.id("mat-input-6")).sendKeys("01-01-2002");
    driver.findElement(By.id("mat-input-7")).sendKeys("1000");
    driver.findElement(By.id("mat-input-8")).sendKeys("test");
    driver.findElement(By.cssSelector(".mat-button")).click();
    driver.findElement(By.cssSelector(".mat-dialog-content")).click();
    wait.until(d -> d.findElement(By.cssSelector(".mat-dialog-content")));
    String confirmationText = driver.findElement(By.cssSelector(".mat-dialog-content")).getText();
    assertThat(confirmationText).isEqualTo("Anuncio creado satisfactoriamente");
  }
}
