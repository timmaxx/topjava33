docker run -p 5432:5432 -d --name topjava_db -e POSTGRES_USER=user -e POSTGRES_PASSWORD=password -e POSTGRES_DB=topjava postgres

-p: преобразование портов HOST PORT:CONTAINER PORT (или -P: все порты)

-d: флаг запускает контейнер в фоновом режиме (как демон)

-e: задание переменной окружения

--name [имя]: устанавливает имя демона для нового контейнера


Основные команды Docker:

docker ps -a : посмотреть все контейнеры

docker stop topjava_db : остановить наш контейнер

docker start topjava_db : запустить его

docker rm topjava_db : удалить

docker help : справка по командам