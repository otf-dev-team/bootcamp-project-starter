package id.co.bni.otf.bootcamp.repository;

import id.co.bni.otf.bootcamp.entity.Stocks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface StocksRepository extends JpaRepository<Stocks, Long>, QuerydslPredicateExecutor<Stocks> {
    Stocks findOneByCode(String code);
}
