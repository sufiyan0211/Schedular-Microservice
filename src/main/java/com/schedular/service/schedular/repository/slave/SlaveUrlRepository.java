package com.schedular.service.schedular.repository.slave;

import com.schedular.service.schedular.model.Url;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SlaveUrlRepository extends JpaRepository<Url, Long> {
    Url findByShortUrl(String shortUrl);
}
