package jp.co.sss.lms.ct.f02_faq;

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
 * 結合テスト よくある質問機能
 * ケース05
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース05 キーワード検索 正常系")
public class Case05 {

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
		FileUtils.copyFile(file, new File("evidence\\case5_1.png"));
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
		FileUtils.copyFile(file, new File("evidence\\case5_2.png"));
	}

	@Test
	@Order(3)
	@DisplayName("テスト03 上部メニューの「ヘルプ」リンクからヘルプ画面に遷移")
	void test03() throws Exception {
		final WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));

		final WebElement dropDown = webDriver.findElement(By.className("dropdown-toggle"));
		final WebElement help = webDriver.findElement(By.xpath("//*[@id=\"nav-content\"]/ul[1]/li[4]/ul/li[4]/a"));

		dropDown.click();
		help.click();

		wait.until(ExpectedConditions.titleContains("ヘルプ | LMS"));
		final String title = webDriver.getTitle();
		assertEquals("ヘルプ | LMS", title);

		File file = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(file, new File("evidence\\case5_3.png"));
	}

	@Test
	@Order(4)
	@DisplayName("テスト04 「よくある質問」リンクからよくある質問画面を別タブに開く")
	void test04() throws Exception {
		final WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));

		final WebElement question = webDriver.findElement(By.linkText("よくある質問"));

		question.click();

		Object[] windowHandles = webDriver.getWindowHandles().toArray();
		webDriver.switchTo().window((String) windowHandles[1]);

		wait.until(ExpectedConditions.titleContains("よくある質問 | LMS"));
		final String title = webDriver.getTitle();
		assertEquals("よくある質問 | LMS", title);

		File file = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(file, new File("evidence\\case5_4.png"));
	}

	@Test
	@Order(5)
	@DisplayName("テスト05 キーワード検索で該当キーワードを含む検索結果だけ表示")
	void test05() throws Exception {

		final WebElement keyword = webDriver.findElement(By.className("form-control"));
		final WebElement searchButton = webDriver.findElement(By.cssSelector(".btn.btn-primary"));

		keyword.clear();
		keyword.sendKeys("研修");
		searchButton.click();

		File file1 = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(file1, new File("evidence\\case5_5(1).png"));

		final List<WebElement> list = webDriver.findElements(By.className("sorting_1"));

		for (WebElement element : list) {
			((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoView();", element);
			element.click();
			String text = element.getText();
			System.out.println(text);
			assertTrue(text.contains("研修"));
		}

		File file2 = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(file2, new File("evidence\\case5_5(2).png"));

	}

	@Test
	@Order(6)
	@DisplayName("テスト06 「クリア」ボタン押下で入力したキーワードを消去")
	void test06() throws Exception {
		final WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));
		final WebElement clear = webDriver.findElement(By.cssSelector("input[value='クリア']"));
		final WebElement keyword = webDriver.findElement(By.className("form-control"));

		((JavascriptExecutor) webDriver).executeScript("window.scrollTo(0,0);");
		wait.until(ExpectedConditions.elementToBeClickable(clear));
		clear.click();
		assertEquals("", keyword.getAttribute("value"));

		File file = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(file, new File("evidence\\case5_6.png"));

	}

}
