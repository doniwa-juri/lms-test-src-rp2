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
 * ケース08
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース08 受講生 レポート修正(週報) 正常系")
public class Case08 {

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
		FileUtils.copyFile(file, new File("evidence\\case8\\test1.png"));
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
		FileUtils.copyFile(file, new File("evidence\\case8\\test2.png"));
	}

	@Test
	@Order(3)
	@DisplayName("テスト03 提出済の研修日の「詳細」ボタンを押下しセクション詳細画面に遷移")
	void test03() throws Exception {
		final WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));

		List<WebElement> tlist = webDriver.findElements(By.tagName("tr"));

		WebElement element = tlist.get(1);

		String text = element.getText();
		System.out.println(text);
		if (text.contains("提出済み")) {
			((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoView({block: 'center'});",
					element);
			final WebElement button = element.findElement(By.cssSelector("input[value='詳細']"));
			wait.until(ExpectedConditions.elementToBeClickable(button));
			button.click();
		}

		wait.until(ExpectedConditions.titleContains("セクション詳細 | LMS"));

		final String title = webDriver.getTitle();

		assertEquals("セクション詳細 | LMS", title);

		File file = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(file, new File("evidence\\case8\\test3.png"));
	}

	@Test
	@Order(4)
	@DisplayName("テスト04 「確認する」ボタンを押下しレポート登録画面に遷移")
	void test04() throws Exception {
		final WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));
		final WebElement submit = webDriver.findElement(By.cssSelector("input[value='提出済み週報【デモ】を確認する']"));
		((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoView();", submit);
		submit.click();

		wait.until(ExpectedConditions.titleContains("レポート登録 | LMS"));
		final String title = webDriver.getTitle();
		assertEquals("レポート登録 | LMS", title);

		File file = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(file, new File("evidence\\case8\\test4.png"));
	}

	@Test
	@Order(5)
	@DisplayName("テスト05 報告内容を修正して「提出する」ボタンを押下しセクション詳細画面に遷移")
	void test05() throws Exception {
		final WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));
		final WebElement textArea1 = webDriver.findElement(By.id("content_1"));
		final WebElement textArea2 = webDriver.findElement(By.id("content_2"));
		final WebElement submit = webDriver.findElement(By.cssSelector(".btn.btn-primary"));

		textArea1.clear();
		textArea1.sendKeys("週報");

		textArea2.clear();
		textArea2.sendKeys("1週間頑張りました");

		File file1 = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(file1, new File("evidence\\case8\\test5(1).png"));

		((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoView({block: 'center'});",
				submit);
		submit.click();

		wait.until(ExpectedConditions.titleContains("セクション詳細 | LMS"));
		final String title = webDriver.getTitle();
		assertEquals("セクション詳細 | LMS", title);

		File file2 = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(file2, new File("evidence\\case8\\test5(2).png"));
	}

	@Test
	@Order(6)
	@DisplayName("テスト06 上部メニューの「ようこそ○○さん」リンクからユーザー詳細画面に遷移")
	void test06() throws Exception {
		final WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));
		final WebElement link = webDriver.findElement(By.xpath("//*[@id=\"nav-content\"]/ul[2]/li[2]/a"));
		link.click();

		wait.until(ExpectedConditions.titleContains("ユーザー詳細"));
		final String title = webDriver.getTitle();
		assertEquals("ユーザー詳細", title);

		File file = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(file, new File("evidence\\case8\\test6.png"));
	}

	@Test
	@Order(7)
	@DisplayName("テスト07 該当レポートの「詳細」ボタンを押下しレポート詳細画面で修正内容が反映される")
	void test07() throws Exception {
		final WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));
		List<WebElement> tlist = webDriver.findElements(By.tagName("tr"));

		for (WebElement element : tlist) {
			String text = element.getText();
			System.out.println(text);
			if (text.contains("2025年7月9日(水)") && text.contains("週報【デモ】")) {
				((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoView({block: 'center'});",
						element);
				final WebElement button = element.findElement(By.cssSelector("input[value='詳細']"));
				wait.until(ExpectedConditions.elementToBeClickable(button));
				button.click();
				break;
			}
		}

		final WebElement text1 = webDriver.findElement(By.xpath("//*[@id=\"main\"]/div[2]/table/tbody/tr[2]/td"));
		final WebElement text2 = webDriver.findElement(By.xpath("//*[@id=\"main\"]/div[2]/table/tbody/tr[3]/td"));

		((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoView({block: 'center'});",
				text1);

		assertEquals("週報", text1.getText());
		assertEquals("1週間頑張りました", text2.getText());

		File file = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(file, new File("evidence\\case8\\test7.png"));
	}

}
