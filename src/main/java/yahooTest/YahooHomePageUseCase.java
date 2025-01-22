package yahooTest;

import java.time.Duration;
import java.util.regex.Pattern;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;


public class YahooHomePageUseCase {
	
	  public YahooHomePageUseCase() {
		  
	        PageFactory.initElements(driver, this);
	    }

	@FindBy(xpath = "//input[@placeholder='Search for news, symbols or companies']")
	private WebElement txt_search;
	
	@FindBy(xpath = "(//div[@data-id='search-assist-input-sugglst'])[1]")
	private WebElement searchListBox;
	
	@FindBy(xpath = "(//ul[@role='listbox']//li)[1]")
	private WebElement shareLists;
	
	@FindBy(xpath = "//div[contains(@class,'modules-module_errorMessage')]")
	private WebElement msg;
	
	@FindBy(xpath = "//span[@data-testid='qsp-price']")
	private WebElement currentrpice;
	
	@FindBy(xpath = "//*[@data-field='regularMarketPreviousClose']")
	private WebElement previousClose;
	
	@FindBy(xpath = "//*[@data-field='regularMarketVolume']")
	private WebElement volume;
	
	private static final String REGEX = "[A-Z]+";
	WebDriver driver = new ChromeDriver();
	String homeUrl = "https://finance.yahoo.com";
	String expectedTxt = "Tesla, Inc.";


	@AfterMethod
	public void teardown() {
		driver.quit();
	}

	@DataProvider(name = "dataprovider")

	public Object[][] inputdata() {
		return new Object[][] {{"TSLA"},{"TYUTU"},{"!@#$%"},{""},{}};
	}

	@Test(dataProvider = "dataprovider")

	public void validateStockCode(String stockcode) {

		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
			driver.get(homeUrl);
			driver.manage().window().maximize();
			wait.until(ExpectedConditions.visibilityOf(txt_search));
			txt_search.clear();
			boolean matcher = Pattern.matches(REGEX, stockcode);
			if(!matcher) {
				
				throw new Exception("Stock code is invalid");
				
			}
			if (!stockcode.equals("") && stockcode != null) {
				txt_search.sendKeys(stockcode);
				wait.until(ExpectedConditions.visibilityOf(searchListBox));
				String compName = shareLists.getAttribute("title");

				if (compName.equalsIgnoreCase(expectedTxt)) {

					shareLists.click();
					System.out.println("Tesla share is clicked as Expected ");

				} else {

					System.out.print("No matching found with msg : " + msg.getText());

				}

				((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", currentrpice);
				wait.until(ExpectedConditions.visibilityOf(currentrpice));
				String values = currentrpice.getText();
				Float actualvalu = Float.parseFloat(values);

				if (actualvalu > 200) {

					wait.until(ExpectedConditions.visibilityOf(previousClose));
					String prevClose = previousClose.getText();
					wait.until(ExpectedConditions.visibilityOf(volume));
					String Totalvolume = volume.getText();

					System.out.println("Previous close for the Stock : " + prevClose);

					System.out.println("Market Volume for the Stock : " + Totalvolume);

				} else {

					System.out.print("Share value is less than 200");

				}
			} else {

				System.out.println("Stock code cannot be empty");

			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}

}
