package jp.co.sss.lms.ct.f04_attendance;

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
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * 結合テスト 勤怠管理機能
 * ケース11
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース11 受講生 勤怠直接編集 正常系")
public class Case11 {

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
		FileUtils.copyFile(file, new File("evidence\\case11\\test01.png"));
	}

	@Test
	@Order(2)
	@DisplayName("テスト02 初回ログイン済みの受講生ユーザーでログイン")
	void test02() throws Exception {
		final WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));
		final WebElement loginId = webDriver.findElement(By.name("loginId"));
		final WebElement password = webDriver.findElement(By.name("password"));
		final WebElement loginButton = webDriver.findElement(By.cssSelector(".btn.btn-primary"));

		loginId.clear();
		loginId.sendKeys("StudentAA02");

		password.clear();
		password.sendKeys("StudentAA02");

		loginButton.click();

		wait.until(ExpectedConditions.titleContains("コース詳細 | LMS"));
		final String title = webDriver.getTitle();
		assertEquals("コース詳細 | LMS", title);

		File file = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(file, new File("evidence\\case11\\test02.png"));
	}

	@Test
	@Order(3)
	@DisplayName("テスト03 上部メニューの「勤怠」リンクから勤怠管理画面に遷移")
	void test03() throws Exception {
		final WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));
		final WebElement attendance = webDriver.findElement(By.xpath("//*[@id=\"nav-content\"]/ul[1]/li[3]/a"));
		attendance.click();

		wait.until(ExpectedConditions.titleContains("勤怠情報変更｜LMS"));
		final String title = webDriver.getTitle();
		assertEquals("勤怠情報変更｜LMS", title);

		File file = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(file, new File("evidence\\case11\\test03.png"));
	}

	@Test
	@Order(4)
	@DisplayName("テスト04 「勤怠情報を直接編集する」リンクから勤怠情報直接変更画面に遷移")
	void test04() throws Exception {
		final WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));
		final WebElement edit = webDriver.findElement(By.linkText("勤怠情報を直接編集する"));
		edit.click();

		wait.until(ExpectedConditions.urlContains("update"));
		assertEquals(webDriver.getCurrentUrl(), "http://localhost:8080/lms/attendance/update");

		File file = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(file, new File("evidence\\case11\\test04.png"));
	}

	@Test
	@Order(5)
	@DisplayName("テスト05 すべての研修日程の勤怠情報を正しく更新し勤怠管理画面に遷移")
	void test05() throws Exception {
		final WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));
		final WebElement update = webDriver.findElement(By.cssSelector("input[value='更新']"));
		((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoView({block: 'center'});",
				update);
		update.click();

		Alert alert = wait.until(ExpectedConditions.alertIsPresent());
		alert.accept();

		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"main\"]/div[1]")));
		final String updateComp = webDriver.findElement(By.xpath("//*[@id=\"main\"]/div[1]")).getText();
		assertTrue(updateComp.contains("勤怠情報の登録が完了しました。"));

		File file = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(file, new File("evidence\\case11\\test05.png"));
	}

}
