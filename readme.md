# Экспорт метрик из Istio в ClickHouse

1. Кроном забираем метрики в соответствии с Prometheus-совместимым протоколом.
2. Загружаем батчами (todo) в ClickHouse
3. GGWP

## Структура таблиц в ClickHouse
```
CREATE TABLE metrics.metrics (
    _id UUID,
    ts Date,
    name String,
		type String
    value Decimal64(5),
    labels Array(String)
)
ENGINE = MergeTree()
ORDER BY (ts)
```

Пример получаемых из ручки /metrics данных находится в [файле](src/main/java/ru/cshse/project/sources/dummy_metrics.txt). 