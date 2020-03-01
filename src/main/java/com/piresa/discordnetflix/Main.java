package com.piresa.discordnetflix;

import net.arikia.dev.drpc.DiscordEventHandlers;
import net.arikia.dev.drpc.DiscordRPC;
import net.arikia.dev.drpc.DiscordRichPresence;

import javax.swing.*;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Scanner;

public class Main {

	private static boolean ready = false;

	public static void main(String[] args) {

		JFrame frame = new JFrame("Netflix");
		
		JLabel text = new JLabel("In Discord, set your active game to: \"Netflix\"");
		frame.getContentPane().add(text, SwingConstants.CENTER);
		frame.setResizable(false);
		frame.pack();
		frame.setVisible(true);

		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			System.out.println("Shutting down DiscordHook.");
			DiscordRPC.discordShutdown();
		}));

		initDiscord();
		new Thread(new Runnable() {
			public void run() {
				App.run();
			}
		}).start();
		frame.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
                int i=JOptionPane.showConfirmDialog(null, "Are you sure you'd like to close netflix?");
                if(i==0)
                	DiscordRPC.discordShutdown();
                	App.driver.quit();
                	frame.dispose();
                	DiscordRPC.discordShutdown();
                	App.driver.quit();
                	frame.dispose();
                    System.exit(0);
            }
		});
		

		int score = 0;
		System.out.println("Running callbacks...");

		while (true) {
			DiscordRPC.discordRunCallbacks();

			if (!ready)
				continue;
			try {
				Thread.sleep(200L);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			DiscordRPC.discordUpdatePresence(new DiscordRichPresence.Builder(App.state)
					.setBigImage("icon-large", "Netflix -> Discord implementation I'm working on, mostly functional. PM me if you'd like to help me test it c:").setDetails(App.details).setStartTimestamps(App.start)
					.setEndTimestamp(App.end).build());

			//System.out.println("Updated");
		}
	}

	private static void initDiscord() {
		App.start = System.currentTimeMillis() / 1000L;
		DiscordEventHandlers handlers = new DiscordEventHandlers.Builder().setReadyEventHandler((user) -> {
			Main.ready = true;
			System.out.println("Welcome " + user.username + "#" + user.discriminator + ".");
			DiscordRPC.discordUpdatePresence(new DiscordRichPresence.Builder(App.state)
					.setBigImage("icon-large", "Netflix -> Discord implementation I'm working on, mostly functional. PM me if you'd like to help me test it c:").setDetails(App.details).setStartTimestamps(App.start)
					.setEndTimestamp(App.end).build());
		}).build();
		DiscordRPC.discordInitialize("475061863122468884", handlers, true);
	}

}