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
        String remoteUrl = System.getProperty("webdriver.remote.url", "http://localhost:4444/wd/hub");
        boolean isRemote = Boolean.parseBoolean(System.getProperty("selenium.remote", "false"));
        baseUrl = System.getProperty("app.baseUrl", "http://localhost:8080");

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");

        if (isRemote) {
            driver = new RemoteWebDriver(new URL(remoteUrl), options);
        } else {
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
        assertTrue(driver.getCurrentUrl().contains("/tasks"),
                "Login failed — current URL: " + driver.getCurrentUrl());
    }

    @Test
    public void testAddItem() {
        testLogin();
        driver.findElement(By.name("description")).sendKeys("Selenium Task");
        driver.findElement(By.id("addTaskBtn")).click();
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
