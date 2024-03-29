## Что реализовано:
1) API может создавать все типы QR. 

Запрос отправляется по адресу: api/v1/qrs/dynamic, api/v1/qrs/static, api/v1/qrs/variable

Локальный порт: 8081

Обязательные параметры у каждого из типов QR можно посмотреть в документации:
https://pay.raif.ru/doc/sbp.html#tag/qr-creation

Также обязательными параметрами у каждого из типов QR являются merchantId и secretKey

2) API может предоставлять информацию о QR

Запрос отправляется по адресу: api/v1/qrs/{qrId}

В теле запроса указываются merchandId и secretKey

3) Добавлена поддержка Swagger
4) Добавлена обработка исключений SbpClient
5) API взаимодействует с databaseAPI:
- загружает созданные QR
- обновляет информацию о QR каждые 5 секунд
6) API хранит в БД информацию (QrKey) о qrId, merchantId и secretKey каждого созданного QR. По завершению работы QR его QrKey удаляется из БД
7) Добавлена поддержка Liquibase и Preliquibase




