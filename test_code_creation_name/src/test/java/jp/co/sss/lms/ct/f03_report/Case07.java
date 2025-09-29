package jp.co.sss.lms.ct.f03_report;

import static jp.co.sss.lms.ct.util.WebDriverUtils.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.time.Duration;
import java.util.List;

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
 * 結合テスト レポート機能
 * ケース07
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース07 受講生 レポート新規登録(日報) 正常系")
public class Case07 {

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
		FileUtils.copyFile(file, new File("evidence\\case7\\test1.png"));
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
		FileUtils.copyFile(file, new File("evidence\\case7\\test2.png"));
	}

	@Test
	@Order(3)
	@DisplayName("テスト03 未提出の研修日の「詳細」ボタンを押下しセクション詳細画面に遷移")
	void test03() throws Exception {
		final WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));

		List<WebElement> tlist = webDriver.findElements(By.tagName("tr"));

		for (WebElement element : tlist) {
			String text = element.getText();
			System.out.println(text);
			if (text.contains("未提出")) {
				((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoView({block: 'center'});",
						element);
				final WebElement button = element.findElement(By.cssSelector("input[value='詳細']"));
				wait.until(ExpectedConditions.elementToBeClickable(button));
				button.click();
				break;
			}
		}

		wait.until(ExpectedConditions.titleContains("セクション詳細 | LMS"));
		final String title = webDriver.getTitle();
		assertEquals("セクション詳細 | LMS", title);

		File file = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(file, new File("evidence\\case7\\test3.png"));
	}

	@Test
	@Order(4)
	@DisplayName("テスト04 「提出する」ボタンを押下しレポート登録画面に遷移")
	void test04() throws Exception {
		final WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));
		final WebElement submit = webDriver.findElement(By.cssSelector("input[value='日報【デモ】を提出する']"));
		((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoView();", submit);
		submit.click();

		wait.until(ExpectedConditions.titleContains("レポート登録 | LMS"));
		final String title = webDriver.getTitle();
		assertEquals("レポート登録 | LMS", title);

		File file = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(file, new File("evidence\\case7\\test4.png"));

	}

	@Test
	@Order(5)
	@DisplayName("テスト05 報告内容を入力して「提出する」ボタンを押下し確認ボタン名が更新される")
	void test05() throws Exception {
		final WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));
		final WebElement textArea = webDriver.findElement(By.name("contentArray[0]"));
		final WebElement submit = webDriver.findElement(By.cssSelector(".btn.btn-primary"));

		textArea.clear();
		textArea.sendKeys("日報");
		submit.click();

		wait.until(ExpectedConditions.titleContains("セクション詳細 | LMS"));
		final WebElement button = webDriver
				.findElement(By.xpath("//*[@id=\"sectionDetail\"]/table/tbody/tr[2]/td/form/input[6]"));
		assertEquals("提出済み日報【デモ】を確認する", button.getAttribute("value"));

		File file = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(file, new File("evidence\\case7\\test5.png"));
	}

}
