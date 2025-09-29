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
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * 結合テスト 勤怠管理機能
 * ケース12
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース12 受講生 勤怠直接編集 入力チェック")
public class Case12 {

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
		FileUtils.copyFile(file, new File("evidence\\case12\\test01.png"));
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
		FileUtils.copyFile(file, new File("evidence\\case12\\test02.png"));
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
		FileUtils.copyFile(file, new File("evidence\\case12\\test03.png"));
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
		FileUtils.copyFile(file, new File("evidence\\case12\\test04.png"));
	}

	@Test
	@Order(5)
	@DisplayName("テスト05 不適切な内容で修正してエラー表示：出退勤の（時）と（分）のいずれかが空白")
	void test05() throws Exception {
		final WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));
		final Select hour = new Select(webDriver.findElement(By.id("startHour0")));
		final Select min = new Select(webDriver.findElement(By.id("startMinute0")));

		hour.selectByIndex(0);
		min.selectByIndex(1);

		final WebElement update = webDriver.findElement(By.cssSelector("input[value='更新']"));
		((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoView({block: 'center'});",
				update);
		update.click();

		Alert alert = wait.until(ExpectedConditions.alertIsPresent());
		alert.accept();

		final WebElement newHour = webDriver.findElement(By.id("startHour0"));
		final String errorMsg = webDriver.findElement(By.cssSelector(".help-inline.error")).getText();
		((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoView({block: 'center'});",
				newHour);
		assertEquals("form-control errorInput", newHour.getAttribute("class"));
		assertTrue(errorMsg.contains("出勤時間が正しく入力されていません。"));

		File file = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(file, new File("evidence\\case12\\test05.png"));

	}

	@Test
	@Order(6)
	@DisplayName("テスト06 不適切な内容で修正してエラー表示：出勤が空白で退勤に入力あり")
	void test06() throws Exception {
		final WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));
		final Select startHour = new Select(webDriver.findElement(By.id("startHour0")));
		final Select startMin = new Select(webDriver.findElement(By.id("startMinute0")));
		final Select endHour = new Select(webDriver.findElement(By.id("endHour0")));
		final Select endMin = new Select(webDriver.findElement(By.id("endMinute0")));

		startHour.selectByIndex(0);
		startMin.selectByIndex(0);
		endHour.selectByIndex(20);
		endMin.selectByIndex(1);

		final WebElement update = webDriver.findElement(By.cssSelector("input[value='更新']"));
		((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoView({block: 'center'});",
				update);
		update.click();

		Alert alert = wait.until(ExpectedConditions.alertIsPresent());
		alert.accept();

		final WebElement newStartHour = webDriver.findElement(By.id("startHour0"));
		final WebElement newStartMin = webDriver.findElement(By.id("startMinute0"));
		final String errorMsg = webDriver.findElement(By.cssSelector(".help-inline.error")).getText();

		((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoView({block: 'center'});",
				newStartHour);

		assertEquals("form-control errorInput", newStartHour.getAttribute("class"));
		assertEquals("form-control errorInput", newStartMin.getAttribute("class"));
		assertTrue(errorMsg.contains("出勤情報がないため退勤情報を入力出来ません。"));

		File file = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(file, new File("evidence\\case12\\test06.png"));
	}

	@Test
	@Order(7)
	@DisplayName("テスト07 不適切な内容で修正してエラー表示：出勤が退勤よりも遅い時間")
	void test07() throws Exception {
		final WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));
		final Select startHour = new Select(webDriver.findElement(By.id("startHour0")));
		final Select startMin = new Select(webDriver.findElement(By.id("startMinute0")));
		final Select endHour = new Select(webDriver.findElement(By.id("endHour0")));
		final Select endMin = new Select(webDriver.findElement(By.id("endMinute0")));

		startHour.selectByIndex(10);
		startMin.selectByIndex(1);
		endHour.selectByIndex(5);
		endMin.selectByIndex(1);

		final WebElement update = webDriver.findElement(By.cssSelector("input[value='更新']"));
		((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoView({block: 'center'});",
				update);
		update.click();

		Alert alert = wait.until(ExpectedConditions.alertIsPresent());
		alert.accept();

		final WebElement newEndHour = webDriver.findElement(By.id("endHour0"));
		final WebElement newEndMin = webDriver.findElement(By.id("endMinute0"));
		final String errorMsg = webDriver.findElement(By.cssSelector(".help-inline.error")).getText();

		((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoView({block: 'center'});",
				newEndHour);

		assertEquals("form-control errorInput", newEndHour.getAttribute("class"));
		assertEquals("form-control errorInput", newEndMin.getAttribute("class"));
		assertTrue(errorMsg.contains("退勤時刻[0]は出勤時刻[0]より後でなければいけません。"));

		File file = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(file, new File("evidence\\case12\\test07.png"));
	}

	@Test
	@Order(8)
	@DisplayName("テスト08 不適切な内容で修正してエラー表示：出退勤時間を超える中抜け時間")
	void test08() throws Exception {
		final WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));
		final Select startHour = new Select(webDriver.findElement(By.id("startHour0")));
		final Select startMin = new Select(webDriver.findElement(By.id("startMinute0")));
		final Select endHour = new Select(webDriver.findElement(By.id("endHour0")));
		final Select endMin = new Select(webDriver.findElement(By.id("endMinute0")));
		final Select blankTime = new Select(webDriver.findElement(By.name("attendanceList[0].blankTime")));

		startHour.selectByIndex(12);
		startMin.selectByIndex(1);
		endHour.selectByIndex(13);
		endMin.selectByIndex(1);
		blankTime.selectByIndex(8);

		final WebElement update = webDriver.findElement(By.cssSelector("input[value='更新']"));
		((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoView({block: 'center'});",
				update);
		update.click();

		Alert alert = wait.until(ExpectedConditions.alertIsPresent());
		alert.accept();

		final WebElement newBlankTime = webDriver.findElement(By.name("attendanceList[0].blankTime"));
		final String errorMsg = webDriver.findElement(By.cssSelector(".help-inline.error")).getText();

		((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoView({block: 'center'});",
				newBlankTime);

		assertEquals("form-control errorInput", newBlankTime.getAttribute("class"));
		assertTrue(errorMsg.contains("中抜け時間が勤務時間を超えています。"));

		File file = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(file, new File("evidence\\case12\\test08.png"));
	}

	@Test
	@Order(9)
	@DisplayName("テスト09 不適切な内容で修正してエラー表示：備考が100文字超")
	void test09() throws Exception {
		final WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));
		final Select startHour = new Select(webDriver.findElement(By.id("startHour0")));
		final Select startMin = new Select(webDriver.findElement(By.id("startMinute0")));
		final Select endHour = new Select(webDriver.findElement(By.id("endHour0")));
		final Select endMin = new Select(webDriver.findElement(By.id("endMinute0")));
		final Select blankTime = new Select(webDriver.findElement(By.name("attendanceList[0].blankTime")));
		final WebElement note = webDriver.findElement(By.name("attendanceList[0].note"));

		startHour.selectByIndex(11);
		startMin.selectByIndex(1);
		endHour.selectByIndex(20);
		endMin.selectByIndex(1);
		blankTime.selectByIndex(0);

		StringBuilder st = new StringBuilder();
		for (int i = 0; i < 110; i++) {
			st.append("a");
		}
		note.sendKeys(st);

		final WebElement update = webDriver.findElement(By.cssSelector("input[value='更新']"));
		((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoView({block: 'center'});",
				update);
		update.click();

		Alert alert = wait.until(ExpectedConditions.alertIsPresent());
		alert.accept();

		final WebElement newNote = webDriver.findElement(By.name("attendanceList[0].note"));
		final String errorMsg = webDriver.findElement(By.cssSelector(".help-inline.error")).getText();

		((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoView({block: 'center'});",
				newNote);

		assertEquals("form-control errorInput", newNote.getAttribute("class"));
		assertTrue(errorMsg.contains("備考の長さが最大値(100)を超えています。"));

		File file = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(file, new File("evidence\\case12\\test09.png"));
	}

}
