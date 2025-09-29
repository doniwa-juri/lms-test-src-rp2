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
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * 結合テスト レポート機能
 * ケース09
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース09 受講生 レポート登録 入力チェック")
public class Case09 {

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
		FileUtils.copyFile(file, new File("evidence\\case9\\test1.png"));
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
		FileUtils.copyFile(file, new File("evidence\\case9\\test2.png"));
	}

	@Test
	@Order(3)
	@DisplayName("テスト03 上部メニューの「ようこそ○○さん」リンクからユーザー詳細画面に遷移")
	void test03() throws Exception {
		final WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));
		final WebElement link = webDriver.findElement(By.xpath("//*[@id=\"nav-content\"]/ul[2]/li[2]/a"));
		link.click();

		wait.until(ExpectedConditions.titleContains("ユーザー詳細"));
		final String title = webDriver.getTitle();
		assertEquals("ユーザー詳細", title);

		File file = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(file, new File("evidence\\case9\\test3.png"));
	}

	@Test
	@Order(4)
	@DisplayName("テスト04 該当レポートの「修正する」ボタンを押下しレポート登録画面に遷移")
	void test04() throws Exception {

		final WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));
		List<WebElement> tlist = webDriver.findElements(By.tagName("tr"));

		for (WebElement element : tlist) {
			String text = element.getText();
			System.out.println(text);
			if (text.contains("2025年7月9日(水)") && text.contains("週報【デモ】")) {
				((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoView({block: 'center'});",
						element);
				final WebElement button = element.findElement(By.cssSelector("input[value='修正する']"));
				wait.until(ExpectedConditions.elementToBeClickable(button));
				button.click();
				break;
			}
		}

		wait.until(ExpectedConditions.titleContains("レポート登録 | LMS"));
		final String title = webDriver.getTitle();
		assertEquals("レポート登録 | LMS", title);

		File file = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(file, new File("evidence\\case9\\test4.png"));

	}

	@Test
	@Order(5)
	@DisplayName("テスト05 報告内容を修正して「提出する」ボタンを押下しエラー表示：学習項目が入力されているとき、理解度が未入力")
	void test05() throws Exception {
		final WebElement section = webDriver.findElement(By.id("intFieldName_0"));
		final Select understand = new Select(webDriver.findElement(By.id("intFieldValue_0")));
		((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoView({block: 'center'});",
				section);
		section.clear();
		section.sendKeys("ITリテラシー");
		understand.selectByIndex(0);

		final WebElement submit = webDriver.findElement(By.cssSelector(".btn.btn-primary"));
		((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoView({block: 'center'});",
				submit);
		submit.click();

		final WebElement newUnderstand = webDriver.findElement(By.id("intFieldValue_0"));
		((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoView({block: 'center'});",
				newUnderstand);
		assertEquals("form-control errorInput", newUnderstand.getAttribute("class"));

		File file = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(file, new File("evidence\\case9\\test5.png"));

		final Select select = new Select(newUnderstand);
		select.selectByIndex(1);
	}

	@Test
	@Order(6)
	@DisplayName("テスト06 不適切な内容で修正して「提出する」ボタンを押下しエラー表示：理解度が入力されているとき学習項目が未入力")
	void test06() throws Exception {
		final Select understand = new Select(webDriver.findElement(By.id("intFieldValue_0")));
		final WebElement section = webDriver.findElement(By.id("intFieldName_0"));
		((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoView({block: 'center'});",
				understand);
		understand.selectByIndex(1);
		section.clear();

		final WebElement submit = webDriver.findElement(By.cssSelector(".btn.btn-primary"));
		((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoView({block: 'center'});",
				submit);
		submit.click();

		final WebElement newSection = webDriver.findElement(By.id("intFieldName_0"));
		((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoView({block: 'center'});",
				newSection);
		assertEquals("form-control errorInput", newSection.getAttribute("class"));

		File file = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(file, new File("evidence\\case9\\test6.png"));

		newSection.clear();
		newSection.sendKeys("ITリテラシー");
	}

	@Test
	@Order(7)
	@DisplayName("テスト07 不適切な内容で修正して「提出する」ボタンを押下しエラー表示：目標の達成度が数値以外")
	void test07() throws Exception {
		final WebElement achievement = webDriver.findElement(By.id("content_0"));
		((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoView({block: 'center'});",
				achievement);
		achievement.clear();
		achievement.sendKeys("達成");

		final WebElement submit = webDriver.findElement(By.cssSelector(".btn.btn-primary"));
		((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoView({block: 'center'});",
				submit);
		submit.click();

		final WebElement newAchievement = webDriver.findElement(By.id("content_0"));
		((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoView({block: 'center'});",
				newAchievement);
		assertEquals("form-control errorInput", newAchievement.getAttribute("class"));

		File file = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(file, new File("evidence\\case9\\test7.png"));

		newAchievement.sendKeys("7");

	}

	@Test
	@Order(8)
	@DisplayName("テスト08 不適切な内容で修正して「提出する」ボタンを押下しエラー表示：目標の達成度が範囲外")
	void test08() throws Exception {
		final WebElement achievement = webDriver.findElement(By.id("content_0"));
		((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoView({block: 'center'});",
				achievement);
		achievement.clear();
		achievement.sendKeys("50");

		final WebElement submit = webDriver.findElement(By.cssSelector(".btn.btn-primary"));
		((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoView({block: 'center'});",
				submit);
		submit.click();

		final WebElement newAchievement = webDriver.findElement(By.id("content_0"));
		((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoView({block: 'center'});",
				newAchievement);
		assertEquals("form-control errorInput", newAchievement.getAttribute("class"));

		File file = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(file, new File("evidence\\case9\\test8.png"));

		newAchievement.sendKeys("7");
	}

	@Test
	@Order(9)
	@DisplayName("テスト09 不適切な内容で修正して「提出する」ボタンを押下しエラー表示：目標の達成度・所感が未入力")
	void test09() throws Exception {
		final WebElement achievement = webDriver.findElement(By.id("content_0"));
		final WebElement impression = webDriver.findElement(By.id("content_1"));
		((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoView({block: 'center'});",
				achievement);
		achievement.clear();
		impression.clear();

		final WebElement submit = webDriver.findElement(By.cssSelector(".btn.btn-primary"));
		((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoView({block: 'center'});",
				submit);
		submit.click();

		final WebElement newAchievement = webDriver.findElement(By.id("content_0"));
		final WebElement newImpression = webDriver.findElement(By.id("content_1"));
		((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoView({block: 'center'});",
				newAchievement, newImpression);

		assertEquals("form-control errorInput", newAchievement.getAttribute("class"));
		assertEquals("form-control errorInput", newImpression.getAttribute("class"));

		File file = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(file, new File("evidence\\case9\\test9.png"));

		newAchievement.sendKeys("7");
		newImpression.sendKeys("所感");
	}

	@Test
	@Order(10)
	@DisplayName("テスト10 不適切な内容で修正して「提出する」ボタンを押下しエラー表示：所感・一週間の振り返りが2000文字超")
	void test10() throws Exception {
		final WebElement impression = webDriver.findElement(By.id("content_1"));
		final WebElement reflection = webDriver.findElement(By.id("content_2"));
		((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoView({block: 'center'});",
				impression);

		StringBuilder st = new StringBuilder();
		for (int i = 0; i < 2010; i++) {
			st.append("a");
		}

		impression.clear();
		impression.sendKeys(st);
		reflection.clear();
		reflection.sendKeys(st);

		final WebElement submit = webDriver.findElement(By.cssSelector(".btn.btn-primary"));
		((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoView({block: 'center'});",
				submit);
		submit.click();

		final WebElement newImpression = webDriver.findElement(By.id("content_1"));
		final WebElement newReflection = webDriver.findElement(By.id("content_2"));
		((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoView({block: 'center'});",
				newImpression, newReflection);

		assertEquals("form-control errorInput", newReflection.getAttribute("class"));
		assertEquals("form-control errorInput", newImpression.getAttribute("class"));

		File file = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(file, new File("evidence\\case9\\test10.png"));

		newImpression.clear();
		newImpression.sendKeys("所感");
		newReflection.clear();
		newReflection.sendKeys("1週間頑張りました");
	}

}
