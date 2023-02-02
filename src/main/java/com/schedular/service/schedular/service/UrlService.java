package com.schedular.service.schedular.service;

import com.google.common.hash.Hashing;
import com.schedular.service.schedular.model.Url;
import com.schedular.service.schedular.repository.master.MasterUrlRepository;
import com.schedular.service.schedular.repository.slave.SlaveUrlRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class UrlService {
    Logger logger = LoggerFactory.getLogger(UrlService.class);
    @Autowired
    private MasterUrlRepository masterUrlRepository;

    @Autowired
    private SlaveUrlRepository slaveUrlRepository;

    @Scheduled(cron = "0 0 7 * * *")
    public void schedulerJob() {
        logger.info("Scheduler Job started on " + LocalDate.now().toString());
        List<Url> listOfAllUrls = listAllUrls();
        for(Url url: listOfAllUrls) {
            LocalDate todayDate = LocalDate.now();
            LocalDate createdDate = url.getCreatedDate();
            long daysBetween = ChronoUnit.DAYS.between(createdDate, todayDate);
            if(daysBetween > 7) {
                logger.warn("Url deleting process started for url "+ url.getLongUrl());
                logger.warn("deleting this url because it got expired.");
                logger.warn("Created Date of the url is: " + url.getCreatedDate().toString());
                slaveUrlRepository.delete(url);
                masterUrlRepository.delete(url);
                logger.warn("Deleting is successful on all the dbs");
            }
        }
        logger.info("Schedular Job ended on" + LocalDate.now().toString());
    }

    public List listAllUrls() {
        return slaveUrlRepository.findAll();
    }

    private String encodeUrl(String url) {
        String encodedUrl = "";
        encodedUrl = Hashing.murmur3_32()
                .hashString(url, StandardCharsets.UTF_8)
                .toString();
        return encodedUrl;
    }
}
