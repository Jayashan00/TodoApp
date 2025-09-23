package com.example.todoapp;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import io.github.bonigarcia.wdm.WebDriverManager;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.net.URL;

public class UiTest {
    private WebDriver driver;
    private static final String REMOTE_URL = System.getProperty("webdriver.remote.url", "http://localhost:4444/wd/hub");
    private static final boolean IS_REMOTE = Boolean.parseBoolean(System.getProperty("selenium.remote", "false"));

    @BeforeEach
    public void setup() throws Exception {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");

        if (IS_REMOTE) {
            driver = new RemoteWebDriver(new URL(REMOTE_URL), options);
        } else {
            WebDriverManager.chromedriver().setup();
            driver = new ChromeDriver(options);
        }
    }

    @Test
    public void testLogin() {
        driver.get("http://localhost:8080/login");
        driver.findElement(By.name("username")).sendKeys(System.getProperty("spring.security.user.name", "user"));
        driver.findElement(By.name("password")).sendKeys(System.getProperty("spring.security.user.password", "user"));
        driver.findElement(By.tagName("button")).submit();
        assertTrue(driver.getCurrentUrl().contains("/tasks"));
    }

    @Test
    public void testAddItem() {
        testLogin();
        driver.findElement(By.name("description")).sendKeys("Selenium Task");
        driver.findElement(By.id("addTaskBtn")).submit();
        assertTrue(driver.getPageSource().contains("Selenium Task"));
    }

    @AfterEach
    public void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }
}