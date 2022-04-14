package com.postgres.datadiff;

import com.postgres.datadiff.model.StoreSaleDateData;
import com.postgres.datadiff.model.UploaderLock;
import com.postgres.datadiff.model.repo.StoreSaleDateRepository;
import com.postgres.datadiff.model.repo.UploaderLockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@SpringBootApplication
@RestController
public class DatadiffApplication implements CommandLineRunner {

    @Autowired
    StoreSaleDateRepository storeSaleDateRepository;

    @Autowired
    UploaderLockRepository uploaderLockRepository;

    public static void main(String[] args) {
        SpringApplication.run(DatadiffApplication.class, args);
    }


    @Override
    public void run(String... args) throws Exception {
        String startdate = "2020-01-01";
        LocalDate lcDate1 = LocalDate.parse(startdate);

        for (int i = 0; i < 100; i++) {
            storeSaleDateRepository.save(StoreSaleDateData.builder()
                    .performedBy(Thread.currentThread().getName())
                    .store("store" + (new Random().nextInt(20) + 1))
                    .saleDate(lcDate1.plusDays(new Random().nextInt(5)))
                    .timeStamp(Instant.now())
                    .build());
        }

    }


    @GetMapping("/run")
    public ResponseEntity<?> gen() {
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        for(int i=0;i<100;i++)
        executorService.submit(()->extracted());
        return ResponseEntity.ok("OK");
    }

    private void extracted() {
        List<String> stores1 = storeSaleDateRepository.getDistinctStore();
        List<String> stores2 = uploaderLockRepository.getDistinctStore();
        List<String> differences = stores1.stream()
                .filter(element -> !stores2.contains(element))
                .collect(Collectors.toList());
        for (String store : differences) {
            LocalDate seedate = LocalDate.parse("2020-01-01");
            do {
                uploaderLockRepository.save(UploaderLock.builder()
                        .store(store)
                        .status(UploaderLock.STATUS.INIT)
                        .saleDate(seedate)
                        .performedBy(Thread.currentThread().getName())
                        .build());
                seedate = seedate.plusDays(1);
            } while (seedate.isBefore(LocalDate.parse("2020-01-06")));
        }
    }
}
