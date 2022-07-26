# Отчет по итогам автоматизации

В соответствии с [планом тестирования](https://github.com/Romankurinov/Graduate-work/blob/main/docs/Plan.md) проведено автоматизированное тестирование всех запланированных позитивных и негативных сценариев для 2 вариантов взаимодействия с сервисом — покупка тура по карте и в кредит. Протестировано API сервиса для Payment Gate и Credit Gate. Подтверждена заявленная поддержка 2 СУБД - MySQL и PostgeSQL.


Автоматизация проведена успешно, все поставленные задачи реализованы. Все запланированные инструменты использовались.

В результате проведенного тестирования сформирован отчет по итогам тестирования - [Report.md](https://github.com/Romankurinov/Graduate-work/blob/main/docs/Report.md).

---
## Сработавшие риски
* Возникли сложности с нахождением CSS-селекторов;
* Трудности с одновременным использованием двух СУБД - MySql и Postgres, сложности в настройке симулятора банковских сервисов;
* Из-за отсутствия опыта автоматизации допущены ошибки при написании тестов, неверно была использована библиотека Faker, что привело к увеличению времени на написание автотестов;
* Необходимость реализации дополнительных сценариев, не предусмотренных изначальным планом;
* Отсутствие технического задания потребовало дополнительного времени на анализ приложения с точки зрения конечного пользователя и безопасности системы;
* Необходимость дополнительного анализа некоторых из используемых инструментов
---
## Общий итог затраченного времени

* На подготовку тестовой инфраструктуры было запланировано 16 часов, фактически ушло 24 часов;
* На написание автотестов, тестирование и отладку автотестов было запланировано 72 часа, фактически потрачено 94 часа (в рамках резерва на решение непредвиденных ситуаций);
* На создание отчетных документов по итогам автоматизированного тестирования и автоматизации в целом затрачено времени согласно плану
