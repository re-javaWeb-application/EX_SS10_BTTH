package com.re.btss10.repository;

import com.re.btss10.model.BorrowRequest;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class BorrowRequestRepository {

    private final List<BorrowRequest> requests = new ArrayList<>();
    private final AtomicLong sequence = new AtomicLong(1);

    public synchronized BorrowRequest save(BorrowRequest request) {
        request.setId(sequence.getAndIncrement());
        requests.add(request);
        return request;
    }

    public synchronized List<BorrowRequest> findAll() {
        return requests.stream()
                .sorted(Comparator.comparing(BorrowRequest::getCreatedAt).reversed())
                .toList();
    }

    public synchronized boolean hasCollision(String itemId, LocalDate receiveDate, LocalDate returnDate) {
        return requests.stream()
                .filter(request -> request.getItemId().equals(itemId))
                .anyMatch(request -> !returnDate.isBefore(request.getReceiveDate())
                        && !receiveDate.isAfter(request.getReturnDate()));
    }
}
