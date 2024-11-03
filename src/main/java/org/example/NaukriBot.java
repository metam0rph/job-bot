package org.example;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.Cookie;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class NaukriBot {

    // Define job search criteria here
    private static ConfigLoader cl = new ConfigLoader("config.properties");
    private static final String JOB_TITLE = cl.getProperty("job-name");
    private static final String LOCATION = cl.getProperty("location");

    private static String userDataDir = FileUtil.getOrCreate("c:\\BotData");
    private static final String USERNAME = cl.getProperty("login-id");
    private static final String PASSWORD = cl.getProperty("password");

    public static void main(String[] args) {
        try (Playwright playwright = Playwright.create()) {
            BrowserType.LaunchPersistentContextOptions contextOptions = new BrowserType.LaunchPersistentContextOptions()
                    .setHeadless(false); // setHeadless(false) to see browser actions

            // Launch the browser with a persistent context, storing data in the specified directory
            BrowserContext context = playwright.chromium().launchPersistentContext(Paths.get(userDataDir+ File.separator), contextOptions);

            // Open a new page within the persistent context
            Page page = context.pages().get(0); // Get the first page (default) after opening the context
            if (page == null) {
                page = context.newPage(); // Create a new page if none exist
            }
            // Navigate to Naukri's login page
            List<Cookie> cookies = context.cookies("https://www.naukri.com");
            if (cookies.isEmpty()) {
                System.out.println("No session found. User is not logged in.");
                page.navigate("https://www.naukri.com/nlogin/login");
                page.locator("input[placeholder='Enter Email ID / Username']").fill(USERNAME);
                page.locator("input[placeholder='Enter Password']").fill(PASSWORD);
                page.locator("button[type='submit'].blue-btn").click();
                page.waitForURL("https://www.naukri.com/mnjuser/homepage");
            } else {
                System.out.println("Session exists. User is logged in.");
                page.navigate("https://www.naukri.com/java-microservice-developer-java-spring-boot-jobs-in-faridabad?k=java%20microservice%20developer%2C%20java%20spring%20boot&l=faridabad%2C%20noida&nignbevent_src=jobsearchDeskGNB&experience=3&functionAreaIdGid=5&glbl_qcrc=1028");

                // Continue with actions requiring login
            }
            // Log in to Naukri

            // Wait for login to complete
           // Navigate to job search page and set filters
//            page.navigate("https://www.naukri.com/");
//            page.locator("span:has-text('Search jobs here')").click();
//            page.locator("input[class='suggestor-input'][placeholder='Enter keyword / designation / companies']").fill(JOB_TITLE);
//            page.locator("input[class='suggestor-input'][placeholder='Enter keyword / designation / companies']").fill(JOB_TITLE);
//            page.locator("input[placeholder='Enter location']").fill(LOCATION);
//            page.locator("button[type='submit']").click();

            // Wait for job listings to load
            page.waitForSelector(".srp-jobtuple-wrapper");

            // Loop through job listings and apply
            Locator jobListings = page.locator(".srp-jobtuple-wrapper");
            int count = jobListings.count();

            for (int i = 0; i < count; i++) {
                Locator job = jobListings.nth(i);

                // Click on the job listing
                job.click();

                // Switch to the new job listing tab

                Page jobPage = page.context().pages().get(1); // Get the newly opened tab
                jobPage.waitForLoadState();
                Locator applyButton = jobPage.locator(".apply-button");
                Locator applyOnSiteButton = jobPage.locator(".apply-on-site-button");


                // Check if the 'Apply' button is present and apply
                if (applyButton.nth(0).isVisible()) {
                    applyButton.nth(0).click();
                    System.out.println("Applied to job: " + jobPage.title());
                } else if (applyOnSiteButton.isVisible()) {
                    applyOnSiteButton.click();
                    System.out.println("Clicked on Apply on company site button.");
                } else {
                    System.out.println("Apply button not found for job: " + jobPage.title());
                }

                // Close the job listing tab and return to main page
                jobPage.close();
            }

            // Close browser after application process
            context.close();
        }
    }

}
