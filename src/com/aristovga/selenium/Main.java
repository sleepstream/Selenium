package com.aristovga.selenium;

import com.google.common.html.HtmlEscapers;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.List;

public class Main {
    private static boolean subMenuWWS = false;
    private static boolean MenuWWS = false;
    private static boolean MenuSub = false;
    private static boolean MenuAb = false;
    private static boolean SidePanel = false;
    private static boolean EducationHeader = false;
    private static boolean StudentTest3 = false;
    private static boolean backPressed = false;
    private static boolean StudentTest3LearnMore=false;
    private static boolean StudentTest3LinkStudents=false;
    public static String  MainLink = "https://www.wiley.com";
    private static boolean test5LogoToHomepage = false;
    private static boolean test6EmptySearch=false;
    private static boolean SuggestionsTest=false;
    private static boolean ProductsTest=false;
    private static boolean test6ShowSearchResult = false;
    private static boolean test8SearchResultPage = false;

    private static List<String> SearchResultItems = new ArrayList<>();
    private static boolean test9SearchResultPage=false;

    public static void main (String[] args)
            {
                System.setProperty("webdriver.chrome.driver", "C:\\chromedriver.exe");
                System.out.println("helllo");




                WebDriver driver = new ChromeDriver();
                driver.get(MainLink+"/en-us");
                WebDriverWait wait = new WebDriverWait(driver, 60);
                WebElement element = wait.until(ExpectedConditions.elementToBeClickable(By.id("main-header-navbar")));

                driver.findElement(By.xpath("//*[@id=\"country-location-form\"]/div[3]/button[2]")).click();
                wait.until(ExpectedConditions.not(ExpectedConditions.visibilityOf(driver.findElement(By.className("modal-dialog")))));


                List<WebElement> listSubMenu = getListOfNavMenu(driver);



                //нужно через счетчик. чтобы потом восстанавливать ссылку нужно переписать
                for(int i =0; i<listSubMenu.size(); i++)
                {
                    //reinicialize after back pressed
                    if(backPressed) {
                        listSubMenu = getListOfNavMenu(driver);
                        backPressed = false;
                    }
                    WebElement item = listSubMenu.get(i);
                    WebElement link = item.findElement(By.className("collapsed"));
                    String name = link.getText();

                    if(link.isDisplayed()) {
                        switch (name.toLowerCase()) {
                            case "who we serve": {
                                MenuWWS = true;
                                //in scenario  only 11 items but on page there are 12 items under sub-header - test failed
                                //maybe mistake in scenario
                                String[] titles = {"Students", "Instructors", "Book Authors",
                                        "Professionals", "Researchers", "Institutions",
                                        "Librarians", "Corporations", "Societies", "Journal Editors",
                                        "Bookstores",
                                        "Government"};
                                List<WebElement> listSubMenuWWS = item.findElements(By.className("dropdown-item"));
                                if (listSubMenuWWS.size() == titles.length) {
                                    int count = 0;

                                    for (WebElement subItem : listSubMenuWWS) {
                                        if (!subItem.findElement(By.tagName("a")).getAttribute("innerHTML").trim().equals(HtmlEscapers.htmlEscaper().escape(titles[count])))
                                            break;
                                        count++;
                                    }
                                    if(count==titles.length)
                                        subMenuWWS = true;
                                }
                                for (int j=0; j<listSubMenuWWS.size(); j++) {

                                    WebElement subItem = listSubMenuWWS.get(j);
                                    if(backPressed)
                                    {
                                        listSubMenu = getListOfNavMenu(driver);
                                        item = listSubMenu.get(i);
                                        listSubMenuWWS = item.findElements(By.className("dropdown-item"));
                                        subItem = listSubMenuWWS.get(j);
                                        backPressed = false;
                                    }

                                    Actions action = new Actions(driver);
                                    action.moveToElement(item).build().perform();
                                    wait.until(ExpectedConditions.visibilityOf(item));
                                    WebElement tmp = subItem.findElement(By.tagName("a"));
                                    //action.moveToElement(tmp).build().perform();
                                    wait.until(ExpectedConditions.visibilityOf(tmp));
                                    if (tmp.getAttribute("innerHTML").trim().equals("Students")) {
                                        //((ChromeDriver) driver).executeScript("arguments[0].click();", tmp);
                                        tmp.click();
                                        try {
                                            wait.until(ExpectedConditions.titleContains("Students"));
                                            if(driver.getCurrentUrl().equals("https://www.wiley.com/en-us/students"))
                                                StudentTest3LinkStudents = true;
                                            StudentTest3 = true;
                                            WebElement learnMoreLink = driver.findElement(By.partialLinkText("Learn More"));
                                            String url = learnMoreLink.getAttribute("href");
                                            if(url != null && url.contains("wileyplus.com"))
                                                StudentTest3LearnMore = true;
                                        }
                                        catch (Exception ex )
                                        {
                                            ex.printStackTrace();
                                        }


                                        driver.get(MainLink+"/en-us");
                                        backPressed = true;
                                        try {
                                            wait.until(ExpectedConditions.titleContains("Homepage"));
                                        }
                                        catch (Exception ex )
                                        {
                                            ex.printStackTrace();
                                        }
                                    }




                                    //break;



                                }
                                break;
                            }
                            case "subjects": {
                                MenuSub = true;
                                String[] sidePanelLinks = {"Information & Library Science",
                                        "Education & Public Policy",
                                        "K-12 General",
                                        "Higher Education General",
                                        "Vocational Technology",
                                        "Conflict Resolution & Mediation (School settings)",
                                        "Curriculum Tools- General",
                                        "Special Educational Needs",
                                        "Theory of Education",
                                        "Education Special Topics",
                                        "Educational Research & Statistics",
                                        "Literacy & Reading",
                                        "Classroom Management"};

                                Actions action = new Actions(driver);
                                action.moveToElement(item).build().perform();

                                List<WebElement> ListSubMenuSub = item.findElements(By.cssSelector(".dropdown-item.dropdown-submenu"));

                                for(WebElement itemSubMenu : ListSubMenuSub)
                                {
                                    action.moveToElement(itemSubMenu).build().perform();

                                    WebElement SubMenuSub = itemSubMenu.findElement(By.tagName("a"));
                                    String tmp = SubMenuSub.getAttribute("innerHTML").trim();
                                    if(tmp.equals("Education"))
                                    {
                                        String linkEd = SubMenuSub.getAttribute("href");

                                        WebDriver driverSub = new ChromeDriver();
                                        driverSub.get(linkEd);
                                        wait = new WebDriverWait(driverSub, 60);
                                        wait.until(ExpectedConditions.elementToBeClickable(By.id("main-header-navbar")));

                                        if(driverSub.getTitle().contains("Education"))
                                            EducationHeader = true;

                                        WebElement mainElementSub = driverSub.findElement(By.className("side-panel"));

                                        List<WebElement> listMainElementSub = mainElementSub.findElements(By.tagName("li"));
                                        int count =0;
                                        for (WebElement itemPanel : listMainElementSub) {
                                            String textLink = itemPanel.findElement(By.tagName("a")).getAttribute("innerHTML");

                                            if (!textLink.equals(HtmlEscapers.htmlEscaper().escape(sidePanelLinks[count]))) {
                                                SidePanel = false;
                                                break;
                                            } else
                                                SidePanel = true;

                                            count++;
                                        }

                                        driverSub.quit();
                                    }
                                }
                                //WebElement SubMenuSub = item.findElement(By.linkText("Education"));


                                break;
                            }
                            case "about": {
                                MenuAb = true;
                                Actions action = new Actions(driver);
                                action.moveToElement(item).build().perform();
                                break;
                            }
                        }
                    }
                }
                //test 5
                chechLogo(driver);

                //test 6
                checkSearch(driver, 1);
                checkSearch( driver, 2);
                checkSearch( driver, 3);
                checkSearch( driver, 4);

                //Results
                System.out.println("Test 1: "+(MenuAb & MenuSub & MenuWWS?"passed":"fail!"));
                System.out.println("Test 2: "+(subMenuWWS?"passed":"fail!"));
                System.out.println("Test 3: "+(StudentTest3 & StudentTest3LearnMore & StudentTest3LinkStudents?"passed":"fail!"));
                System.out.println("Test 4: "+(SidePanel & EducationHeader?"passed":"fail!"));
                System.out.println("Test 5: "+(test5LogoToHomepage?"passed":"fail!"));
                System.out.println("Test 6: "+(test6EmptySearch ?"passed":"fail!"));
                System.out.println("Test 7: "+(test6ShowSearchResult & SuggestionsTest & ProductsTest?"passed":"fail!"));
                System.out.println("Test 8: "+(test8SearchResultPage?"passed":"fail!"));
                System.out.println("Test 9: "+(test9SearchResultPage?"passed":"fail!"));

                driver.quit();

    }

    public  static void chechLogo(WebDriver driver)
    {
        driver.findElement(By.className("simple-responsive-banner-component")).click();
        try {
            WebDriverWait wait = new WebDriverWait(driver, 10);
            wait.until(ExpectedConditions.titleContains("Homepage"));
            test5LogoToHomepage = true;
        }
        catch (Exception ex )
        {
            ex.printStackTrace();
        }

    }

    public  static void checkSearch(WebDriver driver, int key)
    {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        switch (key)
        {
            case 1: {

                driver.findElement(By.className("input-group")).findElement(By.tagName("button")).click();
                try {

                    wait.until(ExpectedConditions.titleContains("Homepage"));
                    test6EmptySearch = true;
                } catch (Exception ex) {
                    ex.printStackTrace();
                    test6EmptySearch = false;
                }
                break;
            }
            case 2: {
                try {
                    driver.findElement(By.className("main-navigation-search")).findElement(By.className("input-group")).findElement(By.tagName("input")).sendKeys("java");
                    wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.className("main-navigation-search")).findElement(By.tagName("aside"))));
                    test6ShowSearchResult = true;
                    //WebElement item = driver.findElement(By.className("main-navigation-search")).findElement(By.className("search_form_SearchBox")).findElement(By.tagName("aside"));
                    //Actions action = new Actions(driver);
                    //action.moveToElement(item).build().perform();

                    List<WebElement> sections = driver.findElement(By.className("main-navigation-search")).findElement(By.tagName("aside")).findElements(By.tagName("section"));

                    for (WebElement elem : sections) {
                        String title = elem.findElement(By.tagName("h3")).getAttribute("innerHTML").trim();

                        switch (title) {
                            case "Suggestions": {
                                //Actions action = new Actions(driver);
                                //action.moveToElement(elem).build().perform();
                                List<WebElement> searchResult = elem.findElement(By.className("search-list")).findElements(By.tagName("div"));
                                if (searchResult.size() == 4) {
                                    for (WebElement result : searchResult) {
                                        String tmp = result.findElement(By.className("search-highlight")).getAttribute("innerHTML").trim().toLowerCase();
                                        if (tmp.equals("java"))
                                            SuggestionsTest = true;
                                        else {
                                            SuggestionsTest = false;
                                            break;
                                        }
                                    }
                                } else {
                                    SuggestionsTest = false;
                                }

                                break;
                            }
                            case "Products": {
                                List<WebElement> searchResult = elem.findElement(By.className("related-content-products")).findElements(By.tagName("div"));
                                if (searchResult.size() == 4) {
                                    //in scenario 5 items in result but in fact have only 4
                                //if (searchResult.size() == 5) {
                                    for (WebElement result : searchResult) {
                                        String tmp = result.findElement(By.className("search-highlight")).getAttribute("innerHTML").trim().toLowerCase();
                                        if (tmp.equals("java"))
                                            ProductsTest = true;
                                        else {
                                            ProductsTest = false;
                                            break;
                                        }
                                    }
                                } else {
                                    ProductsTest = false;
                                }
                                break;
                            }
                        }
                    }
                }
                catch(Exception ex)
                {
                    ex.printStackTrace();
                    test6ShowSearchResult = false;
                }
                break;
            }
            case 3:
            {
                driver.findElement(By.className("input-group")).findElement(By.tagName("button")).click();
                try {

                    wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.className("search-result-page"))));
                    test8SearchResultPage = true;
                    List<WebElement> productsList = driver.findElement(By.className("search-result-page")).findElements(By.className("product-item"));
                    if(productsList.size() == 10)
                    {
                        int  tmpCount = 0;
                        for(WebElement product : productsList)
                        {
                            SearchResultItems.add(product.getAttribute("innerHTML"));
                            if(product.findElement(By.className("product-title")).findElement(By.className("search-highlight")).getAttribute("innerHTML").trim().toLowerCase().contains("java")
                            && product.findElements(By.className("product-button")).size() >0)
                            {
                                tmpCount++;
                            }
                        }

                        if(tmpCount == 10)
                            test8SearchResultPage = true;
                        else
                            test8SearchResultPage = false;


                    }
                    else
                    {
                        test8SearchResultPage = false;
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    test8SearchResultPage = false;
                }

                break;
            }
            case 4:
            {
                driver.findElement(By.className("main-navigation-search")).findElement(By.className("input-group")).findElement(By.tagName("input")).clear();
                driver.findElement(By.className("main-navigation-search")).findElement(By.className("input-group")).findElement(By.tagName("input")).sendKeys("java");
                driver.findElement(By.className("input-group")).findElement(By.tagName("button")).click();


                try {
                    wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.className("search-result-page"))));
                    test9SearchResultPage = true;
                    List<WebElement> productsList = driver.findElement(By.className("search-result-page")).findElements(By.className("product-item"));
                    if(productsList.size() == 10)
                    {
                        int k = 0;
                        for(WebElement product : productsList)
                        {
                            if(product.getAttribute("innerHTML").equals(SearchResultItems.get(k)))
                            {
                                test9SearchResultPage = true;
                            }
                            else
                            {
                                test9SearchResultPage = false;
                                break;
                            }

                            k++;
                        }
                    }
                    else
                    {
                        test9SearchResultPage = false;
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    test9SearchResultPage = false;
                }
                break;
            }

            }

    }

    public static List<WebElement> getListOfNavMenu(WebDriver driver)
    {

        WebElement mainElement = driver.findElement(By.id("main-header-navbar"));

        List<WebElement> listSubMenu = mainElement.findElements(By.className("dropdown-submenu"));

        return listSubMenu;
    }
}
