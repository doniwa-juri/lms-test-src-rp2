package jp.co.sss.lms.ct.f06_login2;

import static jp.co.sss.lms.ct.util.WebDriverUtils.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.time.Duration;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * 結合テスト ログイン機能②
 * ケース15
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース15 受講生 初回ログイン 利用規約に不同意")
public class Case15 {

	/** 前処理 */
	@BeforeAll
	static void before() {
		createDriver();
	}

	/** 後処理 */
	@AfterAll
	static void after() {
		closeDriver();
	}

	@Test
	@Order(1)
	@DisplayName("テスト01 トップページURLでアクセス")
	void test01() throws Exception {
		final WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));
		webDriver.get("http://localhost:8080/lms");

		wait.until(ExpectedConditions.titleContains("ログイン | LMS"));
		final String title = webDriver.getTitle();
		assertEquals("ログイン | LMS", title);

		File file = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(file, new File("evidence\\case15\\test01.png"));
	}

	@Test
	@Order(2)
	@DisplayName("テスト02 DBに初期登録された未ログインの受講生ユーザーでログイン")
	void test02() throws Exception {
		final WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));
		final WebElement loginId = webDriver.findElement(By.name("loginId"));
		final WebElement password = webDriver.findElement(By.name("password"));
		final WebElement loginButton = webDriver.findElement(By.cssSelector(".btn.btn-primary"));

		loginId.clear();
		loginId.sendKeys("StudentAA05");

		password.clear();
		password.sendKeys("StudentAA05");

		loginButton.click();

		wait.until(ExpectedConditions.titleContains("セキュリティ規約 | LMS"));
		final String title = webDriver.getTitle();
		assertEquals("セキュリティ規約 | LMS", title);

		File file = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(file, new File("evidence\\case15\\test02.png"));
	}

	@Test
	@Order(3)
	@DisplayName("テスト03 「同意します」チェックボックスにチェックをせず「次へ」ボタンを押下")
	void test03() throws Exception {
		final WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));
		final WebElement next = webDriver.findElement(By.xpath("//*[@id=\"main\"]/div[2]/form/fieldset/div[2]/button"));
		((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoView({block: 'center'});",
				next);
		next.click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("error")));

		final WebElement error = webDriver.findElement(By.className("error"));
		assertTrue(error.getText().contains("セキュリティ規約への同意は必須です。"));

		File file = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(file, new File("evidence\\case15\\test03.png"));
	}

}
