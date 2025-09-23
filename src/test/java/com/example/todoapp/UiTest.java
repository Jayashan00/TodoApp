package com.example.todoapp;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class UiTest {
    private WebDriver driver;

    @BeforeEach
    public void setup() {
        // ✅ Correct: point to the .exe, not just the folder
        System.setProperty("webdriver.chrome.driver", "C:\\chromedriver-win64\\chromedriver.exe");

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless"); // Run without opening browser window
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");

        driver = new ChromeDriver(options);
    }


    @Test public void testLogin() {
        driver.get("http://localhost:8080/login");
        driver.findElement(By.name("username")).sendKeys("user");
        driver.findElement(By.name("password")).sendKeys("user");
        driver.findElement(By.tagName("button")).submit();
        assertTrue(driver.getCurrentUrl().contains("/tasks"));
    }

    @Test public void testAddItem() {
        // Login first
        testLogin();
        driver.findElement(By.name("description")).sendKeys("Selenium Task");
        driver.findElement(By.id("addTaskBtn")).submit();
        assertTrue(driver.getPageSource().contains("Selenium Task"));
    }

    @AfterEach public void teardown() { driver.quit(); }
}