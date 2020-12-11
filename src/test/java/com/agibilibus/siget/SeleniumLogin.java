package com.agibilibus.siget;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;


public class SeleniumLogin {

 private WebDriver driver;

 @Before
 public void setUp() {

  System.setProperty("webdriver.chrome.driver", "./src/test/resources/ChromeDriver/chromedriver.exe");
  driver = new ChromeDriver();
  driver.manage().window().maximize();
  driver.get("https://sigetagibilibus.herokuapp.com/Login.html");

 }

 @Test
 public void testLoginPage() {

  WebElement nombreUsuario = driver.findElement(By.id("username"));
  WebElement contraseña = driver.findElement(By.id("pass"));
  WebElement btnLogin = driver.findElement(By.id("login"));

  nombreUsuario.clear();
  nombreUsuario.sendKeys("Pilar");

  contraseña.clear();
  contraseña.sendKeys("Hola1234");

  btnLogin.click();

  String actualUrl = "https://sigetagibilibus.herokuapp.com/Login.html";
  String expectedUrl = driver.getCurrentUrl();

  assertEquals(actualUrl, expectedUrl);

 }

 @After
 public void tearDown() {
  driver.quit();
 }
}
