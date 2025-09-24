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

import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class UiTest {
    private WebDriver driver;
    private String baseUrl;

    @BeforeEach
    public void setup() throws Exception {
        // Determine if running against remote Selenium (e.g., GitHub Actions container)
        boolean isRemote = Boolean.parseBoolean(System.getProperty("selenium.remote", "false"));
        String remoteUrl = System.getProperty("webdriver.remote.url", "http://localhost:4444/wd/hub");
        baseUrl = System.getProperty("app.baseUrl", "http://localhost:8080");

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new"); // Headless for CI
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");

        if (isRemote) {
            // Connect to remote Selenium container
            driver = new RemoteWebDriver(new URL(remoteUrl), options);
        } else {
            // Use local ChromeDriver
            WebDriverManager.chromedriver().setup();
            driver = new ChromeDriver(options);
        }
    }

    @Test
    public void testLogin() {
        driver.get(baseUrl + "/login");
        driver.findElement(By.name("username"))
                .sendKeys(System.getProperty("spring.security.user.name", "user"));
        driver.findElement(By.name("password"))
                .sendKeys(System.getProperty("spring.security.user.password", "user"));
        driver.findElement(By.tagName("button")).click();

        // Assert that login redirects to /tasks
        assertTrue(driver.getCurrentUrl().contains("/tasks"),
                "Login failed — current URL: " + driver.getCurrentUrl());
    }

    @Test
    public void testAddItem() {
        // Reuse login
        testLogin();

        // Add a task
        driver.findElement(By.name("description")).sendKeys("Selenium Task");
        driver.findElement(By.id("addTaskBtn")).click();

        // Verify task appears on the page
        assertTrue(driver.getPageSource().contains("Selenium Task"),
                "Task not found in page source!");
    }

    @AfterEach
    public void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
