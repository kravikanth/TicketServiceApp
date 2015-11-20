package com.mycompany;

import java.util.Optional;
import java.util.Scanner;

import com.mycompany.model.Level;
import com.mycompany.model.SimplePerformanceVenue;

/**
 * @author Ravikanth Kunchala
 *
 */
public class AppMain {
	public static void main(String[] args) {
		
		//Time out is hard coded to 30 seconds
		
		SimplePerformanceVenue venue = new SimplePerformanceVenue();
		TicketService ticketService = new TicketServiceManager(venue);
		try {
			try (Scanner in = new Scanner(System.in)) {
				while (true) {
					// read line from the user input
					System.out.println(
							"Please Select an operation " + "\nEnter 1 for - Number of seats in the requested level "
									+ "\nEnter 2 for - Find and hold the best available seats for a customer "
									+ "\nEnter 3 for - Commit seats held for a specific customer "
									+ "\nEnter 0 to  - QUIT " + "\nYour Selection:");

					int num = in.nextInt();
					switch (num) {
					case 1:

						numSeatsOption(venue, ticketService, in);
						break;
					case 2:
						holdSeatsOption(venue, ticketService, in);
						break;
					case 3:
						
						reserveOption(ticketService, in);
						break;
					default:
						System.out.println("Invalid Input");
					}
					if (num == 0) {
						return;
					}
				}
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private static void holdSeatsOption(SimplePerformanceVenue venue, TicketService ticketService, Scanner in) {
		System.out.println("\nEnter Number of Seats:");
		int numberOfSeats = in.nextInt();
		printLevelOptions(venue);
		System.out.println("\nEnter Min Level :");
		int minLevel = in.nextInt();

		System.out.println("\nEnter max Level :");
		int maxLevel = in.nextInt();
		System.out.println("\nEnter email :");
		in.nextLine();
		String email = in.nextLine();
		System.out.println(ticketService.findAndHoldSeats(numberOfSeats,
				minLevel == 0 ? Optional.empty() : Optional.of(minLevel),
				maxLevel == 0 ? Optional.empty() : Optional.of(maxLevel), email));
	}

	private static void reserveOption(TicketService ticketService, Scanner in) {
		System.out.println("\nEnter SeatHold Id :");
		int seatHoldId = in.nextInt();
		System.out.println("\nEnter email :");
		in.nextLine();
		String email = in.nextLine();
		System.out.println(ticketService.reserveSeats(seatHoldId, email));
	}

	private static void numSeatsOption(SimplePerformanceVenue venue, TicketService ticketService, Scanner in) {
		System.out.println("\nEnter Seating Level :");
		printLevelOptions(venue);
		int level = in.nextInt();

		System.out.println(ticketService.numSeatsAvailable(level == 0 ? Optional.empty() : Optional.of(level)));
	}

	private static void printLevelOptions(SimplePerformanceVenue venue) {
		for (Level.Type type : venue.getLevelTypes()) {
			System.out.println("Enter " + type.getId() + " for " + type.getLevelName());
		}
		System.out.println("Enter 0 for All/None");
	}
}
