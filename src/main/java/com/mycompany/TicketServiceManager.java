package com.mycompany;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;
import java.util.stream.Collectors;

import com.mycompany.model.ApplicationException;
import com.mycompany.model.HouseFullException;
import com.mycompany.model.Level;
import com.mycompany.model.Reservation;
import com.mycompany.model.Row;
import com.mycompany.model.Seat;
import com.mycompany.model.SeatHold;
import com.mycompany.model.SimplePerformanceVenue;
import com.mycompany.model.TimeOutException;

/**
 * @author Ravikanth Kunchala
 *
 */
public class TicketServiceManager implements TicketService {

	public static final long TIME_OUT_MILLIS = 10 * 1000;
	private final NavigableMap<Integer, List<Seat>> availableLevelMap = new ConcurrentSkipListMap<>();
	// Read write locks to manage availableLevelMap
	private final Map<Integer, ReadLock> availableReadLockMap;
	private final Map<Integer, WriteLock> availableWriteLockMap;

	private final ConcurrentMap<Integer, SeatHold> seatHoldMapById = new ConcurrentHashMap<>();
	private final ConcurrentMap<String, Reservation> reserved = new ConcurrentHashMap<>();
	// read write locks to manage seatHoldMapById reserved
	private final ReentrantReadWriteLock managerReentrantLock = new ReentrantReadWriteLock();
	private final ReadLock managerReadLock = managerReentrantLock.readLock();
	private final WriteLock managerWriteLock = managerReentrantLock.writeLock();

	private final AtomicInteger seatHoldIds = new AtomicInteger();
	private final AtomicInteger seatReserveIds = new AtomicInteger();

	public TicketServiceManager(SimplePerformanceVenue venue) {

		try {
			managerWriteLock.lock();
			ConcurrentMap<Integer, ReadLock> availableReadLockMapTemp = new ConcurrentHashMap<>();
			ConcurrentMap<Integer, WriteLock> availableWriteLockMapTemp = new ConcurrentHashMap<>();
			for (Level level : venue.getLevels()) {
				List<Seat> queue = new ArrayList<>();
				queue.addAll(level.getRows().stream().map(Row::getSeats).flatMap(l -> l.stream())
						.collect(Collectors.toList()));
				availableLevelMap.put(level.getType().getId(), queue);
				ReentrantReadWriteLock reentrantLock = new ReentrantReadWriteLock();
				availableReadLockMapTemp.put(level.getType().getId(), reentrantLock.readLock());
				availableWriteLockMapTemp.put(level.getType().getId(), reentrantLock.writeLock());
			}

			availableReadLockMap = Collections.unmodifiableMap(availableReadLockMapTemp);
			availableWriteLockMap = Collections.unmodifiableMap(availableWriteLockMapTemp);
		} finally {
			managerWriteLock.unlock();
		}
	}

	@Override
	public int numSeatsAvailable(Optional<Integer> venueLevel) {
		claimTimedOut();
		if (venueLevel.isPresent()) {
			ReadLock readLock = availableReadLockMap.get(venueLevel.get().intValue());
			try {
				readLock.lock();
				return availableLevelMap.get(venueLevel.get().intValue()).size();
			} finally {
				readLock.unlock();
			}
		} else {
			try {
				managerReadLock.lock();
				return availableLevelMap.values().stream().mapToInt(List::size).sum();
			} finally {
				managerReadLock.unlock();
			}
		}
	}

	@Override
	public SeatHold findAndHoldSeats(int numSeats, Optional<Integer> minLevel, Optional<Integer> maxLevel,
			String customerEmail) {

		int allocatedSeats = 0;
		int minLevelCheck = minLevel.orElse(Integer.MIN_VALUE).intValue();
		int maxLevelCheck = maxLevel.orElse(Integer.MAX_VALUE).intValue();

		Map<Integer, List<Seat>> seatsHeldByLevel = new ConcurrentHashMap<>();
		for (Entry<Integer, List<Seat>> entry : availableLevelMap.subMap(minLevelCheck, true, maxLevelCheck, true)
				.entrySet()) {
			WriteLock writeLock = availableWriteLockMap.get(entry.getKey());

			if (allocatedSeats < numSeats) {
				try {
					int moreRequired = numSeats - allocatedSeats;
					List<Seat> holdList = new ArrayList<>();

					writeLock.lock();
					List<Seat> availableList = entry.getValue();
					if (availableList.size() <= moreRequired) {
						holdList.addAll(availableList);
						allocatedSeats += availableList.size();
						availableList = new ArrayList<>();
					} else {
						holdList.addAll(availableList.subList(0, moreRequired));
						availableList = availableList.subList(moreRequired , availableList.size());
						allocatedSeats += moreRequired;
					}
					availableLevelMap.replace(entry.getKey(), availableList);
					seatsHeldByLevel.put(entry.getKey(), new ArrayList<Seat>(holdList));
				} finally {
					writeLock.unlock();
				}
			}
			if (allocatedSeats == numSeats)
				break;
		}

		SeatHold seatHold = new SeatHold(seatHoldIds.getAndIncrement(), customerEmail, seatsHeldByLevel, new Date().toInstant());
		if (allocatedSeats == numSeats) {
			seatHoldMapById.put(seatHold.getId(), seatHold);
			return seatHold;
		} else {
			claimSeatsHeld(seatHold);
			throw new HouseFullException("Number of Seats requested are not available");
		}
	}

	@Override
	public String reserveSeats(int seatHoldId, String customerEmail)
			throws ValidationExcption, ApplicationException, TimeOutException {

		if (customerEmail == null || "".equals(customerEmail)) {
			// may add regular expression check for emil
			throw new ValidationExcption("Invalid customer e-mail address");
		}
		try {
			SeatHold seatHold = seatHoldMapById.get(seatHoldId);
			if (seatHold == null) {
				throw new ValidationExcption("Invalid seatHoldId");
			}
			if (!customerEmail.equals(seatHold.getCustomerEmail())) {
				throw new ValidationExcption("Invalid customer e-mail address, Not maching the SeatHold");
			}
			try {
				managerWriteLock.lock();
				SeatHold seatHoldToReserve = seatHoldMapById.remove(seatHoldId);
				if (seatHoldToReserve != null) {
					Reservation reservation = new Reservation(seatReserveIds.incrementAndGet(), seatHoldToReserve,
							new Date().toInstant());
					reserved.put(customerEmail, reservation);
					return reservation.getId() + "";
				}
				throw new TimeOutException();
			} finally {
				managerWriteLock.unlock();
			}

		} catch (ValidationExcption | TimeOutException e) {
			throw e;
		}

		catch (Throwable t) {
			// TODO write to logger
			t.printStackTrace();
			throw new ApplicationException("Unknown Errors");
		}

	}

	private boolean isTimeOut(SeatHold seatHold) {
		return (System.currentTimeMillis() - seatHold.getHoldStartTime().toEpochMilli()) > TIME_OUT_MILLIS;
	}

	private void claimTimedOut() {
		seatHoldMapById.values().stream().filter(this::isTimeOut).forEach(this::claimSeatsHeld);

	}

	private void claimSeatsHeld(SeatHold seatHold) {

		Map<Integer, List<Seat>> map = seatHold.getSeatByLevelMap();

		for (Entry<Integer, List<Seat>> entry : map.entrySet()) {
			WriteLock writeLock = availableWriteLockMap.get(entry.getKey());
			try {
				writeLock.lock();
				availableLevelMap.get(entry.getKey()).addAll(entry.getValue());
			} finally {
				writeLock.unlock();
			}
		}

		System.out.println("claimSeatsHeldTimedOut");
	}
}