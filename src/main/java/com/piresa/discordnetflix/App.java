package com.piresa.discordnetflix;

import java.util.Scanner;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

/**
 * Hello world!
 *
 */
public class App {
	private static int lastState = 0;

	public static String details = "";
	public static String state = "Logging in...";
	public static Long start = 0L;
	public static Long end = 0L;
	public static WebDriver driver;
	
	private int[] convert = {1,60,3600};
	public static void run() {
		System.setProperty("webdriver.gecko.driver", "D:\\New folder\\Programming\\geckodriver.exe");
		driver = new FirefoxDriver();
		String baseUrl = "https://netflix.com/login";
		driver.get(baseUrl + "/");

		while (true) {
			try {
				// check if logging in
				String status = driver.getCurrentUrl().substring(24);

				if (status.substring(0, 5).equalsIgnoreCase("login")) {
					if (lastState != 0) {
						details = "";
						state = "Logging in...";
						start = System.currentTimeMillis() / 1000L;
						end = 0L;
						lastState = 0;
					}
				} else if (status.substring(0, 6).equalsIgnoreCase("browse")
						|| status.substring(0, 5).equalsIgnoreCase("title")
						|| status.substring(0, 6).equalsIgnoreCase("search")) {
					details = "";
					state = "Browsing...";
					if(lastState ==2){
						start = System.currentTimeMillis()/1000L;
					}
					end = 0L;
					lastState = 1;
				} else if (status.substring(0, 5).equalsIgnoreCase("watch")) {
					start = System.currentTimeMillis() / 1000L;
					// end = 0L;
					lastState = 2;

					// Attempt to find show name
					try {
						String name = driver.findElement(By.className("title")).getText();
						System.out.println("\nName: " + name);
						if( !details.equalsIgnoreCase(name) && lastState == 2 && !details.equals("")){
							state = name;
						}else
						details = name;

					} catch (NoSuchElementException e) {
						
						e.printStackTrace();
					}

					// atempt to find episode name
					try {
						String episode = driver.findElement(By.className("playable-title")).getText();
						System.out.println("Ep: " + episode);
						state = episode;
					} catch (NoSuchElementException e) {
						if(state.equals("Browsing..."))
							state = "";
						e.printStackTrace();
					}

					// attempt to find remaining time
					try {
						String remaining = driver.findElement(By.className("time-remaining__time")).getText();
						if (!remaining.equals("")) {
							System.out.println("Remaining :" + remaining);
							int secs = Integer.parseInt(remaining.substring(remaining.lastIndexOf(":") + 1));
							int mins = 0;
							int hrs = 0;
							remaining = remaining.substring(0,remaining.lastIndexOf(":"));
							if(remaining.contains(":")){
								mins = Integer.parseInt(remaining.substring(remaining.indexOf(":") + 1));
								hrs = Integer.parseInt(remaining.substring(0,(remaining.indexOf(":"))));
							}else{
								mins = Integer.parseInt(remaining);
							}
							start = 0L;
							System.out.printf("%s %s %s%n",secs,mins,hrs);
							end = System.currentTimeMillis()/1000L + secs + (mins * 60) + (hrs*3600);
						}

					} catch (NoSuchElementException e) {
						e.printStackTrace();
					}

				}

				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

	}
}
