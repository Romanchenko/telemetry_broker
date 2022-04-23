# Экспорт метрик из Istio в ClickHouse

1. Кроном забираем метрики в соответствии с Prometheus-совместимым протоколом.
2. Загружаем батчами (todo) в ClickHouse
3. GGWP

## Структура таблиц в ClickHouse
```
CREATE DATABASE metrics


CREATE TABLE metrics.metrics (
    _id UUID,
    ts DateTime64(3, 'Europe/Moscow'),
    name String,
    value Decimal128(5),
    labels Array(String),
    le Nullable(String),
    quantile Nullable(String)
)
ENGINE = MergeTree()
ORDER BY (ts)
```

Пример получаемых из ручки /metrics данных находится в [файле](src/main/java/ru/cshse/project/sources/dummy_metrics.txt). 