package com.yicj.gateway.predicates;

import lombok.Data;
import org.springframework.cloud.gateway.handler.predicate.AbstractRoutePredicateFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

@Component
public class TimeBetweenRoutePredicateFactory extends
        AbstractRoutePredicateFactory<TimeBetweenRoutePredicateFactory.TimeBetweenConfig> {

    public TimeBetweenRoutePredicateFactory() {
        super(TimeBetweenConfig.class);
    }


    @Override
    public Predicate<ServerWebExchange> apply(TimeBetweenConfig config) {
        LocalTime start = config.getStart();
        LocalTime end = config.getEnd();
        return exchange -> {
            LocalTime now = LocalTime.now() ;
            return now.isAfter(start) && now.isBefore(end) ;
        } ;
    }

    //配置类-TimeBetweenConfig与配置文件的映射关系的配置
    // 返回配置类field的名称
    @Override
    public List<String> shortcutFieldOrder() {
        return Arrays.asList("start","end");
    }

    @Data
    static class TimeBetweenConfig{
        private LocalTime start ;
        private LocalTime end ;
    }

    public static void main(String[] args) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT);
        System.out.println(dateTimeFormatter.format(LocalTime.now()));
    }
}
